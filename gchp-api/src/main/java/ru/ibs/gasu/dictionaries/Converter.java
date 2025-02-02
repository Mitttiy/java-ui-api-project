package ru.ibs.gasu.dictionaries;

import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.soap.generated.dictionary.DataRecordEntry;
import ru.ibs.gasu.soap.generated.dictionary.DictionaryDataRecordDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.util.Utils.opt;

public class Converter {

    public static RealizationForm dictToRealizationForm(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationForm realizationForm = new RealizationForm();
        realizationForm.setId(getDictId(dataRecord));
        realizationForm.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        realizationForm.setSortOrder(opt(() -> Integer.parseInt(fieldIndex.get("SORT_ORDER").getExValues().get(0).getValue())));

        return realizationForm;
    }

    public static InitiationMethod dictToInitiationMethod(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        InitiationMethod initiationMethod = new InitiationMethod();
        initiationMethod.setId(getDictId(dataRecord));
        initiationMethod.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        return initiationMethod;
    }

    public static InitiationMethod dictToInitiationMethod2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);
        InitiationMethod initiationMethod = new InitiationMethod();
        initiationMethod.setId(opt(() -> fieldIndex.get("DIC_GASU_GCHP_IMPL_METHOD").getExValues().get(0).getValue()));
        initiationMethod.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_IMPL_METHOD").getExValues().get(0).getTextRepresentation()));
        return initiationMethod;
    }

    public static RealizationLevel dictToRealizationLevel(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationLevel realizationLevel = new RealizationLevel();
        realizationLevel.setId(getDictId(dataRecord));
        realizationLevel.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        return realizationLevel;
    }

    public static RealizationLevel dictToRealizationLevel2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationLevel realizationLevel = new RealizationLevel();
        realizationLevel.setId(opt(() -> fieldIndex.get("DIC_GASU_GCHP_LVL").getExValues().get(0).getValue()));
        realizationLevel.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_LVL").getExValues().get(0).getTextRepresentation()));
        return realizationLevel;
    }

    public static SimpleEgrulDomain dictToSimpleEgrulDomain(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        SimpleEgrulDomain egrulDomain = new EgrulDomain();

        egrulDomain.setId(((DictionaryDataRecordDescriptor) dataRecord).getId());

        egrulDomain.setShortName(opt(() -> fieldIndex.get("SHORT_NAME").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setFullName(opt(() -> fieldIndex.get("FULL_NAME").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setInn(opt(() -> fieldIndex.get("INN").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setOgrn(opt(() -> fieldIndex.get("OGRN").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setRoAddress(opt(() -> fieldIndex.get("RO_ADDRESS").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setPostalCode(opt(() -> fieldIndex.get("POSTAL_CODE").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setRegionType(opt(() -> fieldIndex.get("REGION_TYPE").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setRegionName(opt(() -> fieldIndex.get("REGION_NAME").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setStreetType(opt(() -> fieldIndex.get("STREET_TYPE").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setStreetName(opt(() -> fieldIndex.get("STREET_NAME").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setBuilding(opt(() -> fieldIndex.get("BUILDING").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setOffice(opt(() -> fieldIndex.get("OFFICE").getExValues().get(0).getTextRepresentation()));

        //for EGRIP
        String ipName = convertDataRecordDescriptorToFio(dataRecord);
        if (egrulDomain.getShortName() == null) {
            egrulDomain.setShortName(ipName);
        }
        if (egrulDomain.getFullName() == null) {
            egrulDomain.setFullName(ipName);
        }
        if (egrulDomain.getInn() == null) {
            egrulDomain.setInn(opt(() -> fieldIndex.get("INN_FL").getExValues().get(0).getTextRepresentation()));
        }
        if (egrulDomain.getOgrn() == null) {
            egrulDomain.setOgrn(opt(() -> fieldIndex.get("OGRNIP").getExValues().get(0).getTextRepresentation()));
        }

        return egrulDomain;
    }

    public static String convertDataRecordDescriptorToFio(Object dataRecordDescriptor) {
        String[] fio = new String[3];
        ((DictionaryDataRecordDescriptor) dataRecordDescriptor).getFields().forEach(field -> {
            if (field.getFieldDescriptor().getName().equalsIgnoreCase("LAST_NAME")) {
                fio[0] = opt(() -> field.getExValues().get(0).getValue());
            } else if (field.getFieldDescriptor().getName().equalsIgnoreCase("FIRST_NAME")) {
                fio[1] = opt(() -> field.getExValues().get(0).getValue());
            } else if (field.getFieldDescriptor().getName().equalsIgnoreCase("SECOND_NAME")) {
                fio[2] = opt(() -> field.getExValues().get(0).getValue());
            }
        });
        return String.join(" ", fio);
    }

    public static SimpleEgrulDomainWithCapital dictToEgrulFounder(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        SimpleEgrulDomainWithCapital egrulDomain = new SimpleEgrulDomainWithCapital();
        egrulDomain.setId(((DictionaryDataRecordDescriptor) dataRecord).getId());
        egrulDomain.setFullName(opt(() -> fieldIndex.get("FULL_NAME").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setInn(opt(() -> fieldIndex.get("INN").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setOgrn(opt(() -> fieldIndex.get("OGRN").getExValues().get(0).getTextRepresentation()));
        egrulDomain.setCapitalValue(opt(() -> fieldIndex.get("SHARE_PAR_VALUE_IN_RUBLES").getExValues().get(0).getTextRepresentation()));

        //TODO GASU21-147 : доделать, когда появятся поля с долями в учредителях
//        egrulDomain.setCapitalValue(opt(() -> fieldIndex.get("CAPITAL_SUM").getExValues().get(0).getTextRepresentation()));
//        egrulDomain.setCapitalPercent(opt(() -> fieldIndex.get("CAPITAL_PERCENT").getExValues().get(0).getTextRepresentation()));

        return egrulDomain;
    }

    public static RealizationLevelDependency dictToRealizationLevelDependency(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationLevelDependency o = new RealizationLevelDependency();
        o.setFormId(opt(() -> Long.parseLong(fieldIndex.get("OWNER_ID").getExValues().get(0).getValue())));
        o.setLevelId(opt(() -> Long.parseLong(fieldIndex.get("DIC_GASU_GCHP_LVL").getExValues().get(0).getValue())));
        return o;
    }

    public static ImplMethodDependency dictToImplMethodDependency(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ImplMethodDependency o = new ImplMethodDependency();
        o.setFormId(opt(() -> Long.parseLong(fieldIndex.get("OWNER_ID").getExValues().get(0).getValue())));
        o.setImplMethodId(opt(() -> Long.parseLong(fieldIndex.get("DIC_GASU_GCHP_FORM_IMPL_METHOD").getExValues().get(0).getValue())));
        return o;
    }

    public static DicGasuSp1 dictToDicGasuSp1(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        DicGasuSp1 dicGasuSp1 = new DicGasuSp1();
        dicGasuSp1.setId(getDictId(dataRecord));
        dicGasuSp1.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        Boolean isLeaf = ((DictionaryDataRecordDescriptor) dataRecord).isHasChildren();
        dicGasuSp1.setIsLeaf(isLeaf == null || !isLeaf);
        dicGasuSp1.setBActual(true);
        dicGasuSp1.setAdmCntr((opt(() -> fieldIndex.get("ADM_CNTR").getExValues().get(0).getValue())));
        dicGasuSp1.setIdParent(String.valueOf(((DictionaryDataRecordDescriptor) dataRecord).getParent()));

        return dicGasuSp1;
    }

    public static RealizationSphereEntity dictToRealizationSphereEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationSphereEntity dicRealizationSphereEntity = new RealizationSphereEntity();
        dicRealizationSphereEntity.setId(getDictId(dataRecord));
        dicRealizationSphereEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        return dicRealizationSphereEntity;
    }

    public static RealizationSphereEntity notImplemented(Object dataRecord) {
        RealizationSphereEntity realizationSphereEntity = new RealizationSphereEntity();
        realizationSphereEntity.setName("НЕ РЕАЛИЗОВАНО");
        return realizationSphereEntity;
    }

    public static RealizationSphereEntity dictToRealizationSphereEntity2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationSphereEntity dicRealizationSphereEntity = new RealizationSphereEntity();
        dicRealizationSphereEntity.setId(opt(() -> fieldIndex.get("DIC_GASU_GCHP_SPHERE").getExValues().get(0).getValue()));
        dicRealizationSphereEntity.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_SPHERE").getExValues().get(0).getTextRepresentation()));
        return dicRealizationSphereEntity;
    }

    public static RealizationSectorEntity dictToRealizationSectorEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationSectorEntity dicRealizationSectorEntity = new RealizationSectorEntity();
        dicRealizationSectorEntity.setId(getDictId(dataRecord));
        dicRealizationSectorEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        return dicRealizationSectorEntity;
    }

    public static RealizationSectorEntity dictToRealizationSectorEntity2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationSectorEntity dicRealizationSectorEntity = new RealizationSectorEntity();
        dicRealizationSectorEntity.setDicGasuGchpFormSphereSector(getDictId(dataRecord));
        dicRealizationSectorEntity.setId(opt(() -> fieldIndex.get("DIC_GASU_GCHP_SECTOR").getExValues().get(0).getValue()));
        dicRealizationSectorEntity.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_SECTOR").getExValues().get(0).getTextRepresentation()));

        return dicRealizationSectorEntity;
    }

    public static ObjectKind dictToObjectKind2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);
//
        ObjectKind dicObjectKind = new ObjectKind();
        dicObjectKind.setId(opt(() -> fieldIndex.get("DIC_GASU_GCHP_OBJECT_KIND").getExValues().get(0).getValue()));
        dicObjectKind.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_OBJECT_KIND").getExValues().get(0).getTextRepresentation()));
        return dicObjectKind;
    }

    public static ObjectKind dictToObjectKind(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ObjectKind dicObjectKind = new ObjectKind();
        dicObjectKind.setId(getDictId(dataRecord));
        dicObjectKind.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        dicObjectKind.setFormId(opt(() -> Long.parseLong(fieldIndex.get("FORM_ID").getExValues().get(0).getValue())));
        return dicObjectKind;
    }

    public static List<ObjectKind> dictListToObjectKinds(List<DictionaryDataRecordDescriptor> dataRecords) {
        if (dataRecords == null) return null;
        return dataRecords.stream().map(Converter::dictToObjectKind).collect(Collectors.toList());
    }

    public static AgreementSubject dictToAgreementSubject(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        AgreementSubject dicAgreementSubject = new AgreementSubject();
        dicAgreementSubject.setId(getDictId(dataRecord));
        dicAgreementSubject.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicAgreementSubject;
    }

    public static List<AgreementSubject> dictListToAgreementSubjects(List<DictionaryDataRecordDescriptor> dataRecords) {
        if (dataRecords == null) return null;
        return dataRecords.stream().map(Converter::dictToAgreementSubject).collect(Collectors.toList());
    }

    public static RealizationStatus dictToRealizationStatus(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RealizationStatus dicRealizationStatus = new RealizationStatus();
        dicRealizationStatus.setId(getDictId(dataRecord));
        dicRealizationStatus.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        dicRealizationStatus.setSortOrder(opt(() -> Long.parseLong(fieldIndex.get("SORT_ORDER").getExValues().get(0).getValue())));
        dicRealizationStatus.setUpdateProjectStatusExclusion(opt(() -> Boolean.parseBoolean(fieldIndex.get("UPDATE_PROJECT_STATUS_EXCLUSION").getExValues().get(0).getValue())));

        return dicRealizationStatus;
    }

    public static ProjectStatus dictToProjectStatus(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ProjectStatus dicProjectStatus = new ProjectStatus();
        dicProjectStatus.setId(getLongDictId(dataRecord));
        dicProjectStatus.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        dicProjectStatus.setColorCode(opt(() -> fieldIndex.get("COLOR_CODE").getExValues().get(0).getValue()));

        return dicProjectStatus;
    }

    public static RentObject dictToRentObjects(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        RentObject dicRealizationStatus = new RentObject();
        dicRealizationStatus.setId(getLongDictId(dataRecord));
        dicRealizationStatus.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicRealizationStatus;
    }

    public static AgreementGrounds dictToAgreementGrounds(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        AgreementGrounds dicAgreementGrounds = new AgreementGrounds();
        dicAgreementGrounds.setId(getLongDictId(dataRecord));
        dicAgreementGrounds.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_AGREEMENT_GROUNS").getExValues().get(0).getTextRepresentation()));

        return dicAgreementGrounds;
    }

    public static CompetitionResults dictToCompetitionResults(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        CompetitionResults dicCompetitionResults = new CompetitionResults();
        dicCompetitionResults.setId(getLongDictId(dataRecord));
        dicCompetitionResults.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicCompetitionResults;
    }

    public static CompetitionResultsSign dictToCompetitionResultsSign(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        CompetitionResultsSign dicCompetitionResultsSign = new CompetitionResultsSign();
        dicCompetitionResultsSign.setId(getLongDictId(dataRecord));
        dicCompetitionResultsSign.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicCompetitionResultsSign;
    }

    public static PrivatePartnerCostRecoveryMethodEntity dictToPrivatePartnerCostRecoveryMethodEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PrivatePartnerCostRecoveryMethodEntity dicPrivatePartnerCostRecoveryMethodEntity = new PrivatePartnerCostRecoveryMethodEntity();
        dicPrivatePartnerCostRecoveryMethodEntity.setId(getLongDictId(dataRecord));
        dicPrivatePartnerCostRecoveryMethodEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPrivatePartnerCostRecoveryMethodEntity;
    }

    public static MethodOfExecuteObligationEntity dictToMethodOfExecuteObligationEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        MethodOfExecuteObligationEntity dicMethodOfExecuteObligationEntity = new MethodOfExecuteObligationEntity();
        dicMethodOfExecuteObligationEntity.setId(getLongDictId(dataRecord));
        dicMethodOfExecuteObligationEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicMethodOfExecuteObligationEntity;
    }

    public static OtherGovSupportsEntity dictToOtherGovSupportsEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        OtherGovSupportsEntity dicOtherGovSupportsEntity = new OtherGovSupportsEntity();
        dicOtherGovSupportsEntity.setId(getLongDictId(dataRecord));
        dicOtherGovSupportsEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicOtherGovSupportsEntity;
    }

    public static AgreementsSetEntity dictToAgreementsSetEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        AgreementsSetEntity dicAgreementsSetEntity = new AgreementsSetEntity();
        dicAgreementsSetEntity.setId(getLongDictId(dataRecord));
        dicAgreementsSetEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicAgreementsSetEntity;
    }

    public static ContractPriceOffer dictToContractPriceOffer(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ContractPriceOffer dicContractPriceOffer = new ContractPriceOffer();
        dicContractPriceOffer.setId(getLongDictId(dataRecord));
        dicContractPriceOffer.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicContractPriceOffer;
    }

    public static WinnerContractPriceOfferEntity dictToWinnerContractPriceOfferEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        WinnerContractPriceOfferEntity dicWinnerContractPriceOfferEntity = new WinnerContractPriceOfferEntity();
        dicWinnerContractPriceOfferEntity.setId(getLongDictId(dataRecord));
        dicWinnerContractPriceOfferEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicWinnerContractPriceOfferEntity;
    }

    public static OpfEntity dictToOpfEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        OpfEntity dicOpfEntity = new OpfEntity();
        dicOpfEntity.setId(getDictId(dataRecord));
        dicOpfEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicOpfEntity;
    }

    public static EnsureMethod dictToEnsureMethod(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        EnsureMethod dicEnsureMethod = new EnsureMethod();
        dicEnsureMethod.setId(getDictId(dataRecord));
        dicEnsureMethod.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicEnsureMethod;
    }

    public static GovSupport dictToGovSupport(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        GovSupport dicGovSupport = new GovSupport();
        dicGovSupport.setId(getLongDictId(dataRecord));
        dicGovSupport.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicGovSupport;
    }

    public static List<GovSupport> dictListToGovSupports(List<DictionaryDataRecordDescriptor> dataRecords) {
        if (dataRecords == null) return null;
        return dataRecords.stream().map(Converter::dictToGovSupport).collect(Collectors.toList());
    }

    public static IRSourceEntity dictToIRSourceEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        IRSourceEntity dicIRSourceEntity = new IRSourceEntity();
        dicIRSourceEntity.setId(getLongDictId(dataRecord));
        dicIRSourceEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicIRSourceEntity;
    }

    public static PpResultOfPlacingEntity dictToPpResultsOfPlacingEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PpResultOfPlacingEntity dicPpResultOfPlacingEntity = new PpResultOfPlacingEntity();
        dicPpResultOfPlacingEntity.setId(getLongDictId(dataRecord));
        dicPpResultOfPlacingEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPpResultOfPlacingEntity;
    }

    public static TmCompositionOfCompensationGrantorFaultEntity dictToTmCompositionOfCompensationGrantorFaultEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        TmCompositionOfCompensationGrantorFaultEntity dictToTmCompositionOfCompensationGrantorFaultEntity = new TmCompositionOfCompensationGrantorFaultEntity();
        dictToTmCompositionOfCompensationGrantorFaultEntity.setId(getLongDictId(dataRecord));
        dictToTmCompositionOfCompensationGrantorFaultEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dictToTmCompositionOfCompensationGrantorFaultEntity;
    }

    public static IRLevelEntity dictToIRLevelEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        IRLevelEntity dicIRLevelEntity = new IRLevelEntity();
        dicIRLevelEntity.setId(getLongDictId(dataRecord));
        dicIRLevelEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicIRLevelEntity;
    }

    public static PaymentMethodEntity dictToPaymentMethodEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PaymentMethodEntity dicPaymentMethodEntity = new PaymentMethodEntity();
        dicPaymentMethodEntity.setId(getLongDictId(dataRecord));
        dicPaymentMethodEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPaymentMethodEntity;
    }

    public static TerminationCause dictToTerminationCause(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        TerminationCause dicTerminationCause = new TerminationCause();
        dicTerminationCause.setId(getLongDictId(dataRecord));
        dicTerminationCause.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicTerminationCause;
    }

    public static TerminationAftermathEntity dictToTerminationAftermathEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        TerminationAftermathEntity dicTerminationAftermathEntity = new TerminationAftermathEntity();
        dicTerminationAftermathEntity.setId(getLongDictId(dataRecord));
        dicTerminationAftermathEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicTerminationAftermathEntity;
    }

    public static ChangesReasonEntity dictToChangesReasonEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ChangesReasonEntity dicChangesReasonEntity = new ChangesReasonEntity();
        dicChangesReasonEntity.setId(getLongDictId(dataRecord));
        dicChangesReasonEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        return dicChangesReasonEntity;
    }

    public static ChangesReasonEntity dictToChangesReasonEntity2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        ChangesReasonEntity dicChangesReasonEntity = new ChangesReasonEntity();
        dicChangesReasonEntity.setId(getLongDictId(dataRecord));
        dicChangesReasonEntity.setName(opt(() -> fieldIndex.get("DIC_GASU_GCHP_CHANGES_REASON").getExValues().get(0).getValue()));

        return dicChangesReasonEntity;
    }

    public static Municipality dictToMunicipality(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        Municipality dicMunicipality = new Municipality();
        dicMunicipality.setId(opt(() -> fieldIndex.get("OKTMO_CODE").getExValues().get(0).getValue()));
        dicMunicipality.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        dicMunicipality.setRegionId(opt(() -> fieldIndex.get("DIC_GASU_SP1").getExValues().get(0).getValue()));
        dicMunicipality.setRegionTypeId(opt(() -> fieldIndex.get("DIC_GASU_OKTMO_TYPE_ID").getExValues().get(0).getValue()));
        return dicMunicipality;
    }

//    public static PublicPartnerEntity dictToPublicPartnerEntity(Object dataRecord) {
//        if (dataRecord == null) return null;
//        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);
//
//        PublicPartnerEntity dicPublicPartnerEntity = new PublicPartnerEntity();
//        dicPublicPartnerEntity.setId(getLongDictId(dataRecord));
//        dicPublicPartnerEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
//
//        return dicPublicPartnerEntity;
//    }

    public static CostRecoveryMethodEntity dictToCostRecoveryMethodEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        CostRecoveryMethodEntity dicCostRecoveryMethodEntity = new CostRecoveryMethodEntity();
        dicCostRecoveryMethodEntity.setId(getLongDictId(dataRecord));
        dicCostRecoveryMethodEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicCostRecoveryMethodEntity;
    }

    public static List<CostRecoveryMethodEntity> dictListToCostRecoveryMethods(List<DictionaryDataRecordDescriptor> dataRecords) {
        if (dataRecords == null) return null;
        return dataRecords.stream().map(Converter::dictToCostRecoveryMethodEntity).collect(Collectors.toList());
    }

    public static PriceOrderEntity dictToPriceOrderEntity(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PriceOrderEntity dicPriceOrderEntity = new PriceOrderEntity();
        dicPriceOrderEntity.setId(getLongDictId(dataRecord));
        dicPriceOrderEntity.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPriceOrderEntity;
    }

    public static FinIndDimType dictToFinIndDimType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        FinIndDimType dicFinIndDimType = new FinIndDimType();
        dicFinIndDimType.setId(getLongDictId(dataRecord));
        dicFinIndDimType.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicFinIndDimType;
    }

    public static BusinessModeType dictToBusinessModeType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        BusinessModeType dicBusinessModeType = new BusinessModeType();
        dicBusinessModeType.setId(getLongDictId(dataRecord));
        dicBusinessModeType.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicBusinessModeType;
    }

    public static CompetitionCriterion dictToCompetitionCriterion(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        CompetitionCriterion dicCompetitionCriterion = new CompetitionCriterion();
        dicCompetitionCriterion.setId(getLongDictId(dataRecord));
        dicCompetitionCriterion.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicCompetitionCriterion;
    }

    public static FinRequirement dictToFinRequirement(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        FinRequirement dicFinRequirement = new FinRequirement();
        dicFinRequirement.setId(getLongDictId(dataRecord));
        dicFinRequirement.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicFinRequirement;
    }

    public static NoFinRequirement dictToNonFinRequirement(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        NoFinRequirement dicNonFinRequirement = new NoFinRequirement();
        dicNonFinRequirement.setId(getLongDictId(dataRecord));
        dicNonFinRequirement.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicNonFinRequirement;
    }

    public static PrivateServiceType dictToPrivateServiceType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PrivateServiceType dicPrivateServiceType = new PrivateServiceType();
        dicPrivateServiceType.setId(getLongDictId(dataRecord));
        dicPrivateServiceType.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPrivateServiceType;
    }

    public static PublicServiceType dictToPublicServiceType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        PublicServiceType dicPublicServiceType = new PublicServiceType();
        dicPublicServiceType.setId(getLongDictId(dataRecord));
        dicPublicServiceType.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicPublicServiceType;
    }

    public static UnitOfMeasure dictToUnitOfMeasureType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        UnitOfMeasure um = new UnitOfMeasure();
        um.setId(getLongDictId(dataRecord));
        um.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));
        um.setOkei(opt(() -> fieldIndex.get("NUMBER_CODE").getExValues().get(0).getValue()));
        return um;
    }

    private static String getDictId(Object dataRecord) {
        return String.valueOf(((DictionaryDataRecordDescriptor) dataRecord).getId());
    }

    private static Long getLongDictId(Object dataRecord) {
        return ((DictionaryDataRecordDescriptor) dataRecord).getId();
    }

    private static Map<String, DataRecordEntry> createFieldIndex(Object dataRecord) {
        DictionaryDataRecordDescriptor descriptor = (DictionaryDataRecordDescriptor) dataRecord;

        return descriptor.getFields() == null ? new HashMap<>() :
                descriptor.getFields().stream()
                        .collect(Collectors.toMap(k -> k.getFieldDescriptor().getName().toUpperCase(), v -> v));
    }

    public static TaxCondition dictToTaxCondition(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        TaxCondition dicTaxCondition = new TaxCondition();
        dicTaxCondition.setId(getLongDictId(dataRecord));
        dicTaxCondition.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicTaxCondition;
    }

    public static MinimumGuarantIncomeType dictToMinimumGuarantIncomeType(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        MinimumGuarantIncomeType dicminimumGuarantIncomeType = new MinimumGuarantIncomeType();
        dicminimumGuarantIncomeType.setId(getLongDictId(dataRecord));
        dicminimumGuarantIncomeType.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicminimumGuarantIncomeType;
    }

    public static EnablingPayout dictToEnablingPayout(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        EnablingPayout dicEnablingPayout = new EnablingPayout();
        dicEnablingPayout.setId(getLongDictId(dataRecord));
        dicEnablingPayout.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicEnablingPayout;
    }

    public static Circumstance dictToCircumstance(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        Circumstance circumstance = new Circumstance();
        circumstance.setId(getLongDictId(dataRecord));
        circumstance.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return circumstance;
    }

    public static Circumstance dictToCircumstance2(Object dataRecord) {
        if (dataRecord == null) return null;
        Map<String, DataRecordEntry> fieldIndex = createFieldIndex(dataRecord);

        Circumstance dicCircumstance = new Circumstance();
        dicCircumstance.setId(getLongDictId(dataRecord));
        dicCircumstance.setName(opt(() -> fieldIndex.get("NAME").getExValues().get(0).getValue()));

        return dicCircumstance;
    }

}
