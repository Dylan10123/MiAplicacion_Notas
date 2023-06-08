package org.Aplication;

import java.sql.*;
import java.util.Arrays;

public class Main {
    static Connection connection = DatabaseConnection.getInstance().getConnection();
    // Método para obtener los datos de la tabla de la base de datos y cargarlos en una matriz
    public static Object[][] getDataFromDB(Object[][] data) throws SQLException {
        Statement statement = null;
        Statement statement2 = null;
        ResultSet resultSet = null;
        ResultSet rs2 = null;

        try {
            String query = "SELECT * FROM Tareas WHERE id_usuario = 2";

            // Crea un objeto Statement para ejecutar la consulta
            statement = connection.createStatement();

            // Ejecuta la consulta y obtiene el conjunto de resultados
            resultSet = statement.executeQuery(query);


            String query2 = "SELECT COUNT(*)  FROM Tareas WHERE id_usuario = 2";

            statement2 = connection.createStatement();

            rs2 = statement2.executeQuery(query2);

            int rowCount = rs2.getInt(1);
            
            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Inserta los datos del conjunto de resultados en la matriz
            insertarDatos(data, resultSet);

            // Muestra los datos de la matriz
            for (Object[] rowData : data) {
                for (Object value : rowData) {
                    System.out.print(value + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierra los objetos de conexión y consulta
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public static void main(String[] args) throws SQLException {
        Object[][] data = null;
        getDataFromDB(data);
    }
}
