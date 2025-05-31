import RatingCircle from "./RatingCircle";
import missing from "../assets/missing.png";
function Card({
  title,
  original_title,
  // overview,
  poster_path,
  vote_average,
  original_language,
  release_date,
}) {
  console.log(poster_path);
  const imageUrl = poster_path.endsWith("null") ? missing : poster_path;
  const release_year = new Date(release_date).toISOString().split("-");
  return (
    <div className="card flex flex-col justify-between">
      <img src={imageUrl} alt="" className="poster" />
      <div className="mb-9">
        <div className="info flex flex-row justify-between items-center  mx-3 my-3">
          <div className="">
            <p className="title text-sm">
              {original_language == "en" ? original_title : title}
            </p>
            <p className="text-xs text-amber-500">
              {release_year[0] +
                "-" +
                release_year[1] +
                "-" +
                release_year[2].substring(0, 2)}
            </p>
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
