import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import type { Rol } from "../../types/role.type";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Rol) => void;
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

export default function FormModalCreateRol({ open, onClose, onSubmit }: Props) {
  const { register, handleSubmit, reset } = useForm<Rol>({
    defaultValues: {
      nombre: "",
      descripcion: "",
    },
  });

  const enviar = (data: Rol) => {
    onSubmit(data);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">Crear Rol</Typography>

        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            fullWidth
            label="Nombre"
            {...register("nombre", { required: true })}
            sx={{ mt: 2 }}
          />

          <TextField
            fullWidth
            multiline
            rows={3}
            label="Descripción"
            {...register("descripcion", { required: true })}
            sx={{ mt: 2 }}
          />

          <Box sx={{ mt: 3, display: "flex", justifyContent: "end", gap: 1 }}>
            <Button color="error" onClick={onClose}>Cancelar</Button>
            <Button type="submit" variant="contained">Guardar</Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}
