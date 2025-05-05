import { createContext, useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router";
import axios from "axios";

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  // const location = useLocation();
  const navigate = useNavigate();
  const [watchlist, setWatchlist] = useState(new Set());
  const [watchlistIds, setWatchlistIds] = useState(new Set());
  const [watchedlist, setWatchedlist] = useState(new Set());
  const [watchedlistIds, setWatchedlistIds] = useState(new Set());
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
        getWatchedList();
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
    }
  }, [user]);

  useEffect(() => {
    if (user) {
      getProfilePhoto();
    }
  }, []);

  useEffect(() => {
    if (user) {
      getWatchedList();
    }
  }, [user]);

  // const [file, setFile] = useState(null);
  // const [response, setResponse] = useState(null);

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
          const imageObjectUrl = URL.createObjectURL(res.data);
          setProfilePictureUrl(imageObjectUrl);

          const reader = new FileReader();
          reader.onloadend = () => {
            const base64data = reader.result;
            sessionStorage.setItem("profilePhoto", base64data);
          };
          reader.readAsDataURL(res.data);
        })
        .catch((err) => console.log(err));
    }
  }

  async function searchHandler(searchQuery) {
    if (searchQuery != null) {
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

  // const getProfilePictureUrl = async () => {
  //   if (user) {
  //     await axios
  //       .get("http://localhost:8080/user/profile-picture-url", {
  //         withCredentials: true,
  //       })
  //       .then((res) => {
  //         setProfilePictureUrl("http://localhost:8080/" + res.data);
  //       })
  //       .catch((err) => console.log(err));
  //   }
  // };
  const getWatchList = async () => {
    if (user) {
      await axios
        .get("http://localhost:8080/user/watchlist", {
          withCredentials: true,
        })
        .then((res) => {
          setWatchlist(new Set(res.data));
        })
        .catch((err) => console.log(err));
      await axios
        .get("http://localhost:8080/user/watchlistIdSet", {
          withCredentials: true,
        })
        .then((res) => {
          setWatchlistIds(new Set(res.data));
        })
        .catch((err) => console.log(err));
    }
  };

  const getWatchedList = async () => {
    if (user) {
      await axios
        .get("http://localhost:8080/user/watchedlist", {
          withCredentials: true,
        })
        .then((res) => {
          setWatchedlist(new Set(res.data));
        })
        .catch((err) => console.log(err));
      await axios
        .get("http://localhost:8080/user/watchedlistIdSet", {
          withCredentials: true,
        })
        .then((res) => {
          setWatchedlistIds(new Set(res.data));
        })
        .catch((err) => console.log(err));
    }
  };

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
          getWatchedList();
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
        getWatchList,
        getWatchedList,
        watchlistIds,
        watchedlistIds,
        searchHandler,
        searchResponse,
        handleUpload,
        profilePictureUrl,
        getProfilePhoto,
        storedPhoto,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
