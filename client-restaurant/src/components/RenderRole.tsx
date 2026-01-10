import { Card, CardContent, Typography, Box, Button } from "@mui/material";
import { useState } from "react";
import type { Rol } from "../types/role.type";

import { useRemoveRol, useUpdateRol, useGetAllRoles, useUpdateRolesEstado } from "../services/roles.service";
import FormModalEditRol from "./Modals/FormModalRoleEdit";

export default function CardRol({ rol }: { rol: Rol }) {
  const [openEdit, setOpenEdit] = useState(false);

  const { mutate: remove } = useRemoveRol();
  const { mutate: update } = useUpdateRol();
  const { refetch } = useGetAllRoles();
  const { mutate: toggleEstadoMutate } = useUpdateRolesEstado();


  const handleDelete = () => {
    if (!rol.idRol) return;
    remove(rol.idRol, { onSuccess: () => refetch() });
  };


  const toggleEstado = () => {
  const nuevoEstado = rol.estadoRol === "activo" ? "inactivo" : "activo";

  toggleEstadoMutate(
    { id: rol.idRol!, estado: nuevoEstado },
    {
      onSuccess: () =>{   
        refetch();
        alert(`Sucursal ${nuevoEstado === "activo" ? "activada" : "desactivada"}`);
    }
      ,
      onError: () => alert("Error al actualizar el estado"),
    }
  );
};




  const handleEdit = (data: Rol) => {
    if (!rol.idRol) return;

    update(
      { id: rol.idRol, data },
      {
        onSuccess: () => {
          refetch();
          alert("Rol actualizado correctamente");
        },
      }
    );
  };

  return (
    <>
      <Card sx={{ width: 300 }}>
        <CardContent>
          <Typography variant="h6">{rol.nombre}</Typography>
          <Typography>{rol.descripcion}</Typography>
          <Typography>Estado: {rol.estadoRol}</Typography>

          <Box sx={{ display: "flex", gap: 1, mt: 2 }}>


            {rol.estadoRol==="activo" &&(

            <Button variant="contained" onClick={() => setOpenEdit(true)}>
              Editar
            </Button>
            )
            }

                          <Button variant="contained" color={rol.estadoRol === "activo" ? "warning" : "success"} onClick={toggleEstado}>
              {rol.estadoRol === "activo" ? "Desactivar" : "Activar"}
            </Button>

            <Button variant="contained" color="error" onClick={handleDelete}>
              Eliminar
            </Button>
          </Box>
        </CardContent>
      </Card>

      <FormModalEditRol
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEdit}
        initialData={rol}
      />
    </>
  );
}
