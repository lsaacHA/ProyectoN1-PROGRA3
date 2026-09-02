package proyecto.presentation.funcionarios;

import javax.swing.*;
import java.util.ArrayList;

public class View {
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
        int[] cols = {
                TableModel.ID,
                TableModel.NOMBRE,
                TableModel.TELEFONO
        };

        funcionariosFld.setModel(
                new TableModel(cols, new ArrayList<>())
        );
    }

    public JPanel getPanel() {
        return panel;
    }
}
