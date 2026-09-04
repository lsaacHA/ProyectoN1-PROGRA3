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
        colNames = new String[]{"Id", "Categoria", "Descripcion"};
    }

    @Override
    protected Object getPropetyAt(Recurso element, int col) {
        switch (cols[col]) {
            case ID:
                return element.getId();
            case CATEGORIA:
                return element.getCategoria();
            case DESCRIPCION:
                return element.getDescripcion();
            default:
                return null;
        }
    }
}
