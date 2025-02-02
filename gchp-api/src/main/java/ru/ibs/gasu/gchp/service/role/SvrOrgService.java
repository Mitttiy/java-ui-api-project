package ru.ibs.gasu.gchp.service.role;

import ru.ibs.gasu.gchp.domain.FilterSvrOrgs;

import java.util.List;

public interface SvrOrgService {
    List<SvrOrg> getSvrOrgsByOgrn(String ogrn);

    SvrOrg getSvrOrgById(String id);

    List<SvrOrg> getSvrOrgs(FilterSvrOrgs filter);
}
