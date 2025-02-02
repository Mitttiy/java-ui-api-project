package ru.ibs.gasu.gchp.ws;

import ru.ibs.gasu.dictionaries.domain.DicRegions;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "GerbWebService")
public interface GerbWebService {
    @WebMethod
    DicRegions findGerbById(String id);
}
