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
    genero: yup.string().required(),
    tipoDocumento: yup.string().required(),
    numDocumento: yup.string().required(),
    telefono: yup.string().required(),
    correo: yup
      .string()
      .required("Ingrese correo")
      .email("Correo inválido"),
    fechaNacimiento: yup.string().required(),
  }),

  empleado: yup.object({
    direccion: yup.string().required(),
    estadoEmpleado: yup.string().required(),
    imagenConductor_url: yup.string().required(),
  }),
});
