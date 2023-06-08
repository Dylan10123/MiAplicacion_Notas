package org.Aplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class pruebas {
    static Connection con = DatabaseConnection.getInstance().getConnection();


    public static Object[][] getDataFromDatabase() {
        Object[][] data = null;
        int id = 2;

        try {

            String sql = "SELECT * FROM tareas WHERE id_usuario = ? ";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            // Obtiene el número de columnas en el conjunto de resultados
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Obtiene el número de filas en el conjunto de resultados
            int rowCount = 0;
            if (resultSet.last()) {
                rowCount = resultSet.getRow();
                resultSet.beforeFirst(); // Vuelve al principio del conjunto de resultados
            }

            // Inicializa la matriz con el tamaño adecuado
            data = new Object[rowCount][columnCount];

            // Recorre los resultados y almacena los datos en la matriz
            int row = 0;
            while (resultSet.next()) {
                for (int col = 0; col < columnCount; col++) {
                    data[row][col] = resultSet.getObject(col + 1);
                }
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void main(String[] args) {
        Object[][] matriz = getDataFromDatabase();
        System.out.println(Arrays.deepToString(matriz));
    }

}
