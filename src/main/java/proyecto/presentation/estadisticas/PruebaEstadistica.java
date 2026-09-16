package proyecto.presentation.estadisticas;

import proyecto.logic.Service;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class PruebaEstadistica {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Model model = new Model();
                View view = new View();
                new Controller(model, view, new Service());

                JFrame window = new JFrame("Estadísticas");
                window.setContentPane(view.getPanel());
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.pack();
                window.setSize(1180, 700);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
