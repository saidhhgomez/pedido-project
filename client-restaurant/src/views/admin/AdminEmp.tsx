// RegistroEmpleadoStepper.tsx

import React, { useState, useEffect } from "react";
import {
  Box,
  Button,
  TextField,
  MenuItem,
  Stepper,
  Step,
  StepButton,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import Swal from "sweetalert2";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

import {
  useBuscarPorDni,
  crearEmpleadoNuevaPersona,
  useCrearEmpleadoPersonaExistente,
} from "../../services/empleado.service";
import { useGetAllSucursales } from "../../services/sucursales.service";
import { useGetAllRoles } from "../../services/roles.service";
import { useGetAllContrato } from "../../services/contrato.service";
import EmpleadoPdf from "../../pdf/EmpleadoPdf";

const steps = [
  "Credenciales",
  "Datos Personales",
  "Datos Empleado",
  "Contrato",
  "Vista Previa PDF",
  "Confirmar Registro",
];

export default function RegistroEmpleadoStepper() {
  const [activeStep, setActiveStep] = useState(0);
  const [completed, setCompleted] = useState<{ [k: number]: boolean }>({});
  const [dniBusqueda, setDniBusqueda] = useState("");
  const [empleadoData, setEmpleadoData] = useState<any>({});

  const { register, handleSubmit, watch, setValue, control, reset } = useForm();

  // Selects
  const { data: sucursales } = useGetAllSucursales();
  const { data: roles } = useGetAllRoles();
  const { data: tiposContrato } = useGetAllContrato();

  // Persona por DNI
  const { data: dniData } = useBuscarPorDni(dniBusqueda);

  // Mutaciones
  const crearExistenteMutation = useCrearEmpleadoPersonaExistente();

  // --- AUTO-LENADO POR DNI ---
  useEffect(() => {
    if (dniData) {
      const nombresPartes = dniData.nombreCompleto.split(" ");
      const nombres = nombresPartes[0] || "";
      const apPaterno = nombresPartes[1] || "";
      const apMaterno = nombresPartes.slice(2).join(" ") || "";

      setValue("nombres", nombres);
      setValue("apPaterno", apPaterno);
      setValue("apMaterno", apMaterno);
      setValue("genero", dniData.genero || "");
      setValue("tipoDocumento", dniData.tipoDocumento || "");
      setValue("numDocumento", dniData.numDocumento || "");
      setValue("telefono", dniData.telefono || "");
      setValue("correo", dniData.correo || "");
      setValue("fechaNacimiento", dniData.fechaNacimiento || "");
    }
  }, [dniData, setValue]);

  // Capturar búsqueda de DNI
  const dniInput = watch("numDocumento");
  const tipoDocumento = watch("tipoDocumento");
  useEffect(() => {
    if (tipoDocumento && dniInput && dniInput.length === 8) {
      setDniBusqueda(dniInput);
    }
  }, [dniInput, tipoDocumento]);

  // Actualizar datos para PDF
  const actualizarEmpleadoData = () => {
    setEmpleadoData(watch());
  };

  const handleNext = () => {
    actualizarEmpleadoData();
    setActiveStep((s) => s + 1);
  };
  const handleBack = () => setActiveStep((s) => s - 1);

  // Descargar PDF
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

  const onSubmit = (data: any) => {
    completed[activeStep] = true;
    setCompleted({ ...completed });

    actualizarEmpleadoData();

    if (activeStep < steps.length - 2) {
      handleNext();
      return;
    }

    if (activeStep === steps.length - 2) {
      handleNext(); // Paso 4 → Vista previa PDF
      return;
    }

    // Paso 5 → Confirmar Registro
    const formatFecha = (fecha: string) => (fecha ? fecha.replace("T", " ") + ":00" : null);

    const jsonFinal = dniData?.idPersona
      ? {
          persona: { idPersona: dniData.idPersona },
          empleado: {
            direccion: data.direccion,
            imagenConductor_url: data.imagenConductor_url,
            estadoEmpleado: data.estadoEmpleado,
          },
          contrato: {
            idSucursal: Number(data.idSucursal),
            idTipoContrato: Number(data.idTipoContrato),
            idRol: Number(data.idRol),
            fechaInicio: formatFecha(data.fechaInicio),
            fechaFin: formatFecha(data.fechaFin),
            salario: Number(data.salario),
          },
        }
      : {
          credenciales: { usuario: data.usuario, contrasena: data.contrasena },
          persona: {
            nombres: data.nombres,
            apPaterno: data.apPaterno,
            apMaterno: data.apMaterno,
            genero: data.genero,
            tipoDocumento: data.tipoDocumento,
            numDocumento: data.numDocumento,
            telefono: data.telefono,
            correo: data.correo,
            fechaNacimiento: data.fechaNacimiento,
          },
          empleado: {
            direccion: data.direccion,
            imagenConductor_url: data.imagenConductor_url,
            estadoEmpleado: data.estadoEmpleado,
          },
          contrato: {
            idSucursal: Number(data.idSucursal),
            idTipoContrato: Number(data.idTipoContrato),
            idRol: Number(data.idRol),
            fechaInicio: formatFecha(data.fechaInicio),
            fechaFin: formatFecha(data.fechaFin),
            salario: Number(data.salario),
          },
        };

    if (dniData?.idPersona) {
      crearExistenteMutation.mutate(jsonFinal, {
        onSuccess: () => {
          Swal.fire("Éxito", "Empleado registrado correctamente", "success").then(() => {
            reset();
            setActiveStep(0);
            setCompleted({});
            setDniBusqueda("");
            setEmpleadoData({});
          });
        },
        onError: (error) => {
          console.error(error);
          Swal.fire("Error", "No se pudo registrar el empleado", "error");
        },
      });
    } else {
      crearEmpleadoNuevaPersona(jsonFinal)
        .then(() => {
          Swal.fire("Éxito", "Empleado registrado correctamente", "success").then(() => {
            reset();
            setActiveStep(0);
            setCompleted({});
            setDniBusqueda("");
            setEmpleadoData({});
          });
        })
        .catch((err) => {
          console.error(err);
          Swal.fire("Error", "No se pudo registrar el empleado", "error");
        });
    }
  };

  return (
    <Box sx={{ width: "100%", mt: 4 }}>
      <Stepper nonLinear activeStep={activeStep}>
        {steps.map((label, i) => (
          <Step key={label} completed={completed[i]}>
            <StepButton onClick={() => setActiveStep(i)}>{label}</StepButton>
          </Step>
        ))}
      </Stepper>

      <Box
        component="form"
        onSubmit={handleSubmit(onSubmit)}
        sx={{ mt: 4, display: "flex", flexDirection: "column", gap: 2 }}
      >
        {/* Paso 0 → Credenciales */}
        {activeStep === 0 && !dniData?.idPersona && (
          <>
            <TextField fullWidth label="Usuario" {...register("usuario")} />
            <TextField fullWidth type="password" label="Contraseña" {...register("contrasena")} />
          </>
        )}

        {/* Paso 1 → Datos Persona */}
        {activeStep === 1 && (
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
            <Controller
              name="tipoDocumento"
              control={control}
              defaultValue={dniData?.tipoDocumento || ""}
              render={({ field }) => (
                <TextField
                  fullWidth
                  label="Tipo Documento"
                  select
                  {...field}
                  InputLabelProps={{ shrink: true }}
                  SelectProps={{ displayEmpty: true }}
                  disabled={!!dniData?.idPersona}
                >
                  <MenuItem value="">Seleccione tipo</MenuItem>
                  <MenuItem value="DNI">DNI</MenuItem>
                  <MenuItem value="CE">Carnet Extranjería</MenuItem>
                </TextField>
              )}
            />
            <TextField fullWidth label="Número Documento" {...register("numDocumento")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <TextField fullWidth label="Nombres" {...register("nombres")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <TextField fullWidth label="Apellido Paterno" {...register("apPaterno")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <TextField fullWidth label="Apellido Materno" {...register("apMaterno")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <Controller
              name="genero"
              control={control}
              defaultValue={dniData?.genero || ""}
              render={({ field }) => (
                <TextField fullWidth label="Género" select {...field} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona}>
                  <MenuItem value="">Seleccione género</MenuItem>
                  <MenuItem value="M">Masculino</MenuItem>
                  <MenuItem value="F">Femenino</MenuItem>
                </TextField>
              )}
            />
            <TextField fullWidth label="Teléfono" {...register("telefono")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <TextField fullWidth label="Correo" {...register("correo")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
            <TextField fullWidth type="date" label="Fecha de nacimiento" {...register("fechaNacimiento")} InputLabelProps={{ shrink: true }} disabled={!!dniData?.idPersona} />
          </Box>
        )}

        {/* Paso 2 → Datos Empleado */}
        {activeStep === 2 && (
          <>
            <TextField fullWidth label="Dirección" {...register("direccion")} />
            <TextField fullWidth label="URL Imagen" {...register("imagenConductor_url")} />
            <Controller name="estadoEmpleado" control={control} defaultValue="activo" render={({ field }) => (
              <TextField fullWidth select label="Estado" {...field}>
                <MenuItem value="activo">Activo</MenuItem>
                <MenuItem value="inactivo">Inactivo</MenuItem>
              </TextField>
            )} />
          </>
        )}

        {/* Paso 3 → Contrato */}
        {activeStep === 3 && (
          <>
            <Controller
              name="idSucursal"
              control={control}
              defaultValue=""
              render={({ field }) => (
                <TextField fullWidth select label="Sucursal" {...field} onChange={(e) => field.onChange(Number(e.target.value))}>
                  <MenuItem value="">Seleccione sucursal</MenuItem>
                  {sucursales?.data?.map((s: any) => (
                    <MenuItem key={s.idSucursal} value={s.idSucursal}>{s.nombre}</MenuItem>
                  ))}
                </TextField>
              )}
            />
            <Controller
              name="idTipoContrato"
              control={control}
              defaultValue=""
              render={({ field }) => (
                <TextField fullWidth select label="Tipo Contrato" {...field} onChange={(e) => field.onChange(Number(e.target.value))}>
                  <MenuItem value="">Seleccione tipo contrato</MenuItem>
                  {tiposContrato?.data?.map((t: any) => (
                    <MenuItem key={t.idTipoContrato} value={t.idTipoContrato}>{t.descripcion}</MenuItem>
                  ))}
                </TextField>
              )}
            />
            <Controller
              name="idRol"
              control={control}
              defaultValue=""
              render={({ field }) => (
                <TextField fullWidth select label="Rol" {...field} onChange={(e) => field.onChange(Number(e.target.value))}>
                  <MenuItem value="">Seleccione rol</MenuItem>
                  {roles?.data?.map((r: any) => (
                    <MenuItem key={r.idRol} value={r.idRol}>{r.nombre}</MenuItem>
                  ))}
                </TextField>
              )}
            />
            <TextField fullWidth type="datetime-local" InputLabelProps={{ shrink: true }} label="Fecha Inicio" {...register("fechaInicio")} />
            <TextField fullWidth type="datetime-local" InputLabelProps={{ shrink: true }} label="Fecha Fin" {...register("fechaFin")} />
            <TextField fullWidth type="number" label="Salario" {...register("salario")} />
          </>
        )}

        {/* Paso 4 → Vista Previa PDF */}
        {activeStep === 4 && (
          <Box sx={{ mt: 3 }}>
            <h2>Vista previa PDF</h2>
            <EmpleadoPdf data={empleadoData} />
            <Button variant="contained" color="secondary" sx={{ mt: 2 }} onClick={descargarPDF}>
              Descargar PDF
            </Button>
          </Box>
        )}

        {/* Paso 5 → Confirmar Registro */}
        {activeStep === 5 && (
          <Box sx={{ mt: 3 }}>
            <h2>Confirmar Registro</h2>
            <Button variant="contained" color="success" sx={{ mt: 2 }} onClick={handleSubmit(onSubmit)}>
              Registrar Empleado
            </Button>
          </Box>
        )}

        {/* Botones Navegación */}
        <Box sx={{ display: "flex", justifyContent: "space-between", mt: 4 }}>
          <Button disabled={activeStep === 0} onClick={handleBack}>Volver</Button>
          {activeStep < 5 && <Button variant="contained" onClick={handleNext}>Siguiente</Button>}
        </Box>
      </Box>
    </Box>
  );
}
