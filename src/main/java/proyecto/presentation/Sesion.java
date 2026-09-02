package proyecto.presentation;

import proyecto.logic.Usuario;

public class Sesion {
    private static Usuario usuario;

    private Sesion() {
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Sesion.usuario = usuario;
    }

    public static void logout() {
        usuario = null;
    }

    public static boolean isLoggedIn() {
        return usuario != null;
    }
}
