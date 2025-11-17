package com.tienda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * La clase Carrito representa la funcionalidad de un carrito de compras en un sistema de tienda.
 * Es una clase fundamental para la gestión de los productos seleccionados por el usuario antes de la compra. 
 * Permite agregar productos, calcular totales y generar un ticket de compra detallado.
 * Ahora incluye manejo de excepciones personalizado.
 */
public class Carrito {
    // Declaración de atributos. Se utiliza un array de tipo Producto para almacenar los items.
    private Producto[] productos; // Array que almacena los objetos Producto agregados al carrito.
    private int contador;         // Un contador que indica el número de productos actualmente en el carrito.
    private int capacidad;        // La capacidad máxima del carrito, definida al ser inicializado.

    /**
     * Constructor por defecto de la clase `Carrito`.
     * Inicializa el carrito con una capacidad predeterminada de 10 productos.
     * Este constructor es útil cuando la capacidad no se especifica explícitamente.
     */
    public Carrito() {
        this.capacidad = 10;
        this.productos = new Producto[capacidad];
        this.contador = 0;
    }

    /**
     * Constructor parametrizado de la clase `Carrito`.
     * Permite inicializar el carrito con una capacidad definida por el usuario.
     * 
     * @param capacidad La capacidad máxima de productos que puede contener el carrito
     */
    public Carrito(int capacidad) {
        this.capacidad = capacidad;
        this.productos = new Producto[capacidad];
        this.contador = 0;
    }

    /**
     * Agrega un objeto `Producto` al carrito de compras con validación.
     * Se verifica si el carrito tiene espacio disponible antes de realizar la adición.
     * 
     * @param p El producto a agregar al carrito
     * @throws Invalidar_Email si el carrito está lleno y no se puede agregar más productos
     * @throws IllegalArgumentException si el producto es nulo
     */
    public void agregarProducto(Producto p) throws Invalidar_Email {
        // Se valida que el producto no sea nulo
        if (p == null) {
            throw new IllegalArgumentException("Error: El producto no puede ser nulo.");
        }

        // Se valida si el contador es menor que la capacidad para evitar un `ArrayIndexOutOfBoundsException`.
        if (contador >= capacidad) {
            throw new Invalidar_Email(
                "Error: El carrito esta lleno. No se pueden agregar mas de " + capacidad + " productos."
            );
        }

        // Se agrega el producto y se incrementa el contador
        productos[contador] = p;
        contador++;
        System.out.println(" Producto agregado: " + p.getNombre());
    }

    /**
     * Muestra una representación del contenido actual del carrito en la consola.
     * Es una vista simple que no incluye descuentos, ideal para una revisión rápida.
     */
    public void mostrarCarrito() {
        if (contador == 0) {
            System.out.println(" El carrito esta vacio.");
            return;
        }

        System.out.println("\n=== Carrito de Compras ===");
        for (int i = 0; i < contador; i++) {
            Producto p = productos[i];
            // Se utiliza `printf` para dar formato a la salida y alinear las columnas.
            System.out.printf("%-30s %3d x %6.2f = %7.2f%n", p.getNombre(), p.getStock(), p.getPrecio(), p.getStock() * p.getPrecio());
        }
        System.out.printf("TOTAL APROX: %.2f%n", calcularTotal());
    }

