package ru.ibs.gasu.gchp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.ibs.gasu.dictionaries.domain.DicRegions;

public interface GerbRepository  extends JpaRepository<DicRegions, Long>, JpaSpecificationExecutor<DicRegions>  {
}

