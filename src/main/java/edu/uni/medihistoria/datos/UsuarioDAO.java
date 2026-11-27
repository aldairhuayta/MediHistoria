package edu.uni.medihistoria.datos;

import edu.uni.medihistoria.modelo.Rol;
import edu.uni.medihistoria.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a la tabla Usuario. Todas las sentencias usan PreparedStatement con parámetros. */
public class UsuarioDAO {

    private static final String COLUMNAS_USUARIO =
            "u.idUsuario, u.usuario, u.nombres, u.apellidos, r.nombre AS rol, "
                    + "u.activo, u.bloqueado, u.debeCambiarClave, u.ultimoAcceso";

    private static final String DESDE = " FROM Usuario u JOIN Rol r ON r.idRol = u.idRol ";

    private Usuario mapear(ResultSet rs) throws SQLException {
        Timestamp ultimo = rs.getTimestamp("ultimoAcceso");
        return new Usuario(
                rs.getInt("idUsuario"),
                rs.getString("usuario"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                Rol.desdeBd(rs.getString("rol")),
                rs.getBoolean("activo"),
                rs.getBoolean("bloqueado"),
                rs.getBoolean("debeCambiarClave"),
                ultimo == null ? null : ultimo.toLocalDateTime());
    }

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT " + COLUMNAS_USUARIO + DESDE + "ORDER BY u.apellidos, u.nombres";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Usuarios activos de un rol (por ejemplo, los médicos para asignarles citas). */
    public List<Usuario> listarActivosPorRol(Rol rol) throws SQLException {
        String sql = "SELECT " + COLUMNAS_USUARIO + DESDE
                + "WHERE r.nombre = ? AND u.activo = 1 AND u.bloqueado = 0 ORDER BY u.apellidos, u.nombres";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol.getCodigoBd());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /** Devuelve usuario + hash + sal + intentos, o null si el usuario no existe. */
    public Credencial obtenerCredencial(String nombreUsuario) throws SQLException {
        String sql = "SELECT " + COLUMNAS_USUARIO + ", u.claveHash, u.sal, u.intentosFallidos"
                + DESDE + "WHERE u.usuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Credencial(mapear(rs), rs.getString("claveHash"), rs.getString("sal"),
                            rs.getInt("intentosFallidos"));
                }
            }
        }
        return null;
    }

    public boolean existeUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT 1 FROM Usuario WHERE usuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Inserta el usuario con su contraseña ya convertida en hash. Devuelve el id generado. */
    public int insertar(Usuario u, String claveHash, String sal, boolean debeCambiarClave) throws SQLException {
        String sql = "INSERT INTO Usuario (usuario, nombres, apellidos, claveHash, sal, idRol, activo, debeCambiarClave) "
                + "VALUES (?, ?, ?, ?, ?, (SELECT idRol FROM Rol WHERE nombre = ?), ?, ?)";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getNombres());
            ps.setString(3, u.getApellidos());
            ps.setString(4, claveHash);
            ps.setString(5, sal);
            ps.setString(6, u.getRol().getCodigoBd());
            ps.setBoolean(7, u.isActivo());
            ps.setBoolean(8, debeCambiarClave);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        }
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE Usuario SET nombres = ?, apellidos = ?, "
                + "idRol = (SELECT idRol FROM Rol WHERE nombre = ?), activo = ? WHERE idUsuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombres());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getRol().getCodigoBd());
            ps.setBoolean(4, u.isActivo());
            ps.setInt(5, u.getIdUsuario());
            ps.executeUpdate();
        }
    }

    /** Guarda una contraseña nueva (hash + sal) y desbloquea la cuenta. */
    public void cambiarClave(int idUsuario, String claveHash, String sal, boolean debeCambiarClave) throws SQLException {
        String sql = "UPDATE Usuario SET claveHash = ?, sal = ?, debeCambiarClave = ?, "
                + "intentosFallidos = 0, bloqueado = 0 WHERE idUsuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, claveHash);
            ps.setString(2, sal);
            ps.setBoolean(3, debeCambiarClave);
            ps.setInt(4, idUsuario);
            ps.executeUpdate();
        }
    }

    public void registrarFallo(int idUsuario, int maxIntentos) throws SQLException {
        String sql = "UPDATE Usuario SET intentosFallidos = intentosFallidos + 1, "
                + "bloqueado = CASE WHEN intentosFallidos + 1 >= ? THEN 1 ELSE bloqueado END "
                + "WHERE idUsuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maxIntentos);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    public void registrarAcceso(int idUsuario) throws SQLException {
        String sql = "UPDATE Usuario SET intentosFallidos = 0, ultimoAcceso = GETDATE() WHERE idUsuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    public void desbloquear(int idUsuario) throws SQLException {
        String sql = "UPDATE Usuario SET bloqueado = 0, intentosFallidos = 0 WHERE idUsuario = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    /** Cuenta administradores activos distintos del indicado (para no dejar el sistema sin administrador). */
    public int contarAdministradoresActivos(int excluirIdUsuario) throws SQLException {
        String sql = "SELECT COUNT(*)" + DESDE + "WHERE r.nombre = ? AND u.activo = 1 AND u.idUsuario <> ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, Rol.ADMINISTRADOR.getCodigoBd());
            ps.setInt(2, excluirIdUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
