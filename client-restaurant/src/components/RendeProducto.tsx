import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import type {   Plate, PlateUpdate } from '../types/Plate.type';
import { Box, Button, CardActionArea } from '@mui/material';
import { selectRol } from '../store/slices/auth.slice';
import { useSelector } from 'react-redux';
import { useGetPlate, useRemovePlate, useUpdatePlate } from '../services/plate.service';
import EditModalPlate from './Modals/FormModalEdit';
import { useState } from 'react';


export default function CardPlate({
  Plate,
}: {
  Plate: Plate;
}) {

      const [openEdit, setOpenEdit] = useState(false);

    const rol=useSelector(selectRol);
      const { mutate} =useRemovePlate();
      const {refetch}=useGetPlate();
      const {mutate:updatePlate}=useUpdatePlate();

    const DoRemoveProduct= ()=>{
    mutate(Plate.idCatalogo, {
      onSuccess: ()=>{
        refetch();
        alert("Se elimino exitosamente")
      },onError:()=>{
        alert("Error en eliminar")
      }
  

    }
    )
  }


const handleEditSubmit = (data: Plate) => {
  const { idCatalogo, ...updateData } = data; // quitar idCatalogo
  updatePlate(
    { id: Plate.idCatalogo, data: updateData },
    {
      onSuccess: () => {
        refetch();
        setOpenEdit(false);
        alert("Plato editado correctamente");
      },
    }
  );
};



return (
  <>
    <Card sx={{ maxWidth: 345 }}>
      <CardActionArea>
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            Plato
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Plate.nombre}
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Plate.categoria}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Plate.estadoplato}
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Plate.imagenPlatoUrl}
          </Typography>

          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {Plate.stock}
          </Typography>


        </CardContent>
      </CardActionArea>
                {rol === "empleado" && (
            <Box sx={{ display: "flex", gap: 1 }}>
              <Button
                size="small"
                variant="contained"
                color="error"
                onClick={() => DoRemoveProduct()}
              >
                Eliminar
              </Button>

              <Button
                size="small"
                variant="contained"
                color="primary"
                onClick={() => setOpenEdit(true)}
              >
                Editar
              </Button>
            </Box>
          )}
      <CardActions></CardActions>
    </Card>

      <EditModalPlate
        open={openEdit}
        onClose={() => setOpenEdit(false)}
        onSubmit={handleEditSubmit}
        initialData={Plate}
      />
  </>
);
}