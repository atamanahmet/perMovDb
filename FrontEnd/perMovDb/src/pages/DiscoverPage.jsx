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
    mediaType,
    setMediaType,
    filters,
    setFilters,
    movieData,
    isFetching,
    setCurrentPage,
  } = useUser();

  const [toggleLabel, setToggleLabel] = useState(
    mediaType == "movie" ? "Cinema" : "TV"
  );
  const timeoutRef = useRef(null);
  const [pendingFilters, setPendingFilters] = useState(filters);

  const [showHeader, setShowHeader] = useState(true);
  const lastScrollY = useRef(0);

  useEffect(() => {
    const handleScroll = () => {
      const currentY = window.scrollY;
      if (currentY > lastScrollY.current && currentY > 100) {
        setShowHeader(false); //down
      } else {
        setShowHeader(true); //up
      }
      lastScrollY.current = currentY;
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const handler = setTimeout(() => {
      setFilters(pendingFilters);
    }, 300); //debounce delay

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

  if (!movieData) {
    return <div>Loading...</div>;
  }

  function handleMediaToogle() {
    setToggleLabel(mediaType == "movie" ? "Cinema" : "TV");
    setMediaType(mediaType == "movie" ? "tv" : "movie");
  }

  return (
    <>
      <div className="absolute z-2">
        <div className="relative mt-24 sticky-sidebar">
          <MovieFilterSidebar />
        </div>
      </div>
      <h2 className="text-center p-7 text-amber-100 font-bold text-4xl page-title">
        Discover popular movies
      </h2>
      {movieData && (
        <div className="flex flex-col max-w-11/12 justify-center mx-auto">
          <div className="w-10/12 text-right mt-5">
            <ToggleSwitch
              label={toggleLabel}
              stateChange={() => handleMediaToogle()}
            />
          </div>
          <main className="my-10 flex flex-row flex-wrap gap-5 justify-center discoverPage">
            <CardPlate data={movieData} message={"Loading.."} />
          </main>
        </div>
      )}

      {movieData.length == 0 && (
        <>
          <h2>No data available</h2>
        </>
      )}
    </>
  );
}
