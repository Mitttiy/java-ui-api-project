package ru.ibs.gasu.gchp.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class FinanceIndicator {
    @Id
    private Long id;
    private String serialNum;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinanceIndicator that = (FinanceIndicator) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
