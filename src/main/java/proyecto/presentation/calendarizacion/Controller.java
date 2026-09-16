package proyecto.presentation.calendarizacion;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;
import proyecto.logic.Service;
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

    private static final List<LocalTime> HORAS = generarHoras();

    public Controller(Model model, View view, Service service) {
        this.model = model;
        this.view = view;
        this.service = service;
        model.addPropertyChangeListener(view);
        view.setController(this);
        model.setCategorias(service.getCategorias());
    }

    private static List<LocalTime> generarHoras() {
        List<LocalTime> lista = new ArrayList<>();
        for (int hora = 6; hora <= 22; hora++) lista.add(LocalTime.of(hora, 0));
        return lista;
    }

    private void cargar() {
        try {
            LocalDate fecha = view.getFecha();
            Categoria categoria = view.getCategoriaSeleccionada();
            if (fecha == null) throw new Exception("Debe seleccionar una fecha");
            if (categoria == null) throw new Exception("Debe seleccionar una categoría");

            List<Recurso> recursos = service.buscarRecursos(categoria, "");
            List<List<String>> matriz = service.calendarizarRecursos(fecha, recursos, HORAS);
            model.setMatriz(recursos, HORAS, matriz);
        } catch (Exception exception) {
            view.mostrarError(exception.getMessage());
        }
    }

    private void imprimir() {
        try {
            if (model.getRecursos().isEmpty())
                throw new Exception("Primero debe cargar la calendarización");
            Path archivo = PdfReportes.calendarizacion(
                    view.getFecha(), view.getCategoriaSeleccionada(),
                    model.getRecursos(), model.getHoras(), model.getMatriz(),
                    Path.of("reportes", "calendarizacion.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado en: " + archivo);
        } catch (Exception exception) {
            view.mostrarError("No se pudo generar el reporte: " + exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == view.getCargarButton()) cargar();
        else if (event.getSource() == view.getImprimirButton()) imprimir();
    }
}