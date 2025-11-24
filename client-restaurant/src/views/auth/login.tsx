import { useDispatch } from "react-redux";
import { login } from "../../store/slices/auth.slice";
import { useLogin } from "../../services/auth.service";
import { useNavigate } from "react-router";
import { Box, TextField, Button, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { loginSchema } from "../../validators/login.schema";

export default function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { mutate, } = useLogin();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(loginSchema),
  });

  const doLogin = ({
    usuario,
    contraseña,
  }: {
    usuario: string;
    contraseña: string;
  }) => {
    mutate(
      {
        usuario,
        contraseña,
      },
      {
        onSuccess: (data) => {
          dispatch(login({ token: data?.data?.token,rol:data?.data?.rol,nombre:data?.data?.nombre}));
        },onError: (error:{message:string})=> {
            console.log(" Error en iniciar session",error.message)             
        },
      },    
    );
  };
  return (
    <Box
      sx={{
        width: "400px",
        display: "flex",
        flexDirection: "column",
        gap: 2,
        justifyContent: "center",
        textAlign: "center",
      }}
      component="form"
      onSubmit={handleSubmit(doLogin)}
    >
      <Typography variant="h4">Iniciar sesión</Typography>
      <TextField {...register("usuario")} label="Usuario" variant="outlined" />
      {errors.usuario && (
        <Typography color="error">{errors.usuario.message}</Typography>
      )}
      <TextField
        {...register("contraseña")}
        type="password"
        label="Contraseña"
        variant="outlined"
      />
      {errors.contraseña && (
        <Typography color="error">{errors.contraseña.message}</Typography>
      )}
      <Button variant="contained" type="submit">
        Iniciar sesión
      </Button>
      <Button
        color="primary"
        variant="outlined"
        onClick={() => navigate("/register")}
      >
        Registrarse
      </Button>
    </Box>
  );
}