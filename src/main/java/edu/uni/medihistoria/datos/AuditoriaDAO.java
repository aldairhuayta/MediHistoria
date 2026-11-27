package edu.uni.medihistoria.datos;

import edu.uni.medihistoria.modelo.RegistroAuditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {
    public void insertar(Integer idUsuario, String accion, String entidad, Integer idEntidad, String detalle)
            throws SQLException {
        String sql = "INSERT INTO Auditoria (idUsuario, accion, entidad, idEntidad, detalle) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idUsuario == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            if (entidad == null) ps.setNull(3, Types.VARCHAR); else ps.setString(3, entidad);
            if (idEntidad == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, idEntidad);
            if (detalle == null) {
                ps.setNull(5, Types.NVARCHAR);
            } else {
                ps.setString(5, detalle.length() > 200 ? detalle.substring(0, 200) : detalle);
            }
            ps.executeUpdate();
        }
    }

    static String sqlListar(boolean filtrarAccion) {
        return "SELECT TOP (?) a.idAuditoria, a.fechaHora, u.usuario, a.accion, a.entidad, a.idEntidad, a.detalle "
                + "FROM Auditoria a LEFT JOIN Usuario u ON u.idUsuario = a.idUsuario "
                + "WHERE a.fechaHora >= ? AND a.fechaHora < ?"
                + (filtrarAccion ? " AND a.accion = ?" : "")
                + " ORDER BY a.idAuditoria DESC";
    }

    static final String SQL_ACCIONES = "SELECT DISTINCT accion FROM Auditoria ORDER BY accion";

    /** Eventos entre dos fechas (ambas incluidas), del más reciente al más antiguo. Solo lectura. */
    public List<RegistroAuditoria> listar(LocalDate desde, LocalDate hasta, String accion, int limite)
            throws SQLException {
        boolean filtrar = accion != null && !accion.isBlank();
        List<RegistroAuditoria> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sqlListar(filtrar))) {
            ps.setInt(1, limite);
            ps.setTimestamp(2, Timestamp.valueOf(desde.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));
            if (filtrar) {
                ps.setString(4, accion);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idEntidad = rs.getInt("idEntidad");
                    Integer entidad = rs.wasNull() ? null : idEntidad;
                    lista.add(new RegistroAuditoria(rs.getLong("idAuditoria"),
                            rs.getTimestamp("fechaHora").toLocalDateTime(), rs.getString("usuario"),
                            rs.getString("accion"), rs.getString("entidad"), entidad, rs.getString("detalle")));
                }
            }
        }
        return lista;
    }

    public List<String> listarAcciones() throws SQLException {
        List<String> acciones = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(SQL_ACCIONES);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                acciones.add(rs.getString(1));
            }
        }
        return acciones;
    }
}
