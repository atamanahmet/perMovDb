from flask import Flask, jsonify, request
import requests
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


@app.route("/rec/update", methods=["POST"])
def update_movies():
    loved_json = request.get_json()
    print("Received loved list JSON:", loved_json)  # Debug log

    loved_df = pd.DataFrame(loved_json)
    print("Loved movies DataFrame:", loved_df.head())

    all_movies_resp = requests.get("http://localhost:8080/api/movies")
    print("Movies API status code:", all_movies_resp.status_code)
    print(
        "Movies API response text:", all_movies_resp.text[:500]
    )  # print first 500 chars for safety

    all_movies = pd.DataFrame(all_movies_resp.json())
    print("All movies DataFrame:", all_movies.head())

    vectorizer = TfidfVectorizer(stop_words="english")
    tfidf_all = vectorizer.fit_transform(all_movies["overview"].fillna(""))
    tfidf_loved = vectorizer.transform(loved_df["overview"].fillna(""))

    sim_scores = cosine_similarity(tfidf_loved, tfidf_all)
    avg_scores = sim_scores.mean(axis=0)

    all_movies["score"] = avg_scores

    recommended = (
        all_movies[~all_movies["id"].isin(loved_df["id"])]
        .sort_values(by="score", ascending=False)
        .head(10)
    )

    return recommended.to_json(orient="records")


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8181, debug=True)
