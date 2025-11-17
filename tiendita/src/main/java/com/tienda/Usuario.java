package com.tienda;

/**
 * La clase Usuario representa una entidad de usuario en un sistema de tienda online.
 * Contiene información fundamental para identificar a un cliente, gestionar sus compras
 * y coordinar envíos.
 */
public class Usuario {
    // Atributos de la clase. Se declaran como privados para garantizar el encapsulamiento.
    private String nombre;      // Almacena el nombre completo del usuario.
    private String email;       // Guarda la dirección de correo electrónico, utilizada para comunicación y autenticación.
    private String direccion;   // Contiene la dirección de envío del usuario.

    /**
     * Constructor por defecto de la clase `Usuario`.
     * Inicializa un objeto `Usuario` con valores predeterminados para representar a un
     * cliente anónimo o no registrado.
     * Es útil para escenarios donde no se requiere un perfil completo, como una compra rápida.
     */
    public Usuario() {
        this.nombre = "Invitado";
        this.email = "sinemail@tienda.com";
        this.direccion = "Desconocida";
    }

    /**
     * Constructor parametrizado de la clase `Usuario`.
     * Permite la creación de un objeto `Usuario` completamente inicializado con los
     * datos proporcionados.
     */
    public Usuario(String nombre, String email, String direccion) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
    }

    // --- Métodos de Acceso (Getters) ---
    // Proporcionan acceso de solo lectura a los atributos privados de la clase,
    // permitiendo que otras clases obtengan los datos del usuario sin poder modificarlos directamente.

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    // --- Métodos de Modificación (Setters) ---
    // Permiten la modificación controlada de los atributos de la clase.

    /**
     * Establece el nombre del usuario.
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la dirección de correo electrónico del usuario.
     * @param email El nuevo correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Establece la dirección completa del usuario.
     * @param direccion La nueva dirección completa.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Método sobrecargado para establecer la dirección del usuario.
     * Permite construir la dirección a partir de sus componentes individuales.
     * La **sobrecarga de métodos** es una característica del polimorfismo que permite
     * definir múltiples métodos con el mismo nombre pero con diferentes listas de parámetros.
     *
     * @param calle   Nombre de la calle.
     * @param numero  Número de la dirección.
     * @param colonia Nombre de la colonia o barrio.
     */
    public void setDireccion(String calle, String numero, String colonia) {
        this.direccion = calle + " #" + numero + ", Col. " + colonia;
    }

    /**
     * Sobrescribe el método `toString()` de la clase `Object`.
     * Proporciona una representación de cadena del objeto `Usuario` en un formato legible.
     * Esto es útil para fines de depuración o para mostrar la información del usuario en la consola.
     *
     * @return Una cadena de texto que representa el estado del objeto `Usuario`.
     */
    @Override
    public String toString() {
        return "Usuario: " + nombre + " | Email: " + email + " | Dirección: " + direccion;
    }
}