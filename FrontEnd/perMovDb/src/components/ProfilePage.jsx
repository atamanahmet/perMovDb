import { useUser } from "../context/UserContext";
export default function ProfilePage() {
  const { user, logout } = useUser();

  return (
    <>
      <div>
        <h1>{user}</h1>
        <button
          className="w-10 h-10 bg-amber-700 text-amber-100"
          onClick={logout}
        >
          {user ? user + " Log Out" : "No User"}
        </button>
      </div>
    </>
  );
}
