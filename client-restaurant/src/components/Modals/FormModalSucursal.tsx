import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import type { Sucursal } from "../../types/sucursales.type";
import { sucursalSchema } from "../../validators/sucursal.schema";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: Sucursal) => void;
  initialData?: Sucursal; // opcional para editar
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

export default function FormModalSucursal({ open, onClose, onSubmit, initialData }: Props) {
  const { control, register, handleSubmit, reset, formState: { errors } } = useForm<Sucursal>({
    defaultValues: initialData || { estado: "activo" },
    resolver: yupResolver(sucursalSchema),
  });

  const enviar = (data: Sucursal) => {
    onSubmit(data);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">{initialData ? "Editar Sucursal" : "Registrar Sucursal"}</Typography>
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
            label="Dirección"
            fullWidth
            {...register("direccion")}
            error={!!errors.direccion}
            helperText={errors.direccion?.message}
            sx={{ mt: 2 }}
          />
          <TextField
            label="Teléfono"
            fullWidth
            {...register("telefono")}
            error={!!errors.telefono}
            helperText={errors.telefono?.message}
            sx={{ mt: 2 }}
          />

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
                sx={{ mt: 2 }}
              >
                <MenuItem value="activo">Activo</MenuItem>
                <MenuItem value="inactivo">Inactivo</MenuItem>
              </TextField>
            )}
          />

          <Box sx={{ mt: 3, display: "flex", justifyContent: "end", gap: 1 }}>
            <Button onClick={onClose} color="error">Cancelar</Button>
            <Button type="submit" variant="contained">{initialData ? "Actualizar" : "Guardar"}</Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}
