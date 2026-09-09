package proyecto.presentation;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Test;
import proyecto.logic.Funcionario;
import proyecto.logic.Categoria;
import proyecto.logic.Recurso;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfReportesTest {
    @Test
    void generaListadoDeFuncionariosValido() throws Exception {
        Path destino = Path.of("target", "test-output", "funcionarios.pdf");
        PdfReportes.funcionarios(List.of(
                new Funcionario("111", "Juan Pérez", "3323"),
                new Funcionario("222", "María Pérez", "222222")
        ), destino);

        assertTrue(Files.exists(destino));
        assertTrue(Files.size(destino) > 500);
        try (PdfDocument pdf = new PdfDocument(new PdfReader(destino.toString()))) {
            assertEquals(1, pdf.getNumberOfPages());
        }
    }

    @Test
    void generaListadoDeRecursosValido() throws Exception {
        Path destino = Path.of("target", "test-output", "recursos.pdf");
        Categoria categoria = new Categoria("CAT-000001", "Laptop");
        PdfReportes.recursos(List.of(
                new Recurso("REC-001", categoria, "Laptop Windows")
        ), destino);

        assertTrue(Files.exists(destino));
        assertTrue(Files.size(destino) > 500);
        try (PdfDocument pdf = new PdfDocument(new PdfReader(destino.toString()))) {
            assertEquals(1, pdf.getNumberOfPages());
        }
    }
}
