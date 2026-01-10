import { useState } from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Box, Button, CardActionArea, CardMedia } from '@mui/material';
import { useSelector, useDispatch } from 'react-redux';
import { selectCart, addItem, decreaseItem } from '../store/slices/cart.slice';
import type { Plate } from '../types/Plate.type';
import { useActualizarEstadoCatalogo, useGetPlate, useRemovePlate } from '../services/plate.service';
import FormModalPlate from './Modals/ModalProducto';
import { selectPerfilCliente, selectPerfilEmpleado, selectUsuario } from '../store/slices/auth.slice';

export default function CardPlate({ Plate }: { Plate: Plate }) {
  const [openEdit, setOpenEdit] = useState(false);
  
  const idEmpleado = useSelector(selectPerfilEmpleado);
  const idCliente = useSelector(selectPerfilCliente);
  
  const { mutate: actualizarEstadoMutate, isPending: isUpdating } = useActualizarEstadoCatalogo();
  const { mutate: removePlate } = useRemovePlate();
  const { refetch } = useGetPlate();
  const usuario=useSelector(selectUsuario);

  const cart = useSelector(selectCart);
  const dispatch = useDispatch();

  // Toggle estado del plato
  const toggleEstado = () => {
    actualizarEstadoMutate(
      { id: Plate.idCatalogo, activo: !Plate.estadoplato },
      {
        onSuccess: () => {
          refetch();
          alert(`Catálogo ${!Plate.estadoplato ? "activado" : "desactivado"}`);
        },
        onError: (error) => {
          console.error(error);
          alert("Error al actualizar el estado");
        },
      }
    );
  };

  // Cantidad actual en el carrito
  const cartItem = cart.find(item => item.idProducto === Plate.idCatalogo);
  const cantidad = cartItem ? cartItem.cantidad : 0;

  // Empleado → eliminar
  const handleRemove = () => {
    removePlate(Plate.idCatalogo, {
      onSuccess: () => {
        refetch();
        alert("Se eliminó exitosamente");
      },
      onError: () => alert("Error al eliminar"),
    });
  };

  // Cliente → agregar, +, -
  const handleAdd = () => {
    dispatch(addItem({
      idProducto: Plate.idCatalogo,
      nombre: Plate.nombre,
      cantidad: 1,
      precioUnitario: Plate.precio,
    }));
  };

  const handleIncrease = () => {
    dispatch(addItem({
      idProducto: Plate.idCatalogo,
      nombre: Plate.nombre,
      cantidad: 1,
      precioUnitario: Plate.precio,
    }));
  };

  const handleDecrease = () => {
    dispatch(decreaseItem(Plate.idCatalogo));
  };

  return (

    
    <>

    
      <Card sx={{ maxWidth: 250 }}>
        <CardActionArea>
          <CardMedia
            component="img"
            image={Plate.imagenPlatoUrl}
            alt={Plate.nombre}
            sx={{
              width: "100%",
              height: 180,
              objectFit: "cover"
            }}
          />

          <CardContent>
            <Typography gutterBottom variant="h5">{Plate.nombre}</Typography>
            <Typography variant="body2" color="text.secondary">{Plate.categoria}</Typography>
            <Typography variant="body2" color="text.secondary">S/ {Plate.precio}</Typography>
            <Typography variant="body2" color="text.secondary">Stock: {Plate.stock}</Typography>
            {idEmpleado != null &&

                          <Typography variant="body2" color="text.secondary">
              Estado: {Plate.estadoplato ? "Activo" : "Inactivo"}
            </Typography>
            }

          </CardContent>
        </CardActionArea>

        <Box sx={{ display: "flex", gap: 1, p: 1, alignItems: "center", flexWrap: "wrap" }}>
          {/* Botones para empleado */}
          {idEmpleado != null  && usuario?.rolPrincipal.toLowerCase() === "admin" &&(
            <>
              {/* Botones Eliminar y Editar: solo si está ACTIVO */}
              {Plate.estadoplato && (
                <>
                  <Button 
                    size="small" 
                    variant="contained" 
                    color="error" 
                    onClick={handleRemove}
                  >
                    Eliminar
                  </Button>
                  
                  <Button 
                    size="small" 
                    variant="contained" 
                    color="primary" 
                    onClick={() => setOpenEdit(true)}
                  >
                    Editar
                  </Button>
                </>
              )}

              {/* Botón de Activar/Desactivar: siempre visible */}
{/* Botón solo visible si el plato está inactivo */}
{!Plate.estadoplato && (
  <Button
    size="small"
    variant="contained"
    color="success"
    onClick={toggleEstado}
    disabled={isUpdating}
  >
    {isUpdating ? "..." : "Activar"}
  </Button>
)}

            </>
          )}



          {/* Botones para cliente: solo si el plato está ACTIVO */}
            {(idCliente !=null || usuario?.rolPrincipal.toLowerCase()==="mesero" ) &&(
              <>
                {cantidad === 0 ? (
                  <Button size="small" variant="contained" onClick={handleAdd}>
                    Agregar
                  </Button>
                ) : (
                  <>
                    <Button size="small" variant="contained" onClick={handleDecrease}>
                      -
                    </Button>
                    <Typography sx={{ minWidth: 20, textAlign: "center" }}>
                      {cantidad}
                    </Typography>
                    <Button size="small" variant="contained" onClick={handleIncrease}>
                      +
                    </Button>
                  </>
                )}
              </>
            )}
        </Box>

        <CardActions />
      </Card>

      {/* Modal de edición */}
      <FormModalPlate
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        plateToEdit={Plate}
      />
    </>
  );
}