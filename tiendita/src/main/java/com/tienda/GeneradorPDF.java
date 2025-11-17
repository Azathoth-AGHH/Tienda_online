package com.tienda;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * La clase `GeneradorPDF` es responsable de crear documentos PDF, específicamente tickets
 * de compra. Utiliza la librería externa **Apache PDFBox** para manipular y generar el
 * contenido del archivo.
 * <p>
 * Esta clase demuestra cómo integrar librerías de terceros para extender la funcionalidad
 * de una aplicación.
 */
public class GeneradorPDF {

    /**
     * Clase interna estática para representar un producto dentro del contexto del ticket PDF.
     * Esta clase anidada es un ejemplo de **encapsulamiento** y **cohesión**, ya que
     * agrupa los datos de un producto de manera específica para la generación del PDF,
     * incluyendo atributos como el descuento que no están en la clase `Producto` principal.
     */
    public static class Producto {
        String nombre;
        String categoria;
        String subcategoria;
        String cantidad;
        double precio;
        double descuento;

        /**
         * Constructor para la clase interna `Producto`.
         * Inicializa los atributos necesarios para la representación del producto en el ticket.
         *
         * @param nombre       Nombre del producto.
         * @param categoria    Categoría principal.
         * @param subcategoria Subcategoría del producto.
         * @param cantidad     Cantidad de unidades compradas, en formato de cadena.
         * @param precio       Precio unitario del producto.
         * @param descuento    Descuento aplicado al producto, en formato decimal (ej. 0.10).
         */
        public Producto(String nombre, String categoria, String subcategoria,String cantidad, double precio, double descuento) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.subcategoria = subcategoria;
            this.cantidad = cantidad;
            this.precio = precio;
            this.descuento = descuento;
        }
    }

    /**
     * Método estático para generar el ticket de compra en formato PDF.
     * Este método gestiona todo el proceso de creación del documento, desde la inicialización
     * hasta el guardado. Es estático para que pueda ser llamado sin necesidad de instanciar
     * la clase `GeneradorPDF`.
     *
     * @param destino      Ruta de destino y nombre del archivo PDF a generar.
     * @param empresa      Nombre de la empresa que se mostrará en el ticket.
     * @param usuario      Objeto `Usuario` con los datos del cliente.
     * @param rutaImagen   Ruta del archivo de imagen del logo de la empresa.
     * @param productos    Lista de objetos `Producto` (de la clase interna) que se incluirán en el ticket.
     * @throws IOException Si ocurre un error de entrada/salida durante la generación del PDF.
     */
    public static void generarTicket(String destino, String empresa, Usuario usuario, String rutaImagen, List<Producto> productos) throws IOException {
        // Inicialización de un nuevo documento PDF y una página con tamaño Carta.
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        // Carga de fuentes. Es crucial manejar las excepciones de archivos si las fuentes no se encuentran.
        PDType0Font fontRegular = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf"));
        PDType0Font fontBold = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arialbd.ttf"));

        // Creación del `PDPageContentStream`, que actúa como el "lienzo" para dibujar texto e imágenes en la página.
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Se definen variables para el control de márgenes y la posición del cursor de escritura.
        float pageWidth = page.getMediaBox().getWidth();
        float marginX = 40;
        float startY = page.getMediaBox().getHeight() - 40;
        float yPosition = startY;
        float bottomMargin = 40;

        // Inclusión del logo de la empresa. Un bloque `try-catch` es una buena práctica
        // para manejar errores si la imagen no se puede cargar.
        if (rutaImagen != null) {
            try {
                PDImageXObject pdImage = PDImageXObject.createFromFile(rutaImagen, document);
                float imgWidth = 100;
                float imgHeight = 50;
                float imgX = marginX;
                float imgY = page.getMediaBox().getHeight() - imgHeight - 20;
                contentStream.drawImage(pdImage, imgX, imgY, imgWidth, imgHeight);
            } catch (Exception e) {
                System.err.println("No se pudo cargar el logo: " + e.getMessage());
            }
        }

        // Título del ticket, centrado horizontalmente. El cálculo para centrar es una técnica común
        // en la generación de documentos.
        String textoEmpresa = empresa;
        float titleWidth = (fontBold.getStringWidth(textoEmpresa) / 1000) * 18;
        float startX = (pageWidth - titleWidth) / 2;
        
        contentStream.beginText();
        contentStream.setFont(fontBold, 18);
        contentStream.newLineAtOffset(startX, startY - 20);
        contentStream.showText(textoEmpresa);
        contentStream.endText();

        // Sección de datos del usuario.
        contentStream.setFont(fontBold, 10);
        float userStartY = startY - 50;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, userStartY);
        contentStream.showText("Nombre: " + usuario.getNombre());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, userStartY - 15);
        contentStream.showText("Email: " + usuario.getEmail());
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, userStartY - 30);
        contentStream.showText("Dirección: " + usuario.getDireccion());
        contentStream.endText();

        yPosition = userStartY - 50;

        // Fecha y hora de la generación del ticket, alineado a la derecha.
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaHoraStr = "Fecha: " + fechaHora.format(formato);

        contentStream.beginText();
        contentStream.setFont(fontBold, 10);
        contentStream.newLineAtOffset(pageWidth - marginX - 150, startY - 40);
        contentStream.showText(fechaHoraStr);
        contentStream.endText();

        // Encabezados de la tabla de productos.
        contentStream.setFont(fontBold, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, yPosition);
        contentStream.showText("Producto");
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText("Categoría");
        contentStream.newLineAtOffset(130, 0);
        contentStream.showText("Subcategoría");
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText("Cantidad");
        contentStream.newLineAtOffset(50, 0);
        contentStream.showText("Precio U.");
        contentStream.newLineAtOffset(60, 0);
        contentStream.showText("Subtotal");
        contentStream.endText();

        yPosition -= 18;
        contentStream.setFont(fontRegular, 10);

        double totalSinDesc = 0;
        double totalConDesc = 0;

        // Bucle para iterar sobre cada producto y agregarlo al documento.
        for (Producto p : productos) {
            // Lógica para el manejo de múltiples páginas si el contenido excede el espacio disponible.
            // Esta es una característica avanzada en la generación de documentos.
            if (yPosition <= bottomMargin) {
                contentStream.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = startY - 80;
            }

            // Cálculo de subtotales.
            double cant = Double.parseDouble(p.cantidad);
            double subtotal = p.precio * cant;
            double subtotalConDesc = subtotal * (1 - p.descuento);

            // Acumulación de los totales generales.
            totalSinDesc += subtotal;
            totalConDesc += subtotalConDesc;

            // Escritura de los datos del producto en una línea.
            contentStream.beginText();
            contentStream.newLineAtOffset(marginX, yPosition);
            contentStream.showText(p.nombre);
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText(p.categoria);
            contentStream.newLineAtOffset(130, 0);
            contentStream.showText(p.subcategoria);
            contentStream.newLineAtOffset(120, 0);
            contentStream.showText(p.cantidad);
            contentStream.newLineAtOffset(50, 0);
            contentStream.showText(String.format("$%.2f", p.precio));
            contentStream.newLineAtOffset(60, 0);
            contentStream.showText(String.format("$%.2f", subtotalConDesc));
            contentStream.endText();

            yPosition -= 15;

            // Si hay un descuento, se añade una línea adicional con esta información.
            if (p.descuento > 0) {
                contentStream.setFont(fontRegular, 8);
                contentStream.setNonStrokingColor(0.6f, 0.6f, 0.6f); // Color gris

                contentStream.beginText();
                contentStream.newLineAtOffset(marginX + 20, yPosition);
                contentStream.showText(">> Descuento aplicado: " + (int)(p.descuento * 100) + "%");
                contentStream.endText();

                contentStream.setFont(fontRegular, 10);
                contentStream.setNonStrokingColor(0f, 0f, 0f);
                yPosition -= 15;
            }
        }

        // Sección de totales finales.
        yPosition -= 30;

        if (yPosition <= bottomMargin) {
            contentStream.close();
            page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            yPosition = startY - 80;
        }

        contentStream.setFont(fontBold, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, yPosition);
        contentStream.showText(String.format("TOTAL SIN DESCUENTO: $%.2f", totalSinDesc));
        contentStream.endText();

        yPosition -= 20;

        contentStream.beginText();
        contentStream.newLineAtOffset(marginX, yPosition);
        contentStream.showText(String.format("TOTAL CON DESCUENTO: $%.2f", totalConDesc));
        contentStream.endText();

        // Se cierra el `contentStream`, se guarda el documento y se cierra el `PDDocument`.
        contentStream.close();
        document.save(destino);
        document.close();
        System.out.println(" Ticket PDF generado correctamente en: " + destino);
    }
}