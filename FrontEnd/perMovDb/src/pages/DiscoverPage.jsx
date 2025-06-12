import Card from "../components/Card";
import { useState, useEffect, useRef } from "react";
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
    setFilters,
  } = useUser();
  const [toggleLabel, setToggleLabel] = useState("TV");

  // const [sort, setSort] = useState("");
  // const [sort, setSort] = useState("vote_count.desc");
  const [currentPage, setCurrentPage] = useState(1);

  const [result, setResult] = useState(null);
  const [adult, setAdult] = useState(true);
  const [isFetching, setIsFetching] = useState(false);

  const timeoutRef = useRef(null);
  const [pendingFilters, setPendingFilters] = useState(filters);

  const [showHeader, setShowHeader] = useState(true);
  const lastScrollY = useRef(0);

  useEffect(() => {
    const handleScroll = () => {
      const currentY = window.scrollY;
      if (currentY > lastScrollY.current && currentY > 100) {
        setShowHeader(false); // scrolling down
      } else {
        setShowHeader(true); // scrolling up
      }
      lastScrollY.current = currentY;
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    setResult(null);
    setCurrentPage(1);
  }, [filters, mediaType]);
  useEffect(() => {
    const handler = setTimeout(() => {
      setFilters(pendingFilters);
    }, 300); // debounce delay

    return () => clearTimeout(handler);
  }, [pendingFilters]);

  useEffect(() => {
    const handleScroll = () => {
      if (
        !isFetching &&
        window.scrollY + window.innerHeight >=
          document.documentElement.scrollHeight * 0.75
      ) {
        if (timeoutRef.current) clearTimeout(timeoutRef.current);
        timeoutRef.current = setTimeout(() => {
          setCurrentPage((prev) => prev + 1);
        }, 50);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => {
      clearTimeout(timeoutRef.current);
      window.removeEventListener("scroll", handleScroll);
    };
  }, [isFetching]);

  const fetchData = async () => {
    if (isFetching) return;
    setIsFetching(true);
    try {
      const url = `http://localhost:8080/${mediaType}?adult=${adult}&page=${currentPage}&sort=${
        filters.sort
      }&genreIdList=${filters.genres}&yearRange=${
        filters.yearRange
      }&ratingRange=${
        filters.rating
      }&languages=${filters.languages.toString()}`;

      // console.log(url);
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
  }, [currentPage, adult, mediaType, filters]);

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
      <div className="pt-15.5">
        <div className="absolute z-2">
          <div className="relative mt-24 sticky-sidebar">
            <MovieFilterSidebar />
          </div>
        </div>
        <h2 className="text-center p-7 text-amber-100 font-bold text-4xl page-title">
          Discover popular movies
        </h2>
        <div className="flex flex-col max-w-11/12 justify-center mx-auto">
          <div className="w-10/12 text-right mt-5">
            <ToggleSwitch
              label={toggleLabel}
              stateChange={() => handleMediaToogle()}
            />
          </div>
          <main className="my-10 flex flex-row flex-wrap gap-5 justify-center discoverPage">
            <CardPlate data={result} message={"Loading.."} />
          </main>
        </div>
      </div>
    </>
  );
}
