package com.tienda.ui;

/**
 * Colores proporciona un sistema centralizado de colores para toda la aplicación.
 * Tema: Negro y Rojo
 */
public class ColorTheme {
    // Colores principales (Negro y Rojo)
    public static final String NEGRO_PRINCIPAL = "#1a1a1a";
    public static final String NEGRO_OSCURO = "#0d0d0d";
    public static final String ROJO_PRINCIPAL = "#dc143c";
    public static final String ROJO_OSCURO = "#b22222";
    public static final String ROJO_CLARO = "#ff4444";
    public static final String GRIS_FONDO = "#2a2a2a";
    public static final String GRIS_CLARO = "#404040";
    public static final String BLANCO = "white";
    public static final String BLANCO_HUMO = "#f5f5f5";
    public static final String TEXTO_GRIS = "#cccccc";
    public static final String TEXTO_CLARO = "#e0e0e0";
    public static final String VERDE_EXITO = "#00a82d";
    public static final String AMARILLO_ADVERTENCIA = "#ffc107";
    
    // Estilos CSS reutilizables
    public static final String ESTILO_ENCABEZADO = 
        "-fx-background-color: " + NEGRO_PRINCIPAL + ";";
    
    public static final String ESTILO_BOTON_PRINCIPAL = 
        "-fx-font-size: 14; -fx-font-weight: bold; " +
        "-fx-background-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-text-fill: white;";
    
    public static final String ESTILO_BOTON_PRINCIPAL_HOVER = 
        "-fx-font-size: 14; -fx-font-weight: bold; " +
        "-fx-background-color: " + ROJO_OSCURO + "; " +
        "-fx-text-fill: white;";
    
    public static final String ESTILO_BOTON_SECUNDARIO = 
        "-fx-font-size: 12; -fx-padding: 10; " +
        "-fx-background-color: " + GRIS_CLARO + "; " +
        "-fx-border-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-text-fill: " + TEXTO_CLARO + ";";
    
    public static final String ESTILO_BOTON_CATEGORIA = 
        "-fx-font-size: 13; -fx-padding: 12; " +
        "-fx-background-color: " + GRIS_CLARO + "; " +
        "-fx-border-color: " + GRIS_CLARO + "; " +
        "-fx-border-width: 0 0 1 0; " +
        "-fx-text-fill: " + TEXTO_CLARO + ";";
    
    public static final String ESTILO_BOTON_CATEGORIA_ACTIVO = 
        "-fx-font-size: 13; -fx-padding: 12; " +
        "-fx-background-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-border-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-border-width: 0 0 3 0; " +
        "-fx-text-fill: white; -fx-font-weight: bold;";
    
    public static final String ESTILO_PIE_PAGINA = 
        "-fx-background-color: " + NEGRO_OSCURO + ";";
    
    public static final String ESTILO_CARD_PRODUCTO = 
        "-fx-background-color: " + GRIS_CLARO + "; " +
        "-fx-border-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-border-width: 1; " +
        "-fx-border-radius: 5;";
    
    public static final String ESTILO_FONDO = 
        "-fx-background-color: " + GRIS_FONDO + ";";
    
    public static final String ESTILO_TEXTFIELD = 
        "-fx-control-inner-background: " + GRIS_CLARO + "; " +
        "-fx-text-fill: " + BLANCO + "; " +
        "-fx-border-color: " + ROJO_PRINCIPAL + "; " +
        "-fx-border-width: 1;";
}