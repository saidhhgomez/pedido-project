
import { object,string} from "yup";

export const loginSchema = object({
    
  usuario: string().required("Ingresa tu usuario"),
  contrasena: string().required("Ingresa tu contraseña"),
});



