package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class OperationalCostsIndicatorYearValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String year;
    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "operational_costs_indicator_id")
    @XmlTransient
    private OperationalCostsIndicator operationalCostsIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
    public OperationalCostsIndicator getOperationalCostsIndicator() {
        return operationalCostsIndicator;
    }

    public void setOperationalCostsIndicator(OperationalCostsIndicator operationalCostsIndicator) {
        this.operationalCostsIndicator = operationalCostsIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationalCostsIndicatorYearValue that = (OperationalCostsIndicatorYearValue) o;
        return year == that.year &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, plan, fact);
    }
}
