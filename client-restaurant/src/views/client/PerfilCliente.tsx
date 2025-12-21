import React from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
  Stack,
  CircularProgress,
} from "@mui/material";
import { useSelector } from "react-redux";
import { selectPerfilCliente } from "../../store/slices/auth.slice";
import { useGetPerfilClienteId } from "../../services/perfil.service";

export default function PerfilCliente() {

  // 👉 ID del cliente desde Redux
  const clienteStore = useSelector(selectPerfilCliente);
  const { data: cliente, isLoading, isError } =
    useGetPerfilClienteId(clienteStore?.idCliente);

  /* ================== ESTADOS ================== */
  if (isLoading)
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );

  if (isError)
    return (
      <Typography color="error" variant="h6">
        Error al cargar el perfil del cliente
      </Typography>
    );

  if (!cliente)
    return (
      <Typography variant="h6">
        No se encontró el cliente
      </Typography>
    );

  const data = cliente.data;

  /* ================== UI ================== */
  return (
    <Box p={3}>
      <Card sx={{ display: "flex", boxShadow: 3 }}>
        <CardMedia
          component="img"
          sx={{ width: 220 }}
          image={data.imagenUrl}
          alt={data.nombres}
        />

        <CardContent>
          <Stack spacing={1.2}>
            <Typography variant="h4" fontWeight="bold">
              {data.nombres} {data.apPaterno} {data.apMaterno}
            </Typography>

            <Typography variant="body1">
              <strong>Categoría:</strong>{" "}
              <span style={{ color: data.categoria === "regular" ? "green" : "orange" }}>
                {data.categoria}
              </span>
            </Typography>

            <Typography variant="body1">
              <strong>Documento:</strong> {data.tipoDocumento} - {data.numDocumento}
            </Typography>

            <Typography variant="body1">
              <strong>Género:</strong> {data.genero === "M" ? "Masculino" : "Femenino"}
            </Typography>

            <Typography variant="body1">
              <strong>Correo:</strong> {data.correo}
            </Typography>

            <Typography variant="body1">
              <strong>Teléfono:</strong> {data.telefono}
            </Typography>

            <Typography variant="body1">
              <strong>Fecha de nacimiento:</strong> {data.fechaNacimiento}
            </Typography>

            <Typography variant="body2" color="text.secondary">
              <strong>Registrado el:</strong> {data.fechaRegistro}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}
