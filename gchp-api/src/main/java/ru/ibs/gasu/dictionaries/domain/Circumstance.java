package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Circumstance {
    private Long id;
    private String name;
    private Long stageId;
}
