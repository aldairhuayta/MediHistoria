package edu.uni.medihistoria.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/** Carga de vistas FXML y cambio de pantalla dentro de la misma ventana. */
public final class Navegador {
    private static final String BASE = "/edu/uni/medihistoria/vista/";

    private Navegador() { }

    public static FXMLLoader loader(String fxml) {
        return new FXMLLoader(Navegador.class.getResource(BASE + fxml));
    }

    public static Parent cargar(String fxml) throws IOException {
        return loader(fxml).load();
    }

    /** Reemplaza la escena del Stage (no abre una ventana nueva). */
    public static void irA(Stage stage, String fxml, String titulo, double ancho, double alto) throws IOException {
        Parent raiz = cargar(fxml);
        Scene escena = new Scene(raiz, ancho, alto);
        aplicarEstilos(escena);
        stage.setScene(escena);
        stage.setTitle(titulo);
        stage.centerOnScreen();
    }

    public static void aplicarEstilos(Scene escena) {
        URL css = Navegador.class.getResource(BASE + "estilos.css");
        if (css != null) {
            escena.getStylesheets().add(css.toExternalForm());
        }
    }
}
