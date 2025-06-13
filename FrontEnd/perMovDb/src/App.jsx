import { useState, useEffect, useRef } from "react";
import {
  Routes,
  Route,
  useNavigate,
  useLocation,
  useParams,
} from "react-router-dom";
import Search from "./components/Search";

import axios from "axios";

import "./App.css";
import { useUser } from "./context/UserContext";

import DiscoverPage from "./pages/DiscoverPage";

import Header from "./components/Header";
import Register from "./pages/Register";
import Login from "./pages/Login";
import ProfilePage from "./pages/ProfilePage";
import Upload from "./pages/Upload";
import NewReleases from "./pages/NewReleases";
import Top from "./components/Top";
import DetailsPage from "./pages/DetailsPage";

function App() {
  const { user, login, logOut, watchlist, searchResponse, mediaType } =
    useUser();
  const [result, setResult] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const [username, setUsername] = useState(null);

  const [responseName, setResponseName] = useState(null);

  const [movieId, setMovieId] = useState(null);
  const [actionType, setActionType] = useState(null);

  const [showHeader, setShowHeader] = useState(true);
  const lastScrollY = useRef(0);

  useEffect(() => {
    //scroll up header
    const handleScroll = () => {
      const currentY = window.scrollY;
      if (currentY > lastScrollY.current && currentY > 100) {
        setShowHeader(false); // scrolling down
      } else {
        setShowHeader(true); // scrolling up
      }
      lastScrollY.current = currentY;
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  // const fetchData = async () => {
  //   if (isFetching) return;
  //   setIsFetching(true);
  //   try {
  //     const url = `http://localhost:8080/${mediaType}?adult=${adult}&page=${currentPage}&sort=${
  //       filters.sort
  //     }&genreIdList=${filters.genres}&yearRange=${
  //       filters.yearRange
  //     }&ratingRange=${
  //       filters.rating
  //     }&languages=${filters.languages.toString()}`;

  //     // console.log(url);
  //     const res = await axios.get(url, { withCredentials: true });

  //     // console.log(res.data);
  //     // setResult(res.data);
  //     setResult((prev) => {
  //       if (!prev || currentPage == 1) return res.data;
  //       const existingIds = new Set(prev.map((item) => item.id));
  //       const filteredNewItems = res.data.filter(
  //         (item) => !existingIds.has(item.id)
  //       );
  //       return [...prev, ...filteredNewItems];
  //     });
  //   } catch (err) {
  //     console.error("Backend error:", err);
  //   } finally {
  //     setIsFetching(false);
  //   }
  // };

  //watchlist add and remove calls for api
  useEffect(() => {
    if (movieId && actionType) {
      axios
        .get(
          "http://localhost:8080/user/watchlist/" + movieId + "/" + actionType,
          { withCredentials: true }
        )
        .catch((err) => console.error("Backend error:", err));
    }
  }, [movieId, actionType, watchlist]);

  return (
    <>
      <Header showHeader={showHeader} />
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
        <Route path="/details" element={<DetailsPage />} />
        <Route path="/upload" element={<Upload />} />
      </Routes>
    </>
  );
}

export default App;
