package ru.ibs.gasu.gchp.ws;

import ru.ibs.gasu.gchp.domain.*;
import ru.ibs.gasu.gchp.service.role.SvrOrg;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import java.util.concurrent.ExecutionException;

@WebService(name = "GchpDocumentsWebService")
public interface GchpDocumentsWebService {

    @WebMethod
    ProjectDetailDTO saveGchpDocument(ProjectDetailDTO project);

    @WebMethod
    void softDeleteGchpDocumentById(Long id);

    @WebMethod
    List<ExportTask> runExcelFileGeneration(DocumentsFilterPaginateCriteria criteria);

    @WebMethod
    List<ExportTask> clearUserDoneTasks();

    @WebMethod
    String getFileByUuid(String uuid) throws InterruptedException, ExecutionException;

    @WebMethod
    String getOdsFileByUuid(String uuid);

    @WebMethod
    List<ExportTask> getAllTasksStatus();

    @WebMethod
    ProjectsResult paginateAndFilterDocuments(DocumentsFilterPaginateCriteria criteria);

    @WebMethod
    List<ProjectDetailsRevision> getRevisions(Long id) throws Exception;

    @WebMethod
    Integer updateDocumentStatus();

    @WebMethod
    RolePreferences getRolePreferences();

    @WebMethod
    List<SvrOrg> getSvrOrgs(FilterSvrOrgs filterSvrOrgs);
}
