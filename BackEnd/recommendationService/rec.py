from flask import Flask, request, jsonify
from scipy.sparse import csr_matrix
from implicit.als import AlternatingLeastSquares
import numpy as np

app = Flask(__name__)


@app.route("/recommend", methods=["POST"])
def recommend():
    data = request.json
    all_movies = data["all_movies"]  # list of all movie IDs
    loved_movies = data["loved_movies"]  # list of movie IDs user loved

    num_movies = len(all_movies)
    movie_id_to_idx = {mid: i for i, mid in enumerate(all_movies)}

    # Build interaction vector for single user
    rows = np.zeros(len(loved_movies))
    cols = np.array([movie_id_to_idx[mid] for mid in loved_movies])
    data_vals = np.ones(len(loved_movies))
    user_item = csr_matrix((data_vals, (rows, cols)), shape=(1, num_movies))

    # Train ALS on item-user matrix (transpose)
    model = AlternatingLeastSquares(factors=10, regularization=0.1, iterations=20)
    model.fit(user_item.T)

    # Recommend top 5 movies excluding already loved
    recommendations = model.recommend(
        0, user_item, N=5, filter_already_liked_items=True
    )

    # Map back indices to movie IDs
    recommended_movie_ids = [all_movies[i] for i, score in recommendations]

    return jsonify(recommended_movie_ids)


if __name__ == "__main__":
    app.run(port=8181)


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
