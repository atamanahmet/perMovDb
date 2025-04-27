import Card from "./card";
import { useState } from "react";
import { useUser } from "../context/UserContext";

export default function DiscoverPage({ result, onCardClick, onWatchListAdd }) {
  const user = useUser();
  return (
    <>
      <h2 className="text-center p-7  text-amber-100 font-bold text-4xl page-title">
        Discover new released movies
      </h2>
      <hr className="opacity-20 text-amber-700 horiz mb-11" />
      <main className=" my-10 flex flex-row flex-wrap gap-5 flex-8/12 justify-center discoverPage">
        <CardList
          result={result}
          onCardClick={onCardClick}
          onWatchListAdd={onWatchListAdd}
        />
      </main>
    </>
  );
}

function CardList({ result, onCardClick, onWatchListAdd }) {
  const [watchlistIds, setWatchlistIds] = useState(new Set());

  function handleWatchlistToogle(id) {
    let actionType = null;
    const updateSet = new Set(watchlistIds);
    console.log(updateSet);
    if (updateSet.has(id)) {
      updateSet.delete(id);
      actionType = "del";
    } else {
      updateSet.add(id);
      actionType = "add";
    }
    setWatchlistIds(updateSet);
    onWatchListAdd(id, actionType);
  }

  // if (!result) return <div>Loading film...</div>;
  if (!Array.isArray(result))
    return <div className="text-center text-amber-700">Loading...</div>;

  return (
    <>
      {result.map((item) => (
        <div key={item.id} className="relative mb-4">
          <button
            className="absolute h-7 w-7 text-amber-100 bg-amber-200 rounded left-1 z-0 addButton"
            onClick={() => {
              handleWatchlistToogle(item.id);
            }}
          >
            {watchlistIds.has(item.id) ? (
              <span className="text-amber-100 remove absolute">-</span>
            ) : (
              <span className="text-amber-100 add absolute">+</span>
            )}
          </button>
          <div onClick={() => onCardClick(item)} className="z-1 relative">
            <Card
              key={item.id}
              id={item.id}
              original_title={item.original_title}
              overview={item.overview}
              poster_path={item.poster_path}
              backdrop_path={item.backdrop_path}
              title={item.title}
              vote_average={item.vote_average.toFixed(1)}
              original_language={item.original_language}
            />
          </div>
        </div>
      ))}
    </>
  );
}
