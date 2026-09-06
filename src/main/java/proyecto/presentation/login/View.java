package proyecto.presentation.login;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridLayout;

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
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(Controller controller) {
        ingresarFld.addActionListener(controller);
        cancelarFld.addActionListener(controller);
        cambiarClaveFld.addActionListener(controller);
        getRootPane().setDefaultButton(ingresarFld);
    }

    public String getId() {
        return idFld.getText().trim();
    }

    public void setId(String id) {
        idFld.setText(id);
    }

    public String getClave() {
        return new String(claveFld.getPassword());
    }

    public void limpiarClave() {
        claveFld.setText("");
        claveFld.requestFocusInWindow();
    }

    public String[] solicitarCambioClave() {
        JTextField id = new JTextField(getId(), 15);
        JPasswordField actual = new JPasswordField(15);
        JPasswordField nueva = new JPasswordField(15);
        JPasswordField confirmacion = new JPasswordField(15);

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("ID:"));
        formulario.add(id);
        formulario.add(new JLabel("Clave actual:"));
        formulario.add(actual);
        formulario.add(new JLabel("Clave nueva:"));
        formulario.add(nueva);
        formulario.add(new JLabel("Confirmar clave:"));
        formulario.add(confirmacion);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                formulario,
                "Cambiar clave",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        return new String[]{
                id.getText(),
                new String(actual.getPassword()),
                new String(nueva.getPassword()),
                new String(confirmacion.getPassword())
        };
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema de reservas", JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getIngresarFld() {
        return ingresarFld;
    }

    public JButton getCancelarFld() {
        return cancelarFld;
    }

    public JButton getCambiarClaveFld() {
        return cambiarClaveFld;
    }

    public JPanel getPanel() {
        return panel;
    }
}
