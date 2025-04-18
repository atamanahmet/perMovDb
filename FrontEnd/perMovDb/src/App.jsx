import { useState, useEffect } from "react";
// import reactLogo from "./assets/react.svg";
import "./App.css";
import axios from "axios";
import DiscoverPage from "./components/DiscoverPage";
import Header from "./components/Header";
import DetailsPage from "./components/DetailsPage";
import { Routes, Route } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";

function App() {
  const [result, setResult] = useState();
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [movieId, setMovieId] = useState(null);
  // const location = useLocation();
  // const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/")
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

  useEffect(() => {
    if (movieId != null) {
      axios
        .get("http://localhost:8080/user/qwe/watchlist/" + movieId)
        .catch((err) => console.error("Backend error:", err));
    }
  }, [movieId]);

  return (
    <>
      <Header />
      <Routes>
        <Route
          path="/"
          element={
            selectedMovie ? (
              <DetailsPage item={selectedMovie} />
            ) : (
              <DiscoverPage
                result={result}
                onCardClick={setSelectedMovie}
                onWatchListAdd={setMovieId}
              />
            )
          }
        />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </>
  );
}

export default App;
