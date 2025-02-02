package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;
import ru.ibs.gasu.dictionaries.entities.MeasureType;
import ru.ibs.gasu.gchp.entities.interfaces.Investments;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@Audited
@Entity
public class PlanInvestments implements Investments {
    @Id
    private Long id;

    private Boolean includeNds;

    @ManyToOne
    @JoinColumn(name = "measure_type_id")
    private MeasureType measureType;

    private Date onDate;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @XmlTransient
    private Project project;

    @OneToMany(mappedBy = "investments", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanInvestmentIndicator> indicators = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIncludeNds() {
        return includeNds;
    }

    public void setIncludeNds(Boolean includeNds) {
        this.includeNds = includeNds;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<PlanInvestmentIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<PlanInvestmentIndicator> indicators) {
        this.indicators = indicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanInvestments that = (PlanInvestments) o;

        Long measureTypeId = measureType != null ? measureType.getId() : null;
        Long thatMeasureTypeId = that.measureType != null ? that.measureType.getId() : null;
        return Objects.equals(id, that.id) &&
                Objects.equals(includeNds, that.includeNds) &&
                Objects.equals(measureTypeId, thatMeasureTypeId) &&
                Objects.equals(onDate, that.onDate) &&
                Objects.equals(indicators, that.indicators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, includeNds, measureType, onDate, indicators);
    }
}
