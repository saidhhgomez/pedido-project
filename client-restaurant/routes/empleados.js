const express = require('express');
const router = express.Router();
const { crearEmpleado } = require('../controllers/empleadosController');

router.post('/', crearEmpleado);

module.exports = router;
