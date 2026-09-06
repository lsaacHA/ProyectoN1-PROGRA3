package proyecto;

import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;
import proyecto.presentation.login.Controller;
import proyecto.presentation.login.Model;
import proyecto.presentation.login.View;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (doLogin()) {
                doRun();
            }
        });
    }

    private static boolean doLogin() {
        try {
            Service service = new Service();
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller(model, view, service);
            controller.mostrar();
            return Sesion.isLoggedIn();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudieron cargar los datos: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private static void doRun() {
        Usuario usuario = Sesion.getUsuario();
        JOptionPane.showMessageDialog(
                null,
                "Ingreso correcto: " + usuario.getId() + " (" + usuario.getRol() + ")",
                "Sistema de reservas",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
