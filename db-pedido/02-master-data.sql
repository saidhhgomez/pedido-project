USE db_restaurant;

-- TABLA: Credenciales
INSERT INTO Credenciales (usuario, contrasena, fechaCreacion) VALUES
('cliente_generico', 'sistema123', '2025-12-17 21:04:28'),
('said', '$2a$10$wH8P97L9uPMTxubAdRo3fOqekncuA5TnNGYshktcXEXD2xL5OdcLu', '2025-12-17 21:16:05'),
('ana', '$2a$10$MjVML6jlgK5jpM/CEwepKur7F4PI9z2HXGa8bjJB5O9T6bwsyO.dy', '2025-12-17 21:44:15'),
('carlosm', '$2a$10$5U6fLNVYRTtoMwO09r3xRe96o3SGFHL8c2np6qhYXcwr8jSEfc19u', '2025-12-17 21:46:46'),
('luciar', '$2a$10$DJqPZ92fIpaZU/g.Iz7zDe83nvkjwlsX6KOMbSmBndWckUf7Rlt42', '2025-12-17 21:47:05'),
('josed', '$2a$10$jPEBI13zBvcawereCdqz2e42mECa/wrs9pxiwR7iwpmc3PKchnWQq', '2025-12-17 21:47:21'),
('pedroh', '$2a$10$eq922WEoM.ckFC4SYevFfeCjE0do8yk1EQ.ldAhvrQ4wOrZ7BZw7a', '2025-12-17 21:47:46');

-- TABLA: Persona
INSERT INTO Persona
(idCredencial, nombres, apPaterno, apMaterno, genero, tipoDocumento, numDocumento, telefono, correo, fechaNacimiento) VALUES
(1, 'CLIENTE', 'GENERICO', NULL, NULL, NULL, '00000000', NULL, NULL, NULL),
(2, 'Said Herminio', 'Huallanca', 'Gomez', 'M', 'DNI', '75922228', '940539879', 'said@example.com', '2008-08-17'),
(3, 'Ana Sofia', 'Perez', 'Gomez', 'F', 'DNI', '98765432', '998877665', 'ana.perez@dominio.com', '1995-05-14'),
(4, 'Carlos Alberto', 'Martinez', 'Lopez', 'M', 'DNI', '45678912', '987654321', 'carlos.martinez@dominio.com', '1990-03-21'),
(5, 'Lucia Fernanda', 'Ramirez', 'Torres', 'F', 'DNI', '74125896', '912345678', 'lucia.ramirez@dominio.com', '1998-11-09'),
(6, 'Jose Luis', 'Diaz', 'Castro', 'M', 'DNI', '36985214', '965874123', 'jose.diaz@dominio.com', '1987-07-04'),
(7, 'Pedro Enrique', 'Hernandez', 'Salas', 'M', 'DNI', '15975348', '976543210', 'pedro.hernandez@dominio.com', '1985-09-16');

-- TABLA: Empleado
INSERT INTO Empleado
(idPersona, direccion, estadoEmpleado, fechaRegistro, imagenEmpleado_url) VALUES
(2, 'Jiron Pampa de Nazca, D11, Santiago de Surco', 'activo', '2025-12-17 21:16:05',
 'contratos/imagen_empleado/75922228/75922228_4adf6f6c_20251217211558.jpg');

-- TABLA: Cliente
INSERT INTO Cliente
(idPersona, fechaRegistro, categoria, imagenCliente_url) VALUES
(1, NULL, 'regular', NULL),
(2, '2025-12-17 21:16:05', 'regular', 'contratos/imagen_cliente/75922228/75922228_4adf6f6c_20251217211558.jpg'),
(3, '2025-12-17 21:44:16', 'regular', 'contratos/imagen_cliente/98765432/98765432_4afbf641_20251217214414.webp'),
(4, '2025-12-17 21:46:47', 'regular', 'contratos/imagen_cliente/45678912/45678912_7e59ff49_20251217214646.jpg'),
(5, '2025-12-17 21:47:05', 'regular', 'contratos/imagen_cliente/74125896/74125896_40dcb81b_20251217214704.jpg'),
(6, '2025-12-17 21:47:21', 'regular', 'contratos/imagen_cliente/36985214/36985214_159bec65_20251217214721.jpg'),
(7, '2025-12-17 21:47:46', 'regular', 'contratos/imagen_cliente/15975348/15975348_244e7856_20251217214746.jpg');

