package proyecto.logic;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private static List<Recurso> recursos = new ArrayList<>();

    public static void guardarRecurso(Recurso recurso) {
        borrarRecurso(recurso.getId());
        recursos.add(recurso);
    }

    public static void borrarRecurso(String id) {
        recursos.removeIf(r -> r.getId().equals(id));
    }

    public static List<Recurso> buscarRecursos(Categoria categoria, String descripcion) {
        List<Recurso> resultado = new ArrayList<>();
        for (Recurso r:recursos) {
            boolean coincideCategoria = (categoria==null)||categoria.equals(r.getCategoria());
            boolean coincideDescripcion = (descripcion == null)||descripcion.isEmpty()|| r.getDescripcion().toLowerCase().contains(descripcion.toLowerCase());
            if (coincideCategoria && coincideDescripcion) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public static List<Recurso> listarRecursos() {
        return new ArrayList<>(recursos);
    }
}