package edu.uni.medihistoria.modelo;

/**
 * Roles del sistema (control de acceso por roles, RBAC).
 * Cada rol solo puede abrir los módulos que necesita (principio de mínimo privilegio).
 */
public enum Rol {
    ADMINISTRADOR("Administrador", "Administrador"),
    MEDICO("Medico", "Médico"),
    ENFERMERO("Enfermero", "Enfermero"),
    RECEPCIONISTA("Recepcionista", "Recepcionista");

    private final String codigoBd;   // valor guardado en la tabla Rol
    private final String etiqueta;   // texto que ve el usuario

    Rol(String codigoBd, String etiqueta) {
        this.codigoBd = codigoBd;
        this.etiqueta = etiqueta;
    }

    public String getCodigoBd() {
        return codigoBd;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static Rol desdeBd(String codigo) {
        for (Rol r : values()) {
            if (r.codigoBd.equalsIgnoreCase(codigo)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rol desconocido en la base de datos: " + codigo);
    }

    /** Matriz de permisos del documento de sustentación (sección 5.2). */
    public boolean puedeAcceder(Modulo m) {
        return switch (this) {
            case ADMINISTRADOR -> m == Modulo.USUARIOS || m == Modulo.AUDITORIA || m == Modulo.REPORTES;
            case MEDICO -> m == Modulo.PACIENTES || m == Modulo.CITAS || m == Modulo.TRIAJE
                    || m == Modulo.CONSULTA || m == Modulo.REPORTES;
            case ENFERMERO -> m == Modulo.PACIENTES || m == Modulo.CITAS || m == Modulo.TRIAJE
                    || m == Modulo.CONSULTA;
            case RECEPCIONISTA -> m == Modulo.PACIENTES || m == Modulo.CITAS;
        };
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
