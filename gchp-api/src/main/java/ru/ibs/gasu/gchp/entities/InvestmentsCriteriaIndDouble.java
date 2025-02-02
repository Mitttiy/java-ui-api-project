package ru.ibs.gasu.gchp.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class InvestmentsCriteriaIndDouble {
    @Id
    private Long id;
    private String name;
}
