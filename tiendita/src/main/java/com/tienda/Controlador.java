package com.tienda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * La clase Controlador coordina todas las operaciones de la tienda.
 * Gestiona la interacción con el usuario, validaciones y manejo de excepciones.
 */
public class Controlador {
    private Usuario usuario;
    private Carrito carrito;
    private Producto[] catalogo;
    private Scanner sc;
    
    // Patrón regex para validar emails
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public Controlador() {
        carrito = new Carrito(20);
        sc = new Scanner(System.in);
        cargarCatalogo();
    }

    /**
     * Valida si el email cumple con un formato válido.
     * @param email Email a validar
     * @return true si el email es válido, false en caso contrario
     */
    private boolean esEmailValido(String email) {
        return email != null && pattern.matcher(email).matches() && email.contains(".");
    }

    /**
     * Registra al usuario con validación de datos y excepciones.
     * @throws UsuarioInvalidoException si los datos del usuario son inválidos
     * @throws Invalidar_Email si el email no cumple con el formato válido
     */
    public void registrarUsuario() throws UsuarioInvalidoException, Invalidar_Email {
        System.out.println("\n=== Registro de Usuario ===");
        
        String nombre = null;
        String email = null;
        String direccion = null;
        
        try {
            // Validación de nombre
            System.out.print("Nombre: ");
            nombre = sc.nextLine().trim();
            if (nombre.isEmpty()) {
                throw new UsuarioInvalidoException("Error: El nombre no puede estar vacio.");
            }
            if (nombre.length() < 3) {
                throw new UsuarioInvalidoException("Error: El nombre debe tener al menos 3 caracteres.");
            }

            // Validación de email
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            if (email.isEmpty()) {
                throw new Invalidar_Email("Error: El email no puede estar vacio.");
            }
            if (!esEmailValido(email)) {
                throw new Invalidar_Email("Error: Formato de email invalido. Ejemplo: usuario@correo.com");
            }

            // Validación de dirección
            System.out.print("Direccion: ");
            direccion = sc.nextLine().trim();
            if (direccion.isEmpty()) {
                throw new UsuarioInvalidoException("Error: La direccion no puede estar vacia.");
            }
            if (direccion.length() < 5) {
                throw new UsuarioInvalidoException("Error: La direccion debe tener al menos 5 caracteres.");
            }

            usuario = new Usuario(nombre, email, direccion);
            System.out.println("Usuario registrado correctamente: " + usuario.getNombre());
            
        } catch (Invalidar_Email | UsuarioInvalidoException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Carga el catálogo de productos disponibles.
     */
    private void cargarCatalogo() {
        catalogo = new Producto[58];

        // Lácteos - Leche entera
        catalogo[0] = new Producto("Lala 1 L", "Lacteos", "Leche entera", 28.50, 0);
        catalogo[1] = new Producto("Santa Clara 1 L (6 piezas)", "Lacteos", "Leche entera", 230.00, 0);
        catalogo[2] = new Producto("Alpura 1 L (6 piezas)", "Lacteos", "Leche entera", 180.00, 0);
        catalogo[3] = new Producto("Nutrileche 1 L", "Lacteos", "Leche entera", 25.00, 0);

        // Lácteos - Leche deslactosada
        catalogo[4] = new Producto("Lala 1 L (6 piezas)", "Lacteos", "Leche deslactosada", 159.00, 0);
        catalogo[5] = new Producto("Alpura 1 L", "Lacteos", "Leche deslactosada", 30.00, 0);
        catalogo[6] = new Producto("Santa Clara 1 L", "Lacteos", "Leche deslactosada", 40.00, 0);

        // Lácteos - Leche saborizada
        catalogo[7] = new Producto("Lala Yomi Vainilla 180 ml", "Lacteos", "Leche saborizada", 10.00, 0);
        catalogo[8] = new Producto("Lala Yomi Chocolate 180 ml", "Lacteos", "Leche saborizada", 10.00, 0);
        catalogo[9] = new Producto("Lala Yomi Fresa 180 ml", "Lacteos", "Leche saborizada", 10.00, 0);
        catalogo[10] = new Producto("Alpura Vainilla 180 ml", "Lacteos", "Leche saborizada", 11.00, 0);
        catalogo[11] = new Producto("Alpura Fresa 180 ml", "Lacteos", "Leche saborizada", 11.00, 0);
        catalogo[12] = new Producto("Alpura Chocolate 180 ml", "Lacteos", "Leche saborizada", 11.00, 0);
        catalogo[13] = new Producto("Santa Clara Vainilla 180 ml", "Lacteos", "Leche saborizada", 13.00, 0);
        catalogo[14] = new Producto("Santa Clara Chocolate 180 ml", "Lacteos", "Leche saborizada", 13.00, 0);
        catalogo[15] = new Producto("Santa Clara Fresa 180 ml", "Lacteos", "Leche saborizada", 13.00, 0);

        // Lácteos - Yogurt
        catalogo[16] = new Producto("Lala Fresa 220 g (8 piezas)", "Lacteos", "Yogurt bebible", 70.00, 0);
        catalogo[17] = new Producto("Alpura Natural 1 kg", "Lacteos", "Yogurt natural", 42.00, 0);
        catalogo[18] = new Producto("Danone Griego 150 g", "Lacteos", "Yogurt griego", 18.00, 0);

        // Lácteos - Mantequilla y Margarina
        catalogo[19] = new Producto("Lala sin sal 90 g", "Lacteos", "Mantequilla", 24.00, 0);
        catalogo[20] = new Producto("Primavera 225 g", "Lacteos", "Margarina", 18.00, 0);

        // Snacks - Galletas
        catalogo[21] = new Producto("Marinela Canelitas 300 g", "Snacks", "Galletas", 37.90, 0);
        catalogo[22] = new Producto("Chokiees 300 g", "Snacks", "Galletas", 107.00, 0);
        catalogo[23] = new Producto("Sponch 700 g (4 paquetes)", "Snacks", "Galletas", 79.50, 0);
        catalogo[24] = new Producto("Pasticetas 400 g", "Snacks", "Galletas", 65.90, 0);
        catalogo[25] = new Producto("Surtido de Marinela 450 g", "Snacks", "Galletas", 73.50, 0);

        // Snacks - Botanas
        catalogo[26] = new Producto("Sabritas Original 42 g", "Snacks", "Botanas", 20.00, 0);
        catalogo[27] = new Producto("Sabritas Limon 42 g", "Snacks", "Botanas", 20.00, 0);
        catalogo[28] = new Producto("Sabritas Flamin Hot 42 g", "Snacks", "Botanas", 20.00, 0);
        catalogo[29] = new Producto("Doritos Rojos 75 g", "Snacks", "Botanas", 18.00, 0);
        catalogo[30] = new Producto("Doritos Verdes 35 g", "Snacks", "Botanas", 18.00, 0);
        catalogo[31] = new Producto("Cheetos Torciditos 80 g", "Snacks", "Botanas", 15.00, 0);
        catalogo[32] = new Producto("Cheetos Poffs 80 g", "Snacks", "Botanas", 15.00, 0);
        catalogo[33] = new Producto("Cheetos Flamin Hot 80 g", "Snacks", "Botanas", 15.00, 0);
        catalogo[34] = new Producto("Cacahuates 70 g", "Snacks", "Botanas", 20.00, 0);

        // Snacks - Pastelitos
        catalogo[35] = new Producto("Gansito Marinela 50 g", "Snacks", "Pastelitos", 20.90, 0);
        catalogo[36] = new Producto("Pinguinos Marinela 80 g", "Snacks", "Pastelitos", 27.90, 0);
        catalogo[37] = new Producto("Choco Roles Marinela 122 g (2 piezas)", "Snacks", "Pastelitos", 27.90, 0);
        catalogo[38] = new Producto("Gansito Marinela 3 Piezas", "Snacks", "Pastelitos", 50.90, 0);

        // Limpieza - Multiusos
        catalogo[39] = new Producto("Pinol El Original 5.1 L", "Limpieza", "Multiusos", 179.00, 0);
        catalogo[40] = new Producto("Fabuloso 6 L", "Limpieza", "Multiusos", 199.00, 0);
        catalogo[41] = new Producto("Cloralex 1 L", "Limpieza", "Multiusos", 68.00, 0);
        catalogo[42] = new Producto("Clorox 1 L", "Limpieza", "Multiusos", 65.00, 0);
        catalogo[43] = new Producto("Vanish 1 L", "Limpieza", "Multiusos", 90.00, 0);

        // Limpieza - Detergentes
        catalogo[44] = new Producto("Ariel Liquido Poder y Cuidado 8.5 L", "Limpieza", "Detergentes", 374.25, 0);
        catalogo[45] = new Producto("Persil en Polvo para Ropa de Color 9 kg", "Limpieza", "Detergentes", 439.00, 0);
        catalogo[46] = new Producto("Ariel Liquido Color 2.8 L (45 lavadas)", "Limpieza", "Detergentes", 149.00, 0);
        catalogo[47] = new Producto("Ariel en Polvo con Downy 750 g", "Limpieza", "Detergentes", 35.00, 0);
        catalogo[48] = new Producto("Ariel Expert Liquido 5 L (80 lavadas)", "Limpieza", "Detergentes", 194.90, 0);

        // Limpieza - Lavatrastes
        catalogo[49] = new Producto("Salvo Limon Liquido 1.4 L", "Limpieza", "Lavatrastes", 69.00, 0);
        catalogo[50] = new Producto("Salvo Polvo 1 kg", "Limpieza", "Lavatrastes", 39.00, 0);
        catalogo[51] = new Producto("Salvo Lavatrastes Limon 900 ml", "Limpieza", "Lavatrastes", 55.00, 0);
        catalogo[52] = new Producto("Salvo Lavatrastes Limon 500 ml", "Limpieza", "Lavatrastes", 32.90, 0);

        // Bebidas - Energizantes
        catalogo[53] = new Producto("Amper Mango 475 ml", "Bebidas", "Energizantes", 20, 0);
        catalogo[54] = new Producto("Monster Blanco 355 ml", "Bebidas", "Energizantes", 46, 0);
        catalogo[55] = new Producto("Predator Rojo 475 ml", "Bebidas", "Energizantes", 19, 0);
        catalogo[56] = new Producto("Red Bull 420 ml", "Bebidas", "Energizantes", 52, 0);
        catalogo[57] = new Producto("Vive 100 630 ml", "Bebidas", "Energizantes", 25, 0);
    }

    /**
     * Menú del catálogo con manejo de excepciones para selección de productos.
     */
    public void menuCatalogo() {
        List<String> categorias = new ArrayList<>();
        for (Producto p : catalogo) {
            if (!categorias.contains(p.getCategoria())) {
                categorias.add(p.getCategoria());
            }
        }

        int opcionCategoria;
        do {
            System.out.println("\n=== Categorias ===");
            for (int i = 0; i < categorias.size(); i++) {
                System.out.println((i + 1) + ". " + categorias.get(i));
            }
            System.out.println("0. Regresar al menu principal");
            System.out.print("Seleccione una categoria: ");

            try {
                opcionCategoria = sc.nextInt();
                sc.nextLine();

                if (opcionCategoria > 0 && opcionCategoria <= categorias.size()) {
                    String categoriaSeleccionada = categorias.get(opcionCategoria - 1);
                    mostrarProductosPorCategoria(categoriaSeleccionada);
                } else if (opcionCategoria != 0) {
                    System.err.println("Error: Opcion de categoria no valida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Debe ingresar un numero valido.");
                sc.nextLine();
                opcionCategoria = -1;
            }
        } while (opcionCategoria != 0);
    }

    /**
     * Muestra los productos de una categoría y permite al usuario seleccionar uno.
     * @param categoriaSeleccionada Categoría a mostrar
     */
    private void mostrarProductosPorCategoria(String categoriaSeleccionada) {
        System.out.println("\n- " + categoriaSeleccionada);

        String subcategoriaActual = "";
        int contador = 1;
        int[] indicesMap = new int[catalogo.length];

        for (int i = 0; i < catalogo.length; i++) {
            Producto p = catalogo[i];
            if (p.getCategoria().equals(categoriaSeleccionada)) {
                if (!p.getSubcategoria().equals(subcategoriaActual)) {
                    subcategoriaActual = p.getSubcategoria();
                    System.out.println("    * " + subcategoriaActual);
                }
                System.out.println("        " + contador + ". " + p.getNombre() + " - $" + p.getPrecio());
                indicesMap[contador - 1] = i;
                contador++;
            }
        }

        System.out.print("\nSeleccione un producto (0 para regresar): ");

        try {
            int opcionProducto = sc.nextInt();
            sc.nextLine();

            if (opcionProducto == 0) {
                return;
            }

            if (opcionProducto < 1 || opcionProducto >= contador) {
                throw new ProductoNoEncontradoException(
                    "Error: El producto seleccionado (opcion " + opcionProducto + ") no existe en la categoria."
                );
            }

            int indexReal = indicesMap[opcionProducto - 1];
            Producto seleccionado = catalogo[indexReal];

            System.out.print("Ingrese la cantidad: ");
            int cantidad = sc.nextInt();
            sc.nextLine();

            if (cantidad <= 0) {
                throw new CantidadInvalidaException("Error: La cantidad debe ser mayor a 0.");
            }

            if (cantidad > 100) {
                throw new CantidadInvalidaException("Error: La cantidad no puede exceder 100 unidades.");
            }

            carrito.agregarProducto(new Producto(
                seleccionado.getNombre(),
                seleccionado.getCategoria(),
                seleccionado.getSubcategoria(),
                seleccionado.getPrecio(),
                cantidad
            ));

            System.out.println("Producto agregado al carrito correctamente.");

        } catch (ProductoNoEncontradoException e) {
            System.err.println(e.getMessage());
        } catch (CantidadInvalidaException e) {
            System.err.println(e.getMessage());
        } catch (Invalidar_Email e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Debe ingresar numeros validos.");
        }
    }

    /**
     * Menú principal con manejo de excepciones.
     */
    public void menuPrincipal() {
        int opcion;

        do {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Ver catalogo");
            System.out.println("2. Ver carrito");
            System.out.println("3. Finalizar compra");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1:
                        menuCatalogo();
                        break;
                    case 2:
                        carrito.mostrarCarrito();
                        break;
                    case 3:
                        resumenCompra();
                        opcion = 0;
                        break;
                    case 0:
                        System.out.println("Gracias por visitar City Market.");
                        break;
                    default:
                        System.err.println("Error: Opcion no valida. Intente nuevamente.");
                }

            } catch (NumberFormatException e) {
                System.err.println("Error: Debe ingresar un numero valido.");
                opcion = -1;
            }

        } while (opcion != 0);
    }

    /**
     * Resumen de compra con generación de PDF.
     */
    public void resumenCompra() {
        System.out.println("\n=== Resumen de Compra ===");
        carrito.imprimirTicket(usuario);

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

        try {
            GeneradorPDF.generarTicket(
                "ticket.pdf",
                "City Market",
                usuario,
                "tiendita/src/logo.png",
                productosPDF
            );
            System.out.println("Ticket PDF generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al generar el ticket PDF: " + e.getMessage());
        }
    }

    /**
     * Inicia el proceso completo de la aplicación con manejo de excepciones.
     */
    public void iniciarCompra() {
        System.out.println("=== Bienvenido a City Market ===\n");
        
        boolean usuarioRegistrado = false;
        while (!usuarioRegistrado) {
            try {
                registrarUsuario();
                usuarioRegistrado = true;
            } catch (UsuarioInvalidoException | Invalidar_Email e) {
                System.out.println("Intente nuevamente...\n");
            }
        }
        
        menuPrincipal();
    }
}