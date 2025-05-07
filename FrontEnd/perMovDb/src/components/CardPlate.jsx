import { Navigate } from "react-router";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router";
import Card from "./Card";
import WatchlistButton from "./WatchlistButton";
import WatchedlistButton from "./WatchedlistButton";
import LovedButton from "./LovedButton";

export default function CardPlate({ data, addOrRemove, message }) {
  const {
    user,
    handleWatchList,
    handleWatchedList,
    handleLovedList,
    watchlist,
    // watchedlist,
  } = useUser();
  const navigate = useNavigate();

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
  function onCardClick(item) {
    navigate("/details/" + item.id);
  }

  if (data == null) {
    return (
      <>
        <div className="text-center">
          <h2>{message}</h2>
        </div>
      </>
    );
  }
  function userRelatedButtonCheck(item, handleWatchList, user) {
    // console.log("user: " + user);
    if (user) {
      return (
        <>
          <WatchlistButton
            item={item}
            handleWatchList={handleWatchList}
          ></WatchlistButton>
          <WatchedlistButton
            item={item}
            handleWatchList={handleWatchedList}
          ></WatchedlistButton>
          <LovedButton
            item={item}
            handleWatchList={handleLovedList}
          ></LovedButton>
        </>
      );
    }
  }

  return (
    <>
      {Array.from(data).map((item) => (
        <div key={item.id} className="relative mb-4">
          {userRelatedButtonCheck(item, handleWatchList, user)}
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
