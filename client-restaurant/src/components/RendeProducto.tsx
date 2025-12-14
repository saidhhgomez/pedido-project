import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Box, Button, CardActionArea, CardMedia } from '@mui/material';
import { useSelector, useDispatch } from 'react-redux';
import { selectIdCliente, selectIdEmpleado, selectRol } from '../store/slices/auth.slice';
import { selectCart, addItem, decreaseItem } from '../store/slices/cart.slice';
import { useState } from 'react';
import type { Plate } from '../types/Plate.type';
import { useGetPlate, useRemovePlate, useUpdatePlate } from '../services/plate.service';
import EditModalPlate from './Modals/FormModalEdit';

export default function CardPlate({ Plate }: { Plate: Plate }) {
  const idEmpleado = useSelector(selectIdEmpleado);
    const idCliente = useSelector(selectIdCliente);

  const cart = useSelector(selectCart);
  const dispatch = useDispatch();

  const [openEdit, setOpenEdit] = useState(false);
  const { mutate: removePlate } = useRemovePlate();
  const { mutate: updatePlate } = useUpdatePlate();
  const { refetch } = useGetPlate();

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

  // Empleado → editar
  const handleEditSubmit = (data: Plate) => {
    const { idCatalogo, ...updateData } = data;
    updatePlate({ id: Plate.idCatalogo, data: updateData }, {
      onSuccess: () => {
        refetch();
        setOpenEdit(false);
        alert("Plato editado correctamente");
      },
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




    
      <Card sx={{ maxWidth:250 }}>
        <CardActionArea>
    <CardMedia
      component="img"
      image={"https://www.eatperu.com/wp-content/uploads/2019/10/peruvian-roast-chicken-recipe.jpg"} // url de la imagen
      alt={Plate.nombre} 
    />

          <CardContent>
            <Typography gutterBottom variant="h5">{Plate.nombre}</Typography>
            <Typography variant="body2" color="text.secondary">{Plate.categoria}</Typography>
            <Typography variant="body2" color="text.secondary">{Plate.estadoplato}</Typography>
            <Typography variant="body2" color="text.secondary">{Plate.imagenPlatoUrl}</Typography>
            <Typography variant="body2" color="text.secondary">{Plate.stock}</Typography>
          </CardContent>
        </CardActionArea>

        <Box sx={{ display: "flex", gap: 1, p: 1, alignItems: "center" }}>
          {/* Botones para empleado */}
          {idEmpleado !=null && (
            <>
              <Button size="small" variant="contained" color="error" onClick={handleRemove}>
                Eliminar
              </Button>
              <Button size="small" variant="contained" color="primary" onClick={() => setOpenEdit(true)}>
                Editar
              </Button>
            </>
          )}

          {/* Botones para cliente */}
          {idCliente !=null && (
            <>
              {cantidad === 0 ? (
                <Button size="small" variant="contained" onClick={handleAdd}>
                  Agregar
                </Button>
              ) : (
                <>
                  <Button size="small" variant="contained" onClick={handleDecrease}>-</Button>
                  <Typography sx={{ minWidth: 20, textAlign: "center" }}>{cantidad}</Typography>
                  <Button size="small" variant="contained" onClick={handleIncrease}>+</Button>
                </>
              )}
            </>
          )}
        </Box>

        <CardActions></CardActions>
      </Card>

      <EditModalPlate
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEditSubmit}
        initialData={Plate}
      />
    </>
  );
}
