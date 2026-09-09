package proyecto.presentation;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Test;
import proyecto.logic.Funcionario;
import proyecto.logic.Categoria;
import proyecto.logic.EstadoReserva;
import proyecto.logic.Recurso;
import proyecto.logic.Reserva;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Test
    void generaListadoDeReservasValido() throws Exception {
        Path destino = Path.of("target", "test-output", "reservas.pdf");
        Funcionario funcionario = new Funcionario("222", "María Pérez", "222222");
        Categoria categoria = new Categoria("CAT-000001", "Laptop");
        Recurso recurso = new Recurso("REC-001", categoria, "Laptop Windows");
        Reserva reserva = new Reserva(
                "RES-000001", funcionario, "Reunión de trabajo",
                LocalDate.of(2026, 9, 10), LocalTime.of(8, 0), LocalTime.of(10, 0),
                List.of(recurso), EstadoReserva.ACTIVA);

        PdfReportes.reservas(List.of(reserva), destino);

        assertTrue(Files.exists(destino));
        assertTrue(Files.size(destino) > 500);
        try (PdfDocument pdf = new PdfDocument(new PdfReader(destino.toString()))) {
            assertEquals(1, pdf.getNumberOfPages());
        }
    }
}
