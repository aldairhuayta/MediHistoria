package edu.uni.medihistoria.seguridad;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Manejo seguro de contraseñas: nunca se guarda la contraseña, solo un hash PBKDF2 con sal.
 * (Función de derivación de clave con sal, como se vio en la semana 12.)
 */
public final class PasswordUtil {
    private static final int ITERACIONES = 120_000;
    private static final int LONGITUD_HASH_BITS = 256;
    private static final int LONGITUD_SAL_BYTES = 16;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private static final String MAYUSCULAS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijkmnpqrstuvwxyz";
    private static final String DIGITOS = "23456789";

    private PasswordUtil() { }

    /** Genera una sal aleatoria (Base64). Cada usuario tiene una sal distinta. */
    public static String generarSal() {
        byte[] sal = new byte[LONGITUD_SAL_BYTES];
        ALEATORIO.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }

    /** Calcula el hash PBKDF2-HMAC-SHA256 de la clave con la sal indicada (ambos en Base64). */
    public static String hash(String clave, String salBase64) {
        PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), Base64.getDecoder().decode(salBase64),
                ITERACIONES, LONGITUD_HASH_BITS);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.getEncoder().encodeToString(fabrica.generateSecret(spec).getEncoded());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña.", e);
        } finally {
            spec.clearPassword();
        }
    }

    /** Compara en tiempo constante para no filtrar información por el tiempo de respuesta. */
    public static boolean verificar(String clave, String hashEsperado, String salBase64) {
        byte[] calculado = Base64.getDecoder().decode(hash(clave, salBase64));
        byte[] esperado = Base64.getDecoder().decode(hashEsperado);
        return MessageDigest.isEqual(calculado, esperado);
    }

    /** Devuelve null si la contraseña cumple la política; si no, el motivo para mostrar al usuario. */
    public static String validarFortaleza(String clave) {
        if (clave == null || clave.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres.";
        }
        if (clave.length() > 64) {
            return "La contraseña no debe superar los 64 caracteres.";
        }
        boolean mayuscula = false;
        boolean minuscula = false;
        boolean digito = false;
        for (char c : clave.toCharArray()) {
            if (Character.isUpperCase(c)) mayuscula = true;
            else if (Character.isLowerCase(c)) minuscula = true;
            else if (Character.isDigit(c)) digito = true;
        }
        if (!(mayuscula && minuscula && digito)) {
            return "La contraseña debe incluir mayúsculas, minúsculas y números.";
        }
        return null;
    }

    /** Contraseña temporal de 10 caracteres que siempre cumple la política (sin caracteres ambiguos). */
    public static String generarClaveTemporal() {
        StringBuilder sb = new StringBuilder();
        sb.append(MAYUSCULAS.charAt(ALEATORIO.nextInt(MAYUSCULAS.length())));
        sb.append(MINUSCULAS.charAt(ALEATORIO.nextInt(MINUSCULAS.length())));
        sb.append(DIGITOS.charAt(ALEATORIO.nextInt(DIGITOS.length())));
        String todos = MAYUSCULAS + MINUSCULAS + DIGITOS;
        while (sb.length() < 10) {
            sb.append(todos.charAt(ALEATORIO.nextInt(todos.length())));
        }
        // mezclar para que las tres primeras posiciones no sean siempre mayúscula, minúscula, dígito
        char[] c = sb.toString().toCharArray();
        for (int i = c.length - 1; i > 0; i--) {
            int j = ALEATORIO.nextInt(i + 1);
            char t = c[i];
            c[i] = c[j];
            c[j] = t;
        }
        return new String(c);
    }
}
