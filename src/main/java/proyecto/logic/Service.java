package proyecto.logic;

import proyecto.data.Data;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Punto de acceso de la capa de presentación a la lógica y persistencia. */
public class Service {
    private final Data data;

    public Service() throws Exception { this(Data.DEFAULT_FILE); }
    public Service(Path archivo) throws Exception { data = Data.load(archivo); }

    public Optional<Usuario> findUser(String id) {
        if (id == null) return Optional.empty();
        Optional<Usuario> usuario = data.getUsuarios().stream()
                .filter(item -> id.equals(item.getId())).findFirst();
        if (usuario.isPresent()) return usuario;
        return data.getFuncionarios().stream()
                .filter(item -> id.equals(item.getId()))
                .map(item -> (Usuario) item).findFirst();
    }

    public Usuario login(String id, String clave) throws Exception {
        if (id == null || id.isBlank() || clave == null || clave.isBlank())
            throw new Exception("Debe indicar el ID y la clave");
        Usuario usuario = findUser(id.trim())
                .orElseThrow(() -> new Exception("ID o clave incorrectos"));
        if (!usuario.getClave().equals(clave)) throw new Exception("ID o clave incorrectos");
        return usuario;
    }

    public void changePassword(Usuario usuario, String claveActual, String claveNueva) throws Exception {
        if (usuario == null || !usuario.getClave().equals(claveActual))
            throw new Exception("La clave actual es incorrecta");
        if (claveNueva == null || claveNueva.isBlank())
            throw new Exception("La clave nueva es obligatoria");
        usuario.setClave(claveNueva);
        store();
    }

    public List<Funcionario> buscarFuncionarios(String id, String nombre) {
        String filtroId = id == null ? "" : id.trim().toLowerCase();
        String filtroNombre = nombre == null ? "" : nombre.trim().toLowerCase();
        return data.getFuncionarios().stream()
                .filter(f -> filtroId.isEmpty() || f.getId().toLowerCase().contains(filtroId))
                .filter(f -> filtroNombre.isEmpty() || f.getNombre().toLowerCase().contains(filtroNombre))
                .sorted(Comparator.comparing(Funcionario::getId))
                .toList();
    }

    public Funcionario guardarFuncionario(Funcionario original, String id,
                                           String nombre, String telefono) throws Exception {
        validarFuncionario(id, nombre, telefono);
        String idLimpio = id.trim();
        String nombreLimpio = nombre.trim();
        String telefonoLimpio = telefono.trim();

        Optional<Usuario> existente = findUser(idLimpio);
        if (original == null) {
            if (existente.isPresent()) throw new Exception("Ya existe un usuario con el ID " + idLimpio);
            Funcionario nuevo = new Funcionario(idLimpio, nombreLimpio, telefonoLimpio);
            data.getFuncionarios().add(nuevo);
            store();
            return nuevo;
        }

        if (!original.getId().equals(idLimpio))
            throw new Exception("El ID de un funcionario existente no se puede modificar");
        original.setNombre(nombreLimpio);
        original.setTelefono(telefonoLimpio);
        store();
        return original;
    }

    public void borrarFuncionario(Funcionario funcionario) throws Exception {
        if (funcionario == null) throw new Exception("Debe seleccionar un funcionario");
        boolean tieneReservas = data.getReservas().stream()
                .anyMatch(r -> r.getFuncionario() != null
                        && funcionario.getId().equals(r.getFuncionario().getId()));
        if (tieneReservas)
            throw new Exception("No se puede borrar porque el funcionario tiene reservas registradas");
        if (!data.getFuncionarios().remove(funcionario))
            throw new Exception("El funcionario ya no existe");
        store();
    }

    private void validarFuncionario(String id, String nombre, String telefono) throws Exception {
        if (id == null || id.isBlank()) throw new Exception("El ID es obligatorio");
        if (nombre == null || nombre.isBlank()) throw new Exception("El nombre es obligatorio");
        if (telefono == null || telefono.isBlank()) throw new Exception("El teléfono es obligatorio");
        if (!id.trim().matches("[A-Za-z0-9_-]+"))
            throw new Exception("El ID solo puede contener letras, números, guion y guion bajo");
        if (!telefono.trim().matches("[0-9+() -]+"))
            throw new Exception("El teléfono contiene caracteres inválidos");
    }

    public void store() throws Exception { data.store(); }
    public List<Usuario> getUsuarios() { return data.getUsuarios(); }
    public List<Funcionario> getFuncionarios() { return data.getFuncionarios(); }
    public List<Categoria> getCategorias() { return data.getCategorias(); }
    public List<Recurso> getRecursos() { return data.getRecursos(); }
    public List<Reserva> getReservas() { return data.getReservas(); }
}
