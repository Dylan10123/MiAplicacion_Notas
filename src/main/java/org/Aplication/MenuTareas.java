package org.Aplication;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    final private JButton btnAtras = new JButton();
    final private JButton btnCrearTarea = new JButton();

    final private JPanel panel1 = new JPanel(null);
    final private JPanel panel2 = new JPanel(null);
    final private JPanel panel3 = new JPanel(null);
    final private JPanel panel4 = new JPanel();
    final private JPanel panel5 = new JPanel();
    final private JPanel panelBotones = new JPanel(new BorderLayout());

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
        container.setBackground(new Color(36, 33, 39));

        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(690, 38));
        panel1.setBackground(new Color(116, 102, 64));

        panel2.setBackground(new Color(116, 102, 64));

        panelBotones.setPreferredSize(new Dimension(675, 30));
        panelBotones.setBackground(new Color(36, 33, 39));

        JLabel titulo = new JLabel("TAREAS:");
        titulo.setFont(new Font("Impact", Font.BOLD, 14));
        titulo.setForeground(new Color(243, 242, 239));
        panel1.add(titulo, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        panel2.setLayout(new FlowLayout());

        panel2.add(todasLasTareas);
        todasLasTareas.setText("Todas");
        todasLasTareas.setBackground(new Color(243, 242, 239));
        todasLasTareas.setForeground(new Color(36, 33, 39));

        panel2.add(acabadas);
        acabadas.setText("Acabadas");
        acabadas.setBackground(new Color(243, 242, 239));
        acabadas.setForeground(new Color(36, 33, 39));

        panel2.add(pendientes);
        pendientes.setText("Pendientes");
        pendientes.setBackground(new Color(243, 242, 239));
        pendientes.setForeground(new Color(36, 33, 39));

        panel2.add(enProceso);
        enProceso.setText("En proceso");
        enProceso.setBackground(new Color(243, 242, 239));
        enProceso.setForeground(new Color(36, 33, 39));

        modeloTabla = new DefaultTableModel(data, columnNames);
        table = new JTable();
        table.setModel(modeloTabla);
        table.createDefaultColumnsFromModel();
        table.setDefaultRenderer(Object.class, new MenuTareas.CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 175));
        scrollPane.setBorder(BorderFactory.createSoftBevelBorder(1));
        scrollPane.setBackground(new Color(243, 242, 239));

        panel3.add(scrollPane);
        panel3.setBorder(BorderFactory.createSoftBevelBorder(1));
        panel3.setBackground(new Color(243, 242, 239));
        panel3.setLayout(new FlowLayout());

        panel4.add(btnEliminar);
        btnEliminar.setText("Eliminar");
        btnEliminar.setBackground(new Color(229, 42, 68));
        btnEliminar.setForeground(new Color(243, 242, 239));
        panel4.add(btneditar);
        btneditar.setText("Editar");
        btneditar.setBackground(new Color(238, 198, 28));
        panel4.setBackground(new Color(36, 33, 39));

        panel5.add(btnAtras);
        panel5.add(btnCrearTarea);
        panel5.setBackground(new Color(36, 33, 39));
        btnAtras.setText("Atras");
        btnCrearTarea.setText("Crear tarea");
        btnAtras.setBackground(new Color(238, 198, 28));
        btnCrearTarea.setBackground(new Color(238, 198, 28));

        panel1.add(panel2, BorderLayout.EAST);
        panelBotones.add(panel5, BorderLayout.WEST);
        panelBotones.add(panel4, BorderLayout.EAST);
        container.add(panel1);
        container.add(panel3);
        container.add(panelBotones);
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

        btnAtras.addActionListener(e -> {
            MenuAcciones ma = new MenuAcciones(nombreUsuario, id);
            ma.setVisible(true);
            dispose();
        });

        btnCrearTarea.addActionListener(e -> {
            MenuCrearTareas mct = new MenuCrearTareas(1, nombreUsuario, id);
            mct.setVisible(true);
            dispose();
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

    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Cambiar el color de fondo de las líneas cuya segunda celda contiene "Doe"
            Object estado = table.getValueAt(row, 3);
            if (estado != null && estado.toString().equals("Acabada")) {
                component.setBackground(new Color(119, 232, 62, 218)); // Cambiar el color de fondo de la línea aquí
            } else if (estado != null && estado.toString().equals("En proceso")){
                component.setBackground(new Color(255, 226, 90, 255));
            } else if (estado != null && estado.toString().equals("Pendiente")) {
                component.setBackground(new Color(255, 75, 75, 218));
            } else {
                component.setBackground(table.getBackground());
            }

            return component;
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
