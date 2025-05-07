import { useUser } from "../context/UserContext";
import { useEffect, useState } from "react";
import axios from "axios";
import MoviePlate from "./MoviePlate";
import WatchlistButton from "./WatchlistButton";
import Card from "./Card";
import CardPlate from "./CardPlate";
import ToogleSwitch from "./ToogleSwitch";
import profile from "../assets/profile.png";

import profilePlaceholder from "../assets/profile.png";

export default function ProfilePage() {
  const [adult, setAdult] = useState(false);

  const {
    user,
    logout,
    watchlist,
    lovedlist,
    handleWatchList,
    handleWatchedList,
    handleLovedList,
    watchedlist,
    storedPhoto,
    getProfilePhoto,
  } = useUser();

  function handleToogle() {
    setAdult(!adult);
  }

  const [selection, setSelection] = useState("Watchlist");
  // const [dataSet, setDataSet] = useState(watchlist);

  useEffect(() => {}, []);

  const dataMap = {
    Watchlist: watchlist,
    Loved: lovedlist,
    Watchedlist: watchedlist,
  };

  const dataSet = dataMap[selection] || watchlist;

  function selectiveRender() {
    return (
      <>
        <div className="flex flex-col">
          <h2 className="text-center p-7 text-amber-100 font-bold bg-amber-800 text-4xl">
            {selection}
          </h2>
          <div className="px-10  text-right mt-2">
            <ToogleSwitch label="Adult" stateChange={() => handleToogle()} />
          </div>
          <main className="mt-10 flex flex-row flex-wrap gap-5 justify-center">
            <CardPlate
              data={dataSet}
              message={"Add something to your watchlist.."}
            />
          </main>
        </div>
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
        <div className="flex flex-col mx-70">
          <div className="profileArea flex flex-row mt-20 -mb-19 justify-between px-10">
            <div className="usernameArea z-10 flex flex-row">
              <img
                alt="..."
                src={storedPhoto ? storedPhoto : profile}
                className="shadow-xl rounded-full  border-6
                     border-amber-600 -mt-16 w-35 h-35 z-10"
              />
              <h3 className="text-4xl font-semibold leading-normal text-blueGray-700 mt-6 text-amber-50 ">
                {user}
              </h3>
            </div>
            <div className="buttonSet -mt-18">
              <button
                style={{
                  cursor: "pointer",
                  boxShadow: "2px 4px 8px 0 rgba(90, 44, 0, 0.5)",
                  backgroundColor:
                    selection == "Watchlist" ? "#d97706" : "#f59e0b",
                  color: selection == "Watchlist" ? "#fffbeb" : "black",
                }}
                className="pb-2 mx-1.5 mt-2 rounded-2xl listButton"
                onClick={() => selectionChange("Watchlist")}
              >
                <div className="p-3  text-center text-shadow-xs">
                  <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                    {watchlist.size}
                  </span>
                  <span className="text-sm text-blueGray-400">Watchlist</span>
                </div>
              </button>
              <button
                style={{
                  cursor: "pointer",
                  boxShadow: "2px 4px 8px 0 rgba(90, 44, 0, 0.5)",
                  backgroundColor:
                    selection == "Watchedlist" ? "#d97706" : "#f59e0b",
                  color: selection == "Watchedlist" ? "#fffbeb" : "black",
                }}
                className="pb-2 -z-2 mx-1.5 mt-2 rounded-2xl listButton"
                onClick={() => {
                  selectionChange("Watchedlist");
                }}
              >
                <div className=" p-3 text-center">
                  <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                    {watchedlist.size}
                  </span>
                  <span className="text-sm text-blueGray-400">Watched</span>
                </div>
              </button>

              <button
                style={{
                  cursor: "pointer",
                  boxShadow: "2px 4px 8px 0 rgba(90, 44, 0, 0.5)",
                  backgroundColor: selection == "Loved" ? "#d97706" : "#f59e0b",
                  color: selection == "Loved" ? "#fffbeb" : "black",
                }}
                className="pb-2 -z-2 mx-1.5 mt-2  p-1.5 rounded-2xl listButton"
                onClick={() => selectionChange("Loved")}
              >
                <div className=" p-3 text-center -mt-1.5">
                  <span className="text-xl font-bold block uppercase tracking-wide text-blueGray-600">
                    {lovedlist.size}
                  </span>
                  <span className="text-sm text-blueGray-400">Loved</span>
                </div>
              </button>
            </div>
          </div>

          <div className="bg-amber-50 ">{selectiveRender()}</div>
        </div>
      )}
    </>
  );
}
