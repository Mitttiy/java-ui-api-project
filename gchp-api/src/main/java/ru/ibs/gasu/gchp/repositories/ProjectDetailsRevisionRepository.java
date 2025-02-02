package ru.ibs.gasu.gchp.repositories;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;
import ru.ibs.gasu.gchp.entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProjectDetailsRevisionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List getRevisionsWithModifications(Long id) {
        return AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntityWithChanges(Project.class, false)
                .add(AuditEntity.id().eq(id))
                .add(AuditEntity.property("published").eq(true))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();
    }
}
