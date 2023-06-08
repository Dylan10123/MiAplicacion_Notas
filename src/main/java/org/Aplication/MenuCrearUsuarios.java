package org.Aplication;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MenuCrearUsuarios extends JFrame {

    static Connection con = DatabaseConnection.getInstance().getConnection();

    final private Container container = getContentPane();

    final private JButton btnCrear = new JButton();
    final private JButton btnCancelar = new JButton();

    final private JTextField textNombre = new JTextField();
    final private JPasswordField contra = new JPasswordField();

    final private JPanel panel = new JPanel(new BorderLayout());
    final private JPanel panel1 = new JPanel(null);
    final private JPanel panel2 = new JPanel(null);
    final private JPanel panel3 = new JPanel(null);

    public  MenuCrearUsuarios(){
        crearGUI();
        eventos();
    }

    private void crearGUI(){
        setLocationRelativeTo(null);
        setTitle("Creacion de usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        panel.setBackground(Color.LIGHT_GRAY);
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JLabel titulo = new JLabel("Creacion de usuario:");
        panel.add(titulo, BorderLayout.WEST);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        titulo.setFont(new Font("Impact", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JLabel lblNombre = new JLabel("Nombre: ");
        panel1.add(lblNombre);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0,27,0,0));

        panel1.add(textNombre);
        textNombre.setColumns(10);

        JLabel lblContra = new JLabel("Contraseña: ");
        panel2.add(lblContra);

        panel2.add(contra);
        contra.setColumns(10);

        panel3.add(btnCancelar);
        btnCancelar.setText("Cancelar");
        btnCancelar.setBackground(Color.PINK);

        panel3.add(btnCrear);
        btnCrear.setText("Crear Usuario");
        btnCrear.setBackground(new Color(140, 245, 122));

        
        container.add(panel);
        container.add(panel1);
        container.add(panel2);
        container.add(panel3);
        pack();
    }

    private void eventos() {
        btnCancelar.addActionListener(e -> {
            MenuInicio mi = new MenuInicio();
            mi.setVisible(true);
            dispose();
        });

        btnCrear.addActionListener(e -> {
            String nombre = textNombre.getText();
            String pwd = String.valueOf(contra.getPassword());

            String sql = "INSERT INTO usuarios (Nombre, Contra) VALUES (? , ?);";

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, nombre);
                statement.setString(2, pwd);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuario creado con exito.");

                MenuInicio mi = new MenuInicio();
                mi.setVisible(true);
                dispose();
            } catch (SQLException exception) {
                System.out.println("Error al ejecutar la consulta: " + exception.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuCrearUsuarios().setVisible(true));
    }
}
