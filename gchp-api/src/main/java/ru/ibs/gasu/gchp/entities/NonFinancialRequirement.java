package ru.ibs.gasu.gchp.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;

@Audited
@Entity
@Getter
@Setter
public class NonFinancialRequirement {
    @Id
    private Long id;
    private String name;


}
