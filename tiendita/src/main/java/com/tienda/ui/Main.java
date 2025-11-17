package com.tienda.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase Main que inicia la aplicación JavaFX.
 * Esta es la clase principal de la interfaz gráfica.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear la pantalla de registro como primera pantalla
        PantallaRegistro pantallaRegistro = new PantallaRegistro();
        
        // Mostrar la pantalla de registro
        pantallaRegistro.mostrar(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}