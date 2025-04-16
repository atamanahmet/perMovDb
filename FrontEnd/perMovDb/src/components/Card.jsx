import RatingCircle from "./RatingCircle";
function Card({
  title,
  original_title,
  // overview,
  poster_path,
  vote_average,
  original_language,
}) {
  return (
    <div className="card flex flex-col justify-between">
      <img src={poster_path} alt="" className="poster" />
      <div className="info flex flex-row justify-between items-center  mx-3 my-3">
        <p className="title text-sm ">
          {original_language == "en" ? original_title : title}
        </p>
        <div className="text-shadow-emerald-50">
          <RatingCircle percentage={vote_average} />
        </div>
      </div>
    </div>
  );
}
export default Card;
