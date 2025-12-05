import { Box, Button } from "@mui/material";
import { useState } from "react";
import FormModalJornada from "../../components/Modals/FormModalContrato";
import type { TipoContrato, TipoJornada } from "../../types/contrato.type";
import { useCreateContrato, useGetAllContrato } from "../../services/contrato.service";
import CardContrato from "../../components/RenderContrato";
import CardSucursal from "../../components/RenderSucursales";
import type { Sucursal } from "../../types/sucursales.type";

export default function ContratoAdmin() {

    const {mutate} =useCreateContrato();

        const { data,refetch } = useGetAllContrato();


  const [open, setOpen] = useState(false);


            const handleSubmit = (data: TipoJornada) => {
          mutate(data
          ,{
            onSuccess: ()=>{
              console.log("registro exisamente")
              refetch();
              alert("Registro exitosamente");
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