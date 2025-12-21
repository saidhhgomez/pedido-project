import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect } from "react";

import type { Sucursal } from "../../types/sucursales.type";
import { sucursalSchema } from "../../validators/sucursal.schema";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Partial<Sucursal>) => void;
  initialData?: Sucursal;
}

const style = {
  position: "absolute" as const,
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 420,
  bgcolor: "background.paper",
  borderRadius: "12px",
  boxShadow: 24,
  p: 4,
};

export default function FormModalSucursal({
  open,
  onClose,
  onSubmit,
  initialData,
}: Props) {
  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<Sucursal>({
    resolver: yupResolver(sucursalSchema),
    mode: "onChange", // Validación en tiempo real
  });

  // 🔹 SINCRONIZA CON API
  useEffect(() => {
    if (initialData) {
      reset(initialData); // edición
    } else {
      reset({}); // creación (sin estado)
    }
  }, [initialData, reset]);

  const enviar = (data: Sucursal) => {
    // Trim de todos los campos de texto antes de enviar
    const dataTrimmed = {
      ...data,
      nombre: data.nombre?.trim(),
      direccion: data.direccion?.trim(),
      telefono: data.telefono?.trim(),
    };

    if (!initialData) {
      // 🟢 CREAR → eliminar estado
      const { estado, ...dataSinEstado } = dataTrimmed;
      onSubmit(dataSinEstado);
    } else {
      // 🟢 EDITAR → enviar completo
      onSubmit(dataTrimmed);
    }

    reset({});
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6" mb={2}>
          {initialData ? "Editar Sucursal" : "Registrar Sucursal"}
        </Typography>

        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            label="Nombre"
            fullWidth
            {...register("nombre")}
            error={!!errors.nombre}
            helperText={errors.nombre?.message}
            sx={{ mb: 2 }}
            placeholder="Ej: Sucursal Centro"
          />

          <TextField
            label="Dirección"
            fullWidth
            {...register("direccion")}
            error={!!errors.direccion}
            helperText={errors.direccion?.message}
            sx={{ mb: 2 }}
            placeholder="Ej: Av. Principal 123"
          />

          <TextField
            label="Teléfono"
            fullWidth
            {...register("telefono")}
            error={!!errors.telefono}
            helperText={errors.telefono?.message}
            sx={{ mb: 2 }}
            placeholder="Ej: 987654321"
            onKeyPress={(e) => {
              // Solo permite números
              if (!/[0-9]/.test(e.key)) {
                e.preventDefault();
              }
            }}
            inputProps={{
              maxLength: 9,
              inputMode: 'numeric',
              pattern: '[0-9]*'
            }}
          />

          {/* SOLO EN EDICIÓN */}
          {initialData && (
            <Controller
              name="estado"
              control={control}
              defaultValue={initialData?.estado || "activo"}
              render={({ field }) => (
                <TextField
                  select
                  label="Estado"
                  fullWidth
                  {...field}
                  error={!!errors.estado}
                  helperText={errors.estado?.message}
                  sx={{ mb: 2 }}
                >
                  <MenuItem value="activo">Activo</MenuItem>
                  <MenuItem value="inactivo">Inactivo</MenuItem>
                </TextField>
              )}
            />
          )}

          <Box display="flex" justifyContent="flex-end" gap={1} mt={2}>
            <Button onClick={onClose} color="error" variant="outlined">
              Cancelar
            </Button>
            <Button type="submit" variant="contained">
              {initialData ? "Actualizar" : "Guardar"}
            </Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}