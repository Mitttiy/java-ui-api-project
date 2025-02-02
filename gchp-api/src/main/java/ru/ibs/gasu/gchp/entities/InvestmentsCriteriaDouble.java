package ru.ibs.gasu.gchp.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class InvestmentsCriteriaDouble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "incestment_criteria_double_ind_id")
    private InvestmentsCriteriaIndDouble financeIndicator;
    private Double value;
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

    public InvestmentsCriteriaIndDouble getFinanceIndicator() {
        return financeIndicator;
    }

    public void setFinanceIndicator(InvestmentsCriteriaIndDouble financeIndicator) {
        this.financeIndicator = financeIndicator;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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
}
