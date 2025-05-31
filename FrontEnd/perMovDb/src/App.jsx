import { useState, useEffect } from "react";
import {
  Routes,
  Route,
  useNavigate,
  useLocation,
  useParams,
} from "react-router-dom";
import Search from "./components/Search";
// import { useUser } from "./context/UserContext";
// import { useLocation } from "react-router";

import axios from "axios";

import "./App.css";
import { useUser } from "./context/UserContext";

import DiscoverPage from "./components/DiscoverPage";
import Header from "./components/Header";
// import DetailsPage from "./components/DetailsPage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProfilePage from "./components/ProfilePage";
import Upload from "./components/Upload";
import NewReleases from "./components/NewReleases";
import Top from "./components/Top";

function App() {
  const { user, login, logOut, watchlist, searchResponse } = useUser();
  const [result, setResult] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [username, setUsername] = useState(null);

  const [responseName, setResponseName] = useState(null);

  const [movieId, setMovieId] = useState(null);
  const [actionType, setActionType] = useState(null);

  //For external redirect
  const RedirectToExternal = () => {
    const { movieId } = useParams();
    window.location.href = `https://www.themoviedb.org/movie/${movieId}`;
    return null;
  };

  //first api call for discovery page
  useEffect(() => {
    axios
      .get("http://localhost:8080/", { withCredentials: true })
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

  //watchlist add and remove calls for api
  useEffect(() => {
    if (movieId && actionType) {
      axios
        .get(
          "http://localhost:8080/user/watchlist/" + +movieId + "/" + actionType,
          { withCredentials: true }
        )
        .catch((err) => console.error("Backend error:", err));
    }
  }, [movieId, actionType, watchlist]);

  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<DiscoverPage result={result} />} />
        <Route path="/discover" element={<DiscoverPage result={result} />} />
        <Route path="/top" element={<Top />} />
        <Route path="/new" element={<NewReleases />} />
        <Route path="/register" element={<Register />} />
        <Route path="/Login" element={<Login />} />
        <Route path="/profile" element={<ProfilePage />} />
        {searchResponse != null ? (
          <Route
            path="/search"
            element={<Search data={searchResponse.data} />}
          />
        ) : null}
        <Route path="/details/:movieId" element={<RedirectToExternal />} />
        <Route path="/upload" element={<Upload />} />
      </Routes>
    </>
  );
}

export default App;
