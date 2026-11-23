/* =====================================================================
   MediHistoria - Script 01: crear la base de datos y las 7 tablas (DDL)
   Motor: Microsoft SQL Server
   Uso:   abrir en SSMS, conectarse al servidor local y ejecutar todo (F5).
   Es re-ejecutable: solo crea lo que no existe, no borra nada.
   ===================================================================== */

IF DB_ID('MediHistoria') IS NULL
    CREATE DATABASE MediHistoria;
GO

USE MediHistoria;
GO

/* ---------- Rol ---------- */
IF OBJECT_ID('dbo.Rol', 'U') IS NULL
CREATE TABLE dbo.Rol (
    idRol   INT IDENTITY(1,1) PRIMARY KEY,
    nombre  VARCHAR(20) NOT NULL UNIQUE
);
GO

/* ---------- Usuario ----------
   La contrasena NUNCA se guarda: solo el hash PBKDF2 (Base64) y su sal. */
IF OBJECT_ID('dbo.Usuario', 'U') IS NULL
CREATE TABLE dbo.Usuario (
    idUsuario         INT IDENTITY(1,1) PRIMARY KEY,
    usuario           VARCHAR(30)  NOT NULL UNIQUE,
    nombres           NVARCHAR(60) NOT NULL,
    apellidos         NVARCHAR(60) NOT NULL,
    claveHash         VARCHAR(100) NOT NULL,
    sal               VARCHAR(50)  NOT NULL,
    idRol             INT          NOT NULL,
    activo            BIT          NOT NULL DEFAULT 1,
    intentosFallidos  TINYINT      NOT NULL DEFAULT 0,
    bloqueado         BIT          NOT NULL DEFAULT 0,
    debeCambiarClave  BIT          NOT NULL DEFAULT 0,
    ultimoAcceso      DATETIME     NULL,
    CONSTRAINT CK_Usuario_Intentos CHECK (intentosFallidos >= 0),
    CONSTRAINT FK_Usuario_Rol FOREIGN KEY (idRol) REFERENCES dbo.Rol(idRol)
);
GO

/* ---------- Paciente ---------- */
IF OBJECT_ID('dbo.Paciente', 'U') IS NULL
CREATE TABLE dbo.Paciente (
    idPaciente       INT IDENTITY(1,1) PRIMARY KEY,
    dni              CHAR(8)       NOT NULL UNIQUE,
    nombres          NVARCHAR(60)  NOT NULL,
    apellidos        NVARCHAR(60)  NOT NULL,
    fechaNacimiento  DATE          NOT NULL,
    sexo             CHAR(1)       NOT NULL,
    telefono         VARCHAR(20)   NULL,
    direccion        NVARCHAR(150) NULL,
    alergias         NVARCHAR(300) NULL,
    fechaRegistro    DATETIME      NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CK_Paciente_Dni  CHECK (dni NOT LIKE '%[^0-9]%'),
    CONSTRAINT CK_Paciente_Sexo CHECK (sexo IN ('M', 'F')),
    CONSTRAINT CK_Paciente_Nacimiento CHECK (fechaNacimiento <= CAST(GETDATE() AS DATE))
);
GO

/* ---------- Cita ---------- */
IF OBJECT_ID('dbo.Cita', 'U') IS NULL
CREATE TABLE dbo.Cita (
    idCita      INT IDENTITY(1,1) PRIMARY KEY,
    idPaciente  INT           NOT NULL,
    idMedico    INT           NOT NULL,
    fechaHora   DATETIME      NOT NULL,
    motivo      NVARCHAR(200) NULL,
    estado      VARCHAR(15)   NOT NULL DEFAULT 'Programada',
    CONSTRAINT CK_Cita_Estado CHECK (estado IN ('Programada', 'Atendida', 'Cancelada', 'No asistio')),
    CONSTRAINT FK_Cita_Paciente FOREIGN KEY (idPaciente) REFERENCES dbo.Paciente(idPaciente),
    CONSTRAINT FK_Cita_Medico   FOREIGN KEY (idMedico)   REFERENCES dbo.Usuario(idUsuario)
);
GO

