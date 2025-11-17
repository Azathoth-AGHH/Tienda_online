package com.tienda;

/**
 * Excepción personalizada para errores de cantidad de producto.
 * Se lanza cuando la cantidad ingresada no es válida.
 */
public class CantidadInvalidaException extends Exception {
    public CantidadInvalidaException(String mensaje) {
        super(mensaje);
    }

    public CantidadInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}