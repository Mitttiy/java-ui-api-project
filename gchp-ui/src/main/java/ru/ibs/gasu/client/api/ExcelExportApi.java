package ru.ibs.gasu.client.api;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.common.models.ExportTask;
import ru.ibs.gasu.common.soap.generated.gchpdocs.DocumentsFilterPaginateCriteria;

import javax.ws.rs.*;
import java.util.List;

@Path("rest/gchp")
public interface ExcelExportApi extends RestService {

    ExcelExportApi EXCEL_EXPORT_API = GWT.create(ExcelExportApi.class);

    @GET
    @Path("/export/tasksStatus")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllTasksStatus(MethodCallback<List<ExportTask>> callback);

    @POST
    @Path("/export/runExcelFileGeneration")
    @Consumes("application/json")
    @Produces("application/json")
    void runExcelFileGeneration(DocumentsFilterPaginateCriteria criteria, MethodCallback<List<ExportTask>> callback);

    @GET
    @Path("/export/clearUserDoneTasks")
    @Consumes("application/json")
    @Produces("application/json")
    void clearUserDoneTasks(MethodCallback<List<ExportTask>> callback);
}