/* Un medico no puede tener dos citas activas a la misma hora
   (las canceladas liberan el horario). */
IF NOT EXISTS (SELECT 1 FROM sys.indexes
               WHERE name = 'UX_Cita_Medico_Horario' AND object_id = OBJECT_ID('dbo.Cita'))
    CREATE UNIQUE INDEX UX_Cita_Medico_Horario
        ON dbo.Cita (idMedico, fechaHora) WHERE estado <> 'Cancelada';
GO

/* ---------- Triaje (signos vitales) ---------- */
IF OBJECT_ID('dbo.Triaje', 'U') IS NULL
CREATE TABLE dbo.Triaje (
    idTriaje            INT IDENTITY(1,1) PRIMARY KEY,
    idCita              INT          NOT NULL UNIQUE,
    idEnfermero         INT          NOT NULL,
    pesoKg              DECIMAL(5,2) NOT NULL,
    tallaCm             SMALLINT     NOT NULL,
    presionArterial     VARCHAR(10)  NOT NULL,
    frecuenciaCardiaca  SMALLINT     NOT NULL,
    temperaturaC        DECIMAL(4,1) NOT NULL,
    fechaRegistro       DATETIME     NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CK_Triaje_Peso  CHECK (pesoKg > 0),
    CONSTRAINT CK_Triaje_Talla CHECK (tallaCm > 0),
    CONSTRAINT CK_Triaje_FC    CHECK (frecuenciaCardiaca > 0),
    CONSTRAINT CK_Triaje_Temp  CHECK (temperaturaC BETWEEN 30 AND 45),
    CONSTRAINT FK_Triaje_Cita      FOREIGN KEY (idCita)      REFERENCES dbo.Cita(idCita),
    CONSTRAINT FK_Triaje_Enfermero FOREIGN KEY (idEnfermero) REFERENCES dbo.Usuario(idUsuario)
);
GO

/* ---------- Consulta (nota SOAP) ----------
   Los 4 campos SOAP se guardaran cifrados (texto Base64) desde el Sprint 4. */
IF OBJECT_ID('dbo.Consulta', 'U') IS NULL
CREATE TABLE dbo.Consulta (
    idConsulta     INT IDENTITY(1,1) PRIMARY KEY,
    idCita         INT          NOT NULL UNIQUE,
    subjetivo      VARCHAR(MAX) NULL,
    objetivo       VARCHAR(MAX) NULL,
    analisis       VARCHAR(MAX) NULL,
    plan           VARCHAR(MAX) NULL,
    estado         VARCHAR(10)  NOT NULL DEFAULT 'Borrador',
    fechaRegistro  DATETIME     NOT NULL DEFAULT GETDATE(),
    fechaCierre    DATETIME     NULL,
    CONSTRAINT CK_Consulta_Estado CHECK (estado IN ('Borrador', 'Cerrada')),
    CONSTRAINT FK_Consulta_Cita FOREIGN KEY (idCita) REFERENCES dbo.Cita(idCita)
);
GO

/* ---------- Auditoria (solo se agregan filas; no se editan ni se borran) ---------- */
IF OBJECT_ID('dbo.Auditoria', 'U') IS NULL
CREATE TABLE dbo.Auditoria (
    idAuditoria  BIGINT IDENTITY(1,1) PRIMARY KEY,
    idUsuario    INT           NULL,
    accion       VARCHAR(40)   NOT NULL,
    entidad      VARCHAR(30)   NULL,
    idEntidad    INT           NULL,
    detalle      NVARCHAR(200) NULL,
    fechaHora    DATETIME      NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Auditoria_Usuario FOREIGN KEY (idUsuario) REFERENCES dbo.Usuario(idUsuario)
);
GO

/* ---------- Verificacion: deben aparecer exactamente 7 tablas ---------- */
SELECT name AS tabla FROM sys.tables ORDER BY name;
GO
