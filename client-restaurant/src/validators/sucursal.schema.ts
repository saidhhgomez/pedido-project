import * as yup from "yup";



export const sucursalSchema = yup.object({
  nombre: yup
    .string()
    .required("El nombre es obligatorio")
    .test('no-solo-espacios', 'El nombre no puede contener solo espacios', (value) => {
      return value ? value.trim().length > 0 : false;
    })
    .test('sin-espacios-extremos', 'No se permiten espacios al inicio o al final', (value) => {
      return value ? value === value.trim() : true;
    }),
  
  direccion: yup
    .string()
    .required("La dirección es obligatoria")
    .test('no-solo-espacios', 'La dirección no puede contener solo espacios', (value) => {
      return value ? value.trim().length > 0 : false;
    })
    .test('sin-espacios-extremos', 'No se permiten espacios al inicio o al final', (value) => {
      return value ? value === value.trim() : true;
    }),
  
  telefono: yup
    .string()
    .required("El teléfono es obligatorio")
    .test('no-solo-espacios', 'El teléfono no puede contener solo espacios', (value) => {
      return value ? value.trim().length > 0 : false;
    })
    .matches(/^\d{9}$/, "El teléfono debe contener exactamente 9 dígitos")
    .test('sin-espacios', 'El teléfono no puede contener espacios', (value) => {
      return value ? !/\s/.test(value) : true;
    }),
  
  estado: yup
    .string()
    .optional() // 👈 Ahora es opcional
    .oneOf(['activo', 'inactivo', undefined], 'Estado inválido'),
});