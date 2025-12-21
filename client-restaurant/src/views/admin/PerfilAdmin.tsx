import React from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
  Link,
  Stack,
  Grow,
  CircularProgress,
} from "@mui/material";
import { useGetPerfilEmpleadoId } from "../../services/perfil.service";
import { useSelector } from "react-redux";
import { selectPerfilEmpleado } from "../../store/slices/auth.slice";

export default function PerfilAdmin() {
  // Datos del empleado (ejemplo)


    // Obtener idEmpleado desde el store
  const idEmpleado = useSelector(selectPerfilEmpleado);
  const { data: empleado, isLoading, isError } = useGetPerfilEmpleadoId(idEmpleado?.idEmpleado);

  // Loading centrado
  if (isLoading)
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );

  if (isError)
    return (
      <Typography color="error" variant="h6">
        Error al cargar el perfil
      </Typography>
    );

  if (!empleado)
    return (
      <Typography variant="h6">
        No se encontró el empleado
      </Typography>
    );


  return (
    <Box p={3}>
      {/* Perfil principal */}
      <Card sx={{ display: "flex", mb: 5, boxShadow: 3 }}>
        <CardMedia
          component="img"
          sx={{ width: 200 }}
          image={empleado.data.fotoUrl}
          alt={empleado.data.nombres}
        />
        <CardContent>
          <Stack spacing={1}>
            <Typography variant="h4">{empleado.data.nombres}</Typography>

            <Typography variant="body1">
              <strong>Estado:</strong>{" "}
              <span style={{ color: empleado.data.estado === "activo" ? "green" : "red" }}>
                {empleado.data.estado}
              </span>
            </Typography>
            <Typography variant="body1">
              <strong>Correo:</strong> {empleado.data.correo}
            </Typography>
            <Typography variant="body1">
              <strong>Teléfono:</strong> {empleado.data.telefono}
            </Typography>
          </Stack>
        </CardContent>
      </Card>

      {/* Historial de contratos */}
      <Typography variant="h5" gutterBottom>
        Historial de Contratos
      </Typography>
      <Box
        sx={{
          display: "flex",
          overflowX: "auto",
          gap: 2,
          py: 2,
          px: 1,
        }}
      >
        {empleado.data.historialContratos.map((contrato, index) => (
          <Grow
            in={true}
            style={{ transformOrigin: "0 0 0" }}
            {...{ timeout: 300 + index * 200 }}
            key={contrato.idContrato}
          >
            <Card
              sx={{
                minWidth: 250,
                maxWidth: 300,
                boxShadow: 3,
                flexShrink: 0,
                "&:hover": { transform: "scale(1.03)", transition: "0.3s" },
              }}
            >
              <CardMedia
                component="img"
                height="120"
                image={contrato.fotoUrl}
                alt={contrato.tipo}
              />
              <CardContent>
                <Stack spacing={0.5}>
                  <Typography variant="subtitle1" fontWeight="bold">
                    {contrato.tipo}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Rol:</strong> {contrato.rol}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Sucursal:</strong> {contrato.sucursal}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Estado:</strong>{" "}
                    <span
                      style={{ color: contrato.estado === "activo" ? "green" : "red" }}
                    >
                      {contrato.estado}
                    </span>
                  </Typography>
                  <Typography variant="body2">
                    <strong>Inicio:</strong> {contrato.fechaInicio}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Fin:</strong> {contrato.fechaFin}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Salario:</strong> S/ {contrato.salario.toFixed(2)}
                  </Typography>
                  <Link
                    href={contrato.pdfDownloadUrl}
                    target="_blank"
                    underline="hover"
                  >
                    Ver PDF
                  </Link>
                </Stack>
              </CardContent>
            </Card>
          </Grow>
        ))}
      </Box>
    </Box>
  );
}
