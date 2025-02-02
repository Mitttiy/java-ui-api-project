package ru.ibs.gasu.client.api;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.common.models.UmSearchCriteria;
import ru.ibs.gasu.common.models.*;
import ru.ibs.gasu.common.soap.generated.gchpdicts.*;
import ru.ibs.gasu.common.soap.generated.gchpdocs.*;

import javax.ws.rs.*;
import java.util.List;

@Path("rest/gchp")
public interface GchpApi extends RestService {
    GchpApi GCHP_API = GWT.create(GchpApi.class);

    String FILE_UPLOAD_PATH = "api/files/uploadlist";
    String FILE_DOWNLOAD_PATH = "api/files/download";

    default String getFileDownloadPath(Long fileVersionId) {
        return FILE_DOWNLOAD_PATH + "/" + fileVersionId;
    }

    @GET
    @Path("/role/preferences")
    void getLodRolePreferences(MethodCallback<RolePreferences> callback);

    @GET
    @Path("/project/{id}/revisions")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllRevisions(@PathParam("id") Long id, MethodCallback<List<ProjectDetailsRevision>> callback);

    @POST
    @Path("/svrorg")
    @Consumes("application/json")
    @Produces("application/json")
    void getSvrOrgs(FilterSvrOrgs filter, MethodCallback<List<SvrOrg>> callback);

    @POST
    @Path("/dictionary/realizationform")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredRealizationForms(RealisationFormFilters filters, MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/initiationmethod")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllInitiationMethods(MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/initiationmethodfilter")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredInitiationMethods(Filter filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/realizationlevelfilter")
    @Consumes("application/json") // todo пока не закомментил RestyGWT выдавал ошибку, везде убрать?
    @Produces("application/json")
    void getFilteredRealizationLevels(Filter filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/rfregion")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllRFRegions(RfRegionFilters filters, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/municipality")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredMunicipalities(RfMunicipalityFilters filters, MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/publicpartner")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredPublicPartners(MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/realizationspherefilter")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredRealizationSpheres(Filter filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/realizationsector")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredRealizationSectors(FilterByInitFormAndSphere filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/objecttypefilter")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredObjectKinds(ObjectKindFilter filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/agreementsubject")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllAgreementSubjects(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/projectstatus")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllProjectStatuses(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/realizationstatus")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllRealizationStatuses(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/agreementgrounds")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllAgreementGrounds(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/competitionresults")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllCompetitionResults(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/competitionresultssign")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllCompetitionResultsSign(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/priceorder")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPriceOrders(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/contractpriceoffers")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllContractPriceOffers(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/winnercontractpriceoffers")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllWinnerContractPriceOffers(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/privatepartnercostrecoverymethods")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPrivatePartnerCostRecoveryMethods(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/ensuremethods")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllEnsureMethods(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/govsupport")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllGovSupports(MethodCallback<List<SimpleIdNameModel>> callback);


    @GET
    @Path("/dictionary/irsource")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllIRSources(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/ppresultsofplacing")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPpResultsOfPlacing(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/irlevel")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllIRLevels(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/paymentmethods")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllPaymentMethods(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/costrecoverymethods")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllCostRecoveryMethods(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/terminationcauses")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllTerminationCauses(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/opf")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllOpf(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/agreementset")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllAgreementsSet(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/methodofexecuteobligation")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllMethodOfExecuteObligation(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/othergovsupports")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllOtherGovSupports(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/terminationaftermaths")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllTerminationAftermaths(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/changesreason")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllChangesReasons(MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/um")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredUnitsOfMeasurement(UmSearchCriteria criteria, MethodCallback<PagingLoadResultBean<UmModel>> callback);

    @GET
    @Path("/dictionary/agreementssets")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllAgreementsSets(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/rentobject")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllRentObject(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/competitioncriterion")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllCompetitionCriterion(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/nofinrequirments")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllNoFinRequirments(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/finrequirments")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllFinRequirments(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/businessmodetypes")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllBusinessModeTypes(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/compofcompensation")
    @Consumes("application/json")
    @Produces("application/json")
    void getAllTmCompositionOfCompensationGrantorFault(MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/agreementgroundsfilter")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredAgreementGrounds(FilterByInitFormAndMethod filter, MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/project/save")
    @Consumes("application/json")
    @Produces("application/json")
    void saveProject(Project project, MethodCallback<Project> callback);

    @POST
    @Path("/project/delete")
    @Consumes("application/json")
    @Produces("application/json")
    void deleteProject(Long id, MethodCallback<Void> callback);

    @POST
    @Path("/project/paginateandfilterdocuments")
    @Consumes("application/json")
    @Produces("application/json")
    void paginateAndFilterDocuments(DocumentsFilterPaginateCriteria project, MethodCallback<PagingLoadResultBean<Project>> callback);

    @POST
    @Path("/dictionary/egrul")
    @Consumes("application/json")
    @Produces("application/json")
    void getSimpleEgrulDomains(EgrulCriteria criteria, MethodCallback<PagingLoadResultBean<SimpleEgrulDomain>> callback);

    @GET
    @Path("/dictionary/egrul/{inn}")
    @Consumes("application/json")
    @Produces("application/json")
    void getEgrulDomain(@PathParam("inn") String inn, MethodCallback<EgrulDomain> callback);

    @GET
    @Path("/dictionary/taxconditions")
    @Consumes("application/json")
    @Produces("application/json")
    void getTaxCondition(MethodCallback<List<TaxConditionModel>> callback);

    @GET
    @Path("/dictionary/minimumguarantincometype")
    @Consumes("application/json")
    @Produces("application/json")
    void getMinimumGuarantIncomeTypes(MethodCallback<List<SimpleIdNameModel>> callback);

    @GET
    @Path("/dictionary/enablingpayouts")
    @Consumes("application/json")
    @Produces("application/json")
    void getEnablingPayouts(MethodCallback<List<SimpleIdNameModel>> callback);

    @POST
    @Path("/dictionary/circumstances")
    @Consumes("application/json")
    @Produces("application/json")
    void getFilteredCircumstances(Filter filter, MethodCallback<List<SimpleIdNameModel>> callback);


}