-- TABLA: CatalogoComida
INSERT INTO CatalogoComida
(nombre, categoria, precio, stock, estadoPlato, imagenPlato_url) VALUES
('Lomo Saltado Clásico', 'Platos Criollos', 55.50, 20, 1, 'contratos/imagen_catalogo/platos_criollos/lomo_saltado_clásico_53525fc9_20251219160149.jpg'),
('Ají de Gallina', 'Platos Criollos', 48.00, 15, 1, 'contratos/imagen_catalogo/platos_criollos/ají_de_gallina_eba176db_20251219160236.jpg'),
('Arroz con Pollo', 'Platos Criollos', 45.00, 18, 1, 'contratos/imagen_catalogo/platos_criollos/arroz_con_pollo_73737aec_20251219160254.jpg'),
('Ceviche Mixto', 'Platos Marinos', 60.00, 12, 1, 'contratos/imagen_catalogo/platos_marinos/ceviche_mixto_c02fead3_20251219160402.jpg'),
('Chaufa de Pollo', 'Comida Oriental', 40.50, 25, 1, 'contratos/imagen_catalogo/comida_oriental/chaufa_de_pollo_0b8c5bab_20251219160417.jpg'),
('Pollo a la Brasa', 'Platos Criollos', 52.00, 10, 1, 'contratos/imagen_catalogo/platos_criollos/pollo_a_la_brasa_3dfd2336_20251219160451.jpg'),
('Tallarines Verdes', 'Pastas', 42.00, 16, 1, 'contratos/imagen_catalogo/pastas/tallarines_verdes_d516b659_20251219160504.jpg'),
('Seco de Res', 'Platos Criollos', 50.00, 14, 1, 'contratos/imagen_catalogo/platos_criollos/seco_de_res_cb1a720d_20251219160522.jpg'),
('Causa Limeña', 'Entradas', 30.00, 22, 1, 'contratos/imagen_catalogo/entradas/causa_limeña_914c2010_20251219160539.jpg'),
('Anticuchos', 'Parrillas', 35.00, 20, 0, 'contratos/imagen_catalogo/parrillas/anticuchos_4d242676_20251219160552.jpg'),
('Pescado Frito', 'Platos Marinos', 47.50, 13, 0, 'contratos/imagen_catalogo/platos_marinos/pescado_frito_266fc6c0_20251219160610.jpg'),
('Hamburguesa Clásica', 'Comida Rápida', 28.00, 30, 0, 'contratos/imagen_catalogo/comida_rápida/hamburguesa_clásica_287297ae_20251219160620.jpg'),
('Pizza Pepperoni', 'Comida Italiana', 38.00, 17, 1, 'contratos/imagen_catalogo/comida_italiana/pizza_pepperoni_a7aa6c8c_20251219160633.jpg'),
('Lasagna de Carne', 'Comida Italiana', 44.00, 11, 1, 'contratos/imagen_catalogo/comida_italiana/lasagna_de_carne_5a507959_20251219160652.jpeg'),
('Ensalada César', 'Ensaladas', 25.00, 26, 1, 'contratos/imagen_catalogo/ensaladas/ensalada_césar_edcc69e4_20251219160703.jpg');

-- TABLA: FormaPago
INSERT INTO FormaPago (nombre, estadoFormaPago) VALUES
('Plin', 'activo'),
('Transferencia Bancaria', 'inactivo'),
('Efectivo', 'activo'),
('Tarjeta de Crédito', 'activo'),
('Tarjeta de Débito', 'activo'),
('PayPal', 'activo'),
('Pago Contra Entrega', 'activo');
 
-- TABLA: Sucursal
INSERT INTO Sucursal (nombre, direccion, telefono, estadoSucursal) VALUES
('Chavo Villa el Salvador', 'Auxiliar Av. Mariano Pastor Sevilla, Villa EL Salvador, Lima', '2853408', 'activo'),
('Chavo Chiclayo', 'Av. La Pedro', '7894561', 'activo'),
('Chavo Santiago de Surco', 'Av. Benavides', '7894561', 'activo'),
('Chavo Miraflores', 'Av. Larco', '912345678', 'activo'),
('Chavo San Isidro', 'Av. Javier Prado Este', '987654321', 'activo'),
('Chavo Barranco', 'Jr. Unión', '934567890', 'inactivo'),
('Chavo La Molina', 'Av. La Fontana', '956789012', 'inactivo'),
('Chavo Surquillo', 'Av. Angamos', '923456789', 'inactivo'),
('Chavo San Borja', 'Av. Aviación', '945678901', 'activo'),
('Chavo Pueblo Libre', 'Av. Bolívar', '978901234', 'activo');

