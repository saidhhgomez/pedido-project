import * as yup from "yup";



export const metodoPagoSchema = yup.object({
  nombre: yup
    .string()
    .required("El nombre es obligatorio")
    .test("no-solo-espacios", "El nombre no puede contener solo espacios",
      (value) => (value ? value.trim().length > 0 : false)
    )
    .test("sin-espacios-extremos", "No se permiten espacios al inicio o al final",
      (value) => (value ? value === value.trim() : true)
    )
    .min(3, "El nombre debe tener al menos 3 caracteres")
    .max(50, "El nombre no puede superar 50 caracteres"),

  estadoFormaPago: yup
    .mixed<"activo" | "inactivo">()
    .oneOf(["activo", "inactivo", undefined], "Estado inválido")
    .optional(),
});
