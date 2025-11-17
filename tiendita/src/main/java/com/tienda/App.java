package com.tienda;

/**
 * La clase `App` sirve como punto de entrada para la aplicación de la tienda.
 * Es la clase principal que contiene el método `main`, desde donde se inicia y se controla el flujo de ejecución de todo el programa.
 */
public class App {

    /**
     * El método `main` es el punto de inicio de la aplicación.
     * Su única responsabilidad es crear una instancia de la clase `Controlador` e invocar el método que inicia el proceso de compra.
     */
    public static void main(String[] args) {
        // Se muestra un mensaje de bienvenida al usuario para indicar el inicio del programa.
        System.out.println("=== Bienvenido a City Market ===\n");

        // Se crea una instancia de la clase `Controlador`. Esta instancia es la que gestionará toda la lógica de negocio de la aplicación, como el registro, la visualización del catálogo y la finalización de la compra.
        Controlador controlador = new Controlador();

        // Se llama al método `iniciarCompra()` del controlador, lo que da inicio a la interacción con el usuario, comenzando por el registro y seguido por el menú principal.
        controlador.iniciarCompra();
    }
}