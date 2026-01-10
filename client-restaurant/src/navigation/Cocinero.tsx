import { Route, Routes } from "react-router";
import Page404 from "../views/404";
import ClientLayout from "../layouts/cliente.layout";


export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<ClientLayout />}>

      
        <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}