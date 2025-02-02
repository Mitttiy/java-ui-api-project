package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Audited
@Entity
public class CreationEnsureMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ensureMethodId;

    @Transient
    private String ensureMethodName;

    private String riskType;
    private Long submissionDate;
    @Column(length = 1000)
    private String term;
    private Double value;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @XmlTransient
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnsureMethodId() {
        return ensureMethodId;
    }

    public void setEnsureMethodId(Long ensureMethodId) {
        this.ensureMethodId = ensureMethodId;
    }

    public String getEnsureMethodName() {
        return ensureMethodName;
    }

    public void setEnsureMethodName(String ensureMethodName) {
        this.ensureMethodName = ensureMethodName;
    }


    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public Long getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Long submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreationEnsureMethod that = (CreationEnsureMethod) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ensureMethodId, that.ensureMethodId) &&
                Objects.equals(term, that.term) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ensureMethodId, term, value);
    }
}
