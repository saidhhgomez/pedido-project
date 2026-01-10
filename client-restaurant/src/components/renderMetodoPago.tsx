import { Card, CardContent, Typography, Box, Button } from "@mui/material";
import { useState } from "react";
import type { MetodoPago } from "../types/metodoPago.type";
import { useRemoveMetodoPago, useUpdateMetodoPago, useGetAllMetodosPago, useUpdateMetodoPagoEstado } from "../services/metodoPago.service";
import FormModalEditMetodoPago from "./Modals/FormModalMetodoPagoEdit";

export default function CardMetodoPago({ metodo }: { metodo: MetodoPago }) {
  const [openEdit, setOpenEdit] = useState(false);
  const { mutate: remove } = useRemoveMetodoPago();
  const { mutate: update } = useUpdateMetodoPago();
  const { refetch } = useGetAllMetodosPago();
  const { mutate: toggleEstadoMutate } = useUpdateMetodoPagoEstado();

  const handleDelete = () => {
    if (!metodo.idFormaPago) return;
    remove(metodo.idFormaPago, { onSuccess: () => refetch() });
  };


    const toggleEstado = () => {
  const nuevoEstado = metodo.estadoFormaPago === "activo" ? "inactivo" : "activo";

  toggleEstadoMutate(
    { id: metodo.idFormaPago!, estado: nuevoEstado },
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


  const handleEdit = (data: MetodoPago) => {
    if (!metodo.idFormaPago) return;
    update({ id: metodo.idFormaPago, data }, { onSuccess: () => { refetch(); alert("Método actualizado"); } });
  };

  return (
    <>


      <Card sx={{ width: 300 }}>
        <CardContent>
          <Typography variant="h6">{metodo.nombre}</Typography>
          <Typography >{metodo.estadoFormaPago}</Typography>
          <Box sx={{ display: "flex", gap: 1, mt: 2 }}>

            { metodo.estadoFormaPago==="activo" &&(
              <>
            <Button variant="contained" onClick={() => setOpenEdit(true)}>Editar</Button>
                        <Button variant="contained" color="error" onClick={handleDelete}>Eliminar</Button>
</>
            )
          }
            {metodo.estadoFormaPago==="inactivo" &&(
              <Button variant="contained" color={metodo.estadoFormaPago === "inactivo" ? "success" : "info"} onClick={toggleEstado}>
              {metodo.estadoFormaPago === "inactivo" ? "Activar" : ""}
            </Button>
            )
            }
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
