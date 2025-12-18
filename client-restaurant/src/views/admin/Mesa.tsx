import {
  Card,
  CardContent,
  Typography,
  Button,
  Box,
  CircularProgress,
} from "@mui/material";
import { Link } from "react-router";

import { useGetAllSucursales } from "../../services/sucursales.service";

export default function Mesa() {
  const { data: sucursales, isLoading, error } = useGetAllSucursales();

  if (isLoading) return <CircularProgress />;

  if (error) {
    return (
      <Typography color="error">
        Error al cargar sucursales
      </Typography>
    );
  }

  return (
    <>
      {/* Contenedor horizontal */}
      <Box
        sx={{
          display: "flex",
          gap: 2,
          flexWrap: "wrap", // 👈 baja a otra fila si no entra
        }}
      >
        {sucursales?.data?.map((sucursal) => (
          <Card key={sucursal.idSucursal} sx={{ width: 300 }}>
            <CardContent>
              <Typography variant="h6">
                {sucursal.nombre}
              </Typography>

              <Typography>
                {sucursal.direccion}
              </Typography>

              <Typography>
                {sucursal.telefono}
              </Typography>

              <Typography>
                {sucursal.estado}
              </Typography>

              <Button
                variant="contained"
                component={Link}
                to={`/mesaSucursales`}
  onClick={() =>
    localStorage.setItem("idSucursal", sucursal.idSucursal.toString())
  }
                sx={{ mt: 2 }}
                fullWidth
              >
                MesaS
              </Button>
            </CardContent>
          </Card>
        ))}
      </Box>
    </>
  );
}
