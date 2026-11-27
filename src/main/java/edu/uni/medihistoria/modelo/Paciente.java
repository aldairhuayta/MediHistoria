package edu.uni.medihistoria.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/** Paciente del consultorio. En memoria los datos están en claro; en la base de datos los sensibles van cifrados. */
public class Paciente {
    private final int idPaciente;
    private final String dni;          // no se modifica una vez registrado
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;               // "M" o "F"
    private String telefono;           // opcional
    private String direccion;          // opcional
    private String alergias;           // opcional
    private final LocalDateTime fechaRegistro;

    public Paciente(int idPaciente, String dni, String nombres, String apellidos, LocalDate fechaNacimiento,
                    String sexo, String telefono, String direccion, String alergias, LocalDateTime fechaRegistro) {
        this.idPaciente = idPaciente;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.alergias = alergias;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdPaciente() { return idPaciente; }
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getSexo() { return sexo; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getAlergias() { return alergias; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getNombreCompleto() {
        return apellidos + ", " + nombres;
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getSexoEtiqueta() {
        return "M".equals(sexo) ? "Masculino" : "Femenino";
    }

    /** DNI parcialmente oculto para listados (se ve completo solo al abrir la ficha). */
    public String getDniEnmascarado() {
        return dni != null && dni.length() == 8 ? "******" + dni.substring(6) : "********";
    }
}
