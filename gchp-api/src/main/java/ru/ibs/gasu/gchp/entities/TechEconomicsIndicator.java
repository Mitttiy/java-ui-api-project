package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class TechEconomicsIndicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;

    @Column(name = "um_id")
    private Long umId;

    @Transient
    private UnitOfMeasure um;

    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "object_id")
    @XmlTransient
    private TechEconomicsObjectIndicator object;

    @OneToMany(mappedBy = "economicsIndicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechEconomicsIndicatorYearValue> yearValues = new ArrayList<>();

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

    public Long getUmId() {
        return umId;
    }

    public void setUmId(Long umId) {
        this.umId = umId;
    }

    public UnitOfMeasure getUm() {
        return um;
    }

    public void setUm(UnitOfMeasure um) {
        this.um = um;
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
    public TechEconomicsObjectIndicator getObject() {
        return object;
    }

    public void setObject(TechEconomicsObjectIndicator object) {
        this.object = object;
    }

    public List<TechEconomicsIndicatorYearValue> getYearValues() {
        return yearValues;
    }

    public void setYearValues(List<TechEconomicsIndicatorYearValue> yearValues) {
        this.yearValues = yearValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechEconomicsIndicator that = (TechEconomicsIndicator) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(umId, that.umId) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(yearValues, that.yearValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, umId, plan, fact, yearValues);
    }
}
