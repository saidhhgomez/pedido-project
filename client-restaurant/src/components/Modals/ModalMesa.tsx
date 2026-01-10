import React from "react";
import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";

// -------------------- INTERFAZ --------------------
export interface MesaForm {
  numeroMesa: string;
  capacidad: number;
  ubicacion: string;
  idSucursal?: number; // se agrega internamente
}

// -------------------- PROPS --------------------
interface Props {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: MesaForm) => void;
}

// -------------------- VALIDACIÓN --------------------

// función reutilizable para NO permitir solo espacios
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

const mesaSchema = yup.object({
  numeroMesa: yup
    .number()
    .typeError("Ingrese solo números")
    .required("Ingrese el número de mesa")
    .integer("El número de mesa debe ser entero")
    .positive("El número de mesa debe ser positivo"),
  capacidad: yup
    .number()
    .typeError("Ingrese un número válido")
    .required("Ingrese la capacidad")
    .min(1, "La capacidad debe ser al menos 1"),

  ubicacion: noSoloEspacios("Ingrese la ubicación"),
});

// -------------------- COMPONENTE --------------------
export default function FormModalMesa({ open, onClose, onSubmit }: Props) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<MesaForm>({
    resolver: yupResolver(mesaSchema),
    defaultValues: {
      numeroMesa: "",
      capacidad: 1,
      ubicacion: "",
    },
  });

  const enviar = (data: MesaForm) => {
    const idSucursal = Number(localStorage.getItem("idSucursal") || 0);
    onSubmit({ ...data, idSucursal });
    reset();
    onClose();
  };

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

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        <Typography variant="h6" mb={2}>
          Crear Mesa
        </Typography>

        <form onSubmit={handleSubmit(enviar)} noValidate>
          {/* Número de Mesa */}
          <TextField

            label="Número de Mesa"
            fullWidth
            {...register("numeroMesa")}
            error={!!errors.numeroMesa}
            helperText={errors.numeroMesa?.message}
sx={{
  mb: 2,
}}

          />

          {/* Capacidad */}
          <TextField
            type="number"
            label="Capacidad"
            fullWidth
            {...register("capacidad", { valueAsNumber: true })}
            error={!!errors.capacidad}
            helperText={errors.capacidad?.message}
sx={{
  mb: 2,

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

          />

          {/* Ubicación */}
          <TextField
            label="Ubicación"
            fullWidth
            {...register("ubicacion")}
            error={!!errors.ubicacion}
            helperText={errors.ubicacion?.message}
            sx={{ mb: 2 }}
          />

          {/* Botones */}
          <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 1 }}>
            <Button onClick={onClose} color="error">
              Cancelar
            </Button>
            <Button type="submit" variant="contained">
              Guardar
            </Button>
          </Box>
        </form>
      </Box>
    </Modal>
  );
}
