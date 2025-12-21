import * as yup from "yup";

export const mesaSchema = yup.object({
  numeroMesa: yup
    .string()
    .required("El número de mesa es obligatorio")
    .test('no-solo-espacios', 'El número de mesa no puede contener solo espacios', (value) => {
      return value ? value.trim().length > 0 : false;
    })
    .test('sin-espacios-extremos', 'No se permiten espacios al inicio o al final', (value) => {
      return value ? value === value.trim() : true;
    }),
  
  capacidad: yup
    .number()
    .required("La capacidad es obligatoria")
    .min(1, "La capacidad debe ser al menos 1")
    .max(20, "La capacidad no puede superar 20 personas")
    .typeError("Debe ser un número válido"),
  
  ubicacion: yup
    .string()
    .required("La ubicación es obligatoria")
    .test('no-solo-espacios', 'La ubicación no puede contener solo espacios', (value) => {
      return value ? value.trim().length > 0 : false;
    })
    .test('sin-espacios-extremos', 'No se permiten espacios al inicio o al final', (value) => {
      return value ? value === value.trim() : true;
    }),
  
  estado: yup
    .string()
    .optional()
    .oneOf(['disponible', 'ocupada', 'reservada', 'mantenimiento', undefined], 'Estado inválido'),
});