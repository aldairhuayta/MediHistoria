# Kit para Linear — MediHistoria (Grupo 1)

Sigue la Guía 06A. Todo lo que está en bloques de código se puede copiar y pegar tal cual.
Quién lo hace: **Huayta** (Product Owner) crea el espacio y lo configura; los demás aceptan la invitación y participan en la estimación y en las tareas.

---

## 0. Datos que usarán (decididos)

| Dato | Valor |
|---|---|
| Espacio de trabajo (workspace) | `CIB02 – Grupo 1` |
| Equipo (team) | `MediHistoria` |
| Identificador del equipo | `MHI` (aparecerá en ramas, commits y pull requests: ej. MHI-12) |
| Proyecto | `MediHistoria – Proyecto CIB02` |
| Responsable del proyecto (Lead) | Aldair Dealessandro Huayta Garavito (Product Owner) |
| Invitados | Saúl, Kevin y Yon **+ el docente** (pídanle su correo) |
| Fecha meta del proyecto | La fecha de la entrega final (Semana 15), la que confirme el profesor |

---

## FASE 1 · Iniciación (Pasos 1 a 4)

### Pasos 1 a 3 (resumen)
1. linear.app → "Sign up" (correo institucional, el mismo de GitHub) → crear el workspace `CIB02 – Grupo 1`.
2. Menú del workspace → "Settings" → "Members" → "Invite": los 3 compañeros y el docente como "Member".
3. "Settings" → "Teams": nombre `MediHistoria`, identificador `MHI`. Confirmar que los 4 integrantes están en el equipo.

### Paso 4 · Proyecto y roles

"Projects" → crear proyecto `MediHistoria – Proyecto CIB02`, Lead = Huayta, miembros = los 4, Target date = fecha de la Semana 15.

Hitos (Milestones):
- `Avance parcial – Semana 10` (marcarlo como alcanzado: se presentó el 02/11/2026)
- `Entrega final – Semana 15`

Descripción del proyecto (pegar):

```
Objetivo del producto:
Que los consultorios médicos independientes y las clínicas pequeñas de Lima registren
pacientes, programen citas y documenten la evolución clínica (notas SOAP) en un solo
sistema de escritorio, con acceso diferenciado por rol y datos sensibles protegidos.

Roles Scrum
| Rol                  | Integrante                                              |
|----------------------|---------------------------------------------------------|
| Product Owner        | Aldair Dealessandro Huayta Garavito                     |
| Scrum Master         | Saúl Coro Canchari                                      |
| Equipo de desarrollo | Kevin Ricardo Ojeda Campomanes, Yon Antoni Vega Huaraca |
```

> **Evidencia E1:** captura del proyecto con el responsable, la fecha meta, los dos hitos y la descripción con la tabla de roles.

---

## FASE 2 · Planificación y estimación (Pasos 5 a 13)

### Paso 5 · Estimación
"Settings" → "Teams" → MediHistoria → "Estimates": activar, escala **Fibonacci** (1, 2, 3, 5, 8). Desactivar la opción que cuenta las incidencias sin estimar como 1 punto.

### Paso 6 · Estados (Workflow)
Dejar: `Backlog`, `Todo`, `In Progress`, `In Review` (agregarlo si no existe), `Done`, `Canceled`.

### Paso 7 · Etiquetas (Labels)
- Tipo: `historia`, `tarea-técnica`, `bug`, `documentación`
- Módulo: `usuarios`, `pacientes`, `citas`, `triaje`, `consulta`, `auditoría`, `seguridad`

### Paso 8 · Plantilla "Historia de usuario"
"Templates" → nueva plantilla de issue `Historia de usuario`, con la etiqueta `historia`. Pegar en la descripción:

```
Como <rol>
quiero <capacidad>
para <beneficio>.

Criterios de aceptación
- [ ] ...
- [ ] ...

Definición de terminado (DoD)
- [ ] El código compila y se ejecuta con JDK 21 sin errores
- [ ] Respeta MVC y DAO: ninguna sentencia SQL fuera del DAO y todas con PreparedStatement
- [ ] Se probaron los criterios de aceptación (caso normal y al menos un caso de error) y se registraron
- [ ] Las entradas se validan y los errores muestran mensaje genérico (detalle solo en consola)
- [ ] Las restricciones de integridad (PK, FK, UNIQUE, CHECK) están en el script .sql actualizado
- [ ] El acceso por rol está comprobado (un rol no autorizado no ve ni ejecuta la función)
- [ ] Revisado por otro integrante mediante pull request y fusionado en main
- [ ] La documentación y los diagramas de la fase están actualizados
```

> **Evidencia E2:** captura de la configuración del equipo con Fibonacci activo, el flujo de estados y la plantilla con la DoD.

