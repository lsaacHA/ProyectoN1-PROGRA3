package proyecto.presentation.recursos;

import proyecto.logic.Service;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PruebaRecurso {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Service service = new Service();
                Model model = new Model();
                View view = new View();
                new Controller(model, view, service);

                JFrame frame = new JFrame("SISTEMA DE RESERVAS - admin (ADMIN)");
                frame.setContentPane(view.getPanel());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(Math.max(frame.getWidth(), 900), Math.max(frame.getHeight(), 600));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No se pudo abrir Recursos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
