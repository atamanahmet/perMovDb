import { useUser } from "../context/UserContext";

export default function WatchlistButton({ item }) {
  const { handleWatchList, watchlistIds } = useUser();
  const isInWatchlist = watchlistIds.has(item.id);

  // console.log("isInWatchlist:" + item.title + " " + isInWatchlist);

  return (
    <>
      <button
        className="absolute h-7 w-7 text-amber-100 bg-amber-200 rounded left-1 z-0 addButton"
        onClick={() => {
          handleWatchList(item.id, isInWatchlist ? "del" : "add");
        }}
      >
        {isInWatchlist ? (
          <span className="text-amber-100 remove absolute ">-</span>
        ) : (
          <span className="text-amber-100 add absolute">+</span>
        )}
      </button>
    </>
  );
}
