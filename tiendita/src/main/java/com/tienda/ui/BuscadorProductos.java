package com.tienda.ui;

import com.tienda.Carrito;
import com.tienda.Producto;
import com.tienda.Invalidar_Email;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Popup;
import java.util.ArrayList;
import java.util.List;

/**
 * BuscadorProductos proporciona un sistema de búsqueda con sugerencias desplegables.
 * Utiliza un ListView flotante que aparece automáticamente mientras el usuario escribe.
 */
public class BuscadorProductos {
    private Producto[] catalogo;
    private Carrito carrito;
    private ListView<String> listaSugerencias;
    private Popup popupSugerencias;
    private List<Producto> productosActuales;
    private TextField campoTexto;

    /**
     * Constructor del BuscadorProductos.
     * 
     * @param catalogo Array de productos disponibles
     * @param carrito Carrito para agregar productos
     */
    public BuscadorProductos(Producto[] catalogo, Carrito carrito) {
        this.catalogo = catalogo;
        this.carrito = carrito;
        this.productosActuales = new ArrayList<>();
        this.popupSugerencias = new Popup();
    }

    /**
     * Obtiene el HBox con el campo de búsqueda configurado.
     * 
     * @return HBox con el campo de búsqueda
     */
    public HBox crearBarraBusqueda() {
        HBox barra = new HBox();
        barra.setStyle("-fx-background-color: " + ColorTheme.NEGRO_PRINCIPAL + ";");
        barra.setPadding(new Insets(15, 20, 15, 20));
        barra.setSpacing(10);
        barra.setAlignment(Pos.CENTER_LEFT);

        // TextField para búsqueda
        campoTexto = new TextField();
        campoTexto.setPromptText("Buscar productos...");
        campoTexto.setPrefHeight(40);
        campoTexto.setPrefWidth(600);
        campoTexto.setStyle(ColorTheme.ESTILO_TEXTFIELD);
        
        // Crear ListView para sugerencias
        listaSugerencias = new ListView<>();
        listaSugerencias.setPrefHeight(300);
        listaSugerencias.setPrefWidth(600);
        listaSugerencias.setStyle("-fx-background-color: " + ColorTheme.GRIS_CLARO + "; " +
                                  "-fx-border-color: " + ColorTheme.ROJO_PRINCIPAL + "; " +
                                  "-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; " +
                                  "-fx-font-size: 12; " +
                                  "-fx-border-width: 2;");
        
        // Escuchar cambios en el TextField
        campoTexto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                ocultarSugerencias();
            } else {
                actualizarSugerencias(newValue);
            }
        });
        
        // Evento cuando se selecciona un producto del ListView
        listaSugerencias.setOnMouseClicked(event -> {
            int indice = listaSugerencias.getSelectionModel().getSelectedIndex();
            if (indice >= 0 && indice < productosActuales.size()) {
                Producto seleccionado = productosActuales.get(indice);
                mostrarDetallesProducto(seleccionado);
                campoTexto.clear();
                ocultarSugerencias();
            }
        });
        
        // Permitir navegar con teclado
        campoTexto.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("DOWN")) {
                if (popupSugerencias.isShowing()) {
                    listaSugerencias.requestFocus();
                    if (listaSugerencias.getItems().size() > 0) {
                        listaSugerencias.getSelectionModel().select(0);
                    }
                    event.consume();
                }
            } else if (event.getCode().toString().equals("ESCAPE")) {
                ocultarSugerencias();
                event.consume();
            }
        });
        
        // Teclas en el ListView
        listaSugerencias.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                int indice = listaSugerencias.getSelectionModel().getSelectedIndex();
                if (indice >= 0 && indice < productosActuales.size()) {
                    Producto seleccionado = productosActuales.get(indice);
                    mostrarDetallesProducto(seleccionado);
                    campoTexto.clear();
                    ocultarSugerencias();
                }
                event.consume();
            } else if (event.getCode().toString().equals("ESCAPE")) {
                ocultarSugerencias();
                campoTexto.requestFocus();
                event.consume();
            }
        });

        // LÍNEA 129 - CORREGIDO: Usar getContent().add() en lugar de setContent()
        popupSugerencias.getContent().add(listaSugerencias);
        
        barra.getChildren().add(campoTexto);
        return barra;
    }

    /**
     * Actualiza las sugerencias según el texto ingresado.
     * 
     * @param termino Texto ingresado por el usuario
     */
    private void actualizarSugerencias(String termino) {
        String terminoBusqueda = termino.toLowerCase().trim();
        List<Producto> resultados = buscarProductos(terminoBusqueda);
        
        if (resultados.isEmpty()) {
            ocultarSugerencias();
            return;
        }
        
        this.productosActuales = resultados;
        
        listaSugerencias.getItems().clear();
        listaSugerencias.getSelectionModel().clearSelection();
        
        for (Producto p : resultados) {
            listaSugerencias.getItems().add(p.getNombre() + " - $" + String.format("%.2f", p.getPrecio()));
        }
        
        mostrarSugerencias();
    }

    /**
     * Busca productos que coincidan con el término.
     * 
     * @param termino Término de búsqueda
     * @return Lista de productos encontrados
     */
    private List<Producto> buscarProductos(String termino) {
        List<Producto> resultados = new ArrayList<>();
        
        for (int i = 0; i < catalogo.length; i++) {
            Producto p = catalogo[i];
            if (p != null && (p.getNombre().toLowerCase().contains(termino) || 
                             p.getSubcategoria().toLowerCase().contains(termino) ||
                             p.getCategoria().toLowerCase().contains(termino))) {
                resultados.add(p);
            }
        }
        
        return resultados;
    }

    /**
     * Muestra el popup de sugerencias.
     * LÍNEA 129 - CORREGIDO: Uso de coordenadas locales de escena
     */
    private void mostrarSugerencias() {
        if (!popupSugerencias.isShowing()) {
            try {
                // Verificar que el campo de texto esté en una escena y ventana
                if (campoTexto.getScene() != null && campoTexto.getScene().getWindow() != null) {
                    // Obtener coordenadas locales del campo de texto en su escena
                    Bounds boundsInScene = campoTexto.localToScene(campoTexto.getBoundsInLocal());
                    
                    // Obtener la ventana
                    javafx.stage.Window window = campoTexto.getScene().getWindow();
                    
                    // Calcular posición en pantalla sumando las coordenadas de la ventana
                    double x = window.getX() + boundsInScene.getMinX() + campoTexto.getScene().getX();
                    double y = window.getY() + boundsInScene.getMaxY() + campoTexto.getScene().getY();
                    
                    // Mostrar el popup
                    popupSugerencias.show(window, x, y);
                }
            } catch (Exception e) {
                System.err.println("Error mostrando sugerencias: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Oculta el popup de sugerencias.
     */
    private void ocultarSugerencias() {
        if (popupSugerencias.isShowing()) {
            popupSugerencias.hide();
        }
    }

    /**
     * Muestra una ventana con detalles del producto.
     * 
     * @param producto Producto seleccionado
     */
    private void mostrarDetallesProducto(Producto producto) {
        Stage ventanaDetalles = new Stage();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + ColorTheme.GRIS_CLARO + ";");
        
        // Encabezado
        VBox encabezado = new VBox();
        encabezado.setStyle(ColorTheme.ESTILO_ENCABEZADO);
        encabezado.setPadding(new Insets(15));
        encabezado.setAlignment(Pos.CENTER);
        
        Label titulo = new Label(producto.getNombre());
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setStyle("-fx-text-fill: white;");
        
        Label categoria = new Label(producto.getCategoria() + " > " + producto.getSubcategoria());
        categoria.setFont(Font.font("Arial", 12));
        categoria.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_GRIS + ";");
        
        encabezado.getChildren().addAll(titulo, categoria);
        root.setTop(encabezado);
        
        // Contenido principal
        VBox contenido = new VBox();
        contenido.setPadding(new Insets(30));
        contenido.setSpacing(15);
        contenido.setAlignment(Pos.CENTER);
        contenido.setStyle("-fx-background-color: " + ColorTheme.GRIS_CLARO + ";");
        
        // Área de imagen
        VBox imagenBox = new VBox();
        imagenBox.setStyle("-fx-background-color: " + ColorTheme.NEGRO_PRINCIPAL + "; " +
                          "-fx-border-color: " + ColorTheme.ROJO_PRINCIPAL + ";");
        imagenBox.setPrefHeight(200);
        imagenBox.setAlignment(Pos.CENTER);
        
        Label imagenLabel = new Label("📦");
        imagenLabel.setFont(Font.font("Arial", 100));
        imagenBox.getChildren().add(imagenLabel);
        
        // Información del producto
        VBox infoBox = new VBox();
        infoBox.setSpacing(10);
        
        Label labelPrecio = new Label("Precio: $" + String.format("%.2f", producto.getPrecio()));
        labelPrecio.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        labelPrecio.setStyle("-fx-text-fill: " + ColorTheme.ROJO_PRINCIPAL + ";");
        
        Label labelStock = new Label("Stock: Disponible");
        labelStock.setFont(Font.font("Arial", 14));
        labelStock.setStyle("-fx-text-fill: " + ColorTheme.VERDE_EXITO + ";");
        
        infoBox.getChildren().addAll(labelPrecio, labelStock);
        
        // Selector de cantidad
        HBox cantidadBox = new HBox();
        cantidadBox.setSpacing(10);
        cantidadBox.setAlignment(Pos.CENTER);
        
        Label labelCantidad = new Label("Cantidad:");
        labelCantidad.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; -fx-font-size: 14;");
        
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        spinner.setPrefWidth(100);
        
        cantidadBox.getChildren().addAll(labelCantidad, spinner);
        
        contenido.getChildren().addAll(imagenBox, infoBox, cantidadBox);
        root.setCenter(contenido);
        
        // Pie de página
        HBox piePagina = new HBox();
        piePagina.setStyle(ColorTheme.ESTILO_PIE_PAGINA);
        piePagina.setPadding(new Insets(15));
        piePagina.setSpacing(10);
        piePagina.setAlignment(Pos.CENTER);
        
        Button btnAgregar = new Button("Agregar al Carrito");
        btnAgregar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL);
        btnAgregar.setPrefWidth(200);
        btnAgregar.setOnAction(e -> {
            int cantidad = spinner.getValue();
            agregarProductoAlCarrito(producto, cantidad);
            ventanaDetalles.close();
        });
        btnAgregar.setOnMouseEntered(e -> btnAgregar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL_HOVER));
        btnAgregar.setOnMouseExited(e -> btnAgregar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL));
        
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-font-size: 12; -fx-padding: 10; " +
                            "-fx-background-color: " + ColorTheme.ROJO_OSCURO + "; " +
                            "-fx-text-fill: white;");
        btnCancelar.setPrefWidth(150);
        btnCancelar.setOnAction(e -> ventanaDetalles.close());
        btnCancelar.setOnMouseEntered(e -> btnCancelar.setStyle("-fx-font-size: 12; -fx-padding: 10; " +
                                                                "-fx-background-color: " + ColorTheme.NEGRO_OSCURO + "; " +
                                                                "-fx-text-fill: white;"));
        btnCancelar.setOnMouseExited(e -> btnCancelar.setStyle("-fx-font-size: 12; -fx-padding: 10; " +
                                                               "-fx-background-color: " + ColorTheme.ROJO_OSCURO + "; " +
                                                               "-fx-text-fill: white;"));
        
        piePagina.getChildren().addAll(btnAgregar, btnCancelar);
        root.setBottom(piePagina);
        
        Scene scene = new Scene(root, 600, 500);
        ventanaDetalles.setScene(scene);
        ventanaDetalles.setTitle("Detalles del Producto");
        ventanaDetalles.setResizable(false);
        ventanaDetalles.show();
    }

    /**
     * Agrega un producto al carrito.
     * 
     * @param producto Producto a agregar
     * @param cantidad Cantidad a agregar
     */
    private void agregarProductoAlCarrito(Producto producto, int cantidad) {
        if (cantidad <= 0 || cantidad > 100) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                         "Cantidad inválida", 
                         "La cantidad debe estar entre 1 y 100 unidades.");
            return;
        }

        try {
            Producto nuevoProducto = new Producto(
                    producto.getNombre(),
                    producto.getCategoria(),
                    producto.getSubcategoria(),
                    producto.getPrecio(),
                    cantidad
            );

            carrito.agregarProducto(nuevoProducto);
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", 
                         "Producto agregado", 
                         producto.getNombre() + " ha sido agregado al carrito.");

        } catch (Invalidar_Email ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", 
                         "Error al agregar", ex.getMessage());
        }
    }

    /**
     * Muestra una alerta al usuario.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}