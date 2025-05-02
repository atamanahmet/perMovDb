import { useUser } from "../context/UserContext";
import Card from "./Card";
import WatchlistButton from "./WatchlistButton";
// import WatchedlistButton from "./WatchedlistButton";

export default function CardPlate({ result, data, addOrRemove }) {
  const {
    user,
    handleWatchList,
    watchlist,
    watchlistIds,
    watchedlist,
    watchedlistIds,
  } = useUser();
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

  if (data == null) {
    return (
      <>
        <div className="text-center">
          <h2>Add something to your watchlist..</h2>
        </div>
      </>
    );
  }

  return (
    <>
      {Array.from(data).map((item) => (
        <div key={item.id} className="relative mb-4">
          <WatchlistButton
            item={item}
            handleWatchList={handleWatchList}
          ></WatchlistButton>
          <WatchlistButton
            item={item}
            handleWatchList={handleWatchList}
          ></WatchlistButton>

          <div
            onClick={() => onCardClick(item)}
            className="z-1 relative text-left"
          >
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
              release_date={item.release_date}
            />
          </div>
        </div>
      ))}
    </>
  );
}
