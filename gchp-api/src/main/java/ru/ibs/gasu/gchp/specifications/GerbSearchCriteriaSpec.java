package ru.ibs.gasu.gchp.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import ru.ibs.gasu.dictionaries.domain.DicRegions;

public class GerbSearchCriteriaSpec {

    /**
     * Поиск по критерию
     *
     * @param criteria
     * @return
     */
    public static Specification<DicRegions> criteriaSearch(GerbSearchCriteria criteria) {
        return (root, query, builder) -> {
            Specification<DicRegions> specification = Specification.where(null);

            if (!ObjectUtils.isEmpty(criteria.getId())) {
                specification = specification.and(fineById(criteria.getId()));
            }

            return specification.toPredicate(root, query, builder);
        };
    }


    /**
     * поиск по id  региона
     *
     * @param id
     * @return
     */
    private static Specification<DicRegions> fineById(Integer id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);

    }



}
