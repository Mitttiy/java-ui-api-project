package ru.ibs.gasu.gchp.service;

import ru.ibs.gasu.dictionaries.domain.DicRegions;

public interface GerbService {
    DicRegions findGerbById(String id);
}
