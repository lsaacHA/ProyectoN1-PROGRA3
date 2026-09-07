package proyecto.presentation.reservas;

import proyecto.logic.Categoria;
import proyecto.logic.Reserva;
import proyecto.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String CATEGORIES = "categories";

    private Reserva current;
    private List<Reserva> list;
    private List<Categoria> categories;

    public Model() {
        current = new Reserva();
        list = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public Reserva getCurrent() { return current; }
    public List<Reserva> getList() { return list; }
    public List<Categoria> getCategories() { return categories; }

    public void setCurrent(Reserva current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setList(List<Reserva> list) {
        this.list = new ArrayList<>(list);
        firePropertyChange(LIST);
    }

    public void setCategories(List<Categoria> categories) {
        this.categories = new ArrayList<>(categories);
        firePropertyChange(CATEGORIES);
    }
}
