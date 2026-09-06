package proyecto.logic;

import proyecto.data.Data;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/** Punto de acceso de la capa de presentación a la lógica y persistencia. */
public class Service {
    private final Data data;

    public Service() throws Exception {
        this(Data.DEFAULT_FILE);
    }

    public Service(Path archivo) throws Exception {
        data = Data.load(archivo);
    }

    public Optional<Usuario> findUser(String id) {
        if (id == null) return Optional.empty();

        Optional<Usuario> usuario = data.getUsuarios().stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();

        if (usuario.isPresent()) return usuario;

        return data.getFuncionarios().stream()
                .filter(item -> id.equals(item.getId()))
                .map(item -> (Usuario) item)
                .findFirst();
    }

    public Usuario login(String id, String clave) throws Exception {
        if (id == null || id.isBlank() || clave == null || clave.isBlank()) {
            throw new Exception("Debe indicar el ID y la clave");
        }

        Usuario usuario = findUser(id.trim())
                .orElseThrow(() -> new Exception("ID o clave incorrectos"));

        if (!usuario.getClave().equals(clave)) {
            throw new Exception("ID o clave incorrectos");
        }
        return usuario;
    }

    public void changePassword(Usuario usuario, String claveActual, String claveNueva) throws Exception {
        if (usuario == null || !usuario.getClave().equals(claveActual)) {
            throw new Exception("La clave actual es incorrecta");
        }
        if (claveNueva == null || claveNueva.isBlank()) {
            throw new Exception("La clave nueva es obligatoria");
        }
        usuario.setClave(claveNueva);
        store();
    }

    public void store() throws Exception {
        data.store();
    }

    public List<Usuario> getUsuarios() { return data.getUsuarios(); }
    public List<Funcionario> getFuncionarios() { return data.getFuncionarios(); }
    public List<Categoria> getCategorias() { return data.getCategorias(); }
    public List<Recurso> getRecursos() { return data.getRecursos(); }
    public List<Reserva> getReservas() { return data.getReservas(); }
}
