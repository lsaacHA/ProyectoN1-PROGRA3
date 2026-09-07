package proyecto.presentation.categorias;

import proyecto.logic.Categoria;
import proyecto.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    private List<Categoria> categorias;

    public Model() {
        // TEMPORAL: en memoria, con datos de ejemplo.
        // Cuando tengamos la clase de persistencia XML del equipo, esto cambia.
        categorias = new ArrayList<>();
        categorias.add(new Categoria("CAT-000001", "Sala para 10 personas"));
        categorias.add(new Categoria("CAT-000002", "Laptop windows"));
        categorias.add(new Categoria("CAT-000003", "Sala de Juntas"));
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Categoria> buscarPorDescripcion(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return categorias;
        }
        List<Categoria> resultado = new ArrayList<>();
        for (Categoria c : categorias) {
            if (c.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void guardar(Categoria categoria) {
        if (categoria.getId() == null || categoria.getId().isEmpty()) {
            categoria.setId(generarId());
            categorias.add(categoria);
        } else {
            reemplazar(categoria);
        }
    }

    private void reemplazar(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId().equals(categoria.getId())) {
                categorias.set(i, categoria);
                return;
            }
        }
    }

    public void borrar(String id) {
        categorias.removeIf(c -> c.getId().equals(id));
    }

    private String generarId() {
        int max = 0;
        for (Categoria c : categorias) {
            try {
                int num = Integer.parseInt(c.getId().replace("CAT-", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("CAT-%06d", max + 1);
    }
}