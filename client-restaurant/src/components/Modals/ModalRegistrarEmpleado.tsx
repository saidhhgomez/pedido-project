import {
  Box,
  Typography,
  TextField,
  Button,
  MenuItem,
  Modal,
} from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/empleado.schema";
import { useCrearEmpleado } from "../../services/empleado.service";
import type { RegistrarEmpleadoDTO } from "../../types/empleado.types";
import Swal from "sweetalert2";

interface Props {
  open: boolean;
  handleClose: () => void;
}

export default function RegistrarEmpleadoModal({ open, handleClose }: Props) {
  const { mutate } = useCrearEmpleado();

  const tipo_documento = [
    { value: "DNI", label: "DNI" },
    { value: "Passaporte", label: "Pasaporte" },
    { value: "Carnet de Extranjeria", label: "Carnet de Extranjeria" },
  ];

  const estados = [
    { value: "Activo", label: "Activo" },
    { value: "No Activo", label: "No Activo" },
  ];

  const Genero = [
    { value: "M", label: "M" },
    { value: "F", label: "F" },
  ];

  const {
    register,
    handleSubmit,
    reset,
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
        handleClose(); // 🔥 Cierra el modal
        reset();

        Swal.fire({
          icon: "success",
          title: "Empleado Registrado",
          showConfirmButton: false,
          timer: 1500,
        });
      },
      onError: () => {
        handleClose(); // 🔥 También cierra el modal
        Swal.fire({
          icon: "error",
          title: "Empleado No Registrado",
          showConfirmButton: false,
          timer: 1500,
        });
      },
    });
  };

  const modalStyle = {
    position: "absolute" as const,
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 420,
    bgcolor: "background.paper",
    p: 4,
    borderRadius: 2,
    boxShadow: 24,
    maxHeight: "90vh",
    overflowY: "auto",
  };

  return (
    <Modal open={open} onClose={handleClose}>
      <Box sx={modalStyle} component="form" onSubmit={handleSubmit(onSubmit)}>
        <Typography variant="h5" textAlign="center" mb={2}>
          Registrar Empleado
        </Typography>

        {/* --- CREDENCIALES --- */}
        <TextField
          label="Usuario"
          fullWidth
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
          fullWidth
          {...register("credenciales.contrasena")}
          sx={{ mt: 1 }}
        />
        {errors.credenciales?.contrasena && (
          <Typography color="error">
            {errors.credenciales.contrasena.message}
          </Typography>
        )}

        {/* --- PERSONA --- */}
        <TextField
          label="Nombres"
          fullWidth
          sx={{ mt: 2 }}
          {...register("persona.nombres")}
          error={!!errors.persona?.nombres}
          helperText={errors.persona?.nombres?.message}
        />

        <TextField
          label="Apellido paterno"
          fullWidth
          {...register("persona.apPaterno")}
          error={!!errors.persona?.apPaterno}
          helperText={errors.persona?.apPaterno?.message}
          sx={{ mt: 1 }}
        />

        <TextField
          label="Apellido materno"
          fullWidth
          {...register("persona.apMaterno")}
          error={!!errors.persona?.apMaterno}
          helperText={errors.persona?.apMaterno?.message}
          sx={{ mt: 1 }}
        />

        <TextField
          select
          label="Género"
          fullWidth
          defaultValue=""
          sx={{ mt: 1 }}
          {...register("persona.genero")}
          error={!!errors.persona?.genero}
          helperText={errors.persona?.genero?.message}
        >
          {Genero.map((g) => (
            <MenuItem key={g.value} value={g.value}>
              {g.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          select
          label="Tipo de Documento"
          fullWidth
          defaultValue=""
          sx={{ mt: 1 }}
          {...register("persona.tipoDocumento")}
          error={!!errors.persona?.tipoDocumento}
          helperText={errors.persona?.tipoDocumento?.message}
        >
          {tipo_documento.map((doc) => (
            <MenuItem key={doc.value} value={doc.value}>
              {doc.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          label="Nro documento"
          fullWidth
          sx={{ mt: 1 }}
          {...register("persona.numDocumento")}
          error={!!errors.persona?.numDocumento}
          helperText={errors.persona?.numDocumento?.message}
        />

        <TextField
          label="Teléfono"
          fullWidth
          sx={{ mt: 1 }}
          {...register("persona.telefono")}
          error={!!errors.persona?.telefono}
          helperText={errors.persona?.telefono?.message}
        />

        <TextField
          label="Correo"
          fullWidth
          sx={{ mt: 1 }}
          {...register("persona.correo")}
          error={!!errors.persona?.correo}
          helperText={errors.persona?.correo?.message}
        />

        <TextField
          type="date"
          label="Fecha nacimiento"
          fullWidth
          InputLabelProps={{ shrink: true }}
          sx={{ mt: 1 }}
          {...register("persona.fechaNacimiento")}
        />

        {/* --- ESTADO Y DIRECCIÓN --- */}
        <TextField
          label="Dirección"
          fullWidth
          sx={{ mt: 2 }}
          {...register("empleado.direccion")}
          error={!!errors.empleado?.direccion}
          helperText={errors.empleado?.direccion?.message}
        />

        <TextField
          select
          label="Estado"
          fullWidth
          defaultValue=""
          sx={{ mt: 1 }}
          {...register("empleado.estadoEmpleado")}
          error={!!errors.empleado?.estadoEmpleado}
          helperText={errors.empleado?.estadoEmpleado?.message}
        >
          {estados.map((e) => (
            <MenuItem key={e.value} value={e.value}>
              {e.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          label="Imagen (URL)"
          fullWidth
          sx={{ mt: 1 }}
          {...register("empleado.imagenConductor_url")}
          error={!!errors.empleado?.imagenConductor_url}
          helperText={errors.empleado?.imagenConductor_url?.message}
        />

        {/* BOTONES */}
        <Button variant="contained" fullWidth type="submit" sx={{ mt: 2 }}>
          Registrar
        </Button>

        <Button
          variant="outlined"
          fullWidth
          sx={{ mt: 1 }}
          onClick={handleClose}
        >
          Cancelar
        </Button>
      </Box>
    </Modal>
  );
}
