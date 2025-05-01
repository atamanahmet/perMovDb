import RatingCircle from "./RatingCircle";
function Card({
  title,
  original_title,
  // overview,
  poster_path,
  vote_average,
  original_language,
  release_date,
}) {
  const release_year = new Date(release_date).toISOString().split("-")[0];
  return (
    <div className="card flex flex-col justify-between">
      <img src={poster_path} alt="" className="poster" />
      <div className="mb-9">
        <div className="info flex flex-row justify-between items-center  mx-3 my-3">
          <div className="">
            <p className="title text-sm">
              {original_language == "en" ? original_title : title}
            </p>
            <p className="text-xs text-amber-500">{release_year}</p>
          </div>
          <div className="text-shadow-emerald-50">
            <RatingCircle percentage={vote_average} />
          </div>
        </div>
      </div>
    </div>
  );
}
export default Card;
