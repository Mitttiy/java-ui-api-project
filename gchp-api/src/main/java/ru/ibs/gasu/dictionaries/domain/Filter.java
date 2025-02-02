package ru.ibs.gasu.dictionaries.domain;

import lombok.Data;

@Data
public class Filter {
    private Long id;
    private Long parentId;
    private String name;
}
