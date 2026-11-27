package edu.uni.medihistoria.seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

/**
 * Claves para proteger datos sensibles (cifrado AES y huella HMAC del DNI).
 * Se leen de config/seguridad.properties, que NO se sube a GitHub. Si el archivo no existe, se genera
 * uno nuevo la primera vez. IMPORTANTE: sin este archivo no se pueden leer los datos ya cifrados.
 */
public final class ConfiguracionSeguridad {
    private static final Path RUTA = Paths.get("config", "seguridad.properties");
    private static byte[] claveCifrado;
    private static byte[] claveHmac;

    private ConfiguracionSeguridad() { }

    public static byte[] claveCifrado() {
        cargar();
        return claveCifrado.clone();
    }

    public static byte[] claveHmac() {
        cargar();
        return claveHmac.clone();
    }

    private static synchronized void cargar() {
        if (claveCifrado != null) {
            return;
        }
        try {
            if (!Files.exists(RUTA)) {
                generar();
            }
            Properties p = new Properties();
            try (InputStream in = Files.newInputStream(RUTA)) {
                p.load(in);
            }
            claveCifrado = leerClave(p, "cifrado.clave");
            claveHmac = leerClave(p, "cifrado.hmac");
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + RUTA, e);
        }
    }

    private static byte[] leerClave(Properties p, String nombre) {
        String valor = p.getProperty(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la propiedad '" + nombre + "' en " + RUTA
                    + ". No se genera una nueva para no perder el acceso a los datos ya cifrados.");
        }
        byte[] bytes = Base64.getDecoder().decode(valor.trim());
        if (bytes.length != 32) {
            throw new IllegalStateException("La clave '" + nombre + "' debe ser de 32 bytes (Base64).");
        }
        return bytes;
    }

    private static void generar() throws IOException {
        SecureRandom azar = new SecureRandom();
        byte[] c = new byte[32];
        byte[] h = new byte[32];
        azar.nextBytes(c);
        azar.nextBytes(h);
        Properties p = new Properties();
        p.setProperty("cifrado.clave", Base64.getEncoder().encodeToString(c));
        p.setProperty("cifrado.hmac", Base64.getEncoder().encodeToString(h));
        Files.createDirectories(RUTA.getParent());
        try (OutputStream out = Files.newOutputStream(RUTA)) {
            p.store(out, "Claves de MediHistoria. NO SUBIR A GITHUB. Guardar una copia: sin ellas no se pueden leer los datos cifrados.");
        }
        System.err.println("AVISO: se generó " + RUTA + " con claves nuevas. Guárdelo: sin este archivo "
                + "no se podrán leer los datos cifrados.");
    }
}
