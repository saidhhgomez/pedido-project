import * as yup from "yup";

export const plateSchema = yup.object({
  nombre: yup.string()
    .required("El nombre es obligatorio"),

  categoria: yup.string()
    .required("La categoría es obligatoria"),

  precio: yup.number()
    .typeError("El precio debe ser un número")
    .positive("El precio debe ser mayor a 0")
    .required("El precio es obligatorio"),

  stock: yup.number()
    .typeError("El stock debe ser un número")
    .integer("El stock debe ser un número entero")
    .min(1, "Minimo 1 stock")
    .required("El stock es obligatorio"),

  estadoplato: yup.boolean()
    .required("El estado es obligatorio"),

  imagenPlatoUrl: yup.string()
    .required("La imagen es obligatoria"),
});
