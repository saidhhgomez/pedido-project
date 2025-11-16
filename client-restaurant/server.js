const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
require('dotenv').config();

const app = express();

// Middlewares
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));

// Rutas
const empleadosRoutes = require('./routes/empleados');
app.use('/empleados', empleadosRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Servidor corriendo en puerto ${PORT}`));
