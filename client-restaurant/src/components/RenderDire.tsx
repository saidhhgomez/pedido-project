import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import CardActionArea from '@mui/material/CardActionArea';
import CardActions from '@mui/material/CardActions';
import { useGetDirecciones, useRemoveDireccion } from '../services/direction.service';
import type { Direccion } from '../views/client/direction';
import { useSelector } from 'react-redux';
import { selectIdCliente } from '../store/slices/auth.slice';





export default function MediaCard({Direccion}: {Direccion : Direccion}) {
  const { mutate} =useRemoveDireccion();
  const idcliente=useSelector(selectIdCliente);
  const {refetch}=useGetDirecciones(idcliente);
  
  const DoRemoveDireccion= ()=>{
    mutate(Direccion.idDireccion, {
      onSuccess: ()=>{
        refetch();
        alert("Se elimino exitosamente")
      },onError:()=>{
        alert("Error en eliminar")
      }
  

    }
    )
  }

  return (
   <Card sx={{ maxWidth: 345 }}>
      <CardActionArea>

        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            Direccion
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Direccion.departamento}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Direccion.direccion}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Direccion.distrito}
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Direccion.provincia}
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Direccion.referencia}
          </Typography>
        </CardContent>
      </CardActionArea>
      <CardActions>
        <Button size="small" color="primary" onClick={DoRemoveDireccion}>
          Eliminar
        </Button>
      </CardActions>
    </Card>
  );
}