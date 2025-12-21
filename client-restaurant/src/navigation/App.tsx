import { Route, Routes } from "react-router";
import Home from "../views/client/home";
import Direction from "../views/client/direction";
import Page404 from "../views/404";
import ClientLayout from "../layouts/cliente.layout";
import ResumenPedido from "../views/client/resumenPedido";
import PerfilCliente from "../views/client/PerfilCliente";
import HistorialPedidosCliente from "../views/client/historialPedido";


export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<ClientLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/perfil" element={<PerfilCliente />} />
                <Route path="/historial" element={<HistorialPedidosCliente />} />

        <Route path="/resumenPedido" element={<ResumenPedido />} />
        <Route path="/direction" element={<Direction />} />
        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}