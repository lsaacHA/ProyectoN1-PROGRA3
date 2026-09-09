package proyecto.presentation.recursos;

import proyecto.logic.Recurso;
import proyecto.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Recurso> {
    public static final int ID = 0;
    public static final int CATEGORIA = 1;
    public static final int DESCRIPCION = 2;

    public TableModel(int[] cols, List<Recurso> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"ID", "Categoría", "Descripción"};
    }

    @Override
    protected Object getPropetyAt(Recurso recurso, int col) {
        return switch (cols[col]) {
            case ID -> recurso.getId();
            case CATEGORIA -> recurso.getCategoria() == null ? "" : recurso.getCategoria().getDescripcion();
            case DESCRIPCION -> recurso.getDescripcion();
            default -> "";
        };
    }
}
