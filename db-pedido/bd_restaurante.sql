-- Crear Base de Datos
CREATE DATABASE db_restaurant;
USE db_restaurant;

-- Tabla: Credenciales
CREATE TABLE Credenciales (
	idCredencial INT AUTO_INCREMENT PRIMARY KEY,
	usuario VARCHAR(200) NOT NULL UNIQUE,
	contrasena VARCHAR(255) NOT NULL,
    fechaCreacion DATETIME
);

-- Tabla: Persona
CREATE TABLE Persona (
	idPersona INT AUTO_INCREMENT PRIMARY KEY,
    idCredencial INT NOT NULL,
	nombres VARCHAR(200) NOT NULL,
	apPaterno VARCHAR(150) NOT NULL,
	apMaterno VARCHAR(150),
	genero char(1),
	tipoDocumento VARCHAR(100),
	numDocumento VARCHAR(15) NOT NULL,
	telefono VARCHAR(15),
	correo VARCHAR(150),
	fechaNacimiento DATE,
	FOREIGN KEY (idCredencial) REFERENCES Credenciales(idCredencial)
);

-- Tabla: Cliente (Datos personales del cliente)
CREATE TABLE Cliente (
	idCliente INT AUTO_INCREMENT PRIMARY KEY,
	idPersona INT,
	fechaRegistro DATETIME,
	categoria VARCHAR(50) DEFAULT 'regular',
	imagenCliente_url VARCHAR(500),
	FOREIGN KEY (idPersona) REFERENCES Persona(idPersona)
);

-- Tabla: DireccionCliente
CREATE TABLE DireccionCliente (
	idDireccion INT AUTO_INCREMENT PRIMARY KEY,
	idCliente INT,
	departamento VARCHAR(100),
	provincia VARCHAR(100),
	distrito VARCHAR(100),
	direccion VARCHAR(255) NOT NULL,
	referencia VARCHAR(255),
	FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

-- Tabla: Sucursal
CREATE TABLE Sucursal (
	idSucursal INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(200),
	direccion VARCHAR(100),
	telefono VARCHAR(15),
	estadoSucursal VARCHAR(50) DEFAULT 'activo'
);

-- Tabla: Mesa (Datos personales las mesas "M01",'disponible', 'ocupada', 'reservada', 'mantenimiento')
CREATE TABLE Mesa (
	idMesa INT AUTO_INCREMENT PRIMARY KEY,
	idSucursal INT,
	numeroMesa VARCHAR(10),
	capacidad INT,
    ubicacion VARCHAR(50),
	estado VARCHAR(500) DEFAULT 'disponible',
	FOREIGN KEY (idSucursal) REFERENCES Sucursal(idSucursal)
);

-- Tabla: Empleado (Datos personales del Empleado)
CREATE TABLE Empleado (
	idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
	idPersona INT,
	direccion VARCHAR(255),
	estadoEmpleado VARCHAR(50) DEFAULT 'activo',
    fechaRegistro DATETIME,
	imagenEmpleado_url VARCHAR(500),
	FOREIGN KEY (idPersona) REFERENCES Persona(idPersona)
);

-- Tabla: TipoContrato (Tipos de Contratos como Part-Time, Practicante, Indefinido)
CREATE TABLE TipoContrato (
	idTipoContrato INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100),
	descripcion VARCHAR(255)
);

-- Tabla: Roles (Tipos de Contratos como Mesero, Cocinero, Repartidor)
CREATE TABLE Roles (
	idRol INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(80),
	descripcion VARCHAR(255)
);

-- Tabla: Contrato (Datos personales del cliente)
CREATE TABLE Contrato (
	idContrato INT AUTO_INCREMENT PRIMARY KEY,
	idEmpleado INT,
    idSucursal INT,
	idTipoContrato INT,
	idRol INT,
	fechaInicio DATETIME,
	fechaFin DATETIME,
    salario DECIMAL(10,2),
    estadoContrato VARCHAR(100) DEFAULT 'activo',
	FOREIGN KEY (idEmpleado) REFERENCES Empleado(idEmpleado),
	FOREIGN KEY (idSucursal) REFERENCES Sucursal(idSucursal),
	FOREIGN KEY (idTipoContrato) REFERENCES TipoContrato(idTipoContrato),
	FOREIGN KEY (idRol) REFERENCES Roles(idRol)
);

-- Tabla: Proveedor (Tipo Producto, Servicio)
CREATE TABLE Proveedor (
	idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    idPersona INT,
	razonSocial VARCHAR(150) NOT NULL,
	ruc VARCHAR(20) NOT NULL,
	direccion VARCHAR(255),
    tipoProveedor VARCHAR(100),
	estado VARCHAR(100) DEFAULT 'activo',
	fechaRegistro DATETIME,
	FOREIGN KEY (idPersona) REFERENCES Persona(idPersona)
);

-- Tabla: CatalogoComida
CREATE TABLE CatalogoComida (
	idCatalogo INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(300) NOT NULL,
	categoria VARCHAR(100) NOT NULL,
	precio DECIMAL(10,2) NOT NULL,
	stock INT NOT NULL,
    estadoPlato BOOLEAN DEFAULT TRUE,
	imagenPlato_url VARCHAR(500)
);

-- Tabla: FormaPago
CREATE TABLE FormaPago (
	idFormaPago INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL
);

-- Tabla: FormaPago
CREATE TABLE ClienteFormaPago (
	idClienteFormaPago INT AUTO_INCREMENT PRIMARY KEY,
	idCliente INT,
    idFormaPago INT,
	titular VARCHAR(100) NOT NULL,
    numeroCuenta VARCHAR(100),
    fechaVencimiento DATE,
    codigoReferencia VARCHAR(50),
    esPredeterminado BOOLEAN,
    fechaRegistro DATETIME,
	FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
	FOREIGN KEY (idFormaPago) REFERENCES FormaPago(idFormaPago)
);

-- Tabla: Pedido (cabecera de pedido)
CREATE TABLE Pedido (
	idPedido INT AUTO_INCREMENT PRIMARY KEY,
	idCliente INT,
	idDireccion INT,
    idEmpleado INT,
    idMesa INT, 
	idFormaPago INT NOT NULL,
	fecha DATE NOT NULL,
	hora TIME NOT NULL,
	estado VARCHAR(50) DEFAULT 'pendiente',
	FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
	FOREIGN KEY (idDireccion) REFERENCES DireccionCliente(idDireccion),
	FOREIGN KEY (idEmpleado) REFERENCES Empleado(idEmpleado),
	FOREIGN KEY (idMesa) REFERENCES Mesa(idMesa),
	FOREIGN KEY (idFormaPago) REFERENCES FormaPago(idFormaPago)
);

-- Tabla: DetalleDelivery (detalle del pedido)
CREATE TABLE DetallePedido (
	idDetalle INT AUTO_INCREMENT PRIMARY KEY,
	idPedido INT NOT NULL,
	idCatalogo INT NOT NULL,
    idEmpleado INT,
	cantidad INT NOT NULL,
	precioUnitario DECIMAL(10,2) NOT NULL,
	igv DECIMAL(10,2) NOT NULL,
	subtotal DECIMAL(10,2) NOT NULL,
	FOREIGN KEY (idPedido) REFERENCES Pedido(idPedido),
	FOREIGN KEY (idCatalogo) REFERENCES CatalogoComida(idCatalogo),
	FOREIGN KEY (idEmpleado) REFERENCES Empleado(idEmpleado)
);