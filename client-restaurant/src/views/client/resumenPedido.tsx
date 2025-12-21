import {
  Box,
  Typography,
  MenuItem,
  Select,
  Button,
  Paper,
  IconButton,
  type SelectChangeEvent,
} from "@mui/material";
import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  selectCart,
  addItem,
  decreaseItem,
  removeItem,
  clearCart,
} from "../../store/slices/cart.slice";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import DeleteIcon from "@mui/icons-material/Delete";
import FormModal from "../../components/Modals/ModalDirection";
import { useNavigate } from "react-router";
import {
  useCreateDirection,
  useGetDirecciones,
} from "../../services/direction.service";
import {useGetAllMetodosPagoActivo } from "../../services/metodoPago.service";
import Swal from "sweetalert2";
import { selectPerfilCliente } from "../../store/slices/auth.slice";
import { useCrearPedidoOnline } from "../../services/pedido.service";
import type { DireccionForm } from "../../types/direction.type";
import { useGetAllSucursalesActivas } from "../../services/sucursales.service";

export default function ResumenPedido() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const cart = useSelector(selectCart);
  const idCliente = useSelector(selectPerfilCliente);

  const [open, setOpen] = useState(false);
  const [direccionSeleccionada, setDireccionSeleccionada] = useState<number | "">("");
  const [metodoSeleccionado, setMetodoSeleccionado] = useState<number | "">("");
  const [sucursalSeleccionada, setSucursalSeleccionada] = useState<number | "">("");

  /* ===================== API ===================== */
  const { mutate: enviarPedido } = useCrearPedidoOnline();
  const { mutate: crearDireccion } = useCreateDirection();

  const { data: direcciones, isLoading, refetch } = useGetDirecciones(
    idCliente?.idCliente ?? 0
  );
  const direccionesArray = direcciones?.data?.data ?? [];

  const { data: metodosPago, isLoading: isLoadingMetodos } =
    useGetAllMetodosPagoActivo();
  const metodosPagoArray = metodosPago?.data ?? [];

  const { data: sucursales, isLoading: isLoadingSucursales } =
    useGetAllSucursalesActivas();
  const sucursalesArray = sucursales?.data ?? [];

  /* ===================== HANDLERS ===================== */

  const handleFinalizarCompra = () => {
    if (
      cart.length === 0 ||
      !direccionSeleccionada ||
      !metodoSeleccionado ||
      !sucursalSeleccionada
    ) {
      Swal.fire("Completa todos los datos", "", "warning");
      return;
    }

    const payload = {
      idCliente: idCliente?.idCliente,
      idDireccion: direccionSeleccionada,
      idFormaPago: metodoSeleccionado,
      idSucursal: sucursalSeleccionada,
      detalles: cart.map((item) => ({
        idCatalogo: item.idProducto,
        cantidad: item.cantidad,
      })),
    };

    enviarPedido(payload, {
      onSuccess: (res) => {
        Swal.fire("Pedido creado", res.data?.message, "success");
        dispatch(clearCart());
        navigate("/");
      },
      onError: () => {
        Swal.fire("Error", "No se pudo crear el pedido", "error");
      },
    });
  };

  const handleSubmitDireccion = (data: DireccionForm) => {
    if (!idCliente?.idCliente) return;

    crearDireccion(
      {
        idCliente: idCliente.idCliente,
        ...data,
      },
      {
        onSuccess: (res) => {
          refetch();
          setOpen(false);
          Swal.fire("Dirección agregada", res.data.message, "success");
        },
      }
    );
  };

  const subtotal = cart.reduce(
    (sum, item) => sum + item.precioUnitario * item.cantidad,
    0
  );
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  /* ===================== UI ===================== */

  return (
    <Box
      display="grid"
      gridTemplateColumns="2.5fr 3fr 2fr"
      gap={3}
      p={3}
      minHeight="100vh"
      bgcolor="#f5f5f5"
    >
      {/* ===================== CARRITO ===================== */}
      <Paper sx={{ p: 2 }}>
        <Typography variant="h6" fontWeight="bold" mb={2}>
          Carrito de Compras
        </Typography>

        {cart.length === 0 && (
          <Typography color="gray">No hay productos</Typography>
        )}

        {cart.map((item) => (
          <Paper key={item.idProducto} sx={{ p: 1.5, mb: 1 }}>
            <Box display="flex" justifyContent="space-between">
              <Typography fontWeight="bold">{item.nombre}</Typography>
              <IconButton
                color="error"
                onClick={() => dispatch(removeItem(item.idProducto))}
              >
                <DeleteIcon />
              </IconButton>
            </Box>

            <Box display="flex" justifyContent="space-between" mt={1}>
              <Box display="flex" gap={1}>
                <IconButton
                  onClick={() => dispatch(decreaseItem(item.idProducto))}
                >
                  <RemoveIcon />
                </IconButton>
                <Typography>{item.cantidad}</Typography>
                <IconButton
                  onClick={() => dispatch(addItem({ ...item, cantidad: 1 }))}
                >
                  <AddIcon />
                </IconButton>
              </Box>

              <Typography fontWeight="bold">
                S/. {(item.precioUnitario * item.cantidad).toFixed(2)}
              </Typography>
            </Box>
          </Paper>
        ))}
      </Paper>

      {/* ===================== CENTRO ===================== */}
      <Box display="flex" flexDirection="column" gap={3}>
        {/* Dirección */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" mb={2}>
            Dirección de Entrega
          </Typography>

          {isLoading ? (
            <Typography>Cargando...</Typography>
          ) : direccionesArray.length > 0 ? (
            <Select
              fullWidth
              value={direccionSeleccionada}
              onChange={(e) => setDireccionSeleccionada(Number(e.target.value))}
            >
              <MenuItem value="">-- Selecciona --</MenuItem>
              {direccionesArray.map((dir) => (
                <MenuItem key={dir.idDireccion} value={dir.idDireccion}>
                  {dir.direccion} - {dir.distrito}
                </MenuItem>
              ))}
            </Select>
          ) : (
            <Button
              fullWidth
              variant="contained"
              color="warning"
              onClick={() => setOpen(true)}
            >
              Agregar Dirección
            </Button>
          )}
        </Paper>

        {/* Sucursal */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" mb={2}>
            Sucursal
          </Typography>

          {isLoadingSucursales ? (
            <Typography>Cargando...</Typography>
          ) : (
            <Select
              fullWidth
              value={sucursalSeleccionada}
              onChange={(e) =>
                setSucursalSeleccionada(Number(e.target.value))
              }
            >
              <MenuItem value="">-- Selecciona --</MenuItem>
              {sucursalesArray.map((s) => (
                <MenuItem key={s.idSucursal} value={s.idSucursal}>
                  {s.nombre}
                </MenuItem>
              ))}
            </Select>
          )}
        </Paper>

        {/* Método de Pago */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" mb={2}>
            Método de Pago
          </Typography>

          {isLoadingMetodos ? (
            <Typography>Cargando...</Typography>
          ) : (
            <Select
              fullWidth
              value={metodoSeleccionado}
              onChange={(e) => setMetodoSeleccionado(Number(e.target.value))}
            >
              <MenuItem value="">-- Selecciona --</MenuItem>
              {metodosPagoArray.map((mp) => (
                <MenuItem key={mp.idFormaPago} value={mp.idFormaPago}>
                  {mp.nombre}
                </MenuItem>
              ))}
            </Select>
          )}
        </Paper>
      </Box>

      {/* ===================== RESUMEN ===================== */}
      <Paper sx={{ p: 2, position: "sticky", top: 20, height: "fit-content" }}>
        <Typography variant="h6" mb={2}>
          Resumen del Pedido
        </Typography>

        <Box display="flex" justifyContent="space-between">
          <Typography>Subtotal</Typography>
          <Typography>S/. {subtotal.toFixed(2)}</Typography>
        </Box>

        <Box display="flex" justifyContent="space-between">
          <Typography>IGV (18%)</Typography>
          <Typography>S/. {igv.toFixed(2)}</Typography>
        </Box>

        <Box
          display="flex"
          justifyContent="space-between"
          fontWeight="bold"
          mt={1}
        >
          <Typography>Total</Typography>
          <Typography>S/. {total.toFixed(2)}</Typography>
        </Box>

        <Button
          fullWidth
          variant="contained"
          sx={{ mt: 3 }}
          onClick={handleFinalizarCompra}
          disabled={
            cart.length === 0 ||
            !direccionSeleccionada ||
            !metodoSeleccionado ||
            !sucursalSeleccionada
          }
        >
          Confirmar Pedido
        </Button>
      </Paper>

      {/* ===================== MODAL ===================== */}
      <FormModal
        open={open}
        onClose={() => setOpen(false)}
        onSubmit={handleSubmitDireccion}
      />
    </Box>
  );
}
