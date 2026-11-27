package edu.uni.medihistoria;

/**
 * Punto de entrada. Se usa una clase aparte (que no extiende Application) para que JavaFX
 * arranque bien desde IntelliJ y desde el .jar cuando las librerías van en el classpath.
 */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}
