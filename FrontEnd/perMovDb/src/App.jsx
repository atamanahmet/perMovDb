import { useState, useEffect } from "react";
// import reactLogo from "./assets/react.svg";
import "./App.css";
import axios from "axios";
import MainPage from "./components/MainPage";
import Header from "./components/Header";
import DetailsPage from "./components/DetailsPage";

function App() {
  const [result, setResult] = useState();
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [page, setPage] = useState(1);

  useEffect(() => {
    axios
      .get("http://localhost:8080/page/" + page)
      .then((res) => setResult(res.data))
      .catch((err) => console.error("Backend error:", err));
  }, []);

  return (
    <>
      <Header />
      {selectedMovie ? (
        <DetailsPage item={selectedMovie} />
      ) : (
        <MainPage result={result} onCardClick={setSelectedMovie} />
      )}
    </>
  );
}

export default App;
