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

    private JTextField txtId;
    private JComboBox<Categoria> comboCategoria;
    private JTextField txtDescripcion;
    private JButton btnGuardar;
    private JButton btnBorrar;
    private JButton btnLimpiar;

    private JTable tablaRecursos;
    private TableModel tableModel;

    public View() {
        setLayout(new BorderLayout());
        add(crearPanelFiltro(), BorderLayout.NORTH);
        add(crearPanelRecurso(), BorderLayout.CENTER);
        add(crearPanelListado(), BorderLayout.SOUTH);
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

    private JPanel crearPanelRecurso() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recurso"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(20);
        comboCategoria = new JComboBox<>();
        txtDescripcion = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Categoria"), gbc);
        gbc.gridx = 1;
        panel.add(comboCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descripcion"), gbc);
        gbc.gridx = 1;
        panel.add(txtDescripcion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnGuardar = new JButton("Guardar");
        btnBorrar = new JButton("Borrar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JScrollPane crearPanelListado() {
        tableModel = new TableModel(
                new int[]{TableModel.ID, TableModel.CATEGORIA, TableModel.DESCRIPCION},
                new java.util.ArrayList<>()
        );
        tablaRecursos = new JTable(tableModel);

        JScrollPane scroll = new JScrollPane(tablaRecursos);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado"));
        scroll.setPreferredSize(new Dimension(400, 150));

        return scroll;
    }
}