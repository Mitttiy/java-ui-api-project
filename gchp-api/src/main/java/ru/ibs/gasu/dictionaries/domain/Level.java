package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DIC_GASU_GCHP_PROJ_TYPE_LVLS
 * Загрузка справочника "Уровень реализации" для фильтра
 */
@Getter
@Setter
public class Level {
    private String id;
    private String idParent;
    private String name;
    private Date periodStart;
    private Date periodEnd;
    private String dicGasuGchpProjectType;
    private Long sortOrder;
}
