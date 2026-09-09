package proyecto.presentation.recursos;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;
import proyecto.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String CATEGORIES = "categories";

    private Recurso current;
    private List<Recurso> list;
    private List<Categoria> categories;

    public Model() {
        current = null;
        list = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(CATEGORIES);
    }

    public Recurso getCurrent() { return current; }
    public void setCurrent(Recurso current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Recurso> getList() { return list; }
    public void setList(List<Recurso> list) {
        this.list = list == null ? new ArrayList<>() : list;
        firePropertyChange(LIST);
    }

    public List<Categoria> getCategories() { return categories; }
    public void setCategories(List<Categoria> categories) {
        this.categories = categories == null ? new ArrayList<>() : categories;
        firePropertyChange(CATEGORIES);
    }
}