-- TABLA: Roles
INSERT INTO Roles (nombre, descripcion, estadoRol) VALUES
('Admin', 'Dueño del Local', 'activo'),
('Mesero', 'Se encarga de atender directamente a los clientes', 'activo'),
('Gerente', 'Responsable de la administración general y toma de decisiones del negocio', 'activo'),
('SubGerente', 'Apoya al gerente en la supervisión del personal y operaciones diarias', 'activo'),
('Cocinero', 'Encargado de la preparación de los platos según el menú establecido', 'activo'),
('Repartidor', 'Realiza la entrega de pedidos a domicilio de manera oportuna', 'activo'),
('Cajero', 'Gestiona los cobros, pagos y el manejo de caja', 'activo'),
('Anfitrión', 'Recibe a los clientes y los guía a sus mesas', 'inactivo');

-- TABLA: TipoContrato
INSERT INTO TipoContrato (nombre, descripcion, estadoTipoContrato) VALUES
('Full Time', 'Medio tiempo, 6 dias a la semana', 'activo'),
('Part Time', 'Medio tiempo, 4 días a la semana', 'activo'),
('Freelance', 'Trabajo por proyecto, horario flexible', 'activo'),
('Turno Mañana', 'Horario fijo de lunes a viernes en la mañana', 'inactivo'),
('Turno Tarde', 'Horario fijo de lunes a viernes en la tarde', 'inactivo'),
('Turno Noche', 'Horario nocturno, 5 noches a la semana', 'activo'),
('Fin de Semana', 'Trabajo sábado y domingo', 'activo'),
('Horario Flexible', 'Horas adaptables según disponibilidad', 'activo');
 
-- TABLA: Mesa
INSERT INTO Mesa
(idSucursal, numeroMesa, capacidad, ubicacion, estado) VALUES
(1, 'ONLINE', 0, 'Virtual', 'ocupada'),
(1, 'M01', 4, 'Interior', 'inactivo'),
(1, 'M02', 4, 'Terraza', 'disponible'),
(1, 'M03', 2, 'Interior', 'disponible'),
(1, 'M04', 6, 'Terraza', 'disponible'),
(1, 'M05', 8, 'Salón Principal', 'disponible'),

(2, 'M01', 4, 'Interior', 'disponible'),
(2, 'M02', 2, 'Interior', 'disponible'),
(2, 'M03', 6, 'Salón Principal', 'disponible'),
(2, 'M04', 4, 'Terraza', 'disponible'),
(2, 'M05', 8, 'Salón Principal', 'disponible'),

(3, 'M01', 2, 'Interior', 'disponible'),
(3, 'M02', 4, 'Interior', 'disponible'),
(3, 'M03', 6, 'Salón Principal', 'disponible'),
(3, 'M04', 4, 'Terraza', 'disponible'),
(3, 'M05', 8, 'Terraza', 'disponible'),

(4, 'M01', 2, 'Terraza', 'disponible'),
(4, 'M02', 4, 'Terraza', 'disponible'),
(4, 'M03', 6, 'Salón Principal', 'disponible'),
(4, 'M04', 4, 'Interior', 'disponible'),
(4, 'M05', 8, 'Salón Principal', 'disponible'),

(5, 'M01', 4, 'Interior', 'disponible'),
(5, 'M02', 6, 'Salón Principal', 'disponible'),
(5, 'M03', 2, 'Interior', 'disponible'),
(5, 'M04', 4, 'Terraza', 'disponible'),
(5, 'M05', 8, 'Salón Principal', 'disponible'),

(6, 'M01', 2, 'Terraza', 'disponible'),
(6, 'M02', 4, 'Terraza', 'disponible'),
(6, 'M03', 6, 'Interior', 'disponible'),
(6, 'M04', 4, 'Interior', 'disponible'),
(6, 'M05', 8, 'Salón Principal', 'disponible'),

(7, 'M01', 4, 'Interior', 'disponible'),
(7, 'M02', 6, 'Salón Principal', 'disponible'),
(7, 'M03', 2, 'Interior', 'disponible'),
(7, 'M04', 4, 'Terraza', 'disponible'),
(7, 'M05', 8, 'Salón Principal', 'disponible'),

(8, 'M01', 2, 'Interior', 'disponible'),
(8, 'M02', 4, 'Interior', 'disponible'),
(8, 'M03', 6, 'Salón Principal', 'disponible'),
(8, 'M04', 4, 'Terraza', 'disponible'),
(8, 'M05', 8, 'Salón Principal', 'disponible'),

