import { useNavigate } from "react-router";
import { useSelector } from "react-redux";
import { selectPerfilEmpleado } from "../../store/slices/auth.slice";
import { useGetMesaMesero } from "../../services/mesa.service";
import { useState } from "react";

import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Grid,
  Typography,
} from "@mui/material";

import { useHablitarMesa } from "../../services/pedido.service";

// -------------------- Types --------------------
interface Mesa {
  idMesa: number;
  idSucursal: number;
  numeroMesa: string | null;
  capacidad: number;
  ubicacion: string;
  estado: string;
}

// -------------------- Component --------------------
export default function Mesas() {
  const navigate = useNavigate();
  const perfil = useSelector(selectPerfilEmpleado);

const { mutate: liberarMesa,isPending:cargando} = useHablitarMesa();


const [mesaLiberando, setMesaLiberando] = useState<number | null>(null);



  const handleLiberarMesa = (idMesa: number) => {

      setMesaLiberando(idMesa); // indica cuál se está liberando

    liberarMesa(
      { idMesa },
      {
        onSuccess: () => {
          refetch();
          alert("Liberando Mesa");
                  setMesaLiberando(null); // limpiar estado


        },
        onError: () => {alert("Error al cerrar el pedido")


                  setMesaLiberando(null); // limpiar estado

        },

      }
    );; // ajusta según tu API
  };




  const {
    data: mesas = [],
    isLoading,
    isError,
    refetch
  } = useGetMesaMesero(perfil?.idSucursal);

  // -------------------- Helpers --------------------
  const getEstadoStyles = (estado: string) => {
    switch (estado?.toLowerCase()) {
      case "disponible":
        return {
          bg: "#e8f5e9",
          border: "#2e7d32",
          text: "#1b5e20",
        };
      case "ocupada":
      case "ocupado":
        return {
          bg: "#fdecea",
          border: "#c62828",
          text: "#b71c1c",
        };
      default:
        return {
          bg: "#eeff00ff",
          border: "#e6d925ff",
          text: "#424242",
        };
    }
  };

  // -------------------- Data --------------------
  const mesasFisicas: Mesa[] = mesas.filter(
    (mesa) =>
      typeof mesa.numeroMesa === "string" &&
      mesa.numeroMesa.trim() !== "" &&
      mesa.numeroMesa !== "ONLINE"
  );

  // -------------------- Render --------------------
  return (
    <>
      <Typography variant="h4" gutterBottom>
        Gestión de Mesas
      </Typography>

      <Typography variant="subtitle1" gutterBottom>
        Sucursal ID: {perfil?.idSucursal ?? "—"}
      </Typography>

      <Box sx={{ mb: 2 }}>
        <Button variant="outlined" onClick={() => navigate(-1)}>
          Volver
        </Button>
      </Box>

      <Grid container spacing={2}>
        {isLoading ? (
          <Grid item xs={12} textAlign="center">
            <CircularProgress />
          </Grid>
        ) : isError ? (
          <Grid item xs={12}>
            <Typography color="error">
              Error al cargar las mesas
            </Typography>
          </Grid>
        ) : mesasFisicas.length === 0 ? (
          <Grid item xs={12}>
            <Typography>No hay mesas físicas registradas</Typography>
          </Grid>
        ) : (
          mesasFisicas.map((mesa) => {
            const styles = getEstadoStyles(mesa.estado);

            return (
              <Grid item xs={12} sm={6} md={4} key={mesa.idMesa}>
                <Card
                  sx={{
                    height: "100%",
                    backgroundColor: styles.bg,
                    border: `2px solid ${styles.border}`,
                  }}
                >
                  <CardContent>
                    <Typography variant="h6" sx={{ color: styles.text }}>
                      Mesa {mesa.numeroMesa}
                    </Typography>

                    <Typography sx={{ color: styles.text }}>
                      Capacidad: {mesa.capacidad}
                    </Typography>

                    <Typography sx={{ color: styles.text }}>
                      Ubicación: {mesa.ubicacion}
                    </Typography>
                                        <Typography sx={{ color: styles.text }}>
                      Estado: {mesa.estado}
                    </Typography>
{mesa.estado.toLowerCase() === "disponible" && (
  <Button
    sx={{ mt: 1 }}
    variant="contained"
    color="info"
    size="small"
    onClick={() => navigate(`/pedido-presencial/${mesa.idMesa}`)}
  >
    Pedido
  </Button>
)}

{mesa.estado.toLowerCase() === "ocupada" && (
  <Button
    sx={{ mt: 1 }}
    variant="contained"
    color="secondary"
    size="small"
    onClick={() => navigate(`/resumenPedido/${mesa.idMesa}`)}
  >
    Ver Pedido
  </Button>
)}

{mesa.estado.toLowerCase() === "liberando" && (
<Button
  sx={{ mt: 1 }}
  variant="contained"
  color="warning"
  size="small"
  onClick={() => handleLiberarMesa(mesa.idMesa)}
  disabled={mesaLiberando === mesa.idMesa} // solo esta mesa
>
  {mesaLiberando === mesa.idMesa ? "Procesando..." : "Liberando"}
</Button>

)}
                    
                  </CardContent>
                </Card>
              </Grid>
            );
          })
        )}
      </Grid>


    </>
  );
}
