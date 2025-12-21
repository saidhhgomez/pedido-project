import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect } from "react";
import { mesaSchema } from "../../validators/mesa.schema";

interface Props {
  open: boolean;
  mesa: any;
  onClose: () => void;
  onSubmit: (data: any) => void;
}

const style = {
  position: "absolute" as const,
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 450,
  bgcolor: "background.paper",
  borderRadius: "12px",
  boxShadow: 24,
  p: 4,
};

const ubicaciones = ["Interior", "Exterior", "Terraza", "VIP", "Entrada"];
const estados = ["disponible", "ocupada", "reservada", "mantenimiento"];

export default function ModalEditarMesa({ open, mesa, onClose, onSubmit }: Props) {
  const {
    control,
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(mesaSchema),
    mode: "onChange",
  });

  // Cargar datos de la mesa al abrir el modal
  useEffect(() => {
    if (mesa && open) {
      reset({
        numeroMesa: mesa.numeroMesa || "",
        capacidad: mesa.capacidad || 1,
        ubicacion: mesa.ubicacion || "",
        estado: mesa.estado || "disponible",
      });
    }
  }, [mesa, open, reset]);

  const enviar = (data: any) => {
    // Trim de todos los campos de texto
    const dataTrimmed = {
      numeroMesa: data.numeroMesa?.trim(),
      capacidad: Number(data.capacidad),
      ubicacion: data.ubicacion?.trim(),
      estado: data.estado,
    };

    onSubmit(dataTrimmed);
    reset({});
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6" mb={2}>
          Editar Mesa
        </Typography>

        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            label="Número de Mesa"
            fullWidth
            {...register("numeroMesa")}
            error={!!errors.numeroMesa}
            helperText={errors.numeroMesa?.message}
            sx={{ mb: 2 }}
            placeholder="Ej: Mesa 1"
          />

          <TextField
            label="Capacidad"
            type="number"
            fullWidth
            {...register("capacidad")}
            error={!!errors.capacidad}
            helperText={errors.capacidad?.message}
            sx={{ mb: 2 }}
            placeholder="Ej: 4"
            onKeyPress={(e) => {
              if (!/[0-9]/.test(e.key)) {
                e.preventDefault();
              }
            }}
            inputProps={{
              min: 1,
              max: 20,
            }}
          />

          <Controller
            name="ubicacion"
            control={control}
            defaultValue=""
            render={({ field }) => (
              <TextField
                select
                label="Ubicación"
                fullWidth
                {...field}
                error={!!errors.ubicacion}
                helperText={errors.ubicacion?.message}
                sx={{ mb: 2 }}
              >
                {ubicaciones.map((ubicacion) => (
                  <MenuItem key={ubicacion} value={ubicacion}>
                    {ubicacion}
                  </MenuItem>
                ))}
              </TextField>
            )}
          />

          <Controller
            name="estado"
            control={control}
            defaultValue="disponible"
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
                {estados.map((estado) => (
                  <MenuItem key={estado} value={estado}>
                    {estado.charAt(0).toUpperCase() + estado.slice(1)}
                  </MenuItem>
                ))}
              </TextField>
            )}
          />

          <Box display="flex" justifyContent="flex-end" gap={1} mt={2}>
            <Button onClick={onClose} color="error" variant="outlined">
              Cancelar
            </Button>
            <Button type="submit" variant="contained" color="primary">
              Actualizar
            </Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}