package proyecto.presentation.actividades;

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
        cargar();
    }

    public void cargar() {
        try {
            LocalDate fecha = view.getFechaReferencia();
            model.setFecha(fecha);
            model.setList(service.actividadesSemana(fecha));
        } catch (Exception exception) {
            view.mostrarError(exception.getMessage());
        }
    }

    private void imprimir() {
        try {
            Path archivo = PdfReportes.actividades(model.getList(), model.getFecha(),
                    Path.of("reportes", "actividades-semana.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado correctamente en:\n" + archivo.toAbsolutePath());
        } catch (Exception exception) {
            view.mostrarError("No se pudo generar el reporte: " + exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == view.getCargarFld()) cargar();
        else if (event.getSource() == view.getImprimirFld()) imprimir();
    }
}