### Paso 9 · Product backlog (11 historias)
Crear cada una con la tecla **C**, plantilla `Historia de usuario`, proyecto `MediHistoria – Proyecto CIB02`, estado `Backlog`.
**No pongan la estimación todavía**: se decide en el planning poker (Paso 11). La prioridad la fija Huayta; la dejo como propuesta.

---
**HU-01 Iniciar sesión según el rol** · etiquetas: `historia`, `seguridad` · prioridad: High · Sprint 1
```
Como usuario del consultorio
quiero iniciar sesión con mi usuario y contraseña
para acceder solo a las funciones de mi rol.

Criterios de aceptación
- [ ] Con credenciales correctas entra a la pantalla principal y ve solo el menú de su rol
- [ ] Con usuario o contraseña incorrectos muestra un mensaje genérico, sin indicar cuál falló
- [ ] Tras 5 intentos fallidos consecutivos la cuenta queda bloqueada
- [ ] La contraseña se guarda con hash y sal, nunca en texto plano
- [ ] Con una contraseña temporal, el sistema obliga a cambiarla en el primer ingreso
- [ ] Si SQL Server no responde, muestra un mensaje genérico y el detalle va a la consola
```
**HU-02 Gestionar usuarios** · `historia`, `usuarios` · High · Sprint 1
```
Como administrador
quiero crear y desactivar usuarios y asignarles un rol
para controlar quién accede al sistema.

Criterios de aceptación
- [ ] Al crear un usuario se muestra una contraseña temporal una sola vez
- [ ] No permite dos usuarios con el mismo nombre
- [ ] Un usuario desactivado no puede iniciar sesión
- [ ] No se puede desactivar a sí mismo ni al último administrador activo
- [ ] Puede restablecer la contraseña y desbloquear cuentas
- [ ] Cada acción queda registrada en la auditoría
```
**HU-03 Registrar y buscar pacientes** · `historia`, `pacientes` · High · Sprint 2
```
Como recepcionista
quiero registrar pacientes y buscarlos por DNI o apellidos
para evitar duplicados y encontrar su ficha rápidamente.

Criterios de aceptación
- [ ] Exige DNI de 8 dígitos, nombres, apellidos, fecha de nacimiento y sexo
- [ ] Rechaza un DNI ya registrado
- [ ] La búsqueda por DNI o apellidos devuelve los pacientes coincidentes
- [ ] Médico y enfermero pueden consultar la ficha; el administrador no
- [ ] Los datos sensibles se guardan protegidos en la base de datos
```
**HU-04 Actualizar datos del paciente** · `historia`, `pacientes` · Medium · Sprint 2
```
Como recepcionista
quiero actualizar los datos de contacto de un paciente
para mantener su ficha vigente.

Criterios de aceptación
- [ ] Permite editar teléfono, dirección y alergias
- [ ] El DNI no se puede modificar
- [ ] Valida los formatos antes de guardar
- [ ] El cambio queda registrado en la auditoría
```
**HU-05 Programar, reprogramar y cancelar citas** · `historia`, `citas` · High · Sprint 3
```
Como recepcionista
quiero programar, reprogramar y cancelar citas
para ordenar la agenda sin cruces de horario.

Criterios de aceptación
- [ ] Una cita requiere paciente, médico, fecha y hora futuras
- [ ] No permite dos citas activas del mismo médico a la misma hora
- [ ] Cancelar una cita libera el horario
- [ ] Reprogramar conserva el registro de la cita original
```
**HU-06 Registrar triaje** · `historia`, `triaje` · High · Sprint 3
```
Como enfermero
quiero registrar los signos vitales antes de la consulta
para que el médico los tenga disponibles.

Criterios de aceptación
- [ ] Solo el rol Enfermero puede registrar el triaje
- [ ] Hay un único triaje por cita
- [ ] Rechaza valores fuera de rango (peso mayor que 0, temperatura entre 30 y 45 °C)
- [ ] El médico puede ver el triaje de sus citas
```
**HU-07 Ver agenda y abrir la ficha del paciente** · `historia`, `citas` · High · Sprint 3
```
Como médico
quiero ver mi agenda del día y abrir la ficha del paciente
para atender la consulta.

Criterios de aceptación
- [ ] Muestra solo las citas del médico que inició sesión
- [ ] Permite abrir la ficha del paciente desde la cita
- [ ] Cada apertura de una ficha queda registrada en la auditoría
```
**HU-08 Registrar la nota SOAP** · `historia`, `consulta` · High · Sprint 4
```
Como médico
quiero registrar la evolución en una nota SOAP y cerrarla
para dejar constancia de la atención.

Criterios de aceptación
- [ ] Permite guardar la nota como borrador y editarla mientras no esté cerrada
- [ ] Al cerrarla ya no se puede editar
- [ ] Solo se puede crear sobre una cita existente y no cancelada
- [ ] El contenido se guarda protegido en la base de datos
- [ ] El cierre se hace en una transacción (si falla un paso, no queda nada a medias)
```
**HU-09 Consultar el historial de evoluciones** · `historia`, `consulta` · High · Sprint 4
```
Como médico
quiero consultar el historial de evoluciones de un paciente
para decidir el plan de tratamiento.

Criterios de aceptación
- [ ] Muestra las notas anteriores ordenadas por fecha
- [ ] El enfermero solo ve el apartado Plan
- [ ] La recepcionista no tiene acceso
```
**HU-10 Revisar la auditoría** · `historia`, `auditoría` · Medium · Sprint 4
```
Como administrador
quiero revisar el registro de auditoría
para detectar accesos indebidos.

Criterios de aceptación
- [ ] Lista los eventos con usuario, acción, fecha y hora
- [ ] Permite filtrar por usuario, acción y fecha
- [ ] Es de solo lectura
```
**HU-11 Reporte de atenciones** · `historia` · Low · opcional (si alcanza el tiempo)
```
Como administrador
quiero un reporte de atenciones por médico y período
para supervisar la actividad del consultorio.

Criterios de aceptación
- [ ] Filtra por médico y rango de fechas
- [ ] Muestra solo conteos, sin contenido clínico
```
---

