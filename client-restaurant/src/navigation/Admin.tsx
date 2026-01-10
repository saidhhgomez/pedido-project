import { Route, Routes } from "react-router";
import Page404 from "../views/404";
import AdminHome from "../views/admin/Admin";
import AdminEmp from "../views/admin/AdminEmp";
import AppLayout from "../layouts/app.layout";
import Catalogo from "../views/admin/Catalogo";
import RolAdmin from "../views/admin/Rol";
import ContratoAdmin from "../views/admin/Contrato";
import SucusalAdmin from "../views/admin/Sucursal";
import MetodoPagoAdmin from "../views/admin/MetodoPago";
import Mesa from "../views/admin/Mesa";
import MesaSucusales from "../views/admin/MesaSucursal";
import Historial from "../views/admin/Historial";
import PerfilAdmin from "../views/admin/PerfilAdmin";

export default function AdminRoutes() {
  return (
    <Routes>
      <Route path="/" element={< AppLayout/>}>
        <Route path="/Rol" element={<RolAdmin/>} />
        <Route path="/sucursal" element={<SucusalAdmin/>} />
        <Route path="/Contrato" element={<ContratoAdmin/>} />
        <Route path="/" element={<Catalogo />} />
        <Route path="/mesa" element={<Mesa />} />
        <Route path="/mesaSucursales" element={<MesaSucusales />} />
        <Route path="/historial" element={<Historial />} />
        <Route path="/MetodoPago" element={<MetodoPagoAdmin />} />
        <Route path="/AdminEmp" element={<AdminEmp/>} />
        <Route path="/Perfil" element={<PerfilAdmin/>} />
        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}