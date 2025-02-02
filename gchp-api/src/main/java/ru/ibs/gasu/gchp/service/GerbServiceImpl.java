package ru.ibs.gasu.gchp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.ibs.gasu.dictionaries.domain.DicRegions;
import ru.ibs.gasu.gchp.repositories.GerbRepository;
import ru.ibs.gasu.gchp.specifications.GerbSearchCriteria;

import java.util.List;

import static ru.ibs.gasu.gchp.specifications.GerbSearchCriteriaSpec.criteriaSearch;

@Service
public class GerbServiceImpl implements GerbService {

    @Autowired
    private GerbRepository gerbRepository;

    @Override
    public DicRegions findGerbById(String idRegion) {
        if (ObjectUtils.isEmpty(idRegion)) {
            throw new IllegalArgumentException("region is empty.");
        }

        GerbSearchCriteria criteria = new GerbSearchCriteria();
        criteria.setId(Integer.parseInt(idRegion));

       List<DicRegions> result = gerbRepository.findAll(criteriaSearch(criteria));

        return result.get(0);
    }
}
