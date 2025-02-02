package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * DIC_GASU_OKTMO
 * Муниципальное образование
 */
@Getter
@Setter
public class Municipality {
    private String id;
    private String name;
    private String regionId;
    private String regionTypeId;
}
