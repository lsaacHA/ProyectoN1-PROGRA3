package proyecto;

import proyecto.presentation.funcionarios.View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
            catch (Exception ignored) {
            }

            View view = new View();

            JFrame window = new JFrame("Funcionarios");
            window.setContentPane(view.getPanel());
            window.setSize(800, 600);
            window.setLocationRelativeTo(null);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.setVisible(true);
        });
    }
}