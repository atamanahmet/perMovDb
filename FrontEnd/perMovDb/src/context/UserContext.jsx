import { createContext, useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router";
import axios from "axios";

const UserContext = createContext();
export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);

  useEffect(() => {
    axios
      .get("localhost:8080/api/me", { withCredentials: true })
      .then((res) => setUser(res.data))
      .catch((err) => {
        console.log("Error: " + err);
        setUser(null);
      });
  }, []);

  //logout from api
  const logOut = async () => {
    const response = await axios
      .get("http://localhost:8080/logout", { withCredentials: true })
      .then(setUser(null))
      .catch((err) => console.error("Backend error:", err));
    console.log(response);
  };

  const login = async (formData) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/login",
        {
          username: formData.username,
          password: formData.password,
        },
        { withCredentials: true }
      );
      console.log(response.status);

      if (response.status == 200) {
        setUser(response.data);
        console.log("username : " + response.data);
        console.log("login successsfull. Redirecting");

        setTimeout(() => navigate("/discover"), 2000);
      }
    } catch (err) {
      console.log("Error :" + err);
    }
  };

  return (
    <UserContext.Provider
      value={{
        user,
        setUser,
        login,
        logOut,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
