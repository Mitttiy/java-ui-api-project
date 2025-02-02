package ru.ibs.gasu.client.api;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.UmModel;
import ru.ibs.gasu.common.models.UmSearchCriteria;

import javax.ws.rs.*;
import java.util.List;

@Path("rest/gchp")
public interface WindowDictsApi extends RestService {

    WindowDictsApi WINDOW_DICTS_API = GWT.create(WindowDictsApi.class);

    @POST
    @Path("/dictionary/um")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredUnitsOfMeasurement(UmSearchCriteria criteria, MethodCallback<PagingLoadResultBean<UmModel>> callback);


    @GET
    @Path("/dictionary/ensuremethods")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllEnsureMethods(MethodCallback<List<SimpleIdNameModel>> callback);


    @GET
    @Path("/dictionary/publicservicetypes")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPublicServiceTypes(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/privateservicetypes")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPrivateServiceTypes(MethodCallback<List<SimpleIdNameModel>> callback);

}
