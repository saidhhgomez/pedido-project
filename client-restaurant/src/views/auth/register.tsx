import { Box, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/Cliente.schema";
import MenuItem from "@mui/material/MenuItem";
import type { RegistrarClienteDTO } from "../../types/cliente.types";
import { useRegistrarClienteFormData } from "../../services/cliente.service";
import Swal from "sweetalert2";

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
  const { mutate } = useRegistrarClienteFormData();

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
    },
  });

  // 🔥 ENVÍO CORRECTO COMO MULTIPART
  const doRegister = (data: RegistrarClienteDTO) => {
    const formData = new FormData();

    // JSON EXACTO que espera el backend
    formData.append(
      "data",
      JSON.stringify({
        credenciales: data.credenciales,
        persona: data.persona,
        cliente: {
          imagenCliente_url: "",
        },
      })
    );

    // Archivo
    if (data.imagenCliente && data.imagenCliente.length > 0) {
      formData.append("imagenCliente", data.imagenCliente[0]);
    }

    mutate(formData, {
      onSuccess: () => {
        Swal.fire({
          position: "center",
          icon: "success",
          title: "Cliente registrado correctamente",
          showConfirmButton: false,
          timer: 1500,
        });
        navigate("/");
      },
      onError: () => {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Error al registrar cliente",
          showConfirmButton: false,
          timer: 1500,
        });
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
        backgroundColor: "#e74c3c",
        p: 4,
        overflowY: "auto",
      }}
    >
      <Box
        component="form"
        onSubmit={handleSubmit(doRegister)}
        sx={{
          width: "100%",
          maxWidth: "850px",
          background: "#fff",
          p: 4,
          borderRadius: "12px",
          boxShadow: "0 4px 20px rgba(0,0,0,0.3)",
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Typography
          variant="h4"
          textAlign="center"
          sx={{ mb: 2, fontWeight: "bold", color: "#e74c3c" }}
        >
          Registrarse
        </Typography>

        {/* GRID */}
        <Box
          sx={{
            display: "grid",
            gap: 2,
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
          }}
        >
          <TextField
            {...register("credenciales.usuario")}
            label="Usuario"
            error={!!errors.credenciales?.usuario}
            helperText={errors.credenciales?.usuario?.message}
          />

          <TextField
            {...register("credenciales.contrasena")}
            type="password"
            label="Contraseña"
            error={!!errors.credenciales?.contrasena}
            helperText={errors.credenciales?.contrasena?.message}
          />

          <TextField
            {...register("persona.nombres")}
            label="Nombre Completo"
            error={!!errors.persona?.nombres}
            helperText={errors.persona?.nombres?.message}
          />

          <TextField
            {...register("persona.apPaterno")}
            label="Apellido Paterno"
            error={!!errors.persona?.apPaterno}
            helperText={errors.persona?.apPaterno?.message}
          />

          <TextField
            {...register("persona.apMaterno")}
            label="Apellido Materno"
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
            error={!!errors.persona?.numDocumento}
            helperText={errors.persona?.numDocumento?.message}
          />

          <TextField
            {...register("persona.telefono")}
            label="Teléfono"
            error={!!errors.persona?.telefono}
            helperText={errors.persona?.telefono?.message}
          />

          <TextField
            {...register("persona.correo")}
            label="Correo electrónico"
            error={!!errors.persona?.correo}
            helperText={errors.persona?.correo?.message}
          />

          <TextField
            {...register("persona.fechaNacimiento")}
            type="date"
            label="Fecha de Nacimiento"
            InputLabelProps={{ shrink: true }}
            error={!!errors.persona?.fechaNacimiento}
            helperText={errors.persona?.fechaNacimiento?.message}
          />

          {/* 📷 INPUT FILE */}
          <Box>
            <Typography variant="body2">Imagen del cliente</Typography>
            <input
              type="file"
              accept="image/*"
              {...register("")}
            />
          </Box>
        </Box>

        <Button
          type="submit"
          variant="contained"
          sx={{
            mt: 3,
            py: 1.5,
            fontWeight: "bold",
            backgroundColor: "#e74c3c",
          }}
        >
          Registrarse
        </Button>

        <Button
          variant="outlined"
          sx={{
            mt: 2,
            borderColor: "#e74c3c",
            color: "#e74c3c",
            fontWeight: "bold",
          }}
          onClick={() => navigate("/")}
        >
          Iniciar sesión
        </Button>
      </Box>
    </Box>
  );
}
