// FormModal.tsx
import { Box, Button, Modal, TextField, Typography, MenuItem } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import type { DireccionForm } from "../../types/direction.type";
import { directionSchema } from "../../validators/direction.schema";
import { useState, useEffect } from "react";
import ubigeoData from "../../data/ubigeo.json"; // <--- Importa el json

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

  const [departamentoList, setDepartamentoList] = useState<string[]>([]);
  const [provinciaList, setProvinciaList] = useState<string[]>([]);
  const [distritoList, setDistritoList] = useState<string[]>([]);

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    reset
  } = useForm<DireccionForm>({
    resolver: yupResolver(directionSchema),
  });

  // valores seleccionados en tiempo real
  const departamentoSeleccionado = watch("departamento");
  const provinciaSeleccionada = watch("provincia");

  // Cargar departamentos únicos
  useEffect(() => {
    const departamentos = [...new Set(ubigeoData.map(item => item.Departamento))];
    setDepartamentoList(departamentos);
  }, []);

  // Cargar provincias al cambiar departamento
  useEffect(() => {
    if (!departamentoSeleccionado) return;
    const provincias = ubigeoData
      .filter(item => item.Departamento === departamentoSeleccionado)
      .map(item => item.Provincia);

    setProvinciaList([...new Set(provincias)]);
    setDistritoList([]); // reset distritos
  }, [departamentoSeleccionado]);

  // Cargar distritos al cambiar provincia
  useEffect(() => {
    if (!provinciaSeleccionada) return;
    const distritos = ubigeoData
      .filter(item =>
        item.Departamento === departamentoSeleccionado &&
        item.Provincia === provinciaSeleccionada
      ).map(item => item.Distrito);

    setDistritoList([...new Set(distritos)]);
  }, [provinciaSeleccionada]);

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

          {/* -------------------- DEPARTAMENTO -------------------- */}
          <TextField
            select
            label="Departamento"
            fullWidth
            {...register("departamento")}
            error={!!errors.departamento}
            helperText={errors.departamento?.message}
            sx={{ mt: 2 }}
          >
            {departamentoList.map(dep => (
              <MenuItem key={dep} value={dep}>{dep}</MenuItem>
            ))}
          </TextField>

          {/* -------------------- PROVINCIA -------------------- */}
          <TextField
            select
            label="Provincia"
            fullWidth
            {...register("provincia")}
            error={!!errors.provincia}
            helperText={errors.provincia?.message}
            sx={{ mt: 2 }}
            disabled={!departamentoSeleccionado}
          >
            {provinciaList.map(prov => (
              <MenuItem key={prov} value={prov}>{prov}</MenuItem>
            ))}
          </TextField>

          {/* -------------------- DISTRITO -------------------- */}
          <TextField
            select
            label="Distrito"
            fullWidth
            {...register("distrito")}
            error={!!errors.distrito}
            helperText={errors.distrito?.message}
            sx={{ mt: 2 }}
            disabled={!provinciaSeleccionada}
          >
            {distritoList.map(dist => (
              <MenuItem key={dist} value={dist}>{dist}</MenuItem>
            ))}
          </TextField>

          {/* -------------------- DIRECCIÓN y REFERENCIA -------------------- */}
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
