package proyecto;

import proyecto.logic.Rol;
import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.net.URL;

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
        mostrarAdministracion(usuario);
    }

    private static void mostrarAdministracion(Usuario usuario) {
        try {
            Service service = new Service();
            proyecto.presentation.funcionarios.Model funcionariosModel = new proyecto.presentation.funcionarios.Model();
            proyecto.presentation.funcionarios.View funcionariosView = new proyecto.presentation.funcionarios.View();
            new proyecto.presentation.funcionarios.Controller(funcionariosModel, funcionariosView, service);

            proyecto.presentation.categorias.Model categoriasModel = new proyecto.presentation.categorias.Model();
            proyecto.presentation.categorias.View categoriasView = new proyecto.presentation.categorias.View();
            new proyecto.presentation.categorias.Controller(categoriasModel, categoriasView, service);

            proyecto.presentation.recursos.Model recursosModel = new proyecto.presentation.recursos.Model();
            proyecto.presentation.recursos.View recursosView = new proyecto.presentation.recursos.View();
            new proyecto.presentation.recursos.Controller(recursosModel, recursosView, service);

            proyecto.presentation.calendarizacion.Model calendarizacionModel = new proyecto.presentation.calendarizacion.Model();
            proyecto.presentation.calendarizacion.View calendarizacionView = new proyecto.presentation.calendarizacion.View();
            new proyecto.presentation.calendarizacion.Controller(calendarizacionModel, calendarizacionView, service);

            proyecto.presentation.actividades.Model actividadesModel = new proyecto.presentation.actividades.Model();
            proyecto.presentation.actividades.View actividadesView = new proyecto.presentation.actividades.View();
            new proyecto.presentation.actividades.Controller(actividadesModel, actividadesView, service);

            proyecto.presentation.estadisticas.Model estadisticasModel = new proyecto.presentation.estadisticas.Model();
            proyecto.presentation.estadisticas.View estadisticasView = new proyecto.presentation.estadisticas.View();
            new proyecto.presentation.estadisticas.Controller(estadisticasModel, estadisticasView, service);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Funcionarios", icono("funcionarios.png"), funcionariosView.getPanel());
            tabs.addTab("Categorías", icono("categorias.png"), categoriasView.getPanel());
            tabs.addTab("Recursos", icono("recursos.png"), recursosView.getPanel());
            tabs.addTab("Calendarización", icono("calendarizacion.png"), calendarizacionView.getPanel());
            tabs.addTab("Actividades", icono("actividades.png"), actividadesView.getPanel());
            tabs.addTab("Estadísticas", icono("statistics.png"), estadisticasView.getPanel());

            JFrame window = new JFrame("SISTEMA DE RESERVAS - " + usuario.getId() + " (ADMINISTRADOR)");
            aplicarIconoVentana(window);
            window.setContentPane(tabs);
            window.pack();
            window.setSize(Math.max(window.getWidth(), 1180), Math.max(window.getHeight(), 700));
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

            proyecto.presentation.actividades.Model modelActividades =
                    new proyecto.presentation.actividades.Model();
            proyecto.presentation.actividades.View viewActividades =
                    new proyecto.presentation.actividades.View();
            new proyecto.presentation.actividades.Controller(modelActividades, viewActividades, service);

            proyecto.presentation.estadisticas.Model modelEstadisticas =
                    new proyecto.presentation.estadisticas.Model();
            proyecto.presentation.estadisticas.View viewEstadisticas =
                    new proyecto.presentation.estadisticas.View();
            new proyecto.presentation.estadisticas.Controller(modelEstadisticas, viewEstadisticas, service);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Reservas", icono("reservas.png"), view.getPanel());
            tabs.addTab("Calendarización", icono("calendarizacion.png"), viewCalendarizacion.getPanel());
            tabs.addTab("Actividades", icono("actividades.png"), viewActividades.getPanel());
            tabs.addTab("Estadísticas", icono("statistics.png"), viewEstadisticas.getPanel());

            JFrame window = new JFrame("SISTEMA DE RESERVAS - " + usuario.getId() + " (FUNCIONARIO)");
            aplicarIconoVentana(window);
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

    private static ImageIcon icono(String archivo) {
        URL recurso = Application.class.getResource("/proyecto/presentation/icons/" + archivo);
        return recurso == null ? null : new ImageIcon(recurso);
    }

    private static void aplicarIconoVentana(JFrame window) {
        ImageIcon icono = icono("icon.png");
        if (icono != null) {
            window.setIconImage(icono.getImage());
        }
    }
}
