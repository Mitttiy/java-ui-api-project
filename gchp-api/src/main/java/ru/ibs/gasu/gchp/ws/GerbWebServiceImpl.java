package ru.ibs.gasu.gchp.ws;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ibs.gasu.dictionaries.domain.DicRegions;
import ru.ibs.gasu.gchp.service.GerbService;

import javax.jws.WebService;

@WebService
public class GerbWebServiceImpl implements GerbWebService {

    @Autowired
    private GerbService gerbService;

    @Override
    public DicRegions findGerbById(String id) {
        DicRegions res = gerbService.findGerbById(id);
        return res;
    }
}
