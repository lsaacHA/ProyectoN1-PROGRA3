package proyecto.presentation.funcionarios;

import proyecto.logic.Funcionario;
import proyecto.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Funcionario> implements javax.swing.table.TableModel {
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;

    public TableModel(int[] cols, List<Funcionario> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "ID";
        colNames[NOMBRE] = "Nombre";
        colNames[TELEFONO] = "Teléfono";
    }

    @Override
    protected Object getPropetyAt(Funcionario funcionario, int col) {
        switch (cols[col]) {
            case ID:
                return funcionario.getId();
            case NOMBRE:
                return funcionario.getNombre();
            case TELEFONO:
                return funcionario.getTelefono();
            default:
                return "";
        }
    }
}
