package proyecto.presentation.actividades;

import proyecto.logic.Service;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PruebaActividades {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Model model = new Model();
                View view = new View();
                new Controller(model, view, new Service());
                JFrame ventana = new JFrame("Programación de actividades");
                ventana.setContentPane(view.getPanel());
                ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                ventana.pack();
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
            } catch (Exception exception) {
                javax.swing.JOptionPane.showMessageDialog(null, exception.getMessage(), "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
