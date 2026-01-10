import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  TextField,
  MenuItem,
  Stepper,
  Step,
  StepButton,
  FormControl,
  InputAdornment,
  OutlinedInput,
  FormHelperText,
  Typography,
  CircularProgress,
  Backdrop,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import Swal from "sweetalert2";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs from 'dayjs';
import 'dayjs/locale/es';

import {
  useBuscarPorDni,
  useActualizarPersona,
} from "../../services/empleado.service";
import {
  useCrearEmpleadoNuevoFormData,
  useCrearEmpleadoContratoExistenteFormData,
} from "../../services/empleado.formdata.service";
import { useGetAllSucursales } from "../../services/sucursales.service";
import { useGetAllRolesActivos } from "../../services/roles.service";
import { useGetAllContrato } from "../../services/contrato.service";

import EmpleadoPdf from "../../pdf/EmpleadoPdf";
import { selectPerfilEmpleado } from "../../store/slices/auth.slice";
import { useSelector } from "react-redux";

// Configurar dayjs en español
dayjs.locale('es');

/* ================= STEPS ================= */
const stepsNuevo = [
  "Credenciales",
  "Datos Personales",
  "Datos Empleado",
  "Vista Previa PDF",
  "Subir PDF Firmado",
  "Subir Imagen",
];

const stepsExistente = [
  "Datos Personales",
  "Datos Empleado",
  "Vista Previa PDF",
  "Subir PDF Firmado",
  "Subir Imagen",
];

