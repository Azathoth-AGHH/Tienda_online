package com.tienda;

/**
 * La clase Producto representa una entidad de producto en un sistema de gestión de tienda.
 * Almacena atributos esenciales como nombre, categoría, subcategoría, precio y la cantidad
 * disponible en inventario (stock). Esta clase es fundamental para la gestión de ítems
 * dentro de la aplicación.
 */
public class Producto {
    // Declaración de los atributos de la clase, todos definidos como privados
    // para asegurar el encapsulamiento de los datos.
    private String nombre;          // Nombre descriptivo del producto.
    private String categoria;       // Categoría principal a la que pertenece el producto (ej. "Lácteos").
    private String subcategoria;    // Subcategoría para una clasificación más detallada (ej. "Leche").
    private double precio;          // Precio unitario del producto, utilizando un tipo de dato 'double' para manejar valores con decimales.
    private int stock;              // Cantidad de unidades del producto disponibles en el inventario.

    /**
     * Constructor de la clase Producto.
     * Este método inicializa un nuevo objeto con los valores proporcionados para cada uno de sus atributos.
     *
     */
    public Producto(String nombre, String categoria, String subcategoria, double precio, int stock) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.precio = precio;
        this.stock = stock;
    }

    // --- Métodos de Acceso (Getters) ---
    // Proporcionan acceso de solo lectura a los atributos privados de la clase.

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    // --- Métodos de Modificación (Setters) ---
    // Permiten la modificación controlada de los atributos de la clase.

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Sobrescribe el método `toString()` de la clase `Object` para proporcionar
     * una representación de cadena del objeto Producto.
     *
     * retorna una cadena de texto que contiene los atributos principales del producto
     * en un formato legible, ideal para la visualización en consolas o interfaces de usuario.
     */
    @Override
    public String toString() {
        return nombre + " | " + categoria + " - " + subcategoria + " | $" + precio;
    }
}