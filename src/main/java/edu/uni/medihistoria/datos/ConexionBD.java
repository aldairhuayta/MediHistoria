package edu.uni.medihistoria.datos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Único punto de la aplicación que sabe cómo conectarse a SQL Server.
 * Los datos de conexión se leen de config/db.properties, que NO se sube a GitHub.
 * Cada llamada devuelve una conexión nueva; quien la usa la cierra con try-with-resources
 * (así más adelante se pueden agrupar varias operaciones en una transacción).
 */
public final class ConexionBD {
    private static final Path RUTA_CONFIG = Paths.get("config", "db.properties");
    private static Properties configuracion;

    private ConexionBD() { }

    private static synchronized Properties cargarConfiguracion() throws SQLException {
        if (configuracion != null) {
            return configuracion;
        }
        if (!Files.exists(RUTA_CONFIG)) {
            throw new SQLException("Falta el archivo " + RUTA_CONFIG
                    + ". Copie config/db.properties.example como config/db.properties y complete sus datos.");
        }
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(RUTA_CONFIG)) {
            p.load(in);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer " + RUTA_CONFIG, e);
        }
        for (String clave : new String[]{"db.host", "db.port", "db.name", "db.user", "db.password"}) {
            if (p.getProperty(clave) == null || p.getProperty(clave).isBlank()) {
                throw new SQLException("Falta la propiedad '" + clave + "' en " + RUTA_CONFIG);
            }
        }
        configuracion = p;
        return p;
    }

    public static Connection obtener() throws SQLException {
        Properties p = cargarConfiguracion();
        String url = "jdbc:sqlserver://" + p.getProperty("db.host").trim() + ":" + p.getProperty("db.port").trim()
                + ";databaseName=" + p.getProperty("db.name").trim()
                + ";encrypt=true;trustServerCertificate=true;";
        return DriverManager.getConnection(url, p.getProperty("db.user").trim(), p.getProperty("db.password"));
    }
}