    /**
     * Genera e imprime un ticket de compra detallado en la consola.
     * Incluye información del usuario, fecha, productos, subtotales por categoría y el cálculo final de descuentos.
     * 
     * @param usuario El usuario que realiza la compra, con sus datos personales
     */
    public void imprimirTicket(Usuario usuario) {
        if (contador == 0) {
            System.out.println(" El carrito esta vacio.");
            return;
        }

        String line = "========================================================";
        int ancho = 56;

        // Encabezado del ticket con datos del cliente y la tienda.
        System.out.println(line);
        System.out.println(centerText("City Market", ancho));
        System.out.println("Cliente: " + usuario.getNombre());
        System.out.println("Email:  " + usuario.getEmail());
        System.out.println("Direccion: " + usuario.getDireccion());

        // Se utiliza `LocalDateTime` para obtener la fecha y hora actual de la transacción.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("Fecha: " + dtf.format(LocalDateTime.now()));
        System.out.println(line);

        // Variables para la lógica de agrupación por subcategoría y cálculo de subtotales.
        String subcategoriaActual = "";
        double subtotalSubcategoriaConDesc = 0;

        double totalSinDescuento = 0;
        double totalConDescuento = 0;

        // Se itera sobre los productos para calcular precios y aplicar descuentos.
        for (int i = 0; i < contador; i++) {
            Producto p = productos[i];
            int cantidad = p.getStock();
            double precioUnitario = p.getPrecio();
            double descuento = 0;

            // Lógica para el cálculo de descuentos basada en la cantidad de productos.
            if (cantidad >= 3 && cantidad <= 4) descuento = 0.05;
            else if (cantidad >= 5 && cantidad <= 6) descuento = 0.10;
            else if (cantidad >= 7) descuento = 0.15;

            double subtotal = precioUnitario * cantidad;
            double subtotalConDesc = subtotal * (1 - descuento);

            totalSinDescuento += subtotal;
            totalConDescuento += subtotalConDesc;

            // Lógica para agrupar productos por subcategoría en el ticket.
            // Si la subcategoría cambia, se imprime el subtotal de la categoría anterior y se reinician los valores para la nueva.
            if (!p.getSubcategoria().equals(subcategoriaActual)) {
                if (!subcategoriaActual.equals("")) {
                    System.out.println(String.format("%-40s %15.2f", "Subtotal " + subcategoriaActual + ":", subtotalSubcategoriaConDesc));
                    System.out.println(line);
                }
                subcategoriaActual = p.getSubcategoria();
                subtotalSubcategoriaConDesc = 0;
                System.out.println("\n--- " + subcategoriaActual + " ---");
            }
            subtotalSubcategoriaConDesc += subtotalConDesc;
            
            String descText = descuento > 0 ? String.format(" (Desc %d%%)", (int)(descuento*100)) : "";
            
            System.out.printf("%-30s %3d x %6.2f = %7.2f%s%n",
                p.getNombre(), cantidad, precioUnitario, subtotalConDesc, descText);
        }

        // Se imprime el subtotal de la última categoría.
        System.out.println(String.format("%-40s %15.2f", "Subtotal " + subcategoriaActual + ":", subtotalSubcategoriaConDesc));
        System.out.println(line);

        // Totales finales de la compra.
        System.out.printf("%-40s %15.2f%n", "TOTAL SIN DESCUENTO:", totalSinDescuento);
        System.out.printf("%-40s %15.2f%n", "TOTAL CON DESCUENTO:", totalConDescuento);
        System.out.println(line);
        
        System.out.println(centerText("Gracias por su compra!", ancho));
        System.out.println(line);
    }

