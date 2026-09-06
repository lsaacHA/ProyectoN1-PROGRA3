package proyecto.presentation.categorias;

import proyecto.logic.Categoria;
import proyecto.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Categoria> {

    public static final int ID = 0;
    public static final int DESCRIPCION = 1;

    public TableModel(int[] cols, List<Categoria> rows) {
        super(cols, rows);
    }

    public void setRows(List<Categoria> rows) {
        this.rows = rows;
    }

    @Override
    protected Object getPropetyAt(Categoria element, int col) {
        switch (cols[col]) {
            case ID: return element.getId();
            case DESCRIPCION: return element.getDescripcion();
            default: return null;
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"Id", "Descripcion"};
    }
}