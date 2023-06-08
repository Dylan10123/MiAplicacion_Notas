package org.Aplication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MenuTareas extends JFrame {

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JButton acabadas = new JButton();
    final private JButton pendientes = new JButton();
    final private JButton enProceso = new JButton();
    final private JButton todasLasTareas = new JButton();
    final private JButton btneditar = new JButton();
    final private JButton btnEliminar = new JButton();


    final private JPanel panel1 = new JPanel(null);
    final private JPanel panel2 = new JPanel(null);
    final private JPanel panel3 = new JPanel(null);
    final private JPanel panel4 = new JPanel();

    public static String nombreUsuario;
    public static int id;

    private static JTable table;
    private static DefaultTableModel modeloTabla;

    //Estos osn datos de ejemplo:
    public static Object[][] data = getAllTareas();

    String[] columnNames = {"ID", "Tarea", "Prioridad", "Estado"};

    public MenuTareas(String nombreUsuario, int id) throws SQLException {
        MenuTareas.nombreUsuario = nombreUsuario;
        MenuTareas.id = id;
        crearGUI(nombreUsuario, id);
        eventos();
    }

    private void crearGUI(String nombreUsuario, int id) {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tareas de " + nombreUsuario + " ID de usuario: " + id);

        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        container.setPreferredSize(new Dimension(700, 300));
        container.setBackground(Color.WHITE);

        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(690, 45));
        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);

        JLabel titulo = new JLabel("TAREAS:");
        titulo.setFont(new Font("Impact", Font.BOLD, 14));
        panel1.add(titulo, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        panel2.setLayout(new FlowLayout());

        panel2.add(todasLasTareas);
        todasLasTareas.setText("Todas");

        panel2.add(acabadas);
        acabadas.setText("Acabadas");

        panel2.add(pendientes);
        pendientes.setText("Pendientes");

        panel2.add(enProceso);
        enProceso.setText("En proceso");

        modeloTabla = new DefaultTableModel(data, columnNames);
        table = new JTable();
        table.setModel(modeloTabla);
        table.createDefaultColumnsFromModel();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 175));

        panel3.add(scrollPane);
        panel3.setLayout(new FlowLayout());

        panel4.add(btnEliminar);
        btnEliminar.setText("Eliminar");
        btnEliminar.setBackground(Color.PINK);
        panel4.add(btneditar);
        btneditar.setText("Editar");
        panel4.setBorder(BorderFactory.createEmptyBorder(0,500,0,0));
        panel4.setBackground(Color.WHITE);

        panel1.add(panel2, BorderLayout.EAST);
        container.add(panel1);
        container.add(panel3);
        container.add(panel4);
        pack();
    }


    private void eventos() {
        acabadas.addActionListener((e) -> {
            data = getAllTareasAcabadas();

            table.setVisible(false);
            modeloTabla = new DefaultTableModel(data, columnNames);
            modeloTabla.fireTableDataChanged();
            table.setModel(modeloTabla);
            table.setVisible(true);
        });

        pendientes.addActionListener(e -> {
            data = getAllTareasPendientes();

            table.setVisible(false);
            modeloTabla = new DefaultTableModel(data, columnNames);
            modeloTabla.fireTableDataChanged();
            table.setModel(modeloTabla);
            table.setVisible(true);
        });

        enProceso.addActionListener(e -> {
            data = getAllTareasEnProceso();

            table.setVisible(false);
            modeloTabla = new DefaultTableModel(data, columnNames);
            modeloTabla.fireTableDataChanged();
            table.setModel(modeloTabla);
            table.setVisible(true);
        });

        todasLasTareas.addActionListener(e -> {
            data = getAllTareas();

            table.setVisible(false);
            modeloTabla = new DefaultTableModel(data, columnNames);
            modeloTabla.fireTableDataChanged();
            table.setModel(modeloTabla);
            table.setVisible(true);
        });

        btneditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            int columna = table.getSelectedColumn();
            int user_id = id;

            String id_Tarea = modeloTabla.getValueAt(fila, columna).toString();

            if (esNumerico(id_Tarea)) {
                MenuEditarTarea met = new MenuEditarTarea(nombreUsuario, user_id, Integer.parseInt(id_Tarea));
                met.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione el ID de la tarea que quiere modificar.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            int columna = table.getSelectedColumn();

            String id_Tarea = modeloTabla.getValueAt(fila, columna).toString();

            if (esNumerico(id_Tarea)) {
                String sql = "DELETE FROM Tareas WHERE id=?";

                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "esta seguro de que quiere eliminar esta tarea?");
                    if (respuesta == JOptionPane.YES_OPTION){
                        statement.setInt(1, Integer.parseInt(id_Tarea));
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "La tarea ha sido borrada.");

                        data = getAllTareas();
                        table.setVisible(false);
                        modeloTabla = new DefaultTableModel(data, columnNames);
                        modeloTabla.fireTableDataChanged();
                        table.setModel(modeloTabla);
                        table.setVisible(true);
                    }
                } catch (SQLException exception) {
                    System.out.println("Error al ejecutar la consulta: " + exception.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione el ID de la tarea que quiere modificar.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    //Método para comprobar si un objeto es un numero o no
    public boolean esNumerico(Object valor) {
        try {
            Double.parseDouble(String.valueOf(valor));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para obtener los datos de la tabla de la base de datos y cargarlos en una matriz
    public static Object[][] getAllTareas() {
        Statement statement = null;
        Statement statement2;
        ResultSet resultSet = null;
        ResultSet rs2;
        Object [][] data = null;

        try {
            String query = "SELECT * FROM Tareas WHERE id_usuario = " + id;

            // Crea un objeto Statement para ejecutar la consulta
            statement = con.createStatement();

            // Ejecuta la consulta y obtiene el conjunto de resultados
            resultSet = statement.executeQuery(query);


            String query2 = "SELECT COUNT(*)  FROM Tareas WHERE id_usuario = " + id;

            statement2 = con.createStatement();

            rs2 = statement2.executeQuery(query2);

            int rowCount = rs2.getInt(1);

            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Inserta los datos del conjunto de resultados en la matriz
            insertarDatos(data, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Object[][] getAllTareasAcabadas() {
        Statement statement = null;
        Statement statement2;
        ResultSet resultSet = null;
        ResultSet rs2;
        Object [][] data = null;

        try {
            String query = "SELECT * FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'Acabada'";

            // Crea un objeto Statement para ejecutar la consulta
            statement = con.createStatement();

            // Ejecuta la consulta y obtiene el conjunto de resultados
            resultSet = statement.executeQuery(query);


            String query2 = "SELECT COUNT(*)  FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'Acabada'";

            statement2 = con.createStatement();

            rs2 = statement2.executeQuery(query2);

            int rowCount = rs2.getInt(1);

            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Inserta los datos del conjunto de resultados en la matriz
            insertarDatos(data, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Object[][] getAllTareasPendientes() {
        Statement statement = null;
        Statement statement2;
        ResultSet resultSet = null;
        ResultSet rs2;
        Object [][] data = null;

        try {
            String query = "SELECT * FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'Pendiente'";

            // Crea un objeto Statement para ejecutar la consulta
            statement = con.createStatement();

            // Ejecuta la consulta y obtiene el conjunto de resultados
            resultSet = statement.executeQuery(query);


            String query2 = "SELECT COUNT(*)  FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'Pendiente'";

            statement2 = con.createStatement();

            rs2 = statement2.executeQuery(query2);

            int rowCount = rs2.getInt(1);

            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Inserta los datos del conjunto de resultados en la matriz
            insertarDatos(data, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Object[][] getAllTareasEnProceso() {
        Statement statement = null;
        Statement statement2;
        ResultSet resultSet = null;
        ResultSet rs2;
        Object [][] data = null;

        try {
            String query = "SELECT * FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'En proceso'";

            // Crea un objeto Statement para ejecutar la consulta
            statement = con.createStatement();

            // Ejecuta la consulta y obtiene el conjunto de resultados
            resultSet = statement.executeQuery(query);


            String query2 = "SELECT COUNT(*)  FROM Tareas WHERE id_usuario = " + id + " AND Estado = 'En proceso'";

            statement2 = con.createStatement();

            rs2 = statement2.executeQuery(query2);

            int rowCount = rs2.getInt(1);

            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Inserta los datos del conjunto de resultados en la matriz
            insertarDatos(data, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Método para insertar los datos de la matriz en una tabla de Java Swing
    public static void insertarDatos(Object[][] data, ResultSet resultSet) throws SQLException {
        // Obtiene el número de columnas en el conjunto de resultados
        int columnCount = resultSet.getMetaData().getColumnCount();

        // Recorre los resultados y almacena los datos en la matriz
        int row = 0;
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int col = 0; col < columnCount; col++) {
                rowData[col] = resultSet.getObject(col + 1);
            }
            data[row] = rowData;
            row++;
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new MenuTareas(nombreUsuario, id).setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}