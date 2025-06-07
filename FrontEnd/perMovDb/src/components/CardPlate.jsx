import { Navigate } from "react-router";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router";
import Card from "./Card";
import ListButton from "./ListButton";
import { Tooltip } from "@mui/material";
// import WatchlistButton from "./WatchlistButton";
// import WatchedlistButton from "./WatchedlistButton";
// import LovedButton from "./LovedButton";

export default function CardPlate({ data, addOrRemove, message }) {
  const { user, navigateToDetails } = useUser();

  const navigate = useNavigate();

  function onCardClick(item) {
    navigateToDetails(item);
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

  return (
    <>
      {Array.from(data).map((item) => (
        <div key={item.id} className="relative mb-8">
          {user && <ListButton item={item} />}
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
              itemTitle={item.title}
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
