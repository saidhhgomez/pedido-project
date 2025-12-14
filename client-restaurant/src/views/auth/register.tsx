import { Box, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/Cliente.schema";
import MenuItem from "@mui/material/MenuItem";
import { useRegistrarClienteFormData } from "../../services/cliente.service";
import Swal from "sweetalert2";

export default function Register() {
  const navigate = useNavigate();
  const { mutate } = useRegistrarClienteFormData();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(registrarEmpleadoSchema),
  });

  const doRegister = (form) => {
    mutate(
      {
        // 🔥 JSON EXACTO PARA JACKSON
        data: {
          credenciales: form.credenciales,
          persona: form.persona,
          cliente: {
            imagenCliente_url: "",
          },
        },

        // 🔥 FILE REAL
        imagenCliente: form.imagenCliente[0],
      },
      {
        onSuccess: () => {
          Swal.fire({
            icon: "success",
            title: "Cliente registrado correctamente",
            timer: 1500,
            showConfirmButton: false,
          });
          navigate("/");
        },
        onError: () => {
          Swal.fire({
            icon: "error",
            title: "Error al registrar cliente",
            timer: 1500,
            showConfirmButton: false,
          });
        },
      }
    );
  };

  return (
    <Box sx={{ minHeight: "100vh", background: "#e74c3c", p: 4 }}>
      <Box
        component="form"
        onSubmit={handleSubmit(doRegister)}
        sx={{ maxWidth: 850, mx: "auto", background: "#fff", p: 4 }}
      >
        <Typography variant="h4" textAlign="center" mb={2}>
          Registrarse
        </Typography>

        <TextField {...register("credenciales.usuario")} label="Usuario" fullWidth />
        <TextField {...register("credenciales.contrasena")} type="password" label="Contraseña" fullWidth />

        <TextField {...register("persona.nombres")} label="Nombres" fullWidth />
        <TextField {...register("persona.apPaterno")} label="Apellido paterno" fullWidth />
        <TextField {...register("persona.apMaterno")} label="Apellido materno" fullWidth />

        <TextField select {...register("persona.genero")} label="Género" fullWidth>
          <MenuItem value="M">Masculino</MenuItem>
          <MenuItem value="F">Femenino</MenuItem>
        </TextField>

        <TextField select {...register("persona.tipoDocumento")} label="Tipo Documento" fullWidth>
          <MenuItem value="DNI">DNI</MenuItem>
          <MenuItem value="CARNET">Carnet</MenuItem>
          <MenuItem value="PASAPORTE">Pasaporte</MenuItem>
        </TextField>

        <TextField {...register("persona.numDocumento")} label="Documento" fullWidth />
        <TextField {...register("persona.telefono")} label="Teléfono" fullWidth />
        <TextField {...register("persona.correo")} label="Correo" fullWidth />
        <TextField
          {...register("persona.fechaNacimiento")}
          type="date"
          InputLabelProps={{ shrink: true }}
          label="Fecha Nacimiento"
          fullWidth
        />

        {/* 🔥 INPUT FILE REAL */}
        <input type="file" accept="image/*" {...register("imagenCliente")} />

        <Button type="submit" fullWidth variant="contained" sx={{ mt: 3 }}>
          Registrarse
        </Button>
      </Box>
    </Box>
  );
}
