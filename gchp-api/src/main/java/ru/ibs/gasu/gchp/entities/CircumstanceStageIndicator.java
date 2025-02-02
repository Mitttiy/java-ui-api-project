package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class CircumstanceStageIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @XmlTransient
    private Project project;

    @ManyToOne
    @JoinColumn(name = "circumstance_stage_type_id")
    private CircumstanceStageType circumstanceType;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CircumstanceIndicator> circumstances = new ArrayList<>();

    private Double sum;

    private String years;

    @XmlTransient
    private String parentField;

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

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public CircumstanceStageType getCircumstanceType() {
        return circumstanceType;
    }

    public void setCircumstanceType(CircumstanceStageType circumstanceType) {
        this.circumstanceType = circumstanceType;
    }

    public List<CircumstanceIndicator> getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(List<CircumstanceIndicator> circumstances) {
        this.circumstances = circumstances;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    @XmlTransient
    public String getParentField() {
        return parentField;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircumstanceStageIndicator that = (CircumstanceStageIndicator) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sum, that.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sum);
    }

}
