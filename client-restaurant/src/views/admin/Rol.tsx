import { Box, Button } from "@mui/material";
import { useState } from "react";
import { useGetAllRoles, useCreateRol } from "../../services/roles.service";

import FormModalCreateRol from "../../components/Modals/FormModalRoleCrear";
import CardRol from "../../components/RenderRole";

import type { Rol } from "../../types/role.type";
import Swal from "sweetalert2";

export default function RenderRol() {
  const { data, refetch } = useGetAllRoles();
  const { mutate: create } = useCreateRol();

  const [openCreate, setOpenCreate] = useState(false);

  const handleCreate = (data: Rol) => {
    create(data, {
      onSuccess: () => {
        refetch();
Swal.fire({
  position: "center",
  icon: "success",
  title: "Rol registrado",
  showConfirmButton: false,
  timer: 1500
});      },onError: ()=>{
  Swal.fire({
  position: "center",
  icon: "error",
  title: "Rol No Registrado",
  showConfirmButton: false,
  timer: 1500
});
}
    });
  };

  return (
    <Box>
      <Button variant="contained" onClick={() => setOpenCreate(true)}>
        Nuevo Rol
      </Button>

      <FormModalCreateRol
        open={openCreate}
        onClose={() => setOpenCreate(false)}
        onSubmit={handleCreate}
      />

      <Box sx={{ mt: 2, display: "flex", gap: 2, flexWrap: "wrap" }}>
        {data?.data?.map((rol: Rol) => (
          <CardRol key={rol.idRol} rol={rol} />
        ))}
      </Box>
    </Box>
  );
}
