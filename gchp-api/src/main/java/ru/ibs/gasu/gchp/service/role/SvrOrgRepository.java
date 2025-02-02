package ru.ibs.gasu.gchp.service.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SvrOrgRepository extends JpaRepository<SvrOrg, String>, JpaSpecificationExecutor<SvrOrg> {
}
