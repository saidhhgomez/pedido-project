import { Route, Routes } from "react-router";
import Home from "../views/client/home";
import Hello from "../views/client/hello";
import Direction from "../views/client/direction";
import Page404 from "../views/404";
import ClientLayout from "../layouts/cliente.layout";


export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<ClientLayout />}>
      <Route path="/" element={<Home />} />
      <Route path="/hello" element={<Hello />} />
      <Route path="/direction" element={<Direction />} />
      <Route path="*" element={<Page404 />} />
      </Route>
    </Routes>
  );
}