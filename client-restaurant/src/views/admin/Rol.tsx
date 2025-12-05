import { Box, Button } from "@mui/material";
import { useState } from "react";
import { useGetAllRoles, useCreateRol } from "../../services/roles.service";

import FormModalCreateRol from "../../components/Modals/FormModalRoleCrear";
import CardRol from "../../components/RenderRole";

import type { Rol } from "../../types/role.type";

export default function RenderRol() {
  const { data, refetch } = useGetAllRoles();
  const { mutate: create } = useCreateRol();

  const [openCreate, setOpenCreate] = useState(false);

  const handleCreate = (data: Rol) => {
    create(data, {
      onSuccess: () => {
        refetch();
        alert("Rol creado correctamente");
      },
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
