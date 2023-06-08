package org.Aplication;

import javax.swing.*;
import java.awt.*;

public class MenuAcciones extends JFrame{

    final private Container container = getContentPane();

    final private JButton verTareas = new JButton();
    final private JButton crearTarea = new JButton();

    final private JPanel panel1 = new JPanel(null);
    final private JPanel panel2 = new JPanel(null);

    private static String nombre_Usuario;
    private static int id_Usuario;

    public MenuAcciones(String nombre, int id) {
        MenuAcciones.nombre_Usuario = nombre;
        MenuAcciones.id_Usuario = id;
        crearGUI();
        eventos();
    }

    private void crearGUI() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.setPreferredSize(new Dimension(220, 110));

        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2.setLayout(new GridLayout(2,1, 0,10));

        JLabel titulo = new JLabel("ACCIONES DISPONIBLES:");
        panel1.add(titulo);
        titulo.setFont(new Font("Impact", Font.BOLD, 14));
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        panel2.add(verTareas);
        verTareas.setText("Ver mis tareas");

        panel2.add(crearTarea);
        crearTarea.setText("Crear tarea");

        container.add(panel1);
        container.add(panel2);
        pack();
    }

    private void eventos() {

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAcciones(nombre_Usuario, id_Usuario).setVisible(true));
    }
}
