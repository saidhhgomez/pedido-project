import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import DeleteIcon from "@mui/icons-material/Delete";

import {
  Box,
  Typography,
  Paper,
  CircularProgress,
  Grid,
  Card,
  CardMedia,
  CardContent,
  Button,
  TextField,
  IconButton,
} from "@mui/material";

import {
  useAgregarPlato,
  useCancelarPedido,
  useDeletePedido,
  useFinalizarpedido,
  usePedidoActivoDetalle,
} from "../../services/pedido.service";
import { useGetPlateDisponible } from "../../services/plate.service";
import { useGetAllMetodosPagoActivo } from "../../services/metodoPago.service";

/* ===================== TIPOS ===================== */
type DetallePedido = {
  idCatalogo: number;
  precioUnitario: number;
  idDetalle: number;
  subtotal: number;
  nombrePlato: string;
  cantidad: number;
};

type Pedido = {
  fecha: string;
  estado: string;
  hora: string;
  detalles: DetallePedido[];
  idPedido: number;
  formaPago: string;
  nombreEmpleado: string;
};

type Plato = {
  idCatalogo: number;
  nombre: string;
  categoria: string;
  precio: number;
  stock: number;
  imagenPlatoUrl: string;
  estadoplato: boolean;
};

export default function VerPedido() {
  const { idMesa } = useParams<{ idMesa: string }>();
  const navigate = useNavigate();

  const [mostrarPlatos, setMostrarPlatos] = useState(true);
  const [formaPago, setFormaPago] = useState("");
  const [formasPago, setFormasPago] = useState<string[]>([]);

  const { mutate: cancelarPedido } = useCancelarPedido();
  const { mutate: cerrarPedidoMutate } = useFinalizarpedido();
  const { mutate: agregarPlato } = useAgregarPlato();
  const eliminarDetalle = useDeletePedido();

  const { data: pedidoData, isLoading, isError, refetch } =
    usePedidoActivoDetalle(idMesa ? Number(idMesa) : undefined);

  const { data: platosResponse, isLoading: isLoadingPlatos } =
    useGetPlateDisponible();

  const platos = platosResponse?.data ?? [];

  const { data: formasPagoResponse, isLoading: isLoadingFormas } =
    useGetAllMetodosPagoActivo();

  useEffect(() => {
    if (formasPagoResponse?.data) {
      setFormasPago(
        formasPagoResponse.data
          .filter((f: any) => f.estadoFormaPago === "activo")
          .map((f: any) => f.nombre)
      );
    }
  }, [formasPagoResponse]);

  if (isLoading || isLoadingPlatos || isLoadingFormas) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !pedidoData?.data) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Typography>No se encontró el pedido</Typography>
      </Box>
    );
  }

  const pedido = pedidoData.data;
  const detalles = pedido.detalles ?? [];

  const subtotal = detalles.reduce((sum, d) => sum + d.subtotal, 0);
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  return (
    <Box p={3} bgcolor="#f5f5f5" minHeight="100vh">
      <Box display="flex" flexDirection="column" gap={3}>

        {/* ================= PLATOS DEL PEDIDO ================= */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" fontWeight="bold" mb={2}>
            Platos del Pedido
          </Typography>

          {detalles.length === 0 ? (
            <Typography color="gray">No hay productos</Typography>
          ) : (
            detalles.map((item) => (
              <Paper key={item.idDetalle} sx={{ p: 1.5, mb: 1 }}>
                <Box display="flex" justifyContent="space-between">
                  <Typography fontWeight="bold">
                    {item.nombrePlato}
                  </Typography>
                  <IconButton
                    color="error"
                    onClick={() =>
                      eliminarDetalle.mutate(
                        {
                          idPedido: pedido.idPedido,
                          iddetalle: item.idDetalle,
                        },
                        { onSuccess: () => refetch() }
                      )
                    }
                  >
                    <DeleteIcon />
                  </IconButton>
                </Box>

                <Box display="flex" justifyContent="space-between">
                  <Typography variant="caption">
                    Cantidad: x{item.cantidad}
                  </Typography>
                  <Typography variant="caption">
                    S/. {item.precioUnitario.toFixed(2)}
                  </Typography>
                </Box>

                <Typography fontWeight="bold" align="right">
                  S/. {item.subtotal.toFixed(2)}
                </Typography>
              </Paper>
            ))
          )}
        </Paper>

        {/* ================= RESUMEN ================= */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" fontWeight="bold" mb={1}>
            Resumen del Pedido
          </Typography>

          <Typography>Empleado: {pedido.nombreEmpleado}</Typography>
          <Typography>Fecha: {pedido.fecha}</Typography>
          <Typography>Hora: {pedido.hora}</Typography>
          <Typography>Estado: {pedido.estado}</Typography>

          <Box mt={2}>
            <Typography>Subtotal: S/. {subtotal.toFixed(2)}</Typography>
            <Typography>IGV: S/. {igv.toFixed(2)}</Typography>
            <Typography fontWeight="bold">
              Total: S/. {total.toFixed(2)}
            </Typography>
          </Box>

          <Box mt={2}>
            <Typography fontWeight="bold">Forma de Pago</Typography>
            <TextField
              select
              fullWidth
              size="small"
              value={formaPago}
              onChange={(e) => setFormaPago(e.target.value)}
              SelectProps={{ native: true }}
            >
              <option value="">Selecciona</option>
              {formasPago.map((fp) => (
                <option key={fp} value={fp}>
                  {fp}
                </option>
              ))}
            </TextField>
          </Box>
        </Paper>

        {/* ================= ACCIONES ================= */}
        <Paper sx={{ p: 2 }}>
          <Button
            fullWidth
            variant="contained"
            color="success"
            sx={{ mb: 1 }}
            disabled={pedido.estado === "Cerrado"}
            onClick={() =>
              cerrarPedidoMutate(
                { id: pedido.idPedido, idmesa: Number(idMesa) },
                { onSuccess: () => navigate("/") }
              )
            }
          >
            Cerrar Pedido
          </Button>

          <Button
            fullWidth
            variant="contained"
            color="error"
            sx={{ mb: 1 }}
            disabled={pedido.estado === "Cerrado"}
            onClick={() =>
              cancelarPedido(
                { idPedido: pedido.idPedido },
                { onSuccess: () => navigate("/") }
              )
            }
          >
            Cancelar Pedido
          </Button>

          <Button fullWidth onClick={() => navigate(-1)}>
            Volver
          </Button>
        </Paper>

        {/* ================= PLATOS DISPONIBLES ================= */}
        <Paper sx={{ p: 2 }}>
          <Box display="flex" justifyContent="space-between" mb={2}>
            <Typography variant="h6" fontWeight="bold">
              Platos Disponibles
            </Typography>
            <Button size="small" onClick={() => setMostrarPlatos(!mostrarPlatos)}>
              {mostrarPlatos ? "Ocultar" : "Mostrar"}
            </Button>
          </Box>

          {mostrarPlatos &&
            platos.map((plato) => (
              <Card key={plato.idCatalogo} sx={{ mb: 2 }}>
                {plato.imagenPlatoUrl && (
                  <CardMedia
                    component="img"
                    height="160"
                    image={plato.imagenPlatoUrl}
                  />
                )}
                <CardContent>
                  <Typography fontWeight="bold">
                    {plato.nombre}
                  </Typography>
                  <Typography>
                    S/. {plato.precio.toFixed(2)}
                  </Typography>
                  <Typography
                    color={plato.stock > 0 ? "green" : "error"}
                  >
                    {plato.stock > 0
                      ? `Stock: ${plato.stock}`
                      : "Agotado"}
                  </Typography>

                  <Button
                    fullWidth
                    variant="contained"
                    sx={{ mt: 1 }}
                    disabled={plato.stock === 0}
                    onClick={() =>
                      agregarPlato(
                        {
                          idPedido: pedido.idPedido,
                          detalles: [
                            { idCatalogo: plato.idCatalogo, cantidad: 1 },
                          ],
                        },
                        { onSuccess: () => refetch() }
                      )
                    }
                  >
                    Agregar
                  </Button>
                </CardContent>
              </Card>
            ))}
        </Paper>

      </Box>
    </Box>
  );
}
