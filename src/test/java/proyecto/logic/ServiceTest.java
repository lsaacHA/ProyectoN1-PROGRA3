package proyecto.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTest {
    @Test
    void creaUsuariosInicialesYPermiteLogin() throws Exception {
        Service service = nuevoService("reservas-login-");
        assertEquals(Rol.ADMINISTRADOR, service.login("111", "111").getRol());
        assertEquals(Rol.FUNCIONARIO, service.login("222", "222").getRol());
        assertThrows(Exception.class, () -> service.login("111", "incorrecta"));
    }

    @Test
    void cambiaClaveYLaConservaEnXml() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-clave-").resolve("datos.xml");
        Service service = new Service(archivo);
        service.changePassword(service.login("111", "111"), "111", "nueva");
        assertEquals("111", new Service(archivo).login("111", "nueva").getId());
    }

    @Test
    void reservaPrimerRecursoDisponibleYListaSoloLasPropias() throws Exception {
        Service service = nuevoService("reservas-manual-");
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria sala = new Categoria("CAT001", "Sala");
        Recurso recurso = new Recurso("REC001", sala, "Sala 1");
        service.getCategorias().add(sala);
        service.getRecursos().add(recurso);

        Reserva reserva = service.reservar(funcionario, "Reunión",
                LocalDate.now().plusDays(1), LocalTime.of(8, 0), LocalTime.of(9, 0), List.of(sala));

        assertEquals("RES-000001", reserva.getId());
        assertEquals(List.of(recurso), reserva.getRecursos());
        assertEquals(List.of(reserva), service.reservasDe(funcionario));
    }

    @Test
    void impideTraslapesYPermiteReutilizarRecursoCancelado() throws Exception {
        Service service = nuevoService("reservas-disponibilidad-");
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria sala = new Categoria("CAT001", "Sala de juntas");
        service.getCategorias().add(sala);
        service.getRecursos().add(new Recurso("REC001", sala, "Sala 1"));
        LocalDate fecha = LocalDate.now().plusDays(2);

        Reserva primera = service.reservar(funcionario, "Primera", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), List.of(sala));
        Exception error = assertThrows(Exception.class, () -> service.reservar(funcionario,
                "Traslapada", fecha, LocalTime.of(9, 0), LocalTime.of(11, 0), List.of(sala)));
        assertTrue(error.getMessage().contains("Sala de juntas"));

        service.cancelarReserva(primera, funcionario);
        Reserva segunda = service.reservar(funcionario, "Disponible", fecha,
                LocalTime.of(9, 0), LocalTime.of(11, 0), List.of(sala));
        assertEquals(EstadoReserva.CANCELADA, primera.getEstado());
        assertEquals(EstadoReserva.ACTIVA, segunda.getEstado());
    }

    @Test
    void validaLosDatosObligatorios() throws Exception {
        Service service = nuevoService("reservas-validacion-");
        Funcionario funcionario = service.getFuncionarios().get(0);
        assertThrows(Exception.class, () -> service.reservar(funcionario, "",
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(9, 0), List.of()));
    }

    private Service nuevoService(String prefijo) throws Exception {
        return new Service(Files.createTempDirectory(prefijo).resolve("datos.xml"));
    }
}
