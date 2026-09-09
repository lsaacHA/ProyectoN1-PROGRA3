package proyecto.presentation.recursos;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;
import proyecto.logic.Service;
import proyecto.presentation.AbstractModel;

import java.util.List;

public class Model extends AbstractModel {

    public static final String RECURSOS="recursos";
    public static final String SELECCIONADO ="seleccionado";

    private List<Recurso> recursos;
    private Recurso seleccionado;

    public Model() {
        recursos=Service.listarRecursos();
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public Recurso getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Recurso seleccionado) {
        this.seleccionado = seleccionado;
        firePropertyChange(SELECCIONADO);
    }

    public void buscar(Categoria categoria, String descripcion) {
        recursos = Service.buscarRecursos(categoria, descripcion);
        firePropertyChange(RECURSOS);
    }

    public void guardar(Recurso recurso) {
        Service.guardarRecurso(recurso);
        recursos = Service.listarRecursos();
        firePropertyChange(RECURSOS);
    }

    public void borrar(String id) {
        Service.borrarRecurso(id);
        recursos = Service.listarRecursos();
        setSeleccionado(null);
        firePropertyChange(RECURSOS);
    }

    public void limpiar() {
        setSeleccionado(null);
    }
}