package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EgrulDomainResult {
    private List<SimpleEgrulDomain> data = new ArrayList<>();
    private Integer totalLength = 0;
    private Integer offset;

}
