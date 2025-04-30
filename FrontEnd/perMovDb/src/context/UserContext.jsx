import { createContext, useEffect, useState, useContext } from "react";
import { useLocation } from "react-router";
import axios from "axios";

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  // const navigate = useNavigate();

  const [watchlist, setWatchlist] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const location = useLocation();
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

  const getWatchList = async () => {
    if (user) {
      await axios
        .get("http://localhost:8080/user/watchlistIdSet", {
          withCredentials: true,
        })
        .then((res) => setWatchlist(res.data))
        .catch((err) => console.log(err));
      // console.log(watchlist);
    }
  };

  const login = (username) => {
    setUser(username);
  };

  const logOut = async () => {
    console.log("Logoutrequested");
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
      console.log(logoutResult);
      setUser(null); // Clear user after server confirmed logout
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

        if (res.status === 200) {
          // Convert the watchlist Set to an array
          const updatedArray = [...watchlist];

          if (actionType === "del") {
            const index = updatedArray.indexOf(movieId);
            if (index !== -1) {
              updatedArray.splice(index, 1); // Remove movieId from array
            }
          } else {
            updatedArray.push(movieId); // Add movieId to array
          }

          // Update the state with the modified array
          setWatchlist(updatedArray);
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
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
