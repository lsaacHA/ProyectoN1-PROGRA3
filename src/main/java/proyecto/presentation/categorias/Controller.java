package proyecto.presentation.categorias;

import proyecto.logic.Categoria;

import javax.swing.*;
import java.util.List;

public class Controller {

    private View view;
    private Model model;
    private TableModel tableModel;

    private static final int[] COLUMNAS = {TableModel.ID, TableModel.DESCRIPCION};

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        this.tableModel = new TableModel(COLUMNAS, model.getCategorias());
        view.getTable1().setModel(tableModel);

        registrarListeners();
    }

    private void registrarListeners() {
        view.getBuscarButton().addActionListener(e -> buscar());
        view.getGuardarButton().addActionListener(e -> guardar());
        view.getBorrarButton().addActionListener(e -> borrar());
        view.getLimpiarButton().addActionListener(e -> limpiar());

        view.getTable1().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarFila();
        });
    }

    private void buscar() {
        String texto = view.getBusqTxtFldDesc().getText();
        List<Categoria> resultado = model.buscarPorDescripcion(texto);
        tableModel.setRows(resultado);
        tableModel.fireTableDataChanged();
    }

    private void guardar() {
        try {
            String id = view.getTxtFldID().getText();
            String descripcion = view.getCatTxtFldDesc().getText();
            Categoria categoria = new Categoria(id.isEmpty() ? null : id, descripcion);
            model.guardar(categoria);
            tableModel.setRows(model.getCategorias());
            tableModel.fireTableDataChanged();
            limpiar();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view.getPanel(), ex.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrar() {
        int fila = view.getTable1().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view.getPanel(), "Seleccione una categoría de la lista.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Categoria seleccionada = tableModel.getRowAt(fila);
        int confirmar = JOptionPane.showConfirmDialog(view.getPanel(),
                "¿Desea borrar la categoría \"" + seleccionada.getDescripcion() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            model.borrar(seleccionada.getId());
            tableModel.setRows(model.getCategorias());
            tableModel.fireTableDataChanged();
            limpiar();
        }
    }

    private void limpiar() {
        view.getTxtFldID().setText("");
        view.getCatTxtFldDesc().setText("");
        view.getBusqTxtFldDesc().setText("");
        view.getTable1().clearSelection();
    }

    private void seleccionarFila() {
        int fila = view.getTable1().getSelectedRow();
        if (fila != -1) {
            Categoria categoria = tableModel.getRowAt(fila);
            view.getTxtFldID().setText(categoria.getId());
            view.getCatTxtFldDesc().setText(categoria.getDescripcion());
        }
    }
}