import { useState } from "react";
import { Box, Typography, TextField, Button, MenuItem } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import Swal from "sweetalert2";
import { registrarEmpleadoSchema } from "../../validators/Cliente.schema";
import type { RegistrarClienteDTO } from "../../types/cliente.types";
import { useRegistrarClienteFormData } from "../../services/cliente.service";

export default function Register() {
  const navigate = useNavigate();
  const { mutate } = useRegistrarClienteFormData();
  const [imagenCliente, setImagenCliente] = useState<File | null>(null);

  const tiposDocumento = [
    { value: "DNI", label: "DNI" },
    { value: "CARNET", label: "Carnet de Extranjería" },
    { value: "PASAPORTE", label: "Pasaporte" },
  ];

  const Genero = [
    { value: "M", label: "Masculino" },
    { value: "F", label: "Femenino" },
  ];

  const { register, getValues, formState: { errors }, trigger } = useForm<RegistrarClienteDTO>({
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

  // Función para limpiar undefined y vacíos
  const cleanData = (data: any) => {
    return JSON.parse(JSON.stringify(data, (key, value) =>
      value === undefined || value === "" ? null : value
    ));
  };

  // Función principal para registrar
  const doRegister = async () => {
    // Validar todos los campos antes de enviar
    const valid = await trigger();
    if (!valid) {
      return Swal.fire({
        icon: "error",
        title: "Complete todos los campos correctamente",
        showConfirmButton: false,
        timer: 1500,
      });
    }

    if (!imagenCliente) {
      return Swal.fire({
        icon: "error",
        title: "Debe seleccionar una imagen",
        showConfirmButton: false,
        timer: 1500,
      });
    }

    const data = cleanData(getValues());

    const formData = new FormData();
    formData.append("data", JSON.stringify(data));
    formData.append("archivo", imagenCliente);

    mutate(formData, {
      onSuccess: () => {
        Swal.fire({
          position: "center",
          icon: "success",
          title: "Cliente registrado",
          showConfirmButton: false,
          timer: 1500,
        });
        navigate("/");
      },
      onError: () => {
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Cliente no registrado",
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
          sx={{ mb: 1, fontWeight: "bold", color: "#e74c3c" }}
        >
          Registrarse
        </Typography>

        <Box
          sx={{
            display: "grid",
            gap: 2,
            gridTemplateColumns: { xs: "1fr", sm: "1fr 1fr" },
          }}
        >
          {/* Credenciales */}
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

          {/* Datos personales */}
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

          {/* Imagen */}
          <TextField
            type="file"
            inputProps={{ accept: "image/*" }}
            onChange={(e) => setImagenCliente(e.target.files?.[0] || null)}
            sx={{ backgroundColor: "#fafafa" }}
          />
        </Box>

        {/* Botón principal */}
        <Button
          fullWidth
          variant="contained"
          sx={{
            mt: 3,
            py: 1.5,
            fontSize: "1rem",
            fontWeight: "bold",
            borderRadius: "10px",
            backgroundColor: "#e74c3c",
            "&:hover": { backgroundColor: "#d8433c" },
          }}
          type="button"
          onClick={doRegister}
        >
          Registrarse
        </Button>

        {/* Botón secundario */}
        <Button
          fullWidth
          variant="outlined"
          sx={{
            mt: 2,
            py: 1.2,
            borderRadius: "10px",
            borderColor: "#e74c3c",
            color: "#e74c3c",
            fontWeight: "bold",
            "&:hover": { borderColor: "#d8433c", color: "#d8433c" },
          }}
          onClick={() => navigate("/")}
          type="button"
        >
          Iniciar sesión
        </Button>
      </Box>
    </Box>
  );
}
