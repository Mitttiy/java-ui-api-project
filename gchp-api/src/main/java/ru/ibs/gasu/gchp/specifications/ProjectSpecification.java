package ru.ibs.gasu.gchp.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.ibs.gasu.gchp.domain.DocumentsFilterPaginateCriteria;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.dictionaries.service.DictionaryDataService.RF_ID;

public class ProjectSpecification {
    public static <T> Specification<T> filterByCriteria(DocumentsFilterPaginateCriteria criteria) {
        return (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            Specification<T> specification = Specification.where(filterByNotObsolete());

            if (!isEmpty(criteria.getFormId())) {
                specification = specification.and(filterByRealizationForm(criteria.getFormId()));
            }

            if (!isEmpty(criteria.getDocId())) {
                specification = specification.and(filterByDocId(criteria.getDocId()));
            }

            if (!isEmpty(criteria.getDocName())) {
                specification = specification.and(filterByDocName(criteria.getDocName()));
            }

            if (!isEmpty(criteria.getProjectStatusIds())) {
                specification = specification.and(filterByProjectStatus(criteria.getProjectStatusIds()));
            }

            if (!isEmpty(criteria.getRealizationStatusIds())) {
                specification = specification.and(filterByRealizationStatus(criteria.getRealizationStatusIds()));
            }

            if (!isEmpty(criteria.getLevelId())) {
                specification = specification.and(filterByRealizationLevel(criteria.getLevelId()));
            }

            if (!isEmpty(criteria.getSphereId())) {
                specification = specification.and(filterByRealizationSphere(criteria.getSphereId()));
            }

            if (!isEmpty(criteria.getDicGasuSp1Id())) {
                specification = specification.and(filterByRFRegion(criteria.getDicGasuSp1Id()));
            }

            if (!isEmpty(criteria.getSectorId())) {
                specification = specification.and(filterRealizationSector(criteria.getSectorId()));
            }

            if (!isEmpty(criteria.getMunicipalId())) {
                specification = specification.and(filterByMunicipal(criteria.getMunicipalId()));
            }

            if (!isEmpty(criteria.getPublicPartnerId())) {
                specification = specification.and(filterByPublicPartner(criteria.getPublicPartnerId()));
            }

            return specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        };
    }

    public static <T> Specification<T> filterByRealizationForm(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giRealizationForm"), id);
    }

    public static <T> Specification<T> filterByNotObsolete() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("obsolete"), false);
    }

    public static <T> Specification<T> filterByDocId(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static <T> Specification<T> filterByDocName(String name) {
//    return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("giName"), name);
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("giName")), "%" + name.toUpperCase() + "%");
    }

    public static <T> Specification<T> filterByProjectStatus(List<String> projectStatusIds) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("giProjectStatus").in(projectStatusIds);
    }

    public static <T> Specification<T> filterByRealizationStatus(List<String> realizationStatusIds) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("giRealizationStatus").in(realizationStatusIds);
    }

    public static <T> Specification<T> filterByRealizationLevel(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giRealizationLevel"), id);
    }

    public static <T> Specification<T> filterByRealizationSphere(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giRealizationSphere"), id);
    }

    public static <T> Specification<T> filterByRFRegion(String id) {
        if (id.equals(RF_ID)) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("giRegion"), id),
                    criteriaBuilder.isNull(root.get("giRegion")));
        }

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giRegion"), id);
    }

    public static <T> Specification<T> filterRealizationSector(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giRealizationSector"), id);
    }

    public static <T> Specification<T> filterByMunicipal(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("giMunicipality"), id);
    }

    public static <T> Specification<T> filterByPublicPartner(String ogrn) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("giPublicPartnerOgrn").in(ogrn);
    }

}
