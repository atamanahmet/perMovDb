export default function Card({ original_title, overview, poster_path }) {
  return (
    <div className="card">
      {/* <img src="img_vatar.png" alt="Avatar" style="width:100%" /> */}
      <div className="container">
        <img src={poster_path} alt="" />
        <h4>
          <b>{original_title}</b>
        </h4>
        <p className="overview overflow-hidden h-41">{overview}</p>
      </div>
    </div>
  );
}