El Product Owner **ordena la lista de mayor a menor valor** (arrastrando).

### Paso 10 · Tareas (sub-issues) de las historias del Sprint 1
Etiqueta `tarea-técnica`. Las tareas **no llevan puntos**. Cada integrante se asigna las suyas (el equipo se autoorganiza; esta tabla es solo una propuesta de reparto).

| # | Tarea (título) | Historia | Propuesta |
|---|---|---|---|
| T0 | Estructura base del repositorio, README y modelo (Rol, Modulo, Usuario) | — (Sprint 1) | Huayta |
| T1 | Script SQL: 7 tablas con restricciones y datos iniciales | HU-01 | Coro |
| T2 | Capa de datos: ConexionBD y DAO de usuarios y auditoría | HU-01 | Vega |
| T3 | Seguridad: hash con sal, autenticación con bloqueo, sesión y auditor | HU-01 | Vega |
| T4 | Arranque de la aplicación, navegación entre pantallas y estilos | HU-01 | Ojeda |
| T5 | Cambio de contraseña obligatorio en el primer ingreso | HU-01 | Huayta |
| T6 | Pantalla de Login con mensaje genérico y manejo de errores | HU-01 | Ojeda |
| T7 | Pantalla principal con menú según el rol | HU-01 | Ojeda |
| T8 | Pantalla de gestión de usuarios (crear, editar, restablecer, desbloquear) | HU-02 | Coro |
| T9 | Pruebas del Sprint 1 (casos CP-01 a CP-14) | HU-01 y HU-02 | Coro |

### Paso 11 · Estimar en equipo (planning poker)
1. Reúnanse los cuatro. Huayta lee cada historia y sus criterios y responde dudas.
2. Cada uno elige **en silencio** un valor (1, 2, 3, 5, 8) y todos lo muestran a la vez.
3. Si difieren mucho, el que dio el mayor y el que dio el menor explican y se vuelve a votar.
4. Se anota el valor acordado en "Estimate".

Los cuatro votan como **desarrolladores** (Huayta también, aunque sea Product Owner). La estimación la tienen que hacer ustedes: si la hiciera yo, el profesor lo notaría y la guía lo evalúa expresamente.

> **Evidencia E3:** captura del backlog completo con código HU, estimación, prioridad, etiquetas y orden.

### Paso 12 · Ciclos (sprints)
"Cycles": activar. Duración **1 semana**. Cooldown **desactivado**. Ciclos futuros visibles: 2 o 3. Desactivar el "auto-add" (que nada entre al sprint sin que el equipo lo comprometa).

### Paso 13 · Planificar el Sprint 1
Nombre del ciclo (pegar):
```
Sprint 1 – El personal ingresa de forma segura y el administrador controla el acceso
```
Descripción del ciclo (pegar):
```
Objetivo: Que el personal del consultorio pueda iniciar sesión de forma segura y que el
administrador controle quién accede al sistema y con qué rol.

Se verifica en la revisión del sprint si: (1) cada rol ve solo su menú, (2) el login no
revela si falló el usuario o la contraseña, (3) la cuenta se bloquea tras 5 intentos, y
(4) el administrador puede crear, desactivar y desbloquear usuarios.
```
Comprometer **HU-01 y HU-02**, pasarlas de `Backlog` a `Todo`, y que cada uno se asigne sus tareas.

