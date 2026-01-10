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
import type { MesaForm } from "../../types/Mesa.type";

interface Props {
  open: boolean;
  onClose: () => void;
  mesa: any;
  onSubmit: (data: any) => void;
}

const noSoloEspacios = (mensaje: string) =>
  yup
    .string()
    .transform((value) => value?.trim())
    .required(mensaje)
    .test(
      "no-solo-espacios",
      mensaje,
      (value) => !!value && value.length > 0
    );

// -------------------- VALIDACIÓN --------------------
const schema = yup.object().shape({
capacidad: yup
  .number()
  .typeError("La capacidad debe ser un número")
  .required("La capacidad es requerida")
  .integer("La capacidad debe ser un entero")
  .positive("La capacidad debe ser positiva")
  .min(1, "La capacidad mínima es 1"),

  ubicacion: noSoloEspacios("Ingrese la ubicación"),  
  estado: yup.string().required("El estado es requerido"),
});

export default function ModalEditarMesa({ open, onClose, mesa, onSubmit }: Props) {
  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<MesaForm>({
    resolver: yupResolver(schema),
    defaultValues: mesa?{
        numeroMesa:mesa.numeroMesa ,
        capacidad: mesa.capacidad,
        ubicacion: mesa.ubicacion,
        estado: mesa.estado,
        idSucursal: mesa.idSucursal,
      }:{},
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
          <Controller
  name="numeroMesa"
  control={control}
  defaultValue={mesa?.numeroMesa || ""}
  render={({ field }) => <input type="hidden" {...field} />}
/>

<Controller
  name="idSucursal"
  control={control}
  defaultValue={mesa?.idSucursal || 0}
  render={({ field }) => <input type="hidden" {...field} />}
/>





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
        sx={{
    "& input[type=number]": {
      MozAppearance: "textfield", // Firefox
    },
    "& input[type=number]::-webkit-outer-spin-button": {
      WebkitAppearance: "none",
      margin: 0,
    },
    "& input[type=number]::-webkit-inner-spin-button": {
      WebkitAppearance: "none",
      margin: 0,
    },
  }}
      onKeyDown={(e) => {
        if (e.key === "-" || e.key === "e") {
          e.preventDefault(); // ❌ bloquea negativos y notación científica
        }
      }}
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
                <MenuItem value="disponible">Disponible  </MenuItem>
                <MenuItem value="inactivo">Inactivo</MenuItem>
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
