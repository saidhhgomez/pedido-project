// validators/jornada.schema.ts
import * as yup from "yup";

export const jornadaSchema = yup.object().shape({
  nombre: yup.string().required("El nombre es obligatorio"),
  descripcion: yup.string().required("La descripción es obligatoria"),
});
