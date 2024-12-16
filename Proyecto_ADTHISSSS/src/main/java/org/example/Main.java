package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Configurar la SessionFactory
        SessionFactory sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(Project.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(EmployeeRealistic.class)
                .addAnnotatedClass(Department.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(OrderItem.class)
                .buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                // Llamar a los métodos de procesamiento, pasando la sesión
                CSVInserter.processCustomers(session);
                CSVInserter.processDepartments(session);
                CSVInserter.processEmployeeRealistic(session);
                CSVInserter.processProjects(session);
                CSVInserter.processEmployeeProjects(session);
                CSVInserter.processOrders(session);
                CSVInserter.processOrderItems(session);

                //crear tabla de presupuesto
                int TotalProjects = CSVInserter.getTotalProjects(session);
                System.out.println("Total de proyectos: " + TotalProjects);  // Agrega esta línea
                for (int i = 1; i <= TotalProjects; i++) {
                    CSVInserter.getPresupuestoProyecto(session, i);
                }
                // Confirmar la transacción
                transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        } finally {
            sessionFactory.close();
        }
    }
}
