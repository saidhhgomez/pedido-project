import { Box, Button } from "@mui/material";
import { useState } from "react";
import { useGetAllMetodosPago, useCreateMetodoPago } from "../../services/metodoPago.service";
import CardMetodoPago from "../../components/renderMetodoPago";
import FormModalCreateMetodoPago from "../../components/Modals/FormModalMetodoPagoCrear";
import type { MetodoPago } from "../../types/metodoPago.type";

export default function RenderMetodoPago() {
  const { data, refetch } = useGetAllMetodosPago();
  const { mutate: create } = useCreateMetodoPago();
  const [openCreate, setOpenCreate] = useState(false);

  const handleCreate = (data: MetodoPago) => {
    create(data, { onSuccess: () => { refetch(); alert("Método creado"); } });
  };

  return (
    <Box>
      <Button variant="contained" onClick={() => setOpenCreate(true)}>Nuevo Método de Pago</Button>

      <FormModalCreateMetodoPago open={openCreate} onClose={() => setOpenCreate(false)} onSubmit={handleCreate} />

      <Box sx={{ mt: 2, display: "flex", gap: 2, flexWrap: "wrap" }}>
        {data?.data?.map((metodo: MetodoPago) => (
          <CardMetodoPago key={metodo.idFormaPago} metodo={metodo} />
        ))}
      </Box>
    </Box>
  );
}
