import { Route, Routes } from "react-router";
import Page404 from "../views/404";
import AdminHome from "../views/admin/Admin";
import AdminEmp from "../views/admin/AdminEmp";
import AppLayout from "../layouts/app.layout";
import Catalogo from "../views/admin/Catalogo";

export default function AdminRoutes() {
  return (
    <Routes>
      <Route path="/" element={< AppLayout/>}>
        <Route path="/" element={<AdminHome/>} />
        <Route path="/catalogo" element={<Catalogo />} />
        <Route path="/AdminEmp" element={<AdminEmp/>} />
        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}