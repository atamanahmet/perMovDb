import { useUser } from "../context/UserContext";

export default function WatchlistButton({ item }) {
  const { watchlist, handleWatchList } = useUser();
  return (
    <>
      <button
        className="absolute h-7 w-7 text-amber-100 bg-amber-200 rounded left-1 z-0 addButton"
        onClick={() => {
          handleWatchList(item.id, watchlist.includes(item.id) ? "del" : "add"); // Correctly call handleWatchList
        }}
      >
        {watchlist.includes(item.id) ? (
          <span className="text-amber-100 remove absolute">-</span>
        ) : (
          <span className="text-amber-100 add absolute">+</span>
        )}
      </button>
    </>
  );
}
