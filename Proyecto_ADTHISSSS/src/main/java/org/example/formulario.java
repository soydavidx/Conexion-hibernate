package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class formulario {
    private JPanel panel1;
    private JScrollPane scrollpanel;
    private JTable table;

    // Constructor
    public formulario() {
        // Configurar la tabla
        String[] columnNames = {"Presupuesto ID", "Project ID", "Presupuesto Total", "Total Salary Cost", "Fracción Salario"};

        // Crear el modelo de la tabla y asociarlo con el JTable
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        // Crear la tabla
        table = new JTable(model);

        // Crear el JScrollPane para contener la tabla y permitir el desplazamiento
        scrollpanel = new JScrollPane(table);

        // Añadir el JScrollPane al panel
        panel1.setLayout(new BorderLayout());
        panel1.add(scrollpanel, BorderLayout.CENTER);

        // Llamar al método para cargar los datos en la tabla
        loadDataToTable(model);
    }

    // Método para cargar los datos de la base de datos en el JTable
    public void loadDataToTable(DefaultTableModel model) {
        // Conexión a la base de datos
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:company_database.db")) {
            // Consulta para obtener los datos de la tabla "Presupuesto"
            String sql = "SELECT * FROM Presupuesto";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            // Iterar sobre los resultados y agregar las filas al modelo de la tabla
            while (resultSet.next()) {
                int presupuestoId = resultSet.getInt("presupuesto_id");
                int projectId = resultSet.getInt("project_id");
                String presupuestoTotal = resultSet.getString("Presupuesto_total");
                String totalSalaryCost = resultSet.getString("TotalSalaryCost");
                String fraccionSalario = resultSet.getString("FraccionSalario") + "%";

                // Agregar una fila al modelo con los datos obtenidos
                model.addRow(new Object[]{presupuestoId, projectId, presupuestoTotal, totalSalaryCost, fraccionSalario});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el panel que puedes añadir a un JFrame
    public JPanel getPanel() {
        return panel1;
    }

    // Método principal para probar la interfaz
    public static void main(String[] args) {
        // Crear el formulario
        JFrame frame = new JFrame("Formulario Presupuesto");
        formulario formulario = new formulario();

        // Configurar el JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setContentPane(formulario.getPanel());
        frame.setVisible(true);
    }
}
