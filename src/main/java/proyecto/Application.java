package proyecto;

import proyecto.logic.Rol;
import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
            catch (Exception ignored) { }
            if (doLogin()) doRun();
        });
    }

    private static boolean doLogin() {
        try {
            Service service = new Service();
            proyecto.presentation.login.Model model = new proyecto.presentation.login.Model();
            proyecto.presentation.login.View view = new proyecto.presentation.login.View();
            proyecto.presentation.login.Controller controller =
                    new proyecto.presentation.login.Controller(model, view, service);
            controller.mostrar();
            return Sesion.isLoggedIn();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void doRun() {
        Usuario usuario = Sesion.getUsuario();
        if (usuario.getRol() == Rol.FUNCIONARIO) {
            mostrarReservas(usuario);
            return;
        }
        try {
            Service service = new Service();
            proyecto.presentation.funcionarios.Model model = new proyecto.presentation.funcionarios.Model();
            proyecto.presentation.funcionarios.View view = new proyecto.presentation.funcionarios.View();
            new proyecto.presentation.funcionarios.Controller(model, view, service);

            JFrame window = new JFrame("SISTEMA DE RESERVAS - admin (ADMIN)");
            window.setContentPane(view.getPanel());
            window.pack();
            window.setSize(Math.max(window.getWidth(), 900), Math.max(window.getHeight(), 620));
            window.setLocationRelativeTo(null);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir Funcionarios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostrarReservas(Usuario usuario) {
        try {
            Service service = new Service();
            proyecto.presentation.reservas.Model model = new proyecto.presentation.reservas.Model();
            proyecto.presentation.reservas.View view = new proyecto.presentation.reservas.View();
            new proyecto.presentation.reservas.Controller(model, view, service);

            proyecto.presentation.calendarizacion.Model modelCalendarizacion =
                    new proyecto.presentation.calendarizacion.Model();
            proyecto.presentation.calendarizacion.View viewCalendarizacion =
                    new proyecto.presentation.calendarizacion.View();
            new proyecto.presentation.calendarizacion.Controller(modelCalendarizacion, viewCalendarizacion, service);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Reservas", view.getPanel());
            tabs.addTab("Calendarización", viewCalendarizacion.getPanel());
            tabs.addTab("Actividades", pendiente("Actividades"));
            tabs.addTab("Estadísticas", pendiente("Estadísticas"));

            JFrame window = new JFrame("SISTEMA DE RESERVAS - " + usuario.getId() + " (FUNCIONARIO)");
            window.setContentPane(tabs);
            window.pack();
            window.setMinimumSize(new Dimension(950, 650));
            window.setLocationRelativeTo(null);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir Reservas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel pendiente(String nombre) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(nombre + " (pendiente de integración)", SwingConstants.CENTER));
        return panel;
    }
}
