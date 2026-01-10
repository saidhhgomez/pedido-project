import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { Link, Outlet } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { logout, selectUsuario } from '../store/slices/auth.slice';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import Badge, { type BadgeProps } from '@mui/material/Badge';
import { styled } from '@mui/material/styles';
import { useState } from 'react';
import RightDrawer from '../components/Drawer';
import { selectCart } from '../store/slices/cart.slice';
import letras from "../assets/letra.png"; // 👈 TU LOGO
import { Avatar } from '@mui/material';


const StyledBadge = styled(Badge)<BadgeProps>(({ theme }) => ({
  '& .MuiBadge-badge': {
    right: 2,
    top: 13,
    border: `2px solid ${(theme.vars ?? theme).palette.background.paper}`,
    padding: '0 4px',
  },
}));

export default function ButtonAppBar() {
  const dispatch = useDispatch();
  const nombres = useSelector(selectUsuario);
  const [drawerOpen, setDrawerOpen] = useState(false);

  const cartItems = useSelector(selectCart);
  const totalItems = cartItems.reduce((acc, item) => acc + item.cantidad, 0);

  const doLogout = () => {
    dispatch(logout());
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" sx={{ backgroundColor: "#b93434ff" }}>
        <Toolbar>

          {/* LOGO + NOMBRE */}
          <Box sx={{ display: "flex", alignItems: "center", flexGrow: 1, gap: 1 }}>
            <Box
              component="img"
              src={letras}
              alt="logo"
              sx={{
                width: 120,
                height: 50,
              }}
            />
          </Box>



          {/* Avatar con nombre */}
<Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
  <Link to={"/Perfil"} style={{ display: "flex", alignItems: "center", textDecoration: "none", color: "inherit" }}>
    <Avatar
      alt={nombres?.nombre}
      src={nombres?.fotoUrl} // URL de la imagen
    >
      {nombres?.nombre?.[0]} {/* Si no hay imagen, muestra la primera letra */}
    </Avatar>
    <Typography variant="h6" sx={{ fontWeight: "bold", ml: 1 }}>
      {nombres?.nombre}
    </Typography>
  </Link>
</Box>


          <IconButton onClick={() => setDrawerOpen(true)} aria-label="cart">
            <StyledBadge badgeContent={totalItems} color="secondary">
              <ShoppingCartIcon />
            </StyledBadge>
          </IconButton>

          <Link to={"/"}>
            <Button color="inherit">Home</Button>
          </Link>
          <Link to={"/direction"}>
            <Button color="inherit">Direccion</Button>
          </Link>

          <Button onClick={doLogout} color="inherit">Cerrar Session</Button>
        </Toolbar>
      </AppBar>

      <Box sx={{ p: 2 }}>
        <RightDrawer open={drawerOpen} setOpen={setDrawerOpen} />
        <Outlet />
      </Box>
    </Box>
  );
}
