import Card from "./card";

export default function MainPage({ result, onCardClick }) {
  return (
    <>
      <main className="mx-65 my-10 flex flex-row flex-wrap gap-10">
        <CardList result={result} onCardClick={onCardClick} />
      </main>
    </>
  );
}

function CardList({ result, onCardClick }) {
  // if (!result) return <div>Loading...</div>;
  if (!Array.isArray(result))
    return <div className="text-center text-amber-700">Loading...</div>;

  return (
    <>
      {result.map((item) => (
        <div key={item.id} onClick={() => onCardClick(item)}>
          <Card
            key={item.id}
            original_title={item.original_title}
            overview={item.overview}
            poster_path={item.poster_path}
            backdrop_path={item.backdrop_path}
            title={item.title}
            vote_average={item.vote_average.toFixed(1)}
            original_language={item.original_language}
          />
        </div>
      ))}
    </>
  );
}
