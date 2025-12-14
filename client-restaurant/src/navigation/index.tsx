import { useDispatch, useSelector } from "react-redux";
import { selectIdCliente, selectIdEmpleado ,usarComoCliente,usarComoAdmin} from "../store/slices/auth.slice";
import AuthRoutes from "./Auth";
import AppRoutes from "./App";
import AdminRoutes from "./Admin";
import { Box, Button, Typography } from "@mui/material";

export default function Navigation() {
  const idCliente = useSelector(selectIdCliente);
  const idEmpleado = useSelector(selectIdEmpleado);
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
  onClick={() => dispatch(usarComoAdmin())}
        >
          Admin
        </Button>
      </Box>
    );
  }

  if ((idCliente != null && !idEmpleado)) {
    return <AppRoutes />;
  }

  if ((idEmpleado != null && !idCliente)) {
    return <AdminRoutes />;
  }

  return <AuthRoutes />;
  

}
