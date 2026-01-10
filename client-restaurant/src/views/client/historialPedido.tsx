import {
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  Divider,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Stack,
  CircularProgress,
  Alert,
} from "@mui/material";
import { useGetHistorialCliente } from "../../services/historial.service";
import { useSelector } from "react-redux";
import { selectPerfilCliente } from "../../store/slices/auth.slice";

/* =======================
   TIPOS
======================= */
interface DetallePedido {
  idDetalle: number;
  idCatalogo: number;
  nombrePlato: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

interface Pedido {
  idPedido: number;
  idSucursal: number;
  fecha: string;
  hora: string;
  estado: string;
  direccion: string;
  formaPago: string;
  detalles: DetallePedido[];
}

/* =======================
   PROPS
======================= */

/* =======================
   COMPONENTE
======================= */
export default function HistorialPedidosCliente() {

    const idCliente=useSelector(selectPerfilCliente);
  const { data, isLoading, isError } = useGetHistorialCliente(idCliente?.idCliente);

  const pedidos: Pedido[] = data?.data ?? [];

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError) {
    return (
      <Alert severity="error">
        Error al cargar el historial de pedidos
      </Alert>
    );
  }

  if (pedidos.length === 0) {
    return (
      <Typography color="text.secondary" mt={3}>
        No tienes pedidos registrados
      </Typography>
    );
  }

  return (
    <Box p={3} display="flex" flexDirection="column" gap={3}>
      <Typography variant="h5" fontWeight="bold">
        📦 Historial de Pedidos
      </Typography>

      {pedidos.map((pedido) => {
        const total = pedido.detalles.reduce(
          (acc, d) => acc + d.subtotal,
          0
        );

        return (
          <Card key={pedido.idPedido} elevation={4}>
            <CardContent>
              {/* HEADER */}
              <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                mb={1}
              >
                <Typography variant="h6" fontWeight="bold">
                  Pedido #{pedido.idPedido}
                </Typography>

                <Chip
                  label={pedido.estado.toUpperCase()}
                  color={
                    pedido.estado === "pendiente"
                      ? "warning"
                      : pedido.estado === "cancelado"
                      ? "error"
                      : "success"
                  }
                />
              </Stack>

              {/* INFO */}
              <Typography variant="body2">
                📅 {pedido.fecha} — ⏰ {pedido.hora}
              </Typography>
              <Typography variant="body2">
                🏢 Sucursal: {pedido.idSucursal}
              </Typography>
              <Typography variant="body2">
                📍 Dirección: {pedido.direccion}
              </Typography>
              <Typography variant="body2">
                💳 Pago: {pedido.formaPago}
              </Typography>

              <Divider sx={{ my: 2 }} />

              {/* DETALLES */}
              <Typography fontWeight="bold" mb={1}>
                Detalle del Pedido
              </Typography>

              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Plato</TableCell>
                    <TableCell align="center">Cantidad</TableCell>
                    <TableCell align="right">Precio</TableCell>
                    <TableCell align="right">Subtotal</TableCell>
                  </TableRow>
                </TableHead>

                <TableBody>
                  {pedido.detalles.map((d) => (
                    <TableRow key={d.idDetalle}>
                      <TableCell>{d.nombrePlato}</TableCell>
                      <TableCell align="center">{d.cantidad}</TableCell>
                      <TableCell align="right">
                        S/ {d.precioUnitario.toFixed(2)}
                      </TableCell>
                      <TableCell align="right">
                        S/ {d.subtotal.toFixed(2)}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>

              <Divider sx={{ my: 2 }} />

              {/* TOTAL */}
              <Box display="flex" justifyContent="flex-end">
                <Typography variant="h6" fontWeight="bold">
                  Total: S/ {total.toFixed(2)}
                </Typography>
              </Box>
            </CardContent>
          </Card>
        );
      })}
    </Box>
  );
}
