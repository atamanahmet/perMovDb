import movieLogo from "/movie.png";
import profile from "../assets/profile.png";
// import SingleButton from "./SingleButton";
import { useState, useEffect } from "react";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import ToogleSwitch from "./ToggleSwitch";

export default function Header() {
  const navigate = useNavigate();

  const {
    user,
    logOut,
    getWatchList,
    searchHandler,
    profilePictureUrl,
    storedPhoto,
    getRecommendation,
    setMediaType,
    mediaType,
  } = useUser();

  // useEffect(() => {
  //   getProfilePhoto();
  // }, []);

  const [searchQuery, setSearchQuery] = useState("");

  const handleChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const navigateRegister = () => {
    navigate("/register");
  };
  const navigateLogin = () => {
    navigate("/login");
  };
  const navigateProfile = () => {
    getWatchList();
    getRecommendation();
    navigate("/profile");
  };
  function handleLogOut() {
    logOut();
  }

  return (
    <>
      <nav className="bg-amber-600  dark:bg-amber-600">
        <div className="max-w-screen-xxl flex flex-wrap items-center justify-between p-3 header">
          <a
            href="/"
            className="flex items-center space-x-3 rtl:space-x-reverse"
          >
            <img src={movieLogo} className="h-8 " alt="Our Logo" />
            <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">
              PerMovDb
            </span>
          </a>

          <div className="flex md:order-2 gap-3">
            <a href="/top">
              <button
                className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 top-buttons"
                // onClick={}
              >
                Top
              </button>
            </a>
            <a href="/new">
              <button
                className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 top-buttons"
                // onClick={}
              >
                New+
              </button>
            </a>
            <button
              type="button"
              data-collapse-toggle="navbar-search"
              aria-controls="navbar-search"
              aria-expanded="false"
              className="md:hidden text-amber-500 dark:text-amber-400 hover:bg-amber-3500 dark:hover:bg-red-700 focus:outline-none focus:ring-4 focus:ring-amber-700 dark:focus:ring-amber-700 rounded-lg text-sm p-2.5 me-1"
            >
              <svg
                className="w-5 h-5"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 20 20"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
                />
              </svg>
              <span className="sr-only">Search</span>
            </button>
            {/* <a href="/profile">{user ? user : "nouser"}</a> */}
            <div className="relative hidden md:block">
              <div className="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
                <svg
                  className="w-4 h-4 text-amber-500 dark:text-amber-600"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 20 20"
                >
                  <path
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
                  />
                </svg>
                <span className="sr-only">Search icon</span>
              </div>
              <form
                onSubmit={(e) => {
                  e.preventDefault();
                  searchHandler(searchQuery);
                }}
              >
                <input
                  type="text"
                  id="search"
                  name="search"
                  className="block w-full p-2 ps-10 text-sm text-amber-900 border border-amber-300 rounded-lg bg-amber-50 focus:ring-orange-500 focus:border-orange-500 dark:bg-amber-800 dark:border-amber-600 dark:placeholder-amber-400 dark:text-white dark:focus:ring-orange-500 dark:focus:border-orange-500"
                  placeholder="Search..."
                  value={searchQuery}
                  onChange={handleChange}
                />
              </form>
            </div>
            <div className="ml-1 smallPX flex gap-3">
              {user && (
                <img
                  className="w-10 h-10 p-1 rounded-full ring-2 ring-gray-300 dark:ring-gray-500"
                  src={storedPhoto ? storedPhoto : profile}
                  alt="Bordered avatar"
                  onClick={navigateProfile}
                />
              )}
              {!user && (
                <button
                  className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 top-buttons"
                  onClick={navigateRegister}
                >
                  Register
                </button>
              )}
              {!user && (
                <button
                  className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 top-buttons"
                  onClick={navigateLogin}
                >
                  Login
                </button>
              )}
              {user && (
                <button
                  className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 top-buttons"
                  onClick={handleLogOut}
                >
                  Log Out
                </button>
              )}
            </div>
            {/* <button
              data-collapse-toggle="navbar-search"
              type="button"
              className="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-amber-500 rounded-lg md:hidden hover:bg-amber-100 focus:outline-none focus:ring-2 focus:ring-amber-200 dark:text-amber-400 dark:hover:bg-amber-700 dark:focus:ring-amber-600"
              aria-controls="navbar-search"
              aria-expanded="false"
            ></button> */}
          </div>
        </div>
      </nav>
    </>
  );
}
