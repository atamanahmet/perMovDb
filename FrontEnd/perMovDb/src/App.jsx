import { useState, useEffect } from "react";
// import reactLogo from "./assets/react.svg";
import movieLogo from "/movie.png";
import "./App.css";
import axios from "axios";
import Card from "./components/card";

function App() {
  const [result, setResult] = useState();

  useEffect(() => {
    axios
      .get("http://localhost:8080/")
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

  // var asd = result == null ? "Error" : "OK";
  // var movie = {
  //   original_title: result.original_title,
  //   overview: result.overview,
  //   title: result.title,
  //   id: result.id,
  // };
  // result.forEach((element) => {
  //   console.log(element);
  // });
  function CardList({ result }) {
    // if (!result) return <div>Loading...</div>;
    if (!Array.isArray(result))
      return <div className="text-center text-amber-700">Loading...</div>;
    return (
      <>
        {result.map((e) => (
          <Card
            key={e.id}
            original_title={e.original_title}
            overview={e.overview}
          />
        ))}
      </>
    );
  }

  return (
    <>
      <nav className="bg-white  dark:bg-amber-600">
        <div className="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
          <a
            href="#"
            className="flex items-center space-x-3 rtl:space-x-reverse"
          >
            <img src={movieLogo} className="h-8 rounded-xl" alt="Our Logo" />
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
              className="md:hidden text-amber-500 dark:text-amber-400 hover:bg-amber-3500 dark:hover:bg-amber-700 focus:outline-none focus:ring-4 focus:ring-amber-700 dark:focus:ring-amber-700 rounded-lg text-sm p-2.5 me-1"
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
            <button
              data-collapse-toggle="navbar-search"
              type="button"
              className="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-amber-500 rounded-lg md:hidden hover:bg-amber-100 focus:outline-none focus:ring-2 focus:ring-amber-200 dark:text-amber-400 dark:hover:bg-amber-700 dark:focus:ring-amber-600"
              aria-controls="navbar-search"
              aria-expanded="false"
            >
              <span className="sr-only">Open main menu</span>
              <svg
                className="w-5 h-5"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 17 14"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M1 1h15M1 7h15M1 13h15"
                />
              </svg>
            </button>
          </div>
          <div
            className="items-center justify-between hidden w-full md:flex md:w-auto md:order-1"
            id="navbar-search"
          >
            <div className="relative mt-3 md:hidden">
              <div className="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
                <svg
                  className="w-4 h-4 text-amber-500 dark:text-amber-400"
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
              </div>
              <input
                type="text"
                id="search-navbar"
                className="block w-full p-2 ps-10 text-sm text-amber-900 border border-amber-300 rounded-lg bg-amber-50 focus:ring-orange-500 focus:border-orange-500 dark:bg-amber-700 dark:border-amber-600 dark:placeholder-amber-400 dark:text-white dark:focus:ring-orange-500 dark:focus:border-orange-500"
                placeholder="Search..."
              />
            </div>
            <ul className="flex flex-col p-4 md:p-0 mt-4 font-medium border border-amber-100 rounded-lg bg-amber-50 md:space-x-8 rtl:space-x-reverse md:flex-row md:mt-0 md:border-0 md:bg-white dark:bg-amber-600  ">
              <li>
                <a
                  href="#"
                  className="block py-2 px-3 text-white bg-amber-800 rounded-sm md:bg-transparent md:text-amber-700 md:p-0 md:dark:text-amber-800"
                  aria-current="page"
                >
                  Home
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="block py-2 px-3 text-amber-900 rounded-sm hover:bg-amber-100 md:hover:bg-transparent md:hover:text-amber-800 md:p-0 md:dark:hover:text-amber-800 dark:text-white dark:hover:bg-amber-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-amber-700"
                >
                  About
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="block py-2 px-3 text-amber-900 rounded-sm hover:bg-amber-100 md:hover:bg-transparent md:hover:text-amber-700 md:p-0 dark:text-white md:dark:hover:text-amber-500 dark:hover:bg-amber-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-amber-700"
                >
                  Services
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
      <main className="mx-65 my-10 flex flex-row flex-wrap gap-10">
        <CardList result={result} />
      </main>
    </>
  );
}

export default App;
