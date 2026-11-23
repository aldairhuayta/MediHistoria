/* =====================================================================
   MediHistoria - Script 02: datos iniciales (roles y usuarios de prueba)
   Ejecutar DESPUES del script 01. Es re-ejecutable.

   Los usuarios de prueba tienen una contrasena inicial que el sistema
   obliga a cambiar en el primer ingreso (ver README). Solo guardan el hash.
   ===================================================================== */

USE MediHistoria;
GO
SET NOCOUNT ON;

IF NOT EXISTS (SELECT 1 FROM dbo.Rol WHERE nombre = 'Administrador')  INSERT INTO dbo.Rol (nombre) VALUES ('Administrador');
IF NOT EXISTS (SELECT 1 FROM dbo.Rol WHERE nombre = 'Medico')         INSERT INTO dbo.Rol (nombre) VALUES ('Medico');
IF NOT EXISTS (SELECT 1 FROM dbo.Rol WHERE nombre = 'Enfermero')      INSERT INTO dbo.Rol (nombre) VALUES ('Enfermero');
IF NOT EXISTS (SELECT 1 FROM dbo.Rol WHERE nombre = 'Recepcionista')  INSERT INTO dbo.Rol (nombre) VALUES ('Recepcionista');
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE usuario = 'admin')
    INSERT INTO dbo.Usuario (usuario, nombres, apellidos, claveHash, sal, idRol, activo, debeCambiarClave)
    VALUES ('admin', N'Administrador', N'del Sistema', 'rCt8Xztgo2Yf8ejZu33cT2jyOkuv0fhyOQb4iIdjePU=', '6Ajs5ddZO6wdVdG4gQbNZg==',
            (SELECT idRol FROM dbo.Rol WHERE nombre = 'Administrador'), 1, 1);

IF NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE usuario = 'dra.ramos')
    INSERT INTO dbo.Usuario (usuario, nombres, apellidos, claveHash, sal, idRol, activo, debeCambiarClave)
    VALUES ('dra.ramos', N'Patricia', N'Ramos Salas', 'nWuLKAfZk0HFzWfgFg82qIBjFUwGwJTezQqBenh6F4c=', 'b+5aE97qll2cDbifSwmQmQ==',
            (SELECT idRol FROM dbo.Rol WHERE nombre = 'Medico'), 1, 1);

IF NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE usuario = 'enf.torres')
    INSERT INTO dbo.Usuario (usuario, nombres, apellidos, claveHash, sal, idRol, activo, debeCambiarClave)
    VALUES ('enf.torres', N'Luis', N'Torres Vega', '2f0cU7m8VvE8blLG5wnVEUMMob+lFux8tWGSHfE3Frs=', 'ax4lrjgn19fNr2T7lkO8+g==',
            (SELECT idRol FROM dbo.Rol WHERE nombre = 'Enfermero'), 1, 1);

IF NOT EXISTS (SELECT 1 FROM dbo.Usuario WHERE usuario = 'recep.quispe')
    INSERT INTO dbo.Usuario (usuario, nombres, apellidos, claveHash, sal, idRol, activo, debeCambiarClave)
    VALUES ('recep.quispe', N'Ana', N'Quispe Rojas', 'l4vq+0+tiLSDrBCuaKH7qcK3lL1p+zTkLazD098XxrA=', 'vEnii+EKYlGiOm6BIXgbTw==',
            (SELECT idRol FROM dbo.Rol WHERE nombre = 'Recepcionista'), 1, 1);
GO

SELECT u.usuario, r.nombre AS rol, u.activo, u.debeCambiarClave
FROM dbo.Usuario u JOIN dbo.Rol r ON r.idRol = u.idRol
ORDER BY u.idUsuario;
GO
