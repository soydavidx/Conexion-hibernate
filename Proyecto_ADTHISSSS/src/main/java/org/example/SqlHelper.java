package org.example;

import java.sql.*;

public class SqlHelper {
    private static final String URL = "jdbc:sqlite:src/company_database.db";


    private static void createTable(String sql) {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTableEmployee() {
        String sql = "CREATE TABLE IF NOT EXISTS Employee (" +
                " employee_id INTEGER PRIMARY KEY," +
                "project_id INTEGER NOT NULL," +
                " FOREIGN KEY (project_id) REFERENCES Project(project_id)," +
                " hours_worked FLOAT NOT NULL);";
        createTable(sql);
    }

    public static void createTableCustomer() {
        String sql = "CREATE TABLE IF NOT EXISTS Customer (" +
                " customer_id INTEGER PRIMARY KEY," +
                " customer_name TEXT NOT NULL," +
                " contact_email TEXT NOT NULL," +
                " contact_phone TEXT NOT NULL);";
        createTable(sql);
    }

    public static void createTableProject() {
        String sql = "CREATE TABLE IF NOT EXISTS Project (" +
                " project_id INTEGER PRIMARY KEY," +
                "customer_id INTEGER NOT NULL," +
                " FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)," +
                " project_name TEXT NOT NULL," +
                " budget FLOAT NOT NULL," +
                " start_date TEXT NOT NULL," +
                " end_date TEXT NOT NULL);";
        createTable(sql);
    }

    public static void createTableOrder() {
        String sql = "CREATE TABLE IF NOT EXISTS \"Order\" (" +
                " order_id INTEGER PRIMARY KEY," +
                " customer_id INTEGER NOT NULL," +
                " order_date TEXT NOT NULL," +
                " amount INTEGER NOT NULL," +
                " FOREIGN KEY (customer_id) REFERENCES customer(customer_id));";
        createTable(sql);
    }

    public static void createTableOrderItem() {
        String sql = "CREATE TABLE IF NOT EXISTS Order_item (" +
                " order_item_id INTEGER PRIMARY KEY," +
                "order_id INTEGER NOT NULL," +
                " FOREIGN KEY (order_id) REFERENCES Order(order_id)," +
                " product_name TEXT NOT NULL," +
                " quantity INTEGER NOT NULL," +
                " price REAL NOT NULL);";
        createTable(sql);
    }

    public static void createTableDepartment() {
        String sql = "CREATE TABLE IF NOT EXISTS Department (" +
                " department_id INTEGER PRIMARY KEY," +
                " employee_id INTEGER," +  // Cambiado NOT NULL a opcional
                " FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)," +
                " department_name TEXT NOT NULL" +  // Eliminado punto y coma
                ");";
        createTable(sql);
    }

    public static void createTableEmployeeRealistic() {
        String sql = "CREATE TABLE IF NOT EXISTS EmployeeRealistic (" +
                " employee_id INTEGER PRIMARY KEY," +
                " employee_first_name TEXT NOT NULL," +
                " employee_last_name TEXT NOT NULL," +
                " department_id INTEGER NOT NULL," +
                " FOREIGN KEY (department_id) REFERENCES Department(department_id)," +
                " hire_date TEXT NOT NULL," +
                " salary REAL NOT NULL," +
                " position TEXT NOT NULL);";
        createTable(sql);
    }

    public static void createTablePresupuesto(){
        String sql = "CREATE TABLE IF NOT EXISTS Presupuesto (" +
                " presupuesto_id INTEGER PRIMARY KEY," +
                "project_id INTEGER NOT NULL," +
                " FOREIGN KEY (project_id) REFERENCES Project(project_id)," +
                " PresupuestoTotal TEXT NOT NULL," +
                " TotalSalaryCost TEXT NOT NULL," +
                " FraccionSalario NOT NULL);";
        createTable(sql);
    }

}

