package ru.ibs.gasu.dictionaries.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ibs.gasu.dictionaries.entities.MeasureType;

public interface MeasureTypeRepo extends JpaRepository<MeasureType, Long> {
}
