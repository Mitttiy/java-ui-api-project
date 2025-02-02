package ru.ibs.gasu.gchp.entities;


import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Objects;

@Audited
@Entity
public class JudicialActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String disputeSubject;
    private String judicialDecision;
    private String kadArbitrRuUrl;
    private String comment;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDisputeSubject() {
        return disputeSubject;
    }

    public void setDisputeSubject(String disputeSubject) {
        this.disputeSubject = disputeSubject;
    }

    public String getJudicialDecision() {
        return judicialDecision;
    }

    public void setJudicialDecision(String judicialDecision) {
        this.judicialDecision = judicialDecision;
    }

    public String getKadArbitrRuUrl() {
        return kadArbitrRuUrl;
    }

    public void setKadArbitrRuUrl(String kadArbitrRuUrl) {
        this.kadArbitrRuUrl = kadArbitrRuUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        JudicialActivity that = (JudicialActivity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(date, that.date) &&
                Objects.equals(disputeSubject, that.disputeSubject) &&
                Objects.equals(judicialDecision, that.judicialDecision) &&
                Objects.equals(kadArbitrRuUrl, that.kadArbitrRuUrl) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, disputeSubject, judicialDecision, kadArbitrRuUrl, comment);
    }
}
