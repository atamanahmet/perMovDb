import { Star, Calendar, Users, Globe, Play } from "lucide-react";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router";
import { useState } from "react";
import axios from "axios";

function DetailsPage() {
  const { user, cast, detail } = useUser();

  const [trailer, setTrailer] = useState();

  const navigate = useNavigate();

  if (detail == null) {
    navigate("/discover");
  }
  if (detail.trailer_path == null) {
    axios
      .get("http://localhost:8080/movie/" + detail.id + "/video", {
        withCredentials: true,
      })
      .then((res) => {
        setTrailer(res.data);
      })
      .catch((err) => {
        console.log("Error: " + err);
      });
  }

  console.log(detail);

  const genreMap = {
    18: "Drama",
    53: "Thriller",
    35: "Comedy",
    28: "Action",
    12: "Adventure",
    16: "Animation",
    80: "Crime",
    99: "Documentary",
    10751: "Family",
    14: "Fantasy",
    36: "History",
    27: "Horror",
    10402: "Music",
    9648: "Mystery",
    10749: "Romance",
    878: "Science Fiction",
    10770: "TV Movie",
    53: "Thriller",
    10752: "War",
    37: "Western",
  };

  const getGenreNames = (genreIds) => {
    return genreIds.map((id) => genreMap[id] || "Unknown").join(", ");
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  const getRatingColor = (rating) => {
    if (rating >= 8) return "text-green-400";
    if (rating >= 6) return "text-yellow-400";
    return "text-red-400";
  };

  return (
    <>
      {detail && (
        <div className="min-h-screen bg-amber-950 text-white ">
          {/* Backdrop */}
          <div className="relative h-96 md:h-[500px] overflow-hidden ">
            <img
              src={detail.backdrop_path}
              alt={detail.title}
              className="w-full h-full object-cover"
            />
            <div className="absolute inset-0 bg-gradient-to-t from-amber-950 via-amber-950/60 to-transparent" />
          </div>

          {/* Main Content */}
          <div className="relative -mt-32 md:-mt-117 px-4 md:px-8 max-w-7xl mx-auto">
            <div className="flex flex-col md:flex-row gap-8">
              {/* Poster */}
              <div className="flex-shrink-0">
                <img
                  src={detail.poster_path}
                  alt={detail.title}
                  className="w-64 md:w-80 rounded-lg shadow-2xl mx-auto md:mx-0"
                />
              </div>

              {/* detail Info */}
              <div className="flex-1 pt-8 md:pt-16 -mt-16">
                <div className="mb-4">
                  <h1 className="text-4xl md:text-6xl font-bold mb-2">
                    {detail.title}
                  </h1>
                  {detail.original_title !== detail.title && (
                    <p className="text-xl text-gray-400 italic">
                      {detail.original_title}
                    </p>
                  )}
                </div>

                {/* Rating and Meta Info */}
                <div className="flex flex-wrap details-center gap-6 mb-6">
                  <div className="flex details-center gap-2">
                    <Star className="w-5 h-5 text-yellow-400 fill-current" />
                    <span
                      className={`text-lg font-semibold ${getRatingColor(
                        detail.vote_average
                      )}`}
                    >
                      {detail.vote_average.toFixed(1)}
                    </span>
                    <span className="text-gray-400">
                      ({detail.vote_count.toLocaleString()} votes)
                    </span>
                  </div>

                  <div className="flex details-center gap-2">
                    <Calendar className="w-5 h-5 text-blue-400" />
                    <span>{formatDate(detail.release_date)}</span>
                  </div>

                  <div className="flex details-center gap-2">
                    <Globe className="w-5 h-5 text-green-400" />
                    <span className="uppercase">
                      {detail.original_language}
                    </span>
                  </div>

                  {detail.adult && (
                    <div className="bg-red-600 px-2 py-1 rounded text-sm font-semibold">
                      18+
                    </div>
                  )}
                </div>

                {/* Genres */}
                <div className="mb-6">
                  <div className="flex flex-wrap gap-2">
                    {detail.genre_ids.map((genreId) => (
                      <span
                        key={genreId}
                        className="bg-gray-700 hover:bg-gray-600 transition-colors px-3 py-1 rounded-full text-sm"
                      >
                        {genreMap[genreId] || "Unknown"}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Overview */}
                <div className="mb-8">
                  <h2 className="text-2xl font-semibold mb-4">Overview</h2>
                  <p className="text-gray-300 leading-relaxed text-lg max-w-4xl">
                    {detail.overview}
                  </p>
                </div>

                {/* Action Buttons */}
                <div className="flex flex-wrap gap-4">
                  <a href={trailer}>
                    <button className="bg-red-600 hover:bg-red-700 transition-colors px-6 py-3 rounded-lg flex details-center gap-2 font-semibold">
                      <Play className="w-5 h-5" />
                      Watch Trailer
                    </button>
                  </a>
                  <button className="bg-gray-700 hover:bg-gray-600 transition-colors px-6 py-3 rounded-lg font-semibold">
                    Add to Watchlist
                  </button>
                </div>

                {/* Additional Stats */}
                <div className="mt-8 grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="bg-gray-800 p-4 rounded-lg text-center">
                    <div className="text-2xl font-bold text-blue-400">
                      {detail.popularity.toFixed(0)}
                    </div>
                    <div className="text-sm text-gray-400">Popularity</div>
                  </div>
                  <div className="bg-gray-800 p-4 rounded-lg text-center">
                    <div className="text-2xl font-bold text-green-400">
                      {detail.vote_count.toLocaleString()}
                    </div>
                    <div className="text-sm text-gray-400">Reviews</div>
                  </div>
                  <div className="bg-gray-800 p-4 rounded-lg text-center">
                    <div className="text-2xl font-bold text-purple-400">
                      #{detail.id}
                    </div>
                    <div className="text-sm text-gray-400">detail ID</div>
                  </div>
                  <div className="bg-gray-800 p-4 rounded-lg text-center">
                    <div className="text-2xl font-bold text-yellow-400">
                      {detail.video ? "Yes" : "No"}
                    </div>
                    <div className="text-sm text-gray-400">Has Video</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default DetailsPage;
