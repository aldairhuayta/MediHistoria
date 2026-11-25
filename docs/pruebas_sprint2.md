# Casos de prueba — Sprint 2 (pacientes)

Requisitos: scripts 01, 02 y 03 ejecutados. Probar con `recep.quispe`, `dra.ramos`, `enf.torres` y `admin` (ya con su contraseña cambiada).

| ID | Procedimiento | Resultado esperado | Resultado obtenido | ¿Pasó? |
|---|---|---|---|---|
| CP-15 | Recepcionista: Nuevo paciente con todos los datos válidos y Guardar | "Paciente guardado correctamente." y aparece en la tabla con el DNI parcialmente oculto (`******67`) | Mostró "Paciente guardado correctamente." y el paciente apareció en la tabla con el DNI parcialmente oculto (`******67`) | Sí |
| CP-16 | Recepcionista: registrar otro paciente con el mismo DNI | "Ya existe un paciente con ese DNI." | Mostró "Ya existe un paciente con ese DNI." | Sí |
| CP-17 | Probar DNI de 7 dígitos, con letras y vacío | "El DNI debe tener exactamente 8 dígitos." | En los tres casos mostró "El DNI debe tener exactamente 8 dígitos." | Sí |
| CP-18 | Probar apellidos vacíos, fecha futura, fecha inexistente (31/02/2000) y sin sexo | Cada caso muestra su mensaje y no guarda | Cada caso mostró su mensaje de validación y no se guardó el paciente | Sí |
| CP-19 | Teléfono con letras, o dirección de más de 150 caracteres | Mensaje de validación y no guarda | Mostró el mensaje de validación correspondiente y no guardó | Sí |
| CP-20 | Buscar por el DNI completo de un paciente | Aparece 1 paciente | Apareció 1 paciente | Sí |
| CP-21 | Buscar por parte de un apellido (ej. "qui") y por un nombre | Aparecen los coincidentes | Aparecieron los pacientes coincidentes en ambas búsquedas | Sí |
| CP-22 | Buscar el texto `%` y luego `_` | No devuelve todos los pacientes (se tratan como texto normal) | No devolvió todos los pacientes; `%` y `_` se trataron como texto normal | Sí |
| CP-23 | Seleccionar un paciente: el DNI aparece completo y no se puede editar. Cambiar teléfono, dirección y alergias, Guardar, volver a seleccionarlo | Los datos nuevos se conservan | El DNI apareció completo y no editable; los datos nuevos se conservaron al volver a seleccionarlo | Sí |
| CP-24 | Ingresar como Médico | Puede buscar y editar, pero el botón "Nuevo paciente" está deshabilitado | Pudo buscar y editar; el botón "Nuevo paciente" estaba deshabilitado | Sí |
| CP-25 | Ingresar como Enfermero | Solo puede ver: campos de solo lectura y "Guardar" deshabilitado | Campos en solo lectura y botón "Guardar" deshabilitado | Sí |
| CP-26 | Ingresar como Administrador | El menú **no** muestra Pacientes | El menú no mostró la opción Pacientes | Sí |
| CP-27 | En SSMS: `SELECT dniCifrado, telefonoCifrado, direccionCifrada, alergiasCifradas, nombres FROM Paciente;` | Los 4 primeros campos son texto ilegible (Base64); los nombres están en claro | Los 4 campos cifrados se vieron como texto ilegible (Base64); los nombres en claro | Sí |
| CP-28 | En SSMS: `SELECT TOP 10 accion, entidad, idEntidad, detalle FROM Auditoria ORDER BY idAuditoria DESC;` | Aparecen `PACIENTE_CREADO`, `PACIENTE_CONSULTADO`, `PACIENTE_ACTUALIZADO` **sin** datos personales en `detalle` | Aparecieron `PACIENTE_CREADO`, `PACIENTE_CONSULTADO` y `PACIENTE_ACTUALIZADO` sin datos personales en `detalle` | Sí |
| CP-29 | Cerrar la aplicación y **renombrar** `config/seguridad.properties` a `seguridad_copia.properties`; abrir la aplicación y entrar a Pacientes | La aplicación genera un archivo de claves nuevo; al listar muestra un mensaje genérico de error (no puede leer los datos antiguos), el detalle va a la consola y la aplicación no se cierra | Se generó un archivo de claves nuevo; al listar se mostró un mensaje genérico de error, el detalle fue a la consola y la aplicación no se cerró | Sí |
| CP-30 | Cerrar la aplicación, borrar el archivo generado y volver a llamar `seguridad_copia.properties` como `seguridad.properties`; abrir Pacientes | Los pacientes vuelven a leerse correctamente | Los pacientes volvieron a leerse correctamente | Sí |
