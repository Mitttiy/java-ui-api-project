package ru.ibs.gasu.gchp.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.ibs.gasu.gchp.domain.FilterSvrOrgs;
import ru.ibs.gasu.gchp.service.role.SvrOrg;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.dictionaries.service.DictionaryDataService.RF_ID;

public class SvrOrgSpecification {
    public static Specification<SvrOrg> filterByCriteria(FilterSvrOrgs criteria) {
        return (Specification<SvrOrg>) (root, criteriaQuery, criteriaBuilder) -> {
            Specification<SvrOrg> specification = filterBase();

            if (!isEmpty(criteria.getOktmo())) {
                specification = specification.and(filterByOktmo(criteria.getOktmo()));
            }

            if (!isEmpty(criteria.getOgrn())) {
                specification = specification.and(filterByOgrn(criteria.getOgrn()));
            }

            if (!isEmpty(criteria.getId())) {
                specification = specification.and(filterById(criteria.getId()));
            }

            if (!isEmpty(criteria.getIds())) {
                specification = specification.and(filterByIds(criteria.getIds()));
            }

            if (!isEmpty(criteria.getRegion())) {
                specification = specification.and(filterByRegion(criteria.getRegion()));
            }

            return specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        };
    }

    private static Specification<SvrOrg> filterBase() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("rn"), 1));
    }

    private static Specification<SvrOrg> filterByOktmo(String code) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("oktmoCode"), code);
    }

    private static Specification<SvrOrg> filterByOgrn(String ogrn) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("ogrn"), ogrn);
    }

    private static Specification<SvrOrg> filterById(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    private static Specification<SvrOrg> filterByIds(List<String> id) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("id").in(id);
    }

    private static Specification<SvrOrg> filterByRegion(String sp1) {
        if (sp1.equals(RF_ID)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("sp1"), sp1),
                    criteriaBuilder.equal(root.get("sp1"), ""),
                    criteriaBuilder.isNull(root.get("sp1")));
        }

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("sp1"), sp1);
    }
}
