package proyecto.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceTest {
    @Test
    void creaAdministradorInicialYPermiteLogin() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-login-").resolve("datos.xml");
        Service service = new Service(archivo);

        Usuario usuario = service.login("admin", "admin");

        assertEquals(Rol.ADMINISTRADOR, usuario.getRol());
        assertThrows(Exception.class, () -> service.login("admin", "incorrecta"));
    }

    @Test
    void cambiaClaveYLaConservaEnXml() throws Exception {
        Path archivo = Files.createTempDirectory("reservas-clave-").resolve("datos.xml");
        Service service = new Service(archivo);
        Usuario admin = service.login("admin", "admin");

        service.changePassword(admin, "admin", "nueva");

        Service reloaded = new Service(archivo);
        assertEquals("admin", reloaded.login("admin", "nueva").getId());
    }
}
