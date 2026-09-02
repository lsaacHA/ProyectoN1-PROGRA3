package proyecto.presentation.login;

import javax.swing.*;

public class View extends JDialog {
    private JPanel panel;
    private JPanel loginPanel;
    private JPanel botonesPanel;
    private JTextField idFld;
    private JPasswordField claveFld;
    private JButton ingresarFld;
    private JButton cancelarFld;
    private JButton cambiarClaveFld;

    public View() {
        setContentPane(panel);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public JPanel getPanel() {
        return panel;
    }
}
