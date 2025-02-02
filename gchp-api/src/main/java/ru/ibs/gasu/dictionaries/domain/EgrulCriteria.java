package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;
import ru.ibs.gasu.gchp.domain.PagingSearchCriteria;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class EgrulCriteria extends PagingSearchCriteria {
    private Long id;
    private String fullName;
    private String shortName;
    private String inn;
    private String ogrn;
    //not egrul
    private boolean isEgrip;

    public final static List<String> FIELD_NAMES = Arrays.asList("FULL_NAME", "SHORT_NAME", "INN", "INN_FL", "OGRN", "OGRNIP");
}
