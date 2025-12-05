import { Card, CardContent, Typography, Box, Button } from "@mui/material";
import { useState } from "react";
import type { MetodoPago } from "../types/metodoPago.type";
import { useRemoveMetodoPago, useUpdateMetodoPago, useGetAllMetodosPago } from "../services/metodoPago.service";
import FormModalEditMetodoPago from "./Modals/FormModalMetodoPagoEdit";

export default function CardMetodoPago({ metodo }: { metodo: MetodoPago }) {
  const [openEdit, setOpenEdit] = useState(false);
  const { mutate: remove } = useRemoveMetodoPago();
  const { mutate: update } = useUpdateMetodoPago();
  const { refetch } = useGetAllMetodosPago();

  const handleDelete = () => {
    if (!metodo.idFormaPago) return;
    remove(metodo.idFormaPago, { onSuccess: () => refetch() });
  };

  const handleEdit = (data: MetodoPago) => {
    if (!metodo.idFormaPago) return;
    update({ id: metodo.idFormaPago, data }, { onSuccess: () => { refetch(); alert("Método actualizado"); } });
  };

  return (
    <>
      <Card sx={{ width: 300 }}>
        <CardContent>
          <Typography variant="h6">{metodo.nombre}</Typography>
          <Box sx={{ display: "flex", gap: 1, mt: 2 }}>
            <Button variant="contained" onClick={() => setOpenEdit(true)}>Editar</Button>
            <Button variant="contained" color="error" onClick={handleDelete}>Eliminar</Button>
          </Box>
        </CardContent>
      </Card>

      <FormModalEditMetodoPago
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEdit}
        initialData={metodo}
      />
    </>
  );
}
