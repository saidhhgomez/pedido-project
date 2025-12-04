import { Box, Typography, TextField, Button, MenuItem } from "@mui/material";
import { useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { registrarEmpleadoSchema } from "../../validators/empleado.schema";
import { useCrearEmpleado } from "../../services/empleado.service";
import type { RegistrarEmpleadoDTO } from "../../types/empleado.types";

export default function RegistrarEmpleado() {
  const navigate = useNavigate();
  const { mutate } = useCrearEmpleado();

  const tipo_documento = [
  {
    value: 'DNI',
    label: 'DNI',
  },
  {
    value: 'Passaporte',
    label: 'Pasaporte',
  },
  {
    value: 'Carnet de Extranjeria',
    label: 'Carnet de Extranjeria',
  }
];

  const estados = [
  {
    value: 'Activo',
    label: 'Activo',
  },
  {
    value: 'No Activo',
    label: 'No Activo',
  }
];

  const Genero = [
  {
    value: 'M',
    label: 'M',
  },
  {
    value: 'F',
    label: 'F',
  }
];



    const {
      register,
      handleSubmit,
      formState: { errors },
    } = useForm<RegistrarEmpleadoDTO>({
      resolver: yupResolver(registrarEmpleadoSchema),
      defaultValues: {
        credenciales: {
          usuario: "",
          contrasena: "",
        },
        persona: {
          nombres: "",
          apPaterno: "",
          apMaterno: "",
          genero: "",
          tipoDocumento: "",
          numDocumento: "",
          telefono: "",
          correo: "",
          fechaNacimiento: "",
        },
        empleado: {
          direccion: "",
          estadoEmpleado: "",
          imagenConductor_url: "",
        },
      },
    });

  const onSubmit = (data: RegistrarEmpleadoDTO) => {
    console.log("Datos a enviar:", data); // <-- Aquí ves lo que se envía
    mutate(data, {
      onSuccess: () => {
        alert("Empleado registrado correctamente");
        navigate("/");
      },
      onError: (error) => {
        console.error("Error al registrar empleado:", error);
      },
    });
  };

  return (
    <Box
      sx={{
        width: "400px",
        display: "flex",
        flexDirection: "column",
        gap: 2,
        textAlign: "center",
        margin: "0 auto",
      }}
      component="form"
      onSubmit={handleSubmit(onSubmit)}
    >
      <Typography variant="h4">Registrar Empleado</Typography>

      {/* --- CREDENCIALES --- */}
      <TextField label="Usuario" {...register("credenciales.usuario")} />
      {errors.credenciales?.usuario && (
        <Typography color="error">
          {errors.credenciales.usuario.message}
        </Typography>
      )}

      <TextField
        label="Contraseña"
        type="password"
        {...register("credenciales.contrasena")}

      />
      {errors.credenciales?.contrasena && (
        <Typography color="error">
          {errors.credenciales.contrasena.message}
        </Typography>
      )}

      {/* --- PERSONA --- */}
      <TextField label="Nombres" {...register("persona.nombres")}  
      error={!!errors.persona?.apMaterno}
            helperText={errors.persona?.apMaterno?.message}/>
      <TextField label="Apellido paterno" {...register("persona.apPaterno") }
          error={!!errors.persona?.apPaterno}
            helperText={errors.persona?.apPaterno?.message} />

      <TextField label="Apellido materno" {...register("persona.apMaterno")} />


               <TextField
          id="outlined-select-currency"
          select
          label="Genero"
          defaultValue=""
            {...register("persona.genero")}
            error={!!errors.persona?.genero}
            helperText={errors.persona?.genero?.message}
        >
          {Genero.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>


           <TextField
          id="outlined-select-currency"
          select
          label="Tipo de Documento"
          defaultValue=""
            {...register("persona.tipoDocumento")}
            error={!!errors.persona?.tipoDocumento}
            helperText={errors.persona?.tipoDocumento?.message}
        >
          {tipo_documento.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
        
 

      
      <TextField label="Nro documento" {...register("persona.numDocumento")} 
      error={!!errors.persona?.numDocumento}
            helperText={errors.persona?.numDocumento?.message}/>
      <TextField label="Teléfono" {...register("persona.telefono")} 
      error={!!errors.persona?.telefono}
            helperText={errors.persona?.telefono?.message}/>
      <TextField label="Correo" {...register("persona.correo")} 
      error={!!errors.persona?.correo}
            helperText={errors.persona?.correo?.message}
      />

      <TextField
        type="date"
        label="Fecha nacimiento"
        InputLabelProps={{ shrink: true }}
        {...register("persona.fechaNacimiento")}
      />

      {/* --- Estado --- */}
      <TextField label="Direccion" {...register("empleado.direccion")} 
      error={!!errors.empleado?.direccion}
            helperText={errors.empleado?.direccion?.message}/>

             <TextField
          id="outlined-select-currency"
          select
          label="Estado"
          defaultValue=""
            {...register("empleado.estadoEmpleado")}
            error={!!errors.empleado?.estadoEmpleado}
            helperText={errors.empleado?.estadoEmpleado?.message}
        >
          {estados.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>

      <TextField
        label="Imagen (URL)"
        {...register("empleado.imagenConductor_url")}
        error={!!errors.empleado?.imagenConductor_url}
            helperText={errors.empleado?.imagenConductor_url?.message}
      />

      <Button variant="contained" type="submit">
        Registrar
      </Button>

      <Button variant="outlined" onClick={() => navigate("/")}>
        Volver
      </Button>
    </Box>
  );
}
