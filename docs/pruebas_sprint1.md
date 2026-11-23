# Casos de prueba — Sprint 1 (acceso seguro)

Ejecutar con la base de datos recién creada (scripts 01 y 02). Completar las dos últimas columnas.

| ID | Procedimiento | Resultado esperado | Resultado obtenido | ¿Pasó? |
|---|---|---|---|---|
| CP-01 | Ingresar con `admin` / `Admin2026!` | Pide cambiar la contraseña; luego entra a la pantalla principal | | |
| CP-02 | Ingresar con usuario correcto y contraseña incorrecta | Mensaje genérico "Usuario o contraseña incorrectos…" | | |
| CP-03 | Ingresar con un usuario que no existe | **El mismo** mensaje genérico del CP-02 | | |
| CP-04 | Dejar usuario o contraseña vacíos | "Ingrese usuario y contraseña." | | |
| CP-05 | Fallar 5 veces seguidas con un usuario existente; luego usar la contraseña correcta | Sigue rechazando con el mensaje genérico (cuenta bloqueada) | | |
| CP-06 | Ingresar con cada rol y revisar el menú | Administrador: Usuarios, Auditoría, Reportes. Médico: Pacientes, Citas, Triaje, Consulta, Reportes. Enfermero: Pacientes, Citas, Triaje, Consulta. Recepcionista: Pacientes, Citas | | |
| CP-07 | Como admin, crear un usuario nuevo | Muestra una contraseña temporal una sola vez; el usuario aparece en la tabla | | |
| CP-08 | Crear un usuario con un nombre que ya existe | "Ya existe un usuario con ese nombre." | | |
| CP-09 | Desactivar un usuario y tratar de ingresar con él | No puede ingresar (mensaje genérico) | | |
| CP-10 | Como admin, intentar desactivarse a sí mismo | Lo impide con un mensaje | | |
| CP-11 | En Cambiar contraseña: probar una contraseña débil y una actual incorrecta | Rechaza ambas con su motivo | | |
| CP-12 | Detener el servicio de SQL Server e intentar ingresar | "No se pudo conectar a la base de datos." en pantalla; el detalle del error solo en la consola | | |
| CP-13 | Ejecutar `SELECT * FROM Auditoria ORDER BY idAuditoria DESC;` | Aparecen los eventos de las pruebas anteriores (LOGIN_OK, LOGIN_FALLIDO, USUARIO_CREADO, …) | | |
| CP-14 | Restablecer la contraseña de un usuario bloqueado (CP-05) | Se desbloquea, recibe contraseña temporal y debe cambiarla al ingresar | | |
