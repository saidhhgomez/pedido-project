import { Box, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/empleado.schema";
import { useCrearEmpleado } from "../../services/empleado.service";
import type { RegistrarEmpleadoDTO } from "../../types/empleado.types";

export default function RegistrarEmpleado() {
  const navigate = useNavigate();
  const { mutate } = useCrearEmpleado();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegistrarEmpleadoDTO>({
    resolver: yupResolver(registrarEmpleadoSchema),
    defaultValues: {
      credenciales: {
        usuario: "",
        contrasena: "",
      },
      persona: {
        nombres: "",
        apPaterno: "",
        apMaterno: "",
        genero: "",
        tipoDocumento: "",
        numDocumento: "",
        telefono: "",
        correo: "",
        fechaNacimiento: "",
      },
      empleado: {
        direccion: "",
        estadoEmpleado: "",
        imagenConductor_url: "",
      },
    },
  });

  const onSubmit = (data: RegistrarEmpleadoDTO) => {
    mutate(data, {
      onSuccess: () => {
        alert("Empleado registrado correctamente");
        navigate("/");
      },
    });
  };

  return (
    <Box
      sx={{
        width: "400px",
        display: "flex",
        flexDirection: "column",
        gap: 2,
        textAlign: "center",
        margin: "0 auto",
      }}
      component="form"
      onSubmit={handleSubmit(onSubmit)}
    >
      <Typography variant="h4">Registrar Empleado</Typography>

      {/* --- CREDENCIALES --- */}
      <TextField
        label="Usuario"
        {...register("credenciales.usuario")}
      />
      {errors.credenciales?.usuario && (
        <Typography color="error">
          {errors.credenciales.usuario.message}
        </Typography>
      )}

      <TextField
        label="Contraseña"
        type="password"
        {...register("credenciales.contrasena")}
      />
      {errors.credenciales?.contrasena && (
        <Typography color="error">
          {errors.credenciales.contrasena.message}
        </Typography>
      )}

      {/* --- PERSONA --- */}
      <TextField label="Nombres" {...register("persona.nombres")} />
      <TextField label="Apellido paterno" {...register("persona.apPaterno")} />
      <TextField label="Apellido materno" {...register("persona.apMaterno")} />
      <TextField label="Género" {...register("persona.genero")} />
      <TextField label="Tipo documento" {...register("persona.tipoDocumento")} />
      <TextField label="Nro documento" {...register("persona.numDocumento")} />
      <TextField label="Teléfono" {...register("persona.telefono")} />
      <TextField label="Correo" {...register("persona.correo")} />

      <TextField
        type="date"
        label="Fecha nacimiento"
        InputLabelProps={{ shrink: true }}
        {...register("persona.fechaNacimiento")}
      />

      {/* --- EMPLEADO --- */}
      <TextField label="Dirección" {...register("empleado.direccion")} />
      <TextField label="Estado" {...register("empleado.estadoEmpleado")} />
      <TextField
        label="Imagen (URL)"
        {...register("empleado.imagenConductor_url")}
      />

      <Button variant="contained" type="submit">
        Registrar
      </Button>

      <Button variant="outlined" onClick={() => navigate("/")}>
        Volver
      </Button>
    </Box>
  );
}
