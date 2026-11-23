package edu.uni.medihistoria.modelo;

/** Módulos funcionales del sistema; sirven para decidir qué puede abrir cada rol. */
public enum Modulo {
    USUARIOS("Usuarios"),
    PACIENTES("Pacientes"),
    CITAS("Citas"),
    TRIAJE("Triaje"),
    CONSULTA("Consulta"),
    AUDITORIA("Auditoría"),
    REPORTES("Reportes");

    private final String etiqueta;

    Modulo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
