package proyecto.presentation;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import proyecto.logic.Categoria;
import proyecto.logic.Funcionario;
import proyecto.logic.Recurso;
import proyecto.logic.Reserva;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Genera los reportes PDF compartidos por los módulos del sistema. */
public final class PdfReportes {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter SOLO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private PdfReportes() {
    }

    public static Path funcionarios(List<Funcionario> funcionarios, Path destino) throws Exception {
        Path absoluto = destino.toAbsolutePath();
        if (absoluto.getParent() != null) Files.createDirectories(absoluto.getParent());

        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont negrita = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        try (PdfWriter writer = new PdfWriter(absoluto.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            document.setMargins(30, 30, 30, 30);
            document.add(new Paragraph("Sistema de Reservas")
                    .setFont(negrita).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Listado de Funcionarios")
                    .setFont(negrita).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generado: " + LocalDateTime.now().format(FECHA))
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

            Table tabla = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2}))
                    .useAllAvailableWidth();
            tabla.addHeaderCell(celda("ID", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Nombre", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Teléfono", negrita, TextAlignment.CENTER));

            for (Funcionario funcionario : funcionarios) {
                tabla.addCell(celda(funcionario.getId(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(funcionario.getNombre(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(funcionario.getTelefono(), normal, TextAlignment.LEFT));
            }
            document.add(tabla);
            document.add(new Paragraph("Total: " + funcionarios.size())
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
        }
        return absoluto;
    }

    private static Cell celda(String texto, PdfFont fuente, TextAlignment alineacion) {
        return new Cell()
                .add(new Paragraph(texto == null ? "" : texto).setFont(fuente))
                .setPadding(5)
                .setTextAlignment(alineacion);
    }

    public static void abrir(Path archivo) throws Exception {
        File pdf = archivo.toFile();
        if (!pdf.exists()) throw new Exception("El archivo PDF no existe");
        if (!Desktop.isDesktopSupported())
            throw new Exception("El sistema no permite abrir archivos automáticamente");
        Desktop.getDesktop().open(pdf);
    }

    public static Path categorias(List<Categoria> categorias, Path destino) throws Exception {
        Path absoluto = destino.toAbsolutePath();
        if (absoluto.getParent() != null) Files.createDirectories(absoluto.getParent());

        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont negrita = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        try (PdfWriter writer = new PdfWriter(absoluto.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            document.setMargins(30, 30, 30, 30);
            document.add(new Paragraph("Sistema de Reservas")
                    .setFont(negrita).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Listado de Categorías")
                    .setFont(negrita).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generado: " + LocalDateTime.now().format(FECHA))
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

            Table tabla = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                    .useAllAvailableWidth();
            tabla.addHeaderCell(celda("ID", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Descripción", negrita, TextAlignment.CENTER));

            for (Categoria categoria : categorias) {
                tabla.addCell(celda(categoria.getId(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(categoria.getDescripcion(), normal, TextAlignment.LEFT));
            }
            document.add(tabla);
            document.add(new Paragraph("Total: " + categorias.size())
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
        }
        return absoluto;
    }

    public static Path recursos(List<Recurso> recursos, Path destino) throws Exception {
        Path absoluto = destino.toAbsolutePath();
        if (absoluto.getParent() != null) Files.createDirectories(absoluto.getParent());

        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont negrita = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        try (PdfWriter writer = new PdfWriter(absoluto.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            document.setMargins(30, 30, 30, 30);
            document.add(new Paragraph("Sistema de Reservas")
                    .setFont(negrita).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Listado de Recursos")
                    .setFont(negrita).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generado: " + LocalDateTime.now().format(FECHA))
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

            Table tabla = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3}))
                    .useAllAvailableWidth();
            tabla.addHeaderCell(celda("ID", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Categoría", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Descripción", negrita, TextAlignment.CENTER));

            for (Recurso recurso : recursos) {
                String categoria = recurso.getCategoria() == null
                        ? ""
                        : recurso.getCategoria().getDescripcion();
                tabla.addCell(celda(recurso.getId(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(categoria, normal, TextAlignment.LEFT));
                tabla.addCell(celda(recurso.getDescripcion(), normal, TextAlignment.LEFT));
            }
            document.add(tabla);
            document.add(new Paragraph("Total: " + recursos.size())
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
        }
        return absoluto;
    }

    public static Path reservas(List<Reserva> reservas, Path destino) throws Exception {
        Path absoluto = destino.toAbsolutePath();
        if (absoluto.getParent() != null) Files.createDirectories(absoluto.getParent());

        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont negrita = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        try (PdfWriter writer = new PdfWriter(absoluto.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            document.setMargins(30, 30, 30, 30);
            document.add(new Paragraph("Sistema de Reservas")
                    .setFont(negrita).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Listado de Reservas")
                    .setFont(negrita).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Generado: " + LocalDateTime.now().format(FECHA))
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));

            Table tabla = new Table(UnitValue.createPercentArray(
                    new float[]{1.3f, 2.4f, 1.5f, 1.8f, 2.2f, 1.2f}))
                    .useAllAvailableWidth();
            tabla.addHeaderCell(celda("ID", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Actividad", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Fecha", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Horario", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Recursos", negrita, TextAlignment.CENTER));
            tabla.addHeaderCell(celda("Estado", negrita, TextAlignment.CENTER));

            for (Reserva reserva : reservas) {
                String fecha = reserva.getFecha() == null ? "" : reserva.getFecha().format(SOLO_FECHA);
                String inicio = reserva.getHoraInicio() == null ? "" : reserva.getHoraInicio().format(HORA);
                String fin = reserva.getHoraFin() == null ? "" : reserva.getHoraFin().format(HORA);
                String horario = inicio.isEmpty() && fin.isEmpty() ? "" : inicio + " - " + fin;
                String recursos = reserva.getRecursos() == null ? "" : String.join(", ",
                        reserva.getRecursos().stream().map(Recurso::getId).toList());
                String estado = reserva.getEstado() == null ? "" : reserva.getEstado().toString();

                tabla.addCell(celda(reserva.getId(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(reserva.getActividad(), normal, TextAlignment.LEFT));
                tabla.addCell(celda(fecha, normal, TextAlignment.CENTER));
                tabla.addCell(celda(horario, normal, TextAlignment.CENTER));
                tabla.addCell(celda(recursos, normal, TextAlignment.LEFT));
                tabla.addCell(celda(estado, normal, TextAlignment.CENTER));
            }
            document.add(tabla);
            document.add(new Paragraph("Total: " + reservas.size())
                    .setFont(normal).setFontSize(9).setTextAlignment(TextAlignment.RIGHT));
        }
        return absoluto;
    }
}
