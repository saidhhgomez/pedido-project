// FormModal.tsx
import { Box, Button, MenuItem, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { plateSchema } from "../../validators/plate.schema";
import type { Plate } from "../../types/Plate.type";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Plate) => void;
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

export default function FormModalPlate({ open, onClose, onSubmit }: Props) {

      const opcionesBooleanas = [
      {
        value: true,
        label: "Activo",
      },
      {
        value: false,
        label: "No activo",
      },
    ];


  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm<Plate>({
    resolver: yupResolver(plateSchema),
  });

  const enviar = (data: Plate) => {
    onSubmit(data);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">Registrar Dirección</Typography>

        <form onSubmit={handleSubmit(enviar)}>
          
          <TextField
            label="nombre"
            fullWidth
            {...register("nombre")}
            error={!!errors.nombre}
            helperText={errors.nombre?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Precio"
            fullWidth
            {...register("precio")}
            error={!!errors.precio}
            helperText={errors.precio?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Stock"
            fullWidth
            {...register("stock")}
            error={!!errors.stock}
            helperText={errors.stock?.message}
            sx={{ mt: 2 }}
          />

        <TextField
          id="outlined-select-currency"
          fullWidth
          select
          label="Estado del Plato"
          defaultValue=""
                      {...register("estadoplato")}
            error={!!errors.estadoplato}
            helperText={errors.estadoplato?.message}
                      sx={{ mt: 2 }}

        >
          {opcionesBooleanas.map((option) => (
            <MenuItem key={String(option.value)} value={String(option.value)}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>

          <TextField
            label="URL de la Imagen"
            fullWidth
            {...register("imagenPlatoUrl")}
            error={!!errors.imagenPlatoUrl}
            helperText={errors.imagenPlatoUrl?.message}
            sx={{ mt: 2 }}
          />

          <Box sx={{ mt: 3, display: "flex", justifyContent: "end", gap: 1 }}>
            <Button onClick={onClose} color="error">Cancelar</Button>
            <Button type="submit" variant="contained">Guardar</Button>
          </Box>

        </form>
      </Box>
    </Modal>
  );
}