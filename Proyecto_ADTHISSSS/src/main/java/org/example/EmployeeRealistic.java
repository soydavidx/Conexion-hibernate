package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "EmployeeRealistic")
public class EmployeeRealistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employee_id;

    @Column(name = "employee_first_name")
    private String employee_first_name;

    @Column(name = "employee_last_name")
    private String employee_last_name;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)  // Relación Many-to-One con Department
    private Department department;

    @Column(name = "hire_date")
    private String hire_date;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "position")
    private String position;  // Puede ser "Manager" o algún otro cargo

    // Getters y Setters
    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_first_name() {
        return employee_first_name;
    }

    public void setEmployee_first_name(String employee_first_name) {
        this.employee_first_name = employee_first_name;
    }

    public String getEmployee_last_name() {
        return employee_last_name;
    }

    public void setEmployee_last_name(String employee_last_name) {
        this.employee_last_name = employee_last_name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
