import { createContext, useEffect, useState, useContext } from "react";
// import { useLocation } from "react-router";
import axios from "axios";

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  // const location = useLocation();
  // const navigate = useNavigate();
  const [watchlist, setWatchlist] = useState(new Set());
  const [watchlistIds, setWatchlistIds] = useState(new Set());
  const [watchedlist, setWatchedlist] = useState(new Set());
  const [watchedlistIds, setWatchedlistIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);
  const [logoutResult, setLogoutResult] = useState(null);

  const fetchUser = () => {
    setLoading(true);
    axios
      .get("http://localhost:8080/api/me", { withCredentials: true })
      .then((res) => {
        console.log("Token verification, username: " + res.data);
        login(res.data);
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
      getWatchedList();
    }
  }, [user]);

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

  return (
    <UserContext.Provider
      value={{
        user,
        login,
        loading,
        logOut,
        handleWatchList,
        watchlist,
        watchedlist,
        getWatchList,
        watchlistIds,
        watchedlistIds,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
