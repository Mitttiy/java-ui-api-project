package ru.ibs.gasu.dictionaries.domain;

import lombok.Data;

@Data
public class FilterByInitFormAndSphere {
    private Long formId;
    private Long sphereId;
    private Long id;
}
