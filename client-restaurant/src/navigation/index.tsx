import { useDispatch, useSelector } from "react-redux";
import {selectPerfilCliente, selectPerfilEmpleado, selectUsuario, usarComoCliente, usarComoEmpleado,} from "../store/slices/auth.slice";
import AuthRoutes from "./Auth";
import AppRoutes from "./App";
import AdminRoutes from "./Admin";
import MeseroRoutes from "./Mesero";

import { Box, Button, Typography } from "@mui/material";

export default function Navigation() {
  const idCliente = useSelector(selectPerfilCliente);
  const idEmpleado = useSelector(selectPerfilEmpleado);
  const usuario=useSelector(selectUsuario);

const dispatch = useDispatch();


  if (idCliente != null && idEmpleado != null ) {
    return (
      <Box sx={{ textAlign: "center", mt: 10 }}>
        <Typography variant="h6" sx={{ mb: 2 }}>
          Tienes ambos roles, selecciona cómo quieres ingresar:
        </Typography>
        <Button
          variant="contained"
          sx={{ mr: 2 }}
  onClick={() => dispatch(usarComoCliente())}
        > 
        Cliente        
        </Button>
        <Button variant="contained" 
  onClick={() => dispatch(usarComoEmpleado())}
        >
          {usuario?.rolPrincipal}
        </Button>
      </Box>
    );
  }



  if ((idEmpleado != null && usuario?.rolPrincipal.toLowerCase()==="mesero" && idEmpleado.estado=== "activo" )) {
    return <MeseroRoutes />;
  }



  if ((idCliente != null && !idEmpleado)) {
    return <AppRoutes />;
  }



  if ((idEmpleado != null && usuario?.rolPrincipal.toLowerCase()==="admin" && idEmpleado.estado=== "activo")) {
    return <AdminRoutes />;
  }




  return <AuthRoutes />;
  

}
