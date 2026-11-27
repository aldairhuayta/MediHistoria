package edu.uni.medihistoria.seguridad;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Protección de datos sensibles en la base de datos.
 *  - cifrar / descifrar: AES-256-GCM (cifra y además detecta si el dato fue alterado).
 *  - huellaBusqueda: HMAC-SHA256, para buscar por DNI y garantizar su unicidad sin guardarlo en claro.
 */
public final class CifradoUtil {
    private static final int BYTES_IV = 12;
    private static final int BITS_ETIQUETA = 128;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private CifradoUtil() { }

    /** Devuelve Base64(IV + texto cifrado). Un mismo texto produce resultados distintos cada vez (IV aleatorio). */
    public static String cifrar(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            byte[] iv = new byte[BYTES_IV];
            ALEATORIO.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(ConfiguracionSeguridad.claveCifrado(), "AES"),
                    new GCMParameterSpec(BITS_ETIQUETA, iv));
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            byte[] salida = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, salida, 0, iv.length);
            System.arraycopy(cifrado, 0, salida, iv.length, cifrado.length);
            return Base64.getEncoder().encodeToString(salida);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo cifrar el dato.", e);
        }
    }

    public static String descifrar(String base64) {
        if (base64 == null) {
            return null;
        }
        try {
            byte[] datos = Base64.getDecoder().decode(base64);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ConfiguracionSeguridad.claveCifrado(), "AES"),
                    new GCMParameterSpec(BITS_ETIQUETA, datos, 0, BYTES_IV));
            byte[] claro = cipher.doFinal(datos, BYTES_IV, datos.length - BYTES_IV);
            return new String(claro, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException("No se pudo descifrar el dato. ¿Cambió el archivo "
                    + "config/seguridad.properties respecto al usado al guardarlo?", e);
        }
    }

    /** Huella determinista (hex, 64 caracteres) de un texto; misma entrada, misma huella. */
    public static String huellaBusqueda(String texto) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(ConfiguracionSeguridad.claveHmac(), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo calcular la huella de búsqueda.", e);
        }
    }
}
