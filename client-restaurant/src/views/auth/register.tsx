import { Box, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/Cliente.schema";
import MenuItem from '@mui/material/MenuItem';
import type { RegistrarClienteDTO } from "../../types/cliente.types"
import { useRegisterCliente } from "../../services/cliente.service";

export default function Register() {

  const tiposDocumento: { value: string; label: string; }[] = [
    { value: "DNI", label: "DNI" },
    { value: "CARNET", label: "Carnet de Extranjería" },
    { value: "PASAPORTE", label: "Pasaporte" },
  ];

  const Genero: { value: string; label: string; }[] = [
    { value: "M", label: "Masculino" },
    { value: "F", label: "Femenino" },
  ];

  const navigate = useNavigate();
  const { mutate } = useRegisterCliente();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegistrarClienteDTO>({
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
      cliente: {
        imagenCliente_url: "",
      },
    },
  });

  const doRegister = (data: RegistrarClienteDTO) => {
    mutate(data, {
      onSuccess: () => {
        alert("Tu cuenta ha sido creada, puedes iniciar sesión");
        navigate("/");
      },
      onError: () => {
        alert("Ocurrió un error al registrar");
      }
    });
  };
return (
  <Box
  sx={{
    width: "100%",
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-start",   // ← ya no queda tan arriba
    backgroundColor: "#f5f5f5",
    paddingTop: 4,               // ← baja el formulario
    paddingX: 2,
  }}
  >
    <Box
      component="form"
      onSubmit={handleSubmit(doRegister)}
      sx={{
        width: "100%",
        maxWidth: "620px",
        padding: 4,
        background: "#fff",
        borderRadius: "16px",
        boxShadow: 4,
      }}
    >
      {/* ⭐ Título más visible y separado */}
      <Typography
        variant="h4"
        textAlign="center"
        sx={{ mb: 4, fontWeight: "bold", color: "#333" }}
      >
        Registrarse
      </Typography>

      <Box
        sx={{
          display: "grid",
          gap: 2.5,
          gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
        }}
      >
        {/* CAMPOS */}
        <TextField
          {...register("credenciales.usuario")}
          label="Usuario"
          variant="outlined"
          error={!!errors.credenciales?.usuario}
          helperText={errors.credenciales?.usuario?.message}
        />

        <TextField
          {...register("credenciales.contrasena")}
          type="password"
          label="Contraseña"
          variant="outlined"
          error={!!errors.credenciales?.contrasena}
          helperText={errors.credenciales?.contrasena?.message}
        />

        <TextField
          {...register("persona.nombres")}
          label="Nombre Completo"
          variant="outlined"
          error={!!errors.persona?.nombres}
          helperText={errors.persona?.nombres?.message}
        />

        <TextField
          {...register("persona.apPaterno")}
          label="Apellido Paterno"
          variant="outlined"
          error={!!errors.persona?.apPaterno}
          helperText={errors.persona?.apPaterno?.message}
        />

        <TextField
          {...register("persona.apMaterno")}
          label="Apellido Materno"
          variant="outlined"
          error={!!errors.persona?.apMaterno}
          helperText={errors.persona?.apMaterno?.message}
        />

        <TextField
          select
          label="Género"
          {...register("persona.genero")}
          error={!!errors.persona?.genero}
          helperText={errors.persona?.genero?.message}
        >
          {Genero.map((op) => (
            <MenuItem key={op.value} value={op.value}>
              {op.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          select
          label="Tipo de documento"
          {...register("persona.tipoDocumento")}
          error={!!errors.persona?.tipoDocumento}
          helperText={errors.persona?.tipoDocumento?.message}
        >
          {tiposDocumento.map((op) => (
            <MenuItem key={op.value} value={op.value}>
              {op.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          {...register("persona.numDocumento")}
          label="N° de Documento"
          variant="outlined"
          error={!!errors.persona?.numDocumento}
          helperText={errors.persona?.numDocumento?.message}
        />

        <TextField
          {...register("persona.telefono")}
          label="Teléfono"
          variant="outlined"
          error={!!errors.persona?.telefono}
          helperText={errors.persona?.telefono?.message}
        />

        <TextField
          {...register("persona.correo")}
          label="Correo electrónico"
          variant="outlined"
          error={!!errors.persona?.correo}
          helperText={errors.persona?.correo?.message}
        />

        <TextField
          {...register("persona.fechaNacimiento")}
          label="Fecha de Nacimiento"
          type="date"
          InputLabelProps={{ shrink: true }}
          error={!!errors.persona?.fechaNacimiento}
          helperText={errors.persona?.fechaNacimiento?.message}
        />

        <TextField
          {...register("cliente.imagenCliente_url")}
          label="Imagen (URL)"
          variant="outlined"
          error={!!errors.cliente?.imagenCliente_url}
          helperText={errors.cliente?.imagenCliente_url?.message}
        />
      </Box>

      {/* ⭐ Botón principal destacado */}
      <Button
        fullWidth
        variant="contained"
        sx={{
          mt: 4,
          py: 1.4,
          fontSize: "1rem",
          fontWeight: "bold",
        }}
        type="submit"
      >
        Registrarse
      </Button>

      <Button
        fullWidth
        color="primary"
        variant="outlined"
        sx={{ mt: 2 }}
        onClick={() => navigate("/")}
      >
        Iniciar sesión
      </Button>
    </Box>
  </Box>
);


}
