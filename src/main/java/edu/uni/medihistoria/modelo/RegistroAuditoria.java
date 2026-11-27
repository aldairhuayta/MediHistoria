package edu.uni.medihistoria.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Una fila de la bitácora de auditoría (solo lectura). */
public class RegistroAuditoria {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final long idAuditoria;
    private final LocalDateTime fechaHora;
    private final String usuario;
    private final String accion;
    private final String entidad;
    private final Integer idEntidad;
    private final String detalle;

    public RegistroAuditoria(long idAuditoria, LocalDateTime fechaHora, String usuario, String accion,
                             String entidad, Integer idEntidad, String detalle) {
        this.idAuditoria = idAuditoria;
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.idEntidad = idEntidad;
        this.detalle = detalle;
    }

    public long getIdAuditoria() { return idAuditoria; }
    public String getUsuario() { return usuario == null ? "(sistema)" : usuario; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad == null ? "" : entidad; }
    public String getIdEntidadTexto() { return idEntidad == null ? "" : String.valueOf(idEntidad); }
    public String getDetalle() { return detalle == null ? "" : detalle; }
    public String getFechaTexto() { return FORMATO.format(fechaHora); }
}
