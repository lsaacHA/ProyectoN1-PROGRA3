package proyecto.presentation.funcionarios;

import javax.swing.*;

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

    public JPanel getPanel() {
        return panel;
    }
}
