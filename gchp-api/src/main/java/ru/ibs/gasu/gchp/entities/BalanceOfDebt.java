package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class BalanceOfDebt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer period;
    private Double factValue;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @XmlTransient
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getFactValue() {
        return factValue;
    }

    public void setFactValue(Double fact) {
        this.factValue = fact;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceOfDebt that = (BalanceOfDebt) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(period, that.period) &&
                Objects.equals(factValue, that.factValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, factValue);
    }
}
