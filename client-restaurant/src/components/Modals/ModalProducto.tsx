import { Box, Button, Modal, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { toast } from "react-toastify";
import CircularProgress from "@mui/material/CircularProgress";
import { useEffect } from "react";

import { plateSchemaCreate, plateSchemaEdit } from "../../validators/plate.schema"; // ← CAMBIAR ESTE IMPORT
import type { PlateForm, PlateDTO, Plate } from "../../types/Plate.type";
import { useCreatePlate, useUpdatePlate, useGetPlate } from "../../services/plate.service";
import Swal from "sweetalert2";

interface Props {
  open: boolean;
  onClose: () => void;
  plateToEdit?: Plate | null;
}

function LoadingOverlay() {
  return (
    <Box
      sx={{
        position: "absolute",
        inset: 0,
        bgcolor: "rgba(255,255,255,0.6)",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        zIndex: 10,
      }}
    >
      <CircularProgress />
    </Box>
  );
}

export default function FormModalPlate({ open, onClose, plateToEdit = null }: Props) {
  console.log("🔷 FormModalPlate renderizado");
  console.log("🔷 open:", open);
  console.log("🔷 plateToEdit:", plateToEdit);

  const isEditing = !!plateToEdit; // ← MOVER ESTO ANTES DEL useForm

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm<PlateForm>({
    resolver: yupResolver(isEditing ? plateSchemaEdit : plateSchemaCreate), // ← CAMBIAR AQUÍ
  });

  console.log("🔷 isEditing:", isEditing);
  console.log("🔷 Errores de validación:", errors); // ← Ver si hay errores

  const { refetch } = useGetPlate();
  const archivo = watch("imagenPlato");
  const nombreArchivo = archivo?.[0]?.name || "";

  const { mutate: createMutate, isPending: isCreating } = useCreatePlate();
  const { mutate: updateMutate, isPending: isUpdating } = useUpdatePlate();

  const isPending = isCreating || isUpdating;

  console.log("🔷 isEditing:", isEditing);
  console.log("🔷 isPending:", isPending);

  // Cargar datos cuando se abre para editar
  useEffect(() => {
    console.log("🔶 useEffect ejecutado - open:", open, "plateToEdit:", plateToEdit);
    if (open && plateToEdit) {
      console.log("🔶 Cargando datos del plato:");
      console.log("   - nombre:", plateToEdit.nombre);
      console.log("   - categoria:", plateToEdit.categoria);
      console.log("   - precio:", plateToEdit.precio);
      console.log("   - stock:", plateToEdit.stock);
      
      setValue("nombre", plateToEdit.nombre);
      setValue("categoria", plateToEdit.categoria);
      setValue("precio", plateToEdit.precio);
      setValue("stock", plateToEdit.stock);
    } else if (open && !plateToEdit) {
      console.log("🔶 Limpiando formulario (modo crear)");
      reset();
    }
  }, [open, plateToEdit, setValue, reset]);

  const enviar = (data: PlateForm) => {
    console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    console.log("📝 FUNCIÓN ENVIAR EJECUTADA");
    console.log("📝 isEditing:", isEditing);
    console.log("📝 plateToEdit:", plateToEdit);
    console.log("📝 data del formulario:", data);
    console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

    if (isEditing) {
      console.log("🟢 MODO EDITAR");
      
      // ACTUALIZAR - Enviar JSON con imagenPlatoUrl del plato original
      const updateData = {
        nombre: data.nombre,
        categoria: data.categoria,
        precio: Number(data.precio),
        stock: Number(data.stock),
        estadoplato: plateToEdit.estadoplato ?? true,
        imagenPlatoUrl: plateToEdit.imagenPlatoUrl,
      };

      console.log("🔍 ID del plato a actualizar:", plateToEdit.idCatalogo);
      console.log("🔍 Datos que se enviarán (updateData):");
      console.log(JSON.stringify(updateData, null, 2));

      console.log("🚀 Llamando a updateMutate...");
      
      updateMutate(
        { id: plateToEdit.idCatalogo, data: updateData },
        {
          onSuccess: (response) => {
            console.log("✅ onSuccess ejecutado");
            console.log("✅ Response:", response);
            toast.success("✅ Plato actualizado correctamente");
            refetch();
            reset();
            onClose();
          },
          onError: (error: any) => {
            console.log("❌ onError ejecutado");
            console.error("❌ Error completo:", error);
            console.error("❌ Error response:", error?.response);
            console.error("❌ Error response data:", error?.response?.data);
            console.error("❌ Error response status:", error?.response?.status);
            console.error("❌ Error message:", error?.message);
            
            const mensaje =
              error?.response?.data?.message || "❌ Error al actualizar el plato";
            toast.error(mensaje);
          },
        }
      );
      
      console.log("🚀 updateMutate llamado (esperando respuesta...)");
      
    } else {
      console.log("🟢 MODO CREAR");
      
      // CREAR - Enviar FormData
      const formData = new FormData();

      const jsonData: PlateDTO = {
        nombre: data.nombre,
        categoria: data.categoria,
        precio: Number(data.precio),
        stock: Number(data.stock),
      };

      formData.append("data", JSON.stringify(jsonData));
      formData.append("imagenPlato", data.imagenPlato[0]);

      console.log("🔍 Datos para crear:");
      console.log(JSON.stringify(jsonData, null, 2));
      console.log("🔍 Imagen:", data.imagenPlato[0]?.name);

      createMutate(formData, {
        onSuccess: (responses) => {
  

          refetch();
          reset();
          onClose();


          Swal.fire({
  position: "center",
  icon: "success",
  title: "Plato exitoso",
  showConfirmButton: false,
  timer: 1500
});
        },
        onError: (error) => {
Swal.fire({
  position: "center",
  icon: "error",
  title: error.message,
  showConfirmButton: false,
  timer: 1500
});
        },
      });
    }
    
    console.log("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: "relative",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          minHeight: "100vh",
          px: 2,
        }}
      >
        <Box
          sx={{
            position: "relative",
            p: 4,
            bgcolor: "background.paper",
            width: { xs: "100%", sm: 420 },
            maxHeight: "90vh",
            overflowY: "auto",
            borderRadius: 2,
            boxShadow: 24,
          }}
        >
          {isPending && <LoadingOverlay />}

          <Typography variant="h6" textAlign="center" mb={2}>
            {isEditing ? "Editar Plato" : "Registrar Plato"}
          </Typography>

          <form onSubmit={handleSubmit(enviar)} noValidate>
            <TextField
              fullWidth
              label="Nombre"
              {...register("nombre")}
              error={!!errors.nombre}
              helperText={errors.nombre?.message}
              sx={{ mb: 2 }}
            />

            <TextField
              fullWidth
              label="Categoría"
              {...register("categoria")}
              error={!!errors.categoria}
              helperText={errors.categoria?.message}
              sx={{ mb: 2 }}
            />

            <TextField
              fullWidth
              label="Precio"
              type="number"
              {...register("precio")}
              error={!!errors.precio}
              helperText={errors.precio?.message}
              sx={{ mb: 2 }}
            />

            <TextField
              fullWidth
              label="Stock"
              type="number"
              {...register("stock")}
              error={!!errors.stock}
              helperText={errors.stock?.message}
              sx={{ mb: 2 }}
            />

            {/* Campo de imagen SOLO al crear */}
            {!isEditing && (
              <TextField
                fullWidth
                label="Imagen del plato"
                value={nombreArchivo}
                InputProps={{
                  readOnly: true,
                  endAdornment: (
                    <Button component="label" variant="outlined">
                      Examinar
                      <input
                        type="file"
                        hidden
                        accept="image/*"
                        {...register("imagenPlato")}
                      />
                    </Button>
                  ),
                }}
                error={!!errors.imagenPlato}
                helperText={errors.imagenPlato?.message}
                sx={{ mb: 3 }}
              />
            )}

            <Box display="flex" justifyContent="flex-end" gap={1} sx={{ mt: isEditing ? 1 : 0 }}>
              <Button 
                onClick={() => {
                  console.log("🔴 Botón Cancelar clickeado");
                  onClose();
                }} 
                disabled={isPending}
              >
                Cancelar
              </Button>
              <Button 
                type="submit" 
                variant="contained" 
                disabled={isPending}
                onClick={() => {
                  console.log("🟡 Botón submit clickeado (antes del handleSubmit)");
                }}
              >
                {isPending ? "Guardando..." : isEditing ? "Actualizar" : "Guardar"}
              </Button>
            </Box>
          </form>
        </Box>
      </Box>
    </Modal>
  );
}