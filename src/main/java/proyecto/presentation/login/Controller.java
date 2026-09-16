package proyecto.presentation.login;

import proyecto.logic.Service;
import proyecto.logic.Sesion;
import proyecto.logic.Usuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private final Model model;
    private final View view;
    private final Service service;

    public Controller(Model model, View view, Service service) {
        this.model = model;
        this.view = view;
        this.service = service;
        view.setController(this);
    }

    public void mostrar() {
        view.setVisible(true);
    }

    public void login() {
        try {
            Usuario usuario = service.login(view.getId(), view.getClave());
            model.setCurrent(usuario);
            Sesion.iniciar(usuario);
            view.dispose();
        } catch (Exception exception) {
            Sesion.cerrar();
            view.mostrarError(exception.getMessage());
            view.limpiarClave();
        }
    }

    public void cambiarClave() {
        String[] datos = view.solicitarCambioClave();
        if (datos == null) {
            return;
        }

        String id = datos[0].trim();
        String claveActual = datos[1];
        String claveNueva = datos[2];
        String confirmacion = datos[3];

        try {
            if (claveNueva.isBlank()) {
                throw new Exception("La clave nueva es obligatoria");
            }
            if (!claveNueva.equals(confirmacion)) {
                throw new Exception("La confirmación no coincide con la clave nueva");
            }

            Usuario usuario = service.login(id, claveActual);
            service.changePassword(usuario, claveActual, claveNueva);
            view.setId(usuario.getId());
            view.limpiarClave();
            view.mostrarMensaje("La clave se cambió correctamente");
        } catch (Exception exception) {
            view.mostrarError(exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == view.getIngresarFld()) {
            login();
        } else if (event.getSource() == view.getCancelarFld()) {
            Sesion.cerrar();
            view.dispose();
        } else if (event.getSource() == view.getCambiarClaveFld()) {
            cambiarClave();
        }
    }
}
