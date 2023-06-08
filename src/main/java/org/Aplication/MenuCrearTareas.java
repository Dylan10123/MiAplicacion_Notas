package org.Aplication;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuCrearTareas extends JFrame {

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JButton btnCrear = new JButton();
    final private JButton btnCancelar = new JButton();

    final private JTextField textTarea = new JTextField();

    final private String[] estados = {"Acabada", "En proceso", "Pendiente"};
    final private JComboBox<String> boxEstados = new JComboBox<>(estados);

    final private String[] prioridades = {"1", "2", "3", "4", "5"};
    final private JComboBox<String> boxPrioridades = new JComboBox<>(prioridades);

    final private JPanel panelTitulo = new JPanel(new BorderLayout());
    final private JPanel panel1 = new JPanel();
    final private JPanel panel2 = new JPanel();

    static int panelAnterior;
    static String nombre_Usuario;
    static int id_Usuario;

    public MenuCrearTareas(int panelAnterior, String nombre_Usuario, int id_Usuario){
        MenuCrearTareas.panelAnterior = panelAnterior;
        MenuCrearTareas.nombre_Usuario = nombre_Usuario;
        MenuCrearTareas.id_Usuario = id_Usuario;
        crearGUI();
        eventos();
    }

    private void crearGUI(){
        setLocationRelativeTo(null);
        setTitle("Inicio de sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.setPreferredSize(new Dimension(600, 125));
        container.setLayout(new FlowLayout(FlowLayout.CENTER));

        panelTitulo.setBackground(Color.LIGHT_GRAY);
        panelTitulo.setPreferredSize(new Dimension(590, 38));

        JLabel titulo = new JLabel("Creacion de tareas:");
        panelTitulo.add(titulo, BorderLayout.WEST);
        panelTitulo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        titulo.setFont(new Font("", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2.setBackground(Color.LIGHT_GRAY);

        JLabel lblTarea = new JLabel("Tarea: ");
        panel1.add(lblTarea);
        panel1.add(textTarea);
        textTarea.setColumns(10);

        JLabel lblPrioridad = new JLabel("Prioridad: ");
        panel1.add(lblPrioridad);
        panel1.add(boxPrioridades);

        JLabel lblEstados = new JLabel("Estado: ");
        panel1.add(lblEstados);
        panel1.add(boxEstados);

        panel2.add(btnCancelar);
        panel2.add(btnCrear);
        btnCrear.setText("Crear Tarea");
        btnCancelar.setText("Cancelar");

        panelTitulo.add(panel2, BorderLayout.EAST);
        container.add(panelTitulo);
        container.add(panel1);
        pack();
    }

    private void eventos() {
        btnCancelar.addActionListener(e -> {
            if (panelAnterior == 0) {
                MenuAcciones ma = new MenuAcciones(nombre_Usuario, id_Usuario);
                ma.setVisible(true);
                dispose();
            } else if (panelAnterior == 1) {
                try {
                    MenuTareas mt = new MenuTareas(nombre_Usuario, id_Usuario);
                    mt.setVisible(true);
                    dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnCrear.addActionListener(e -> {
            String sql = "INSERT INTO Tareas (Tarea,Prioridad,Estado,id_usuario) VALUES (?, ?, ?, ?)";
            String tarea = textTarea.getText();
            int prio = Integer.parseInt(boxPrioridades.getSelectedItem().toString());
            String estado = boxEstados.getSelectedItem().toString();
            int id_user = id_Usuario;

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, tarea);
                statement.setInt(2, prio);
                statement.setString(3, estado);
                statement.setInt(4, id_user);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Tarea creada con exito.");

                if (panelAnterior == 0) {
                    MenuAcciones ma = new MenuAcciones(nombre_Usuario, id_Usuario);
                    ma.setVisible(true);
                    dispose();
                } else if (panelAnterior == 1) {
                    try {
                        MenuTareas mt = new MenuTareas(nombre_Usuario, id_Usuario);
                        mt.setVisible(true);
                        dispose();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } catch (SQLException exception) {
                System.out.println("Error al ejecutar la consulta: " + exception.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuCrearTareas(panelAnterior, nombre_Usuario, id_Usuario).setVisible(true));
    }
}
