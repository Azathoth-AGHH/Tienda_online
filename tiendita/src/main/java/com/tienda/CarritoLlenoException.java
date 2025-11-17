package com.tienda;

/**
 * Excepción personalizada para errores de carrito.
 * Se lanza cuando hay problemas al agregar productos al carrito.
 */
public class CarritoLlenoException extends Exception {
    public CarritoLlenoException(String mensaje) {
        super(mensaje);
    }

    public CarritoLlenoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}