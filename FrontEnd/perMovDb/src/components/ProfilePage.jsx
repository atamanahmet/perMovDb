import { useUser } from "../context/UserContext";
export default function ProfilePage() {
  // console.log(user.username);
  // console.log(user.token);

  const { user, logout } = useUser();

  return (
    <>
      <div>
        <h1>{user}</h1>
        <button
          className="w-10 h-10 bg-amber-700 text-amber-100"
          onClick={logout}
        >
          {user ? user : "No User"}
        </button>
        {/* <h2>{user.token}</h2> */}
        {/* <h1>{user.token}</h1> */}
      </div>
    </>
  );
}
