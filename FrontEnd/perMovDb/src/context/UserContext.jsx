import { createContext, useEffect, useState, useContext } from "react";
// import { useNavigate } from "react-router";
import axios from "axios";

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  // const navigate = useNavigate();

  const [user, setUser] = useState(() => {
    const cachedUser = sessionStorage.getItem("userData");
    return cachedUser ? JSON.parse(cachedUser) : null;
  });

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/me", { withCredentials: true })
      .then((res) => login(res.data))
      .catch((err) => {
        console.log("Error: " + err);
        setUser(null);
      });
  }, []);

  //
  const login = (username) => {
    setUser(username);
    sessionStorage.setItem("userData", JSON.stringify(username));
  };
  //
  const logOut = () => {
    setUser(null);
    sessionStorage.removeItem("userData");
  };

  return (
    <UserContext.Provider
      value={{
        user,
        login,
        logOut,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
