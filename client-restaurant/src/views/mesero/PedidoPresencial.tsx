import {
  Box,
  Button,
  Typography,
  Card,
  CardContent,
  CardMedia,
  Grid,
  Chip,
  Divider,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import { useGetPlateDisponible } from "../../services/plate.service";
import type { Plate } from "../../types/Plate.type";
import { useDispatch, useSelector } from "react-redux";
import {
  addItem,
  decreaseItem,
  clearCart,
  selectCart,
} from "../../store/slices/cart.slice";
import { useNavigate, useParams } from "react-router";
import { selectPerfilEmpleado, selectUsuario } from "../../store/slices/auth.slice";
import { useGetAllMetodosPagoActivo } from "../../services/metodoPago.service";
import { useForm, Controller } from "react-hook-form";
import { useCrearPedidoPresencial } from "../../services/pedido.service";
import Swal from "sweetalert2";

// -------------------- FORM INTERFACE --------------------
interface PedidoForm {
  idFormaPago: number;
}

export default function PedidoPresencial() {
  const { idMesa } = useParams<{ idMesa: string }>();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const usuario = useSelector(selectUsuario);
  const cart = useSelector(selectCart);
    const empleado = useSelector(selectPerfilEmpleado);
    const {mutate}=useCrearPedidoPresencial();


  // 🟢 PLATOS
  const { data } = useGetPlateDisponible();
  const platos: Plate[] = data?.data ?? [];

  // 🔵 MÉTODOS DE PAGO
  const { data: pagosData } = useGetAllMetodosPagoActivo();
  const formasPago = pagosData?.data ?? [];

  // 🧮 TOTALES
  const subtotal = cart.reduce(
    (sum, i) => sum + i.cantidad * i.precioUnitario,
    0
  );
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  // 📝 REACT HOOK FORM
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<PedidoForm>({
    defaultValues: {
      idFormaPago: undefined,
    },
  });

  // -------------------- HELPERS --------------------
  const getCantidad = (idCatalogo: number) => {
    const item = cart.find((i) => i.idProducto === idCatalogo);
    return item ? item.cantidad : 0;
  };

  const agregar = (plato: Plate) => {
    dispatch(
      addItem({
        idProducto: plato.idCatalogo,
        nombre: plato.nombre,
        cantidad: 1,
        precioUnitario: plato.precio,
      })
    );
  };

  const disminuir = (idCatalogo: number) => {
    dispatch(decreaseItem(idCatalogo));
  };

  // -------------------- SUBMIT --------------------
  const onSubmit = (data: PedidoForm) => {
    if (cart.length === 0) {
      alert("Agregue productos al pedido");
      return;
    }

    const pedido = {
      idEmpleado:empleado?.idEmpleado,
      idMesa: Number(idMesa),
      idSucursal:empleado?.idSucursal,
      idFormaPago: data.idFormaPago,
      detalles: cart.map((item) => ({
        idCatalogo: item.idProducto,
        cantidad: item.cantidad,
      })),
    };

        console.log(pedido)


     mutate(pedido, {
       
       onSuccess: () => {
         Swal.fire({
           icon: "success",
           title: "Pedido realizado",
           timer: 1200,
           showConfirmButton: false,
         });
         dispatch(clearCart());
         navigate("/");
       },
       onError: () => {
         Swal.fire("Error al registrar pedido");
       },
     });





  };

  return (
    <>
      {/* HEADER */}
      <Box sx={{ m: 1, display: "flex", justifyContent: "space-between" }}>
        <Typography fontSize="2rem">
          Pedido – Mesa {idMesa}
        </Typography>

        <Button
          color="error"
          variant="contained"
          onClick={() => {
            dispatch(clearCart());
            navigate(-1);
          }}
        >
          Cancelar
        </Button>
      </Box>

      <Grid container spacing={2} sx={{ p: 1 }}>
        {/* 🟩 PLATOS */}
        <Grid item xs={12} md={8}>
          <Grid container spacing={2}>
            {platos.map((plato) => {
              const cantidad = getCantidad(plato.idCatalogo);

              return (
                <Grid item key={plato.idCatalogo}>
                  <Card sx={{ width: 180 }}>
                    <CardMedia
                      component="img"
                      height="110"
                      image={plato.imagenPlatoUrl}
                    />

                    <CardContent sx={{ p: 1 }}>
                      <Typography variant="subtitle2" noWrap>
                        {plato.nombre}
                      </Typography>

                      <Typography variant="caption">
                        S/ {plato.precio.toFixed(2)}
                      </Typography>

                      <Typography variant="caption" display="block">
                        Stock: {plato.stock}
                      </Typography>

                      <Chip size="small" label={plato.categoria} />

                      {plato.estadoplato && (
                        <>
                          {cantidad === 0 ? (
                            <Button
                              fullWidth
                              size="small"
                              sx={{ mt: 1 }}
                              onClick={() => agregar(plato)}
                              disabled={plato.stock === 0}
                            >
                              Agregar
                            </Button>
                          ) : (
                            <Box
                              sx={{
                                mt: 1,
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                              }}
                            >
                              <Button
                                size="small"
                                onClick={() =>
                                  disminuir(plato.idCatalogo)
                                }
                              >
                                -
                              </Button>

                              <Typography>{cantidad}</Typography>

                              <Button
                                size="small"
                                onClick={() => agregar(plato)}
                              >
                                +
                              </Button>
                            </Box>
                          )}
                        </>
                      )}
                    </CardContent>
                  </Card>
                </Grid>
              );
            })}
          </Grid>
        </Grid>

        {/* 🟦 RESUMEN + PAGO */}
        <Grid item xs={12} md={4}>
          <form onSubmit={handleSubmit(onSubmit)}>
            <Card>
              <CardContent>
                <Typography fontWeight="bold">
                  Resumen del Pedido
                </Typography>

                <Divider sx={{ my: 1 }} />

                {cart.map((item) => (
                  <Box
                    key={item.idProducto}
                    sx={{ display: "flex", justifyContent: "space-between" }}
                  >
                    <Typography variant="caption">
                      {item.nombre} x{item.cantidad}
                    </Typography>
                    <Typography variant="caption">
                      S/ {(item.cantidad * item.precioUnitario).toFixed(2)}
                    </Typography>
                  </Box>
                ))}

                <Divider sx={{ my: 1 }} />

                <Typography>Subtotal: S/ {subtotal.toFixed(2)}</Typography>
                <Typography>IGV: S/ {igv.toFixed(2)}</Typography>
                <Typography fontWeight="bold">
                  Total: S/ {total.toFixed(2)}
                </Typography>

                <Divider sx={{ my: 2 }} />

                {/* 🔵 MÉTODO DE PAGO */}
                <Typography fontWeight="bold">
                  Método de Pago
                </Typography>

                <FormControl
                  fullWidth
                  sx={{ mt: 1 }}
                  error={!!errors.idFormaPago}
                >
                  <InputLabel id="metodo-pago-label">
                    Seleccione método
                  </InputLabel>

                  <Controller
                    name="idFormaPago"
                    control={control}
                    rules={{ required: "Seleccione un método de pago" }}
                    render={({ field }) => (
                      <Select
                        {...field}
                        labelId="metodo-pago-label"
                        label="Seleccione método"
                      >
                        {formasPago.map((fp: any) => (
                          <MenuItem
                            key={fp.idFormaPago}
                            value={fp.idFormaPago}
                          >
                            {fp.nombre}
                          </MenuItem>
                        ))}
                      </Select>
                    )}
                  />

                  {errors.idFormaPago && (
                    <Typography variant="caption" color="error">
                      {errors.idFormaPago.message}
                    </Typography>
                  )}
                </FormControl>

                <Button
                  fullWidth
                  sx={{ mt: 2 }}
                  variant="contained"
                  color="success"
                  type="submit"
                  disabled={cart.length === 0}
                >
                  Realizar Pedido
                </Button>
              </CardContent>
            </Card>
          </form>
        </Grid>
      </Grid>


      
    </>
  );
}
