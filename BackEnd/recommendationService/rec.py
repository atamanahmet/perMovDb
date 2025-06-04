from flask import Flask, jsonify, request
import requests
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
from sklearn.preprocessing import MultiLabelBinarizer

app = Flask(__name__)

model = SentenceTransformer("all-MiniLM-L6-v2")
mlb = MultiLabelBinarizer()

WEIGHT_OVERVIEW = 0.5
WEIGHT_TITLE = 0.2
WEIGHT_GENRE = 0.3

SIMILARITY_THRESHOLD = 0.4


def normalize(vecs):
    """Normalize vectors row-wise to unit length."""
    norms = np.linalg.norm(vecs, axis=1, keepdims=True)
    norms[norms == 0] = 1
    return vecs / norms


def prepare_features(movies, fit_mlb=True):
    """Create combined embedding vectors for overview, title, and genres."""
    overviews = [m.get("overview", "") or "" for m in movies]
    titles = [m.get("title", "") or "" for m in movies]
    genres = [m.get("genre_ids", []) for m in movies]

    emb_overview = model.encode(overviews, convert_to_numpy=True)
    emb_title = model.encode(titles, convert_to_numpy=True)

    if fit_mlb:
        genre_enc = mlb.fit_transform(genres)
    else:
        genre_enc = mlb.transform(genres)

    emb_overview_norm = normalize(emb_overview)
    emb_title_norm = normalize(emb_title)
    genre_norm = normalize(genre_enc.astype(float))

    combined = np.hstack(
        [
            emb_overview_norm * WEIGHT_OVERVIEW,
            emb_title_norm * WEIGHT_TITLE,
            genre_norm * WEIGHT_GENRE,
        ]
    )

    return combined


@app.route("/rec/update", methods=["POST"])
def rec_update():
    movie_db_response = requests.get("http://localhost:8080/api/movies")
    movie_db = movie_db_response.json()

    combined_all = prepare_features(movie_db, fit_mlb=True)

    loved_movies = request.get_json()
    if not loved_movies:
        return jsonify([])

    combined_loved = prepare_features(loved_movies, fit_mlb=False)
    if combined_loved.shape[0] == 0:
        return jsonify([])

    user_vec = np.mean(combined_loved, axis=0).reshape(1, -1)

    similarities = cosine_similarity(user_vec, combined_all)[0]

    loved_ids_set = set(m.get("id") for m in loved_movies if "id" in m)

    recommendations = [
        (movie_db[i], sim)
        for i, sim in enumerate(similarities)
        if movie_db[i]["id"] not in loved_ids_set and sim >= SIMILARITY_THRESHOLD
    ]

    if not recommendations:
        return jsonify([])

    popularity_scores = np.array(
        [movie.get("popularity", 0) for movie, _ in recommendations]
    )
    if popularity_scores.max() - popularity_scores.min() > 1e-8:
        pop_norm = (popularity_scores - popularity_scores.min()) / (
            popularity_scores.max() - popularity_scores.min()
        )
    else:
        pop_norm = np.zeros_like(popularity_scores)

    sim_scores = np.array([sim for _, sim in recommendations])

    loved_genres = set(
        gid for movie in loved_movies for gid in movie.get("genre_ids", [])
    )

    def genre_overlap(movie):
        movie_genres = set(movie.get("genre_ids", []))
        return len(loved_genres.intersection(movie_genres))

    genre_overlaps = np.array([genre_overlap(movie) for movie, _ in recommendations])

    overviewFactor = 0.8
    popularityFactor = 0.15
    genreFactor = 0.05

    final_scores = (
        overviewFactor * sim_scores
        + popularityFactor * pop_norm
        + genreFactor * genre_overlaps
    )

    sorted_idx = np.argsort(final_scores)[::-1]
    top_recs = [recommendations[i][0] for i in sorted_idx[:50]]

    return jsonify(top_recs)


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8181, debug=True)


# from flask import Flask, jsonify, request
# import requests
# from sentence_transformers import SentenceTransformer
# from sklearn.metrics.pairwise import cosine_similarity
# import numpy as np

# app = Flask(__name__)

# model = SentenceTransformer("all-MiniLM-L6-v2")


# @app.route("/rec/update", methods=["POST"])
# def rec_update():
#     movie_db_response = requests.get("http://localhost:8080/api/movies")
#     movie_db = movie_db_response.json()

#     movie_texts = [m["overview"] for m in movie_db]
#     movie_embeddings = model.encode(movie_texts, convert_to_numpy=True)
#     loved_movies = request.get_json()
#     if not loved_movies:
#         return jsonify([])

#     for m in loved_movies:
#         print(m)

#     loved_texts = [m.get("overview", "") for m in loved_movies]

#     if not loved_texts:
#         return jsonify([])

#     loved_embeddings = model.encode(loved_texts, convert_to_numpy=True)
#     user_vec = np.mean(loved_embeddings, axis=0).reshape(1, -1)

#     similarities = cosine_similarity(user_vec, movie_embeddings)[0]

#     loved_ids_set = set(m["id"] for m in loved_movies if "id" in m)

#     recommendations = [
#         (movie_db[i], sim)
#         for i, sim in enumerate(similarities)
#         if movie_db[i]["id"] not in loved_ids_set
#     ]
#     recommendations.sort(key=lambda x: x[1], reverse=True)

#     top_similar = recommendations[:100]

#     top_similar.sort(key=lambda x: x[0].get("popularity", 0), reverse=True)

#     top_recs = [rec[0] for rec in top_similar[:50]]

#     return jsonify(top_recs)


# if __name__ == "__main__":
#     app.run(host="127.0.0.1", port=8181, debug=True)


## cosine similarity
##############
# from flask import Flask, jsonify, request
# import requests
# import pandas as pd
# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.metrics.pairwise import cosine_similarity
# from sklearn.preprocessing import MultiLabelBinarizer
# from scipy.sparse import hstack

# app = Flask(__name__)

# mlb = MultiLabelBinarizer()


# @app.route("/rec/update", methods=["POST"])
# def update_movies():
#     loved_json = request.get_json()
#     loved_df = pd.DataFrame(loved_json)

#     all_movies_resp = requests.get("http://localhost:8080/api/movies")
#     all_movies = pd.DataFrame(all_movies_resp.json())

#     vectorizer = TfidfVectorizer(stop_words="english")
#     tfidf_all = vectorizer.fit_transform(all_movies["overview"].fillna(""))
#     tfidf_loved = vectorizer.transform(loved_df["overview"].fillna(""))

#     mlb.fit(all_movies["genre_ids"])
#     genre_all = mlb.transform(all_movies["genre_ids"])
#     genre_loved = mlb.transform(loved_df["genre_ids"])

#     features_all = hstack([tfidf_all, genre_all])
#     features_loved = hstack([tfidf_loved, genre_loved])

#     sim_scores = cosine_similarity(features_loved, features_all)
#     avg_scores = sim_scores.mean(axis=0)

#     all_movies["score"] = avg_scores
#     recommended = (
#         all_movies[~all_movies["id"].isin(loved_df["id"])]
#         .sort_values(by="score", ascending=False)
#         .head(30)
#     )

#     return recommended.to_json(orient="records")


# if __name__ == "__main__":
#     app.run(host="127.0.0.1", port=8181, debug=True)
