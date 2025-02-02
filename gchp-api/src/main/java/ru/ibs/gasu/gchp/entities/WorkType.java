package ru.ibs.gasu.gchp.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
public class WorkType {
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkType workType = (WorkType) o;
        return Objects.equals(id, workType.id) &&
                Objects.equals(name, workType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
