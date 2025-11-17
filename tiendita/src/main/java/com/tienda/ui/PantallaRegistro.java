package com.tienda.ui;

import com.tienda.Usuario;
import com.tienda.Invalidar_Email;
import com.tienda.UsuarioInvalidoException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.regex.Pattern;

/**
 * PantallaRegistro crea la interfaz gráfica para el registro de usuarios.
 * Valida los datos del usuario antes de permitir el acceso a la tienda.
 * Utiliza ColorTheme para mantener coherencia visual con toda la aplicación.
 */
public class PantallaRegistro {
    private Stage stage;
    private Usuario usuarioRegistrado;
    
    // Patrón regex para validar emails
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Valida si el email cumple con un formato válido.
     */
    private boolean esEmailValido(String email) {
        return email != null && pattern.matcher(email).matches() && email.contains(".");
    }

    /**
     * Muestra la pantalla de registro en el Stage proporcionado.
     */
    public void mostrar(Stage primaryStage) {
        this.stage = primaryStage;

        // Crear el layout principal
        BorderPane root = new BorderPane();
        root.setStyle(ColorTheme.ESTILO_FONDO);

        // Encabezado
        VBox encabezado = crearEncabezado();
        root.setTop(encabezado);

        // Contenido central con scroll
        ScrollPane contenidoScroll = new ScrollPane();
        VBox contenido = crearContenidoRegistro();
        contenidoScroll.setContent(contenido);
        contenidoScroll.setFitToWidth(true);
        contenidoScroll.setStyle("-fx-control-inner-background: " + ColorTheme.GRIS_FONDO + "; " +
                                "-fx-background: " + ColorTheme.GRIS_FONDO + ";");
        root.setCenter(contenidoScroll);

        // Crear la escena
        Scene scene = new Scene(root, 600, 700);

        // Configurar el stage
        stage.setTitle("City Market - Registro de Usuario");
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

        Label subtitulo = new Label("Bienvenido a nuestra tienda online");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_GRIS + ";");

        encabezado.getChildren().addAll(titulo, subtitulo);
        return encabezado;
    }

    /**
     * Crea el contenido del formulario de registro.
     */
    private VBox crearContenidoRegistro() {
        VBox contenido = new VBox();
        contenido.setPadding(new Insets(40));
        contenido.setSpacing(15);
        contenido.setAlignment(Pos.TOP_CENTER);
        contenido.setStyle("-fx-background-color: " + ColorTheme.NEGRO_PRINCIPAL + ";");

        // Título del formulario
        Label labelRegistro = new Label("Formulario de Registro");
        labelRegistro.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        labelRegistro.setStyle("-fx-text-fill: " + ColorTheme.ROJO_PRINCIPAL + ";");

        // Separador
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: " + ColorTheme.ROJO_PRINCIPAL + ";");

        // Campo de nombre
        Label labelNombre = new Label("Nombre:");
        labelNombre.setFont(Font.font("Arial", 12));
        labelNombre.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + ";");
        
        TextField textNombre = new TextField();
        textNombre.setPromptText("Ingrese su nombre completo");
        textNombre.setPrefHeight(35);
        textNombre.setStyle(ColorTheme.ESTILO_TEXTFIELD);

        // Campo de email
        Label labelEmail = new Label("Email:");
        labelEmail.setFont(Font.font("Arial", 12));
        labelEmail.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + ";");
        
        TextField textEmail = new TextField();
        textEmail.setPromptText("ejemplo@correo.com");
        textEmail.setPrefHeight(35);
        textEmail.setStyle(ColorTheme.ESTILO_TEXTFIELD);

        // Campo de dirección
        Label labelDireccion = new Label("Direccion:");
        labelDireccion.setFont(Font.font("Arial", 12));
        labelDireccion.setStyle("-fx-text-fill: " + ColorTheme.TEXTO_CLARO + ";");
        
        TextField textDireccion = new TextField();
        textDireccion.setPromptText("Ingrese su direccion completa");
        textDireccion.setPrefHeight(35);
        textDireccion.setStyle(ColorTheme.ESTILO_TEXTFIELD);

        // Label para mostrar mensajes de error
        Label labelError = new Label();
        labelError.setStyle("-fx-text-fill: " + ColorTheme.ROJO_CLARO + "; -fx-font-size: 11;");
        labelError.setWrapText(true);

        // Espaciador
        VBox espaciador = new VBox();
        VBox.setVgrow(espaciador, javafx.scene.layout.Priority.ALWAYS);

        // Botón de registro
        Button btnRegistrar = new Button("REGISTRARSE");
        btnRegistrar.setPrefHeight(40);
        btnRegistrar.setPrefWidth(200);
        btnRegistrar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL);
        btnRegistrar.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        btnRegistrar.setOnMouseEntered(e -> btnRegistrar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL_HOVER));
        btnRegistrar.setOnMouseExited(e -> btnRegistrar.setStyle(ColorTheme.ESTILO_BOTON_PRINCIPAL));

        // Acción del botón de registro
        btnRegistrar.setOnAction(e -> {
            String nombre = textNombre.getText().trim();
            String email = textEmail.getText().trim();
            String direccion = textDireccion.getText().trim();

            // Limpiar mensaje de error previo
            labelError.setText("");

            try {
                // Validar nombre
                if (nombre.isEmpty()) {
                    labelError.setText("Error: El nombre no puede estar vacio.");
                    return;
                }
                if (nombre.length() < 3) {
                    labelError.setText("Error: El nombre debe tener al menos 3 caracteres.");
                    return;
                }

                // Validar email
                if (email.isEmpty()) {
                    labelError.setText("Error: El email no puede estar vacio.");
                    return;
                }
                if (!esEmailValido(email)) {
                    labelError.setText("Error: Formato de email invalido. Ejemplo: usuario@correo.com");
                    return;
                }

                // Validar dirección
                if (direccion.isEmpty()) {
                    labelError.setText("Error: La direccion no puede estar vacia.");
                    return;
                }
                if (direccion.length() < 5) {
                    labelError.setText("Error: La direccion debe tener al menos 5 caracteres.");
                    return;
                }

                // Si todo es válido, crear el usuario y pasar a la siguiente pantalla
                usuarioRegistrado = new Usuario(nombre, email, direccion);
                
                // Mostrar alerta de éxito
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Registro Exitoso");
                alerta.setHeaderText("Bienvenido, " + nombre);
                alerta.setContentText("Tu registro ha sido completado correctamente.");
                alerta.showAndWait();

                // Pasar a la pantalla principal
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal(usuarioRegistrado);
                pantallaPrincipal.mostrar(stage);

            } catch (Exception ex) {
                labelError.setText("Error: " + ex.getMessage());
            }
        });

        // Agregar componentes al contenido
        contenido.getChildren().addAll(
                labelRegistro,
                sep1,
                labelNombre,
                textNombre,
                labelEmail,
                textEmail,
                labelDireccion,
                textDireccion,
                labelError,
                espaciador,
                btnRegistrar
        );

        return contenido;
    }

    /**
     * Obtiene el usuario registrado.
     */
    public Usuario getUsuarioRegistrado() {
        return usuarioRegistrado;
    }
}