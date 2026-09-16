package proyecto.presentation.calendarizacion;

import proyecto.logic.Recurso;

import java.time.LocalTime;
import java.util.List;

public class TableModel extends javax.swing.table.AbstractTableModel {
    private final List<LocalTime> horas;
    private final List<Recurso> recursos;
    private final List<List<String>> matriz;

    public TableModel(List<LocalTime> horas, List<Recurso> recursos, List<List<String>> matriz) {
        this.horas = horas;
        this.recursos = recursos;
        this.matriz = matriz;
    }

    @Override
    public int getRowCount() {
        return horas.size();
    }

    @Override
    public int getColumnCount() {
        return 1 + recursos.size();
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return "Hora";
        return recursos.get(column - 1).getDescripcion();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            LocalTime hora = horas.get(row);
            return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
        }
        return matriz.get(row).get(column - 1);
    }
}