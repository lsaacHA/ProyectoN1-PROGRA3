package proyecto.presentation.estadisticas;

import com.github.lgooddatepicker.components.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Map;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel recursosPanel;
    private JPanel filtroRecursosPanel;
    private JPanel botonesRecursosPanel;
    private DatePicker recursosDesdeFld;
    private DatePicker recursosHastaFld;
    private JButton cargarRecursosFld;
    private JButton imprimirRecursosFld;
    private JTable recursosFld;
    private JPanel graficoRecursosFld;
    private JPanel actividadesPanel;
    private JPanel filtroActividadesPanel;
    private JPanel botonesActividadesPanel;
    private DatePicker actividadesDesdeFld;
    private DatePicker actividadesHastaFld;
    private JButton cargarActividadesFld;
    private JButton imprimirActividadesFld;
    private JTable actividadesFld;
    private JPanel graficoActividadesFld;

    private void createUIComponents() {
        recursosDesdeFld = new DatePicker();
        recursosHastaFld = new DatePicker();
        actividadesDesdeFld = new DatePicker();
        actividadesHastaFld = new DatePicker();
    }

    public View() {
        LocalDate hoy = LocalDate.now();
        recursosDesdeFld.setDate(hoy.minusMonths(1));
        recursosHastaFld.setDate(hoy);
        actividadesDesdeFld.setDate(hoy.minusMonths(1));
        actividadesHastaFld.setDate(hoy);
        recursosFld.setFillsViewportHeight(true);
        actividadesFld.setFillsViewportHeight(true);
    }

    public void setController(ActionListener controller) {
        cargarRecursosFld.addActionListener(controller);
        imprimirRecursosFld.addActionListener(controller);
        cargarActividadesFld.addActionListener(controller);
        imprimirActividadesFld.addActionListener(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        Model model = (Model) event.getSource();
        if (Model.RECURSOS.equals(event.getPropertyName())) {
            recursosFld.setModel(new TableModel("Categoría", model.getRecursos()));
            mostrarGrafico(graficoRecursosFld, "Recursos usados", "Categoría", "Recurso",
                    model.getRecursos(), new Color(65, 105, 225));
        }
        if (Model.ACTIVIDADES.equals(event.getPropertyName())) {
            actividadesFld.setModel(new TableModel("Semana", model.getActividades()));
            mostrarGrafico(graficoActividadesFld, "Actividades realizadas", "Semana", "Semana",
                    model.getActividades(), new Color(220, 50, 47));
        }
    }

    private void mostrarGrafico(JPanel contenedor, String titulo, String eje, String leyenda,
                                Map<String, Integer> datos, Color color) {
        contenedor.removeAll();
        contenedor.setLayout(new BorderLayout());
        if (datos.isEmpty()) {
            contenedor.add(new JLabel("No hay datos en el periodo seleccionado", SwingConstants.CENTER),
                    BorderLayout.CENTER);
            contenedor.revalidate();
            contenedor.repaint();
            return;
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        datos.forEach((etiqueta, cantidad) -> dataset.addValue(cantidad, leyenda, etiqueta));
        JFreeChart chart = ChartFactory.createBarChart(titulo, eje, "Cantidad", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.14);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setShadowVisible(false);
        chart.getCategoryPlot().getDomainAxis().setLowerMargin(0.08);
        chart.getCategoryPlot().getDomainAxis().setUpperMargin(0.08);
        chart.getCategoryPlot().setBackgroundPaint(new Color(238, 238, 238));
        chart.getCategoryPlot().setRangeGridlinePaint(Color.WHITE);
        NumberAxis cantidadAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        cantidadAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        cantidadAxis.setLowerBound(0);
        ChartPanel chartPanel = new ChartPanel(chart);
        // La rueda/trackpad puede alejar indefinidamente los gráficos categóricos.
        // El usuario aún puede ampliar arrastrando un rectángulo vertical y restaurar
        // la escala desde el menú contextual del gráfico.
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(true);
        chartPanel.setFillZoomRectangle(true);
        contenedor.add(chartPanel, BorderLayout.CENTER);
        contenedor.revalidate();
        contenedor.repaint();
    }

    public LocalDate getRecursosDesde() { return recursosDesdeFld.getDate(); }
    public LocalDate getRecursosHasta() { return recursosHastaFld.getDate(); }
    public LocalDate getActividadesDesde() { return actividadesDesdeFld.getDate(); }
    public LocalDate getActividadesHasta() { return actividadesHastaFld.getDate(); }
    public JPanel getPanel() { return panel; }
    public JButton getCargarRecursosFld() { return cargarRecursosFld; }
    public JButton getImprimirRecursosFld() { return imprimirRecursosFld; }
    public JButton getCargarActividadesFld() { return cargarActividadesFld; }
    public JButton getImprimirActividadesFld() { return imprimirActividadesFld; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
