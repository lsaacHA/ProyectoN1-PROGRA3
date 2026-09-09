package proyecto.presentation.reservas;

import com.github.lgooddatepicker.components.DatePicker;
import proyecto.logic.Categoria;
import proyecto.logic.Reserva;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel nuevaReservaPanel;
    private JPanel botonesPanel;
    private JPanel misReservasPanel;
    private JTextArea fraseFld;
    private JButton extraerFld;
    private JTextField actividadFld;
    private DatePicker fechaFld;
    private JComboBox<String> horaInicioFld;
    private JComboBox<String> horaFinFld;
    private JList<Categoria> categoriasFld;
    private JButton reservarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarFld;
    private JTable reservasFld;
    private JScrollPane reservasScroll;
    private JButton imprimirFld;

    private static final LocalTime[] HORAS_INICIO = {
            LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
            LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0)
    };
    private static final LocalTime[] HORAS_FIN = {
            LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.NOON,
            LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0)
    };

    public View() {
        cancelarReservaFld.setEnabled(false);
        fechaFld.setDate(LocalDate.now().plusDays(1));
        reservasFld.getSelectionModel().addListSelectionListener(event ->
                cancelarReservaFld.setEnabled(reservasFld.getSelectedRow() >= 0));
    }

    public void setController(ActionListener controller) {
        reservarFld.addActionListener(controller);
        cancelarReservaFld.addActionListener(controller);
        limpiarFld.addActionListener(controller);
        imprimirFld.addActionListener(controller);
        extraerFld.addActionListener(controller);
    }

    public String getActividad() { return actividadFld.getText(); }
    public String getFrase() { return fraseFld.getText(); }
    public LocalDate getFecha() { return fechaFld.getDate(); }
    public LocalTime getHoraInicio() { return HORAS_INICIO[horaInicioFld.getSelectedIndex()]; }
    public LocalTime getHoraFin() { return HORAS_FIN[horaFinFld.getSelectedIndex()]; }
    public List<Categoria> getCategoriasSeleccionadas() { return categoriasFld.getSelectedValuesList(); }

    public void cargarExtraccion(String actividad, LocalDate fecha, LocalTime inicio,
                                 LocalTime fin, List<Categoria> categorias) {
        int indiceInicio = indiceHora(HORAS_INICIO, inicio);
        int indiceFin = indiceHora(HORAS_FIN, fin);
        if (indiceInicio < 0 || indiceFin < 0)
            throw new IllegalArgumentException("El horario extraído no está disponible en los selectores");

        actividadFld.setText(actividad);
        fechaFld.setDate(fecha);
        horaInicioFld.setSelectedIndex(indiceInicio);
        horaFinFld.setSelectedIndex(indiceFin);
        int[] indices = categorias.stream().mapToInt(categoria -> {
            for (int i = 0; i < categoriasFld.getModel().getSize(); i++)
                if (categoriasFld.getModel().getElementAt(i).getId().equals(categoria.getId())) return i;
            return -1;
        }).filter(i -> i >= 0).toArray();
        categoriasFld.setSelectedIndices(indices);
    }

    private int indiceHora(LocalTime[] horas, LocalTime buscada) {
        for (int i = 0; i < horas.length; i++) if (horas[i].equals(buscada)) return i;
        return -1;
    }

    public Reserva getReservaSeleccionada() {
        int fila = reservasFld.getSelectedRow();
        if (fila < 0) return null;
        int modelo = reservasFld.convertRowIndexToModel(fila);
        return ((TableModel) reservasFld.getModel()).getRowAt(modelo);
    }

    public void limpiar() {
        fraseFld.setText("");
        actividadFld.setText("");
        fechaFld.setDate(LocalDate.now().plusDays(1));
        horaInicioFld.setSelectedIndex(0);
        horaFinFld.setSelectedIndex(0);
        categoriasFld.clearSelection();
        reservasFld.clearSelection();
        actividadFld.requestFocusInWindow();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Sistema de reservas", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (Model.LIST.equals(event.getPropertyName())) {
            Model model = (Model) event.getSource();
            int[] columnas = {TableModel.ID, TableModel.ACTIVIDAD, TableModel.FECHA,
                    TableModel.HORARIO, TableModel.RECURSOS, TableModel.ESTADO};
            reservasFld.setModel(new TableModel(columnas, model.getList()));
        }
        if (Model.CATEGORIES.equals(event.getPropertyName())) {
            Model model = (Model) event.getSource();
            DefaultListModel<Categoria> lista = new DefaultListModel<>();
            model.getCategories().forEach(lista::addElement);
            categoriasFld.setModel(lista);
        }
    }

    public JPanel getPanel() { return panel; }
    public JButton getExtraerFld() { return extraerFld; }
    public JButton getReservarFld() { return reservarFld; }
    public JButton getCancelarReservaFld() { return cancelarReservaFld; }
    public JButton getLimpiarFld() { return limpiarFld; }
    public JButton getImprimirFld() { return imprimirFld; }
}
