package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")

public class Employee {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos genera automáticamente el valor (como un autoincremento)
    @Column(name = "employee_id") // Especifica el nombre de la columna
    private int employee_id;

    @Column(name = "hours_worked", nullable = false) // La columna 'hours_worked' es obligatoria
    private float hours_worked;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    // Getters y setters
    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public float getHours_worked() {
        return hours_worked;
    }

    public void setHours_worked(float hours_worked) {
        this.hours_worked = hours_worked;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
