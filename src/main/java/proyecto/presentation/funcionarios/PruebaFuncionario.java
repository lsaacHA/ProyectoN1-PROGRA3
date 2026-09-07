package proyecto.presentation.funcionarios;

import proyecto.logic.Service;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/** Lanzador de desarrollo para probar únicamente la pantalla de Funcionarios. */
public class PruebaFuncionario {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

                Service service = new Service();
                Model model = new Model();
                View view = new View();
                new Controller(model, view, service);

                JFrame frame = new JFrame("Prueba - Funcionarios");
                frame.setContentPane(view.getPanel());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(Math.max(frame.getWidth(), 900), Math.max(frame.getHeight(), 620));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo abrir Funcionarios: " + exception.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
