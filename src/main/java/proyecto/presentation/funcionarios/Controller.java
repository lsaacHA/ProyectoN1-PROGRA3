package proyecto.presentation.funcionarios;

import proyecto.logic.Funcionario;
import proyecto.logic.Service;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        model.setList(service.buscarFuncionarios(view.getIdBuscar(), view.getNombreBuscar()));
    }

    private void guardar() {
        try {
            Funcionario guardado = service.guardarFuncionario(
                    view.getSeleccionado(), view.getId(), view.getNombre(), view.getTelefono());
            model.setCurrent(guardado);
            view.limpiarFiltros();
            model.setList(service.buscarFuncionarios("", ""));
            model.setCurrent(null);
            view.mostrarMensaje("Funcionario guardado correctamente");
        } catch (Exception e) { view.mostrarError(e.getMessage()); }
    }

    private void borrar() {
        Funcionario seleccionado = view.getSeleccionado();
        if (seleccionado == null) { view.mostrarError("Debe seleccionar un funcionario"); return; }
        int respuesta = JOptionPane.showConfirmDialog(view.getPanel(),
                "¿Desea borrar al funcionario " + seleccionado.getId() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            service.borrarFuncionario(seleccionado);
            model.setCurrent(null);
            model.setList(service.buscarFuncionarios(view.getIdBuscar(), view.getNombreBuscar()));
            view.mostrarMensaje("Funcionario borrado correctamente");
        } catch (Exception e) { view.mostrarError(e.getMessage()); }
    }

    private void limpiar() {
        view.limpiarFiltros();
        model.setCurrent(null);
        model.setList(service.buscarFuncionarios("", ""));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == view.getBuscarFld()) buscar();
        else if (source == view.getGuardarFld()) guardar();
        else if (source == view.getBorrarFld()) borrar();
        else if (source == view.getLimpiarFld()) limpiar();
        else if (source == view.getImprimirFld()) view.imprimir();
        else buscar();
    }
}
