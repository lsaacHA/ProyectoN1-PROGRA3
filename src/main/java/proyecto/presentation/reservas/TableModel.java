package proyecto.presentation.reservas;

import proyecto.logic.Recurso;
import proyecto.logic.Reserva;
import proyecto.presentation.AbstractTableModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TableModel extends AbstractTableModel<Reserva> {
    public static final int ID = 0;
    public static final int ACTIVIDAD = 1;
    public static final int FECHA = 2;
    public static final int HORARIO = 3;
    public static final int RECURSOS = 4;
    public static final int ESTADO = 5;

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    public TableModel(int[] cols, List<Reserva> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Reserva reserva, int col) {
        return switch (cols[col]) {
            case ID -> reserva.getId();
            case ACTIVIDAD -> reserva.getActividad();
            case FECHA -> reserva.getFecha();
            case HORARIO -> reserva.getHoraInicio().format(HORA) + " - " + reserva.getHoraFin().format(HORA);
            case RECURSOS -> reserva.getRecursos().stream().map(Recurso::getId).collect(Collectors.joining(", "));
            case ESTADO -> reserva.getEstado();
            default -> "";
        };
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"};
    }
}
