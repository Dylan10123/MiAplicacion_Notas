package org.Aplication;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class MenuAcciones extends JFrame{

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JButton verTareas = new JButton();
    final private JButton crearTarea = new JButton();
    final private JButton guardarArchivo = new JButton();

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
        container.setPreferredSize(new Dimension(220, 140));
        container.setBackground(new Color(36, 33, 39));

        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(new Color(36, 33, 39));
        panel2.setLayout(new GridLayout(3,1, 0,10));
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

        panel2.add(guardarArchivo);
        guardarArchivo.setText("Guardar archivo");
        guardarArchivo.setBackground(new Color(238, 198, 28));

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

        guardarArchivo.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int seleccion = fileChooser.showSaveDialog(MenuAcciones.this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    guardarTareas(file);
                    JOptionPane.showMessageDialog(MenuAcciones.this, "Archivo guardado correctamente.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MenuAcciones.this, "Error al guardar el archivo.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MenuAcciones.this, "Error al ejecutar la consulta SQL.");
                }
            }
        });
    }

    private void guardarTareas(File file) throws IOException, SQLException {
        Statement statement = con.createStatement();
        String sql = "SELECT Tarea, Prioridad, Estado FROM tareas WHERE id_usuario = " + id_Usuario;
        ResultSet resultSet = statement.executeQuery(sql);

        // Escribe el contenido de la consulta en el archivo seleccionado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            writer.write("-----------------------------------------------");
            writer.newLine();
            writer.write("\t\tTareas del usaurio: " + nombre_Usuario);
            writer.newLine();
            writer.write("-----------------------------------------------");
            writer.newLine();
            writer.newLine();

            // Escribe los datos de cada fila en el archivo
            while (resultSet.next()) {
                writer.write(".- La tarea ");
                for (int i = 1; i <= columnCount; i++) {
                    if (metaData.getColumnName(i).equals("Prioridad")){
                        writer.write("tiene una prioridad ");
                        switch (resultSet.getInt(i)) {
                            case 1:
                                writer.write("muy alta");
                                break;
                            case 2:
                                writer.write("alta");
                                break;
                            case 3:
                                writer.write("media");
                                break;
                            case 4:
                                writer.write("baja");
                                break;
                            case 5:
                                writer.write("muy baja");
                                break;
                        }
                        writer.write(", y esta");
                    } else {
                        writer.write(resultSet.getString(i));
                    }
                    if (i < columnCount) {
                        writer.write(" ");
                    }
                }
                writer.write(".");
                writer.newLine();
                writer.newLine();
            }
        }

        statement.close();
        con.close();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuAcciones(nombre_Usuario, id_Usuario).setVisible(true));
    }
}
