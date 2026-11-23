package edu.uni.medihistoria.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Usuario del sistema. No contiene la contraseña ni el hash: eso queda solo en la capa de datos. */
public class Usuario {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int idUsuario;
    private final String usuario;
    private String nombres;
    private String apellidos;
    private Rol rol;
    private boolean activo;
    private final boolean bloqueado;
    private final boolean debeCambiarClave;
    private final LocalDateTime ultimoAcceso;

    public Usuario(int idUsuario, String usuario, String nombres, String apellidos, Rol rol,
                   boolean activo, boolean bloqueado, boolean debeCambiarClave, LocalDateTime ultimoAcceso) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rol = rol;
        this.activo = activo;
        this.bloqueado = bloqueado;
        this.debeCambiarClave = debeCambiarClave;
        this.ultimoAcceso = ultimoAcceso;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsuario() { return usuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public Rol getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public boolean isBloqueado() { return bloqueado; }
    public boolean isDebeCambiarClave() { return debeCambiarClave; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }

    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public String getEstado() {
        if (!activo) {
            return "Inactivo";
        }
        return bloqueado ? "Bloqueado" : "Activo";
    }

    public String getUltimoAccesoTexto() {
        return ultimoAcceso == null ? "Nunca" : ultimoAcceso.format(FORMATO);
    }
}
