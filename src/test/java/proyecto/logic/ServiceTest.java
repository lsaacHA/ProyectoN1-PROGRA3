package proyecto.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceTest {
    @Test
    void creaUsuariosInicialesYPermiteLogin() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-login-").resolve("datos.xml");
        Service service = new Service(archivo);

        Usuario admin = service.login("111", "111");
        Usuario funcionario = service.login("222", "222");

        assertEquals(Rol.ADMINISTRADOR, admin.getRol());
        assertEquals(Rol.FUNCIONARIO, funcionario.getRol());
        assertThrows(Exception.class, () -> service.login("111", "incorrecta"));
    }

    @Test
    void cambiaClaveYLaConservaEnXml() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-clave-").resolve("datos.xml");
        Service service = new Service(archivo);
        Usuario admin = service.login("111", "111");

        service.changePassword(admin, "111", "nueva");

        Service reloaded = new Service(archivo);
        assertEquals("111", reloaded.login("111", "nueva").getId());
    }
}
