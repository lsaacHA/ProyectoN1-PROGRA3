package proyecto.presentation.reservas;

import proyecto.logic.Funcionario;
import proyecto.logic.Reserva;
import proyecto.logic.ReservaExtraccion;
import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;
import proyecto.presentation.PdfReportes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller implements ActionListener {
    private final Model model;
    private final View view;
    private final Service service;
    private final Funcionario funcionario;

    public Controller(Model model, View view, Service service) throws Exception {
        this.model = model;
        this.view = view;
        this.service = service;
        this.funcionario = obtenerFuncionarioAutenticado();
        model.addPropertyChangeListener(view);
        view.setController(this);
        cargar();
    }

    private Funcionario obtenerFuncionarioAutenticado() throws Exception {
        Usuario usuario = Sesion.getUsuario();
        if (usuario == null) throw new Exception("No hay una sesión activa");
        return service.getFuncionarios().stream()
                .filter(item -> item.getId().equals(usuario.getId()))
                .findFirst()
                .orElseThrow(() -> new Exception("El usuario autenticado no es funcionario"));
    }

    private void cargar() {
        model.setCategories(service.getCategorias());
        model.setList(service.reservasDe(funcionario));
    }

    private void reservar() {
        try {
            Reserva reserva = service.reservar(
                    funcionario,
                    view.getActividad(),
                    view.getFecha(),
                    view.getHoraInicio(),
                    view.getHoraFin(),
                    view.getCategoriasSeleccionadas()
            );
            model.setCurrent(reserva);
            model.setList(service.reservasDe(funcionario));
            view.limpiar();
            view.mostrarMensaje("Reserva " + reserva.getId() + " guardada correctamente");
        } catch (Exception exception) {
            view.mostrarError(exception.getMessage());
        }
    }

    private void cancelar() {
        Reserva reserva = view.getReservaSeleccionada();
        if (reserva == null) {
            view.mostrarError("Debe seleccionar una reserva");
            return;
        }
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                view.getPanel(), "¿Desea cancelar la reserva " + reserva.getId() + "?",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (respuesta != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            service.cancelarReserva(reserva, funcionario);
            model.setList(service.reservasDe(funcionario));
            view.mostrarMensaje("Reserva cancelada correctamente");
        } catch (Exception exception) {
            view.mostrarError(exception.getMessage());
        }
    }

    private void imprimir() {
        try {
            Path archivo = PdfReportes.reservas(
                    model.getList(), Path.of("reportes", "reservas.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado correctamente en " + archivo);
        } catch (Exception exception) {
            view.mostrarError("No se pudo generar el reporte: " + exception.getMessage());
        }
    }

    private void extraer() {
        try {
            ReservaExtraccion resultado = service.extraerReserva(view.getFrase());
            if (resultado.getActividad() == null || resultado.getFecha() == null
                    || resultado.getHoraInicio() == null || resultado.getHoraFinal() == null)
                throw new Exception("La frase no contiene actividad, fecha y horario completos");

            List<proyecto.logic.Categoria> seleccionadas = new ArrayList<>();
            for (String descripcion : resultado.getCategoriasRecurso()) {
                model.getCategories().stream()
                        .filter(c -> c.getDescripcion().equalsIgnoreCase(descripcion))
                        .findFirst().ifPresent(seleccionadas::add);
            }
            if (seleccionadas.isEmpty())
                throw new Exception("La IA no identificó ninguna categoría registrada");

            view.cargarExtraccion(resultado.getActividad(), LocalDate.parse(resultado.getFecha()),
                    LocalTime.parse(resultado.getHoraInicio()), LocalTime.parse(resultado.getHoraFinal()),
                    seleccionadas);
            view.mostrarMensaje("Información extraída correctamente. Revísela antes de reservar");
        } catch (Exception exception) {
            view.mostrarError("No se pudo extraer la reserva: " + exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == view.getReservarFld()) reservar();
        else if (event.getSource() == view.getCancelarReservaFld()) cancelar();
        else if (event.getSource() == view.getLimpiarFld()) view.limpiar();
        else if (event.getSource() == view.getImprimirFld()) imprimir();
        else if (event.getSource() == view.getExtraerFld()) extraer();
    }
}
