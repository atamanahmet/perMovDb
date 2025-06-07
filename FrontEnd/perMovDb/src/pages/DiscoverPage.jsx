import Card from "../components/Card";
import { useState, useEffect } from "react";
import { useUser } from "../context/UserContext";
import WatchlistButton from "../components/WatchlistButton";
import axios from "axios";
import CardPlate from "../components/CardPlate";
import ToogleSwitch from "../components/ToogleSwitch";

export default function DiscoverPage({}) {
  const { user, loading, handleWatchList } = useUser();
  const [currentPage, setCurrentPage] = useState(1);

  const [result, setResult] = useState(null);
  const [adult, setAdult] = useState(false);
  const [isFetching, setIsFetching] = useState(false);

  useEffect(() => {
    let timeoutId;
    const handleScroll = () => {
      if (!isFetching) {
        const scrollY = window.scrollY;
        const visible = window.innerHeight;
        const fullHeight = document.documentElement.scrollHeight;

        if (scrollY + visible >= fullHeight * 0.9 && !isFetching) {
          console.log("Requested page, current page: " + currentPage);
          clearTimeout(timeoutId); // Clear any existing timeout
          timeoutId = setTimeout(() => {
            setCurrentPage((prev) => prev + 1); // Increment page after a short delay
          }, 100);
        }
      }
    };

    window.addEventListener("scroll", handleScroll);
    clearTimeout(timeoutId);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const fetchData = async () => {
    if (isFetching) return;
    setIsFetching(true);
    try {
      const res = await axios.get(
        "http://localhost:8080/?adult=true" + "&page=" + currentPage,
        { withCredentials: true }
      );
      // console.log(res.data);
      // setResult(res.data);
      setResult((prev) => {
        if (!prev || currentPage == 1) return res.data;
        const existingIds = new Set(prev.map((item) => item.id));
        const filteredNewItems = res.data.filter(
          (item) => !existingIds.has(item.id)
        );
        return [...prev, ...filteredNewItems];
      });
    } catch (err) {
      console.error("Backend error:", err);
    } finally {
      setIsFetching(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [currentPage, adult]);

  if (!result) {
    return <div>Loading...</div>;
  }

  // function handleToogle() {
  //   setAdult((prev) => !prev);
  // }

  return (
    <>
      <h2 className="text-center p-7  text-amber-100 font-bold text-4xl page-title">
        Discover popular movies
      </h2>

      <div className="flex flex-col  max-w-11/12 justify-center mx-auto ">
        {/* <div className="w-10/12 -ml-2 text-right mt-5">
          <ToogleSwitch label="Adult" stateChange={() => handleToogle()} />
        </div> */}
        <main className=" my-10 flex flex-row flex-wrap gap-5 justify-center discoverPage">
          <CardPlate data={result} message={"Loading.."} />
        </main>
      </div>
    </>
  );
}
