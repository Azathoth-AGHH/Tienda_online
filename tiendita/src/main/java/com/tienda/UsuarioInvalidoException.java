package com.tienda;

/**
 * Excepción personalizada para errores de validación de usuario.
 * Se lanza cuando los datos del usuario son inválidos o incompletos.
 */
public class UsuarioInvalidoException extends Exception {
    public UsuarioInvalidoException(String mensaje) {
        super(mensaje);
    }

    public UsuarioInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}