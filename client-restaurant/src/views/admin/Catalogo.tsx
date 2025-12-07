import { useState } from "react";
import FormModalPlate from "../../components/Modals/ModalProducto";
import { Button } from "@mui/material";
import type { Plate, PlateEit } from "../../types/Plate.type";
import { useCreatePlate, useGetPlate } from "../../services/plate.service";
import CardPlate from "../../components/RendeProducto";
import Swal from "sweetalert2";

export default function Catalogo() {
        const { data,refetch } = useGetPlate();

      const [open, setOpen] = useState(false);
    const {mutate} =useCreatePlate();
      
            const Submit = (data: PlateEit) => {
          mutate(data
          ,{
            onSuccess: ()=>{
Swal.fire({
  position: "center",
  icon: "success",
  title: "Plato registrado",
  showConfirmButton: false,
  timer: 1500
});
              refetch();
              alert("Registro exitosamente");
            },onError:()=>{
              Swal.fire({
  position: "center",
  icon: "error",
  title: "Plato no registrado",
  showConfirmButton: false,
  timer: 1500
});

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