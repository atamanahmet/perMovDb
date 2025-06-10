import { Navigate } from "react-router";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router";
import Card from "./Card";
import ListButton from "./ListButton";
import { Tooltip } from "@mui/material";
import { useState } from "react";
import ToogleSwitch from "./ToggleSwitch";

export default function CardPlate({ data, addOrRemove, message }) {
  const { user, navigateToDetails } = useUser();

  const style = {
    watchlist:
      "absolute h-7 w-7 text-amber-100 bg-amber-200 rounded left-1 z-0 addButton",
    watchedlist:
      " h-7 w-7 text-amber-100 bg-amber-200 rounded z-0 addButton absolute ml-10",
    lovedlist:
      " h-7 w-7 text-amber-100 bg-amber-200 rounded z-0 addButton absolute ml-19",
  };

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
          {user && <ListButton item={item} style={style} />}
          <div
            onClick={() => onCardClick(item)}
            className="z-1 relative text-left"
          >
            <Card
              // key={item.id}
              // id={item.id}
              // original_title={item.original_title}
              // overview={item.overview}
              // poster_path={item.poster_path}
              // backdrop_path={item.backdrop_path}
              // itemTitle={item.title}
              // vote_average={item.vote_average.toFixed(1)}
              // original_language={item.original_language}
              // release_date={item.release_date}
              item={item}
            />
          </div>
        </div>
      ))}
    </>
  );
}
