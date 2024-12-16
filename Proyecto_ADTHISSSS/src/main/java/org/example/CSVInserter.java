package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CSVInserter {

    public static void processCustomers(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/customers.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.out.println("Archivo 'customers.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true;
            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    Customer customer = new Customer();
                    customer.setCustomer_name(nextLine[1]);
                    customer.setCustomer_email(nextLine[2]);
                    customer.setCustomer_phone(nextLine[3]);
                    session.persist(customer);
                } catch (Exception e) {
                    System.out.println("Error procesando cliente: " + e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static void processDepartments(Session session) {
        try (InputStream inputStreamDepartments = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/departments.csv");
             InputStream inputStreamEmployees = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/employees_realistic.csv");
             CSVReader readerDepartments = new CSVReader(new InputStreamReader(inputStreamDepartments));
             CSVReader readerEmployees = new CSVReader(new InputStreamReader(inputStreamEmployees))) {

            if (inputStreamDepartments == null || inputStreamEmployees == null) {
                System.out.println("Archivos 'departments.csv' o 'employees_realistic.csv' no encontrados.");
                return;
            }

            Map<Integer, String[]> employeeMap = new HashMap<>();
            String[] employeeLine;
            boolean isFirstEmployeeLine = true;

            // Leer empleados, omitiendo la primera línea (encabezado)
            while ((employeeLine = readerEmployees.readNext()) != null) {
                if (isFirstEmployeeLine) {
                    isFirstEmployeeLine = false;
                    continue;  // Saltar la primera línea (encabezado)
                }

                try {
                    employeeMap.put(Integer.parseInt(employeeLine[0]), employeeLine);
                } catch (Exception e) {
                    System.out.println("Error procesando empleado: " + e.getMessage());
                }
            }

            String[] nextLine;
            boolean isFirstDepartmentLine = true;

            // Leer departamentos, omitiendo la primera línea (encabezado)
            while ((nextLine = readerDepartments.readNext()) != null) {
                if (isFirstDepartmentLine) {
                    isFirstDepartmentLine = false;
                    continue; // Saltar encabezado
                }

                try {
                    Department department = new Department();
                    int departmentId = Integer.parseInt(nextLine[0]);
                    department.setDepartmentName(nextLine[1]);

                    // Obtener el ID del gerente (empleado con posición Manager)
                    int managerId = Integer.parseInt(nextLine[2]);
                    String[] employeeData = employeeMap.get(managerId);

                    if (employeeData == null) {
                        System.out.println("Empleado con ID " + managerId + " no encontrado.");
                        continue;
                    }

                    if (!"Manager".equalsIgnoreCase(employeeData[6])) {
                        System.out.println("Empleado con ID " + managerId + " no es un gerente.");
                        continue;
                    }

                    // Crear el empleado gerente y asignarlo al departamento
                    EmployeeRealistic manager = new EmployeeRealistic();
                    manager.setEmployee_first_name(employeeData[1]);
                    manager.setEmployee_last_name(employeeData[2]);
                    manager.setHire_date(employeeData[4]);
                    manager.setSalary(Float.parseFloat(employeeData[5]));
                    manager.setPosition(employeeData[6]);

                    // Asignar el gerente al departamento
                    department.setManager(manager);

                    // Persistir el departamento y el gerente
                    session.persist(department);
                    session.persist(manager);

                } catch (Exception e) {
                    System.out.println("Error procesando departamento: " + e.getMessage());
                }
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }



    public static void processEmployeeRealistic(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/employees_realistic.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.out.println("Archivo 'employees_realistic.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true;
            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    EmployeeRealistic employee = new EmployeeRealistic();
                    employee.setEmployee_first_name(nextLine[1]);
                    employee.setEmployee_last_name(nextLine[2]);

                    int departmentId = Integer.parseInt(nextLine[3]);
                    Department department = session.find(Department.class, departmentId);

                    if (department != null) {
                        employee.setDepartment(department);
                    } else {
                        System.out.println("Departamento con ID " + departmentId + " no encontrado para el empleado Realistic.");
                        continue;
                    }

                    employee.setHire_date(nextLine[4]);
                    employee.setSalary(Float.valueOf(nextLine[5]));
                    employee.setPosition(nextLine[6]);
                    session.persist(employee);
                } catch (Exception e) {
                    System.out.println("Error procesando empleado: " + e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public static void processProjects(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/projects.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("Archivo 'projects.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true; // Indicador para omitir la primera línea (encabezado)

            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Saltamos la primera línea
                    continue;
                }

                try {
                    Project project = new Project();
                    project.setProject_name(nextLine[1]);

                    int departmentId = Integer.parseInt(nextLine[2]);
                    Department department = session.find(Department.class, departmentId);

                    if (department != null) {
                        project.setDepartment(department);
                    } else {
                        System.out.println("Departamento con ID " + departmentId + " no encontrado. Saltando línea.");
                        continue;
                    }

                    project.setBudget(Float.valueOf(nextLine[3]));
                    project.setProject_start_date(nextLine[4]);
                    project.setProject_end_date(nextLine[5]);

                    session.persist(project);
                } catch (Exception e) {
                    System.out.println("Error procesando proyecto: " + e.getMessage() + ". Saltando línea.");
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }


    public static void processEmployeeProjects(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/employee_projects.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("Archivo 'employee_projects.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true; // Indicador para omitir la primera línea (encabezado)

            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Saltamos la primera línea
                    continue;
                }

                try {
                    int employeeId = Integer.parseInt(nextLine[0]);
                    int projectId = Integer.parseInt(nextLine[1]);

                    EmployeeRealistic employee = session.find(EmployeeRealistic.class, employeeId);
                    Project project = session.find(Project.class, projectId);

                    if (employee == null) {
                        System.out.println("Empleado con ID " + employeeId + " no encontrado. Saltando línea.");
                        continue;
                    }
                    if (project == null) {
                        System.out.println("Proyecto con ID " + projectId + " no encontrado. Saltando línea.");
                        continue;
                    }

                    Employee employeeProjects = new Employee();
                    employeeProjects.setProject(project);

                    // Usar Float.parseFloat para manejar decimales como float
                    employeeProjects.setHours_worked(Float.parseFloat(nextLine[2]));

                    session.persist(employeeProjects);
                } catch (Exception e) {
                    System.out.println("Error procesando relación empleado-proyecto: " + e.getMessage() + ". Saltando línea.");
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }



    public static void processOrders(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/orders.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.out.println("Archivo 'orders.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true; // Indicador para detectar encabezados
            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Saltamos la primera fila (encabezados)
                }

                try {
                    Order order = new Order();

                    int customerId = Integer.parseInt(nextLine[1]);
                    Customer customer = session.find(Customer.class, customerId);
                    if (customer != null) {
                        order.setCustomer(customer);
                    } else {
                        System.out.println("Cliente con ID " + customerId + " no encontrado. Saltando registro...");
                        continue;
                    }

                    order.setOrder_date(nextLine[2]);

                    // Convertimos el campo `amount` a double
                    order.setAmount(Double.parseDouble(nextLine[3]));

                    session.persist(order);
                } catch (Exception e) {
                    System.out.println("Error procesando registro: " + String.join(",", nextLine));
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }



    public static void processOrderItems(Session session) {
        try (InputStream inputStream = CSVInserter.class.getClassLoader().getResourceAsStream("csvs/order_items.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("Archivo 'order_items.csv' no encontrado.");
                return;
            }

            String[] nextLine;
            boolean isFirstLine = true; // Indicador para omitir la primera línea (encabezado)

            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Saltamos la primera línea
                    continue;
                }

                try {
                    int orderId = Integer.parseInt(nextLine[1]);
                    Order order = session.find(Order.class, orderId);

                    if (order == null) {
                        System.out.println("Orden con ID " + orderId + " no encontrada. Saltando línea.");
                        continue;
                    }

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct_name(nextLine[2]);
                    orderItem.setQuantity(Integer.parseInt(nextLine[3]));
                    orderItem.setPrice(Double.parseDouble(nextLine[4]));

                    session.persist(orderItem);
                } catch (Exception e) {
                    System.out.println("Error procesando ítem de orden: " + e.getMessage() + ". Saltando línea.");
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el total de proyectos
    public static int getTotalProjects(Session session) {
        Query query = session.createQuery("SELECT COUNT(*) FROM Project");
        return ((Number) query.uniqueResult()).intValue();
    }

    // Método para obtener el presupuesto de un proyecto
    public static void getPresupuestoProyecto(Session session, int projectId) {
        // Obtener el salario total del proyecto
        float salarioProyecto = getTotalSalarioProyecto(session, projectId);
        if (salarioProyecto == 0) {
            System.out.println("El salario total del proyecto es 0, no se puede calcular la fracción del salario.");
            return; // No continuar si el salario es 0
        }else {
            System.out.println("El salario total del proyecto es: " + salarioProyecto);
        }

        // Obtener la fracción del salario en relación con el presupuesto
        float fraccion = getFraccionSalarioProyecto(session, projectId, salarioProyecto);

        // Obtener el presupuesto del proyecto
        Float budget = getProjectBudget(session, projectId);
        if (budget == null) {
            System.out.println("No se encontró el presupuesto para el proyecto con ID: " + projectId);
            return; // No continuar si no se encuentra el presupuesto
        }

        // Crear y persistir el objeto Presupuesto
        Presupuesto presupuesto = new Presupuesto();
        Project project = session.find(Project.class, projectId); // Obtener el proyecto

        presupuesto.setProject(project);
        presupuesto.setPresupuesto_total(budget);
        presupuesto.setFraccionSalario(fraccion);
        presupuesto.setTotalSalaryCost(salarioProyecto);

        // Persistir el presupuesto en la base de datos
        session.persist(presupuesto);
    }

    // Método para obtener el salario total de los empleados asociados a un proyecto
    public static Float getTotalSalarioProyecto(Session session, int projectId) {
        Query query = session.createQuery(
                "SELECT SUM(er.salary) " +
                        "FROM EmployeeRealistic er " +
                        "WHERE er.employee_id IN ( " +
                        "    SELECT e.employee_id " +
                        "    FROM Employee e " +
                        "    WHERE e.project.project_id = :projectId " +  // Relaciona con el proyecto
                        ")"
        );
        query.setParameter("projectId", projectId);
        // Comprobar si la consulta devuelve un valor nulo
        Object result = query.uniqueResult();
        if (result == null) {
            return 0f; // Si no hay empleados, el salario total es 0
        }
        return ((Number) result).floatValue();
    }

    // Método para obtener la fracción del presupuesto en relación con el salario total
    public static Float getFraccionSalarioProyecto(Session session, int projectId, float salarioProyecto) {
        Float budget = getProjectBudget(session, projectId);
        if (budget == null || salarioProyecto == 0) {
            return 0f; // Retornar 0 si no hay presupuesto o salario
        }
        return salarioProyecto / budget * 100; // Calcular la fracción en porcentaje
    }

    // Método para obtener el presupuesto del proyecto
    public static Float getProjectBudget(Session session, int projectId) {
        Query query = session.createQuery("SELECT budget FROM Project WHERE project_id = :projectId");
        query.setParameter("projectId", projectId);
        Object result = query.uniqueResult();
        if (result == null) {
            return null; // Retornar null si no se encuentra presupuesto
        }
        return ((Number) result).floatValue();
    }


// Métodos restantes refactorizados de manera similar...
}
