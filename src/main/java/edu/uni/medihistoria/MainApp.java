package edu.uni.medihistoria;

import edu.uni.medihistoria.util.Navegador;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Navegador.irA(stage, "Login.fxml", "MediHistoria - Iniciar sesión", 380, 360);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
