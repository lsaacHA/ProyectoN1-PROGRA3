package proyecto.presentation.actividades;

import proyecto.logic.Reserva;
import proyecto.presentation.AbstractModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String FECHA = "fecha";
    public static final String LIST = "list";

    private LocalDate fecha;
    private List<Reserva> list;

    public Model() {
        fecha = LocalDate.now();
        list = new ArrayList<>();
    }

    public LocalDate getFecha() { return fecha; }
    public List<Reserva> getList() { return list; }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        firePropertyChange(FECHA);
    }

    public void setList(List<Reserva> list) {
        this.list = new ArrayList<>(list);
        firePropertyChange(LIST);
    }
}
