package proyecto.presentation.reservas;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class View {
    private JPanel panel;
    private JPanel nuevaReservaPanel;
    private JPanel botonesPanel;
    private JPanel misReservasPanel;
    private JTextArea fraseFld;
    private JButton extraerFld;
    private JTextField actividadFld;
    private JTextField fechaFld;
    private JButton seleccionarFechaFld;
    private JComboBox<String> horaInicioFld;
    private JComboBox<String> horaFinFld;
    private JList<String> categoriasFld;
    private JButton reservarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarFld;
    private JTable reservasFld;
    private JScrollPane reservasScroll;
    private JButton imprimirFld;

    public JPanel getPanel() {
        return panel;
    }
}
