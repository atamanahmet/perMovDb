import { createContext, useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router";
import axios from "axios";

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  // const location = useLocation();
  const navigate = useNavigate();
  const [watchlist, setWatchlist] = useState(new Set());
  const [watchlistIdSet, setWatchlistIdSet] = useState(new Set());
  const [watchedlistIdSet, setWatchedlistIdSet] = useState(new Set());
  const [watchedlist, setWatchedlist] = useState(new Set());
  const [lovedlist, setLovedlist] = useState(new Set());
  const [recommendation, setRecommendation] = useState();
  const [lovedlistIdSet, setLovedlistIdSet] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [profilePictureUrl, setProfilePictureUrl] = useState(null); // For creating local url for response blob
  const [user, setUser] = useState(null);
  const [userPhoto, setUserPhoto] = useState(null);
  const [logoutResult, setLogoutResult] = useState(null);
  const [searchResponse, setSearchResponse] = useState(null);

  const storedPhoto = sessionStorage.getItem("profilePhoto");

  const fetchUser = () => {
    setLoading(true);
    axios
      .get("http://localhost:8080/api/me", { withCredentials: true })
      .then((res) => {
        console.log("Token verification, username: " + res.data);
        login(res.data);
        getProfilePhoto();
        getWatchList();
      })
      .catch((err) => {
        console.log("Error: " + err);
        setUser(null);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchUser();
  }, [location.pathname]);

  useEffect(() => {
    if (user) {
      getWatchList();
      getProfilePhoto();
      // getWatchedList();
      // getLovedList();
    }
  }, [user]);

  async function handleUpload(file) {
    console.log(file);
    if (file != null) {
      console.log("file not null");
      const formData = new FormData();
      formData.append("profilePicture", file);

      const response = await axios
        .post("http://localhost:8080/user/upload", formData, {
          headers: {
            "content-type": "multipart/form-data",
          },
          withCredentials: true,
        })
        .catch((err) => console.log("Error: " + err));
      getProfilePhoto();
    }
  }

  async function getProfilePhoto() {
    if (user) {
      await axios
        .get(`http://localhost:8080/user/photo`, {
          responseType: "blob",
          withCredentials: true,
        })
        .then((res) => {
          if (!res.data) {
            const imageObjectUrl = URL.createObjectURL(res.data);
            setProfilePictureUrl(imageObjectUrl);

            const reader = new FileReader();
            reader.onloadend = () => {
              const base64data = reader.result;
              sessionStorage.setItem("profilePhoto", base64data);
            };
            reader.readAsDataURL(res.data);
          }
        })
        .catch((err) => console.log(err));
    }
  }

  async function searchHandler(searchQuery) {
    if (searchQuery != null) {
      searchQuery = searchQuery.replace(" ", "+");
      await axios
        .get("http://localhost:8080/search/" + searchQuery, {
          withCredentials: true,
        })
        .then((response) => {
          setSearchResponse(response);
          navigate("/search");
        })
        .catch((err) => console.log(err));

      if (searchResponse.status == 200) {
        navigate("/search");
      }
    }
  }

  const getWatchList = async () => {
    if (user) {
      await axios
        .get("http://localhost:8080/user/lists", {
          withCredentials: true,
        })
        .then((res) => {
          setWatchlist(new Set(res.data.watchlist));
          setWatchedlist(new Set(res.data.watchedlist));
          setLovedlist(new Set(res.data.lovedlist));
          setRecommendation(new Set(res.data.lovedlist));
          setWatchlistIdSet(new Set(res.data.watchlistIdSet));
          setWatchedlistIdSet(new Set(res.data.watchedlistIdSet));
          setLovedlistIdSet(new Set(res.data.lovedlistIdSet));
        })
        .catch((err) => console.log(err));
    }
  };
  const getRecommendation = async () => {
    setRecommendation(null);
    if (user) {
      await axios
        .get("http://localhost:8080/user/recommendation", {
          withCredentials: true,
        })
        .then((res) => {
          setRecommendation(new Set(res.data));
        })
        .catch((err) => console.log(err));
    }
  };
  // const getLovedList = async () => {
  //   if (user) {
  //     await axios
  //       .get("http://localhost:8080/user/lovedlist", {
  //         withCredentials: true,
  //       })
  //       .then((res) => {
  //         // console.log("lovelist returned: " + res.data);
  //         setLovedlist(new Set(res.data));
  //       })
  //       .catch((err) => console.log(err));
  //     await axios
  //       .get("http://localhost:8080/user/lovedlistIdSet", {
  //         withCredentials: true,
  //       })
  //       .then((res) => {
  //         setLovedlistIds(new Set(res.data));
  //       })
  //       .catch((err) => console.log(err));
  //   }
  // };

  // const getWatchedList = async () => {
  //   if (user) {
  //     await axios
  //       .get("http://localhost:8080/user/watchedlist", {
  //         withCredentials: true,
  //       })
  //       .then((res) => {
  //         setWatchedlist(new Set(res.data));
  //       })
  //       .catch((err) => console.log(err));
  //     await axios
  //       .get("http://localhost:8080/user/watchedlistIdSet", {
  //         withCredentials: true,
  //       })
  //       .then((res) => {
  //         setWatchedlistIds(new Set(res.data));
  //       })
  //       .catch((err) => console.log(err));
  //   }
  // };

  const login = (username) => {
    setUser(username);
  };

  const logOut = async () => {
    console.log("Log out requested");
    const url = "http://localhost:8080/logout";
    try {
      await axios
        .post(
          url,
          {},
          {
            withCredentials: true,
          }
        )
        .then((res) => setLogoutResult(res.data));

      console.log("Logged out");
      sessionStorage.clear();
      setUser(null);
    } catch (err) {
      console.log("Error during logout: ", err);
    }
  };

  const handleWatchList = async (movieId, actionType) => {
    console.log("watchlist context called");

    if (movieId && actionType) {
      try {
        const res = await axios.get(
          `http://localhost:8080/user/watchlist/${movieId}/${actionType}`,
          { withCredentials: true }
        );

        if (res.status == 200) {
          const updatedSet = new Set(watchlist);

          if (actionType == "del") {
            updatedSet.delete(movieId);
          } else {
            updatedSet.add(movieId);
          }

          setWatchlist(updatedSet);
          getWatchList();
        }
      } catch (err) {
        console.error("Backend error:", err);
      }
    }
  };
  const handleLovedList = async (movieId, actionType) => {
    console.log("watchlist context called");

    if (movieId && actionType) {
      try {
        const res = await axios.get(
          `http://localhost:8080/user/lovedlist/${movieId}/${actionType}`,
          { withCredentials: true }
        );

        if (res.status == 200) {
          const updatedSet = new Set(lovedlist);

          if (actionType == "del") {
            updatedSet.delete(movieId);
          } else {
            updatedSet.add(movieId);
          }

          setLovedlist(updatedSet);
          getWatchList();
        }
      } catch (err) {
        console.error("Backend error:", err);
      }
    }
  };
  const handleWatchedList = async (movieId, actionType) => {
    console.log("watchedlist context called");

    if (movieId && actionType) {
      try {
        const res = await axios.get(
          `http://localhost:8080/user/watchedlist/${movieId}/${actionType}`,
          { withCredentials: true }
        );

        if (res.status == 200) {
          const updatedSet = new Set(watchedlist);

          if (actionType == "del") {
            updatedSet.delete(movieId);
          } else {
            updatedSet.add(movieId);
          }

          setWatchedlist(updatedSet);
          getWatchList();
        }
      } catch (err) {
        console.error("Backend error:", err);
      }
    }
  };

  return (
    <UserContext.Provider
      value={{
        user,
        login,
        loading,
        logOut,
        handleWatchList,
        handleWatchedList,
        watchlist,
        watchedlist,
        lovedlist,
        lovedlistIdSet,
        getWatchList,
        watchlistIdSet,
        watchedlistIdSet,
        searchHandler,
        getRecommendation,
        searchResponse,
        handleUpload,
        profilePictureUrl,
        getProfilePhoto,
        storedPhoto,
        handleLovedList,
        recommendation,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
