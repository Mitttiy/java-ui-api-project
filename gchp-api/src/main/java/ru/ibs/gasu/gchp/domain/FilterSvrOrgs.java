package ru.ibs.gasu.gchp.domain;

import lombok.Data;

import java.util.List;

@Data
public class FilterSvrOrgs {
    private String id;
    private List<String> ids;
    private String ogrn;
    private String oktmo;
    private String region;
}
