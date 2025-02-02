package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class TaxConditionIndicatorYearValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private Double tax;

    @ManyToOne
    @JoinColumn(name = "tax_condition_indicator_id")
    @XmlTransient
    private TaxConditionIndicator taxConditionIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @XmlTransient
    public TaxConditionIndicator getTaxConditionIndicator() {
        return taxConditionIndicator;
    }

    public void setTaxConditionIndicator(TaxConditionIndicator taxConditionIndicator) {
        this.taxConditionIndicator = taxConditionIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxConditionIndicatorYearValue that = (TaxConditionIndicatorYearValue) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(year, that.year) &&
                Objects.equals(tax, that.tax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, tax);
    }
}
