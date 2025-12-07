import { useCreateDirection, useGetDirecciones } from "../../services/direction.service";
import MediaCard from "../../components/RenderDire";
import {  Box, Button, Typography } from "@mui/material";
import FormModal from "../../components/Modals/ModalDirection";
import { useState } from "react";
import type { DireccionForm } from "../../types/direction.type";
import Swal from "sweetalert2";
import { selectIdCliente } from "../../store/slices/auth.slice";
import { useSelector } from "react-redux";

  export interface Direccion {
    idDireccion:string,
    departamento: string;
    provincia: string;
    distrito: string;
    direccion: string;
    referencia: string;
  }


  export default function MyDirection() {
    const idcliente=useSelector(selectIdCliente);
    const [open, setOpen] = useState(false);
    const {mutate}=useCreateDirection();

      const {data,refetch}=useGetDirecciones(idcliente);



        const Submit = (data: DireccionForm) => {
      mutate({...data,
        idCliente:idcliente,
      },{
        onSuccess: ()=>{
          refetch();
Swal.fire({
  position: "center",
  icon: "success",
  title: "Registro Direccion Exitoso",
  showConfirmButton: false,
  timer: 1200
});        }, onError:()=>{

Swal.fire({
  position: "center",
  icon: "error",
  title: "No se registro Direccion",
  showConfirmButton: false,
  timer: 1500
}); 

}
      }); 
    };






    return <> 
      
    <Box    alignItems="center"
   sx={{    m: 1,   // margin en todos los lados
}}>
<Typography
  sx={{
    fontSize: "2rem",
    m:2 // tamaño más grande, ajusta a tu gusto
  }}
>
Direccion 
</Typography>
<Box 
>
          <Button   variant="contained" onClick={() => setOpen(true)}>
          Nueva Dirección
        </Button>
</Box>


</Box>


        <div className="grid grid-cols-3 gap-x-8 gap-y-4">
                {
          data?.data.data.map((el:Direccion)=>(
            <MediaCard Direccion={el}/>
          )
          )

        }
    
        </div>




            <FormModal
          open={open}
          onClose={() => setOpen(false)}
          onSubmit={Submit}
        />
    
    </>;
  }