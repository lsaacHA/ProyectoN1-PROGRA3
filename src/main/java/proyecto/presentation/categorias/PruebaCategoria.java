package proyecto.presentation.categorias;

import proyecto.logic.Service;

import javax.swing.*;

public class PruebaCategoria {
    public static void main(String[] args) throws Exception {
        Service service = new Service();
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view, service);

        JFrame frame = new JFrame("Prueba - Categorías");
        frame.setContentPane(view.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}