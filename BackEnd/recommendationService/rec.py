from flask import Flask, jsonify, request
import requests
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np

app = Flask(__name__)

model = SentenceTransformer("all-MiniLM-L6-v2")


@app.route("/rec/update", methods=["POST"])
def rec_update():
    movie_db_response = requests.get("http://localhost:8080/api/movies")
    movie_db = movie_db_response.json()

    movie_texts = [m["overview"] for m in movie_db]
    movie_embeddings = model.encode(movie_texts, convert_to_numpy=True)
    loved_movies = request.get_json()
    if not loved_movies:
        return jsonify([])

    # Extract titles from loved movies
    loved_texts = [m.get("overview", "") for m in loved_movies]

    if not loved_texts:
        return jsonify([])

    # Compute embedding for loved movies average
    loved_embeddings = model.encode(loved_texts, convert_to_numpy=True)
    user_vec = np.mean(loved_embeddings, axis=0).reshape(1, -1)

    # Compute cosine similarity with all movies
    similarities = cosine_similarity(user_vec, movie_embeddings)[0]

    # Rank and exclude loved movies (by title match)
    loved_ids_set = set(m["id"] for m in loved_movies if "id" in m)

    recommendations = [
        (movie_db[i], sim)
        for i, sim in enumerate(similarities)
        if movie_db[i]["id"] not in loved_ids_set
    ]
    recommendations.sort(key=lambda x: x[1], reverse=True)

    # Return top 3 recommendations
    top_recs = [rec[0] for rec in recommendations[:50]]

    return jsonify(top_recs)


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8181, debug=True)


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
