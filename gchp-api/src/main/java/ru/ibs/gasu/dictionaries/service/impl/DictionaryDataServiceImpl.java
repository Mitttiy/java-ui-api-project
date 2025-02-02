package ru.ibs.gasu.dictionaries.service.impl;

import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.Converter;
import ru.ibs.gasu.dictionaries.DictCache;
import ru.ibs.gasu.dictionaries.Dictionary;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.domain.UmSearchCriteria;
import ru.ibs.gasu.soap.generated.dictionary.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.dictionaries.Dictionary.*;
import static ru.ibs.gasu.gchp.util.Utils.opt;

@Service
public class DictionaryDataServiceImpl implements DictionaryDataService {

    @Resource
    private DictCache dictCache;

    @Resource
    private IDictionaryDataWebService dictDataService;

    @Resource
    private IDictionaryMetadataWebService dictMetadataService;

    /**
     * DIC_GASU_GCHP_FORM
     */
    @Override
    public List<RealizationForm> getAllRealizationForms() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_FORM);
        return descriptors.stream().map(Converter::dictToRealizationForm)
                .sorted(new Comparator<RealizationForm>() {
                    @Override
                    public int compare(RealizationForm o1, RealizationForm o2) {
                        return o1.getSortOrder().compareTo(o2.getSortOrder());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RealizationForm> getAllFilteredRealizationForms(RealisationFormFilters formFilters) {
        Predicate<RealizationForm> predicate = sp1 -> true;

        if (formFilters.getId() != null) {
            predicate = predicate.and((sp1) -> sp1.getId().equals(formFilters.getId()));
        }
        List<RealizationForm> realizationForms = getAllRealizationForms().stream().filter(predicate).collect(Collectors.toList());

        if (formFilters.getImplLevelId() != null) {
            Iterator<RealizationForm> iterator = realizationForms.iterator();
            while (iterator.hasNext()) {
                Filter filter = new Filter();
                filter.setParentId(Long.valueOf(iterator.next().getId()));
                filter.setId(formFilters.getImplLevelId());
                try {
                    getFilteredRealizationLevels(filter);
                } catch (Exception e) {
                    iterator.remove();
                }
            }
        }

        if (formFilters.getImplSphereId() != null) {
            Iterator<RealizationForm> iterator = realizationForms.iterator();
            while (iterator.hasNext()) {
                Filter filter = new Filter();
                filter.setParentId(Long.valueOf(iterator.next().getId()));
                filter.setId(formFilters.getImplSphereId());
                try {
                    getFilteredRealizationSpheres(filter);
                } catch (Exception e) {
                    iterator.remove();
                }
            }
        }

        return realizationForms;
    }

    /**
     * DIC_GASU_GCHP_IMPL_METHOD
     */
    @Override
    public List<InitiationMethod> getAllInitiationMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_IMPL_METHOD);
        return descriptors.stream().map(Converter::dictToInitiationMethod).collect(Collectors.toList());
    }

    @Override
    public List<InitiationMethod> getFilteredInitiationMethods(Filter filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_GCHP_FORM_IMPL_METHOD.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_GCHP_FORM_IMPL_METHOD.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();

        if (filter.getId() != null) {
            domainDescriptor.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_IMPL_METHOD")).findFirst()
                    .ifPresent(field -> {
                        FilterConfig filterConfig = new FilterConfig();
                        filterConfig.setField("F_" + field.getId());
                        filterConfig.setComparison(Comparison.EQUAL);
                        filterConfig.setValue(filter.getId());
                        loadConfig.getFilters().add(filterConfig);
                    });
        }

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getParentId(), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + DIC_GASU_GCHP_FORM_IMPL_METHOD.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        return values.stream().map(Converter::dictToInitiationMethod2)
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_LVL
     */
    @Override
    public List<RealizationLevel> getAllRealizationLevels() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_LVL);
        return descriptors.stream().map(Converter::dictToRealizationLevel).collect(Collectors.toList());
    }

    @Override
    public List<RealizationLevel> getFilteredRealizationLevels(Filter filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_GCHP_FORM_LEVEL.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_GCHP_FORM_LEVEL.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();

        if (filter.getId() != null) {
            domainDescriptor.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_LVL")).findFirst()
                    .ifPresent(field -> {
                        FilterConfig filterConfig = new FilterConfig();
                        filterConfig.setField("F_" + field.getId());
                        filterConfig.setComparison(Comparison.EQUAL);
                        filterConfig.setValue(filter.getId());
                        loadConfig.getFilters().add(filterConfig);
                    });
        }

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getParentId(), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + DIC_GASU_GCHP_FORM_LEVEL.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        return values.stream().map(Converter::dictToRealizationLevel2)
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_SP1
     */
    @Override
    public List<DicGasuSp1> getAllFilteredRFRegions(RFRegionFilters filters) {

        final String RUSSIANFEDERATIONID = "8";

        Predicate<DicGasuSp1> predicate = sp1 -> true;

        if (filters.getId() != null) {
            predicate = predicate.and((sp1) -> sp1.getId().equals(filters.getId()));
        }

        List<DicGasuSp1> regions = getAllRFRegions().stream()
                .filter(r -> r.getIsLeaf() && r.getBActual() && r.getAdmCntr() != null)
                .filter((r) -> {
                    if (r.getName().contains("округ")){
                        return !r.getIdParent().equals(RUSSIANFEDERATIONID);
                    }
                    return true;
                })
                .sorted(Comparator.comparing(DicGasuSp1::getName))
                .collect(Collectors.toList());
        return regions.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_SP1
     */
    @Override
    public List<DicGasuSp1> getAllRFRegions() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_SP1);
        return descriptors.stream().map(Converter::dictToDicGasuSp1)
                .sorted(Comparator.comparing(DicGasuSp1::getName))
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_SPHERE
     */
    @Override
    public List<RealizationSphereEntity> getAllRealizationSpheres() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_SPHERE);
        return descriptors.stream().map(Converter::dictToRealizationSphereEntity).collect(Collectors.toList());
    }

    @Override
    public List<RealizationSphereEntity> getFilteredRealizationSpheres(Filter filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_GCHP_FORM_SPHERE.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_GCHP_FORM_SPHERE.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();

        if (filter.getId() != null) {
            domainDescriptor.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_SPHERE")).findFirst()
                    .ifPresent(field -> {
                        FilterConfig filterConfig = new FilterConfig();
                        filterConfig.setField("F_" + field.getId());
                        filterConfig.setComparison(Comparison.EQUAL);
                        filterConfig.setValue(filter.getId());
                        loadConfig.getFilters().add(filterConfig);
                    });
        }

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getParentId(), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_CHANGE_REASON.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        return values.stream().map(Converter::dictToRealizationSphereEntity2)
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_SECTOR
     */
    @Override
    public List<RealizationSectorEntity> getAllRealizationSectors() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_SECTOR);
        return descriptors.stream().map(Converter::dictToRealizationSectorEntity).collect(Collectors.toList());
    }


    @Override
    public List<RealizationSectorEntity> getFilteredRealizationSectors(FilterByInitFormAndSphere filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(Dictionary.DIC_GASU_GCHP_FORM_SPHERE.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE.name() + " не найден");
        }

        FilterPagingLoadConfig config = new FilterPagingLoadConfig();

        FilterConfig filterConfig = new FilterConfig();
        filterConfig.setField("F_10783");
        filterConfig.setComparison(Comparison.EQUAL);
        filterConfig.setValue(filter.getSphereId());
        config.getFilters().add(filterConfig);

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getFormId(), false, config);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        List<Long> collect = values.stream()
                .mapToLong(DictionaryDataRecordDescriptor::getId).boxed().collect(Collectors.toList());

        List<DictionaryDataRecordDescriptor> res1 = new ArrayList<>();

        // 2 iter
        for (Long aLong : collect) {
            DictionaryDomainDescriptor domainDescriptor1 = dictMetadataService.getDomainByName(Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR.name());
            if (domainDescriptor1 == null) {
                throw new IllegalArgumentException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR.name() + " не найден");
            }

            FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();
            if (filter.getId() != null) {
                domainDescriptor1.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_SECTOR")).findFirst()
                        .ifPresent(field -> {
                            FilterConfig idSectorFilter = new FilterConfig();
                            idSectorFilter.setField("F_" + field.getId());
                            idSectorFilter.setComparison(Comparison.EQUAL);
                            idSectorFilter.setValue(filter.getId());
                            loadConfig.getFilters().add(idSectorFilter);
                        });
            }

            DataPage dataPage1 = dictDataService.getDataRecordsByOwnerId(domainDescriptor1, aLong, false, loadConfig);
            if (isEmpty(dataPage1.getContent())) {
                throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR.name() + " не содержит записей");
            }

            List<DictionaryDataRecordDescriptor> dataList1 = dataPage1.getContent().stream()
                    .map(o -> (DictionaryDataRecordDescriptor) o)
                    .collect(Collectors.toList());
            res1.addAll(dataList1);
        }

        return res1.stream().map(Converter::dictToRealizationSectorEntity2).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_OBJ_KIND
     */
    @Override
    public List<ObjectKind> getAllObjectKinds() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_OBJ_KIND);
        return descriptors.stream().map(Converter::dictToObjectKind).collect(Collectors.toList());
    }

    @Override
    public List<ObjectKind> getFilteredObjectKinds(ObjectKindFilter filter) {
        FilterByInitFormAndSphere filterByInitFormAndSphere = new FilterByInitFormAndSphere();
        filterByInitFormAndSphere.setFormId(filter.getFormId());
        filterByInitFormAndSphere.setSphereId(filter.getSphereId());
        filterByInitFormAndSphere.setId(filter.getSectorId());
        List<RealizationSectorEntity> sectors = getFilteredRealizationSectors(filterByInitFormAndSphere);

        if (isEmpty(sectors)) {
            return new ArrayList<>();
        }

        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR_OBJECT_TYPE.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR_OBJECT_TYPE.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();
        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, Long.valueOf(sectors.get(0).getDicGasuGchpFormSphereSector()), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_SPHERE_SECTOR_OBJECT_TYPE.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        return dataList.stream().map(Converter::dictToObjectKind2)
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_AGRMT_SUBJ
     */
    @Override
    public List<AgreementSubject> getAllAgreementSubjects() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_AGRMT_SUBJ);
        return descriptors.stream().map(Converter::dictToAgreementSubject).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PROJECT_STATUS
     */
    @Override
    public List<ProjectStatus> getAllProjectStatuses() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PROJECT_STATUS);
        return descriptors.stream().map(Converter::dictToProjectStatus).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_IMPL_STATUS
     */
    @Override
    public List<RealizationStatus> getAllRealizationStatuses() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_IMPL_STATUS);
        return descriptors.stream().map(Converter::dictToRealizationStatus)
                .sorted(new Comparator<RealizationStatus>() {
                    @Override
                    public int compare(RealizationStatus o1, RealizationStatus o2) {
                        return o1.getSortOrder().compareTo(o2.getSortOrder());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_RENT_OBJECT
     */
    @Override
    public List<RentObject> getAllRentObjects() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_RENT_OBJECT);
        return descriptors.stream().map(Converter::dictToRentObjects).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_AGREEMENT_GROUNDS
     */
    @Override
    public List<AgreementGrounds> getAllAgreementGrounds() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_AGREEMENT_GROUNDS);
        return descriptors.stream().map(Converter::dictToAgreementGrounds).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_AGREEMENT_GROUNDS
     */
    @Override
    public List<AgreementGrounds> getFilteredAgreementGrounds(FilterByInitFormAndMethod filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD.name() + " не найден");
        }

        FilterPagingLoadConfig config = new FilterPagingLoadConfig();

        FilterConfig filterConfig = new FilterConfig();
        filterConfig.setField("F_10774");
        filterConfig.setComparison(Comparison.EQUAL);
        filterConfig.setValue(filter.getInitMethodId());
        config.getFilters().add(filterConfig);

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getFormId(), false, config);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        List<Long> collect = values.stream()
                .mapToLong(DictionaryDataRecordDescriptor::getId).boxed().collect(Collectors.toList());

        List<DictionaryDataRecordDescriptor> res1 = new ArrayList<>();

        // 2 iter
        for (Long aLong : collect) {
            DictionaryDomainDescriptor domainDescriptor1 = dictMetadataService.getDomainByName(Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD_AGR_GROUNDS.name());
            if (domainDescriptor == null) {
                throw new IllegalArgumentException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD_AGR_GROUNDS.name() + " не найден");
            }

            DataPage dataPage1 = dictDataService.getDataRecordsByOwnerId(domainDescriptor1, aLong, false, null);
            if (isEmpty(dataPage.getContent())) {
                throw new IllegalStateException("Справочник " + Dictionary.DIC_GASU_GCHP_FORM_IMPL_METHOD_AGR_GROUNDS.name() + " не содержит записей");
            }

            List<DictionaryDataRecordDescriptor> dataList1 = dataPage1.getContent().stream()
                    .map(o -> (DictionaryDataRecordDescriptor) o)
                    .collect(Collectors.toList());
            res1.addAll(dataList1);
        }
        return res1.stream().map(Converter::dictToAgreementGrounds).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_COMPETITION_RESULTS
     */
    @Override
    public List<CompetitionResults> getAllCompetitionResults() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_COMPETITION_RESULTS);
        return descriptors.stream().map(Converter::dictToCompetitionResults).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_COMPETITION_RESULT_SIGN
     */
    @Override
    public List<CompetitionResultsSign> getAllCompetitionResultSigns() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_COMPETITION_RESULT_SIGN);
        return descriptors.stream().map(Converter::dictToCompetitionResultsSign).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PR_P_COST_RECOVERY_METHODS
     */
    @Override
    public List<PrivatePartnerCostRecoveryMethodEntity> getAllPrivatePartnerCostRecoveryMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PR_P_COST_RECOVERY_METHODS);
        return descriptors.stream().map(Converter::dictToPrivatePartnerCostRecoveryMethodEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_METHOD_OF_EXECUTE_OBLIGATION
     */
    @Override
    public List<MethodOfExecuteObligationEntity> getAllMethodOfExecuteObligations() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_METHOD_OF_EXECUTE_OBLIGATION);
        return descriptors.stream().map(Converter::dictToMethodOfExecuteObligationEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_OTHER_GOV_SUPPORTS
     */
    @Override
    public List<OtherGovSupportsEntity> getAllOtherGovSupports() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_OTHER_GOV_SUPPORTS);
        return descriptors.stream().map(Converter::dictToOtherGovSupportsEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_AGREEMENT_SETS
     */
    @Override
    public List<AgreementsSetEntity> getAllAgreementsSets() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_AGREEMENT_SETS);
        return descriptors.stream().map(Converter::dictToAgreementsSetEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_CONTRACT_PRICE_OFFER
     */
    @Override
    public List<ContractPriceOffer> getAllContractPriceOffers() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_CONTRACT_PRICE_OFFER);
        return descriptors.stream().map(Converter::dictToContractPriceOffer).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_WINNER_CONTRACT_PRICE_OFFER
     */
    @Override
    public List<WinnerContractPriceOfferEntity> getAllWinnerContractPriceOffers() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_CONTRACT_PRICE_OFFER);
        return descriptors.stream().map(Converter::dictToWinnerContractPriceOfferEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_OPF
     */
    @Override
    public List<OpfEntity> getAllOpfs() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_OPF);
        return descriptors.stream().map(Converter::dictToOpfEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_ENSURE_METHD
     */
    @Override
    public List<EnsureMethod> getAllEnsureMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_ENSURE_METHD);
        return descriptors.stream().map(Converter::dictToEnsureMethod).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_GOV_SUPPORT
     */
    @Override
    public List<GovSupport> getAllGovSupports() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_GOV_SUPPORT);
        return descriptors.stream().map(Converter::dictToGovSupport).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_IR_SOURCE
     */
    @Override
    public List<IRSourceEntity> getAllIRSources() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_IR_SOURCE);
        return descriptors.stream().map(Converter::dictToIRSourceEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_RESULTS_PLACING_AN_OFFER
     */
    @Override
    public List<PpResultOfPlacingEntity> getAllPpResultsOfPlacing() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_RESULTS_PLACING_AN_OFFER);
        return descriptors.stream().map(Converter::dictToPpResultsOfPlacingEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_COMP_OF_COMPENSATION
     */
    @Override
    public List<TmCompositionOfCompensationGrantorFaultEntity> getAllTmCompositionOfCompensationGrantorFault() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_COMP_OF_COMPENSATION);
        return descriptors.stream().map(Converter::dictToTmCompositionOfCompensationGrantorFaultEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_IR_LEVEL
     */
    @Override
    public List<IRLevelEntity> getAllIRLevels() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_IR_LEVEL);
        return descriptors.stream().map(Converter::dictToIRLevelEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PAYMENT_METHOD
     */
    @Override
    public List<PaymentMethodEntity> getAllPaymentMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PAYMENT_METHOD);
        return descriptors.stream().map(Converter::dictToPaymentMethodEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_TERMINATION_CAUSE
     */
    @Override
    public List<TerminationCause> getAllTerminationCauses() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_TERMINATION_CAUSE);
        return descriptors.stream().map(Converter::dictToTerminationCause).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_TERMINATION_AFTERMATH
     */
    @Override
    public List<TerminationAftermathEntity> getAllTerminationAftermaths() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_TERMINATION_AFTERMATH);
        return descriptors.stream().map(Converter::dictToTerminationAftermathEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_CHANGES_REASON
     */
    @Override
    public List<ChangesReasonEntity> getAllChangesReasons() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_CHANGES_REASON);
        return descriptors.stream().map(Converter::dictToChangesReasonEntity).collect(Collectors.toList());
    }

    @Override
    public List<ChangesReasonEntity> getFilteredChangesReasons(Filter filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_GCHP_FORM_CHANGE_REASON.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_GCHP_FORM_CHANGE_REASON.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();

        if (filter.getId() != null) {
            domainDescriptor.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_CHANGES_REASON")).findFirst()
                    .ifPresent(field -> {
                        FilterConfig filterConfig = new FilterConfig();
                        filterConfig.setField("F_" + field.getId());
                        filterConfig.setComparison(Comparison.EQUAL);
                        filterConfig.setValue(filter.getId());
                        loadConfig.getFilters().add(filterConfig);
                    });
        }

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getParentId(), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + DIC_GASU_GCHP_FORM_CHANGE_REASON.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        return values.stream().map(Converter::dictToChangesReasonEntity2)
                .collect(Collectors.toList());
    }

    /**
     * DIC_GASU_OKTMO
     */
    @Override
    public List<Municipality> getFilteredMunicipality(RFMunicipalityFilters filters) {
        if (filters == null || filters.getRegionId() == null || filters.getRegionId().isEmpty())
            return new ArrayList<>();
        List<String> municipalityGroups = Arrays.asList("52", "53", "55", "102", "103", "104", "54", "2");
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_OKTMO);
        Stream<Municipality> stream = descriptors.stream().map(Converter::dictToMunicipality)
                .filter(i -> i.getRegionId() != null && i.getRegionId().equals(filters.getRegionId()) && !municipalityGroups.contains(i.getRegionTypeId()))
                .sorted(Comparator.comparing(Municipality::getName));// сортировка по имени

        if (filters.getOktmo() != null) {
            stream = stream.filter(i -> i.getId().equals(filters.getOktmo()) && !municipalityGroups.contains(i.getRegionTypeId()));
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public Municipality findMunicipalityById(Object id) {
        if (id == null) return null;
        return dictCache.getDictFromCache(Dictionary.DIC_GASU_OKTMO).stream()
                .filter(i -> id.toString().equals(i.getOid()))
                .map(Converter::dictToMunicipality)
                .findFirst()
                .orElse(null);
    }

    /**
     * DIC_GASU_GCHP_COST_RECOVERY_METHOD
     */
    @Override
    public List<CostRecoveryMethodEntity> getAllCostRecoveryMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_COST_RECOVERY_METHOD);
        return descriptors.stream().map(Converter::dictToCostRecoveryMethodEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PRICE_ORDER
     */
    @Override
    public List<PriceOrderEntity> getAllPriceOrderMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PRICE_ORDER);
        return descriptors.stream().map(Converter::dictToPriceOrderEntity).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_FIN_IND_DIM_TYPE
     */
    @Override
    public List<FinIndDimType> getAllFinIndDimTypeMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_FIN_IND_DIM_TYPE);
        return descriptors.stream().map(Converter::dictToFinIndDimType).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_BUSINESS_MODE_TYPE
     */
    @Override
    public List<BusinessModeType> getAllBusinessModeTypeMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_BUSINESS_MODE_TYPE);
        return descriptors.stream().map(Converter::dictToBusinessModeType).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_COMPETITION_CRITERION
     */
    @Override
    public List<CompetitionCriterion> getAllCompetitionCriterionMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_COMPETITION_CRITERION);
        return descriptors.stream().map(Converter::dictToCompetitionCriterion).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_FIN_REQUIREMENT
     */
    @Override
    public List<FinRequirement> getAllFinRequirementMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_FIN_REQUIREMENT);
        return descriptors.stream().map(Converter::dictToFinRequirement).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_NO_FIN_REQUIREMENT
     */
    @Override
    public List<NoFinRequirement> getAllNonFinRequirementMethods() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_NO_FIN_REQUIREMENT);
        return descriptors.stream().map(Converter::dictToNonFinRequirement).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PRIVATE_SERVICE_TYPE
     */
    @Override
    public List<PrivateServiceType> getAllPrivateServiceTypes() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PRIVATE_SERVICE_TYPE);
        return descriptors.stream().map(Converter::dictToPrivateServiceType).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE
     */
    @Override
    public List<PublicServiceType> getAllPublicServiceTypes() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE);
        return descriptors.stream().map(Converter::dictToPublicServiceType).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_OKEI
     */
    public List<PublicServiceType> getAllOkei() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE);
        return descriptors.stream().map(Converter::dictToPublicServiceType).collect(Collectors.toList());
    }

    /**
     * DIC_GASU_OKEI
     */
    public UmResult getFilteredOkei(UmSearchCriteria umSearchCriteria) {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(DIC_GASU_OKEI);

        Predicate<UnitOfMeasure> predicate = new Predicate<UnitOfMeasure>() {
            @Override
            public boolean test(UnitOfMeasure unitOfMeasure) {
                if (umSearchCriteria.getIdOrName() == null || umSearchCriteria.getIdOrName().isEmpty()) return true;
                boolean nameCheck = unitOfMeasure.getName().contains(umSearchCriteria.getIdOrName());
                boolean idCheck = unitOfMeasure.getOkei().equals(umSearchCriteria.getIdOrName());
                ;
                return nameCheck || idCheck;
            }
        };
        List<UnitOfMeasure> data = descriptors.stream().map(Converter::dictToUnitOfMeasureType)
                .filter(predicate)
                .skip(umSearchCriteria.getOffset())
                .limit(umSearchCriteria.getLimit())
                .collect(Collectors.toList());
        UmResult res = new UmResult();
        res.setData(data);
        res.setOffset(umSearchCriteria.getOffset());
        res.setTotalLength(descriptors.size());
        return res;
    }

    @Override
    public EgrulDomainResult getSimpleEgrulDomains(EgrulCriteria criteria) {
        //егрюл
        Dictionary dictionaryEnum = criteria.isEgrip() ? DIC_GASU_EGRIP : DIC_GASU_EGRUL;
        DictionaryDomainDescriptor egrulDomainDescriptor = dictMetadataService.getDomainByName(dictionaryEnum.name());
        if (egrulDomainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + dictionaryEnum.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();
        loadConfig.setLimit(criteria.getLimit());
        loadConfig.setOffset(criteria.getOffset());

        if (criteria.getSortInfo() != null) {
            criteria.getSortInfo().getFields().forEach(field -> {
                SortInfo sortInfo = new SortInfo();
                sortInfo.setDirection(Direction.fromValue(field.getDirection().name()));
                sortInfo.setField(field.getField());
                loadConfig.getSortInfo().add(sortInfo);
            });
        }

        if (criteria.getId() != null) {
            loadConfig.getFilters().add(createFilterConfig("id", criteria.getId()));
        } else {
            FilterConfig innFilterConfig = new FilterConfig();
            innFilterConfig.setDisjunction(true);
            loadConfig.getFilters().add(innFilterConfig);

            FilterConfig ogrnFilterConfig = new FilterConfig();
            ogrnFilterConfig.setDisjunction(true);
            loadConfig.getFilters().add(ogrnFilterConfig);

            egrulDomainDescriptor.getFields().forEach(field -> {
                if (field.getName().equals(EgrulCriteria.FIELD_NAMES.get(0)) && criteria.getFullName() != null) {
                    loadConfig.getFilters().add(createFilterConfig("F_" + field.getId(), criteria.getFullName()));
                } else if (field.getName().equals(EgrulCriteria.FIELD_NAMES.get(1)) && criteria.getShortName() != null) {
                    loadConfig.getFilters().add(createFilterConfig("F_" + field.getId(), criteria.getShortName()));
                } else if ((field.getName().equals(EgrulCriteria.FIELD_NAMES.get(2)) || field.getName().equals(EgrulCriteria.FIELD_NAMES.get(3))) && criteria.getInn() != null) {
                    innFilterConfig.getFilters().add(createFilterConfig("F_" + field.getId(), criteria.getInn()));
                } else if ((field.getName().equals(EgrulCriteria.FIELD_NAMES.get(4)) || field.getName().equals(EgrulCriteria.FIELD_NAMES.get(5))) && criteria.getOgrn() != null) {
                    ogrnFilterConfig.getFilters().add(createFilterConfig("F_" + field.getId(), criteria.getOgrn()));
                }
            });
        }

        DataPage dataPage = dictDataService.getDataRecords(egrulDomainDescriptor, false, loadConfig);
        EgrulDomainResult result = new EgrulDomainResult();
        if (isEmpty(dataPage.getContent())) {
            return result;
        }

        List<SimpleEgrulDomain> domains = dataPage.getContent().stream().map(Converter::dictToSimpleEgrulDomain)
                .collect(Collectors.toList());

        result.setData(new ArrayList<>(domains));
        result.setOffset(criteria.getOffset());
        result.setTotalLength((int) dataPage.getTotalElements());
        return result;
    }

    @Override
    public EgrulDomain getEgrulDomain(String inn) {
        EgrulCriteria criteria = new EgrulCriteria();
        criteria.setInn(inn);
        //ищем в ЕГРЮЛ
        EgrulDomain egrulDomain = (EgrulDomain) getSimpleEgrulDomains(criteria)
                .getData().stream().findFirst()
                .orElseGet(() -> {

                    //если не нашли в ЕГРЮЛ, то ищем в ЕГРИП
                    criteria.setEgrip(true);
                    return getSimpleEgrulDomains(criteria)
                            .getData().stream().findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Нет записи в ЕГРЮЛ/ИП с ИНН = " + inn));
                });
        if (criteria.isEgrip()) {
            egrulDomain.getFioTopManagers().add(egrulDomain.getFullName());
        } else {
            setEgrulTopManagers(egrulDomain);
            setEgrulFounders(egrulDomain);
            setEgrulCapital(egrulDomain);
        }
        return egrulDomain;
    }


    private FilterConfig createFilterConfig(String field, Object value) {
        FilterConfig filterConfig = new FilterConfig();
        filterConfig.setField(field);
        filterConfig.setComparison(Comparison.EQUAL);
        filterConfig.setValue(value);
        return filterConfig;
    }

    private void setEgrulTopManagers(EgrulDomain egrulDomain) {

        //ген. дир
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_EGRUL_FL.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_EGRUL_FL.name() + " не найден");
        }

        DataPage dataPage = dictDataService.getDataRecordsByDomainIdAndOwnerId(domainDescriptor.getId(), egrulDomain.getId(), false, new FilterPagingLoadConfig());

        List<String> managers = dataPage.getContent().stream()
                .map(Converter::convertDataRecordDescriptorToFio).collect(Collectors.toList());
        egrulDomain.getFioTopManagers().clear();
        egrulDomain.getFioTopManagers().addAll(managers);
    }

    private void setEgrulFounders(EgrulDomain egrulDomain) {
        //учредители
        List<Dictionary> founders = Arrays.asList(
                DIC_GASU_EGRUL_FOUNDER_FOR,
                DIC_GASU_EGRUL_FOUNDER_IND,
                DIC_GASU_EGRUL_FOUNDER_PIF,
                DIC_GASU_EGRUL_FOUNDER_RF_MO,
                DIC_GASU_EGRUL_FOUNDER_RUS
        );

        for (Dictionary dictionary : founders) {
            DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(dictionary.name());
            if (domainDescriptor == null) {
                throw new IllegalArgumentException("Справочник " + dictionary.name() + " не найден");
            }

            DataPage dataPage = dictDataService.getDataRecordsByDomainIdAndOwnerId(domainDescriptor.getId(), egrulDomain.getId(), false, new FilterPagingLoadConfig());
            if (!isEmpty(dataPage.getContent())) {

                List<SimpleEgrulDomainWithCapital> domains = dataPage.getContent().stream().map(Converter::dictToEgrulFounder)
                        .collect(Collectors.toList());
                egrulDomain.getFounders().addAll(domains);
            }
        }
    }

    private void setEgrulCapital(EgrulDomain egrulDomain) {
        //капитал
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_EGRUL_AUTH_CAPITAL.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_EGRUL_AUTH_CAPITAL.name() + " не найден");
        }

        DataPage dataPage = dictDataService.getDataRecordsByDomainIdAndOwnerId(domainDescriptor.getId(), egrulDomain.getId(), false, new FilterPagingLoadConfig());
        if (!isEmpty(dataPage.getContent())) {
            dataPage.getContent().stream().findFirst().ifPresent(dataRecord -> {
                ((DictionaryDataRecordDescriptor) dataRecord).getFields().forEach(field -> {
                    if (field.getFieldDescriptor().getName().equalsIgnoreCase("CAPITAL_NAME")) {
                        egrulDomain.setCapitalInfo(opt(() -> field.getExValues().get(0).getValue()));
                    } else if (field.getFieldDescriptor().getName().equalsIgnoreCase("CAPITAL_SUM")) {
                        egrulDomain.setCapitalValue(opt(() -> field.getExValues().get(0).getValue()));
                    }
                });
            });

        }
    }

    @Override
    public List<TaxCondition> getAllTaxConditions() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_TAX_CONDITIONS_PROJECT);
        return descriptors.stream().map(Converter::dictToTaxCondition).collect(Collectors.toList());
    }

    @Override
    public List<MinimumGuarantIncomeType> getAllMinimumGuarantIncomeType() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_MINIMUM_GUARANT_INCOME_TYPE);
        return descriptors.stream().map(Converter::dictToMinimumGuarantIncomeType).collect(Collectors.toList());
    }

    @Override
    public List<EnablingPayout> getAllEnablingPayouts() {
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_ENABLING_PAYOUTS);
        return descriptors.stream().map(Converter::dictToEnablingPayout).collect(Collectors.toList());
    }

    @Override
    public List<Circumstance> getFilteredCircumstances(Filter filter) {
        DictionaryDomainDescriptor domainDescriptor = dictMetadataService.getDomainByName(DIC_GASU_GCHP_CIRCUMSTANCES.name());
        if (domainDescriptor == null) {
            throw new IllegalArgumentException("Справочник " + DIC_GASU_GCHP_CIRCUMSTANCES.name() + " не найден");
        }

        FilterPagingLoadConfig loadConfig = new FilterPagingLoadConfig();

        if (filter.getId() != null) {
            domainDescriptor.getFields().stream().filter(field -> field.getName().equals("DIC_GASU_GCHP_CIRCUMSTANCES_STAGES")).findFirst()
                    .ifPresent(field -> {
                        FilterConfig filterConfig = new FilterConfig();
                        filterConfig.setField("F_" + field.getId());
                        filterConfig.setComparison(Comparison.EQUAL);
                        filterConfig.setValue(filter.getId());
                        loadConfig.getFilters().add(filterConfig);
                    });
        }

        DataPage dataPage = dictDataService.getDataRecordsByOwnerId(domainDescriptor, filter.getParentId(), false, loadConfig);
        if (isEmpty(dataPage.getContent())) {
            throw new IllegalStateException("Справочник " + DIC_GASU_GCHP_CIRCUMSTANCES_STAGES.name() + " не содержит записей");
        }

        List<DictionaryDataRecordDescriptor> dataList = dataPage.getContent().stream()
                .map(o -> (DictionaryDataRecordDescriptor) o)
                .collect(Collectors.toList());

        Map<Long, DictionaryDataRecordDescriptor> res = dataList.stream()
                .collect(Collectors.toMap(DictionaryDataRecordDescriptor::getId, v -> v));

        Collection<DictionaryDataRecordDescriptor> values = res.values();

        List<Circumstance> result = values.stream().map(Converter::dictToCircumstance2)
                .collect(Collectors.toList());
        return result;
    }
}
