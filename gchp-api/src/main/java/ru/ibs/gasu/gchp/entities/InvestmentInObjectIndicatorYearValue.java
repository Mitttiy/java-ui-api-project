package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;
import ru.ibs.gasu.gchp.entities.interfaces.InvestmentIndicator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class InvestmentInObjectIndicatorYearValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long year;
    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "object_indicator_id")
    @XmlTransient
    private InvestmentInObjectIndicator objectIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
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
    public InvestmentInObjectIndicator getObjectIndicator() {
        return objectIndicator;
    }

    public void setObjectIndicator(InvestmentInObjectIndicator objectIndicator) {
        this.objectIndicator = objectIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentInObjectIndicatorYearValue that = (InvestmentInObjectIndicatorYearValue) o;
        return year == that.year &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, plan, fact);
    }
}
