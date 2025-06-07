import RatingCircle from "./RatingCircle";
import missing from "../assets/missing.png";
import { Tooltip } from "@mui/material";
function Card({
  itemTitle,
  original_title,
  // overview,
  poster_path,
  vote_average,
  original_language,
  release_date,
}) {
  const imageUrl = poster_path.endsWith("null") ? missing : poster_path;
  const release_year = new Date(release_date).toISOString().split("-");
  return (
    <div className="card flex flex-col justify-between">
      <Tooltip
        title={
          <>
            <span className="font-bold">Title: </span>
            {original_title}
            <br />
            <span className="font-bold"> Release Date: </span>
            {release_date}
            <br />
            <span className="font-bold"> Original Language: </span>
            {original_language}
          </>
        }
        placement="right-start"
      >
        <img src={imageUrl} alt="" className="poster" />
      </Tooltip>
      <div className="mb-9">
        <div className="info flex flex-row justify-between items-center  mx-3 my-3">
          <div className="">
            <p className="title text-sm">
              {original_language == "en" ? original_title : itemTitle}
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