> **Evidencia E4:** captura del Sprint 1 con el objetivo, las historias comprometidas, sus estimaciones y sus responsables.

**Objetivos propuestos para los siguientes sprints** (pegar al planificar cada uno):
```
Sprint 2 – La recepción registra y encuentra pacientes sin duplicados, con sus datos sensibles protegidos
Sprint 3 – La recepción programa citas sin cruces y el enfermero registra los signos vitales antes de la consulta
Sprint 4 – El médico registra y cierra la nota SOAP de una atención y el administrador audita los accesos
```

---

## FASE 3 · Implementación (Pasos 14 a 16)

### Paso 14 · Conectar GitHub
"Settings" → "Integrations" → GitHub: autorizar y elegir el repositorio. En el Workflow del equipo: PR abierto → `In Progress` (o `In Review`), PR fusionado → `Done`.

### Paso 15 · Una tarea de extremo a extremo
Ver `docs/FLUJO_DE_TRABAJO.md` del repositorio. En resumen: rama con el nombre que sugiere Linear → commits que empiezan con `MHI-n` → pull request con `Fixes MHI-n` → otro integrante revisa y fusiona → la tarea pasa sola a `Done`.

> **Evidencia E5:** una issue en `Done` con el PR enlazado, y el PR fusionado con el texto `Fixes MHI-…`.

### Paso 16 · Daily
Cada uno actualiza sus issues antes del daily (15 minutos). Una sola pregunta: **¿qué nos impide cumplir el objetivo del sprint?**

---

## FASE 4 · Revisión y retrospectiva (Pasos 17 a 19)

- **Paso 17:** revisar el gráfico del ciclo (alcance, meta, iniciado, completado).
- **Paso 18 (Sprint Review):** mostrar a Huayta (y al docente) la aplicación funcionando en JavaFX. Solo lo que cumple criterios de aceptación y DoD queda en `Done`. Publicar un "Project update" con estado de salud (`On track` / `At risk` / `Off track`).
- **Paso 19:** documento `Retrospectiva – Sprint 1` y convertir cada acción de mejora en una issue `tarea-técnica` para el Sprint 2.

> **Evidencia E6:** gráfico del ciclo y Project update. **E7:** documento de retrospectiva y las acciones en el Sprint 2.

Plantilla del documento (pegar y completar **con lo que realmente pasó**):
```
Retrospectiva – Sprint 1

Objetivo del sprint: ...        ¿Se cumplió? Sí / Parcialmente / No
Puntos comprometidos: ..        Puntos completados: ..

Qué funcionó bien
- ...

Qué no funcionó
- ...

Acciones de mejora (cada una con responsable)
- [ ] ... (MHI-..)
```

## FASE 5 · Lanzamiento (Paso 20)
Marcar los hitos como alcanzados, capturar la vista "Timeline/Roadmap" y publicar la actualización final con los puntos comprometidos y completados por sprint.

> **Evidencia E8:** línea de tiempo del proyecto con los dos hitos.

---

## Ejercicio propio (lo que el profesor exige como mínimo)
- [ ] 10 o más historias con criterios, estimación y prioridad (ya hay 11)
- [ ] 2 ciclos ejecutados, cada uno con objetivo, revisión (Project update) y retrospectiva
- [ ] 3 historias o tareas cerradas automáticamente desde un PR con `Fixes MHI-n`
- [ ] Una historia de 13 puntos o más dividida en otras más pequeñas, con la división en los comentarios

*Sobre este último punto:* con la escala 1-2-3-5-8 no se puede votar 13. Si en el planning poker alguna historia (por ejemplo HU-08, la nota SOAP) les parece más grande que un 8, anótenlo en un comentario como "estimada ≥ 13", y divídanla en dos historias más chicas: **HU-08a** guardar la nota como borrador y **HU-08b** cerrarla de forma inmutable. Conviene confirmar con el profesor cómo quiere que lo evidencien.

## Evidencias a entregar (PDF con pie de figura)
| Evidencia | Qué muestra | Cuándo |
|---|---|---|
| E1 | Proyecto con Lead, fecha meta, hitos y roles | Al crear el proyecto |
| E2 | Fibonacci, estados y plantilla con DoD | Al configurar |
| E3 | Backlog completo ordenado | Después del planning poker |
| E4 | Sprint 1 planificado | Al iniciar el Sprint 1 |
| E5 | Issue en Done + PR fusionado | Con el primer PR fusionado |
| E6 | Gráfico del ciclo + Project update | Al cerrar el Sprint 1 |
| E7 | Retrospectiva + acciones en el Sprint 2 | Al cerrar el Sprint 1 |
| E8 | Línea de tiempo con los dos hitos | Al final |
