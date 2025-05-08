import { useUser } from "../context/UserContext";
import emptyHeart from "../assets/emptyHeart.png";
import solidHeart from "../assets/solidHeart.png";

export default function LovedButton({ item }) {
  const { lovedlistIds, handleLovedList } = useUser();

  const isInLovedlist = lovedlistIds.has(item.id);

  return (
    <>
      <button
        className=" h-7 w-7 text-amber-100 bg-amber-200 rounded z-0 addButton absolute ml-19"
        onClick={() => {
          handleLovedList(item.id, isInLovedlist ? "del" : "add");
        }}
      >
        {isInLovedlist ? (
          <span className="text-amber-100 add size-4 ">
            <img
              src={solidHeart}
              alt=""
              className=" closedEye max-w-10/12 scale-90 "
            />
          </span>
        ) : (
          <span className="text-amber-100 add size-6">
            <img
              src={emptyHeart}
              alt=""
              className="closedEye max-w-10/12  scale-90"
            />
          </span>
        )}
      </button>
    </>
  );
}
