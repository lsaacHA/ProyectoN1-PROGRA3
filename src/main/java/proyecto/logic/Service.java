package proyecto.logic;

import proyecto.data.Data;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Reserva reservar(Funcionario funcionario, String actividad, LocalDate fecha,
                            LocalTime horaInicio, LocalTime horaFin,
                            List<Categoria> categorias) throws Exception {
        validarReserva(funcionario, actividad, fecha, horaInicio, horaFin, categorias);

        Funcionario funcionarioRegistrado = data.getFuncionarios().stream()
                .filter(item -> item.getId().equals(funcionario.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("El funcionario no está registrado"));

        List<Recurso> asignados = new ArrayList<>();
        List<String> noDisponibles = new ArrayList<>();

        for (Categoria categoria : categorias) {
            Optional<Recurso> disponible = data.getRecursos().stream()
                    .filter(recurso -> mismaCategoria(recurso.getCategoria(), categoria))
                    .filter(recurso -> estaDisponible(recurso, fecha, horaInicio, horaFin))
                    .findFirst();

            if (disponible.isPresent()) {
                asignados.add(disponible.get());
            } else {
                noDisponibles.add(categoria.getDescripcion());
            }
        }

        if (!noDisponibles.isEmpty()) {
            throw new Exception("No hay recursos disponibles para: " + String.join(", ", noDisponibles));
        }

        Reserva reserva = new Reserva(
                siguienteIdReserva(), funcionarioRegistrado, actividad.trim(), fecha,
                horaInicio, horaFin, asignados, EstadoReserva.ACTIVA
        );
        data.getReservas().add(reserva);
        store();
        return reserva;
    }

    public List<Reserva> reservasDe(Funcionario funcionario) {
        if (funcionario == null || funcionario.getId() == null) {
            return List.of();
        }
        return data.getReservas().stream()
                .filter(reserva -> reserva.getFuncionario() != null)
                .filter(reserva -> funcionario.getId().equals(reserva.getFuncionario().getId()))
                .sorted(Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicio))
                .collect(Collectors.toList());
    }

    public void cancelarReserva(Reserva reserva, Funcionario funcionario) throws Exception {
        if (reserva == null) {
            throw new Exception("Debe seleccionar una reserva");
        }
        if (funcionario == null || reserva.getFuncionario() == null
                || !funcionario.getId().equals(reserva.getFuncionario().getId())) {
            throw new Exception("La reserva no pertenece al funcionario autenticado");
        }
        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new Exception("La reserva ya está cancelada");
        }
        if (reserva.getFecha() == null || !reserva.getFecha().isAfter(LocalDate.now())) {
            throw new Exception("Solamente se pueden cancelar reservas futuras");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        store();
    }

    private void validarReserva(Funcionario funcionario, String actividad, LocalDate fecha,
                                LocalTime horaInicio, LocalTime horaFin,
                                List<Categoria> categorias) throws Exception {
        if (funcionario == null) throw new Exception("Debe ingresar como funcionario");
        if (actividad == null || actividad.isBlank()) throw new Exception("La actividad es obligatoria");
        if (fecha == null) throw new Exception("La fecha es obligatoria");
        if (fecha.isBefore(LocalDate.now())) throw new Exception("La fecha no puede estar en el pasado");
        if (horaInicio == null || horaFin == null) throw new Exception("Debe indicar el horario");
        if (!horaFin.isAfter(horaInicio)) throw new Exception("La hora final debe ser posterior a la inicial");
        if (categorias == null || categorias.isEmpty()) {
            throw new Exception("Debe seleccionar al menos una categoría");
        }
        long distintas = categorias.stream().map(Categoria::getId).distinct().count();
        if (distintas != categorias.size()) throw new Exception("No debe repetir categorías");
    }

    private boolean estaDisponible(Recurso recurso, LocalDate fecha,
                                    LocalTime horaInicio, LocalTime horaFin) {
        return data.getReservas().stream()
                .filter(reserva -> reserva.getEstado() == EstadoReserva.ACTIVA)
                .filter(reserva -> fecha.equals(reserva.getFecha()))
                .filter(reserva -> reserva.getRecursos() != null)
                .filter(reserva -> reserva.getRecursos().stream()
                        .anyMatch(asignado -> asignado.getId().equals(recurso.getId())))
                .noneMatch(reserva -> horaInicio.isBefore(reserva.getHoraFin())
                        && horaFin.isAfter(reserva.getHoraInicio()));
    }

    private boolean mismaCategoria(Categoria primera, Categoria segunda) {
        return primera != null && segunda != null && primera.getId().equals(segunda.getId());
    }

    private String siguienteIdReserva() {
        int maximo = data.getReservas().stream()
                .map(Reserva::getId)
                .filter(id -> id != null && id.startsWith("RES-"))
                .map(id -> id.substring(4))
                .filter(numero -> numero.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return "RES-%06d".formatted(maximo + 1);
    }

    public void store() throws Exception { data.store(); }
    public List<Usuario> getUsuarios() { return data.getUsuarios(); }
    public List<Funcionario> getFuncionarios() { return data.getFuncionarios(); }
    public List<Categoria> getCategorias() { return data.getCategorias(); }
    public List<Recurso> getRecursos() { return data.getRecursos(); }
    public List<Reserva> getReservas() { return data.getReservas(); }
}
