package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class BankGuaranteeByYears {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private Double plan; // not using here (i.e. always null)
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @XmlTransient
    private BankGuarantee payment;

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
    public BankGuarantee getPayment() {
        return payment;
    }

    public void setPayment(BankGuarantee payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankGuaranteeByYears that = (BankGuaranteeByYears) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(id, that.id) &&
                Objects.equals(fact, that.fact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, plan, fact);
    }
}
