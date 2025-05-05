import Card from "./Card";
import { useState, useEffect } from "react";
import { useUser } from "../context/UserContext";
import WatchlistButton from "./WatchlistButton";
import axios from "axios";
import CardPlate from "./CardPlate";

export default function DiscoverPage({}) {
  const { user, loading, handleWatchList } = useUser();

  const [result, setResult] = useState(null);

  //first api call for discovery page
  useEffect(() => {
    axios
      .get("http://localhost:8080/", { withCredentials: true })
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

  // if (loading) {
  //   return <div>Loading...</div>; // wait for fetch
  // }

  return (
    <>
      <h2 className="text-center p-7  text-amber-100 font-bold text-4xl page-title">
        Discover new released movies
      </h2>
      <hr className="opacity-20 text-amber-700 horiz mb-11" />
      <main className=" my-10 flex flex-row flex-wrap gap-5 flex-8/12 justify-center discoverPage">
        <CardPlate data={result} message={"Loading.."} />
      </main>
    </>
  );
}
