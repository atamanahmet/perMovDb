import movieLogo from "/movie.png";
import SingleButton from "./SingleButton";
import { useNavigate, useLocation } from "react-router-dom";

export default function Header() {
  const navigate = useNavigate();
  const location = useLocation();

  const openRegister = () => {
    navigate("/register", { state: { backgroundLocation: location } });
  };
  const openLogin = () => {
    navigate("/login", { state: { backgroundLocation: location } });
  };

  return (
    <>
      <nav className="bg-amber-600  dark:bg-amber-600">
        <div className="max-w-screen-xl flex flex-wrap items-center justify-between p-3 header">
          <a
            href="/"
            className="flex items-center space-x-3 rtl:space-x-reverse"
          >
            <img src={movieLogo} className="h-8 " alt="Our Logo" />
            <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">
              PerMovDb
            </span>
          </a>
          <div className="flex md:order-2">
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
              <input
                type="text"
                id="search-navbar"
                className="block w-full p-2 ps-10 text-sm text-amber-900 border border-amber-300 rounded-lg bg-amber-50 focus:ring-orange-500 focus:border-orange-500 dark:bg-amber-800 dark:border-amber-600 dark:placeholder-amber-400 dark:text-white dark:focus:ring-orange-500 dark:focus:border-orange-500"
                placeholder="Search..."
              />
            </div>
            <div className="ml-1 smallPX">
              <SingleButton
                text="Sign Up"
                path="/register"
                onClick={openRegister}
              />
              <SingleButton text="Login" path="/login" onClick={openLogin} />
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
