package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DIC_GCHP_RENT_OBJECT
 */
@Getter
@Setter
public class RentObject {
    private Long id;
    private String name;
}
