package edu.uni.medihistoria.seguridad;

import edu.uni.medihistoria.datos.AuditoriaDAO;

import java.sql.SQLException;

/** Punto único para registrar eventos de auditoría. Un fallo al auditar no debe tumbar la operación. */
public final class Auditor {
    private Auditor() { }

    public static void registrar(Integer idUsuario, String accion, String entidad, Integer idEntidad, String detalle) {
        try {
            new AuditoriaDAO().insertar(idUsuario, accion, entidad, idEntidad, detalle);
        } catch (SQLException e) {
            System.err.println("No se pudo registrar el evento de auditoría (" + accion + "): " + e.getMessage());
        }
    }
}
