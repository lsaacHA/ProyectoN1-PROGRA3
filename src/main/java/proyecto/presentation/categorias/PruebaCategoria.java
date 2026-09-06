package proyecto.presentation.categorias;

import javax.swing.*;

public class PruebaCategoria {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prueba - Categorías");

        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);

        frame.setContentPane(view.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}