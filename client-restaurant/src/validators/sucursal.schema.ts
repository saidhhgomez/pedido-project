import * as yup from "yup";

export const sucursalSchema = yup.object({
  nombre: yup.string().required("El nombre es obligatorio"),
  direccion: yup.string().required("La dirección es obligatoria"),
  telefono: yup
    .string()
    .required("El teléfono es obligatorio")
    .matches(/^\d+$/, "El teléfono debe contener solo números"),
  estado: yup.string().required("El estado es obligatorio"),
});
