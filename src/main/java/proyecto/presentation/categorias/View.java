package proyecto.presentation.categorias;

import proyecto.logic.Categoria;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JTextField BusqTxtFldDesc;
    private JButton buscarButton;
    private JButton imprimirButton;
    private JTextField TxtFldID;
    private JTextField CatTxtFldDesc;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTable table1;
    private JPanel panel;
    private JButton funcionariosButton;
    private JButton categoriasButton;
    private JButton recursosButton;
    private JButton calendarizacionButton;
    private JButton estadisticasButton;
    private JButton actividadesButton1;

    public View() {
        TxtFldID.setEditable(false); // el ID de la categoría siempre es autogenerado
        borrarButton.setEnabled(false);
        table1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarFila();
        });
    }

    public void setController(ActionListener controller) {
        buscarButton.addActionListener(controller);
        imprimirButton.addActionListener(controller);
        guardarButton.addActionListener(controller);
        borrarButton.addActionListener(controller);
        limpiarButton.addActionListener(controller);
        BusqTxtFldDesc.addActionListener(controller);
    }

    private void seleccionarFila() {
        Categoria seleccionada = getSeleccionado();
        if (seleccionada == null) return;
        TxtFldID.setText(seleccionada.getId());
        CatTxtFldDesc.setText(seleccionada.getDescripcion());
        borrarButton.setEnabled(true);
    }

    public Categoria getSeleccionado() {
        int fila = table1.getSelectedRow();
        if (fila < 0 || !(table1.getModel() instanceof TableModel modelo)) return null;
        return modelo.getRowAt(table1.convertRowIndexToModel(fila));
    }

    public void limpiarEdicion() {
        table1.clearSelection();
        TxtFldID.setText("");
        CatTxtFldDesc.setText("");
        borrarButton.setEnabled(false);
        CatTxtFldDesc.requestFocusInWindow();
    }

    public void limpiarFiltros() {
        BusqTxtFldDesc.setText("");
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Categorías", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.LIST.equals(event.getPropertyName())) {
            int[] columnas = {TableModel.ID, TableModel.DESCRIPCION};
            table1.setModel(new TableModel(columnas, model.getList()));
        }
        if (Model.CURRENT.equals(event.getPropertyName()) && model.getCurrent() == null)
            limpiarEdicion();
    }

    public JPanel getPanel() { return panel; }
    public String getBuscarDescripcion() { return BusqTxtFldDesc.getText(); }
    public String getDescripcion() { return CatTxtFldDesc.getText(); }
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getImprimirButton() { return imprimirButton; }
    public JButton getGuardarButton() { return guardarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
}