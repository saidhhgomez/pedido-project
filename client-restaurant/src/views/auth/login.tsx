import { useDispatch } from "react-redux";
import { login } from "../../store/slices/auth.slice";
import { useLogin } from "../../services/auth.service";
import { useNavigate } from "react-router";
import { Box, TextField, Button, Typography, Card } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { loginSchema } from "../../validators/login.schema";
import Swal from "sweetalert2";

export default function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { mutate } = useLogin();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(loginSchema),
  });

const doLogin = ({
  usuario,
  contrasena,
}: {
  usuario: string;
  contrasena: string;
}) => {
  mutate(
    { usuario, contrasena },
    {
      onSuccess: (response) => {

                Swal.fire({
                  icon: "success",
                  title: "Cliente registrado",
                  timer: 1500,
                  showConfirmButton: false,
                });
        console.log(response);

        dispatch(login(response.data)); // ✅
      },
      onError: () => {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Usuario y/o Contraseña Incorrecta",
          showConfirmButton: false,
          timer: 1200,
        });
      },
    }
  );
};
  
  return (
    <Card
      sx={{
        width: "400px",
        padding: 4,
        borderRadius: 4,
        boxShadow: "0 8px 20px rgba(0,0,0,0.3)", 
        backgroundColor: "white", 
      }}
    >
      <Box
        component="form"
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 2,
          textAlign: "center",
        }}
        onSubmit={handleSubmit(doLogin)}
      >
        <Typography variant="h4" sx={{ fontWeight: "bold", color: "#d32f2f" }}>
          Iniciar sesión
        </Typography>

        <TextField
          {...register("usuario")}
          label="Usuario"
          variant="outlined"
          sx={{
            backgroundColor: "#fff",
            borderRadius: 1,
          }}
        />
        {errors.usuario && (
          <Typography color="error">{errors.usuario.message}</Typography>
        )}

        <TextField
          {...register("contrasena")}
          type="password"
          label="Contraseña"
          variant="outlined"
          sx={{
            backgroundColor: "#fff",
            borderRadius: 1,
          }}
        />
        {errors.contrasena && (
          <Typography color="error">{errors.contrasena.message}</Typography>
        )}

        <Button
          variant="contained"
          type="submit"
          sx={{
            backgroundColor: "#d32f2f",
            "&:hover": { backgroundColor: "#b71c1c" },
            paddingY: 1.2,
            fontWeight: "bold",
          }}
        >
          Iniciar sesión
        </Button>

        <Button
          variant="outlined"
          onClick={() => navigate("/register")}
          sx={{
            borderColor: "#d32f2f",
            color: "#d32f2f",
            "&:hover": {
              borderColor: "#b71c1c",
              color: "#b71c1c",
            },
            paddingY: 1.2,
            fontWeight: "bold",
          }}
        >
          Registrarse
        </Button>
      </Box>
    </Card>
  );
}
