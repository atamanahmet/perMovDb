import { useState, useEffect } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";
import { useUser } from "./context/UserContext";

import axios from "axios";

import "./App.css";

import DiscoverPage from "./components/DiscoverPage";
import Header from "./components/Header";
import DetailsPage from "./components/DetailsPage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProfilePage from "./components/ProfilePage";

function App() {
  const { user, login } = useUser();
  // const [response, setResponse] = useState();
  const [result, setResult] = useState();
  const [isLoading, setIsLoading] = useState(true);

  // const [movieId, setMovieId] = useState(null);
  // const [actionType, setActionType] = useState(null);

  // const navigate = useNavigate();
  // useEffect(() => {
  //   const fetchUser = async () => {
  //     try {
  //       const res = await axios.get("http://localhost:8080/api/me", {
  //         withCredentials: true,
  //       });
  //       login(res.data); // Assuming login sets the user data in context
  //     } catch (err) {
  //       console.error("Backend error:", err);
  //     } finally {
  //       setLoading(false); // Once the request finishes, stop loading
  //     }
  //   };

  //   fetchUser();
  // }, [login]);
  console.log("user: " + user);
  useEffect(() => {
    const fetchUser = async () => {
      const res = await axios
        .get("http://localhost:8080/api/me", { withCredentials: true })
        .catch((err) => console.error("User not exist."));
      if (res == null) {
        console.log("res null no user");
      } else {
        login(res.data);
      }

      axios
        .get("http://localhost:8080/", { withCredentials: true })
        .then((res) => setResult(res.data))
        .catch((err) => console.error("Backend error:", err));
    };
    fetchUser();
  }, []);
  console.log("user: " + user);

  //first api call for discovery page
  // useEffect(() => {
  //   axios
  //     .get("http://localhost:8080/", { withCredentials: true })
  //     .then((res) => setResult(res.data))
  //     .catch((err) => console.error("Backend error:", err));
  // }, []);

  //watchlist add and remove calls for api
  // useEffect(() => {
  //   if (movieId && actionType) {
  //     axios
  //       .get(
  //         "http://localhost:8080/user/" +
  //           cookies.get("user") +
  //           "/watchlist/" +
  //           movieId +
  //           "/" +
  //           actionType,
  //         { withCredentials: true }
  //       )
  //       .catch((err) => console.error("Backend error:", err));
  //   }
  // }, [movieId, actionType]);

  return (
    <>
      <Header />
      <Routes>
        {/* <Route
          path="/"
          element={
            isLoading ? (
             
            ) : (
              <DiscoverPage
                result={result}
                // onCardClick={setSelectedMovie}
                // onWatchListAdd={setSelectedMovie}
                // user={user}
              />
            )
          }
        /> */}
        <Route
          path="/"
          element={
            <DiscoverPage
              result={result}
              // onCardClick={setSelectedMovie}
              // onWatchListAdd={setSelectedMovie}
            />
          }
        />

        <Route path="/register" element={<Register />} />
        <Route path="/Login" element={<Login />} />
        <Route path="/profile" element={<ProfilePage user={user} />} />
      </Routes>
    </>
  );
}

export default App;
