package com.tienda.ui;

import com.tienda.Usuario;
import com.tienda.Carrito;
import com.tienda.Producto;
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

/**
 * PantallaCarrito muestra los productos agregados al carrito.
 * Permite ver el total, descuentos aplicados y proceder con la compra.
 * Utiliza ColorTheme para mantener coherencia visual con toda la aplicación.
 */
public class PantallaCarrito {
    private Stage stage;
    private Usuario usuario;
    private Carrito carrito;

    public PantallaCarrito(Usuario usuario, Carrito carrito) {
        this.usuario = usuario;
        this.carrito = carrito;
    }

    /**
     * Muestra la pantalla del carrito.
     */
    public void mostrar(Stage primaryStage) {
        this.stage = primaryStage;

        BorderPane root = new BorderPane();
        root.setStyle(ColorTheme.ESTILO_FONDO);

        // Encabezado
        VBox encabezado = crearEncabezado();
        root.setTop(encabezado);

        // Contenido - Tabla de productos con scroll negro
        ScrollPane contenidoScroll = new ScrollPane();
        VBox contenido = crearContenidoCarrito();
        contenidoScroll.setContent(contenido);
        contenidoScroll.setFitToWidth(true);
        contenidoScroll.setStyle("-fx-control-inner-background: " + ColorTheme.GRIS_FONDO + "; " +
                                "-fx-background: " + ColorTheme.GRIS_FONDO + ";");
        root.setCenter(contenidoScroll);

        // Pie de página
        HBox piePagina = crearPiePagina();
        root.setBottom(piePagina);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("City Market - Carrito de Compras");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crea el encabezado.
     */
    private VBox crearEncabezado() {
        VBox encabezado = new VBox();
        encabezado.setStyle(ColorTheme.ESTILO_ENCABEZADO);
        encabezado.setPadding(new Insets(15));
        encabezado.setAlignment(Pos.CENTER);

        Label titulo = new Label("CARRITO DE COMPRAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setStyle("-fx-text-fill: white;");

        encabezado.getChildren().add(titulo);
        return encabezado;
    }

    /**
     * Crea el contenido principal del carrito.
     */
    private VBox crearContenidoCarrito() {
        VBox contenido = new VBox();
        contenido.setPadding(new Insets(20));
        contenido.setSpacing(15);
        contenido.setStyle("-fx-background-color: " + ColorTheme.GRIS_FONDO + ";");

        // Verificar si el carrito está vacío
        if (carrito.getContador() == 0) {
            Label labelVacio = new Label("El carrito esta vacio");
            labelVacio.setFont(Font.font("Arial", 18));
            labelVacio.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_GRIS + ";");
            contenido.setAlignment(Pos.CENTER);
            contenido.getChildren().add(labelVacio);
            return contenido;
        }

        // Tabla de productos
        TableView<ProductoCarrito> tablaProductos = new TableView<>();
        tablaProductos.setPrefHeight(350);
        tablaProductos.setStyle("-fx-background-color: " + ColorTheme.GRIS_CLARO + "; " +
                               "-fx-text-fill: " + ColorTheme.TEXTO_CLARO + "; " +
                               "-fx-border-color: " + ColorTheme.ROJO_PRINCIPAL + ";");

        // Columna: Producto
        TableColumn<ProductoCarrito, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().nombre));
        colProducto.setPrefWidth(250);
        estilizarColumnHeader(colProducto);

