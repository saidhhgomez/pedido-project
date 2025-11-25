import { useSelector } from "react-redux";
import { /*selectRol, */selectToken,selectTipoId } from "../store/slices/auth.slice";
import AuthRoutes from "./Auth";
import AppRoutes from "./App";
import AdminRoutes from "./Admin";

export default function Navigation() {
  const token = useSelector(selectToken);
  /* const rol = useSelector(selectRol); */
  const tipo= useSelector(selectTipoId);


  if (token && (tipo==="cliente") ) {
    return <AppRoutes />;
  }
  if (token && (tipo==="empleado")) {
    return <AdminRoutes/>;
  }


  return <AuthRoutes />;
}