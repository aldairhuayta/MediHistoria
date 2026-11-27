package edu.uni.medihistoria.datos;

import edu.uni.medihistoria.modelo.Usuario;

/** Datos de autenticación de un usuario tal como están en la base de datos. Uso interno de la capa de seguridad. */
public record Credencial(Usuario usuario, String hash, String sal, int intentosFallidos) { }
