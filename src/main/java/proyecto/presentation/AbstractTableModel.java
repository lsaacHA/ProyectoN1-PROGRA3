package proyecto.presentation;

import java.util.List;

public abstract class AbstractTableModel<E>
        extends javax.swing.table.AbstractTableModel
        implements javax.swing.table.TableModel {

    protected List<E> rows;
    protected int[] cols;
    protected String[] colNames;

    public AbstractTableModel(int[] cols, List<E> rows) {
        this.cols = cols;
        this.rows = rows;
        initColNames();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return colNames[cols[col]];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return super.getColumnClass(col);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        E element = rows.get(row);
        return getPropetyAt(element, col);
    }

    public E getRowAt(int row) {
        return rows.get(row);
    }

    protected abstract Object getPropetyAt(E element, int col);

    protected abstract void initColNames();
}
