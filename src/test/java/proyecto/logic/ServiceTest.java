package proyecto.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTest {
    @Test
    void creaUsuariosInicialesYPermiteLogin() throws Exception {
        Service service = nuevoService();
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
    void creaBuscaModificaYBorraFuncionario() throws Exception {
        Path archivo = Files.createTempDirectory("funcionarios-crud-").resolve("datos.xml");
        Service service = new Service(archivo);
        Funcionario creado = service.guardarFuncionario(null, "333", "María Pérez", "8888-9999");
        assertEquals(creado, service.buscarFuncionarios("33", "maría").get(0));

        service.guardarFuncionario(creado, "333", "María Sol Pérez", "2222-3333");
        assertEquals("María Sol Pérez", new Service(archivo).buscarFuncionarios("333", "").get(0).getNombre());

        service.borrarFuncionario(creado);
        assertEquals(0, service.buscarFuncionarios("333", "").size());
    }

    @Test
    void rechazaIdRepetidoYDatosInvalidos() throws Exception {
        Service service = nuevoService();
        assertThrows(Exception.class,
                () -> service.guardarFuncionario(null, "222", "Otro", "88889999"));
        assertThrows(Exception.class,
                () -> service.guardarFuncionario(null, "333", "", "teléfono"));
    }

    @Test
    void creaBuscaModificaYPersisteRecursos() throws Exception {
        Path archivo = Files.createTempDirectory("recursos-crud-").resolve("datos.xml");
        Service service = new Service(archivo);
        Categoria categoria = service.guardarCategoria(null, "Equipo portátil");
        Recurso creado = service.guardarRecurso(null, "REC-001", categoria, "Laptop Windows");

        assertEquals(creado, service.buscarRecursos(categoria, "wind").get(0));
        service.guardarRecurso(creado, "REC-001", categoria, "Laptop Windows 11");

        Service recargado = new Service(archivo);
        assertEquals("Laptop Windows 11",
                recargado.buscarRecursos(null, "windows 11").get(0).getDescripcion());
    }

    @Test
    void rechazaRecursoRepetidoYPermiteBorrarlo() throws Exception {
        Service service = nuevoService();
        Categoria categoria = service.guardarCategoria(null, "Sala");
        Recurso recurso = service.guardarRecurso(null, "REC-001", categoria, "Sala de juntas");

        assertThrows(Exception.class,
                () -> service.guardarRecurso(null, "REC-001", categoria, "Duplicado"));
        service.borrarRecurso(recurso);
        assertEquals(0, service.buscarRecursos(null, "").size());
    }

    @Test
    void reservaPrimerRecursoDisponibleYListaSoloLasPropias() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria sala = service.guardarCategoria(null, "Sala");
        Recurso recurso = service.guardarRecurso(null, "REC001", sala, "Sala 1");

        Reserva reserva = service.reservar(funcionario, "Reunión", LocalDate.now().plusDays(1),
                LocalTime.of(8, 0), LocalTime.of(9, 0), List.of(sala));

        assertEquals("RES-000001", reserva.getId());
        assertEquals(List.of(recurso), reserva.getRecursos());
        assertEquals(List.of(reserva), service.reservasDe(funcionario));
    }

    @Test
    void impideTraslapesYPermiteReutilizarRecursoCancelado() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria sala = service.guardarCategoria(null, "Sala de juntas");
        service.guardarRecurso(null, "REC001", sala, "Sala 1");
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
    void validaLosDatosObligatoriosDeReserva() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        assertThrows(Exception.class, () -> service.reservar(funcionario, "",
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(9, 0), List.of()));
    }

    @Test
    void rechazaCategoriasQueNoPertenecenAlXml() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria ajena = new Categoria("CAT-AJENA", "Categoría ajena");
        Exception error = assertThrows(Exception.class, () -> service.reservar(funcionario, "Reunión",
                LocalDate.now().plusDays(1), LocalTime.of(8, 0), LocalTime.of(9, 0), List.of(ajena)));
        assertTrue(error.getMessage().contains("no registrada"));
    }

    @Test
    void listaSolamenteActividadesActivasDeLaSemana() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria categoria = service.guardarCategoria(null, "Sala");
        service.guardarRecurso(null, "REC-SEMANA", categoria, "Sala semanal");
        LocalDate miercoles = LocalDate.now().plusWeeks(2).with(java.time.DayOfWeek.WEDNESDAY);

        Reserva activa = service.reservar(funcionario, "Charla técnica", miercoles,
                LocalTime.of(8, 0), LocalTime.of(10, 0), List.of(categoria));
        Reserva cancelada = service.reservar(funcionario, "Reunión cancelada", miercoles.plusDays(1),
                LocalTime.of(11, 0), LocalTime.of(12, 0), List.of(categoria));
        service.cancelarReserva(cancelada, funcionario);

        assertEquals(List.of(activa), service.actividadesSemana(miercoles));
        assertThrows(Exception.class, () -> service.actividadesSemana(null));
    }

    @Test
    void calculaEstadisticasDeRecursosYActividadesPorSemana() throws Exception {
        Service service = nuevoService();
        Funcionario funcionario = service.getFuncionarios().get(0);
        Categoria sala = service.guardarCategoria(null, "Sala");
        Categoria laptop = service.guardarCategoria(null, "Laptop");
        service.guardarCategoria(null, "Proyector sin reservas");
        service.guardarRecurso(null, "SALA-1", sala, "Sala 1");
        service.guardarRecurso(null, "LAP-1", laptop, "Laptop 1");
        LocalDate lunes = LocalDate.now().plusWeeks(3).with(java.time.DayOfWeek.MONDAY);

        service.reservar(funcionario, "Reunión", lunes,
                LocalTime.of(8, 0), LocalTime.of(9, 0), List.of(sala, laptop));
        Reserva cancelada = service.reservar(funcionario, "Cancelada", lunes.plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(11, 0), List.of(sala));
        service.cancelarReserva(cancelada, funcionario);

        Map<String, Integer> recursos = service.estadisticasRecursos(lunes, lunes.plusDays(6));
        Map<String, Integer> actividades = service.estadisticasActividades(lunes, lunes.plusDays(6));
        assertEquals(1, recursos.get("Sala"));
        assertEquals(1, recursos.get("Laptop"));
        assertTrue(!recursos.containsKey("Proyector sin reservas"));
        assertEquals(1, actividades.values().iterator().next());
    }

    @Test
    void rechazaPeriodoEstadisticoInvertido() throws Exception {
        Service service = nuevoService();
        LocalDate hoy = LocalDate.now();
        assertThrows(Exception.class, () -> service.estadisticasRecursos(hoy, hoy.minusDays(1)));
        assertThrows(Exception.class, () -> service.estadisticasActividades(null, hoy));
        assertTrue(service.estadisticasActividades(hoy, hoy.plusWeeks(4)).isEmpty());
    }

    private Service nuevoService() throws Exception {
        return new Service(Files.createTempDirectory("funcionarios-").resolve("datos.xml"));
    }
}
