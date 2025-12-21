import { Route, Routes } from "react-router";
import Login from "../views/auth/login";
import Page404 from "../views/404";
import AuthLayout from "../layouts/auth.layout";
import Register from "../views/auth/register";
import AdminEmp from "../views/admin/AdminEmp";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<AuthLayout />}>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<Page404 />} />
        <Route path="/AdminEmp" element={<AdminEmp/>} />
      </Route>
    </Routes>
  );
}