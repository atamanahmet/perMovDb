import { useState, useEffect } from "react";
import "./App.css";
import axios from "axios";
import DiscoverPage from "./components/DiscoverPage";
import Header from "./components/Header";
import DetailsPage from "./components/DetailsPage";
import { Routes, Route, useNavigate } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProfilePage from "./components/ProfilePage";

function App() {
  // For login and jwt token
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
  });

  //user state
  const [user, setUser] = useState(null);

  // const [response, setResponse] = useState();
  const [result, setResult] = useState();
  const [buttonText, setButtonText] = useState("Login");
  const [selectedMovie, setSelectedMovie] = useState(null);
  // const [movieId, setMovieId] = useState(null);
  // const [actionType, setActionType] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/me", { withCredentials: true })
      .then((res) => console.log("api/me" + res.data))

      .catch((err) => console.error("Backend error:", err));
    // console.log(user);
  }, []);

  function handleSubmit(formData) {
    login(formData);
  }

  
  const logOut = async () => {
    const response = await axios
      .get("http://localhost:8080/logout", { withCredentials: true })
      .catch((err) => console.error("Backend error:", err));
    console.log(response);
  };

  // const location = useLocation();

  // function handleWatchList(id, action) {
  //   setMovieId(id);
  //   setActionType(action);
  // }

  //first api call for discovery page
  useEffect(() => {
    axios
      .get("http://localhost:8080/", { withCredentials: true })
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

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

  function LogOutRoute({ logOut }) {
    const navigate = useNavigate();
    useEffect(() => {
      logOut();
      setButtonText("Login");
      navigate("/discover");
    }, []);
    return null;
  }

  return (
    <>
      <Header buttonText={buttonText} user={user} />
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
                onWatchListAdd={setSelectedMovie}
                user={user}
              />
            )
          }
        />
        <Route
          path="/discover"
          element={
            <DiscoverPage
              result={result}
              onCardClick={setSelectedMovie}
              onWatchListAdd={setSelectedMovie}
            />
          }
        />

        <Route path="/register" element={<Register />} />
        <Route path="/Login" element={<Login handleSubmit={handleSubmit} />} />
        <Route path="/LogOut" element={<LogOutRoute logOut={logOut} />} />
        <Route path="/profile" element={<ProfilePage user={user} />} />
      </Routes>
      {/* <button
        className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 trans top-buttons"
        onClick={testFunc}
      >
        Click
      </button> */}
    </>
  );
}

export default App;
