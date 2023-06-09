package org.Aplication;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MenuEditarTarea extends JFrame{

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JButton aceptar = new JButton();
    final private JButton cancelar = new JButton();

    final private JTextField textoTarea = new JTextField();

    final private ButtonGroup radioGroup = new ButtonGroup();
    final private JRadioButton acabadas = new JRadioButton("A");
    final private JRadioButton pendientes = new JRadioButton("P");
    final private JRadioButton enProceso = new JRadioButton("EP");

    final private String[] prioridades = {"1", "2", "3", "4", "5"};
    final private JComboBox<String> boxPrioridades = new JComboBox<>(prioridades);

    final private JPanel panel1 = new JPanel(new BorderLayout());
    final private JPanel panel2 = new JPanel();
    final private JPanel panelTarea = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final private JPanel panelPE = new JPanel();
    final private JPanel panel3 = new JPanel();

    static String nombreUsuario;
    static int id;
    static int id_Tarea;

    public MenuEditarTarea(String nombreUsuario, int id, int id_Tarea) {
        MenuEditarTarea.nombreUsuario = nombreUsuario;
        MenuEditarTarea.id = id;
        MenuEditarTarea.id_Tarea = id_Tarea;
        crearGUI();
        eventos();
    }

    private void crearGUI() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Usuario: " + nombreUsuario + " -- UID: " + id + " -- Tarea: " + id_Tarea);

        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.setPreferredSize(new Dimension(600, 200));
        container.setBackground(new Color(36, 33, 39));

        panel1.setPreferredSize(new Dimension(590, 45));
        panel1.setBackground(Color.LIGHT_GRAY);

        panel2.setBackground(Color.LIGHT_GRAY);

        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.setPreferredSize(new Dimension(540, 125));
        panel3.setBackground(new Color(36, 33, 39));

        JLabel titulo = new JLabel("TAREA:");
        titulo.setFont(new Font("", Font.BOLD, 14));
        panel1.add(titulo, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        panel2.add(aceptar);
        aceptar.setText("Aceptar");

        panel2.add(cancelar);
        cancelar.setText("Cancelar");

        JLabel lblTarea = new JLabel("Tarea: ");
        panelTarea.add(lblTarea);
        panelTarea.add(textoTarea);
        panelTarea.setBorder(BorderFactory.createEmptyBorder(30,10,0,0));
        panelTarea.setBackground(new Color(36, 33, 39));
        textoTarea.setColumns(30);
        if (getPrioridad(id_Tarea) != 0){
            textoTarea.setText(getTextoTarea(id_Tarea));
        } else {
            textoTarea.setText("null");
        }

        JLabel prio = new JLabel("Prioridad: ");
        panelPE.add(prio);
        panelPE.add(boxPrioridades);
        boxPrioridades.setBorder(BorderFactory.createEmptyBorder(0,0,0,50));
        boxPrioridades.setBackground(new Color(36, 33, 39));
        if (getPrioridad(id_Tarea) != 0){
            boxPrioridades.setSelectedIndex(getPrioridad(id_Tarea) - 1);
        }

        JLabel estado = new JLabel("Estado: ");
        panelPE.add(estado);
        radioGroup.add(acabadas);
        radioGroup.add(pendientes);
        radioGroup.add(enProceso);
        panelPE.add(acabadas);
        panelPE.add(pendientes);
        panelPE.add(enProceso);
        panelPE.setBackground(new Color(36, 33, 39));
        acabadas.setBackground(new Color(36, 33, 39));
        pendientes.setBackground(new Color(36, 33, 39));
        enProceso.setBackground(new Color(36, 33, 39));

        panel3.add(panelTarea);
        panel3.add(panelPE);
        panel1.add(panel2, BorderLayout.EAST);
        container.add(panel1);
        container.add(panel3);
        pack();
    }

    private void eventos() {
        cancelar.addActionListener(e -> {
            MenuTareas mt;
            try {
                mt = new MenuTareas(nombreUsuario, id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            mt.setVisible(true);
            dispose();
        });

        aceptar.addActionListener(e -> {
            String texto = textoTarea.getText();
            int prioridad = Integer.parseInt(boxPrioridades.getSelectedItem().toString());
            String estado = null;
            if (acabadas.isSelected()){
                estado = "Acabada";
            } else if (pendientes.isSelected()) {
                estado = "Pendiente";
            } else if (enProceso.isSelected()) {
                estado = "En proceso";
            }

            String sql = "UPDATE Tareas SET Tarea=?,Prioridad=?,Estado=?,id_usuario=? WHERE id=?;";

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, texto);
                statement.setInt(2, prioridad);
                statement.setString(3, estado);
                statement.setInt(4, id);
                statement.setInt(5, id_Tarea);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos actualizados con exito.");
            } catch (SQLException exception) {
                System.out.println("Error al ejecutar la consulta: " + exception.getMessage());
            }

            MenuTareas mt;
            try {
                mt = new MenuTareas(nombreUsuario, id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            mt.setVisible(true);
            dispose();
        });
    }

    private int getPrioridad(int id_Tarea) {
        String sql = "SELECT Prioridad FROM Tareas WHERE id = " + id_Tarea;
        int prio;

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            prio = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prio;
    }

    private String getTextoTarea(int id_Tarea) {
        String sql = "SELECT Tarea FROM Tareas WHERE id = " + id_Tarea;
        String texto;

        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            texto = resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return texto;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuEditarTarea(nombreUsuario, id, id_Tarea).setVisible(true));
    }
}
