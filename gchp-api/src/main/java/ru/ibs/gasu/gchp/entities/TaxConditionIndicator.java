package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class TaxConditionIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;

    @Column(name = "taxCondition_id")
    private Long taxConditionId;

    @Transient
    private TaxCondition taxCondition;

    private Double tax;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @XmlTransient
    private Project project;

    @OneToMany(mappedBy = "taxConditionIndicator", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaxConditionIndicatorYearValue> yearValues = new ArrayList<>();

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

    public Long getTaxConditionId() {
        return taxConditionId;
    }

    public void setTaxConditionId(Long taxConditionId) {
        this.taxConditionId = taxConditionId;
    }

    public TaxCondition getTaxCondition() {
        return taxCondition;
    }

    public void setTaxCondition(TaxCondition taxCondition) {
        this.taxCondition = taxCondition;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<TaxConditionIndicatorYearValue> getYearValues() {
        return yearValues;
    }

    public void setYearValues(List<TaxConditionIndicatorYearValue> yearValues) {
        this.yearValues = yearValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxConditionIndicator that = (TaxConditionIndicator) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(taxConditionId, that.taxConditionId) &&
                Objects.equals(tax, that.tax) &&
                Objects.equals(yearValues, that.yearValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taxCondition, tax, yearValues);
    }
}
