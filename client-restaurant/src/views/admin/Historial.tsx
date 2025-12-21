import { useState } from "react";
import { Box, Grid, Button, Avatar, Typography, Card, CardContent, CardActions } from "@mui/material";
import { useGetEmpleadoHistorialCompleto, useGetEmpleadoActivosVencimiento, useRemoveEmpleado } from "../../services/empleado.service";
import DeleteIcon from "@mui/icons-material/Delete";
import Swal from "sweetalert2";

export default function HistorialAdmin() {
  const [mostrarActivosVencidos, setMostrarActivosVencidos] = useState(false);

  // Queries
  const historialCompletoQuery = useGetEmpleadoHistorialCompleto();
  const activosVencidosQuery = useGetEmpleadoActivosVencimiento();
  const eliminarBajaEmpleado=useRemoveEmpleado();

  const empleados = mostrarActivosVencidos
    ? activosVencidosQuery.data?.data || []
    : historialCompletoQuery.data?.data || [];

  const isLoading = mostrarActivosVencidos
    ? activosVencidosQuery.isLoading
    : historialCompletoQuery.isLoading;



      // -------------------- Eliminar mesa --------------------
const handleEliminar = (idEmpleado: number) => {
  Swal.fire({
    title: "¿Eliminar empleado?",
    text: "Esta acción no se puede deshacer",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Sí, eliminar",
    cancelButtonText: "Cancelar",
  }).then((result) => {
    if (result.isConfirmed) {
      // Aquí usamos .mutate()
      eliminarBajaEmpleado.mutate(idEmpleado, {
        onSuccess: () => {
          // Refrescar los datos después de eliminar
          historialCompletoQuery.refetch();
          activosVencidosQuery.refetch();

          Swal.fire({
            icon: "success",
            title: "Empleado De Baja",
            showConfirmButton: false,
            timer: 1200,
          });
        },
        onError: (err) => {
          Swal.fire({
            icon: "error",
            title: err.response.data.mensaje,
            showConfirmButton: false,
            timer: 1200,
          });
        },
      });
    }
  });
};


  if (isLoading) return <Typography>Cargando empleados...</Typography>;

  return (
    <Box p={2}>
      <Typography variant="h4" mb={2}>Historial de Empleados</Typography>

      <Box mb={2}>
        <Button
          variant="contained"
          onClick={() => setMostrarActivosVencidos(!mostrarActivosVencidos)}
        >
          {mostrarActivosVencidos ? "Mostrar Historial Completo" : "Mostrar Activos/Vencidos"}
        </Button>
      </Box>

      <Grid container spacing={2}>
        {empleados.map((emp: any) => (
          <Grid item xs={12} sm={6} md={4} key={emp.idEmpleado}>
            <Card>
              <CardContent>
                <Grid container spacing={1} alignItems="center">
                  <Grid item>
                    <Avatar src={emp.fotoUrl} alt={emp.nombre} sx={{ width: 56, height: 56 }} />
                  </Grid>
                  <Grid item>
                    <Typography variant="h6">{emp.nombre}</Typography>
                    <Typography variant="body2">DNI: {emp.dni}</Typography>
                    <Typography variant="body2">Rol: {emp.rol}</Typography>
                    <Typography variant="body2">Sucursal: {emp.sucursal}</Typography>
                    <Typography variant="body2">Estado Empleado: {emp.estadoEmpleado}</Typography>
                    <Typography variant="body2">Estado Contrato: {emp.estadoContrato}</Typography>
                    <Typography variant="body2">Inicio: {emp.fechaInicio}</Typography>
                    <Typography variant="body2">Fin: {emp.fechaFin}</Typography>
                  </Grid>
                </Grid>
              </CardContent>
              <CardActions>
                {emp.pdfDownloadUrl && (
                  <Button
                    variant="outlined"
                    color="primary"
                    href={emp.pdfDownloadUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Descargar PDF
                  </Button>
                )}

                {emp.estadoEmpleado==="activo"&&

                   <Button
                      variant="contained"
                      color="error"
                      size="small"
                      startIcon={<DeleteIcon />}
                      onClick={() => handleEliminar(emp.idEmpleado)}
                    >
                      Eliminar
                    </Button>


                }

              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
