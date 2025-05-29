from flask import Flask, request, jsonify
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

df = pd.DataFrame()

movies = pd.DataFrame(
    {
        "title": ["The Matrix", "Inception", "The Terminator"],
        "description": [
            "A hacker discovers reality is a simulation",
            "A thief steals ideas through dreams",
            "A cyborg assassin travels through time",
        ],
    }
)

# tf-idf vector
vectorizer = TfidfVectorizer()
tfidf_matrix = vectorizer.fit_transform(movies["description"])

# similarity matrix
cosine_sim = cosine_similarity(tfidf_matrix)


def recommend(title, top_n=2):
    idx = movies.index[movies["title"] == title][0]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)[1 : top_n + 1]
    return [movies["title"][i] for i, _ in sim_scores]


# print(recommend("The Matrix"))


# @app.route("/movie-data", methods=["POST"])
# def receive_movie():
#     global df
#     movie = request.json
#     if movie:
#         df = pd.concat([df, pd.DataFrame([movie])], ignore_index=True)
#         print(f"Movie received: {movie.get('title')}")
#         return jsonify({"status": "received"}), 200
#     return jsonify({"error": "no data"}), 400


# @app.route("/rec/update", methods=["POST"])
# def update_movies():
#     global movies_df
#     data = request.json

#     if data["event"] == "add":
#         movies_df = pd.concat(
#             [movies_df, pd.DataFrame([data["movie"]])], ignore_index=True
#         )
#     elif data["event"] == "remove":
#         movies_df = movies_df[movies_df["id"] != data["movieId"]]

#     return {"status": "ok"}, 200


@app.route("/rec/update", methods=["POST"])
def update_movies():
    data = request.get_json()
    loved_df = pd.DataFrame(data)

    # Dummy recommender logic
    all_movies = pd.read_csv("all_movies.csv")
    recommendations = all_movies.sample(5)

    return recommendations.to_json(orient="records")


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8181)
