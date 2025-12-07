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
import { selectIdCliente } from "../../store/slices/auth.slice";
import { selectCart, addItem, decreaseItem, removeItem,clearCart } from "../../store/slices/cart.slice";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";
import DeleteIcon from "@mui/icons-material/Delete";
import FormModal from "../../components/Modals/ModalDirection";
import type { DireccionForm } from "../../types/direction.type";
import { useNavigate } from "react-router";
import { useCreateDirection, useGetDirecciones } from "../../services/direction.service";
import { useGetAllMetodosPago } from "../../services/metodoPago.service";
import { useCrearPedido } from "../../services/pedido.service";
import Swal from "sweetalert2";

export default function ResumenPedido() {
  const navigate = useNavigate();
  const idCliente = useSelector(selectIdCliente);
  const cart = useSelector(selectCart);
  const dispatch = useDispatch();

  const { mutate: enviarPedido } = useCrearPedido();

  const handleFinalizarCompra = () => {
    if (cart.length === 0 || !direccionSeleccionada || !metodoSeleccionado) {
      alert("Debes tener productos, seleccionar una dirección y un método de pago");
      return;
    }

    const payload = {
      idCliente,
      idDireccion: direccionSeleccionada,
      idEmpleado: 0,
      idMesa: 0,
      idFormaPago: metodoSeleccionado,
      estado: "PENDIENTE",
      detalles: cart.map((item) => ({
        idProducto: item.idProducto,
        cantidad: item.cantidad,
        precioUnitario: item.precioUnitario,
        subtotal: item.cantidad * item.precioUnitario,
        igv: item.cantidad * item.precioUnitario * 0.18,
        total: item.cantidad * item.precioUnitario * 1.18,
      })),
    };

    enviarPedido(payload, {
      onSuccess: () => {
        Swal.fire({
  position: "center",
  icon: "success",
  title: "Pedido Exitoso",
  showConfirmButton: false,
  timer: 1200
});
        dispatch(clearCart());
        navigate("/");
      },
      onError: () => {
        Swal.fire({
  position: "center",
  icon: "error",
  title: "Pedido Fallido",
  showConfirmButton: false,
  timer: 1200
});      },
    });
  };

  const [open, setOpen] = useState(false);
  const [direccionSeleccionada, setDireccionSeleccionada] = useState<number | "">("");
  const [metodoSeleccionado, setMetodoSeleccionado] = useState<number | "">("");

  const { mutate } = useCreateDirection();
  const { data: direcciones, isLoading, refetch } = useGetDirecciones(idCliente ?? 0);
  const direccionesArray = direcciones?.data?.data ?? [];

  const { data: metodosPago, isLoading: isLoadingMetodos } = useGetAllMetodosPago();
  const metodosPagoArray = metodosPago?.data ?? [];

  const handleChangeDireccion = (event: SelectChangeEvent<string>) => {
    const selectedId = Number(event.target.value);
    setDireccionSeleccionada(selectedId);
  };

  const handleSubmitDireccion = (data: DireccionForm) => {
    mutate(
      { ...data, idCliente },
      {
        onSuccess: () => {
          refetch();
          setOpen(false);
        Swal.fire({
  position: "center",
  icon: "success",
  title: "Direccion Registrada",
  showConfirmButton: false,
  timer: 1200
});
        },
      }
    );
  };

  const subtotal = cart.reduce((sum, item) => sum + item.precioUnitario * item.cantidad, 0);
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  return (
    <Box
      display="grid"
      gridTemplateColumns="repeat(6, 1fr)"
      gridTemplateRows="repeat(7, 1fr)"
      gap="8px"
      height="100vh"
      p={2}
    >
      {/* Carrito */}
      <Box color="black" p={2} gridColumn="span 2" gridRow="span 7" overflow="auto">
        <Typography variant="h6" sx={{ mb: 2, fontWeight: "bold" }}>
          Carrito de Compras
        </Typography>

        {cart.length === 0 && (
          <Typography sx={{ color: "gray" }}>No hay productos en el carrito</Typography>
        )}

        {cart.map((item) => (
          <Paper key={item.idProducto} sx={{ p: 1, mb: 1 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography>{item.nombre}</Typography>
              <IconButton
                size="small"
                color="error"
                onClick={() => dispatch(removeItem(item.idProducto))}
              >
                <DeleteIcon fontSize="small" />
              </IconButton>
            </Box>

            <Box display="flex" alignItems="center" justifyContent="space-between" mt={1}>
              <Box display="flex" alignItems="center" gap={1}>
                <IconButton
                  size="small"
                  onClick={() => dispatch(decreaseItem(item.idProducto))}
                  color="secondary"
                >
                  <RemoveIcon fontSize="small" />
                </IconButton>

                <Typography>{item.cantidad}</Typography>

                <IconButton
                  size="small"
                  onClick={() => dispatch(addItem({ ...item, cantidad: 1 }))}
                  color="secondary"
                >
                  <AddIcon fontSize="small" />
                </IconButton>
              </Box>

              <Typography>S/. {(item.precioUnitario * item.cantidad).toFixed(2)}</Typography>
            </Box>
          </Paper>
        ))}
      </Box>

      {/* Dirección */}
      <Box
        color="black"
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="flex-start"
        gridColumn="3 / span 2"
        gridRow="1 / span 3"
        p={2}
      >
        <Typography variant="h6" sx={{ mb: 2 }}>
          Selecciona tu dirección
        </Typography>

        {isLoading ? (
          <Typography>Cargando direcciones...</Typography>
        ) : direccionesArray.length > 0 ? (
          <Select
            value={direccionSeleccionada}
            onChange={handleChangeDireccion}
            sx={{ width: "100%", backgroundColor: "white", color: "black" }}
          >
            <MenuItem value="">-- Selecciona --</MenuItem>
            {direccionesArray.map((dir) => (
              <MenuItem key={dir.idDireccion} value={dir.idDireccion}>
                {dir.direccion}, {dir.distrito}
              </MenuItem>
            ))}
          </Select>
        ) : (
          <Box display="flex" flexDirection="column" alignItems="center" gap={1}>
            <Typography color="yellow" sx={{ textAlign: "center" }}>
              No tienes direcciones registradas.
            </Typography>
            <Button variant="contained" color="warning" onClick={() => setOpen(true)}>
              Agregar Dirección
            </Button>
          </Box>
        )}
      </Box>

      {/* Método de pago */}
      <Box
        color="black"
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="flex-start"
        gridColumn="3 / span 2"
        gridRow="4 / span 4"
        p={2}
        gap={2}
      >
        <Typography variant="h6" sx={{ mb: 2 }}>
          Selecciona tu método de pago
        </Typography>

        {isLoadingMetodos ? (
          <Typography>Cargando métodos de pago...</Typography>
        ) : metodosPagoArray.length > 0 ? (
          <Select
            value={metodoSeleccionado}
            onChange={(e) => setMetodoSeleccionado(Number(e.target.value))}
            sx={{ width: "100%", backgroundColor: "white", color: "black" }}
          >
            <MenuItem value="">-- Selecciona --</MenuItem>
            {metodosPagoArray.map((mp) => (
              <MenuItem key={mp.idFormaPago} value={mp.idFormaPago}>
                {mp.nombre}
              </MenuItem>
            ))}
          </Select>
        ) : (
          <Typography color="yellow">No tienes métodos de pago registrados.</Typography>
        )}
      </Box>

      {/* Resumen */}
      <Box
        color="black"
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="flex-start"
        gridColumn="5 / span 2"
        gridRow="1 / span 7"
        p={2}
        gap={2}
      >
        <Typography variant="h6" sx={{ mb: 2 }}>
          Resumen de Pedido
        </Typography>

        <Box display="flex" justifyContent="space-between" width="100%">
          <Typography>Subtotal:</Typography>
          <Typography>S/. {subtotal.toFixed(2)}</Typography>
        </Box>

        <Box display="flex" justifyContent="space-between" width="100%">
          <Typography>IGV (18%):</Typography>
          <Typography>S/. {igv.toFixed(2)}</Typography>
        </Box>

        <Box display="flex" justifyContent="space-between" width="100%" fontWeight="bold">
          <Typography>Total:</Typography>
          <Typography>S/. {total.toFixed(2)}</Typography>
        </Box>

        <Button
          variant="contained"
          color="primary"
          fullWidth
          sx={{ mt: 2 }}
          onClick={handleFinalizarCompra}
          disabled={cart.length === 0 || !direccionSeleccionada || !metodoSeleccionado}
        >
          Confirmar Pedido
        </Button>
      </Box>

      {/* Modal Dirección */}
      <FormModal open={open} onClose={() => setOpen(false)} onSubmit={handleSubmitDireccion} />
    </Box>
  );
}
