package proyecto.presentation.funcionarios;

import proyecto.logic.Funcionario;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel busquedaPanel;
    private JPanel funcionarioPanel;
    private JPanel botonesPanel;
    private JScrollPane listadoScroll;
    private JTextField idBuscarFld;
    private JTextField nombreBuscarFld;
    private JButton buscarFld;
    private JButton imprimirFld;
    private JTextField idFld;
    private JTextField nombreFld;
    private JTextField telefonoFld;
    private JButton guardarFld;
    private JButton borrarFld;
    private JButton limpiarFld;
    private JTable funcionariosFld;

    public View() {
        borrarFld.setEnabled(false);
        funcionariosFld.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarFila();
        });
    }

    public void setController(ActionListener controller) {
        buscarFld.addActionListener(controller);
        imprimirFld.addActionListener(controller);
        guardarFld.addActionListener(controller);
        borrarFld.addActionListener(controller);
        limpiarFld.addActionListener(controller);
        idBuscarFld.addActionListener(controller);
        nombreBuscarFld.addActionListener(controller);
    }

    private void seleccionarFila() {
        Funcionario seleccionado = getSeleccionado();
        if (seleccionado == null) return;
        idFld.setText(seleccionado.getId());
        nombreFld.setText(seleccionado.getNombre());
        telefonoFld.setText(seleccionado.getTelefono());
        idFld.setEditable(false);
        borrarFld.setEnabled(true);
    }

    public Funcionario getSeleccionado() {
        int fila = funcionariosFld.getSelectedRow();
        if (fila < 0 || !(funcionariosFld.getModel() instanceof TableModel modelo)) return null;
        return modelo.getRowAt(funcionariosFld.convertRowIndexToModel(fila));
    }

    public void limpiarEdicion() {
        funcionariosFld.clearSelection();
        idFld.setEditable(true);
        idFld.setText("");
        nombreFld.setText("");
        telefonoFld.setText("");
        borrarFld.setEnabled(false);
        idFld.requestFocusInWindow();
    }

    public void limpiarFiltros() {
        idBuscarFld.setText("");
        nombreBuscarFld.setText("");
    }

    public void imprimir() {
        try { funcionariosFld.print(JTable.PrintMode.FIT_WIDTH); }
        catch (Exception e) { mostrarError("No se pudo imprimir: " + e.getMessage()); }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Funcionarios", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.LIST.equals(event.getPropertyName())) {
            int[] columnas = {TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO};
            funcionariosFld.setModel(new TableModel(columnas, model.getList()));
        }
        if (Model.CURRENT.equals(event.getPropertyName()) && model.getCurrent() == null)
            limpiarEdicion();
    }

    public JPanel getPanel() { return panel; }
    public String getIdBuscar() { return idBuscarFld.getText(); }
    public String getNombreBuscar() { return nombreBuscarFld.getText(); }
    public String getId() { return idFld.getText(); }
    public String getNombre() { return nombreFld.getText(); }
    public String getTelefono() { return telefonoFld.getText(); }
    public JButton getBuscarFld() { return buscarFld; }
    public JButton getImprimirFld() { return imprimirFld; }
    public JButton getGuardarFld() { return guardarFld; }
    public JButton getBorrarFld() { return borrarFld; }
    public JButton getLimpiarFld() { return limpiarFld; }
}
