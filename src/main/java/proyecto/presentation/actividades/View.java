package proyecto.presentation.actividades;

import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel semanaPanel;
    private DatePicker fechaFld;
    private JButton cargarFld;
    private JButton imprimirFld;
    private JTable actividadesFld;

    private void createUIComponents() {
        fechaFld = new DatePicker();
    }

    public View() {
        setFechaReferencia(LocalDate.now());
        actividadesFld.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        actividadesFld.setRowHeight(42);
    }

    public void setController(Controller controller) {
        cargarFld.addActionListener(controller);
        imprimirFld.addActionListener(controller);
    }

    public LocalDate getFechaReferencia() {
        LocalDate fecha = fechaFld.getDate();
        if (fecha == null) throw new IllegalArgumentException("Debe indicar una fecha de referencia");
        return fecha;
    }

    public void setFechaReferencia(LocalDate fecha) { fechaFld.setDate(fecha); }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.FECHA.equals(event.getPropertyName())) setFechaReferencia(model.getFecha());
        if (Model.LIST.equals(event.getPropertyName())) {
            LocalDate lunes = model.getFecha().with(DayOfWeek.MONDAY);
            actividadesFld.setModel(new TableModel(lunes, model.getList()));
            actividadesFld.getColumnModel().getColumn(0).setPreferredWidth(70);
            for (int i = 1; i < actividadesFld.getColumnCount(); i++)
                actividadesFld.getColumnModel().getColumn(i).setPreferredWidth(170);
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Actividades", JOptionPane.INFORMATION_MESSAGE);
    }

    public JPanel getPanel() { return panel; }
    public JButton getCargarFld() { return cargarFld; }
    public JButton getImprimirFld() { return imprimirFld; }
}
