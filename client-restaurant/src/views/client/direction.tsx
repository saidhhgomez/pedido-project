import { useDispatch, useSelector } from "react-redux";

import { logout, selectIdCliente } from "../../store/slices/auth.slice";
import { Link, useNavigate } from "react-router";
import { useGetDirecciones } from "../../services/direction.service";
import MediaCard from "../../components/RenderDire";

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


  const {data}=useGetDirecciones(idcliente);



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
        data?.data.data.map(el=>(
          <MediaCard Direccion={el}/>
        )
        )

      }
  
      </div>


    
  
  </>;
}