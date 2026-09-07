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
import proyecto.logic.Funcionario;

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
}
