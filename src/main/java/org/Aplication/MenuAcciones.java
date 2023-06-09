package org.Aplication;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

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
        container.setBackground(new Color(36, 33, 39));

        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(new Color(36, 33, 39));
        panel2.setLayout(new GridLayout(2,1, 0,10));
        panel2.setBackground(new Color(36, 33, 39));

        JLabel titulo = new JLabel("ACCIONES DISPONIBLES:");
        titulo.setForeground(new Color(36, 33, 39));
        panel1.add(titulo);
        titulo.setFont(new Font("Impact", Font.BOLD, 14));
        panel1.setBorder(BorderFactory.createEmptyBorder());
        panel1.setBackground(new Color(243, 242, 239));

        panel2.add(verTareas);
        verTareas.setText("Ver mis tareas");
        verTareas.setBackground(new Color(238, 198, 28));

        panel2.add(crearTarea);
        crearTarea.setText("Crear tarea");
        crearTarea.setBackground(new Color(238, 198, 28));

        container.add(panel1);
        container.add(panel2);
        pack();
    }

    private void eventos() {
        verTareas.addActionListener(e -> {
            MenuTareas mt;
            try {
                mt = new MenuTareas(nombre_Usuario, id_Usuario);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            mt.setVisible(true);
            dispose();
        });

        crearTarea.addActionListener(e -> {
            MenuCrearTareas mct = new MenuCrearTareas(0, nombre_Usuario, id_Usuario);
            mct.setVisible(true);
            dispose();
        });
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAcciones(nombre_Usuario, id_Usuario).setVisible(true));
    }
}
