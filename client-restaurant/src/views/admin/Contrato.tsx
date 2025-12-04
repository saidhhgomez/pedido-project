import { Button } from "@mui/material";
import { useState } from "react";
import FormModalJornada from "../../components/Modals/FormModalContrato";
import type { TipoJornada } from "../../types/contrato.type";

export default function ContratoAdmin() {


  const [open, setOpen] = useState(false);

  const handleSubmit = (data: TipoJornada) => {
    console.log("Jornada registrada:", data);
  };

              const Submit = (data: PlateEit) => {
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
    </>
  );
}