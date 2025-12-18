import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Box,
} from "@mui/material";
import { useEffect } from "react";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";

interface Props {
  open: boolean;
  onClose: () => void;
  mesa: any;
  onSubmit: (data: any) => void;
}

// -------------------- VALIDACIÓN --------------------
const schema = yup.object().shape({
  capacidad: yup
    .number()
    .typeError("La capacidad debe ser un número")
    .required("La capacidad es requerida")
    .min(1, "La capacidad mínima es 1"),
  ubicacion: yup.string().required("La ubicación es requerida"),
  estado: yup.string().required("El estado es requerido"),
});

export default function ModalEditarMesa({ open, onClose, mesa, onSubmit }: Props) {
  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: {
      capacidad: "",
      ubicacion: "",
      estado: "disponible",
    },
  });

  // Cuando cambia la mesa seleccionada, rellenamos el formulario
  useEffect(() => {
    if (mesa) {
      reset({
        capacidad: mesa.capacidad,
        ubicacion: mesa.ubicacion,
        estado: mesa.estado,
      });
    }
  }, [mesa, reset]);

  const onFormSubmit = (data: any) => {
    onSubmit({
      idMesa: mesa.idMesa,
      ...data,
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Editar Mesa</DialogTitle>

      <DialogContent>
        <Box display="flex" flexDirection="column" gap={2} mt={1}>
          {/* CAPACIDAD */}
          <Controller
            name="capacidad"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                type="number"
                label="Capacidad"
                fullWidth
                error={!!errors.capacidad}
                helperText={errors.capacidad?.message}
              />
            )}
          />

          {/* UBICACIÓN */}
          <Controller
            name="ubicacion"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Ubicación"
                fullWidth
                error={!!errors.ubicacion}
                helperText={errors.ubicacion?.message}
              />
            )}
          />

          {/* ESTADO */}
          <Controller
            name="estado"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                select
                label="Estado"
                fullWidth
                error={!!errors.estado}
                helperText={errors.estado?.message}
              >
                <MenuItem value="disponible">Disponible</MenuItem>
                <MenuItem value="ocupado">Ocupado</MenuItem>
              </TextField>
            )}
          />
        </Box>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose} color="secondary">
          Cancelar
        </Button>
        <Button variant="contained" onClick={handleSubmit(onFormSubmit)}>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}
