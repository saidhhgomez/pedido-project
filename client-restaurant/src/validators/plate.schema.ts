import * as yup from "yup";

const MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB
const SUPPORTED_FORMATS = ["image/jpg", "image/jpeg", "image/png", "image/webp"];

// Campos comunes para crear y editar
const commonFields = {
  nombre: yup
    .string()
    .trim()
    .min(3, "Debe tener al menos 3 caracteres")
    .max(80, "Máximo 80 caracteres")
    .matches(
      /^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\s]+$/,
      "No se permiten caracteres especiales"
    )
    .required("El nombre es obligatorio"),

  categoria: yup
    .string()
    .trim()
    .min(3, "Categoría inválida")
    .max(50, "Máximo 50 caracteres")
    .required("La categoría es obligatoria"),

  precio: yup
    .number()
    .typeError("El precio debe ser un número")
    .positive("Debe ser mayor a 0")
    .max(9999, "Precio demasiado alto")
    .test(
      "decimales",
      "Máximo 2 decimales",
      value => value !== undefined && /^\d+(\.\d{1,2})?$/.test(value.toString())
    )
    .required("El precio es obligatorio"),

  stock: yup
    .number()
    .typeError("El stock debe ser un número")
    .integer("El stock debe ser entero")
    .min(1, "Stock mínimo 1")
    .max(10000, "Stock demasiado alto")
    .required("El stock es obligatorio"),
};

// Schema para CREAR (con imagen obligatoria)
export const plateSchemaCreate = yup.object({
  ...commonFields,
  imagenPlato: yup
    .mixed()
    .required("La imagen es obligatoria")
    .test(
      "fileExists",
      "Debe seleccionar una imagen",
      value => value && value.length > 0
    )
    .test(
      "fileSize",
      "La imagen no debe superar 2MB",
      value => value && value[0]?.size <= MAX_IMAGE_SIZE
    )
    .test(
      "fileFormat",
      "Formato no soportado (jpg, jpeg, png, webp)",
      value => value && SUPPORTED_FORMATS.includes(value[0]?.type)
    ),
});

// Schema para EDITAR (sin imagen - no se permite cambiar)
export const plateSchemaEdit = yup.object({
  ...commonFields,
  // NO incluir imagenPlato aquí
});

// Mantener el original por compatibilidad
export const plateSchema = plateSchemaCreate;