import { Route, Routes } from "react-router";
import Home from "../views/client/home";
import Hello from "../views/client/hello";
import Page404 from "../views/404";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/hello" element={<Hello />} />
      <Route path="*" element={<Page404 />} />
    </Routes>
  );
}