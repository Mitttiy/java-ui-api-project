package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class Sanction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "SANCTION_SANCTIONTYPE",
            joinColumns = @JoinColumn(name = "SANCTION_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "SANCTIONTYPE_ID", referencedColumnName = "id"))
    private List<SanctionType> type = new ArrayList<>();

    private Date date;
    private Double sum;
    private String cause;

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

    public List<SanctionType> getType() {
        return type;
    }

    public void setType(List<SanctionType> type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
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
        Sanction sanction = (Sanction) o;
        return Objects.equals(id, sanction.id) &&
                Objects.equals(type, sanction.type) &&
                Objects.equals(date, sanction.date) &&
                Objects.equals(sum, sanction.sum) &&
                Objects.equals(cause, sanction.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, date, sum, cause);
    }
}
