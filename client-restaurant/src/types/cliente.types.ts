export interface RegistrarClienteDTO {
  credenciales: {
    usuario: string;
    contrasena: string;
  };

  persona: {
    nombres: string;
    apPaterno: string;
    apMaterno: string;
    genero: "M" | "F" | "";
    tipoDocumento: string;
    numDocumento: string;
    telefono: string;
    correo: string;
    fechaNacimiento: string;
  };

  cliente: {
    imagenCliente?: FileList; // 🔥 CLAVE: opcional
    imagenCliente_url?: string;
  };
}

