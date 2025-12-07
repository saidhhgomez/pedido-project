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
    genero: yup.string().required("Seleccione su Genero"),
    tipoDocumento: yup.string().required("Seleccione su Tipo de documento"),
    numDocumento: yup.string().required("Ingrese su numero de Documento"),
telefono: yup
  .string()
  .required("Ingrese su número de teléfono")
  .matches(/^[0-9]+$/, "El teléfono solo puede contener números"),    correo: yup
      .string()
      .required("Ingrese correo")
      .email("Correo inválido"),
    fechaNacimiento: yup.string().required("Seleccione una Fecha"),
  }),

  cliente: yup.object({
    imagenCliente_url: yup.string().required(),
  }),
});
