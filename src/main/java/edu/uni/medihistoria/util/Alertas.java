package edu.uni.medihistoria.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import java.util.Optional;

/** Ventanas de mensaje. Los errores muestran un texto genérico; el detalle técnico va a la consola. */
public final class Alertas {
    private Alertas() { }

    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Información", mensaje);
    }

    public static void advertencia(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "Atención", mensaje);
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, "Error", mensaje);
    }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK, ButtonType.CANCEL);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    /** Pide un texto corto. Devuelve vacío si el usuario cancela (puede devolver texto en blanco si acepta sin escribir). */
    public static Optional<String> pedirTexto(String titulo, String mensaje) {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        return dialogo.showAndWait();
    }

    /** Muestra la contraseña temporal en un campo seleccionable (se ve una sola vez). */
    public static void claveTemporal(String usuario, String clave) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Contraseña temporal");
        alerta.setHeaderText("Contraseña temporal para " + usuario);
        TextField campo = new TextField(clave);
        campo.setEditable(false);
        Label aviso = new Label("Entréguela al usuario. Deberá cambiarla en su primer ingreso.\n"
                + "Esta contraseña no se volverá a mostrar.");
        alerta.getDialogPane().setContent(new VBox(10, aviso, campo));
        alerta.showAndWait();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
