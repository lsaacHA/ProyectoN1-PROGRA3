package proyecto.presentation.calendarizacion;

import proyecto.logic.Categoria;
import proyecto.logic.Recurso;
import proyecto.presentation.AbstractModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String CATEGORIAS = "categorias";
    public static final String MATRIZ = "matriz";

    private List<Categoria> categorias;
    private List<Recurso> recursos;
    private List<LocalTime> horas;
    private List<List<String>> matriz;

    public Model() {
        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
        horas = new ArrayList<>();
        matriz = new ArrayList<>();
    }

    public List<Categoria> getCategorias() { return categorias; }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }

    public List<Recurso> getRecursos() { return recursos; }
    public List<LocalTime> getHoras() { return horas; }
    public List<List<String>> getMatriz() { return matriz; }

    public void setMatriz(List<Recurso> recursos, List<LocalTime> horas, List<List<String>> matriz) {
        this.recursos = recursos;
        this.horas = horas;
        this.matriz = matriz;
        firePropertyChange(MATRIZ);
    }
}