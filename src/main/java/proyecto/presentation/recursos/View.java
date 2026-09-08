package proyecto.presentation.recursos;

import proyecto.logic.Categoria;
import proyecto.presentation.AbstractModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public class View extends JPanel {

    private JComboBox<Categoria> comboFiltroCategoria;
    private JTextField txtFiltroDescripcion;
    private JButton btnBuscar;
    private JButton btnImprimir;

    public View() {
        setLayout(new BorderLayout());
        add(crearPanelFiltro(), BorderLayout.NORTH);
    }

    private JPanel crearPanelFiltro() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Filtro"));

        comboFiltroCategoria = new JComboBox<>();
        txtFiltroDescripcion = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnImprimir = new JButton("Imprimir");

        panel.add(new JLabel("Categoria"));
        panel.add(comboFiltroCategoria);
        panel.add(new JLabel("Descripcion"));
        panel.add(txtFiltroDescripcion);
        panel.add(btnBuscar);
        panel.add(btnImprimir);

        return panel;
    }
}