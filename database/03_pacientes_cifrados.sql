/* =====================================================================
   MediHistoria - Script 03 (Sprint 2): tabla Paciente con datos protegidos
   Ejecutar DESPUES de los scripts 01 y 02. Es re-ejecutable.

   Cambio: el DNI deja de guardarse en claro. Ahora se guarda:
     - dniHash    : huella HMAC-SHA256 del DNI (sirve para buscar y evitar duplicados)
     - dniCifrado : DNI cifrado con AES-256-GCM (texto Base64)
   Telefono, direccion y alergias tambien se guardan cifrados.
   Nombres y apellidos quedan en claro para poder buscar por apellido.

   Si la tabla Paciente ya tiene datos, este script NO la modifica (no hay
   que perder informacion): avisa y se detiene.
   ===================================================================== */

USE MediHistoria;
GO

/* 1. Quitar la clave foranea que apunta a Paciente (solo si hay que recrear la tabla y esta vacia) */
IF COL_LENGTH('dbo.Paciente', 'dniHash') IS NULL
   AND NOT EXISTS (SELECT 1 FROM dbo.Paciente)
   AND EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Cita_Paciente')
    ALTER TABLE dbo.Cita DROP CONSTRAINT FK_Cita_Paciente;
GO

/* 2. Eliminar la tabla Paciente del Sprint 1 (esta vacia) */
IF COL_LENGTH('dbo.Paciente', 'dniHash') IS NULL
   AND NOT EXISTS (SELECT 1 FROM dbo.Paciente)
    DROP TABLE dbo.Paciente;
GO

/* 3. Crear la tabla Paciente con el esquema del Sprint 2 */
IF OBJECT_ID('dbo.Paciente', 'U') IS NULL
CREATE TABLE dbo.Paciente (
    idPaciente          INT IDENTITY(1,1) PRIMARY KEY,
    dniHash             CHAR(64)      NOT NULL UNIQUE,
    dniCifrado          VARCHAR(200)  NOT NULL,
    nombres             NVARCHAR(60)  NOT NULL,
    apellidos           NVARCHAR(60)  NOT NULL,
    fechaNacimiento     DATE          NOT NULL,
    sexo                CHAR(1)       NOT NULL,
    telefonoCifrado     VARCHAR(200)  NULL,
    direccionCifrada    VARCHAR(800)  NULL,
    alergiasCifradas    VARCHAR(1500) NULL,
    fechaRegistro       DATETIME      NOT NULL DEFAULT GETDATE(),
    fechaActualizacion  DATETIME      NULL,
    CONSTRAINT CK_Paciente_Sexo       CHECK (sexo IN ('M', 'F')),
    CONSTRAINT CK_Paciente_Nacimiento CHECK (fechaNacimiento <= CAST(GETDATE() AS DATE))
);
GO

/* 4. Restaurar la clave foranea de Cita hacia Paciente */
IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name = 'FK_Cita_Paciente')
    ALTER TABLE dbo.Cita ADD CONSTRAINT FK_Cita_Paciente
        FOREIGN KEY (idPaciente) REFERENCES dbo.Paciente(idPaciente);
GO

/* 5. Verificacion */
IF COL_LENGTH('dbo.Paciente', 'dniHash') IS NULL
    PRINT 'ATENCION: la tabla Paciente tiene datos y conserva el esquema anterior. Consulte con el equipo antes de migrarla.';
ELSE
    PRINT 'Tabla Paciente lista (esquema del Sprint 2).';
GO

SELECT c.name AS columna, t.name AS tipo, c.is_nullable
FROM sys.columns c JOIN sys.types t ON t.user_type_id = c.user_type_id
WHERE c.object_id = OBJECT_ID('dbo.Paciente')
ORDER BY c.column_id;
GO
