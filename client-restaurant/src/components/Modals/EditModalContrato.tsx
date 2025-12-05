// components/Modals/EditModalContrato.tsx
import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm, type SubmitHandler } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import type { TipoContrato } from "../../types/contrato.type";
import { jornadaSchema } from "../../validators/contrato.schema";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: TipoContrato) => void;
  initialData: TipoContrato;
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

export default function EditModalContrato({ open, onClose, onSubmit, initialData }: Props) {
  const { register, handleSubmit, formState: { errors }, reset } = useForm<TipoContrato>({
    resolver: yupResolver(jornadaSchema),
    defaultValues: initialData,
  });

  const enviar: SubmitHandler<TipoContrato> = (data: TipoContrato) => {
    onSubmit(data);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">Editar Contrato</Typography>

        <form onSubmit={handleSubmit(enviar)}>
          <TextField
            label="Nombre"
            fullWidth
            {...register("nombre")}
            error={!!errors.nombre}
            helperText={errors.nombre?.message}
            sx={{ mt: 2 }}
          />

          <TextField
            label="Descripción"
            fullWidth
            {...register("descripcion")}
            error={!!errors.descripcion}
            helperText={errors.descripcion?.message}
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
