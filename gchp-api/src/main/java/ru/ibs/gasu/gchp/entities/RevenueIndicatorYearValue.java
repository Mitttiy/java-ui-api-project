package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class RevenueIndicatorYearValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;
    private Double plan;
    private Double fact;
    private String um;

    @ManyToOne
    @JoinColumn(name = "revenueIndicator_id")
    @XmlTransient
    private RevenueIndicator revenueIndicator;

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

    @XmlTransient
    public RevenueIndicator getRevenueIndicator() {
        return revenueIndicator;
    }

    public void setRevenueIndicator(RevenueIndicator revenueIndicator) {
        this.revenueIndicator = revenueIndicator;
    }

    public Double getFact() {
        return fact;
    }

    public void setFact(Double fact) {
        this.fact = fact;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevenueIndicatorYearValue that = (RevenueIndicatorYearValue) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(um, that.um);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plan, fact, um);
    }
}
