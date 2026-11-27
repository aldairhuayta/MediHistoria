package edu.uni.medihistoria.controlador;

import edu.uni.medihistoria.seguridad.AutenticacionServicio;
import edu.uni.medihistoria.seguridad.Sesion;
import edu.uni.medihistoria.util.Alertas;
import edu.uni.medihistoria.util.Navegador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;

public class CambiarClaveController {
    @FXML private Label lblAviso;
    @FXML private Label lblMensaje;
    @FXML private PasswordField txtActual;
    @FXML private PasswordField txtNueva;
    @FXML private PasswordField txtConfirmar;

    private final AutenticacionServicio autenticacion = new AutenticacionServicio();
    private boolean exito = false;

    /** Abre la ventana como diálogo modal y devuelve true si la contraseña se cambió. */
    public static boolean mostrar(Window propietario, boolean obligatorio) {
        try {
            FXMLLoader loader = Navegador.loader("CambiarClave.fxml");
            Parent raiz = loader.load();
            CambiarClaveController controlador = loader.getController();
            controlador.configurar(obligatorio);

            Stage dialogo = new Stage();
            dialogo.initOwner(propietario);
            dialogo.initModality(Modality.APPLICATION_MODAL);
            dialogo.setTitle("Cambiar contraseña");
            Scene escena = new Scene(raiz);
            Navegador.aplicarEstilos(escena);
            dialogo.setScene(escena);
            dialogo.setResizable(false);
            dialogo.showAndWait();
            return controlador.exito;
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.error("No se pudo abrir la ventana de cambio de contraseña.");
            return false;
        }
    }

    private void configurar(boolean obligatorio) {
        lblAviso.setText(obligatorio
                ? "Por seguridad, debe cambiar su contraseña inicial antes de continuar."
                : "Ingrese su contraseña actual y elija una nueva.");
    }

    @FXML
    private void onAceptar() {
        String actual = txtActual.getText();
        String nueva = txtNueva.getText();
        String confirmar = txtConfirmar.getText();

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            lblMensaje.setText("Complete los tres campos.");
            return;
        }
        if (!nueva.equals(confirmar)) {
            lblMensaje.setText("La confirmación no coincide con la nueva contraseña.");
            return;
        }
        try {
            autenticacion.cambiarClave(Sesion.getUsuario().getUsuario(), actual, nueva);
            exito = true;
            cerrar();
        } catch (AutenticacionServicio.ClaveInvalidaException e) {
            lblMensaje.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensaje.setText("No se pudo actualizar la contraseña. Intente nuevamente.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void cerrar() {
        ((Stage) txtNueva.getScene().getWindow()).close();
    }
}
