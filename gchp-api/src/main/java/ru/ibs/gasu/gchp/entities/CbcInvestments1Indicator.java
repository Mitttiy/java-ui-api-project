package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;
import ru.ibs.gasu.gchp.entities.interfaces.InvestmentIndicator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class CbcInvestments1Indicator implements InvestmentIndicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investment_indicator_type_id")
    private InvestmentIndicatorType type;

    private Double plan;

    private Double fact;

    @ManyToOne
    @JoinColumn(name = "cbc_investments1")
    @XmlTransient
    private CbcInvestments1 investments;

    @OneToMany(mappedBy = "investmentIndicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbcInvestments1IndicatorYearValue> valuesByYears = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvestmentIndicatorType getType() {
        return type;
    }

    public void setType(InvestmentIndicatorType type) {
        this.type = type;
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
    public CbcInvestments1 getInvestments() {
        return investments;
    }

    public void setInvestments(CbcInvestments1 planInvestments) {
        this.investments = planInvestments;
    }

    public List<CbcInvestments1IndicatorYearValue> getValuesByYears() {
        return valuesByYears;
    }

    public void setValuesByYears(List<CbcInvestments1IndicatorYearValue> valuesByYears) {
        this.valuesByYears = valuesByYears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CbcInvestments1Indicator that = (CbcInvestments1Indicator) o;

        Long typeId = type != null ? type.getId() : null;
        Long thatTypeId = that.type != null ? that.type.getId() : null;
        return Objects.equals(typeId, thatTypeId) &&
                Objects.equals(id, that.id) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(valuesByYears, that.valuesByYears);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, plan, fact, valuesByYears);
    }
}
