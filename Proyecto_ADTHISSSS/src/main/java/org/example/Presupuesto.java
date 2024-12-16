package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Presupuesto")

public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presupuesto_id")
    private int presupuesto_id;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = true)
    private Project project;


    @Column(name = "presupuesto_total")
    private float presupuesto_total;

    @Column(name = "TotalSalaryCost")
    private float TotalSalaryCost;

    @Column(name = "FraccionSalario")
    private float FraccionSalario;


    public int getPresupuesto_id() {
        return presupuesto_id;
    }

    public void setPresupuesto_id(int presupuesto_id) {
        this.presupuesto_id = presupuesto_id;
    }

    public float getPresupuesto_total() {
        return presupuesto_total;
    }

    public void setPresupuesto_total(float presupuesto_total) {
        this.presupuesto_total = presupuesto_total;
    }

    public float getTotalSalaryCost() {
        return TotalSalaryCost;
    }

    public void setTotalSalaryCost(float TotalSalaryCost) {
        this.TotalSalaryCost = TotalSalaryCost;
    }

    public float getFraccionSalario() {
        return FraccionSalario;
    }

    public void setFraccionSalario(float FraccionSalario) {
        this.FraccionSalario = FraccionSalario;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


}
