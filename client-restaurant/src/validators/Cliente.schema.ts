import * as yup from "yup";

export const registrarEmpleadoSchema = yup.object({
  credenciales: yup.object({
    usuario: yup.string().required("Ingresa tu usuario"),
    contrasena: yup.string().required("Ingresa tu contraseña"),
  }),

  persona: yup.object({
    nombres: yup.string().required("Ingrese nombres"),
    apPaterno: yup.string().required("Ingrese apellido paterno"),
    apMaterno: yup.string().required("Ingrese apellido materno"),
    genero: yup.string().required("Seleccione su género"),
    tipoDocumento: yup.string().required("Seleccione tipo de documento"),
    numDocumento: yup.string().required("Ingrese número de documento"),
    telefono: yup
      .string()
      .required("Ingrese teléfono")
      .matches(/^[0-9]+$/, "Solo números"),
    correo: yup.string().email("Correo inválido").required("Ingrese correo"),
    fechaNacimiento: yup.string().required("Seleccione fecha"),
  }),

  // 👇 ARCHIVO, NO STRING
  imagenCliente: yup
    .mixed()
    .required("Debe subir una imagen"),
});
