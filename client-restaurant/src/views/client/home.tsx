import { useDispatch } from "react-redux";
import { logout, selectNombre } from "../../store/slices/auth.slice";
import { Link } from "react-router";
import { useSelector } from "react-redux";



export default function Home() {
  const nombre = useSelector(selectNombre);

  const dispatch = useDispatch();

  const doLogout = () => {
    dispatch(logout());
  };

  return (
    <>
      <h1>Bienvenido Cliente :${nombre}</h1>
      <button onClick={doLogout}>Logout</button>
      <Link to="/hello">Go hello</Link>
    </>
  );
}