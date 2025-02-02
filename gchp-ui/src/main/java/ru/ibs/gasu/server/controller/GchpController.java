package ru.ibs.gasu.server.controller;

import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.TaxConditionModel;
import ru.ibs.gasu.common.models.UmModel;
import ru.ibs.gasu.common.models.UmSearchCriteria;
import ru.ibs.gasu.common.soap.generated.gchpdicts.*;
import ru.ibs.gasu.common.soap.generated.gchpdocs.*;

import java.lang.Exception;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

//import ru.ibs.gasu.common.soap.generated.gchpdocs.PagingLoadResultBean;

//import ru.ibs.gasu.common.soap.generated.gchpdocs.PagingLoadResultBean;

@Controller
@RequestMapping("gchpclient/rest/gchp")
@Slf4j
public class GchpController {

    @Autowired
    private Mapper dozerMapper;

    @Autowired
    private GchpDictionariesWebService gchpDictionariesWebService;

    @Autowired
    private GchpDocumentsWebService gchpDocumentsWebService;

    @RequestMapping(value = "/role/preferences", method = GET)
    @ResponseBody
    public RolePreferences getLodRolePreferences() {
        return gchpDocumentsWebService.getRolePreferences();
    }

    @RequestMapping(value = "/project/{id}/revisions", method = GET)
    @ResponseBody
    public List<ProjectDetailsRevision> getAllRevisions(@PathVariable("id") Long id) throws Exception_Exception {
        return gchpDocumentsWebService.getRevisions(id);
    }

