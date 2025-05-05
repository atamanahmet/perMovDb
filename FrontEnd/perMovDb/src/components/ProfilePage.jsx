import { useUser } from "../context/UserContext";
import { useEffect, useState } from "react";
import axios from "axios";
import MoviePlate from "./MoviePlate";
import WatchlistButton from "./WatchlistButton";
import Card from "./Card";
import CardPlate from "./CardPlate";

import profilePlaceholder from "../assets/profile.png";

export default function ProfilePage() {
  const {
    user,
    logout,
    watchlist,
    handleWatchList,
    watchedlist,
    storedPhoto,
    getProfilePhoto,
  } = useUser();

  const [selection, setSelection] = useState("Watchlist");
  // const [dataSet, setDataSet] = useState(watchlist);

  const dataSet = selection === "Watchlist" ? watchlist : watchedlist;

  function selectiveRender() {
    return (
      <>
        <h2>{selection}</h2>
        <main className=" my-10 flex flex-row flex-wrap gap-5 justify-center">
          <CardPlate
            data={dataSet}
            message={"Add something to your watchlist.."}
          />
        </main>
      </>
    );
  }

  const [locationInfo, setLocationInfo] = useState({
    country: "",
    ip: "",
    source: "",
  });

  useEffect(() => {
    async function fetchCountry() {
      // const httpsUrl = 'https://ipapi.co/json/';
      const httpUrl = "http://ip-api.com/json/";

      try {
        const response = await axios.get(httpUrl);
        const data = response.data;

        setLocationInfo({
          country: data.country || data.country_name || "",
          city: data.city || "",
          ip: data.query || data.ip || "",
          source: httpUrl,
        });
      } catch (error) {
        console.error("Failed to fetch location info:", error);
        setLocationInfo({
          country: "",
          ip: "",
          source: "Error",
        });
      }
    }
    // getProfilePhoto();
    fetchCountry();
  }, []);

  function selectionChange(select) {
    setSelection(select);
  }

  return (
    <>
      {!user && (
        <div className="text-center mt-10">
          <h2>
            Please{" "}
            <a href="/login" className="font-bold italic text-amber-950">
              login...
            </a>
          </h2>
        </div>
      )}
      {user && (
        <div className="container px-32 mx-auto">
          <div className=" flex flex-col min-w-0 break-words bg-amber-50 w-full mb-6 mt-23 shadow-xl rounded-lg ">
            <div className="px-15">
              <div className="flex flex-row justify-between ">
                <div className="flex flex-row">
                  <img
                    alt="..."
                    src={storedPhoto}
                    onError={(e) => {
                      e.target.src = profilePlaceholder;
                    }}
                    className="shadow-xl rounded-full  align-middle border-6
                     border-amber-600 -mt-16 max-w-35 max-h-35"
                  />
                  <h3 className="text-4xl font-semibold leading-normal text-blueGray-700 mt-6 ">
                    {user}
                  </h3>
                </div>

                {/* <div className="text-sm leading-normal mt-0 mb-2 text-blueGray-400 font-bold uppercase">
                      <i className="fas fa-map-marker-alt mr-2 text-lg text-blueGray-400"></i>
                      {locationInfo.city}/{locationInfo.country}
                    </div> */}
                {/* <div className="w-full lg:w-4/12 px-4 lg:order-3 lg:text-right lg:self-center">
                    <div className="py-6 px-3 mt-32 sm:mt-0">
                      <button
                        className="bg-amber-500 active:bg-amber-700 uppercase text-amber-50 font-bold hover:shadow-md shadow text-xs px-4 py-2 rounded outline-none focus:outline-none sm:mr-2 mb-1 ease-linear transition-all duration-150"
                        type="button"
                      >
                        Connect
                      </button>
                    </div>
                  </div> */}
                <div className="flex py-4 lg:pt-4 ">
                  <button onClick={() => selectionChange("Watchlist")}>
                    <div className="mr-4 p-3 text-center">
                      <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                        {watchlist.size}
                      </span>
                      <span className="text-sm text-blueGray-400">
                        Watchlist
                      </span>
                    </div>
                  </button>
                  <button onClick={() => selectionChange("Watched")}>
                    <div className="mr-4 p-3 text-center">
                      <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                        {watchedlist.size}
                      </span>
                      <span className="text-sm text-blueGray-400">Watched</span>
                    </div>
                  </button>
                  <button onClick={() => selectionChange("Watched")}>
                    <div className="lg:mr-4 p-3 text-center">
                      <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                        89
                      </span>
                      <span className="text-sm text-blueGray-400">
                        Reviewed
                      </span>
                    </div>
                  </button>
                </div>
              </div>

              <div className="">{selectiveRender()}</div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
