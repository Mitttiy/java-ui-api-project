package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class RevenueIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;
    private Double plan;
    private Double fact;
    private String um;

    @ManyToOne
    @JoinColumn(name = "revenueService_id")
    @XmlTransient
    private RevenueServiceIndicator revenueService;

    @OneToMany(mappedBy = "revenueIndicator", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RevenueIndicatorYearValue> yearValues = new ArrayList<>();

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
    public RevenueServiceIndicator getRevenueService() {
        return revenueService;
    }

    public void setRevenueService(RevenueServiceIndicator revenueService) {
        this.revenueService = revenueService;
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

    public List<RevenueIndicatorYearValue> getYearValues() {
        return yearValues;
    }

    public void setYearValues(List<RevenueIndicatorYearValue> yearValues) {
        this.yearValues = yearValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevenueIndicator that = (RevenueIndicator) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(um, that.um) &&
                Objects.equals(yearValues, that.yearValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plan, fact, um, yearValues);
    }
}
