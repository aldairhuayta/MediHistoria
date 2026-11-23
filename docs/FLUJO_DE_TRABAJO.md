# Flujo de trabajo del equipo (Linear + Git)

Cada tarea técnica es una *issue* de Linear con identificador `MHI-n`. El código de esa tarea
viaja en **una rama propia**, se sube con **un pull request (PR)** y lo **revisa otro integrante**.
Al fusionar el PR, Linear pasa la tarea a *Done* automáticamente.

## Paso a paso (ejemplo con la tarea MHI-5)

```bash
git checkout main
git pull
git checkout -b ojeda/mhi-5-login          # Linear sugiere este nombre (copiar desde la issue)

# ... trabajar, probar ...

git add <archivos de SU tarea>             # evitar "git add ." si hay archivos de otros
git commit -m "MHI-5 Agrega pantalla y controlador de Login"
git push -u origin ojeda/mhi-5-login
```

1. En GitHub: **New pull request** hacia `main`. En la descripción escribir `Fixes MHI-5`.
2. Otro integrante revisa (pestaña *Files changed*), comenta si hay algo por corregir y **fusiona** (*Merge*).
3. Volver a Linear: la issue debe estar en **Done** con el enlace al PR.

## Reglas del equipo (coinciden con la definición de terminado)

- Nadie sube directo a `main`: todo entra por pull request.
- Quien abre el PR **no** lo fusiona; lo hace otro integrante.
- Commits chicos, con el identificador al inicio: `MHI-5 Descripción en presente`.
- Antes de empezar a trabajar: `git pull`. Al terminar la sesión: `git push`.
- Nunca subir `config/db.properties` ni contraseñas (está en `.gitignore`).
- Palabras del PR: `Fixes MHI-n` (cierra la tarea), `Part of MHI-n` (la enlaza sin cerrarla).

## Orden de integración del Sprint 1

Algunas clases dependen de otras, por eso los PR se fusionan en este orden para que `main` siempre compile:

| Orden | Tarea | Contenido |
|---|---|---|
| 1 | Base del repositorio y modelo | `pom.xml`, `.gitignore`, `README.md`, `config/`, `db-backup/`, `docs/`, `modelo/*` |
| 2 | Scripts SQL | `database/*.sql` |
| 3 | Capa de datos | `datos/*` |
| 4 | Seguridad | `seguridad/*`, `util/Validador.java` |
| 5 | Arranque y navegación | `Launcher`, `MainApp`, `util/Navegador`, `util/Alertas`, `estilos.css` |
| 6 | Cambio de contraseña | `CambiarClave.fxml`, `CambiarClaveController` |
| 7 | Login | `Login.fxml`, `LoginController` |
| 8 | Pantalla principal con menú por rol | `Principal.fxml`, `PrincipalController` |
| 9 | Gestión de usuarios | `Usuarios.fxml`, `UsuarioController` |
| 10 | Pruebas del Sprint 1 | resultados en `docs/pruebas_sprint1.md` |
