package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class InvestmentInObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;
    private Double plan;
    private Double fact;

    @ManyToOne
    @JoinColumn(name = "main_indicator_id")
    @XmlTransient
    private InvestmentInObjectMainIndicator mainObjectIndicator;

    @OneToMany(mappedBy = "object", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentInObjectIndicator> indicators = new ArrayList<>();

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
    public InvestmentInObjectMainIndicator getMainObjectIndicator() {
        return mainObjectIndicator;
    }

    public void setMainObjectIndicator(InvestmentInObjectMainIndicator mainObjectIndicator) {
        this.mainObjectIndicator = mainObjectIndicator;
    }

    public List<InvestmentInObjectIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<InvestmentInObjectIndicator> yearValues) {
        this.indicators = yearValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentInObject that = (InvestmentInObject) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(plan, that.plan) &&
                Objects.equals(fact, that.fact) &&
                Objects.equals(indicators, that.indicators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, plan, fact, indicators);
    }
}
