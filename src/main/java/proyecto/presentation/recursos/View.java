package proyecto.presentation.recursos;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel filtroPanel;
    private JPanel recursoPanel;
    private JPanel listadoPanel;
    private JComboBox<Categoria> categoriaFiltroFld;
    private JTextField descripcionFiltroFld;
    private JButton buscarFld;
    private JButton imprimirFld;
    private JTextField idFld;
    private JComboBox<Categoria> categoriaFld;
    private JTextField descripcionFld;
    private JButton guardarFld;
    private JButton borrarFld;
    private JButton limpiarFld;
    private JTable recursosFld;

    public View() {
        borrarFld.setEnabled(false);
        recursosFld.setAutoCreateRowSorter(true);
        recursosFld.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recursosFld.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarFila();
        });
    }

    public void setController(ActionListener controller) {
        buscarFld.addActionListener(controller);
        imprimirFld.addActionListener(controller);
        guardarFld.addActionListener(controller);
        borrarFld.addActionListener(controller);
        limpiarFld.addActionListener(controller);
        descripcionFiltroFld.addActionListener(controller);
    }

    private void seleccionarFila() {
        Recurso seleccionado = getSeleccionado();
        if (seleccionado == null) return;
        idFld.setText(seleccionado.getId());
        idFld.setEditable(false);
        categoriaFld.setSelectedItem(seleccionado.getCategoria());
        descripcionFld.setText(seleccionado.getDescripcion());
        borrarFld.setEnabled(true);
    }

    public Recurso getSeleccionado() {
        int fila = recursosFld.getSelectedRow();
        if (fila < 0 || !(recursosFld.getModel() instanceof TableModel tableModel)) return null;
        return tableModel.getRowAt(recursosFld.convertRowIndexToModel(fila));
    }

    public void limpiarEdicion() {
        recursosFld.clearSelection();
        idFld.setText("");
        idFld.setEditable(true);
        if (categoriaFld.getItemCount() > 0) categoriaFld.setSelectedIndex(0);
        descripcionFld.setText("");
        borrarFld.setEnabled(false);
        idFld.requestFocusInWindow();
    }

    public void limpiarFiltros() {
        categoriaFiltroFld.setSelectedItem(null);
        descripcionFiltroFld.setText("");
    }

    private void actualizarCategorias(Model model) {
        Categoria filtroAnterior = (Categoria) categoriaFiltroFld.getSelectedItem();
        Categoria categoriaAnterior = (Categoria) categoriaFld.getSelectedItem();
        categoriaFiltroFld.removeAllItems();
        categoriaFiltroFld.addItem(null);
        categoriaFld.removeAllItems();
        for (Categoria categoria : model.getCategories()) {
            categoriaFiltroFld.addItem(categoria);
            categoriaFld.addItem(categoria);
        }
        if (filtroAnterior != null) categoriaFiltroFld.setSelectedItem(filtroAnterior);
        if (categoriaAnterior != null) categoriaFld.setSelectedItem(categoriaAnterior);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.CATEGORIES.equals(event.getPropertyName())) actualizarCategorias(model);
        if (Model.LIST.equals(event.getPropertyName())) {
            int[] columnas = {TableModel.ID, TableModel.CATEGORIA, TableModel.DESCRIPCION};
            recursosFld.setModel(new TableModel(columnas, model.getList()));
        }
        if (Model.CURRENT.equals(event.getPropertyName()) && model.getCurrent() == null)
            limpiarEdicion();
    }

    public JPanel getPanel() { return panel; }
    public Categoria getCategoriaFiltro() { return (Categoria) categoriaFiltroFld.getSelectedItem(); }
    public String getDescripcionFiltro() { return descripcionFiltroFld.getText(); }
    public String getId() { return idFld.getText(); }
    public Categoria getCategoria() { return (Categoria) categoriaFld.getSelectedItem(); }
    public String getDescripcion() { return descripcionFld.getText(); }
    public JButton getBuscarFld() { return buscarFld; }
    public JButton getImprimirFld() { return imprimirFld; }
    public JButton getGuardarFld() { return guardarFld; }
    public JButton getBorrarFld() { return borrarFld; }
    public JButton getLimpiarFld() { return limpiarFld; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Recursos", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
