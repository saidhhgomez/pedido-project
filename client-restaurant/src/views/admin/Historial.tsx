import React, { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  Box,
  Typography,
  Button,
  Card,
  CardContent,
  Divider,
  TextField,
  CircularProgress,
} from "@mui/material";

export default function Historial() {
  const [idUsuario, setIdUsuario] = useState<number | "">("");

  // 1️⃣ Traer todos los pedidos automáticamente
  const { data: pedidos, isLoading: loadingAll } = useQuery(["historial"], getHistorial);

  // 2️⃣ Buscar pedido por ID usando useQuery
  const {
    data: pedido,
    isLoading: loadingId,
    refetch: buscarPorId,
  } = useQuery(
    ["pedido", idUsuario],
    () => getPedidoById(Number(idUsuario)),
    {
      enabled: false, // no se ejecuta automáticamente
      retry: false,   // no reintenta automáticamente
    }
  );

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Historial de Pedidos
      </Typography>

      {/* Input y botón para buscar por ID */}
      <Box sx={{ display: "flex", gap: 2, mb: 3 }}>
        <TextField
          label="Buscar Pedido por ID"
          value={idUsuario}
          onChange={(e) => setIdUsuario(e.target.value === "" ? "" : Number(e.target.value))}
          type="number"
        />
        <Button
          variant="contained"
          color="primary"
          onClick={() => buscarPorId()} // dispara la query
        >
          Buscar
        </Button>
      </Box>

      {/* Resultado de la búsqueda */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h5" gutterBottom>
          Resultado de la búsqueda
        </Typography>
        {loadingId ? (
          <CircularProgress />
        ) : pedido ? (
          <Card variant="outlined">
            <CardContent>
              <Typography variant="subtitle1">Pedido ID: {pedido.id}</Typography>
              <Divider sx={{ my: 1 }} />
              <Typography variant="body2">Fecha: {pedido.fecha}</Typography>
              <Typography variant="body2">Total: S/ {pedido.total}</Typography>
            </CardContent>
          </Card>
        ) : (
          <Typography variant="body2">No se ha buscado ningún pedido aún.</Typography>
        )}
      </Box>

      {/* Historial general */}
      <Typography variant="h5" gutterBottom>
        Todos los Pedidos
      </Typography>
      {loadingAll ? (
        <CircularProgress />
      ) : (
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
          {pedidos?.map((p: any) => (
            <Card key={p.id} variant="outlined">
              <CardContent>
                <Typography variant="subtitle1">Pedido #{p.id}</Typography>
                <Divider sx={{ my: 1 }} />
                <Typography variant="body2">Fecha: {p.fecha}</Typography>
                <Typography variant="body2">Total: S/ {p.total}</Typography>
              </CardContent>
            </Card>
          ))}
        </Box>
      )}
    </Box>
  );
}
