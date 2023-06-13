package org.Aplication;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class MenuInicio extends JFrame {

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JTextField textNombre = new JTextField();
    final private JPasswordField contra = new JPasswordField();

    final private JButton crearUsuario = new JButton();
    final private JButton iniciarSesion = new JButton();

    final private JPanel panel1 = new JPanel(null);
    final private JPanel panel2 = new JPanel(null);
    final private JPanel panel3 = new JPanel(null);

    public MenuInicio() {
          crearGUI();
          eventos();
    }

    private void crearGUI() {
        setLocationRelativeTo(null);
        setTitle("Inicio de sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(36, 33, 39));

        panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel1.setBackground(new Color(36, 33, 39));
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.setBackground(new Color(36, 33, 39));
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel3.setBackground(new Color(36, 33, 39));

        JLabel lblNombre = new JLabel("Nombre: ");
        lblNombre.setForeground(new Color(249, 248, 249));
        panel1.add(lblNombre);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));

        panel1.add(textNombre);
        textNombre.setColumns(10);
        textNombre.setBorder(BorderFactory.createLineBorder(new Color(36, 33, 39), 0));

        JLabel lblContra = new JLabel("Contraseña: ");
        lblContra.setForeground(new Color(249, 248, 249));
        panel2.add(lblContra);

        panel2.add(contra);
        contra.setColumns(10);
        contra.setBorder(BorderFactory.createLineBorder(new Color(36, 33, 39), 0));

        panel3.add(crearUsuario);
        crearUsuario.setText("Crear Usuario");
        crearUsuario.setBackground(new Color(238, 198, 28));
        crearUsuario.setForeground(new Color(36, 33, 39));

        panel3.add(iniciarSesion);
        iniciarSesion.setText("Iniciar sesion");
        iniciarSesion.setBackground(new Color(238, 198, 28));
        iniciarSesion.setForeground(new Color(36, 33, 39));

        container.add(panel1);
        container.add(panel2);
        container.add(panel3);
        pack();
    }

    private void eventos() {
        //Al darle a iniciar sesion se pasa al menu de acciones
        iniciarSesion.addActionListener((e) -> {
            String nombre = textNombre.getText();
            String passwd = String.valueOf(contra.getPassword());
            if (existeUsuario(nombre, passwd)) {
                MenuAcciones ma;
                ma = new MenuAcciones(nombre, getIdUsuario(nombre, passwd));
                ma.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        //Al darle a crear usuario se abre el menu de creacion de usarios
        crearUsuario.addActionListener(e -> {
            MenuCrearUsuarios mcu = new MenuCrearUsuarios();
            mcu.setVisible(true);
            dispose();
        });
    }

    //este metodo comprueba si existe un usuario con esa contraseña en la base de datos
    private boolean existeUsuario(String nombre, String contra) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE Nombre = ? AND Contra = ?";
        boolean existe = false;

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, contra);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                int contador = resultado.getInt(1);
                if (contador > 0) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return existe;
    }

    //Sacar el ID del usuario a partir del usuario y contraseña para pasarlo a menus posteriores
    private int getIdUsuario(String nombre, String contra) {
        int id = 0;
        String sql = "SELECT id FROM usuarios WHERE Nombre = ? AND Contra = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, contra);
            ResultSet resultado = statement.executeQuery();
            id = resultado.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }

        return id;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuInicio().setVisible(true));
    }
}
