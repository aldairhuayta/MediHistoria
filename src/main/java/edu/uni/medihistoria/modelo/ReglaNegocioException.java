package edu.uni.medihistoria.modelo;

/** Se incumple una regla del negocio (ej. horario ocupado). El mensaje es apto para mostrarse al usuario. */
public class ReglaNegocioException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
