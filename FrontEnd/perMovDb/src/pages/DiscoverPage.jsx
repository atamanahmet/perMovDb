import Card from "../components/Card";
import { useState, useEffect } from "react";
import { useUser } from "../context/UserContext";
import WatchlistButton from "../components/WatchlistButton";
import axios from "axios";
import CardPlate from "../components/CardPlate";
import ToggleSwitch from "../components/ToggleSwitch";
import MovieFilterSidebar from "../components/MovieFilterSidebar";

export default function DiscoverPage({}) {
  const {
    user,
    loading,
    handleWatchList,
    mediaType,
    handleToggle,
    setMediaType,
    filters,
  } = useUser();
  const [toggleLabel, setToggleLabel] = useState("TV");

  const [sort, setSort] = useState("");
  // const [sort, setSort] = useState("vote_count.desc");
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
      const url = `http://localhost:8080/${mediaType}?adult=${adult}&page=${currentPage}&sort=${sort}&genreIdList=${filters.genres}`;
      console.log(url);
      const res = await axios.get(url, { withCredentials: true });
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
  }, [currentPage, adult, mediaType, toggleLabel, filters]);

  if (!result) {
    return <div>Loading...</div>;
  }

  function handleMediaToogle() {
    setToggleLabel(mediaType == "movie" ? "TV Series" : "Cinema");
    setMediaType(mediaType == "movie" ? "tv" : "movie");
    // handleToggle;
  }

  return (
    <>
      <div className="absolute z-2">
        <div className="relative mt-24">
          <MovieFilterSidebar></MovieFilterSidebar>
        </div>
      </div>

      <h2 className="text-center p-7  text-amber-100 font-bold text-4xl page-title">
        Discover popular movies
      </h2>
      <div className="flex flex-col  max-w-11/12 justify-center mx-auto ">
        <div className="w-10/12 text-right mt-5">
          {/* <ToggleSwitch
            label={toggleLabel}
            stateChange={() => handleMediaToogle()}
          /> */}
          <ToggleSwitch
            label={toggleLabel}
            stateChange={() => handleMediaToogle()}
          />
        </div>
        <main className=" my-10 flex flex-row flex-wrap gap-5 justify-center discoverPage">
          <CardPlate data={result} message={"Loading.."} />
        </main>
      </div>
    </>
  );
}
