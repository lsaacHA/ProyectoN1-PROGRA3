package proyecto.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Service nuevoService() throws Exception {
        return new Service(Files.createTempDirectory("funcionarios-").resolve("datos.xml"));
    }
}
