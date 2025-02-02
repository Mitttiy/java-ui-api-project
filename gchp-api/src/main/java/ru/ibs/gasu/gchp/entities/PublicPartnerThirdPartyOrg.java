package ru.ibs.gasu.gchp.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Entity
public class PublicPartnerThirdPartyOrg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String name;

    private String inn;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PUBLIC_PARTNER_THIRD_PARTY_ORG_WORKTYPE",
            joinColumns = @JoinColumn(name = "ORG_ID", referencedColumnName = "id")
    )
    @Column(name = "WORKTYPE_ID")
    private List<Long> workTypesIds = new ArrayList<>();

    @Transient
    private List<WorkType> workTypes = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Long> getWorkTypesIds() {
        return workTypesIds;
    }

    public void setWorkTypesIds(List<Long> workTypesIds) {
        this.workTypesIds = workTypesIds;
    }

    public List<WorkType> getWorkTypes() {
        return workTypes;
    }

    public void setWorkTypes(List<WorkType> workTypes) {
        this.workTypes = workTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicPartnerThirdPartyOrg that = (PublicPartnerThirdPartyOrg) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(inn, that.inn) &&
                Objects.equals(workTypesIds, that.workTypesIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, inn, workTypesIds);
    }
}
