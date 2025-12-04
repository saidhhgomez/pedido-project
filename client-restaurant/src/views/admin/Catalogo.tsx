import { useState } from "react";
import FormModalPlate from "../../components/Modals/ModalProducto";
import { Button } from "@mui/material";
import type { Plate, PlateEit } from "../../types/Plate.type";
import { useCreatePlate, useGetPlate } from "../../services/plate.service";
import CardPlate from "../../components/RendeProducto";

export default function Catalogo() {
        const { data,refetch } = useGetPlate();

      const [open, setOpen] = useState(false);
    const {mutate} =useCreatePlate();
      
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
      <h1>Catalgo De Platos</h1>

    <Button variant="contained" onClick={() => setOpen(true)}>
        Agregar plato
    </Button>

                    {
              data?.data?.map((el:Plate)=>(
                <CardPlate key={el.idCatalogo} Plate={el}
              />
              )
              )
    
            }
      
      <FormModalPlate         
        open={open}
        onClose={() => setOpen(false)}
        onSubmit={Submit}/>
    </>
  );
}