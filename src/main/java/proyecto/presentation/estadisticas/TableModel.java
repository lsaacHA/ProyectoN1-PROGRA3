package proyecto.presentation.estadisticas;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Map;

public class TableModel extends AbstractTableModel {
    private final String primeraColumna;
    private final ArrayList<Map.Entry<String, Integer>> filas;

    public TableModel(String primeraColumna, Map<String, Integer> datos) {
        this.primeraColumna = primeraColumna;
        filas = new ArrayList<>(datos.entrySet());
    }

    @Override public int getRowCount() { return filas.size(); }
    @Override public int getColumnCount() { return 2; }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? primeraColumna : "Cantidad";
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return column == 0 ? String.class : Integer.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Map.Entry<String, Integer> dato = filas.get(row);
        return column == 0 ? dato.getKey() : dato.getValue();
    }
}