    @RequestMapping(value = "/dictionary/realizationform", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredRealizationForms(@RequestBody RealisationFormFilters filters) {
        return gchpDictionariesWebService.getAllFilteredRealizationForms(filters)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/initiationmethod", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllInitiationMethods() {
        return gchpDictionariesWebService.getAllInitiationMethods()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/initiationmethodfilter", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredInitiationMethods(@RequestBody Filter filter) {
        return gchpDictionariesWebService.getFilteredInitiationMethods(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/realizationlevelfilter", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredRealizationLevels(@RequestBody Filter filter) {
        return gchpDictionariesWebService.getFilteredRealizationLevels(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/rfregion", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getAllRFRegions(@RequestBody RfRegionFilters filters) {
        List<SimpleIdNameModel> regions = gchpDictionariesWebService.getAllFilteredRFRegions(filters)
                .stream()
                .map(r -> new SimpleIdNameModel(r.getId(), r.getName()))
                .collect(Collectors.toList());
        return regions;
    }

    @RequestMapping(value = "/dictionary/municipality", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredMunicipalities(@RequestBody RfMunicipalityFilters filters) {
        return gchpDictionariesWebService.getFilteredMunicipality(filters)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/publicpartner", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredPublicPartners() {
        return new ArrayList<>();
    }

    @RequestMapping(value = "/dictionary/realizationsphere", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllRealizationSpheres() {
        return gchpDictionariesWebService.getAllRealizationSpheres()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/realizationspherefilter", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredRealizationSpheres(@RequestBody Filter filter) {
        return gchpDictionariesWebService.getFilteredRealizationSpheres(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/realizationsector", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getAllRealizationSectors(@RequestBody FilterByInitFormAndSphere filter) {
        return gchpDictionariesWebService.getFilteredRealizationSectors(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/objecttypefilter", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredObjectKinds(@RequestBody ObjectKindFilter filter) {
        return gchpDictionariesWebService.getFilteredObjectKinds(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/agreementsubject", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllAgreementSubjects() {
        return gchpDictionariesWebService.getAllAgreementSubjects()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/agreementgrounds", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllAgreementGrounds() {
        return gchpDictionariesWebService.getAllAgreementGrounds()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/projectstatus", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllProjectStatuses() {
        return gchpDictionariesWebService.getAllProjectStatuses()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/realizationstatus", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllRealizationStatuses() {
        return gchpDictionariesWebService.getAllRealizationStatuses()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/competitionresults", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllCompetitionResults() {
        return gchpDictionariesWebService.getAllCompetitionResults()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/competitionresultssign", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllCompetitionResultsSign() {
        return gchpDictionariesWebService.getAllCompetitionResultSigns()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/priceorder", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPriceOrders() {
        return gchpDictionariesWebService.getAllPriceOrders()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/contractpriceoffers", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllContractPriceOffers() {
        return gchpDictionariesWebService.getAllContractPriceOffers()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/rentobject", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllRentObject() {
        return gchpDictionariesWebService.getAllRentObjects()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/competitioncriterion", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllCompetitionCriterion() {
        return gchpDictionariesWebService.getAllCompetitionCriterions()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/finrequirments", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllFinRequirments() {
        return gchpDictionariesWebService.getAllFinRequirements()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/privateservicetypes", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPrivateServiceTypes() {
        return gchpDictionariesWebService.getAllPrivateServiceTypes()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/publicservicetypes", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPublicServiceTypes() {
        return gchpDictionariesWebService.getAllPublicServiceTypes()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/nofinrequirments", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllNoFinRequirments() {
        return gchpDictionariesWebService.getAllNonFinRequirements()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/businessmodetypes", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllBusinessModeTypes() {
        return gchpDictionariesWebService.getAllBusinessModeTypes()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/winnercontractpriceoffers", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllWinnerContractPriceOffers() {
        return gchpDictionariesWebService.getAllWinnerContractPriceOffers()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/privatepartnercostrecoverymethods", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPrivatePartnerCostRecoveryMethods() {
        return gchpDictionariesWebService.getAllPrivatePartnerCostRecoveryMethods()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/ensuremethods", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllEnsureMethods() {
        return gchpDictionariesWebService.getAllEnsureMethods()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/govsupport", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllGovSupports() {
        return gchpDictionariesWebService.getAllGovSupports()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/irsource", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllIRSources() {
        return gchpDictionariesWebService.getAllIRSources()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/ppresultsofplacing", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPpResultsOfPlacing() {
        return gchpDictionariesWebService.getAllPpResultsOfPlacing()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/irlevel", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllIRLevels() {
        return gchpDictionariesWebService.getAllIRLevels()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/paymentmethods", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllPaymentMethods() {
        return gchpDictionariesWebService.getAllPaymentMethods()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/costrecoverymethods", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllCostRecoveryMethods() {
        return gchpDictionariesWebService.getAllCostRecoveryMethods()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/terminationcauses", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllTerminationCauses() {
        return gchpDictionariesWebService.getAllTerminationCauses()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/opf", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllOpf() {
        return gchpDictionariesWebService.getAllOpfs()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/agreementset", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllAgreementsSet() {
        return gchpDictionariesWebService.getAllAgreementsSets()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/methodofexecuteobligation", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllMethodOfExecuteObligation() {
        return gchpDictionariesWebService.getAllMethodOfExecuteObligations()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/othergovsupports", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllOtherGovSupports() {
        return gchpDictionariesWebService.getAllOtherGovSupports()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/terminationaftermaths", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllTerminationAftermaths() {
        return gchpDictionariesWebService.getAllTerminationAftermaths()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/changesreason", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllChangesReasons() {
        return gchpDictionariesWebService.getAllChangesReasons()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/agreementssets", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllAgreementsSets() {
        return gchpDictionariesWebService.getAllAgreementsSets()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    ;

    @RequestMapping(value = "/dictionary/compofcompensation", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllTmCompositionOfCompensationGrantorFault() {
        return gchpDictionariesWebService.getAllTmCompositionOfCompensationGrantorFault()
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    ;

    @RequestMapping(value = "/dictionary/um", method = POST)
    @ResponseBody
    public PagingLoadResultBean<UmModel> getFilteredUnitsOfMeasurement(@RequestBody UmSearchCriteria criteria) {
        ru.ibs.gasu.common.soap.generated.gchpdicts.UmSearchCriteria um = new ru.ibs.gasu.common.soap.generated.gchpdicts.UmSearchCriteria();
        um.setIdOrName(criteria.getIdOrName());
        um.setLimit(criteria.getLimit());
        um.setOffset(criteria.getOffset());
        UmResult filteredOkei = gchpDictionariesWebService.getFilteredOkei(um);

        List<UmModel> res = filteredOkei.getData().stream().map(i -> new UmModel(i.getId(), i.getName(), i.getOkei())).collect(Collectors.toList());

        return new PagingLoadResultBean<>(res, filteredOkei.getTotalLength(), filteredOkei.getOffset());
    }

    @RequestMapping(value = "/dictionary/agreementgroundsfilter", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getFilteredAgreementGrounds(@RequestBody FilterByInitFormAndMethod filter) {
        return gchpDictionariesWebService.getFilteredAgreementGrounds(filter)
                .stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/project/save", method = POST)
    @ResponseBody
    public ProjectDetailDTO saveProject(@RequestBody ProjectDetailDTO project) {
        log.info("FILED adsWorkPlacesCount:" + project.getAdsWorkPlacesCount());
        ProjectDetailDTO saveGchpDocument = gchpDocumentsWebService.saveGchpDocument(project);
        return saveGchpDocument;
    }

    @RequestMapping(value = "/project/delete", method = POST)
    @ResponseBody
    public void deleteProject(@RequestBody Long id) {
        gchpDocumentsWebService.softDeleteGchpDocumentById(id);
    }

    @RequestMapping(value = "/svrorg", method = POST)
    @ResponseBody
    public List<SvrOrg> getSvrOrgs(@RequestBody FilterSvrOrgs filter) {
        return gchpDocumentsWebService.getSvrOrgs(filter);
    }

    @RequestMapping(value = "/project/paginateandfilterdocuments", method = POST)
    @ResponseBody
    public PagingLoadResultBean<ProjectDetailDTO> paginateAndFilterDocuments(@RequestBody DocumentsFilterPaginateCriteria criteria) {
        ProjectsResult projectsResult = gchpDocumentsWebService.paginateAndFilterDocuments(criteria);
        PagingLoadResultBean<ProjectDetailDTO> res = new PagingLoadResultBean<>();
        res.setOffset(projectsResult.getOffset());
        res.setTotalLength(projectsResult.getTotalLength());
        res.setData(projectsResult.getList());
        return res;
    }

    @RequestMapping(value = "export/tasksStatus", method = GET)
    @ResponseBody
    public List<ExportTask> getAllTasksStatus() {
        return gchpDocumentsWebService.getAllTasksStatus();
    }

    @RequestMapping(value = "export/runExcelFileGeneration", method = POST)
    @ResponseBody
    public List<ExportTask> runExcelFileGeneration(@RequestBody DocumentsFilterPaginateCriteria criteria) {
        return gchpDocumentsWebService.runExcelFileGeneration(criteria);
    }

    @RequestMapping(value = "export/getFileByUuid/{uuid}", method = GET)
    @ResponseBody
    public ResponseEntity<Object> getFileByUuid(@PathVariable String uuid) throws Exception {
        String fileByUuid = gchpDocumentsWebService.getFileByUuid(uuid);

        byte[] bytes = Base64.getDecoder().decode(fileByUuid);

        String fileName = "Проекты ГЧП";
        String encodedName = URLEncoder.encode(fileName, "UTF-8");

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName + ".xlsx")
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(bytes));
    }

    @RequestMapping(value = "export/getOdsFileByUuid/{uuid}", method = GET)
    @ResponseBody
    public ResponseEntity<Object> getOdsFileByUuid(@PathVariable String uuid) throws Exception {
        String fileByUuid = gchpDocumentsWebService.getOdsFileByUuid(uuid);

        byte[] bytes = Base64.getDecoder().decode(fileByUuid);

        String fileName = "Проекты ГЧП";
        String encodedName = URLEncoder.encode(fileName, "UTF-8");

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName + ".ods")
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(bytes));
    }

    @RequestMapping(value = "export/clearUserDoneTasks", method = GET)
    @ResponseBody
    public List<ExportTask> clearUserDoneTasks() {
        return gchpDocumentsWebService.clearUserDoneTasks();
    }

    @RequestMapping(value = "/dictionary/egrul", method = POST)
    @ResponseBody
    public PagingLoadResultBean<SimpleEgrulDomain> getSimpleEgrulDomains(@RequestBody EgrulCriteria criteria) {
        EgrulDomainResult egrulDomainResult = gchpDictionariesWebService.getSimpleEgrulDomains(criteria);
        return new PagingLoadResultBean<>(egrulDomainResult.getData(), egrulDomainResult.getTotalLength(), egrulDomainResult.getOffset());
    }

    @RequestMapping(value = "/dictionary/egrul/{inn}", method = GET)
    @ResponseBody
    public EgrulDomain getEgrulDomain(@PathVariable("inn") String inn) {
        return gchpDictionariesWebService.getEgrulDomain(inn);
    }

    @RequestMapping(value = "/dictionary/taxconditions", method = GET)
    @ResponseBody
    public List<TaxConditionModel> getAllTaxConditions() {
        List<TaxConditionModel> res = gchpDictionariesWebService.getAllTaxConditions().stream().map(i -> new TaxConditionModel(i.getId(), i.getName())).collect(Collectors.toList());
        return res;
    }

    @RequestMapping(value = "/dictionary/minimumguarantincometype", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllMinimumGuarantIncomeTypes() {
        return gchpDictionariesWebService.getAllMinimumGuarantIncomeType().stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/enablingpayouts", method = GET)
    @ResponseBody
    public List<SimpleIdNameModel> getAllEnablingPayouts() {
        return gchpDictionariesWebService.getAllEnablingPayouts().stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/dictionary/circumstances", method = POST)
    @ResponseBody
    public List<SimpleIdNameModel> getAllCircumstances(@RequestBody Filter filter) {
        return gchpDictionariesWebService.getAllCircumstances(filter).stream().map(i -> dozerMapper.map(i, SimpleIdNameModel.class)).collect(Collectors.toList());
    }
}
