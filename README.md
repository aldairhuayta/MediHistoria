# MediHistoria

Sistema de Historial Clínico Electrónico para Consultorios. Aplicación de escritorio en **Java 21 + JavaFX**, arquitectura **MVC** con capa DAO y base de datos **Microsoft SQL Server** (JDBC y `PreparedStatement`).

Proyecto del curso **Ingeniería de Software (CIB02)** — UNI-FIEE — Grupo 1.

**Estado actual:** Sprint 1 (acceso seguro): inicio de sesión contra la base de datos, control de acceso por roles y gestión de usuarios.

## Integrantes

| Integrante | Código UNI | Rol en Scrum |
|---|---|---|
| Ojeda Campomanes, Kevin Ricardo | 20182575I | Equipo de desarrollo |
| Vega Huaraca, Yon Antoni | 20172639D | Equipo de desarrollo |
| Huayta Garavito, Aldair Dealessandro | 20221488K | Product Owner |
| Coro Canchari, Saúl | 20221524G | Scrum Master |

## Tecnologías

| Aspecto | Herramienta |
|---|---|
| Lenguaje / JDK | Java, JDK 21 |
| Interfaz | JavaFX 21 (FXML) |
| Construcción | Maven |
| IDE | IntelliJ IDEA Community Edition 2025.3.6.1 |
| Base de datos | Microsoft SQL Server (Developer) + SSMS; `mssql-jdbc` 12.6.2 |
| Control de versiones | Git y GitHub |
| Gestión ágil | Linear (Scrum) |

## Cómo ejecutar el proyecto

### 1. Preparar la base de datos
1. Abrir **SSMS** y conectarse al servidor local.
2. Abrir y ejecutar (F5) `database/01_crear_base_datos.sql` y después `database/02_datos_iniciales.sql`.
3. Verificar que aparecen **7 tablas** y **4 usuarios**.

> Si Java no logra conectarse: en *SQL Server Configuration Manager* habilitar **TCP/IP** (puerto 1433) y reiniciar el servicio de SQL Server. La autenticación debe permitir usuario y contraseña (modo mixto) si usan `sa`.

### 2. Configurar la conexión (cada integrante en su equipo)
1. Copiar `config/db.properties.example` como `config/db.properties`.
2. Completar `db.user` y `db.password` con los de su SQL Server.
3. **Ese archivo no se sube a GitHub** (está en `.gitignore`).

### 3. Ejecutar la aplicación
- **IntelliJ:** abrir el proyecto (archivo `pom.xml`), esperar a que Maven descargue las dependencias y ejecutar la clase `edu.uni.medihistoria.Launcher`.
  Si no encuentra `config/db.properties`, en *Run → Edit Configurations* poner **Working directory** = la carpeta raíz del proyecto.
- **Terminal:** `mvn javafx:run`

## Usuarios de prueba

Todos deben **cambiar su contraseña en el primer ingreso**.

| Usuario | Rol | Contraseña inicial |
|---|---|---|
| `admin` | Administrador | `Admin2026!` |
| `dra.ramos` | Médico | `Clinica2026!` |
| `enf.torres` | Enfermero | `Clinica2026!` |
| `recep.quispe` | Recepcionista | `Clinica2026!` |

Son cuentas de demostración del curso; en un sistema real se eliminarían antes de operar.

## Estructura del repositorio

```
MediHistoria/
├── src/main/java/edu/uni/medihistoria/
│   ├── modelo/        Rol, Modulo, Usuario (reglas del dominio, sin JavaFX)
│   ├── vista/         (FXML en src/main/resources/.../vista)
│   ├── controlador/   Login, Principal, CambiarClave, Usuario
│   ├── datos/         ConexionBD, DAO (único lugar con SQL)
│   ├── seguridad/     hash de contraseñas, autenticación, sesión, auditoría
│   └── util/          Navegador, Alertas, Validador
├── src/main/resources/edu/uni/medihistoria/vista/   Pantallas .fxml y estilos.css
├── database/          Scripts SQL (esquema y datos iniciales)
├── db-backup/         Backup .bak de la base de datos (entrega final)
├── config/            Ejemplo de configuración de conexión
└── docs/              Documento de sustentación, diagramas y guías
```

## Backup de la base de datos

El backup final (`.bak`) se entrega en la carpeta **`db-backup/`**, o bien su enlace de descarga queda indicado en `db-backup/LEEME.txt`.

- Nombre del archivo: *(se completa en la entrega final)*
- Restauración: SSMS → clic derecho en *Databases* → *Restore Database…*, o con `RESTORE DATABASE MediHistoria FROM DISK = '<ruta>\MediHistoria.bak'`.

## Flujo de trabajo

El trabajo se planifica en Linear (identificador de equipo **MHI**). Cada tarea tiene su rama, su pull request con `Fixes MHI-n` y la revisión de otro integrante. Ver `docs/FLUJO_DE_TRABAJO.md`.
