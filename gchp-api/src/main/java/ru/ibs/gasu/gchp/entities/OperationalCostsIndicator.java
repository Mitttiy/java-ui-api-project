package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class OperationalCostsIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;
    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @XmlTransient
    private Project project;

    @ManyToOne
    @JoinColumn(name = "investment_indicator_type_id")
    private InvestmentIndicatorType type;

    @OneToMany(mappedBy = "operationalCostsIndicator", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationalCostsIndicatorYearValue> years = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPlan() {
        return plan;
    }

    public void setPlan(Double plan) {
        this.plan = plan;
    }

    public Double getFact() {
        return fact;
    }

    public void setFact(Double fact) {
        this.fact = fact;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public InvestmentIndicatorType getType() {
        return type;
    }

    public void setType(InvestmentIndicatorType type) {
        this.type = type;
    }

    public List<OperationalCostsIndicatorYearValue> getYears() {
        return years;
    }

    public void setYears(List<OperationalCostsIndicatorYearValue> years) {
        this.years = years;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationalCostsIndicator that = (OperationalCostsIndicator) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(years, that.years);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plan, fact, years);
    }
}
