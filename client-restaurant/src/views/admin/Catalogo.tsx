import { useState } from "react";
import FormModalPlate from "../../components/Modals/ModalProducto";
import { Button } from "@mui/material";
import type { Plate } from "../../types/Plate.type";
import { useCreatePlate } from "../../services/plate.service";

export default function Catalogo() {

      const [open, setOpen] = useState(false);
    const {mutate} =useCreatePlate();
      
            const Submit = (data: Plate) => {
          mutate(data
          ,{
            onSuccess: ()=>{
              alert("Registro exitosamente");
            }
          }); 
        };
    
  return (
    <>
      <h1>Catalgo De Platos</h1>

    <Button variant="contained" onClick={() => setOpen(true)}>
        Agregar plato
    </Button>
      
      <FormModalPlate         
        open={open}
        onClose={() => setOpen(false)}
        onSubmit={Submit}/>
    </>
  );
}