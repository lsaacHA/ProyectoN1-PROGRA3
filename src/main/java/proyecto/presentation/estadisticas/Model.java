package proyecto.presentation.estadisticas;

import proyecto.presentation.AbstractModel;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Model extends AbstractModel {
    public static final String RECURSOS = "recursos";
    public static final String ACTIVIDADES = "actividades";

    private LocalDate recursosDesde;
    private LocalDate recursosHasta;
    private LocalDate actividadesDesde;
    private LocalDate actividadesHasta;
    private Map<String, Integer> recursos;
    private Map<String, Integer> actividades;

    public Model() {
        LocalDate hoy = LocalDate.now();
        recursosDesde = hoy.minusMonths(1);
        recursosHasta = hoy;
        actividadesDesde = hoy.minusMonths(1);
        actividadesHasta = hoy;
        recursos = new LinkedHashMap<>();
        actividades = new LinkedHashMap<>();
    }

    public LocalDate getRecursosDesde() { return recursosDesde; }
    public LocalDate getRecursosHasta() { return recursosHasta; }
    public LocalDate getActividadesDesde() { return actividadesDesde; }
    public LocalDate getActividadesHasta() { return actividadesHasta; }
    public Map<String, Integer> getRecursos() { return recursos; }
    public Map<String, Integer> getActividades() { return actividades; }

    public void setRecursos(LocalDate desde, LocalDate hasta, Map<String, Integer> recursos) {
        recursosDesde = desde;
        recursosHasta = hasta;
        this.recursos = new LinkedHashMap<>(recursos);
        firePropertyChange(RECURSOS);
    }

    public void setActividades(LocalDate desde, LocalDate hasta, Map<String, Integer> actividades) {
        actividadesDesde = desde;
        actividadesHasta = hasta;
        this.actividades = new LinkedHashMap<>(actividades);
        firePropertyChange(ACTIVIDADES);
    }
}
