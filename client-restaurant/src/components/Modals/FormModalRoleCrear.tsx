import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect } from "react";
import type { Rol } from "../../types/role.type";
import { rolSchema } from "../../validators/role.schema";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Rol) => void;
  initialData?: Rol;
}

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 420,
  bgcolor: "background.paper",
  borderRadius: "12px",
  boxShadow: 24,
  p: 4,
};

export default function FormModalEditRol({ open, onClose, onSubmit, initialData }: Props) {
  const {
    register,
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm<Rol>({
    resolver: yupResolver(rolSchema),
    mode: "onChange",
  });

  // Cargar datos cuando se edita
  useEffect(() => {
    if (initialData && open) {
      reset(initialData);
    } else if (!open) {
      reset({
        nombre: "",
        descripcion: "",
        estadoRol: "activo",
      });
    }
  }, [initialData, open, reset]);

  const enviar = (data: Rol) => {
    // Trim de todos los campos de texto antes de enviar
    const dataTrimmed = {
      ...data,
      nombre: data.nombre?.trim(),
      descripcion: data.descripcion?.trim(),
    };

    onSubmit(dataTrimmed);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6" mb={2}>
          Editar Rol
        </Typography>

        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            fullWidth
            label="Nombre del Rol"
            {...register("nombre")}
            error={!!errors.nombre}
            helperText={errors.nombre?.message}
            placeholder="Ej: Administrador"
            sx={{ mb: 2 }}
          />

          <TextField
            fullWidth
            multiline
            rows={4}
            label="Descripción"
            {...register("descripcion")}
            error={!!errors.descripcion}
            helperText={errors.descripcion?.message}
            placeholder="Describe las responsabilidades y permisos de este rol..."
            sx={{ mb: 2 }}
          />



          <Box sx={{ mt: 3, display: "flex", justifyContent: "flex-end", gap: 1 }}>
            <Button color="error" variant="outlined" onClick={onClose}>
              Cancelar
            </Button>
            <Button type="submit" variant="contained">
              Actualizar
            </Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}