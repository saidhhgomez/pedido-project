// components/CardContrato.tsx
import { Box, Button, Card, CardActions, CardContent, Typography } from "@mui/material";
import type { TipoContrato } from "../types/contrato.type";
import { useState } from "react";
import { useRemoveContrato, useUpdateContrato, useGetAllContrato } from "../services/contrato.service";
import EditModalContrato from "./Modals/EditModalContrato";

interface Props {
  contrato: TipoContrato;
}

export default function CardContrato({ contrato }: Props) {
  const [openEdit, setOpenEdit] = useState(false);

  const { mutate: removeContrato } = useRemoveContrato();
  const { mutate: updateContrato } = useUpdateContrato();
  const { refetch } = useGetAllContrato();

  const handleRemove = () => {
    removeContrato(contrato.idTipoContrato, {
      onSuccess: () => {
        refetch();
        alert("Contrato eliminado correctamente");
      },
      onError: () => alert("Error al eliminar contrato"),
    });
  };

  const handleEditSubmit = (data: TipoContrato) => {
    const { idTipoContrato, ...updateData } = data;
    updateContrato(
      { id: contrato.idTipoContrato, data: updateData },
      {
        onSuccess: () => {
          refetch();
          setOpenEdit(false);
          alert("Contrato editado correctamente");
        },
        onError: () => alert("Error al editar contrato"),
      }
    );
  };

  return (
    <>
      <Card sx={{ maxWidth: 345, mb: 2 }}>
        <CardContent>
          <Typography variant="h6">{contrato.nombre}</Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {contrato.descripcion}
          </Typography>
        </CardContent>

        <CardActions>
          <Box sx={{ display: "flex", gap: 1 }}>
            <Button size="small" color="error" variant="contained" onClick={handleRemove}>
              Eliminar
            </Button>
            <Button size="small" color="primary" variant="contained" onClick={() => setOpenEdit(true)}>
              Editar
            </Button>
          </Box>
        </CardActions>
      </Card>

      <EditModalContrato
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEditSubmit}
        initialData={contrato}
      />
    </>
  );
}
