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
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import Swal from "sweetalert2";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

import {
  useBuscarPorDni,
  useActualizarPersona,
} from "../../services/empleado.service";
import {
  useCrearEmpleadoNuevoFormData,
  useCrearEmpleadoContratoExistenteFormData,
} from "../../services/empleado.formdata.service";
import { useGetAllSucursales } from "../../services/sucursales.service";
import { useGetAllRoles } from "../../services/roles.service";
import { useGetAllContrato } from "../../services/contrato.service";

import EmpleadoPdf from "../../pdf/EmpleadoPdf";

/* ================= STEPS ================= */
const stepsNuevo = [
  "Credenciales",
  "Datos Personales",
  "Datos Empleado",
  "Vista Previa PDF",
  "Subir PDF Firmado",
  "Subir Imagen",
  "Confirmar",
];

const stepsExistente = [
  "Datos Personales",
  "Datos Empleado",
  "Vista Previa PDF",
  "Subir PDF Firmado",
  "Subir Imagen",
  "Confirmar",
];

export default function RegistroEmpleadoStepper() {
  const [tipoFlujo, setTipoFlujo] = useState<"NUEVO" | "EXISTENTE" | null>(null);
  const [activeStep, setActiveStep] = useState(0);
  const [dniBusqueda, setDniBusqueda] = useState("");
  const [modoEdicion, setModoEdicion] = useState(false);
  const [personaEditada, setPersonaEditada] = useState(false);

  const [empleadoData, setEmpleadoData] = useState<any>({});
  const [pdfFirmado, setPdfFirmado] = useState<File | null>(null);
  const [imagenEmpleado, setImagenEmpleado] = useState<File | null>(null);

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    control,
    reset,
    formState: { errors, isValid },
  } = useForm({
    mode: "onChange", // validación en tiempo real
  });

  const { data: dniData } = useBuscarPorDni(dniBusqueda);
  const { data: sucursales } = useGetAllSucursales();
  const { data: roles } = useGetAllRoles();
  const { data: tiposContrato } = useGetAllContrato();

  const crearNuevoFD = useCrearEmpleadoNuevoFormData();
  const crearExistenteFD = useCrearEmpleadoContratoExistenteFormData();
  const actualizarPersonaMutation = useActualizarPersona();

  const steps = tipoFlujo === "NUEVO" ? stepsNuevo : stepsExistente;

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

  /* ================= HELPERS ================= */
  const actualizarEmpleadoData = () => setEmpleadoData(watch());

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

  /* ================= NAV ================= */
  const handleNext = () => {
    actualizarEmpleadoData();
    setActiveStep((s) => s + 1);
  };
  const handleBack = () => setActiveStep((s) => s - 1);

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
  };

  /* ================= SUBMIT ================= */
  const onSubmit = async (data: any) => {
    if (!pdfFirmado || !imagenEmpleado) {
      Swal.fire("Error", "Debe subir PDF firmado e imagen", "warning");
      return;
    }

    const formatFecha = (f: string) => (f ? f.replace("T", " ") + ":00" : null);

    // Si es EXISTENTE y se editó la persona, actualizarla
    if (tipoFlujo === "EXISTENTE" && personaEditada) {
      const personaPayload = {
        idPersona: dniData.idPersona,
        tipoDocumento: data.tipoDocumento,
        numDocumento: data.numDocumento,
        nombres: data.nombres,
        apPaterno: data.apPaterno,
        apMaterno: data.apMaterno,
        genero: data.genero,
        telefono: data.telefono,
        correo: data.correo,
        fechaNacimiento: data.fechaNacimiento,
      };

      await actualizarPersonaMutation.mutateAsync(personaPayload);
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

    const payload = { data: jsonFinal, pdfFirmado, imagenEmpleado };
    const mutation = tipoFlujo === "EXISTENTE" ? crearExistenteFD : crearNuevoFD;

    mutation.mutate(payload, {
      onSuccess: () => {
        Swal.fire("Éxito", "Empleado registrado", "success");
        reset();
        setTipoFlujo(null);
        setActiveStep(0);
        setDniBusqueda("");
        setPdfFirmado(null);
        setImagenEmpleado(null);
        setEmpleadoData({});
        setModoEdicion(false);
        setPersonaEditada(false);
      },
    });
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
        />
        {dniBusqueda.length === 8 && (
          <Box sx={{ mt: 2 }}>
            {dniData?.idPersona ? (
              <>
                <Typography>Persona encontrada</Typography>
                <Button
                  variant="contained"
                  onClick={() => setTipoFlujo("EXISTENTE")}
                >
                  Agregar Persona como Empleado
                </Button>
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
        }}
        sx={{ mb: 2 }}
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
        {/* STEP Credenciales */}
        {tipoFlujo === "NUEVO" && activeStep === 0 && (
          <>
            <TextField
              label="Usuario"
              {...register("usuario", { required: "Usuario obligatorio" })}
              error={!!errors.usuario}
              helperText={errors.usuario?.message}
              fullWidth
            />
            <TextField
              label="Contraseña"
              type="password"
              {...register("contrasena", { required: "Contraseña obligatoria" })}
              error={!!errors.contrasena}
              helperText={errors.contrasena?.message}
              fullWidth
            />
          </>
        )}

        {/* STEP Datos Personales */}
        {activeStep === (tipoFlujo === "NUEVO" ? 1 : 0) && (
          <>
            {tipoFlujo === "EXISTENTE" && !modoEdicion && (
              <Button
                variant="contained"
                onClick={() => setModoEdicion(true)}
                sx={{ mb: 2 }}
              >
                Editar Datos
              </Button>
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
              })}
              error={!!errors.numDocumento}
              helperText={errors.numDocumento?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Nombres"
              {...register("nombres", { required: "Nombre obligatorio" })}
              error={!!errors.nombres}
              helperText={errors.nombres?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Apellido Paterno"
              {...register("apPaterno", { required: "Apellido Paterno obligatorio" })}
              error={!!errors.apPaterno}
              helperText={errors.apPaterno?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Apellido Materno"
              {...register("apMaterno", { required: "Apellido Materno obligatorio" })}
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
              })}
              error={!!errors.telefono}
              helperText={errors.telefono?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Correo"
              {...register("correo", {
                required: "Correo obligatorio",
                pattern: {
                  value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                  message: "Correo no válido",
                },
              })}
              error={!!errors.correo}
              helperText={errors.correo?.message}
              fullWidth
              disabled={tipoFlujo === "EXISTENTE" && !modoEdicion}
            />
            <TextField
              label="Dirección"
              {...register("direccion", { required: "Dirección obligatoria" })}
              error={!!errors.direccion}
              helperText={errors.direccion?.message}
              fullWidth
            />
          </>
        )}

        {/* STEP Contrato */}
        {activeStep === (tipoFlujo === "NUEVO" ? 2 : 1) && (
          <>
            <Controller
              name="idSucursal"
              control={control}
              rules={{ required: "Seleccione una sucursal" }}
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
              name="idTipoContrato"
              control={control}
              rules={{ required: "Seleccione un tipo de contrato" }}
              render={({ field }) => (
                <TextField
                  select
                  label="Tipo Contrato"
                  {...field}
                  fullWidth
                  error={!!errors.idTipoContrato}
                  helperText={errors.idTipoContrato?.message}
                >
                  {tiposContrato?.data?.map((t: any) => (
                    <MenuItem key={t.idTipoContrato} value={t.idTipoContrato}>
                      {t.descripcion}
                    </MenuItem>
                  ))}
                </TextField>
              )}
            />
            <Controller
              name="idRol"
              control={control}
              rules={{ required: "Seleccione un rol" }}
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
            <TextField
              type="datetime-local"
              label="Fecha Inicio"
              InputLabelProps={{ shrink: true }}
              {...register("fechaInicio", { required: "Fecha inicio obligatoria" })}
              error={!!errors.fechaInicio}
              helperText={errors.fechaInicio?.message}
              fullWidth
            />
            <FormControl fullWidth>
              <OutlinedInput
                type="number"
                {...register("salario", { required: "Salario obligatorio" })}
                startAdornment={<InputAdornment position="start">S/.</InputAdornment>}
                error={!!errors.salario}
              />
              <FormHelperText>{errors.salario?.message || "Salario"}</FormHelperText>
            </FormControl>
          </>
        )}

        {/* STEP PDF */}
        {activeStep === (tipoFlujo === "NUEVO" ? 3 : 2) && (
          <>
            <EmpleadoPdf data={empleadoPdfData} />
            <Button onClick={descargarPDF}>Descargar PDF</Button>
          </>
        )}

        {/* STEP Subir PDF */}
        {activeStep === (tipoFlujo === "NUEVO" ? 4 : 3) && (
          <Button component="label">
            Subir PDF Firmado
            <input
              hidden
              type="file"
              accept="application/pdf"
              onChange={(e) => setPdfFirmado(e.target.files?.[0] || null)}
            />
          </Button>
        )}

        {/* STEP Subir Imagen */}
        {activeStep === (tipoFlujo === "NUEVO" ? 5 : 4) && (
          <Button component="label">
            Subir Imagen
            <input
              hidden
              type="file"
              accept="image/*"
              onChange={(e) => setImagenEmpleado(e.target.files?.[0] || null)}
            />
          </Button>
        )}

        {/* STEP Confirmar */}
        {activeStep === (tipoFlujo === "NUEVO" ? 6 : 5) && (
          <Button type="submit" variant="contained" color="success" disabled={!isValid}>
            Registrar Empleado
          </Button>
        )}

        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 3 }}>
          <Button disabled={activeStep === 0} onClick={handleBack}>
            Volver
          </Button>
          {activeStep < steps.length - 1 && (
            <Button onClick={handleNext} disabled={!isValid}>
              Siguiente
            </Button>
          )}
        </Box>
      </Box>
    </Box>
  );
}
