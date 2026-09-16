package proyecto.presentation.categorias;

import proyecto.logic.Categoria;
import proyecto.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Categoria current;
    private List<Categoria> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public Model() {
        current = new Categoria();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Categoria getCurrent() {
        return current;
    }

    public void setCurrent(Categoria current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Categoria> getList() {
        return list;
    }

    public void setList(List<Categoria> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}