package ru.ibs.gasu.gchp.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import ru.ibs.gasu.gchp.entities.interfaces.InvestmentType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Audited
@Entity
@Getter
@Setter
public class InvestmentIndicatorType implements InvestmentType {
    @Id
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentIndicatorType that = (InvestmentIndicatorType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
