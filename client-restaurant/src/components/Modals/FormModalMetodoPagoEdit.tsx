import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
import type { MetodoPago } from "../../types/metodoPago.type";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: MetodoPago) => void;
  initialData?: MetodoPago;
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

export default function FormModalEditMetodoPago({ open, onClose, onSubmit, initialData }: Props) {
  const { register, handleSubmit, reset } = useForm<MetodoPago>({
    defaultValues: { nombre: "" },
  });

  useEffect(() => {
    if (initialData) reset(initialData);
  }, [initialData, reset]);

  const enviar = (data: MetodoPago) => {
    onSubmit(data);
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">Editar Método de Pago</Typography>
        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            fullWidth
            label="Nombre"
            {...register("nombre", { required: true })}
            sx={{ mt: 2 }}
          />
          <Box sx={{ mt: 3, display: "flex", justifyContent: "end", gap: 1 }}>
            <Button color="error" onClick={onClose}>Cancelar</Button>
            <Button type="submit" variant="contained">Actualizar</Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}
