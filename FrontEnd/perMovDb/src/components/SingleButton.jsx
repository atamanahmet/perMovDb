import { useNavigate } from "react-router-dom";

export default function SingleButton({ text, path, onClick }) {
  const navigate = useNavigate();
  const handleClick = () => {
    onClick(); // Call the passed down onClick prop
    navigate(path); // Navigate to the provided path
  };

  return (
    <>
      <button
        className="bg-amber-900 text-amber-50  rounded-lg text-sm py-2 px-4 me-1 trans top-buttons"
        onClick={handleClick}
      >
        {text}
      </button>
    </>
  );
}
