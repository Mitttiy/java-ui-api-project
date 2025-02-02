package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class CbcInvestments1IndicatorYearValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int year;
    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "investment_indicator_id")
    private CbcInvestments1Indicator investmentIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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
    public CbcInvestments1Indicator getInvestmentIndicator() {
        return investmentIndicator;
    }

    public void setInvestmentIndicator(CbcInvestments1Indicator investmentIndicator) {
        this.investmentIndicator = investmentIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbcInvestments1IndicatorYearValue that = (CbcInvestments1IndicatorYearValue) o;
        return year == that.year &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, plan, fact);
    }
}
