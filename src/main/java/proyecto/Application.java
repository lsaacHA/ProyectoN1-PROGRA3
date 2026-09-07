package proyecto;

import proyecto.logic.Rol;
import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;
import proyecto.presentation.login.Controller;
import proyecto.presentation.login.Model;
import proyecto.presentation.login.View;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;

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
            JOptionPane.showMessageDialog(null,
                    "No se pudieron cargar los datos: " + exception.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void doRun() {
        Usuario usuario = Sesion.getUsuario();
        if (usuario.getRol() == Rol.FUNCIONARIO) {
            mostrarReservas(usuario);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Ingreso correcto: " + usuario.getId() + " (ADMINISTRADOR)\n"
                            + "La ventana administrativa se integrará desde sus módulos.",
                    "Sistema de reservas", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void mostrarReservas(Usuario usuario) {
        try {
            Service service = new Service();
            proyecto.presentation.reservas.Model model = new proyecto.presentation.reservas.Model();
            proyecto.presentation.reservas.View view = new proyecto.presentation.reservas.View();
            new proyecto.presentation.reservas.Controller(model, view, service);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Reservas", view.getPanel());
            tabs.addTab("Calendarización", pendiente("Calendarización"));
            tabs.addTab("Actividades", pendiente("Actividades"));
            tabs.addTab("Estadísticas", pendiente("Estadísticas"));

            JFrame frame = new JFrame("SISTEMA DE RESERVAS - " + usuario.getId() + " (FUNCIONARIO)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(tabs);
            frame.pack();
            frame.setMinimumSize(new Dimension(950, 650));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo abrir Reservas: " + exception.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel pendiente(String nombre) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(nombre + " (pendiente de integración)", SwingConstants.CENTER));
        return panel;
    }
}
