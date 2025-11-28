// FormModal.tsx
import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import type { DireccionForm } from "../../types/direction.type";
import { directionSchema } from "../../validators/direction.schema";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: DireccionForm) => void;
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

export default function FormModal({ open, onClose, onSubmit }: Props) {




  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm<DireccionForm>({
    resolver: yupResolver(directionSchema),
  });

  const enviar = (data: DireccionForm) => {
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
            label="Departamento"
            fullWidth
            {...register("departamento")}
            error={!!errors.departamento}
            helperText={errors.departamento?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Provincia"
            fullWidth
            {...register("provincia")}
            error={!!errors.provincia}
            helperText={errors.provincia?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Distrito"
            fullWidth
            {...register("distrito")}
            error={!!errors.distrito}
            helperText={errors.distrito?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Dirección"
            fullWidth
            {...register("direccion")}
            error={!!errors.direccion}
            helperText={errors.direccion?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Referencia"
            fullWidth
            {...register("referencia")}
            error={!!errors.referencia}
            helperText={errors.referencia?.message}
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
