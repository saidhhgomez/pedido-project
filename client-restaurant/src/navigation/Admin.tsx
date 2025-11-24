import { Route, Routes } from "react-router";
import Page404 from "../views/404";
import AdminHome from "../views/admin/Admin";
import AdminEmp from "../views/admin/AdminEmp";

export default function AdminRoutes() {
  return (
    <Routes>
      <Route path="/" element={<AdminHome/>} />
      <Route path="/AdminEmp" element={<AdminEmp/>} />
      <Route path="*" element={<Page404 />} />
    </Routes>
  );
}