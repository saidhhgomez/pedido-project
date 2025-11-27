import * as yup from "yup";


export const directionSchema = yup.object({
  departamento: yup.string().required(),
  provincia: yup.string().required(),
  distrito: yup.string().required(),
  direccion: yup.string().required(),
  referencia: yup.string().required(),
});