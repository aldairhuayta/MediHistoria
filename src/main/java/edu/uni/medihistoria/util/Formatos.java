package edu.uni.medihistoria.util;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Formato de fechas dd/mm/aaaa para los selectores de fecha. */
public final class Formatos {
    public static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Formatos() { }

    public static StringConverter<LocalDate> convertidorFecha() {
        return new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate fecha) {
                return fecha == null ? "" : FECHA.format(fecha);
            }

            @Override
            public LocalDate fromString(String texto) {
                return parsear(texto);
            }
        };
    }

    /** Prepara un DatePicker para escribir o elegir fechas con formato dd/mm/aaaa. */
    public static void configurar(DatePicker selector) {
        selector.setConverter(convertidorFecha());
        selector.setPromptText("dd/mm/aaaa");
    }

    /** Lee la fecha escrita o elegida (aunque no se haya confirmado con Enter); null si está vacía o es inválida. */
    public static LocalDate leerFecha(DatePicker selector) {
        return parsear(selector.getEditor().getText());
    }

    private static LocalDate parsear(String texto) {
        try {
            return texto == null || texto.isBlank() ? null : LocalDate.parse(texto.trim(), FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
