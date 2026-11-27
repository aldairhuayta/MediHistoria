package edu.uni.medihistoria.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/** Validaciones de entrada reutilizables (sin dependencias de JavaFX). */
public final class Validador {
    private static final Pattern USUARIO = Pattern.compile("^[a-z0-9._]{4,30}$");
    private static final Pattern DNI = Pattern.compile("^[0-9]{8}$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9+() -]{6,20}$");
    private static final Pattern DECIMAL = Pattern.compile("^[0-9]{1,3}([.,][0-9]{1,2})?$");
    private static final Pattern ENTERO = Pattern.compile("^[0-9]{1,3}$");
    private static final Pattern PRESION = Pattern.compile("^([0-9]{2,3})/([0-9]{2,3})$");

    private Validador() { }

    /** Entre 4 y 30 caracteres: minúsculas, números, punto o guion bajo. */
    public static boolean esUsuarioValido(String texto) {
        return texto != null && USUARIO.matcher(texto).matches();
    }

    /** Texto no vacío y de hasta {@code max} caracteres. */
    public static boolean esTextoValido(String texto, int max) {
        return texto != null && !texto.isBlank() && texto.trim().length() <= max;
    }

    /** DNI peruano: exactamente 8 dígitos. */
    public static boolean esDni(String texto) {
        return texto != null && DNI.matcher(texto).matches();
    }

    /** Teléfono opcional: vacío es válido; si se escribe, de 6 a 20 caracteres (números, espacios, + ( ) y guion). */
    public static boolean esTelefonoValido(String texto) {
        return texto == null || texto.isBlank() || TELEFONO.matcher(texto.trim()).matches();
    }

    /** Fecha de nacimiento: no futura y posterior a 1900. */
    public static boolean esFechaNacimientoValida(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now()) && fecha.isAfter(LocalDate.of(1900, 1, 1));
    }

    /** Convierte "36.5" o "36,5" en número; devuelve null si no es un decimal simple (hasta 3 enteros y 2 decimales). */
    public static Double parseDecimal(String texto) {
        if (texto == null || !DECIMAL.matcher(texto.trim()).matches()) {
            return null;
        }
        return Double.valueOf(texto.trim().replace(',', '.'));
    }

    /** Convierte un entero de hasta 3 dígitos; devuelve null si el texto no es válido. */
    public static Integer parseEntero(String texto) {
        if (texto == null || !ENTERO.matcher(texto.trim()).matches()) {
            return null;
        }
        return Integer.valueOf(texto.trim());
    }

    public static boolean esPesoValido(double kg) {
        return kg >= 1 && kg <= 400;
    }

    public static boolean esTallaValida(int cm) {
        return cm >= 30 && cm <= 260;
    }

    public static boolean esFrecuenciaCardiacaValida(int lpm) {
        return lpm >= 20 && lpm <= 250;
    }

    /** La base de datos exige entre 30 y 45 °C. */
    public static boolean esTemperaturaValida(double celsius) {
        return celsius >= 30 && celsius <= 45;
    }

    /** Formato sistólica/diastólica (ej. 120/80), con la sistólica mayor que la diastólica. */
    public static boolean esPresionArterial(String texto) {
        if (texto == null) {
            return false;
        }
        var m = PRESION.matcher(texto.trim());
        if (!m.matches()) {
            return false;
        }
        int sistolica = Integer.parseInt(m.group(1));
        int diastolica = Integer.parseInt(m.group(2));
        return sistolica >= 50 && sistolica <= 260 && diastolica >= 30 && diastolica <= 160 && sistolica > diastolica;
    }
}
