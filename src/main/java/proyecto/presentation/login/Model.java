package proyecto.presentation.login;

import proyecto.logic.Usuario;
import proyecto.presentation.AbstractModel;

public class Model extends AbstractModel {
    private Usuario current;

    public static final String CURRENT = "current";

    public Model() {
        current = new Usuario();
    }

    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
}
