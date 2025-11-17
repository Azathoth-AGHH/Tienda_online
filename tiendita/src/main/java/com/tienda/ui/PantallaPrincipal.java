package com.tienda.ui;

import com.tienda.Usuario;
import com.tienda.Carrito;
import com.tienda.Producto;
import com.tienda.GeneradorPDF;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PantallaPrincipal muestra el menú principal de la tienda con opciones para:
 * - Ver el catálogo
 * - Ver el carrito
 * - Finalizar compra
 */
public class PantallaPrincipal {
    private Stage stage;
    private Usuario usuario;
    private Carrito carrito;

    /**
     * Constructor que recibe un usuario y un carrito existente
     */
    public PantallaPrincipal(Usuario usuario, Carrito carrito) {
        this.usuario = usuario;
        this.carrito = carrito;
    }

    /**
     * Constructor que recibe solo un usuario (crea carrito nuevo)
     */
    public PantallaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.carrito = new Carrito(20);
    }

    /**
     * Muestra la pantalla principal.
     */
    public void mostrar(Stage primaryStage) {
        this.stage = primaryStage;

        // Crear el layout principal
        BorderPane root = new BorderPane();
        root.setStyle(ColorTheme.ESTILO_FONDO);

        // Encabezado
        VBox encabezado = crearEncabezado();
        root.setTop(encabezado);

        // Contenido central
        VBox contenido = crearContenidoPrincipal();
        root.setCenter(contenido);

        // Pie de página con datos del usuario
        HBox piePagina = crearPiePagina();
        root.setBottom(piePagina);

        // Crear la escena
        Scene scene = new Scene(root, 800, 600);

        // Configurar el stage
        stage.setTitle("City Market - Menu Principal");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crea el encabezado de la pantalla.
     */
    private VBox crearEncabezado() {
        VBox encabezado = new VBox();
        encabezado.setStyle(ColorTheme.ESTILO_ENCABEZADO);
        encabezado.setPadding(new Insets(20));
        encabezado.setAlignment(Pos.CENTER);

        Label titulo = new Label("CITY MARKET");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titulo.setStyle("-fx-text-fill: white;");

        Label subtitulo = new Label("Tu tienda online de confianza");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_GRIS + ";");

        encabezado.getChildren().addAll(titulo, subtitulo);
        return encabezado;
    }

    /**
     * Crea el contenido principal con los botones del menú.
     * LÍNEA 51 - CORREGIDO: Se agregó manejo de excepciones al finalizar compra usando ColorTheme
     */
    private VBox crearContenidoPrincipal() {
        VBox contenido = new VBox();
        contenido.setPadding(new Insets(60));
        contenido.setSpacing(20);
        contenido.setAlignment(Pos.CENTER);

        Label labelMenu = new Label("Menu Principal");
        labelMenu.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        labelMenu.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + ";");

        // Botón Ver Catálogo
        Button btnCatalogo = crearBoton("Ver Catalogo", 250);
        btnCatalogo.setOnAction(e -> {
            // Pasar el carrito existente a PantallaCatalogo
            PantallaCatalogo pantallaCatalogo = new PantallaCatalogo(usuario, carrito);
            pantallaCatalogo.mostrar(stage);
        });

        // Botón Ver Carrito
        Button btnCarrito = crearBoton("Ver Carrito", 250);
        btnCarrito.setOnAction(e -> {
            // Pasar el carrito existente a PantallaCarrito
            PantallaCarrito pantallaCarrito = new PantallaCarrito(usuario, carrito);
            pantallaCarrito.mostrar(stage);
        });

        // Botón Finalizar Compra
        Button btnFinalizar = crearBoton("Finalizar Compra", 250);
        btnFinalizar.setOnAction(e -> {
            if (carrito.getContador() == 0) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Carrito Vacio");
                alerta.setHeaderText("No hay productos");
                alerta.setContentText("Debes agregar productos antes de finalizar la compra.");
                alerta.showAndWait();
            } else {
                // LÍNEA 51 - CORREGIDO: Try-catch para manejar excepciones usando ColorTheme
                try {
                    // Mostrar el ticket en consola
                    carrito.imprimirTicket(usuario);
                    
                    // Preparar datos para generar PDF
                    List<GeneradorPDF.Producto> productosPDF = new ArrayList<>();
                    Producto[] productosCarrito = carrito.getProductos();

                    for (Producto p : productosCarrito) {
                        if (p == null) continue;

                        int cantidad = p.getStock();
                        double descuento = 0;
                        if (cantidad >= 3 && cantidad <= 4) descuento = 0.05;
                        else if (cantidad >= 5 && cantidad <= 6) descuento = 0.10;
                        else if (cantidad >= 7) descuento = 0.15;

                        productosPDF.add(new GeneradorPDF.Producto(
                            p.getNombre(),
                            p.getCategoria(),
                            p.getSubcategoria(),
                            String.valueOf(cantidad),
                            p.getPrecio(),
                            descuento
                        ));
                    }

                    // Intentar generar el PDF
                    try {
                        GeneradorPDF.generarTicket(
                            "ticket.pdf",
                            "City Market",
                            usuario,
                            "tiendita/src/logo.png",
                            productosPDF
                        );
                        System.out.println("PDF generado correctamente en ticket.pdf");
                    } catch (IOException ioException) {
                        System.err.println("Error al generar PDF: " + ioException.getMessage());
                        // El PDF puede fallar, pero continuamos con el proceso
                    }
                    
                    // Mostrar alerta de éxito
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Compra Finalizada");
                    alerta.setHeaderText("Gracias por su compra");
                    alerta.setContentText("Tu compra ha sido procesada exitosamente.\nTotal: $" 
                        + String.format("%.2f", carrito.calcularTotal()));
                    alerta.showAndWait();

                    // Vaciar carrito y volver al menú
                    carrito.vaciarCarrito();
                    mostrar(stage);
                    
                } catch (Exception ex) {
                    // Capturar cualquier otra excepción inesperada
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Error");
                    alerta.setHeaderText("Error al procesar compra");
                    alerta.setContentText("Ocurrió un error: " + ex.getMessage());
                    alerta.showAndWait();
                    ex.printStackTrace();
                }
            }
        });

        // Botón Salir
        Button btnSalir = crearBoton("Salir", 250);
        btnSalir.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: " + 
                         ColorTheme.ROJO_OSCURO + "; -fx-text-fill: white;");
        btnSalir.setOnMouseEntered(e -> btnSalir.setStyle("-fx-font-size: 14; -fx-font-weight: bold; " +
                                                          "-fx-background-color: " + ColorTheme.NEGRO_OSCURO + 
                                                          "; -fx-text-fill: white;"));
        btnSalir.setOnMouseExited(e -> btnSalir.setStyle("-fx-font-size: 14; -fx-font-weight: bold; " +
                                                         "-fx-background-color: " + ColorTheme.ROJO_OSCURO + 
                                                         "; -fx-text-fill: white;"));
        btnSalir.setOnAction(e -> stage.close());

        contenido.getChildren().addAll(
                labelMenu,
                new Separator(),
                btnCatalogo,
                btnCarrito,
                btnFinalizar,
                btnSalir
        );

        return contenido;
    }

    /**
     * Crea el pie de página con información del usuario.
     */
    private HBox crearPiePagina() {
        HBox piePagina = new HBox();
        piePagina.setStyle(ColorTheme.ESTILO_PIE_PAGINA);
        piePagina.setPadding(new Insets(15));
        piePagina.setSpacing(40);

        Label labelUsuario = new Label("Usuario: " + usuario.getNombre());
        labelUsuario.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; -fx-font-size: 12;");

        Label labelEmail = new Label("Email: " + usuario.getEmail());
        labelEmail.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; -fx-font-size: 12;");

        Label labelDireccion = new Label("Direccion: " + usuario.getDireccion());
        labelDireccion.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; -fx-font-size: 12;");

        piePagina.getChildren().addAll(labelUsuario, labelEmail, labelDireccion);
        return piePagina;
    }

    /**
     * Método auxiliar para crear botones con estilo consistente.
     */
    private Button crearBoton(String texto, int ancho) {
        Button boton = new Button(texto);
        boton.setPrefHeight(50);
        boton.setPrefWidth(ancho);
        boton.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL);
        boton.setOnMouseEntered(e -> boton.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL_HOVER));
        boton.setOnMouseExited(e -> boton.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL));
        return boton;
    }

    /**
     * Obtiene el carrito actual
     */
    public Carrito getCarrito() {
        return carrito;
    }
}