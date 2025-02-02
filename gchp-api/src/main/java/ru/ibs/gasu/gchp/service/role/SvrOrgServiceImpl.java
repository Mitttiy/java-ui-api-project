package ru.ibs.gasu.gchp.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.domain.FilterSvrOrgs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.specifications.SvrOrgSpecification.filterByCriteria;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SvrOrgServiceImpl implements SvrOrgService {
    @Autowired
    private SvrOrgRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SvrOrg> getSvrOrgsByOgrn(String ogrn) {
        if(ogrn == null) {
            return new ArrayList<>();
        }

        FilterSvrOrgs filterSvrOrgs = new FilterSvrOrgs();
        filterSvrOrgs.setOgrn(ogrn);
        return getSvrOrgs(filterSvrOrgs);
    }

    @Override
    public SvrOrg getSvrOrgById(String id) {
        if(id == null) {
            return null;
        }

        FilterSvrOrgs filterSvrOrgs = new FilterSvrOrgs();
        filterSvrOrgs.setId(id);
        return getSvrOrgs(filterSvrOrgs).stream().findFirst().orElse(null);
    }

    @Override
    public List<SvrOrg> getSvrOrgs(FilterSvrOrgs filter) {
        if(filter.getId() == null && filter.getIds() == null && filter.getOgrn() == null && filter.getOktmo() == null && filter.getRegion() == null) {
            String query = "select distinct p.giPublicPartner" +
                    "        from Project p" +
                    "        WHERE p.obsolete = false";
            Query entityManagerQuery = entityManager.createQuery(query);
            List<Object> resultList = entityManagerQuery.getResultList();
            filter.setIds(resultList.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList()));
        }

        return repository.findAll(filterByCriteria(filter));
    }
}
