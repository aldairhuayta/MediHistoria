package edu.uni.medihistoria.seguridad;

import edu.uni.medihistoria.datos.Credencial;
import edu.uni.medihistoria.datos.UsuarioDAO;
import edu.uni.medihistoria.modelo.Usuario;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Reglas de autenticación: verificación de contraseña con hash, bloqueo tras intentos fallidos
 * y cambio de contraseña. El controlador solo llama a esta clase; no conoce el SQL ni el hash.
 */
public class AutenticacionServicio {
    public static final int MAX_INTENTOS = 5;
    /** Sal ficticia para gastar el mismo tiempo cuando el usuario no existe (evita adivinar usuarios por tiempos). */
    private static final String SAL_FICTICIA = PasswordUtil.generarSal();

    private final UsuarioDAO dao = new UsuarioDAO();

    /** Error de validación al cambiar la contraseña; el mensaje es apto para mostrarse al usuario. */
    public static class ClaveInvalidaException extends Exception {
        private static final long serialVersionUID = 1L;

        public ClaveInvalidaException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Devuelve el usuario si las credenciales son correctas; Optional vacío en cualquier otro caso
     * (usuario inexistente, clave incorrecta, cuenta bloqueada o inactiva). El motivo exacto solo
     * queda en la auditoría, nunca se le informa a quien intenta ingresar.
     */
    public Optional<Usuario> autenticar(String nombreUsuario, String clave) throws SQLException {
        Credencial c = dao.obtenerCredencial(nombreUsuario);

        if (c == null) {
            PasswordUtil.hash(clave, SAL_FICTICIA);
            Auditor.registrar(null, "LOGIN_FALLIDO", "Usuario", null, "Usuario inexistente: " + recortar(nombreUsuario));
            return Optional.empty();
        }

        Usuario u = c.usuario();
        if (!u.isActivo() || u.isBloqueado()) {
            PasswordUtil.hash(clave, SAL_FICTICIA);
            Auditor.registrar(u.getIdUsuario(), "LOGIN_RECHAZADO", "Usuario", u.getIdUsuario(),
                    u.isBloqueado() ? "Cuenta bloqueada" : "Cuenta inactiva");
            return Optional.empty();
        }

        if (PasswordUtil.verificar(clave, c.hash(), c.sal())) {
            dao.registrarAcceso(u.getIdUsuario());
            Auditor.registrar(u.getIdUsuario(), "LOGIN_OK", "Usuario", u.getIdUsuario(), null);
            return Optional.of(u);
        }

        dao.registrarFallo(u.getIdUsuario(), MAX_INTENTOS);
        Auditor.registrar(u.getIdUsuario(), "LOGIN_FALLIDO", "Usuario", u.getIdUsuario(), "Contraseña incorrecta");
        if (c.intentosFallidos() + 1 >= MAX_INTENTOS) {
            Auditor.registrar(u.getIdUsuario(), "CUENTA_BLOQUEADA", "Usuario", u.getIdUsuario(),
                    "Se alcanzó el máximo de intentos fallidos");
        }
        return Optional.empty();
    }

    /** Cambia la contraseña del propio usuario (exige la actual y una nueva que cumpla la política). */
    public void cambiarClave(String nombreUsuario, String actual, String nueva)
            throws SQLException, ClaveInvalidaException {
        Credencial c = dao.obtenerCredencial(nombreUsuario);
        if (c == null || !PasswordUtil.verificar(actual, c.hash(), c.sal())) {
            Integer id = c == null ? null : c.usuario().getIdUsuario();
            Auditor.registrar(id, "CLAVE_CAMBIO_FALLIDO", "Usuario", id, "Contraseña actual incorrecta");
            throw new ClaveInvalidaException("La contraseña actual no es correcta.");
        }
        String problema = PasswordUtil.validarFortaleza(nueva);
        if (problema != null) {
            throw new ClaveInvalidaException(problema);
        }
        if (actual.equals(nueva)) {
            throw new ClaveInvalidaException("La nueva contraseña debe ser distinta de la actual.");
        }
        int id = c.usuario().getIdUsuario();
        String sal = PasswordUtil.generarSal();
        dao.cambiarClave(id, PasswordUtil.hash(nueva, sal), sal, false);
        Auditor.registrar(id, "CLAVE_CAMBIADA", "Usuario", id, null);
    }

    private static String recortar(String texto) {
        return texto.length() <= 30 ? texto : texto.substring(0, 30);
    }
}
