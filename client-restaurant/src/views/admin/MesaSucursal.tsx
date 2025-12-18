  import { Button, Card, CardContent, Typography, Grid } from "@mui/material";
  import { useNavigate } from "react-router";
  import { useState } from "react";
  import { useCreateMesa, useGetMesa, useUpdateMesa } from "../../services/mesa.service";
  import Swal from "sweetalert2";
  import ModalEditarMesa from "../../components/Modals/ModalMesaEdit";
  import FormModalMesa from "../../components/Modals/ModalMesa";

  export default function MesaSucursales() {
    const navigate = useNavigate();
    const idSucursal = localStorage.getItem("idSucursal");

    const [openCrear, setOpenCrear] = useState(false);
    const [openEditar, setOpenEditar] = useState(false);
    const [mesaSeleccionada, setMesaSeleccionada] = useState<any>(null);

    const { data, refetch } = useGetMesa();
    const { mutate: crearMesa } = useCreateMesa();
    const { mutate: actualizarMesa } = useUpdateMesa();

    // -------------------- Crear mesa --------------------
    const handleCrear = (data: any) => {
      crearMesa(
        { ...data, idSucursal: Number(idSucursal) },
        {
          onSuccess: () => {
            refetch();
            Swal.fire({
              position: "center",
              icon: "success",
              title: "Mesa Registrada",
              showConfirmButton: false,
              timer: 1200,
            });
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
    {
      console.log(JSON.stringify(mesaActualizada))
    }
    actualizarMesa(
      { id: mesaSeleccionada.idMesa, data: mesaActualizada }, // ID en URL, resto en body
      {
        onSuccess: () => {
          refetch();
          Swal.fire({
            position: "center",
            icon: "success",
            title: "Mesa Actualizada",
            showConfirmButton: false,
            timer: 1200,
          });
        },
        onError: () => {
          Swal.fire({
            position: "center",
            icon: "error",
            title: "Error al actualizar mesa",
            showConfirmButton: false,
            timer: 1200,
          });
        },
      }
    );
  };

    // -------------------- Filtrar mesas por sucursal --------------------
    const mesasFiltradas = data?.data?.filter(
      (mesa: any) => mesa.idSucursal.toString() === idSucursal
    );

    return (
      <>
        <h1>Bienvenido Mesas</h1>
        <p>ID Sucursal: {idSucursal}</p>

        {/* Botón Crear */}
        <Button
          variant="contained"
          color="primary"
          onClick={() => setOpenCrear(true)}
          sx={{ mb: 2 }}
        >
          Crear Mesa
        </Button>

        {/* Botón Volver */}
        <Button
          variant="contained"
          color="secondary"
          onClick={() => {
            localStorage.removeItem("idSucursal");
            navigate(-1);
          }}
          sx={{ mb: 2, ml: 1 }}
        >
          Volver
        </Button>

        {/* Mostrar mesas filtradas */}
        <Grid container spacing={2}>
          {mesasFiltradas && mesasFiltradas.length > 0 ? (
            mesasFiltradas.map((mesa: any) => (
              <Grid item xs={12} sm={6} md={4} key={mesa.idMesa}>
                <Card>
                  <CardContent>
                    <Typography variant="h6">Mesa: {mesa.numeroMesa}</Typography>
                    <Typography>Capacidad: {mesa.capacidad}</Typography>
                    <Typography>Ubicación: {mesa.ubicacion}</Typography>
                    <Typography>Estado: {mesa.estado}</Typography>

                    <Button
                      variant="outlined"
                      color="primary"
                      size="small"
                      sx={{ mt: 2 }}
                      onClick={() => {
                        setMesaSeleccionada(mesa);
                        setOpenEditar(true);
                      }}
                    >
                      Editar
                    </Button>
                  </CardContent>
                </Card>
              </Grid>
            ))
          ) : (
            <Typography>No hay mesas en esta sucursal.</Typography>
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
