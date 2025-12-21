import { Box, Button } from "@mui/material";
import { useState } from "react";
import FormModalJornada from "../../components/Modals/FormModalContrato";
import type { TipoContrato, TipoJornada } from "../../types/contrato.type";
import { useCreateContrato, useGetAllContrato } from "../../services/contrato.service";
import CardContrato from "../../components/RenderContrato";
import Swal from "sweetalert2";

export default function ContratoAdmin() {

    const {mutate} =useCreateContrato();

        const { data,refetch } = useGetAllContrato();


  const [open, setOpen] = useState(false);


            const handleSubmit = (data: TipoJornada) => {
          mutate(data
          ,{
            onSuccess: ()=>{

              refetch();
Swal.fire({
  position: "center",
  icon: "success",
  title: "Nuva Jornada registrada",
  showConfirmButton: false,
  timer: 1500
});            },onError: (err)=>{
  Swal.fire({
  position: "center",
  icon: "error",
  title: err.response.data.mensaje,
  showConfirmButton: false,
  timer: 1500
});
}
          }); 
        };
  

  return (
    <>


   
      <Button variant="contained" onClick={() => setOpen(true)}>Nueva Jornada</Button>
      <FormModalJornada open={open} onClose={() => setOpen(false)} onSubmit={handleSubmit} />


      <Box sx={{ mt: 2, display: "flex", flexWrap: "wrap", gap: 2 }}>
        {data?.data?.map((TipoContrato: TipoContrato) => (
          <CardContrato key={TipoContrato.idTipoContrato} contrato={TipoContrato} onEdit={() => handleEdit(TipoContrato)} />
        ))}
      </Box>

        
    </>
  );
}