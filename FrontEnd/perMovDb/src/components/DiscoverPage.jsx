import Card from "./card";
// import { useState } from "react";

export default function DiscoverPage({ result, onCardClick, onWatchListAdd }) {
  return (
    <>
      <main className="discoverPage my-10 flex flex-row flex-wrap gap-8 flex-8/12 justify-center discoverPage">
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
  // const [addIcon, setAddIcon] = useState("+");
  // if (!result) return <div>Loading...</div>;
  if (!Array.isArray(result))
    return <div className="text-center text-amber-700">Loading...</div>;

  return (
    <>
      {result.map((item) => (
        <div key={item.id}>
          <div>
            <button
              className="h-10 w-10 bg-amber-200 rounded"
              onClick={() => {
                onWatchListAdd(item.id);
              }}
            >
              +
            </button>
          </div>
          <div onClick={() => onCardClick(item)}>
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
