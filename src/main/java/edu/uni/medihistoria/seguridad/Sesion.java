package edu.uni.medihistoria.seguridad;

import edu.uni.medihistoria.modelo.Accion;
import edu.uni.medihistoria.modelo.Modulo;
import edu.uni.medihistoria.modelo.Usuario;

/** Usuario que inició sesión en la aplicación y verificación de sus permisos. */
public final class Sesion {
    private static Usuario actual;

    private Sesion() { }

    public static void iniciar(Usuario usuario) {
        actual = usuario;
    }

    public static void cerrar() {
        actual = null;
    }

    public static Usuario getUsuario() {
        return actual;
    }

    public static boolean puedeAcceder(Modulo modulo) {
        return actual != null && actual.getRol().puedeAcceder(modulo);
    }

    /** Lanza SecurityException (y deja constancia en la auditoría) si el rol no puede usar el módulo. */
    public static void exigirAcceso(Modulo modulo) {
        if (!puedeAcceder(modulo)) {
            Integer id = actual == null ? null : actual.getIdUsuario();
            Auditor.registrar(id, "ACCESO_DENEGADO", "Modulo", null, modulo.name());
            throw new SecurityException("No tiene permiso para acceder al módulo " + modulo.getEtiqueta() + ".");
        }
    }

    public static boolean puede(Modulo modulo, Accion accion) {
        return actual != null && actual.getRol().puede(modulo, accion);
    }

    /** Verifica permiso por acción (crear, leer, actualizar). Si no corresponde, lo registra y lanza SecurityException. */
    public static void exigirAccion(Modulo modulo, Accion accion) {
        if (!puede(modulo, accion)) {
            Integer id = actual == null ? null : actual.getIdUsuario();
            Auditor.registrar(id, "ACCESO_DENEGADO", "Modulo", null, modulo.name() + ":" + accion.name());
            throw new SecurityException("No tiene permiso para realizar esta acción en el módulo "
                    + modulo.getEtiqueta() + ".");
        }
    }
}
