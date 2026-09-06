package proyecto.logic;

/** Mantiene al usuario autenticado durante la ejecución de la aplicación. */
public final class Sesion {
    private static Usuario usuario;

    private Sesion() {
    }

    public static void iniciar(Usuario usuarioAutenticado) {
        usuario = usuarioAutenticado;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static boolean isLoggedIn() {
        return usuario != null;
    }

    public static void cerrar() {
        usuario = null;
    }
}