export default function RegistroEmpleadoStepper() {
  const [tipoFlujo, setTipoFlujo] = useState<"NUEVO" | "EXISTENTE" | null>(null);
  const [activeStep, setActiveStep] = useState(0);
  const [dniBusqueda, setDniBusqueda] = useState("");
  const [modoEdicion, setModoEdicion] = useState(false);
  const [personaEditada, setPersonaEditada] = useState(false);
  const [alertaMostrada, setAlertaMostrada] = useState(false);

  const [empleadoData, setEmpleadoData] = useState<any>({});
  const [pdfFirmado, setPdfFirmado] = useState<File | null>(null);
  const [imagenEmpleado, setImagenEmpleado] = useState<File | null>(null);
  const [pdfDescargado, setPdfDescargado] = useState(false);
  const [loading, setLoading] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    control,
    reset,
    trigger,
    formState: { errors },
  } = useForm({
    mode: "onChange",
  });

  const { data: dniData } = useBuscarPorDni(dniBusqueda);
  const { data: sucursales } = useGetAllSucursales();
  const { data: roles } = useGetAllRolesActivos();
  const { data: tiposContrato } = useGetAllContrato();
  const idEmpleado = useSelector(selectPerfilEmpleado);

  const crearNuevoFD = useCrearEmpleadoNuevoFormData();
  const crearExistenteFD = useCrearEmpleadoContratoExistenteFormData();
  const actualizarPersonaMutation = useActualizarPersona();

  const steps = tipoFlujo === "NUEVO" ? stepsNuevo : stepsExistente;

  /* ================= VALIDACIONES PERSONALIZADAS ================= */
  const validarSinEspacios = (value: string) => {
    if (!value) return true; // La validación de required se encarga de esto
    if (value.trim() === "") {
      return "No se permiten solo espacios en blanco";
    }
    if (value !== value.trim()) {
      return "No se permiten espacios al inicio o al final";
    }
    return true;
  };

  const validarUsuarioSinEspacios = (value: string) => {
    if (!value) return "Usuario obligatorio";
    if (/\s/.test(value)) {
      return "El usuario no puede contener espacios";
    }
    if (value.trim() === "") {
      return "No se permiten solo espacios en blanco";
    }
    return true;
  };

  const validarContrasenaSinEspacios = (value: string) => {
    if (!value) return "Contraseña obligatoria";
    if (/\s/.test(value)) {
      return "La contraseña no puede contener espacios";
    }
    if (value.trim() === "") {
      return "No se permiten solo espacios en blanco";
    }
    return true;
  };

  /* ================= AUTOLLENADO ================= */
  useEffect(() => {
    if (dniData?.idPersona) {
      const partes = dniData.nombreCompleto?.split(" ") || [];
      setValue("nombres", partes[0] || "");
      setValue("apPaterno", partes[1] || "");
      setValue("apMaterno", partes.slice(2).join(" ") || "");
      setValue("genero", dniData.genero || "");
      setValue("telefono", dniData.telefono || "");
      setValue("correo", dniData.correo || "");
      setValue("fechaNacimiento", dniData.fechaNacimiento || "");
      setValue("tipoDocumento", dniData.tipoDocumento || "DNI");
      setValue("numDocumento", dniData.numDocumento || "");
    }
  }, [dniData, setValue]);

  /* ================= WATCH FORM ================= */
  useEffect(() => {
    const subscription = watch(() => {
      setEmpleadoData(watch());
    });
    return () => subscription.unsubscribe();
  }, [watch]);

  // Detectar cambios en los campos cuando está en modo edición
  useEffect(() => {
    console.log("👀 useEffect de detección - tipoFlujo:", tipoFlujo, "modoEdicion:", modoEdicion);
    
    if (tipoFlujo === "EXISTENTE" && modoEdicion && dniData?.idPersona) {
      console.log("✅ Activando detector de cambios para persona existente");
      
      const subscription = watch((value, { name, type }) => {
        console.log("📝 Cambio detectado:", { name, type, value: value[name], modoEdicion });
        
        if (name && modoEdicion) {
          console.log(`🔄 Campo '${name}' modificado, activando personaEditada`);
          setPersonaEditada(true);
        }
      });
      
      return () => {
        console.log("🧹 Limpiando detector de cambios");
        subscription.unsubscribe();
      };
    } else {
      console.log("⚠️ Condiciones no cumplidas para activar detector:");
      console.log("   - tipoFlujo === 'EXISTENTE':", tipoFlujo === "EXISTENTE");
      console.log("   - modoEdicion:", modoEdicion);
      console.log("   - dniData?.idPersona:", dniData?.idPersona);
    }
  }, [watch, tipoFlujo, modoEdicion, dniData]);

  /* ================= MOSTRAR ALERTA CUANDO NO PUEDE CONTRATAR ================= */
  useEffect(() => {
    if (dniData?.idPersona && !dniData?.puedeContratar && !alertaMostrada) {
      Swal.fire({
        icon: "warning",
        title: "No se puede contratar",
        text: dniData?.mensajeEstado || "Esta persona no puede ser contratada en este momento",
        confirmButtonText: "Entendido"
      });
      setAlertaMostrada(true);
    }
    
    // Resetear la alerta cuando se limpia la búsqueda
    if (dniBusqueda.length !== 8) {
      setAlertaMostrada(false);
    }
  }, [dniData, alertaMostrada, dniBusqueda]);

  /* ================= HELPERS ================= */
  const textoPorId = (lista: any[], id: number, idKey: string, textKey: string) =>
    lista?.find((i) => Number(i[idKey]) === Number(id))?.[textKey] || "";

  const empleadoPdfData = {
    ...empleadoData,
    sucursalTexto: textoPorId(
      sucursales?.data,
      empleadoData.idSucursal,
      "idSucursal",
      "nombre"
    ),
    rolTexto: textoPorId(roles?.data, empleadoData.idRol, "idRol", "nombre"),
    tipoContratoTexto: textoPorId(
      tiposContrato?.data,
      empleadoData.idTipoContrato,
      "idTipoContrato",
      "descripcion"
    ),
  };

  /* ================= VALIDACION STEP ================= */
  const validarStepActual = async () => {
    let camposStep: string[] = [];

    if (tipoFlujo === "NUEVO") {
      switch (activeStep) {
        case 0:
          camposStep = ["usuario", "contrasena"];
          break;
        case 1:
          camposStep = [
            "tipoDocumento",
            "numDocumento",
            "nombres",
            "apPaterno",
            "apMaterno",
            "genero",
            "telefono",
            "correo",
            "direccion",
          ];
          break;
        case 2:
          camposStep = ["idSucursal", "idTipoContrato", "idRol", "fechaInicio", "salario"];
          break;
      }
    } else if (tipoFlujo === "EXISTENTE") {
      switch (activeStep) {
        case 0:
          camposStep = [
            "tipoDocumento",
            "numDocumento",
            "nombres",
            "apPaterno",
            "apMaterno",
            "genero",
            "telefono",
            "correo",
            "direccion",
          ];
          break;
        case 1:
          camposStep = ["idSucursal", "idTipoContrato", "idRol", "fechaInicio", "salario"];
          break;
      }
    }

    await trigger(camposStep);

    const erroresStep = camposStep.filter((campo) => !!errors[campo]);
    if (erroresStep.length > 0) {
      Swal.fire({
        icon: "warning",
        title: "Campos incompletos o inválidos",
        text: "Por favor, complete correctamente todos los campos requeridos antes de continuar.",
      });
      return false;
    }

    return true;
  };

  /* ================= NAV ================= */
  const handleNext = async () => {
    const valido = await validarStepActual();
    if (!valido) return;

    // Validación especial: PDF
    if (
      (tipoFlujo === "NUEVO" && activeStep === 3) ||
      (tipoFlujo === "EXISTENTE" && activeStep === 2)
    ) {
      if (!pdfDescargado) {
        Swal.fire({
          icon: "warning",
          title: "PDF no descargado",
          text: "Debe descargar el PDF antes de continuar",
        });
        return;
      }
    }

    setActiveStep((s) => s + 1);
  };

  const handleBack = () => setActiveStep((s) => s - 1);

  /* ================= BOTON SIGUIENTE DINAMICO ================= */
  const siguienteDisabled = () => {
    // Paso dirección
    if ((tipoFlujo === "NUEVO" && activeStep === 1) ||
        (tipoFlujo === "EXISTENTE" && activeStep === 0)) {
      return !watch("direccion");
    }

    // Paso PDF
    if ((tipoFlujo === "NUEVO" && activeStep === 3) ||
        (tipoFlujo === "EXISTENTE" && activeStep === 2)) {
      return !pdfDescargado;
    }

    return false;
  };

  /* ================= PDF ================= */
  const descargarPDF = async () => {
    const element = document.getElementById("empleado-pdf");
    if (!element) return;
    const canvas = await html2canvas(element, { scale: 3 });
    const imgData = canvas.toDataURL("image/png");
    const pdf = new jsPDF("p", "mm", "a4");
    const width = pdf.internal.pageSize.getWidth();
    const height = (canvas.height * width) / canvas.width;
    pdf.addImage(imgData, "PNG", 0, 0, width, height);
    pdf.save("empleado.pdf");

    setPdfDescargado(true);
  };

  /* ================= SUBMIT ================= */
  const onSubmit = async (data: any) => {
    console.log("🚀 INICIANDO SUBMIT");
    console.log("   - tipoFlujo:", tipoFlujo);
    console.log("   - personaEditada:", personaEditada);
    console.log("   - modoEdicion:", modoEdicion);
    
    if (!pdfFirmado || !imagenEmpleado) {
      Swal.fire("Error", "Debe subir PDF firmado e imagen", "warning");
      return;
    }

    setLoading(true);

    const formatFecha = (f: string) => (f ? f.replace("T", " ") + ":00" : null);

    try {
      if (tipoFlujo === "EXISTENTE" && personaEditada) {
        console.log("=".repeat(60));
        console.log("🔄 ACTUALIZANDO DATOS DE PERSONA");
        console.log("=".repeat(60));
        
        const personaPayload = {
          nombres: data.nombres,
          apPaterno: data.apPaterno,
          apMaterno: data.apMaterno,
          genero: data.genero,
          tipoDocumento: data.tipoDocumento,
          numDocumento: data.numDocumento,
          telefono: data.telefono,
          correo: data.correo,
          fechaNacimiento: data.fechaNacimiento,
        };
        
        console.log("🆔 ID de Persona:", dniData.idPersona);
        console.log("🌐 URL:", `rest-restaurant-api/api/empleado/actualizar/${dniData.idPersona}`);
        console.log("📦 JSON QUE SE ENVÍA EN EL BODY:");
        console.log(JSON.stringify(personaPayload, null, 2));
        console.table(personaPayload);
        
        // Estructura correcta según tu service: { id, data }
        const resultadoActualizacion = await actualizarPersonaMutation.mutateAsync({
          id: dniData.idPersona,
          data: personaPayload
        });
        
        console.log("✅ PERSONA ACTUALIZADA EXITOSAMENTE");
        console.log("📥 RESPUESTA DEL SERVIDOR:");
        console.log(JSON.stringify(resultadoActualizacion, null, 2));
        console.log("=".repeat(60));
      } else {
        console.log("ℹ️ No se requiere actualizar persona");
        console.log("   - Flujo:", tipoFlujo);
        console.log("   - personaEditada:", personaEditada);
      }

      const jsonFinal =
        tipoFlujo === "EXISTENTE"
          ? {
              persona: { idPersona: dniData.idPersona, numDocumento: dniData.numDocumento },
              empleado: { direccion: data.direccion, imagenConductor_url: "" },
              contrato: {
                idSucursal: Number(data.idSucursal),
                idTipoContrato: Number(data.idTipoContrato),
                idRol: Number(data.idRol),
                fechaInicio: formatFecha(data.fechaInicio),
                salario: Number(data.salario),
              },
            }
          : {
              credenciales: { usuario: data.usuario, contrasena: data.contrasena },
              persona: {
                tipoDocumento: data.tipoDocumento,
                numDocumento: data.numDocumento,
                nombres: data.nombres,
                apPaterno: data.apPaterno,
                apMaterno: data.apMaterno,
                genero: data.genero,
                telefono: data.telefono,
                correo: data.correo,
                fechaNacimiento: data.fechaNacimiento,
              },
              empleado: { direccion: data.direccion, imagenConductor_url: "" },
              contrato: {
                idSucursal: Number(data.idSucursal),
                idTipoContrato: Number(data.idTipoContrato),
                idRol: Number(data.idRol),
                fechaInicio: formatFecha(data.fechaInicio),
                salario: Number(data.salario),
              },
            };

      const payload = { idAdmin:idEmpleado?.idEmpleado  ,data: jsonFinal, pdfFirmado, imagenEmpleado };
      const mutation = tipoFlujo === "EXISTENTE" ? crearExistenteFD : crearNuevoFD;

      mutation.mutate(payload, {
        onSuccess: () => {
          setLoading(false);
          Swal.fire("Éxito", "Empleado registrado correctamente", "success");
          reset();
          setTipoFlujo(null);
          setActiveStep(0);
          setDniBusqueda("");
          setPdfFirmado(null);
          setImagenEmpleado(null);
          setEmpleadoData({});
          setModoEdicion(false);
          setPersonaEditada(false);
          setPdfDescargado(false);
        },
        onError: (error: any) => {
          setLoading(false);
          Swal.fire(
            "Error", 
            error?.response?.data?.message || "Ocurrió un error al registrar el empleado", 
            "error"
          );
        },
      });
    } catch (error: any) {
      setLoading(false);
      Swal.fire(
        "Error", 
        error?.response?.data?.message || "Ocurrió un error inesperado", 
        "error"
      );
    }
  };

  /* ================= UI ================= */
  if (!tipoFlujo) {
    return (
      <Box sx={{ mt: 4 }}>
        <TextField
          label="DNI"
          value={dniBusqueda}
          onChange={(e) => setDniBusqueda(e.target.value)}
          fullWidth
          onKeyPress={(e) => {
            if (!/[0-9]/.test(e.key)) {
              e.preventDefault();
            }
          }}
          inputProps={{
            maxLength: 8,
            inputMode: 'numeric',
            pattern: '[0-9]*'
          }}
        />
        {dniBusqueda.length === 8 && (
          <Box sx={{ mt: 2 }}>
            {dniData?.idPersona ? (
              <>
                <Typography variant="h5">Persona encontrada</Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1, mb: 2 }}>
                  {dniData.nombreCompleto}
                </Typography>
                
                {dniData.puedeContratar ? (
                  <Button
                    variant="contained"
                    onClick={() => setTipoFlujo("EXISTENTE")}
                  >
                    Agregar Persona como Empleado
                  </Button>
                ) : (
                  <Typography variant="body2" color="error" sx={{ mt: 2 }}>
                    ❌ {dniData.mensajeEstado || "No se puede contratar a esta persona"}
                  </Typography>
                )}
              </>
            ) : (
              <>
                <Typography>No existe la persona</Typography>
                <Button
                  variant="contained"
                  color="success"
                  onClick={() => setTipoFlujo("NUEVO")}
                >
                  Agregar Empleado Nuevo
                </Button>
              </>
            )}
          </Box>
        )}
      </Box>
    );
  }

  return (
    <Box sx={{ mt: 4 }}>
      {/* ================= LOADING BACKDROP ================= */}
      <Backdrop
        sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
          <CircularProgress color="inherit" size={60} />
          <Typography variant="h6">Procesando registro...</Typography>
          <Typography variant="body2">Por favor espere</Typography>
        </Box>
      </Backdrop>

      <Button
        variant="outlined"
        color="secondary"
        onClick={() => {
          setTipoFlujo(null);
          setActiveStep(0);
          setDniBusqueda("");
          reset();
          setPdfFirmado(null);
          setImagenEmpleado(null);
          setEmpleadoData({});
          setModoEdicion(false);
          setPersonaEditada(false);
          setPdfDescargado(false);
        }}
        sx={{ mb: 2 }}
        disabled={loading}
      >
        Regresar al Buscador
      </Button>

      <Stepper nonLinear activeStep={activeStep}>
        {steps.map((label, i) => (
          <Step key={label}>
            <StepButton onClick={() => setActiveStep(i)}>{label}</StepButton>
          </Step>
        ))}
      </Stepper>

      <Box
        component="form"
        onSubmit={handleSubmit(onSubmit)}
        sx={{ mt: 3, display: "flex", flexDirection: "column", gap: 2 }}
      >
        {/* ================== STEP: Credenciales ================== */}
        {tipoFlujo === "NUEVO" && activeStep === 0 && (
          <>
            <TextField
              label="Usuario"
              {...register("usuario", { 
                required: "Usuario obligatorio",
                validate: validarUsuarioSinEspacios
              })}
              error={!!errors.usuario}
              helperText={errors.usuario?.message}
              fullWidth
            />
            <TextField
              label="Contraseña"
              type="password"
              {...register("contrasena", { 
                required: "Contraseña obligatoria",
                validate: validarContrasenaSinEspacios
              })}
              error={!!errors.contrasena}
              helperText={errors.contrasena?.message}
              fullWidth
            />
          </>
        )}

        {/* ================== STEP: Datos Personales ================== */}
        {activeStep === (tipoFlujo === "NUEVO" ? 1 : 0) && (
          <>
            {tipoFlujo === "EXISTENTE" && !modoEdicion && (
              <Button
                variant="contained"
                onClick={() => {
                  console.log("✏️ Modo edición activado");
                  setModoEdicion(true);
                  setPersonaEditada(false); // Reset al entrar en modo edición
                }}
                sx={{ mb: 2 }}
              >
                Editar Datos
              </Button>
            )}

            {tipoFlujo === "EXISTENTE" && modoEdicion && (
              <Typography variant="caption" color="info.main" sx={{ mb: 2, display: 'block' }}>
                ℹ️ Modo edición activo - personaEditada: {personaEditada ? "✅ true" : "❌ false"}
              </Typography>
            )}

            <Controller
              name="tipoDocumento"
              control={control}
              rules={{ required: "Seleccione un tipo de documento" }}
              defaultValue="DNI"
              render={({ field }) => (
                <TextField
                  select
                  label="Tipo Documento"
                  {...field}
                  fullWidth
                  error={!!errors.tipoDocumento}
                  helperText={errors.tipoDocumento?.message}
                  disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
                >
                  <MenuItem value="DNI">DNI</MenuItem>
                  <MenuItem value="CE">CE</MenuItem>
                </TextField>
              )}
            />

            <TextField
              label="Número Documento"
              {...register("numDocumento", {
                required: "DNI obligatorio",
                pattern: { value: /^[0-9]{8}$/, message: "DNI debe tener 8 dígitos" },
                validate: validarSinEspacios
              })}
              error={!!errors.numDocumento}
              helperText={errors.numDocumento?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault();
                }
              }}
              inputProps={{
                maxLength: 8,
                inputMode: 'numeric',
                pattern: '[0-9]*'
              }}
            />
            <TextField
              label="Nombres"
              {...register("nombres", { 
                required: "Nombre obligatorio",
                validate: validarSinEspacios
              })}
              error={!!errors.nombres}
              helperText={errors.nombres?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Apellido Paterno"
              {...register("apPaterno", { 
                required: "Apellido Paterno obligatorio",
                validate: validarSinEspacios
              })}
              error={!!errors.apPaterno}
              helperText={errors.apPaterno?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Apellido Materno"
              {...register("apMaterno", { 
                required: "Apellido Materno obligatorio",
                validate: validarSinEspacios
              })}
              error={!!errors.apMaterno}
              helperText={errors.apMaterno?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <Controller
              name="genero"
              control={control}
              rules={{ required: "Seleccione un género" }}
              render={({ field }) => (
                <TextField
                  select
                  label="Género"
                  {...field}
                  fullWidth
                  error={!!errors.genero}
                  helperText={errors.genero?.message}
                  disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
                >
                  <MenuItem value="M">Masculino</MenuItem>
                  <MenuItem value="F">Femenino</MenuItem>
                </TextField>
              )}
            />
            <TextField
              label="Teléfono"
              {...register("telefono", {
                required: "Teléfono obligatorio",
                pattern: { value: /^[0-9]{9}$/, message: "Debe tener 9 dígitos" },
                validate: validarSinEspacios
              })}
              error={!!errors.telefono}
              helperText={errors.telefono?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault();
                }
              }}
              inputProps={{
                maxLength: 9,
                inputMode: 'numeric',
                pattern: '[0-9]*'
              }}
            />
            <TextField
              label="Correo"
              {...register("correo", {
                required: "Correo obligatorio",
                pattern: {
                  value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                  message: "Correo no válido",
                },
                validate: validarSinEspacios
              })}
              error={!!errors.correo}
              helperText={errors.correo?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Dirección"
              {...register("direccion", { 
                required: "Dirección obligatoria",
                validate: validarSinEspacios
              })}
              error={!!errors.direccion}
              helperText={errors.direccion?.message}
              fullWidth
            />
          </>
        )}

        {/* ================== STEP: Datos Empleado ================== */}
        {activeStep === (tipoFlujo === "NUEVO" ? 2 : 1) && (
          <>
            <Controller
              name="idSucursal"
              control={control}
              rules={{ required: "Seleccione sucursal" }}
              render={({ field }) => (
                <TextField
                  select
                  label="Sucursal"
                  {...field}
                  fullWidth
                  error={!!errors.idSucursal}
                  helperText={errors.idSucursal?.message}
                >
                  {sucursales?.data?.map((s: any) => (
                    <MenuItem key={s.idSucursal} value={s.idSucursal}>
                      {s.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              )}
            />
            <Controller
              name="idRol"
              control={control}
              rules={{ required: "Seleccione rol" }}
              render={({ field }) => (
                <TextField
                  select
                  label="Rol"
                  {...field}
                  fullWidth
                  error={!!errors.idRol}
                  helperText={errors.idRol?.message}
                >
                  {roles?.data?.map((r: any) => (
                    <MenuItem key={r.idRol} value={r.idRol}>
                      {r.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              )}
            />
            <Controller
              name="idTipoContrato"
              control={control}
              rules={{ required: "Seleccione tipo contrato" }}
              render={({ field }) => (
                <TextField
                  select
                  label="Tipo Contrato"
                  {...field}
                  fullWidth
                  error={!!errors.idTipoContrato}
                  helperText={errors.idTipoContrato?.message}
                >
                  {tiposContrato?.data?.map((c: any) => (
                    <MenuItem key={c.idTipoContrato} value={c.idTipoContrato}>
                      {c.descripcion}
                    </MenuItem>
                  ))}
                </TextField>
              )}
            />
            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="es">
              <Controller
                name="fechaInicio"
                control={control}
                rules={{ required: "Fecha inicio obligatoria" }}
                render={({ field }) => (
                  <DateTimePicker
                    label="Fecha Inicio"
                    value={field.value ? dayjs(field.value) : null}
                    onChange={(newValue) => {
                      field.onChange(newValue ? newValue.format('YYYY-MM-DDTHH:mm') : '');
                    }}
                    slotProps={{
                      textField: {
                        fullWidth: true,
                        error: !!errors.fechaInicio,
                        helperText: errors.fechaInicio?.message,
                        InputProps: {
                          readOnly: true,
                        },
                      },
                      field: {
                        readOnly: true,
                      },
                    }}
                  />
                )}
              />
            </LocalizationProvider>
            <TextField
              label="Salario"
              {...register("salario", { 
                required: "Salario obligatorio",
                min: { value: 0, message: "El salario debe ser mayor a 0" }
              })}
              error={!!errors.salario}
              helperText={errors.salario?.message}
              fullWidth
              onKeyPress={(e) => {
                if (!/[0-9.]/.test(e.key)) {
                  e.preventDefault();
                }
              }}
              InputProps={{
                startAdornment: <InputAdornment position="start">S/</InputAdornment>,
              }}
              inputProps={{
                inputMode: 'decimal',
                pattern: '[0-9]*[.]?[0-9]*'
              }}
            />
          </>
        )}

        {/* ================== STEP: PDF Preview ================== */}
        {(tipoFlujo === "NUEVO" ? 3 : 2) === activeStep && (
          <Box>
            <EmpleadoPdf data={empleadoPdfData} id="empleado-pdf" />
            <Button variant="contained" onClick={descargarPDF} sx={{ mt: 2 }}>
              Descargar PDF
            </Button>
          </Box>
        )}

        {/* ================== STEP: Subir PDF firmado ================== */}
        {(tipoFlujo === "NUEVO" ? 4 : 3) === activeStep && (
<Box>
  <Typography variant="h6" sx={{ mb: 2 }}>
    Subir PDF Firmado
  </Typography>

  <TextField
    fullWidth
    type="file"
    inputProps={{ accept: "application/pdf" }}
    onChange={(e) => setPdfFirmado(e.target.files?.[0] || null)}
    sx={{
      height: 150,
      "& .MuiInputBase-root": {
        height: "100%",
        alignItems: "center",
      },
      "& input": {
        height: "100%",
        cursor: "pointer",
      },
    }}
  />

  {pdfFirmado && (
    <Box sx={{ mt: 2, p: 2, border: "1px solid #ddd", borderRadius: 1 }}>
      <Typography variant="body2" color="success.main">
        ✅ PDF cargado: {pdfFirmado.name}
      </Typography>
      <Typography variant="caption" color="text.secondary">
        Tamaño: {(pdfFirmado.size / 1024).toFixed(2)} KB
      </Typography>
    </Box>
  )}
</Box>

        )}

        {/* ================== STEP: Subir Imagen ================== */}
        {(tipoFlujo === "NUEVO" ? 5 : 4) === activeStep && (
<Box>
  <Typography variant="h6" sx={{ mb: 2 }}>
    Subir Imagen del Empleado
  </Typography>

  <TextField
    fullWidth
    type="file"
    inputProps={{ accept: "image/*" }}
    onChange={(e) => setImagenEmpleado(e.target.files?.[0] || null)}
    sx={{
      height: 150,
      "& .MuiInputBase-root": {
        height: "100%",
        alignItems: "center",
      },
      "& input": {
        height: "100%",
        cursor: "pointer",
      },
      mb: 2,
    }}
  />

  {imagenEmpleado && (
    <Box sx={{ mt: 2 }}>
      <Typography variant="body2" color="success.main" sx={{ mb: 1 }}>
        ✅ Imagen cargada: {imagenEmpleado.name}
      </Typography>

      <Box
        component="img"
        src={URL.createObjectURL(imagenEmpleado)}
        alt="Vista previa"
        sx={{
          width: 250,
          height: 250,
          objectFit: "cover",
          border: "2px solid #ddd",
          borderRadius: 1,
          display: "block",
        }}
      />
    </Box>
  )}
</Box>

        )}

        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 3 }}>
          <Button disabled={activeStep === 0 || loading} onClick={handleBack}>
            Volver
          </Button>

          {/* Botón Siguiente solo si NO estamos en el último paso */}
          {activeStep < steps.length - 1 && (
            <Button 
              onClick={handleNext} 
              disabled={siguienteDisabled() || loading}
            >
              Siguiente
            </Button>
          )}

          {/* Botón Finalizar en el último paso (Subir Imagen) */}
          {activeStep === steps.length - 1 && (
            <Button 
              type="submit" 
              variant="contained" 
              color="success"
              disabled={!pdfFirmado || !imagenEmpleado || loading}
            >
              Finalizar Registro
            </Button>
          )}
        </Box>
      </Box>
    </Box>
  );
}