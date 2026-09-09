package proyecto.logic;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import proyecto.data.Data;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public ReservaExtraccion extraerReserva(String frase) throws Exception {
        if (frase == null || frase.isBlank())
            throw new Exception("Debe escribir una frase para extraer la reserva");
        if (data.getCategorias().isEmpty())
            throw new Exception("No hay categorías registradas para interpretar la frase");

        OpenAiChatModel modelo = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        ReservaExtractorService extractor = AiServices.create(ReservaExtractorService.class, modelo);
        String categorias = data.getCategorias().stream()
                .map(Categoria::getDescripcion)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .reduce((primera, siguiente) -> primera + "\n" + siguiente)
                .orElse("");
        ReservaExtraccion resultado = extractor.extraer(frase.trim(), categorias, LocalDate.now().toString());
        if (resultado == null) throw new Exception("La IA no devolvió información de la reserva");
        return resultado;
    }

    public List<Funcionario> buscarFuncionarios(String id, String nombre) {
        String filtroId = normalizar(id);
        String filtroNombre = normalizar(nombre);
        return data.getFuncionarios().stream()
                .filter(f -> filtroId.isEmpty() || normalizar(f.getId()).contains(filtroId))
                .filter(f -> filtroNombre.isEmpty() || normalizar(f.getNombre()).contains(filtroNombre))
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

    public List<Categoria> buscarCategorias(String descripcion) {
        String filtro = normalizar(descripcion);
        return data.getCategorias().stream()
                .filter(c -> filtro.isEmpty() || normalizar(c.getDescripcion()).contains(filtro))
                .sorted(Comparator.comparing(Categoria::getId))
                .toList();
    }

    public Categoria guardarCategoria(Categoria original, String descripcion) throws Exception {
        if (descripcion == null || descripcion.isBlank())
            throw new Exception("La descripción es obligatoria");
        String descripcionLimpia = descripcion.trim();
        if (original == null) {
            Categoria nueva = new Categoria(generarIdCategoria(), descripcionLimpia);
            data.getCategorias().add(nueva);
            store();
            return nueva;
        }
        original.setDescripcion(descripcionLimpia);
        store();
        return original;
    }

    public void borrarCategoria(Categoria categoria) throws Exception {
        if (categoria == null) throw new Exception("Debe seleccionar una categoría");
        boolean enUso = data.getRecursos().stream()
                .anyMatch(r -> mismaCategoria(categoria, r.getCategoria()));
        if (enUso)
            throw new Exception("No se puede borrar: la categoría está siendo utilizada por al menos un recurso");
        if (!data.getCategorias().remove(categoria))
            throw new Exception("La categoría ya no existe");
        store();
    }

    private String generarIdCategoria() {
        int max = 0;
        for (Categoria c : data.getCategorias()) {
            String numero = c.getId() == null ? "" : c.getId().replaceAll("[^0-9]", "");
            if (!numero.isEmpty()) {
                try { max = Math.max(max, Integer.parseInt(numero)); }
                catch (NumberFormatException ignored) { }
            }
        }
        return String.format("CAT-%06d", max + 1);
    }

    public List<Recurso> buscarRecursos(Categoria categoria, String descripcion) {
        String filtro = normalizar(descripcion);
        return data.getRecursos().stream()
                .filter(r -> categoria == null || mismaCategoria(categoria, r.getCategoria()))
                .filter(r -> filtro.isEmpty() || normalizar(r.getDescripcion()).contains(filtro))
                .sorted(Comparator.comparing(Recurso::getId))
                .toList();
    }

    public Recurso guardarRecurso(Recurso original, String id, Categoria categoria,
                                   String descripcion) throws Exception {
        validarRecurso(id, categoria, descripcion);
        String idLimpio = id.trim();
        String descripcionLimpia = descripcion.trim();
        Optional<Recurso> existente = data.getRecursos().stream()
                .filter(r -> idLimpio.equalsIgnoreCase(r.getId()))
                .findFirst();

        if (original == null) {
            if (existente.isPresent()) throw new Exception("Ya existe un recurso con el ID " + idLimpio);
            Recurso nuevo = new Recurso(idLimpio, categoria, descripcionLimpia);
            data.getRecursos().add(nuevo);
            store();
            return nuevo;
        }

        if (!original.getId().equals(idLimpio))
            throw new Exception("El ID de un recurso existente no se puede modificar");
        original.setCategoria(categoria);
        original.setDescripcion(descripcionLimpia);
        store();
        return original;
    }

    public void borrarRecurso(Recurso recurso) throws Exception {
        if (recurso == null) throw new Exception("Debe seleccionar un recurso");
        boolean enUso = data.getReservas().stream()
                .filter(r -> r.getRecursos() != null)
                .flatMap(r -> r.getRecursos().stream())
                .anyMatch(asignado -> asignado != null && recurso.getId().equals(asignado.getId()));
        if (enUso)
            throw new Exception("No se puede borrar: el recurso está asignado a una reserva");
        if (!data.getRecursos().remove(recurso))
            throw new Exception("El recurso ya no existe");
        store();
    }

    private void validarRecurso(String id, Categoria categoria, String descripcion) throws Exception {
        if (id == null || id.isBlank()) throw new Exception("El ID es obligatorio");
        if (categoria == null) throw new Exception("Debe seleccionar una categoría");
        if (descripcion == null || descripcion.isBlank())
            throw new Exception("La descripción es obligatoria");
        if (!id.trim().matches("[A-Za-z0-9_-]+"))
            throw new Exception("El ID solo puede contener letras, números, guion y guion bajo");
        boolean categoriaExiste = data.getCategorias().stream()
                .anyMatch(c -> mismaCategoria(c, categoria));
        if (!categoriaExiste) throw new Exception("La categoría seleccionada ya no existe");
    }

    public Reserva reservar(Funcionario funcionario, String actividad, LocalDate fecha,
                            LocalTime horaInicio, LocalTime horaFin,
                            List<Categoria> categorias) throws Exception {
        validarReserva(funcionario, actividad, fecha, horaInicio, horaFin, categorias);
        Funcionario registrado = data.getFuncionarios().stream()
                .filter(f -> f.getId().equals(funcionario.getId()))
                .findFirst().orElseThrow(() -> new Exception("El funcionario no está registrado"));

        List<Recurso> asignados = new ArrayList<>();
        List<String> faltantes = new ArrayList<>();
        for (Categoria categoria : categorias) {
            Optional<Recurso> disponible = data.getRecursos().stream()
                    .filter(r -> mismaCategoria(r.getCategoria(), categoria))
                    .filter(r -> estaDisponible(r, fecha, horaInicio, horaFin))
                    .findFirst();
            if (disponible.isPresent()) asignados.add(disponible.get());
            else faltantes.add(categoria.getDescripcion());
        }
        if (!faltantes.isEmpty())
            throw new Exception("No hay recursos disponibles para: " + String.join(", ", faltantes));

        Reserva reserva = new Reserva(siguienteIdReserva(), registrado, actividad.trim(), fecha,
                horaInicio, horaFin, asignados, EstadoReserva.ACTIVA);
        data.getReservas().add(reserva);
        store();
        return reserva;
    }

    public List<Reserva> reservasDe(Funcionario funcionario) {
        if (funcionario == null || funcionario.getId() == null) return List.of();
        return data.getReservas().stream()
                .filter(r -> r.getFuncionario() != null
                        && funcionario.getId().equals(r.getFuncionario().getId()))
                .sorted(Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicio))
                .toList();
    }

    /** Reservas activas comprendidas en la semana (lunes a domingo) de la fecha indicada. */
    public List<Reserva> actividadesSemana(LocalDate fechaReferencia) throws Exception {
        if (fechaReferencia == null) throw new Exception("Debe indicar una fecha de referencia");
        LocalDate lunes = fechaReferencia.with(java.time.temporal.TemporalAdjusters.previousOrSame(
                java.time.DayOfWeek.MONDAY));
        LocalDate domingo = lunes.plusDays(6);
        return data.getReservas().stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA)
                .filter(r -> r.getFecha() != null
                        && !r.getFecha().isBefore(lunes)
                        && !r.getFecha().isAfter(domingo))
                .sorted(Comparator.comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHoraInicio))
                .toList();
    }

    public void cancelarReserva(Reserva reserva, Funcionario funcionario) throws Exception {
        if (reserva == null) throw new Exception("Debe seleccionar una reserva");
        if (funcionario == null || reserva.getFuncionario() == null
                || !funcionario.getId().equals(reserva.getFuncionario().getId()))
            throw new Exception("La reserva no pertenece al funcionario autenticado");
        if (reserva.getEstado() != EstadoReserva.ACTIVA)
            throw new Exception("La reserva ya está cancelada");
        if (reserva.getFecha() == null || !reserva.getFecha().isAfter(LocalDate.now()))
            throw new Exception("Solamente se pueden cancelar reservas futuras");
        reserva.setEstado(EstadoReserva.CANCELADA);
        store();
    }

    private void validarReserva(Funcionario funcionario, String actividad, LocalDate fecha,
                                LocalTime inicio, LocalTime fin,
                                List<Categoria> categorias) throws Exception {
        if (funcionario == null) throw new Exception("Debe ingresar como funcionario");
        if (actividad == null || actividad.isBlank()) throw new Exception("La actividad es obligatoria");
        if (fecha == null) throw new Exception("La fecha es obligatoria");
        if (fecha.isBefore(LocalDate.now())) throw new Exception("La fecha no puede estar en el pasado");
        if (inicio == null || fin == null) throw new Exception("Debe indicar el horario");
        if (!fin.isAfter(inicio)) throw new Exception("La hora final debe ser posterior a la inicial");
        if (fecha.equals(LocalDate.now()) && !inicio.isAfter(LocalTime.now()))
            throw new Exception("La hora inicial debe estar en el futuro");
        if (categorias == null || categorias.isEmpty())
            throw new Exception("Debe seleccionar al menos una categoría");
        if (categorias.stream().anyMatch(c -> c == null || c.getId() == null))
            throw new Exception("La selección contiene una categoría inválida");
        if (categorias.stream().map(Categoria::getId).distinct().count() != categorias.size())
            throw new Exception("No debe repetir categorías");
        boolean todasRegistradas = categorias.stream().allMatch(seleccionada ->
                data.getCategorias().stream().anyMatch(registrada -> mismaCategoria(registrada, seleccionada)));
        if (!todasRegistradas) throw new Exception("La selección contiene una categoría no registrada");
    }

    private boolean estaDisponible(Recurso recurso, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        return data.getReservas().stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA && fecha.equals(r.getFecha()))
                .filter(r -> r.getRecursos() != null && r.getRecursos().stream()
                        .anyMatch(asignado -> asignado != null
                                && recurso.getId().equals(asignado.getId())))
                .noneMatch(r -> inicio.isBefore(r.getHoraFin()) && fin.isAfter(r.getHoraInicio()));
    }

    private String siguienteIdReserva() {
        int maximo = data.getReservas().stream().map(Reserva::getId)
                .filter(id -> id != null && id.startsWith("RES-"))
                .map(id -> id.substring(4)).filter(n -> n.matches("\\d+"))
                .mapToInt(Integer::parseInt).max().orElse(0);
        return "RES-%06d".formatted(maximo + 1);
    }

    private boolean mismaCategoria(Categoria primera, Categoria segunda) {
        return primera != null && segunda != null && primera.getId() != null
                && primera.getId().equals(segunda.getId());
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase();
    }

    public void store() throws Exception { data.store(); }
    public List<Usuario> getUsuarios() { return data.getUsuarios(); }
    public List<Funcionario> getFuncionarios() { return data.getFuncionarios(); }
    public List<Categoria> getCategorias() { return data.getCategorias(); }
    public List<Recurso> getRecursos() { return data.getRecursos(); }
    public List<Reserva> getReservas() { return data.getReservas(); }
}
