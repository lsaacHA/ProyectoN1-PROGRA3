package proyecto.presentation.categorias;

import proyecto.logic.Categoria;
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
        buscar();
    }

    private void buscar() {
        model.setList(service.buscarCategorias(view.getBuscarDescripcion()));
    }

    private void guardar() {
        try {
            Categoria guardada = service.guardarCategoria(view.getSeleccionado(), view.getDescripcion());
            model.setCurrent(guardada);
            view.limpiarFiltros();
            model.setList(service.buscarCategorias(""));
            model.setCurrent(null);
            view.mostrarMensaje("Categoría guardada correctamente");
        } catch (Exception e) { view.mostrarError(e.getMessage()); }
    }

    private void borrar() {
        Categoria seleccionada = view.getSeleccionado();
        if (seleccionada == null) { view.mostrarError("Debe seleccionar una categoría"); return; }
        int respuesta = JOptionPane.showConfirmDialog(view.getPanel(),
                "¿Desea borrar la categoría \"" + seleccionada.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            service.borrarCategoria(seleccionada);
            model.setCurrent(null);
            model.setList(service.buscarCategorias(view.getBuscarDescripcion()));
            view.mostrarMensaje("Categoría borrada correctamente");
        } catch (Exception e) { view.mostrarError(e.getMessage()); }
    }

    private void limpiar() {
        view.limpiarFiltros();
        model.setCurrent(null);
        model.setList(service.buscarCategorias(""));
    }

    private void imprimir() {
        try {
            Path archivo = PdfReportes.categorias(model.getList(), Path.of("reportes", "categorias.pdf"));
            PdfReportes.abrir(archivo);
            view.mostrarMensaje("Reporte generado en: " + archivo);
        } catch (Exception e) {
            view.mostrarError("No se pudo generar el PDF: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == view.getBuscarButton()) buscar();
        else if (source == view.getImprimirButton()) imprimir();
        else if (source == view.getGuardarButton()) guardar();
        else if (source == view.getBorrarButton()) borrar();
        else if (source == view.getLimpiarButton()) limpiar();
        else buscar();
    }
}