import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { useEffect } from "react";
import type { Rol } from "../../types/role.type";

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
  const { register, handleSubmit, control, reset } = useForm<Rol>({
    defaultValues: {
      nombre: "",
      descripcion: "",
      estadoRol: "activo",
    },
  });

  useEffect(() => {
    if (open && initialData) {
      reset(initialData);
    }
  }, [open, initialData, reset]);

  const enviar = (data: Rol) => {
    onSubmit(data);
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">Editar Rol</Typography>

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
            <Button type="submit" variant="contained">Actualizar</Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}
