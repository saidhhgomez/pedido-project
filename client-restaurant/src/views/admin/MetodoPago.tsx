import { Box, Button } from "@mui/material";
import { useState } from "react";
import { useGetAllMetodosPago, useCreateMetodoPago } from "../../services/metodoPago.service";
import CardMetodoPago from "../../components/renderMetodoPago";
import FormModalCreateMetodoPago from "../../components/Modals/FormModalMetodoPagoCrear";
import type { MetodoPago } from "../../types/metodoPago.type";
import Swal from "sweetalert2";

export default function RenderMetodoPago() {
  const { data, refetch } = useGetAllMetodosPago();
  const { mutate: create } = useCreateMetodoPago();
  const [openCreate, setOpenCreate] = useState(false);

  const handleCreate = (data: MetodoPago) => {
    create(data, { onSuccess: () => { 
      refetch(); 
Swal.fire({
  position: "center",
  icon: "success",
  title: "Metodo de Pago Registrado",
  showConfirmButton: false,
  timer: 1200
});    }, onError: ()=>{
  Swal.fire({
  position: "center",
  icon: "error",
  title: "Metodo de Pago No Registrado",
  showConfirmButton: false,
  timer: 1500
});
}
  });
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
