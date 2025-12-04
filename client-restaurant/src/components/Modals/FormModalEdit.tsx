import { Box, Button, MenuItem, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { plateSchema } from "../../validators/plate.schema";
import type { Plate, PlateUpdate } from "../../types/Plate.type";
import React from "react";

interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: PlateUpdate) => void; // OJO: PlateUpdate
  initialData?: Plate;
}

const style = {
  position: "absolute" ,
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 420,
  bgcolor: "background.paper",
  borderRadius: "12px",
  boxShadow: 24,
  p: 4,
};

export default function EditModalPlate({ open, onClose, onSubmit, initialData }: Props) {
  const opcionesBooleanas = [
    { value: "true", label: "Activo" },
    { value: "false", label: "No activo" },
  ];

  const { register, handleSubmit, formState: { errors }, reset, setValue } = useForm<PlateUpdate>({
    resolver: yupResolver(plateSchema),
    defaultValues: initialData ? { ...initialData } : undefined,
  });

  // Rellenar campos al abrir
  React.useEffect(() => {
    if (initialData) {
      const { idCatalogo, ...fields } = initialData; // quitamos idCatalogo
      Object.entries(fields).forEach(([key, value]) => {
        setValue(key as keyof PlateUpdate, value);
      });
    }
  }, [initialData, setValue]);

  const enviar = (data: PlateUpdate) => {
      console.log("Enviar al backend:", data);

    onSubmit(data);
    reset();
    onClose();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6">{initialData ? "Editar Plato" : "Registrar Plato"}</Typography>

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
            label="Categoría"
            fullWidth
            {...register("categoria")}
            error={!!errors.categoria}
            helperText={errors.categoria?.message}
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
            type="number"
            {...register("stock")}
            error={!!errors.stock}
            helperText={errors.stock?.message}
            sx={{ mt: 2 }}
          />
<TextField
  select
  label="Estado del Plato"
  fullWidth
  {...register("estadoplato", {
    setValueAs: (v) => v === "true",
  })}
  defaultValue={initialData?.estadoplato ?? true}
>

            {opcionesBooleanas.map((option) => (
              <MenuItem key={option.value} value={option.value}>
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
