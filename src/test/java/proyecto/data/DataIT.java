package proyecto.data;

import org.junit.jupiter.api.Test;
import proyecto.logic.Categoria;
import proyecto.logic.EstadoReserva;
import proyecto.logic.Funcionario;
import proyecto.logic.Recurso;
import proyecto.logic.Reserva;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataIT {
    @Test
    void guardaYCargaTodoElModeloConReferencias() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-xml-").resolve("datos.xml");
        Data data = Data.load(archivo);

        Funcionario funcionario = new Funcionario("F001", "María Pérez", "22222222");
        Categoria categoria = new Categoria("CAT001", "Laptop Windows");
        Recurso recurso = new Recurso("REC001", categoria, "Laptop 238715");
        Reserva reserva = new Reserva(
                "RES001", funcionario, "Reunión de trabajo",
                LocalDate.of(2026, 8, 14), LocalTime.of(8, 0), LocalTime.of(10, 0),
                List.of(recurso), EstadoReserva.ACTIVA
        );

        data.getFuncionarios().add(funcionario);
        data.getCategorias().add(categoria);
        data.getRecursos().add(recurso);
        data.getReservas().add(reserva);
        data.store();

        Data loaded = Data.load(archivo);
        assertEquals(LocalDate.of(2026, 8, 14), loaded.getReservas().get(0).getFecha());
        assertEquals(LocalTime.of(8, 0), loaded.getReservas().get(0).getHoraInicio());
        assertSame(loaded.getCategorias().get(0), loaded.getRecursos().get(0).getCategoria());
        assertSame(loaded.getFuncionarios().get(0), loaded.getReservas().get(0).getFuncionario());
        assertTrue(Files.size(archivo) > 0);
    }
}
