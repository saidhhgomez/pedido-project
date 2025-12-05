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

export default function AdminRoutes() {
  return (
    <Routes>
      <Route path="/" element={< AppLayout/>}>
        <Route path="/" element={<AdminHome/>} />
        <Route path="/Rol" element={<RolAdmin/>} />
        <Route path="/sucursal" element={<SucusalAdmin/>} />
        <Route path="/Contrato" element={<ContratoAdmin/>} />
        <Route path="/catalogo" element={<Catalogo />} />
        <Route path="/MetodoPago" element={<MetodoPagoAdmin />} />

        <Route path="/AdminEmp" element={<AdminEmp/>} />
        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}