        // Columna: Categoría
        TableColumn<ProductoCarrito, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().categoria));
        colCategoria.setPrefWidth(120);
        estilizarColumnHeader(colCategoria);

        // Columna: Cantidad
        TableColumn<ProductoCarrito, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().cantidad));
        colCantidad.setPrefWidth(80);
        estilizarColumnHeader(colCantidad);

        // Columna: Precio Unitario
        TableColumn<ProductoCarrito, String> colPrecio = new TableColumn<>("Precio Unit.");
        colPrecio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", cellData.getValue().precio)));
        colPrecio.setPrefWidth(100);
        estilizarColumnHeader(colPrecio);

        // Columna: Subtotal
        TableColumn<ProductoCarrito, String> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", cellData.getValue().subtotal())));
        colSubtotal.setPrefWidth(100);
        estilizarColumnHeader(colSubtotal);

        // Columna: Descuento
        TableColumn<ProductoCarrito, String> colDescuento = new TableColumn<>("Descuento");
        colDescuento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().descuentoTexto()));
        colDescuento.setPrefWidth(80);
        estilizarColumnHeader(colDescuento);

        tablaProductos.getColumns().addAll(colProducto, colCategoria, colCantidad, colPrecio, colSubtotal, colDescuento);

        // Llenar tabla con productos del carrito
        Producto[] productosCarrito = carrito.getProductos();
        for (int i = 0; i < carrito.getContador(); i++) {
            Producto p = productosCarrito[i];
            int cantidad = p.getStock();
            double descuento = calcularDescuento(cantidad);
            tablaProductos.getItems().add(new ProductoCarrito(p.getNombre(), p.getCategoria(), cantidad, p.getPrecio(), descuento));
        }

        // Panel de totales
        VBox panelTotales = crearPanelTotales();

        contenido.getChildren().addAll(tablaProductos, panelTotales);
        return contenido;
    }

    /**
     * Estiliza los encabezados de las columnas de la tabla.
     */
    private void estilizarColumnHeader(TableColumn<?, ?> columna) {
        columna.setStyle("-fx-text-fill: " + ColorTheme.ROJO_PRINCIPAL + "; " +
                        "-fx-font-weight: bold;");
    }

    /**
     * Calcula el descuento según la cantidad.
     */
    private double calcularDescuento(int cantidad) {
        if (cantidad >= 3 && cantidad <= 4) return 0.05;
        else if (cantidad >= 5 && cantidad <= 6) return 0.10;
        else if (cantidad >= 7) return 0.15;
        return 0;
    }

    /**
     * Crea el panel de totales.
     */
    private VBox crearPanelTotales() {
        VBox panel = new VBox();
        panel.setStyle("-fx-border-color: " + ColorTheme.ROJO_PRINCIPAL + "; " +
                      "-fx-border-radius: 5; -fx-background-color: " + ColorTheme.GRIS_CLARO + ";");
        panel.setPadding(new Insets(15));
        panel.setSpacing(10);
        panel.setAlignment(Pos.CENTER_RIGHT);

        double totalSinDesc = 0;
        double totalConDesc = 0;

        Producto[] productos = carrito.getProductos();
        for (int i = 0; i < carrito.getContador(); i++) {
            Producto p = productos[i];
            int cantidad = p.getStock();
            double descuento = calcularDescuento(cantidad);
            double subtotal = p.getPrecio() * cantidad;
            totalSinDesc += subtotal;
            totalConDesc += subtotal * (1 - descuento);
        }

        Label labelTotalSin = new Label("Total sin descuento: $" + String.format("%.2f", totalSinDesc));
        labelTotalSin.setStyle("-fx-font-size: 14; -fx-text-fill: " + ColorTheme.TEXTO_CLARO + ";");

        Label labelTotalCon = new Label("Total con descuento: $" + String.format("%.2f", totalConDesc));
        labelTotalCon.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelTotalCon.setStyle("-fx-text-fill: " + ColorTheme.ROJO_PRINCIPAL + ";");

        panel.getChildren().addAll(labelTotalSin, labelTotalCon);
        return panel;
    }

    /**
     * Crea el pie de página con botones de navegación.
     */
    private HBox crearPiePagina() {
        HBox piePagina = new HBox();
        piePagina.setStyle(ColorTheme.ESTILO_PIE_PAGINA);
        piePagina.setPadding(new Insets(10));
        piePagina.setSpacing(10);
        piePagina.setAlignment(Pos.CENTER);

        Button btnVolver = new Button("Volver al Menu");
        btnVolver.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL);
        btnVolver.setPrefWidth(150);
        btnVolver.setOnAction(e -> {
            // Pasar el carrito existente a PantallaPrincipal
            PantallaPrincipal pantallaPrincipal = new PantallaPrincipal(usuario, carrito);
            pantallaPrincipal.mostrar(stage);
        });
        btnVolver.setOnMouseEntered(ev -> btnVolver.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL_HOVER));
        btnVolver.setOnMouseExited(ev -> btnVolver.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL));

        Button btnVaciar = new Button("Vaciar Carrito");
        btnVaciar.setPrefWidth(150);
        btnVaciar.setStyle("-fx-font-size: 12; -fx-padding: 8; " +
                          "-fx-background-color: " + ColorTheme.ROJO_OSCURO + "; " +
                          "-fx-text-fill: white; -fx-font-weight: bold;");
        btnVaciar.setOnAction(e -> {
            carrito.vaciarCarrito();
            mostrar(stage);
        });
        btnVaciar.setOnMouseEntered(ev -> btnVaciar.setStyle("-fx-font-size: 12; -fx-padding: 8; " +
                                                              "-fx-background-color: " + ColorTheme.NEGRO_OSCURO + "; " +
                                                              "-fx-text-fill: white; -fx-font-weight: bold;"));
        btnVaciar.setOnMouseExited(ev -> btnVaciar.setStyle("-fx-font-size: 12; -fx-padding: 8; " +
                                                            "-fx-background-color: " + ColorTheme.ROJO_OSCURO + "; " +
                                                            "-fx-text-fill: white; -fx-font-weight: bold;"));

        piePagina.getChildren().addAll(btnVolver, btnVaciar);
        return piePagina;
    }

    /**
     * Clase auxiliar para representar un producto en la tabla.
     */
    public static class ProductoCarrito {
        public String nombre;
        public String categoria;
        public int cantidad;
        public double precio;
        public double descuento;

        public ProductoCarrito(String nombre, String categoria, int cantidad, double precio, double descuento) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.cantidad = cantidad;
            this.precio = precio;
            this.descuento = descuento;
        }

        public double subtotal() {
            return precio * cantidad * (1 - descuento);
        }

        public String descuentoTexto() {
            return descuento > 0 ? (int)(descuento * 100) + "%" : "0%";
        }
    }
}