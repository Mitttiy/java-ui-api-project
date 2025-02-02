package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEgrulDomain {
    private Long id;
    private String fullName;
    private String shortName;
    private String inn;
    private String ogrn;
    private String roAddress;
    private String postalCode;
    private String regionType;
    private String regionName;
    private String streetType;
    private String streetName;
    private String building;
    private String office;

}
