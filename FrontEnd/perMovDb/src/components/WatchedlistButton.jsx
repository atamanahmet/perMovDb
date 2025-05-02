import { useUser } from "../context/UserContext";

export default function WatchedlistButton({ item }) {
  const { handleWatchedList, watchedlistIds } = useUser();

  const isInWatchedlist = watchedlistIds.has(item.id);

  return (
    <>
      <button
        className="absolute h-7 w-7 text-amber-100 bg-amber-200 rounded left-1 z-0 addButton"
        onClick={() => {
            handleWatchedList(item.id, isInWatchedlist ? "del" : "add");
        }}
      >
        {isInWatchedlist ? (
          <span className="text-amber-100 remove absolute">-</span>
        ) : (
          <span className="text-amber-100 add absolute">+</span>
        )}
      </button>
    </>
  );
}