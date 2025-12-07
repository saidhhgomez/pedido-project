import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import DeleteIcon from '@mui/icons-material/Delete';
import Divider from '@mui/material/Divider';
import { useSelector, useDispatch } from 'react-redux';
import { selectCart, addItem, decreaseItem, removeItem } from '../store/slices/cart.slice';
import Paper from '@mui/material/Paper';
import { Button } from '@mui/material';
import { useNavigate } from 'react-router';

interface RightDrawerProps {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function RightDrawer({ open, setOpen }: RightDrawerProps) {
  const toggleDrawer = (isOpen: boolean) => (
    event: React.KeyboardEvent | React.MouseEvent
  ) => {
    if (
      event.type === 'keydown' &&
      ((event as React.KeyboardEvent).key === 'Tab' ||
        (event as React.KeyboardEvent).key === 'Shift')
    ) {
      return;
    }
    setOpen(isOpen);
  };
const navigate = useNavigate();

  const cart = useSelector(selectCart);
  const dispatch = useDispatch();

  const subtotal = cart.reduce((sum, item) => sum + item.precioUnitario * item.cantidad, 0);
  const igv = subtotal * 0.18;
  const total = subtotal + igv;

  return (
    <Drawer anchor="right" open={open} onClose={toggleDrawer(false)}>
      <Box sx={{ width: 400, p: 2, backgroundColor: '#f9f9f9', height: '100%' }}>
        {/* Botón para cerrar */}
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
          <IconButton onClick={toggleDrawer(false)}>
            <ArrowBackIcon />
          </IconButton>
        </Box>

        <Typography variant="h5" gutterBottom sx={{ fontWeight: 'bold', mb: 2 }}>
          Carrito de Compras
        </Typography>

        <Divider />

        <List sx={{ mt: 2 }}>
          {cart.length === 0 && (
            <Typography sx={{ mt: 2, textAlign: 'center', color: 'gray' }}>
              No hay productos en el carrito
            </Typography>
          )}

          {cart.map((item) => (
            <Paper key={item.idProducto} sx={{ p: 2, mb: 2, boxShadow: 2 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography sx={{ fontWeight: 500 }}>{item.nombre}</Typography>
                <IconButton onClick={() => dispatch(removeItem(item.idProducto))} color="error">
                  <DeleteIcon />
                </IconButton>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <IconButton
                    size="small"
                    onClick={() => dispatch(decreaseItem(item.idProducto))}
                    color="primary"
                  >
                    <RemoveIcon />
                  </IconButton>
                  <Typography sx={{ minWidth: 24, textAlign: 'center' }}>{item.cantidad}</Typography>
                  <IconButton
                    size="small"
                    onClick={() => dispatch(addItem({ ...item, cantidad: 1 }))}
                    color="primary"
                  >
                    <AddIcon />
                  </IconButton>
                </Box>

                <Typography sx={{ fontWeight: 'bold' }}>
                  S/. {(item.precioUnitario * item.cantidad).toFixed(2)}
                </Typography>
              </Box>
            </Paper>
          ))}
        </List>

        <Divider sx={{ my: 2 }} />

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Typography>Subtotal:</Typography>
            <Typography>S/. {subtotal.toFixed(2)}</Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Typography>IGV (18%):</Typography>
            <Typography>S/. {igv.toFixed(2)}</Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold' }}>
            <Typography>Total:</Typography>
            <Typography>S/. {total.toFixed(2)}</Typography>
          </Box>

          <Button
            variant="contained"
            color="primary"
            sx={{ mt: 2 }}
            disabled={cart.length === 0}
            onClick={() => {
  setOpen(false); // cerrar drawer
  navigate('/resumenPedido'); // ir a resumen
}}
          >
            Realizar Compra
          </Button>
        </Box>
      </Box>
    </Drawer>
  );
}
