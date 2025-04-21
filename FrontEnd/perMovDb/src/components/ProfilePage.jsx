export default function ProfilePage({ user }) {
  // console.log(user.username);
  // console.log(user.token);

  return (
    <>
      <div>
        <h1>{user.username}</h1>
        <h2>{user.token}</h2>
        {/* <h1>{user.token}</h1> */}
      </div>
    </>
  );
}
