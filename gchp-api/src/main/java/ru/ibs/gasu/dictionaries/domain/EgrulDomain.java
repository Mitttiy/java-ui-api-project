package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EgrulDomain extends SimpleEgrulDomainWithCapital {
    private List<String> fioTopManagers = new ArrayList<>();
    //Сведения об учредителях (участниках) ЮЛ (ИП);
    private List<SimpleEgrulDomainWithCapital> founders = new ArrayList<>();


}
