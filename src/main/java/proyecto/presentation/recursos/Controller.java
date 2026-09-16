package proyecto.presentation.recursos;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;
import proyecto.logic.Service;
import proyecto.presentation.PdfReportes;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

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
        model.setCategories(service.buscarCategorias(""));
        buscar();
    }

    private void buscar() {
        model.setList(service.buscarRecursos(view.getCategoriaFiltro(), view.getDescripcionFiltro()));
    }

    private void guardar() {
        try {
            Recurso guardado = service.guardarRecurso(
                    view.getSeleccionado(), view.getId(), view.getCategoria(), view.getDescripcion());
            model.setCurrent(guardado);
            view.limpiarFiltros();
            model.setList(service.buscarRecursos(null, ""));
            model.setCurrent(null);
            view.mostrarMensaje("Recurso guardado correctamente");
        } catch (Exception e) {
            view.mostrarError(e.getMessage());
        }
    }

    private void borrar() {
        Recurso seleccionado = view.getSeleccionado();
        if (seleccionado == null) {
            view.mostrarError("Debe seleccionar un recurso");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(view.getPanel(),
                "¿Desea borrar el recurso \"" + seleccionado.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            service.borrarRecurso(seleccionado);
            model.setCurrent(null);
            buscar();
            view.mostrarMensaje("Recurso borrado correctamente");
        } catch (Exception e) {
            view.mostrarError(e.getMessage());
        }
    }

    private void limpiar() {
        view.limpiarFiltros();
        model.setCurrent(null);
        model.setList(service.buscarRecursos(null, ""));
    }

    private void imprimir() {
        try {
            Path archivo = PdfReportes.recursos(model.getList(), Path.of("reportes", "recursos.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado en: " + archivo);
        } catch (Exception e) {
            view.mostrarError("No se pudo generar el PDF: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == view.getBuscarFld()) buscar();
        else if (source == view.getGuardarFld()) guardar();
        else if (source == view.getBorrarFld()) borrar();
        else if (source == view.getLimpiarFld()) limpiar();
        else if (source == view.getImprimirFld()) imprimir();
        else buscar();
    }
}
