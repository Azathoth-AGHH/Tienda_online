package com.tienda;

/**
 * Excepción personalizada para errores de selección de producto.
 * Se lanza cuando el usuario selecciona un producto que no existe en el catálogo.
 */
public class ProductoNoEncontradoException extends Exception {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public ProductoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}