package com.tienda;

/**
 * Excepción personalizada para errores relacionados con la validación del correo electrónico.
 * Se lanza cuando el email del usuario no cumple con el formato válido.
 */
public class Invalidar_Email extends Exception {
    public Invalidar_Email(String mensaje) {
        super(mensaje);
    }

    public Invalidar_Email(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}


