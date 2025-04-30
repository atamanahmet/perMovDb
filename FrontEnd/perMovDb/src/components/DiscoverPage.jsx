import Card from "./Card";
import { useState } from "react";
import { useUser } from "../context/UserContext";
import WatchlistButton from "./WatchlistButton";

export default function DiscoverPage({ result, onCardClick }) {
  const { user, loading, handleWatchList, watchlist } = useUser();

  if (loading) {
    return <div>Loading...</div>; // wait for fetch
  }

  return (
    <>
      <h2 className="text-center p-7  text-amber-100 font-bold text-4xl page-title">
        Discover new released movies + User: {user}
      </h2>
      <hr className="opacity-20 text-amber-700 horiz mb-11" />
      <main className=" my-10 flex flex-row flex-wrap gap-5 flex-8/12 justify-center discoverPage">
        <CardList
          result={result}
          onCardClick={onCardClick}
          user={user}
          list={watchlist}
          addOrRemove={handleWatchList}
        />
      </main>
    </>
  );
}

function CardList({ result, onCardClick, addOrRemove }) {
  const { user, watchlist } = useUser();
  function handleWatchlistToogle(id) {
    let actionType = null;
    const updateSet = new Set(watchlist);
    console.log(updateSet);
    if (updateSet.has(id)) {
      actionType = "del";
    } else {
      actionType = "add";
    }
    addOrRemove(id, actionType);
  }

  // if (!result) return <div>Loading film...</div>;
  if (!Array.isArray(result))
    return <div className="text-center text-amber-700">Loading...</div>;

  return (
    <>
      {result.map((item) => (
        <div key={item.id} className="relative mb-4">
          {user && <WatchlistButton item={item} />}
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
