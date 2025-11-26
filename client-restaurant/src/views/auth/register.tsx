import { Box, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/Cliente.schema";
import MenuItem from "@mui/material/MenuItem";
import type { RegistrarClienteDTO } from "../../types/cliente.types";
import { useRegisterCliente } from "../../services/cliente.service";

export default function Register() {
  const tiposDocumento = [
    { value: "DNI", label: "DNI" },
    { value: "CARNET", label: "Carnet de Extranjería" },
    { value: "PASAPORTE", label: "Pasaporte" },
  ];

  const Genero = [
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
      credenciales: { usuario: "", contrasena: "" },
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
      cliente: { imagenCliente_url: "" },
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
      },
    });
  };

  return (
    <Box
      sx={{
        width: "100%",
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
      }}
    >
      <Box
        component="form"
        onSubmit={handleSubmit(doRegister)}
        sx={{
          width: "100%",
          maxWidth: "850px",      // 👈 MÁS ANCHO COMO PEDISTE
          background: "#fff",
    p: 2,
    pt:4,

          display: "flex",
          flexDirection: "column",
        }}
      >
        <Typography
          variant="h4"
          textAlign="center"
          sx={{ mb: 1, fontWeight: "bold" }}
        >
          Registrarse
        </Typography>

        <Box
          sx={{
            display: "grid",
            gap:1,
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
          }}
        >
          <TextField
            {...register("credenciales.usuario")}
            label="Usuario"
            variant="outlined"
            error={!!errors.credenciales?.usuario}
            helperText={errors.credenciales?.usuario?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("credenciales.contrasena")}
            type="password"
            label="Contraseña"
            error={!!errors.credenciales?.contrasena}
            helperText={errors.credenciales?.contrasena?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.nombres")}
            label="Nombre Completo"
            error={!!errors.persona?.nombres}
            helperText={errors.persona?.nombres?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.apPaterno")}
            label="Apellido Paterno"
            error={!!errors.persona?.apPaterno}
            helperText={errors.persona?.apPaterno?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.apMaterno")}
            label="Apellido Materno"
            error={!!errors.persona?.apMaterno}
            helperText={errors.persona?.apMaterno?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            select
            label="Género"
            {...register("persona.genero")}
            error={!!errors.persona?.genero}
            helperText={errors.persona?.genero?.message}
            sx={{ backgroundColor: "#fafafa" }}
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
            sx={{ backgroundColor: "#fafafa" }}
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
            error={!!errors.persona?.numDocumento}
            helperText={errors.persona?.numDocumento?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.telefono")}
            label="Teléfono"
            error={!!errors.persona?.telefono}
            helperText={errors.persona?.telefono?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.correo")}
            label="Correo electrónico"
            error={!!errors.persona?.correo}
            helperText={errors.persona?.correo?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("persona.fechaNacimiento")}
            label="Fecha de Nacimiento"
            type="date"
            InputLabelProps={{ shrink: true }}
            error={!!errors.persona?.fechaNacimiento}
            helperText={errors.persona?.fechaNacimiento?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />

          <TextField
            {...register("cliente.imagenCliente_url")}
            label="Imagen (URL)"
            error={!!errors.cliente?.imagenCliente_url}
            helperText={errors.cliente?.imagenCliente_url?.message}
            sx={{ backgroundColor: "#fafafa" }}
          />
        </Box>

        <Button
          fullWidth
          variant="contained"
          sx={{
            mt: 3,
            py: 1.5,
            fontSize: "1rem",
            fontWeight: "bold",
            borderRadius: "10px",
          }}
          type="submit"
        >
          Registrarse
        </Button>

        <Button
          fullWidth
          variant="outlined"
          sx={{ mt: 2, py: 1.2, borderRadius: "10px" }}
          onClick={() => navigate("/")} type="button"
        >
          Iniciar sesión
        </Button>
      </Box>
    </Box>
  );
}
