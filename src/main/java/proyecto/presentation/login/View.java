package proyecto.presentation.login;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Frame;

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
        super((Frame) null, "Ingreso al sistema", true);
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cambiarClaveFld.setEnabled(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(Controller controller) {
        ingresarFld.addActionListener(controller);
        cancelarFld.addActionListener(controller);
        getRootPane().setDefaultButton(ingresarFld);
    }

    public String getId() {
        return idFld.getText().trim();
    }

    public String getClave() {
        return new String(claveFld.getPassword());
    }

    public void limpiarClave() {
        claveFld.setText("");
        claveFld.requestFocusInWindow();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Ingreso incorrecto", JOptionPane.ERROR_MESSAGE);
    }

    public JButton getIngresarFld() {
        return ingresarFld;
    }

    public JButton getCancelarFld() {
        return cancelarFld;
    }

    public JPanel getPanel() {
        return panel;
    }
}
