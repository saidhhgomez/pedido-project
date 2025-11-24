import { useSelector } from "react-redux";
import { selectRol, selectToken } from "../store/slices/auth.slice";
import AuthRoutes from "./Auth";
import AppRoutes from "./App";
import AdminRoutes from "./Admin";

export default function Navigation() {
  const token = useSelector(selectToken);
  const rol = useSelector(selectRol);


  if (token && (rol==="cliente") ) {
    return <AppRoutes />;
  }
  if (token && (rol==="repartidor") ) {
    return <AdminRoutes/>;
  }


  return <AuthRoutes />;
}