package proyecto.presentation.calendarizacion;

import com.github.lgooddatepicker.components.CalendarPanel;
import proyecto.logic.Categoria;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JPanel PanelMain;
    private JTextField textField1;
    private JButton button1;
    private JComboBox comboBox1;
    private JButton cargarButton;
    private JButton imprimirButton;
    private JTable table1;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

    private LocalDate fechaSeleccionada;

    public View() {
        textField1.setEditable(false);
        button1.addActionListener(e -> elegirFecha());
    }

    public void setController(ActionListener controller) {
        cargarButton.addActionListener(controller);
        imprimirButton.addActionListener(controller);
    }

    private void elegirFecha() {
        CalendarPanel calendarPanel = new CalendarPanel();
        if (fechaSeleccionada != null) calendarPanel.setSelectedDate(fechaSeleccionada);

        int resultado = JOptionPane.showConfirmDialog(panel1, calendarPanel, "Seleccionar fecha",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION && calendarPanel.getSelectedDate() != null) {
            fechaSeleccionada = calendarPanel.getSelectedDate();
            textField1.setText(fechaSeleccionada.format(FORMATO_FECHA));
        }
    }

    public LocalDate getFecha() {
        return fechaSeleccionada;
    }

    public Categoria getCategoriaSeleccionada() {
        return (Categoria) comboBox1.getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private void actualizarCategorias(List<Categoria> categorias) {
        Categoria seleccionActual = getCategoriaSeleccionada();
        comboBox1.removeAllItems();
        for (Categoria categoria : categorias) comboBox1.addItem(categoria);
        if (seleccionActual != null) comboBox1.setSelectedItem(seleccionActual);
    }

    private void actualizarTabla(List<java.time.LocalTime> horas,
                                 List<proyecto.logic.Recurso> recursos,
                                 List<List<String>> matriz) {
        table1.setModel(new TableModel(horas, recursos, matriz));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.CATEGORIAS.equals(event.getPropertyName())) {
            actualizarCategorias(model.getCategorias());
        }
        if (Model.MATRIZ.equals(event.getPropertyName())) {
            actualizarTabla(model.getHoras(), model.getRecursos(), model.getMatriz());
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel1, mensaje, "Calendarización", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel1, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getPanel() { return panel1; }
    public JButton getCargarButton() { return cargarButton; }
    public JButton getImprimirButton() { return imprimirButton; }
}