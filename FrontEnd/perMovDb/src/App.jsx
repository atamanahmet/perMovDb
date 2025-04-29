import { useState, useEffect } from "react";
import { Routes, Route, useNavigate, useLocation } from "react-router-dom";
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

function App() {
  const { user, login, logOut } = useUser();
  const [result, setResult] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [username, setUsername] = useState(null);

  const [responseName, setResponseName] = useState(null);

  const [movieId, setMovieId] = useState(null);
  const [actionType, setActionType] = useState(null);

  //first api call for discovery page
  useEffect(() => {
    axios
      .get("http://localhost:8080/", { withCredentials: true })
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, [user]);

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
  }, [movieId, actionType]);

  return (
    <>
      <Header />
      <Routes>
        <Route
          path="/"
          element={
            <DiscoverPage
              result={result}
              // onCardClick={setSelectedMovie}
            />
          }
        />
        <Route path="/register" element={<Register />} />
        <Route path="/Login" element={<Login />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Routes>
    </>
  );
}

export default App;
