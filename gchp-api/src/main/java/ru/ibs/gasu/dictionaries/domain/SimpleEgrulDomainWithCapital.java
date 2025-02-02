package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEgrulDomainWithCapital extends SimpleEgrulDomain {
    //Сведения об уставном капитале (складочном капитале, уставном фонде, паевых взносах).
    private String capitalInfo;
    private String capitalValue;
    private String capitalPercent;

}
