# Casos de prueba — Sprint 1 (acceso seguro)

Ejecutar con la base de datos recién creada (scripts 01 y 02). Completar las dos últimas columnas.

| ID | Procedimiento | Resultado esperado | Resultado obtenido | ¿Pasó? |
|---|---|---|---|---|
| CP-01 | Ingresar con `admin` / `Admin2026!` | Pide cambiar la contraseña; luego entra a la pantalla principal | Solicitó cambio de contraseña; tras cambiarla ingresó a la pantalla principal | Sí |
| CP-02 | Ingresar con usuario correcto y contraseña incorrecta | Mensaje genérico "Usuario o contraseña incorrectos…" | Mostró el mensaje genérico "Usuario o contraseña incorrectos…" | Sí |
| CP-03 | Ingresar con un usuario que no existe | **El mismo** mensaje genérico del CP-02 | Mostró el mismo mensaje genérico que en CP-02 | Sí |
| CP-04 | Dejar usuario o contraseña vacíos | "Ingrese usuario y contraseña." | Mostró "Ingrese usuario y contraseña." | Sí |
| CP-05 | Fallar 5 veces seguidas con un usuario existente; luego usar la contraseña correcta | Sigue rechazando con el mensaje genérico (cuenta bloqueada) | Tras 5 intentos fallidos la cuenta quedó bloqueada; con la contraseña correcta siguió mostrando el mensaje genérico | Sí |
| CP-06 | Ingresar con cada rol y revisar el menú | Administrador: Usuarios, Auditoría, Reportes. Médico: Pacientes, Citas, Triaje, Consulta, Reportes. Enfermero: Pacientes, Citas, Triaje, Consulta. Recepcionista: Pacientes, Citas | Cada rol mostró exactamente las opciones de menú esperadas | Sí |
| CP-07 | Como admin, crear un usuario nuevo | Muestra una contraseña temporal una sola vez; el usuario aparece en la tabla | Se mostró la contraseña temporal una sola vez y el usuario apareció en la tabla | Sí |
| CP-08 | Crear un usuario con un nombre que ya existe | "Ya existe un usuario con ese nombre." | Mostró "Ya existe un usuario con ese nombre." | Sí |
| CP-09 | Desactivar un usuario y tratar de ingresar con él | No puede ingresar (mensaje genérico) | El usuario desactivado no pudo ingresar; se mostró el mensaje genérico | Sí |
| CP-10 | Como admin, intentar desactivarse a sí mismo | Lo impide con un mensaje | El sistema impidió la acción y mostró un mensaje | Sí |
| CP-11 | En Cambiar contraseña: probar una contraseña débil y una actual incorrecta | Rechaza ambas con su motivo | Rechazó la contraseña débil y la actual incorrecta, cada una con su motivo | Sí |
| CP-12 | Detener el servicio de SQL Server e intentar ingresar | "No se pudo conectar a la base de datos." en pantalla; el detalle del error solo en la consola | En pantalla: "No se pudo conectar a la base de datos."; el detalle del error solo en la consola | Sí |
| CP-13 | Ejecutar `SELECT * FROM Auditoria ORDER BY idAuditoria DESC;` | Aparecen los eventos de las pruebas anteriores (LOGIN_OK, LOGIN_FALLIDO, USUARIO_CREADO, …) | Se listaron los eventos de las pruebas anteriores (LOGIN_OK, LOGIN_FALLIDO, USUARIO_CREADO, …) | Sí |
| CP-14 | Restablecer la contraseña de un usuario bloqueado (CP-05) | Se desbloquea, recibe contraseña temporal y debe cambiarla al ingresar | La cuenta se desbloqueó, recibió contraseña temporal y se pidió cambiarla al ingresar | Sí |
