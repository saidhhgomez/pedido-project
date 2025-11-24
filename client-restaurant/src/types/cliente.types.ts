import type { CredencialesDTO, PersonaDTO } from "./empleado.types";


export interface ClienteDTO{
    imagenCliente_url:string;
}


export interface RegistrarClienteDTO{
    credenciales: CredencialesDTO;
    persona:PersonaDTO;
    cliente:ClienteDTO;
}


