import * as yup from "yup";



export const roleSchema = yup.object({
  nombre: yup.string().required("El nombre es obligatorio"),
  descripcion: yup.string().required("La descripción es obligatoria"),
  estadoRol: yup.string().oneOf(["activo", "inactivo"], "Estado inválido").required("El estado es obligatorio"),
});
