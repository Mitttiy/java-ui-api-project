package ru.ibs.gasu.dictionaries.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.service.role.SvrOrg;
import ru.ibs.gasu.gchp.service.role.SvrOrgRepository;

@Service
public class DbDictConverter {

    @Autowired
    private SvrOrgRepository svrOrgRepository;

    public SvrOrg findSvrOrgBy(Object id) {
        if (id == null) return null;
        return svrOrgRepository.findById(id.toString()).orElse(null);
    }
}