    /**
     * Método auxiliar para centrar texto dentro de un ancho de columna.
     * Es una técnica de formato comúnmente utilizada para mejorar la legibilidad de la salida.
     * 
     * @param text El texto a centrar
     * @param width El ancho total de la columna
     * @return El texto centrado con espacios en blanco
     */
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    /**
     * Calcula el total de la compra con los descuentos aplicados.
     * Este método es utilizado por `mostrarCarrito()` para dar un total aproximado.
     * 
     * @return El total de la compra con descuentos aplicados
     */
    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < contador; i++) {
            Producto p = productos[i];
            int cantidad = p.getStock();
            double precioUnitario = p.getPrecio();
            double descuento = 0;

            if (cantidad >= 3 && cantidad <= 4) descuento = 0.05;
            else if (cantidad >= 5 && cantidad <= 6) descuento = 0.10;
            else if (cantidad >= 7) descuento = 0.15;

            total += precioUnitario * cantidad * (1 - descuento);
        }
        return total;
    }

    /**
     * Obtiene el número de productos actualmente en el carrito.
     * 
     * @return El número de productos en el carrito
     */
    public int obtenerCantidadProductos() {
        return contador;
    }

    /**
     * Obtiene la capacidad máxima del carrito.
     * 
     * @return La capacidad máxima del carrito
     */
    public int obtenerCapacidad() {
        return capacidad;
    }

    /**
     * Verifica si el carrito está vacío.
     * 
     * @return true si el carrito está vacío, false en caso contrario
     */
    public boolean estaVacio() {
        return contador == 0;
    }

    /**
     * Verifica si el carrito está lleno.
     * 
     * @return true si el carrito está lleno, false en caso contrario
     */
    public boolean estaLleno() {
        return contador >= capacidad;
    }

    /**
     * Vacía completamente el carrito, removiendo todos los productos.
     */
    public void vaciarCarrito() {
        for (int i = 0; i < contador; i++) {
            productos[i] = null;
        }
        contador = 0;
        System.out.println(" El carrito ha sido vaciado.");
    }

    /**
     * Obtiene el producto en una posición específica del carrito.
     * 
     * @param indice La posición del producto en el carrito
     * @return El producto en la posición indicada, o null si no existe
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public Producto obtenerProducto(int indice) {
        if (indice < 0 || indice >= contador) {
            throw new IndexOutOfBoundsException("Error: Indice " + indice + " fuera de rango.");
        }
        return productos[indice];
    }

    /**
     * Remueve un producto del carrito por su índice.
     * 
     * @param indice La posición del producto a remover
     * @return El producto removido
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public Producto removerProducto(int indice) {
        if (indice < 0 || indice >= contador) {
            throw new IndexOutOfBoundsException("Error: Indice " + indice + " fuera de rango.");
        }

        Producto productoRemovido = productos[indice];

        // Se corre todos los elementos posteriores una posición hacia atrás
        for (int i = indice; i < contador - 1; i++) {
            productos[i] = productos[i + 1];
        }

        // Se limpia la última posición y se decrementa el contador
        productos[contador - 1] = null;
        contador--;

        System.out.println(" Producto removido: " + productoRemovido.getNombre());
        return productoRemovido;
    }

    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     * 
     * @param indice La posición del producto en el carrito
     * @param nuevaCantidad La nueva cantidad para el producto
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     * @throws CantidadInvalidaException si la cantidad es inválida
     */
    public void actualizarCantidad(int indice, int nuevaCantidad) throws CantidadInvalidaException {
        if (indice < 0 || indice >= contador) {
            throw new IndexOutOfBoundsException("Error: Indice " + indice + " fuera de rango.");
        }

        if (nuevaCantidad <= 0) {
            throw new CantidadInvalidaException("Error: La cantidad debe ser mayor a 0.");
        }

        if (nuevaCantidad > 100) {
            throw new CantidadInvalidaException("Error: La cantidad no puede exceder 100 unidades.");
        }

        Producto p = productos[indice];
        p.setStock(nuevaCantidad);
        System.out.println(" Cantidad actualizada para " + p.getNombre() + " a " + nuevaCantidad + " unidades.");
    }

    // --- Métodos de Acceso (Getters) ---

    /**
     * Obtiene el array completo de productos del carrito.
     * 
     * @return El array de productos
     */
    public Producto[] getProductos() {
        return productos;
    }

    /**
     * Obtiene el contador de productos en el carrito.
     * 
     * @return El número de productos en el carrito
     */
    public int getContador() {
        return contador;
    }

    /**
     * Obtiene la capacidad actual del carrito.
     * 
     * @return La capacidad máxima del carrito
     */
    public int getCapacidad() {
        return capacidad;
    }
}