(9, 'M01', 4, 'Interior', 'disponible'),
(9, 'M02', 2, 'Interior', 'disponible'),
(9, 'M03', 6, 'Salón Principal', 'disponible'),
(9, 'M04', 4, 'Terraza', 'disponible'),
(9, 'M05', 8, 'Salón Principal', 'disponible'),

(10, 'M01', 2, 'Interior', 'disponible'),
(10, 'M02', 4, 'Interior', 'disponible'),
(10, 'M03', 6, 'Salón Principal', 'disponible'),
(10, 'M04', 4, 'Terraza', 'disponible'),
(10, 'M05', 8, 'Salón Principal', 'disponible');

-- TABLA: Contrato
INSERT INTO Contrato
(idEmpleado, idSucursal, idTipoContrato, idRol, fechaInicio, fechaFin, salario, estadoContrato, pdf_generado_key, pdf_firmado_key) VALUES
(1, 1, 1, 1, '2025-12-20 03:30:00', NULL, 3500.00, 'activo', NULL,
 'contratos/firmados/75922228/75922228_304e1e52_20251217211605.pdf');
 
-- TABLA: storage_file
INSERT INTO storage_file
(bucket, object_key, filename, content_type, size, uploaded_by, related_table, related_id, created_at, estado) VALUES
('restaurant-project', 'contratos/firmados/75922228/75922228_304e1e52_20251217211605.pdf', '75922228_304e1e52_20251217211605.pdf', 'form-data', 124480, 1, 'Contrato', 1, '2025-12-17 21:16:06', 1),
('restaurant-project', 'contratos/imagen_empleado/75922228/75922228_4adf6f6c_20251217211558.jpg', '75922228_4adf6f6c_20251217211558.jpg', 'form-data', 1287139, 1, 'Empleado', 1, '2025-12-17 21:16:07', 1),
('restaurant-project', 'contratos/imagen_cliente/75922228/75922228_4adf6f6c_20251217211558.jpg', '75922228_4adf6f6c_20251217211558.jpg', 'form-data', 1287139, 1, 'Cliente', 2, '2025-12-17 21:16:07', 1),
('restaurant-project', 'contratos/imagen_cliente/98765432/98765432_4afbf641_20251217214414.webp', '98765432_4afbf641_20251217214414.webp', 'form-data', 26680, 3, 'Cliente', 3, '2025-12-17 21:44:16', 1),
('restaurant-project', 'contratos/imagen_cliente/45678912/45678912_7e59ff49_20251217214646.jpg', '45678912_7e59ff49_20251217214646.jpg', 'form-data', 4504, 4, 'Cliente', 4, '2025-12-17 21:46:47', 1),
('restaurant-project', 'contratos/imagen_cliente/74125896/74125896_40dcb81b_20251217214704.jpg', '74125896_40dcb81b_20251217214704.jpg', 'form-data', 7262, 5, 'Cliente', 5, '2025-12-17 21:47:05', 1),
('restaurant-project', 'contratos/imagen_cliente/36985214/36985214_159bec65_20251217214721.jpg', '36985214_159bec65_20251217214721.jpg', 'form-data', 13291, 6, 'Cliente', 6, '2025-12-17 21:47:21', 1),
('restaurant-project', 'contratos/imagen_cliente/15975348/15975348_244e7856_20251217214746.jpg', '15975348_244e7856_20251217214746.jpg', 'form-data', 8982, 7, 'Cliente', 7, '2025-12-17 21:47:46', 1),
('restaurant-project', 'contratos/imagen_catalogo/platos_criollos/lomo_saltado_clásico_53525fc9_20251219160149.jpg', 'lomo_saltado_clásico_53525fc9_20251219160149.jpg', 'form-data', 122590, 1, 'CatalogoComida', 1, '2025-12-19 16:02:00', 1),
('restaurant-project', 'contratos/imagen_catalogo/platos_criollos/ají_de_gallina_eba176db_20251219160236.jpg', 'ají_de_gallina_eba176db_20251219160236.jpg', 'form-data', 83561, 1, 'CatalogoComida', 2, '2025-12-19 16:02:38', 1),
('restaurant-project', 'contratos/imagen_catalogo/platos_criollos/arroz_con_pollo_73737aec_20251219160254.jpg', 'arroz_con_pollo_73737aec_20251219160254.jpg', 'form-data', 222243, 1, 'CatalogoComida', 3, '2025-12-19 16:02:56', 1);