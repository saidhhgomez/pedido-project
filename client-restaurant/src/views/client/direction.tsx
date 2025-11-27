import { useDispatch, useSelector } from "react-redux";

import { logout, selectIdCliente } from "../../store/slices/auth.slice";
import { Link, useNavigate } from "react-router";
import { useCreateDirection, useGetDirecciones } from "../../services/direction.service";
import MediaCard from "../../components/RenderDire";
import {  Button } from "@mui/material";
import FormModal from "../../components/ModalDirection";
import { useState } from "react";
import type { DireccionForm } from "../../types/direction.type";

export interface Direccion {
  idDireccion:string,
  departamento: string;
  provincia: string;
  distrito: string;
  direccion: string;
  referencia: string;
}


export default function MyDirection() {
  const dispatch = useDispatch();
  const idcliente=useSelector(selectIdCliente);
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const {mutate}=useCreateDirection();

    const {data,refetch}=useGetDirecciones(idcliente);



      const handleSubmit = (data: DireccionForm) => {
    mutate({...data,
      idCliente:idcliente,
    },{
      onSuccess: ()=>{
        refetch();
        alert("Registro exitosamente");
      }
    }); 
  };






  const doLogout = () => {
    dispatch(logout());
    navigate("/");

  };

  return <> 
    
      <h1>Bienvenido Direccion</h1>
      <button onClick={doLogout}>Logout</button>

      <Link to="/">Go Home</Link>

      <div className="grid grid-cols-3 gap-x-8 gap-y-4">
              {
        data?.data.data.map((el:Direccion)=>(
          <MediaCard Direccion={el}/>
        )
        )

      }
  
      </div>

      <Button variant="contained" onClick={() => setOpen(true)}>
        Nueva Dirección
      </Button>


          <FormModal
        open={open}
        onClose={() => setOpen(false)}
        onSubmit={handleSubmit}
      />
  
  </>;
}