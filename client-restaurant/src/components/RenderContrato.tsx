// components/CardContrato.tsx
import { Box, Button, Card, CardActions, CardContent, Typography } from "@mui/material";
import type { TipoContrato } from "../types/contrato.type";
import { useState } from "react";
import { useRemoveContrato, useUpdateContrato, useGetAllContrato, useUpdateContratoEstado } from "../services/contrato.service";
import EditModalContrato from "./Modals/EditModalContrato";

interface Props {
  contrato: TipoContrato;
}

export default function CardContrato({ contrato }: Props) {
  const [openEdit, setOpenEdit] = useState(false);

  const { mutate: removeContrato } = useRemoveContrato();
  const { mutate: updateContrato } = useUpdateContrato();
  const { refetch } = useGetAllContrato();
  const { mutate: toggleEstadoMutate } = useUpdateContratoEstado();
  

  const handleRemove = () => {
        if (!contrato.idTipoContrato) return;

    removeContrato(contrato.idTipoContrato, {
      onSuccess: () => {
        refetch();
        alert("Contrato eliminado correctamente");
      },
      onError: () => alert("Error al eliminar contrato"),
    });
  };



  const toggleEstado = () => {
  const nuevoEstado = contrato.estadoTipoContrato === "activo" ? "inactivo" : "activo";

  toggleEstadoMutate(
    { id: contrato.idTipoContrato!, estado: nuevoEstado },
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


            { contrato.estadoTipoContrato==="activo" &&(
              <Button size="small" color="error" variant="contained" onClick={handleRemove}>
              Eliminar
            </Button>
            )
          }
              <Button variant="contained" color={contrato.estadoTipoContrato === "activo" ? "warning" : "success"} onClick={toggleEstado}>
              {contrato.estadoTipoContrato === "activo" ? "Desactivar" : "Activar"}
            </Button> 

            { contrato.estadoTipoContrato==="activo" &&(
            <Button size="small" color="primary" variant="contained" onClick={() => setOpenEdit(true)}>
              Editar
            </Button>
            )
          }


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
