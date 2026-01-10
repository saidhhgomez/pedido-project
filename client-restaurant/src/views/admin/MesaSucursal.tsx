import { Button, Card, CardContent, Typography, Grid, Box, CircularProgress } from "@mui/material";
import { useNavigate } from "react-router";
import { useState } from "react";
import { useCreateMesa, useDeleteMesa, useGetMesaById, useUpdateMesa } from "../../services/mesa.service";
import Swal from "sweetalert2";
import ModalEditarMesa from "../../components/Modals/ModalMesaEdit";
import FormModalMesa from "../../components/Modals/ModalMesa";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";

export default function MesaSucursales() {
  const navigate = useNavigate();
  const idSucursal = localStorage.getItem("idSucursal");

  const [openCrear, setOpenCrear] = useState(false);
  const [openEditar, setOpenEditar] = useState(false);
  const [mesaSeleccionada, setMesaSeleccionada] = useState<any>(null);

  const { data, isLoading, isError, refetch } = useGetMesaById(idSucursal);
  const { mutate: crearMesa } = useCreateMesa();
  const { mutate: actualizarMesa } = useUpdateMesa();
  const {mutate:eliminarMesa}=useDeleteMesa();

  // -------------------- Crear mesa --------------------
  const handleCrear = (data: any) => {
    crearMesa(
      { ...data, idSucursal: Number(idSucursal) },
      {
        onSuccess: (response) => {
          refetch();
          Swal.fire({
            position: "center",
            icon: "success",
            title: response.data.message,
            showConfirmButton: false,
            timer: 1200,
          });
          setOpenCrear(false);
        },
        onError: () => {
          Swal.fire({
            position: "center",
            icon: "error",
            title: "Error al registrar mesa",
            showConfirmButton: false,
            timer: 1200,
          });
        },
      }
    );
  };

  // -------------------- Editar mesa --------------------
  const handleEditar = (mesaActualizada: any) => {
    if (!mesaSeleccionada) return;
    
    console.log(mesaActualizada)

    actualizarMesa(
      { id: mesaSeleccionada.idMesa, data: mesaActualizada },
      {
        onSuccess: (response) => {
          refetch();
          Swal.fire({
            position: "center",
            icon: "success",
            title: response.data.message,
            showConfirmButton: false,
            timer: 1200,
          });
          setOpenEditar(false);
          setMesaSeleccionada(null);
        },
        onError: (response) => {
          Swal.fire({
            position: "center",
            icon: "error",
            title: response.message,
            showConfirmButton: false,
            timer: 1200,
          });
        },
      }
    );
  };

  // -------------------- Eliminar mesa --------------------
  const handleEliminar = (idMesa: number) => {
    Swal.fire({
      title: "¿Eliminar mesa?",
      text: "Esta acción no se puede deshacer",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        eliminarMesa(idMesa, {
          onSuccess: () => {
            refetch();
            Swal.fire({
              icon: "success",
              title: "Mesa eliminada",
              showConfirmButton: false,
              timer: 1200,
            });
          },
          onError: () => {
            Swal.fire({
              icon: "error",
              title: "Error al eliminar mesa",
              showConfirmButton: false,
              timer: 1200,
            });
          },
        });
      }
    });
  };

  // -------------------- Render --------------------
  // Filtramos solo mesas físicas (descartando "Online")
  const mesasFisicas = data?.data?.filter((mesa: any) => mesa.numeroMesa !== "ONLINE") || [];

  return (


    
    <>
      <Typography variant="h4" gutterBottom>Bienvenido Mesas</Typography>
      <Typography variant="subtitle1" gutterBottom>ID Sucursal: {idSucursal}</Typography>

      {/* Botones Crear y Volver */}
      <Box sx={{ display: "flex", gap: 1, mb: 2 }}>
        <Button
          variant="contained"
          color="primary"
          onClick={() => setOpenCrear(true)}
        >
          Crear Mesa
        </Button>

        <Button
          variant="contained"
          color="secondary"
          onClick={() => {
            localStorage.removeItem("idSucursal");
            navigate(-1);
          }}
        >
          Volver
        </Button>
      </Box>

      {/* Mostrar mesas */}
      <Grid container spacing={2}>
        {isLoading ? (
          <Box sx={{ width: "100%", display: "flex", justifyContent: "center", mt: 4 }}>
            <CircularProgress />
          </Box>
        ) : isError ? (
          <Typography color="error">Error al cargar mesas.</Typography>
        ) : mesasFisicas.length === 0 ? (
          <Typography>No hay mesas en esta sucursal.</Typography>
        ) : (
          mesasFisicas.map((mesa: any) => (
            <Grid item xs={12} sm={6} md={4} key={mesa.idMesa || mesa.numeroMesa}>
              <Card>
                <CardContent>
                  <Typography variant="h6">Mesa: {mesa.numeroMesa}</Typography>
                  <Typography>Capacidad: {mesa.capacidad}</Typography>
                  <Typography>Ubicación: {mesa.ubicacion}</Typography>
                  <Typography>Estado: {mesa.estado}</Typography>

                  <Box sx={{ display: "flex", gap: 1, mt: 2 }}>

               
                      
                                        <Button
                      variant="contained"
                      color="info"
                      size="small"
                      startIcon={<EditIcon />}
                      onClick={() => {
                        setMesaSeleccionada(mesa);
                        setOpenEditar(true);
                      }}
                    >
                      Editar
                    </Button>


                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      startIcon={<DeleteIcon />}
                      onClick={() => handleEliminar(mesa.idMesa)}
                    >
                      Eliminar
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))
        )}
      </Grid>

      {/* Modal Crear */}
      <FormModalMesa
        open={openCrear}
        onClose={() => setOpenCrear(false)}
        onSubmit={handleCrear}
      />

      {/* Modal Editar */}
      <ModalEditarMesa
        open={openEditar}
        mesa={mesaSeleccionada}
        onClose={() => {
          setOpenEditar(false);
          setMesaSeleccionada(null);
        }}
        onSubmit={handleEditar}
      />
    </>
  );
}
