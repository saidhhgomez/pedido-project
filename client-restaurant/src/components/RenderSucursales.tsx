import { Card, CardContent, Typography, Box, Button } from "@mui/material";
import { useState } from "react";
import type { Sucursal } from "../types/sucursales.type";
import { useRemoveSucursal, useUpdateSucursal, useGetAllSucursales } from "../services/sucursales.service";
import FormModalSucursal from "./Modals/FormModalSucursal";

export default function CardSucursal({ sucursal }: { sucursal: Sucursal }) {
  const [openEdit, setOpenEdit] = useState(false);
  const { mutate: deleteMutate } = useRemoveSucursal();
  const { mutate: updateMutate } = useUpdateSucursal();
  const { refetch } = useGetAllSucursales();

  const handleRemove = () => {
    if (!sucursal.idSucursal) return;
    deleteMutate(sucursal.idSucursal, { onSuccess: () => refetch() });
  };

  const handleEditSubmit = (data: Sucursal) => {
    if (!sucursal.idSucursal) return;
    const { idSucursal, ...updateData } = data;
    updateMutate(
      { id: sucursal.idSucursal, data: updateData },
      { onSuccess: () => { refetch(); setOpenEdit(false); alert("Sucursal editada"); } }
    );
  };

  return (
    <>
      <Card sx={{ maxWidth: 345, mb: 2 }}>
        <CardContent>
          <Typography variant="h6">{sucursal.nombre}</Typography>
          <Typography>{sucursal.direccion}</Typography>
          <Typography>{sucursal.telefono}</Typography>
          <Typography>{sucursal.estado}</Typography>
          <Box sx={{ display: "flex", gap: 1, mt: 2 }}>
            <Button variant="contained" color="primary" onClick={() => setOpenEdit(true)}>Editar</Button>
            <Button variant="contained" color="error" onClick={handleRemove}>Eliminar</Button>
          </Box>
        </CardContent>
      </Card>

      <FormModalSucursal
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEditSubmit}
        initialData={sucursal}
      />
    </>
  );
}
