package proyecto.presentation.actividades;

import proyecto.logic.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Matriz semanal: filas por hora y columnas por día. */
public class TableModel extends AbstractTableModel {
    private static final LocalTime PRIMERA_HORA = LocalTime.of(6, 0);
    private static final int CANTIDAD_HORAS = 16; // 06:00 a 21:00
    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd");

    private LocalDate lunes;
    private List<Reserva> reservas;

    public TableModel(LocalDate lunes, List<Reserva> reservas) {
        this.lunes = lunes;
        this.reservas = new ArrayList<>(reservas);
    }

    @Override public int getRowCount() { return CANTIDAD_HORAS; }
    @Override public int getColumnCount() { return 8; }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "Hora" : DIA.format(lunes.plusDays(column - 1));
    }

    @Override
    public Object getValueAt(int row, int column) {
        LocalTime hora = PRIMERA_HORA.plusHours(row);
        if (column == 0) return hora.toString();
        LocalDate fecha = lunes.plusDays(column - 1);
        return reservas.stream()
                .filter(r -> fecha.equals(r.getFecha()))
                .filter(r -> ocupaHora(r, hora))
                .map(this::descripcion)
                .reduce((a, b) -> a + " | " + b)
                .orElse("");
    }

    private boolean ocupaHora(Reserva reserva, LocalTime hora) {
        return reserva.getHoraInicio() != null && reserva.getHoraFin() != null
                && !hora.isBefore(reserva.getHoraInicio())
                && hora.isBefore(reserva.getHoraFin());
    }

    private String descripcion(Reserva reserva) {
        String funcionario = reserva.getFuncionario() == null ? ""
                : reserva.getFuncionario().getNombre();
        return reserva.getActividad() + (funcionario == null || funcionario.isBlank()
                ? "" : " (" + funcionario + ")");
    }
}
