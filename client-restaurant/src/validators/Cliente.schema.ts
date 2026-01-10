import * as yup from "yup";

export const registrarEmpleadoSchema = yup.object({
  credenciales: yup.object({
    usuario: yup
      .string()
      .trim()
      .required("Usuario obligatorio"),

    contrasena: yup
      .string()
      .min(6, "Mínimo 6 caracteres")
      .required("Contraseña obligatoria"),
  }),

  persona: yup.object({
    nombres: yup
      .string()
      .trim()
      .required("Nombres obligatorios"),

    apPaterno: yup
      .string()
      .trim()
      .required("Apellido paterno obligatorio"),

    apMaterno: yup
      .string()
      .trim()
      .required("Apellido materno obligatorio"),

    genero: yup
      .string()
      .oneOf(["M", "F"], "Seleccione género")
      .required("Género obligatorio"),

    tipoDocumento: yup
      .string()
      .required("Tipo de documento obligatorio"),

    numDocumento: yup
      .string()
      .matches(/^\d+$/, "Solo números")
      .min(8, "Mínimo 8 dígitos")
      .required("Documento obligatorio"),

    telefono: yup
      .string()
      .matches(/^\d{9}$/, "Teléfono debe tener 9 dígitos")
      .required("Teléfono obligatorio"),

    correo: yup
      .string()
      .email("Correo inválido")
      .required("Correo obligatorio"),

    fechaNacimiento: yup
      .string()
      .required("Fecha de nacimiento obligatoria"),
  }),

  cliente: yup.object({
    imagenCliente: yup
      .mixed<FileList>()
      .test(
        "required",
        "La imagen es obligatoria",
        (value) => value instanceof FileList && value.length > 0
      )
      .test(
        "fileSize",
        "La imagen no debe superar 2MB",
        (value) =>
          value instanceof FileList &&
          value.length > 0 &&
          value[0].size <= 2_000_000
      )
      .test(
        "fileType",
        "Solo se permiten imágenes JPG o PNG",
        (value) =>
          value instanceof FileList &&
          value.length > 0 &&
          ["image/jpeg", "image/png"].includes(value[0].type)
      ),
  }),
});
