import { Route, Routes } from "react-router";
import Page404 from "../views/404";
import ResumenPedido from "../views/mesero/resumenPedido";
import MeseroLayout from "../layouts/mesero.layout";
import PedidoPresencial from "../views/mesero/PedidoPresencial";
import MesaMesero from "../views/mesero/Mesas";
import PerfilMesero from "../views/mesero/PerfilMesero";


export default function Mesero() {
  return (
    <Routes>
      <Route path="/" element={<MeseroLayout/>}>
        <Route path="/" element={<MesaMesero     />} />
        <Route path="/pedido-presencial/:idMesa" element={<PedidoPresencial />} />
        <Route path="/perfil" element={<PerfilMesero/>} />
                <Route path="/resumenPedido/:idMesa" element={<ResumenPedido/>} />

        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}