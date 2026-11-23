# Guía del código del Sprint 1

Para entender qué hace cada parte antes de explicarla o responder preguntas.

## Idea general
```
Pantalla (.fxml)  ->  Controlador  ->  Servicio de seguridad  ->  DAO  ->  SQL Server
   (Vista)         (recibe clics)     (reglas)                 (único con SQL)
```
El **modelo** (`Usuario`, `Rol`, `Modulo`) son los datos y reglas; no sabe nada de pantallas ni de SQL.

## Qué hace cada archivo

| Archivo | Para qué sirve |
|---|---|
| `modelo/Rol.java` | Los 4 roles y qué módulos puede abrir cada uno (matriz de permisos) |
| `modelo/Modulo.java` | Lista de módulos del sistema |
| `modelo/Usuario.java` | Datos de un usuario (nunca guarda la contraseña) |
| `datos/ConexionBD.java` | Se conecta a SQL Server leyendo `config/db.properties` |
| `datos/UsuarioDAO.java` | Todas las consultas de la tabla Usuario, con `PreparedStatement` |
| `datos/AuditoriaDAO.java` | Guarda eventos en la tabla Auditoria |
| `datos/Credencial.java` | Paquete interno: usuario + hash + sal + intentos fallidos |
| `seguridad/PasswordUtil.java` | Convierte contraseñas en hash con sal; valida la política; genera claves temporales |
| `seguridad/AutenticacionServicio.java` | Reglas del login: verifica, cuenta intentos, bloquea, cambia la contraseña |
| `seguridad/Sesion.java` | Guarda quién inició sesión y comprueba permisos |
| `seguridad/Auditor.java` | Atajo para registrar eventos de auditoría |
| `util/Validador.java` | Valida el formato del usuario y los textos |
| `util/Navegador.java` | Carga pantallas y cambia de escena en la misma ventana |
| `util/Alertas.java` | Ventanas de mensaje (info, error, confirmar, clave temporal) |
| `controlador/LoginController.java` | Lógica de la pantalla de Login |
| `controlador/PrincipalController.java` | Menú según el rol y apertura de módulos |
| `controlador/CambiarClaveController.java` | Ventana de cambio de contraseña |
| `controlador/UsuarioController.java` | Pantalla de gestión de usuarios (solo administrador) |
| `Launcher.java` / `MainApp.java` | Arranque de la aplicación JavaFX |

## Conceptos que conviene saber explicar

- **MVC:** separar lo que se ve (vista), lo que se decide (controlador) y los datos (modelo). Cambiar una pantalla no obliga a tocar el resto.
- **DAO:** una clase por tabla que concentra el SQL. Los controladores nunca escriben consultas.
- **PreparedStatement:** envía los datos separados de la instrucción SQL, para que nadie pueda inyectar órdenes maliciosas en un campo (inyección SQL).
- **Hash con sal:** la contraseña no se guarda. Se guarda el resultado de una operación de un solo sentido (PBKDF2) mezclada con un valor aleatorio propio de cada usuario (la sal). Dos usuarios con la misma contraseña tienen hashes distintos.
- **RBAC:** cada rol solo ve y usa lo que necesita. Se comprueba al mostrar el menú **y** otra vez al abrir el módulo.
- **Mensaje genérico:** el login responde igual si falla el usuario o la contraseña, para no dar pistas.
- **Bloqueo por intentos:** al 5.º intento fallido la cuenta se bloquea; el administrador la desbloquea.
- **Auditoría:** cada evento importante (ingresos, fallos, altas, cambios) queda con quién, qué y cuándo.

## Preguntas que pueden hacerles
- *¿Dónde se guardan las contraseñas?* No se guardan; solo el hash PBKDF2 y su sal en la tabla Usuario.
- *¿Qué pasa si SQL Server está apagado?* El usuario ve un mensaje genérico; el detalle va a la consola.
- *¿Cómo evitan que un recepcionista abra Usuarios?* El menú no se la muestra y, además, `Sesion.exigirAcceso` lo bloquea y lo registra en la auditoría.
- *¿Por qué no se sube `db.properties`?* Porque tiene la contraseña de la base de datos de cada integrante.
