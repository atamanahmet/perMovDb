import { useState, useEffect } from "react";
// import reactLogo from "./assets/react.svg";
import "./App.css";
import axios from "axios";
import DiscoverPage from "./components/DiscoverPage";
import Header from "./components/Header";
import DetailsPage from "./components/DetailsPage";
import { Routes, Route, useNavigate } from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProfilePage from "./components/ProfilePage";
import Cookies from "universal-cookie";

function App() {
  const cookies = new Cookies();

  // const getAuthHeaders = () => {
  //   // const token = cookies.get("jwt_token");
  //   // return token ? { Authorization: `Bearer ${token}` } : {};
  // };

  // For login and jwt token
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
  });

  const [user, setUser] = useState({
    username: cookies.get("user") || "",
    authenticated: false,
  });

  const [test, setTest] = useState(false);

  const [result, setResult] = useState();
  const [buttonText, setButtonText] = useState("Login");
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [movieId, setMovieId] = useState(null);
  const [actionType, setActionType] = useState(null);

  const navigate = useNavigate();

  function handleSubmit(formData) {
    login(formData);
  }

  const logOut = () => {
    setUser(null);
    cookies.remove("jwt_token");
    console.log("removed");
  };

  const login = async (formData) => {
    try {
      const response = await axios
        .post(
          "http://localhost:8080/login",
          {
            username: formData.username,
            password: formData.password,
          },
          { withCredentials: true }
        )
        .catch((err) => console.error("Backend error:", err));

      console.log(response.status);

      if (response.status == 200) {
        setUser({
          username: response.data,
          authenticated: true,
        });
        console.log("username : " + response.data);

        cookies.set("user", response.data);

        console.log("login successsfull. Redirecting");

        setButtonText("LogOut");
        // alert("Login successfull");
        setInterval(() => {}, 2000);
        navigate("/profile");
      }
    } catch (err) {
      console.log("Error :" + err);
    }
  };

  // const location = useLocation();

  function handleWatchList(id, action) {
    setMovieId(id);
    setActionType(action);
  }

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
          "http://localhost:8080/user/" +
            cookies.get("user") +
            "/watchlist/" +
            movieId +
            "/" +
            actionType,
          { withCredentials: true }
        )
        .catch((err) => console.error("Backend error:", err));
    }
  }, [movieId, actionType]);

  function LogOutRoute({ logOut }) {
    const navigate = useNavigate();
    useEffect(() => {
      logOut();
      setButtonText("Login");
      navigate("/discover");
    }, []);
    return null;
  }

  function testFunc() {
    setTest(true);
  }

  useEffect(() => {
    if (test) {
      axios
        .get("http://localhost:8080/test", { withCredentials: true })
        .then((res) => console.log(res.data))
        .catch((err) => console.error("Backend error:", err));
    } else {
      console.log("noauth");
    }
  }, [test]);

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
                onWatchListAdd={handleWatchList}
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
              onWatchListAdd={handleWatchList}
            />
          }
        />

        <Route path="/register" element={<Register />} />
        <Route path="/Login" element={<Login handleSubmit={handleSubmit} />} />
        <Route path="/LogOut" element={<LogOutRoute logOut={logOut} />} />
        <Route
          path="/profile"
          element={<ProfilePage user={cookies.get("user")} />}
        />
      </Routes>
      <button
        className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 trans top-buttons"
        onClick={testFunc}
      >
        Click
      </button>
    </>
  );
}

export default App;
