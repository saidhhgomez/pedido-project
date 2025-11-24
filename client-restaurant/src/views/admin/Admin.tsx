import { useDispatch } from "react-redux";
import { logout } from "../../store/slices/auth.slice";
import { Link } from "react-router";

export default function AdminEmp() {
  const dispatch = useDispatch();

  const doLogout = () => {
    dispatch(logout());
  };

  return (
    <>
      <h1>Bienvenido Admin</h1>
      <button onClick={doLogout}>Logout</button>
      <Link to="/AdminEmp">Go hello</Link>
    </>
  );
}