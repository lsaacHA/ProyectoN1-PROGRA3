package proyecto.presentation.estadisticas;

import proyecto.logic.Service;
import proyecto.presentation.PdfReportes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.time.LocalDate;

public class Controller implements ActionListener {
    private final Model model;
    private final View view;
    private final Service service;

    public Controller(Model model, View view, Service service) {
        this.model = model;
        this.view = view;
        this.service = service;
        model.addPropertyChangeListener(view);
        view.setController(this);
        cargarRecursos();
        cargarActividades();
    }

    private void cargarRecursos() {
        try {
            LocalDate desde = view.getRecursosDesde();
            LocalDate hasta = view.getRecursosHasta();
            model.setRecursos(desde, hasta, service.estadisticasRecursos(desde, hasta));
        } catch (Exception e) {
            view.mostrarError(e.getMessage());
        }
    }

    private void cargarActividades() {
        try {
            LocalDate desde = view.getActividadesDesde();
            LocalDate hasta = view.getActividadesHasta();
            model.setActividades(desde, hasta, service.estadisticasActividades(desde, hasta));
        } catch (Exception e) {
            view.mostrarError(e.getMessage());
        }
    }

    private void imprimirRecursos() {
        try {
            Path archivo = PdfReportes.estadisticas("Estadísticas de recursos",
                    model.getRecursosDesde(), model.getRecursosHasta(), "Categoría",
                    model.getRecursos(), Path.of("reportes", "estadisticas-recursos.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado en: " + archivo);
        } catch (Exception e) {
            view.mostrarError("No se pudo generar el PDF: " + e.getMessage());
        }
    }

    private void imprimirActividades() {
        try {
            Path archivo = PdfReportes.estadisticas("Estadísticas de actividades",
                    model.getActividadesDesde(), model.getActividadesHasta(), "Semana",
                    model.getActividades(), Path.of("reportes", "estadisticas-actividades.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado en: " + archivo);
        } catch (Exception e) {
            view.mostrarError("No se pudo generar el PDF: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == view.getCargarRecursosFld()) cargarRecursos();
        else if (source == view.getImprimirRecursosFld()) imprimirRecursos();
        else if (source == view.getCargarActividadesFld()) cargarActividades();
        else if (source == view.getImprimirActividadesFld()) imprimirActividades();
    }
}
