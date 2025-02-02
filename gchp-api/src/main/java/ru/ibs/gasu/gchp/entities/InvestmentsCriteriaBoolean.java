package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class InvestmentsCriteriaBoolean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "incestment_criteria_bool_ind_id")
    private InvestmentsCriteriaIndBoolean financeIndicator;
    private Boolean value;
    private String comment;

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

    public InvestmentsCriteriaIndBoolean getFinanceIndicator() {
        return financeIndicator;
    }

    public void setFinanceIndicator(InvestmentsCriteriaIndBoolean financeIndicator) {
        this.financeIndicator = financeIndicator;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        InvestmentsCriteriaBoolean that = (InvestmentsCriteriaBoolean) o;

        Long indId = financeIndicator != null ? financeIndicator.getId() : null;
        Long thatIndId = that.financeIndicator != null ? that.financeIndicator.getId() : null;
        return Objects.equals(id, that.id) &&
                Objects.equals(indId, thatIndId) &&
                Objects.equals(value, that.value) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financeIndicator, value, comment);
    }
}
