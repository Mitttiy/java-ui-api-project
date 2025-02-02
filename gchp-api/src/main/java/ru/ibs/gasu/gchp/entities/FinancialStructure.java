package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class FinancialStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "finance_indicator_type_id")
    private FinanceIndicator financeIndicator;
    private Double value;

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

    public FinanceIndicator getFinanceIndicator() {
        return financeIndicator;
    }

    public void setFinanceIndicator(FinanceIndicator financeIndicator) {
        this.financeIndicator = financeIndicator;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
        FinancialStructure that = (FinancialStructure) o;

        Long financeIndicatorId = financeIndicator == null ? null : financeIndicator.getId();
        Long thatFinanceIndicatorId = that.financeIndicator == null ? null : that.financeIndicator.getId();

        return Objects.equals(id, that.id) &&
                Objects.equals(financeIndicatorId, thatFinanceIndicatorId) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financeIndicator, value);
    }
}
