const axios = require('axios');

const crearEmpleado = async (req, res) => {
  const data = {
    credenciales: {
      usuario: req.body.usuario,
      contrasena: req.body.contrasena
    },
    persona: {
      nombres: req.body.nombres,
      apPaterno: req.body.apPaterno,
      apMaterno: req.body.apMaterno,
      genero: req.body.genero,
      tipoDocumento: req.body.tipoDocumento,
      numDocumento: req.body.numDocumento,
      telefono: req.body.telefono,
      correo: req.body.correo,
      fechaNacimiento: req.body.fechaNacimiento
    },
    empleado: {
      direccion: req.body.direccion,
      estadoEmpleado: req.body.estadoEmpleado,
      imagenConductor_url: req.body.imagenConductor_url
    }
  };

  try {
    // Llamada a tu endpoint de Jersey
    const response = await axios.post(process.env.API_JERSEY_URL, data, {
      headers: { 'Content-Type': 'application/json' }
    });

    // Mostrar respuesta de la API Jersey en el navegador
    res.send(`<h2>Empleado creado con éxito!</h2><pre>${JSON.stringify(response.data, null, 2)}</pre>`);

  } catch (error) {
    console.error(error.message);
    if (error.response) {
      res.status(error.response.status)
         .send(`<h2>Error API Jersey</h2><pre>${JSON.stringify(error.response.data, null, 2)}</pre>`);
    } else {
      res.status(500)
         .send(`<h2>Error al conectar con API Jersey</h2><pre>${error.message}</pre>`);
    }
  }
};

module.exports = { crearEmpleado };
