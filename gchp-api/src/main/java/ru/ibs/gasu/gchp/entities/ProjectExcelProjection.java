package ru.ibs.gasu.gchp.entities;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import ru.ibs.gasu.dictionaries.Dictionary;
import ru.ibs.gasu.dictionaries.service.DbDictConverter;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.service.CircumstanceDataConverter;
import ru.ibs.gasu.gchp.service.InvestmentsDataConverter;
import ru.ibs.gasu.gchp.service.excel.ExcelField;
import ru.ibs.gasu.gchp.service.excel.ExcelFields;
import ru.ibs.gasu.gchp.service.excel.ExcelService;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "project_details")
@Immutable
public class ProjectExcelProjection {

    @Id
    @ExcelField(
            name = "ID проекта",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    private Long id;

    // 1. Общие сведения
    /**
     * Наименование проекта
     */
    @ExcelField(
            name = "Наименование проекта",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(columnDefinition = "TEXT")
    private String giName;

    /**
     * Форма реализации
     */
    @ExcelField(
            name = "Форма реализации",
            converterMethod = "dictToRealizationForm",
            dictionary = Dictionary.DIC_GASU_GCHP_FORM,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_realization_form_id")
    String giRealizationForm;

    /**
     * Способ инициации проекта
     */
    @ExcelField(
            name = "Способ инициации проекта",
            converterMethod = "dictToInitiationMethod",
            dictionary = Dictionary.DIC_GASU_GCHP_IMPL_METHOD,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_initiation_method_id")
    String giInitiationMethod;

    /**
     * Уровень реализации проекта
     */
    @ExcelField(
            name = "Уровень реализации проекта",
            converterMethod = "dictToRealizationLevel",
            dictionary = Dictionary.DIC_GASU_GCHP_LVL,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_realization_level_id")
    String giRealizationLevel;

    /**
     * Субъект РФ
     */
    @ExcelField(
            name = "Субъект РФ",
            converterMethod = "dictToDicGasuSp1",
            dictionary = Dictionary.DIC_GASU_SP1,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_region_id")
    private String giRegion;

    /**
     * Муниципальное образование
     */
    @ExcelField(
            name = "Муниципальное образование",
            converterClass = DictionaryDataService.class,
            converterMethod = "findMunicipalityById",
            type = "CUSTOM_DICT",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_municipality_id")
    private String giMunicipality;

    /**
     * Концедент/Публичная сторона
     */
    @ExcelField(
            name = "Концедент/Публичная сторона",
            converterClass = DbDictConverter.class,
            converterMethod = "findSvrOrgBy",
            type = "DB",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_public_partner_id")
    private String giPublicPartner;

     // Для ролевой модели.
    @Column(name = "gi_public_partner_ogrn")
    private String giPublicPartnerOgrn;

    /**
     * Частный партнер/ Концессионер/ Исполнитель/ поставщик-инвестор/ совместное…
     */
    @ExcelField(
            name = "Концессионер/Частная сторона",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(length = 500)
    private String crConcessionaire;

    @Column(length = 500)
    private String giImplementer;

//    /**
//     * ОПФ
//     */
//    @ExcelField(
//            name = "ОПФ",
//            converterMethod = "dictToOpfEntity",
//            dictionary = Dictionary.DIC_GASU_GCHP_OPF
//    )
//    @Column(name = "gi_opf_id")
//    private String giOPF;

    /**
     * ИНН Концессионера (частного партнера)
     */
    @ExcelField(
            name = "ИНН",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    private String crConcessionaireInn;

//    /**
//     * Иностранный инвестор
//     */
//    @ExcelField(
//            name = "Иностранный инвестор"
//    )
//    private Boolean giIsForeignInvestor;

    /**
     * Сфера реализации
     */
    @ExcelField(
            name = "Сфера реализации",
            converterMethod = "dictToRealizationSphereEntity",
            dictionary = Dictionary.DIC_GASU_GCHP_SPHERE,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_realization_sphere_id")
    private String giRealizationSphere;

    /**
     * Отрасль реализации
     */
    @ExcelField(
            name = "Отрасль реализации",
            converterMethod = "dictToRealizationSectorEntity",
            dictionary = Dictionary.DIC_GASU_GCHP_SECTOR,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_realization_sector_id")
    private String giRealizationSector;

    /**
     * Вид объекта
     */
    @ExcelField(
            name = "Вид объекта",
            converterMethod = "dictToObjectKind",
            dictionary = Dictionary.DIC_GASU_GCHP_OBJ_KIND,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "PROJECT_OBJECTKIND",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "OBJECTKIND_ID")
    private Set<Long> giObjectType = new HashSet<>();

    /**
     * Место нахождения объекта
     */
    @ExcelField(
            name = "Место нахождения объекта",
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(length = 5000)
    private String giObjectLocation;
    /**
     * Предмет соглашения
     */
    @ExcelField(
            name = "Предмет соглашения",
            converterMethod = "dictToAgreementSubject",
            dictionary = Dictionary.DIC_GASU_GCHP_AGRMT_SUBJ,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "PROJECT_AGREEMENTSUBJECT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "AGREEMENTSUBJECT_ID")
    private Set<Long> giAgreementSubject = new HashSet<>();

    /**
     * Статус реализции
     */
    @ExcelField(
            name = "Стадия реализации проекта",
            converterMethod = "dictToRealizationStatus",
            dictionary = Dictionary.DIC_GASU_GCHP_IMPL_STATUS,
            color = ExcelService.SectionColor.GENERAL_INFO
    )
    @Column(name = "gi_realization_status_id")
    private String giRealizationStatus;

    /**
     * Статус проекта
     */
    @Column(name = "gi_project_status_id")
    private Long giProjectStatus;

    // 2. Описание объекта
    /**
     * Наименование объекта / товара
     */
    @ExcelField(
            name = "Наименование объекта",
            color = ExcelService.SectionColor.DESCRIPTION
    )
    @Column(columnDefinition = "TEXT")
    private String odObjectName;

    /**
     * Дата решения (распоряжения)
     */
    @ExcelField(
            name = "Дата решения (распоряжения)",
            color = ExcelService.SectionColor.PREPARATION
    )
    private Date ppActDate;


    /**
     * Планируемый объем инвестиций на стадии создания/реконструкции
     */
    @ExcelFields(
            fields = {
                    @ExcelField(
                            name = "Планируемый объем частных инвестиций на стадии создания/реконструкции",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId2Plan",
                            color = ExcelService.SectionColor.PREPARATION),
                    @ExcelField(
                            name = "Планируемый объем бюджетных расходов на стадии создания/реконструкции",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId3Plan",
                            color = ExcelService.SectionColor.PREPARATION),
                    @ExcelField(
                            name = "За счет средств федерального бюджета - план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId7Plan",
                            color = ExcelService.SectionColor.PREPARATION),
                    @ExcelField(
                            name = "За счет средств регионального бюджета - план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId8Plan",
                            color = ExcelService.SectionColor.PREPARATION),
                    @ExcelField(
                            name = "За счет средств местного бюджета - план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId9Plan",
                            color = ExcelService.SectionColor.PREPARATION)
            }
    )
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlanCreationInvestments ppCreationInvestmentPlanningAmount;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private RemainingDebt remainingDebt;

//    /**
//     * Планируемый объем инвестиций на стадии эксплуатации
//     */
//    @ExcelFields(
//            fields = {
//                    @ExcelField(
//                            name = "Планируемый объем бюджетных расходов на стадии эксплуатации",
//                            type = "COMPOUND",
//                            converterClass = InvestmentsDataConverter.class,
//                            converterMethod = "getId3Plan"),
//                    @ExcelField(
//                            name = "За счет средств федерального бюджета - план",
//                            type = "COMPOUND",
//                            converterClass = InvestmentsDataConverter.class,
//                            converterMethod = "getId7Plan"),
//                    @ExcelField(
//                            name = "За счет средств регионального бюджета - план",
//                            type = "COMPOUND",
//                            converterClass = InvestmentsDataConverter.class,
//                            converterMethod = "getId8Plan"),
//                    @ExcelField(
//                            name = "За счет средств местного бюджета - план",
//                            type = "COMPOUND",
//                            converterClass = InvestmentsDataConverter.class,
//                            converterMethod = "getId9Plan")
//            }
//    )
//    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    private PlanInvestments ppInvestmentPlanningAmount;

    /**
     * Ссылка на torgi.gov.ru, где размещено предложение о заключении соглашения
     */
    @ExcelField(
            name = "Ссылка на torgi.gov.ru, где размещено предложение о заключении соглашения",
            color = ExcelService.SectionColor.PREPARATION
    )
    private String ppConcludeAgreementLink;

    /**
     * Результаты размещения предложения на torgi.gov.ru
     */
    @ExcelField(
            name = "Результаты размещения предложения на torgi.gov.ru",
            converterMethod = "dictToPpResultsOfPlacingEntity",
            dictionary = Dictionary.DIC_GASU_GCHP_RESULTS_PLACING_AN_OFFER,
            color = ExcelService.SectionColor.PREPARATION
    )
    @Column(name = "pp_results_of_placing_id")
    private Long ppResultsOfPlacing;

    /**
     * Реквизиты и копия решения уполномоченного органа - ссылка на torgi.gov.ru
     */
    @ExcelField(
            name = "Ссылка на torgi.gov.ru, где размещено сообщение о конкурсе",
            color = ExcelService.SectionColor.PREPARATION
    )
    private String ppTorgiGovRuUrl;


    /**
     * Результаты проведения конкурса - Результаты проведения конкурса
     */
    @ExcelField(
            name = "Результаты проведения конкурса",
            converterMethod = "dictToCompetitionResults",
            dictionary = Dictionary.DIC_GASU_GCHP_COMPETITION_RESULTS,
            color = ExcelService.SectionColor.PREPARATION
    )
    @Column(name = "pp_competition_results_id")
    private Long ppCompetitionResults;

    /**
     * Дата заключения соглашения
     */
    @ExcelField(
            name = "Дата заключения соглашения",
            color = ExcelService.SectionColor.CREATION
    )
    private Date crAgreementStartDate;

    /**
     * Дата окончания действия соглашения
     */
    @ExcelField(
            name = "Дата окончания действия соглашения",
            color = ExcelService.SectionColor.CREATION
    )
    private Date crAgreementEndDate;

    /**
     * Срок действия соглашения, лет
     */
    @ExcelField(
            name = "Срок действия соглашения, лет",
            color = ExcelService.SectionColor.CREATION
    )
    private Double crAgreementValidity;

//    /**
//     * Объем финансирования
//     */
//    @ExcelField(
//            name = "Объем финансирования"
//    )
//    private Double crFinancialClosingValue;

    /**
     * Наличие соглашения между концедентом (публичным партнером), концессионером (частным партнером) и
     * финансирующей организацией (прямое соглашение)
     */
    @ExcelField(
            name = "Наличие соглашения между концедентом (публичным партнером), концессионером (частным партнером) и финансирующей организацией (прямое соглашение)",
            color = ExcelService.SectionColor.CREATION
    )
    private Boolean crFinancialClosingIsMutualAgreement;

//    /**
//     * Дата создания первого объекта соглашения - план
//     */
//    @ExcelField(
//            name = "Дата создания первого объекта соглашения - план"
//    )
//    private Date crFirstObjectCreationPlanDate;

//    /**
//     * Дата создания первого объекта соглашения - факт
//     */
//    @ExcelField(
//            name = "Дата создания первого объекта соглашения - факт"
//    )
//    private Date crFirstObjectCreationFactDate;

//    /**
//     * Дата создания последнего объекта соглашения - план
//     */
//    @ExcelField(
//            name = " Дата создания последнего объекта соглашения - план"
//    )
//    private Date crLastObjectCreationPlanDate;

//    /**
//     * Дата создания последнего объекта соглашения - факт
//     */
//    @ExcelField(
//            name = "Дата создания последнего объекта соглашения - факт"
//    )
//    private Date crLastObjectCreationFactDate;

    /**
     * Объем инвестиций на стадии создания
     */
    @ExcelFields(
            fields = {
                    @ExcelField(
                            name = "Объем частных инвестиций, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId2Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Объем частных инвестиций, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId2Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Собственные инвестиции, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId10Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Собственные инвестиции, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId10Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Заемные средства, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId11Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Заемные средства, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId11Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Объем бюджетных расходов, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId3Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "Объем бюджетных расходов, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId3Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств федерального бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId7Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств федерального бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId7Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств регионального бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId8Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств регионального бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId8Fact",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств местного бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId9Plan",
                            color = ExcelService.SectionColor.CREATION),
                    @ExcelField(
                            name = "За счет средств местного бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId9Fact",
                            color = ExcelService.SectionColor.CREATION)
            }
    )
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CreationInvestments crInvestmentCreationAmount;

    /**
     * Договор о предоставлении земельного участка - Обеспечение исполнения обязательств на этапе создания
     */
    @ExcelField(
            name = "Обеспечение исполнения обязательств на этапе создания",
            color = ExcelService.SectionColor.CREATION
    )
    private Boolean crIsObligationExecuteOnCreationStage;

    // 5. Эксплуатация.
    /**
     * Дата ввода последнего объекта в эксплуатацию - планируемая дата
     */
    @ExcelField(
            name = "Дата ввода последнего объекта в эксплуатацию - планируемая дата",
            color = ExcelService.SectionColor.EXPLOITATION
    )
    private Date exLastObjectPlanDate;

    /**
     * Дата ввода последнего объекта в эксплуатацию - фактическая дата
     */
    @ExcelField(
            name = "Дата ввода последнего объекта в эксплуатацию - фактическая дата",
            color = ExcelService.SectionColor.EXPLOITATION
    )
    private Date exLastObjectFactDate;

    /**
     * Объем инвестиций на этапе эксплуатации
     */
    @ExcelFields(
            fields = {
                    @ExcelField(
                            name = "Объем бюджетных расходов, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId3Plan",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "Объем бюджетных расходов, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId3Fact",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств федерального бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId7Plan",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств федерального бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId7Fact",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств регионального бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId8Plan",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств регионального бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId8Fact",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств местного бюджета, план",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId9Plan",
                            color = ExcelService.SectionColor.EXPLOITATION),
                    @ExcelField(
                            name = "За счет средств местного бюджета, факт",
                            type = "COMPOUND",
                            converterClass = InvestmentsDataConverter.class,
                            converterMethod = "getId9Fact",
                            color = ExcelService.SectionColor.EXPLOITATION)
            }
    )
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExploitationInvestments exInvestmentExploitationAmount;

    /**
     * Обеспечение исполнения обязательств на этапе эксплуатации
     */
    @ExcelField(
            name = "Обеспечение исполнения обязательств на этапе эксплуатации",
            color = ExcelService.SectionColor.EXPLOITATION
    )
    private Boolean exIsObligationExecutingOnOperationPhase;

    // 6. Прекращение
    /**
     * Причина прекращения
     */
    @ExcelField(
            name = "Причина прекращения",
            converterMethod = "dictToTerminationCause",
            dictionary = Dictionary.DIC_GASU_GCHP_TERMINATION_CAUSE,
            color = ExcelService.SectionColor.TERMINATE
    )
    @Column(name = "tm_termination_cause_id")
    private Long tmCause;

//    /**
//     * Дата прекращения соглашения - планируемая дата
//     */
//    @ExcelField(
//            name = "Дата прекращения соглашения - планируемая дата"
//    )
//    private Date tmActPlanDate;

    /**
     * Дата прекращения соглашения - фактическая дата
     */
    @ExcelField(
            name = "Дата прекращения соглашения - фактическая дата",
            color = ExcelService.SectionColor.TERMINATE
    )
    private Date tmActFactDate;

    /**
     * Соглашение сторон - Концедентом (публичным партнером) выплачена компенсация концессионеру при расторжении соглашения
     */
    @ExcelField(
            name = "Соглашение сторон - Концедентом (публичным партнером) выплачена компенсация концессионеру при расторжении соглашения",
            color = ExcelService.SectionColor.TERMINATE
    )
    private Boolean tmIsCompensationPayed;

    /**
     * Соглашение сторон - Сумма компенсации
     */
    @ExcelField(
            name = "Соглашение сторон - Сумма компенсации",
            color = ExcelService.SectionColor.TERMINATE
    )
    private BigDecimal tmCompensationValue;


    @ExcelField(
            name = "Предельная сумма компенсации концессионеру/частному партнеру по обеспечению его минимального гарантированного дохода",
            type = "COMPOUND",
            converterClass = InvestmentsDataConverter.class,
            converterMethod = "getCbcInvestments1",
            color = ExcelService.SectionColor.CBC)
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments1 cbcInvestments1;

    @ExcelField(
            name = "Размер фактически выплаченной компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода",
            type = "COMPOUND",
            converterClass = InvestmentsDataConverter.class,
            converterMethod = "getCbcInvestments2",
            color = ExcelService.SectionColor.CBC)
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments2 cbcInvestments2;


    @ExcelField(
            name = "Размер фактически выплаченной концессионеру/частному партнеру компенсации недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг",
            type = "COMPOUND",
            converterClass = InvestmentsDataConverter.class,
            converterMethod = "getCbcInvestments3",
            color = ExcelService.SectionColor.CBC)
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments3 cbcInvestments3;

    @ExcelField(
            name = "Размер фактически выплаченной компенсации концессионеру/частному партнеру льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента",
            type = "COMPOUND",
            converterClass = InvestmentsDataConverter.class,
            converterMethod = "getCbcInvestments4",
            color = ExcelService.SectionColor.CBC)
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments4 cbcInvestments4;


    @ExcelField(
            name = "Предельная сумма компенсации концессионеру/частному партнеру его дополнительных расходов/недополученных доходов по соглашению",
            type = "COMPOUND",
            converterClass = CircumstanceDataConverter.class,
            converterMethod = "getId1Sum",
            color = ExcelService.SectionColor.CBC)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_field = 'cbcLimitCompensationAdditionalCostsAmount'")
    private List<CircumstanceStageIndicator> cbcLimitCompensationAdditionalCostsAmount;

    @ExcelField(
            name = "Размер фактически выплаченной компенсации концессионеру/частному партнеру его дополнительных расходов/недополученных доходов по соглашению",
            type = "COMPOUND",
            converterClass = CircumstanceDataConverter.class,
            converterMethod = "getId1Sum",
            color = ExcelService.SectionColor.CBC)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_field = 'cbcCircumstancesAdditionalCostsAmount'")
    private List<CircumstanceStageIndicator> cbcCircumstancesAdditionalCostsAmount;

    // 7. Изменение условий
    /**
     * В соглашение (контракт) внесены изменения
     */
    @ExcelField(
            name = "В соглашение (контракт) внесены изменения",
            color = ExcelService.SectionColor.CHANGE_CONDITIONS
    )
    private Boolean ccIsChangesMade;

    /**
     * Изменение условий соглашения (контракта)
     */
    @ExcelField(
            name = "Изменение условий соглашения (контракта)",
            converterMethod = "dictToChangesReasonEntity",
            dictionary = Dictionary.DIC_GASU_GCHP_CHANGES_REASON,
            color = ExcelService.SectionColor.CHANGE_CONDITIONS
    )
    @Column(name = "cc_changes_reason_id")
    private Long ccReason;

    @Column
    private Long version;

    @Column(name = "CREATE_DATE")
    @CreatedDate
    private Date createDate;

    @ExcelField(name = "Дата последнего изменения по проекту")
    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    private Date updateDate;

    @ExcelField(name = "Дата первичного подписания информации по проекту")
    @Formula("(select h.update_date from gchp.project_details_his h where h.id = id and h.published = true order by h.rev asc limit 1)")
    private Date firstSignDate;

    @Column(name = "CREATE_USER_ID")
    @CreatedBy
    private Long createUserId;

    @Column(name = "UPDATE_USER_ID")
    @LastModifiedBy
    private Long updateUserId;

    private Boolean obsolete = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGiName() {
        return giName;
    }

    public void setGiName(String giName) {
        this.giName = giName;
    }

    public String getGiRealizationForm() {
        return giRealizationForm;
    }

    public void setGiRealizationForm(String giRealizationForm) {
        this.giRealizationForm = giRealizationForm;
    }

    public String getGiInitiationMethod() {
        return giInitiationMethod;
    }

    public void setGiInitiationMethod(String giInitiationMethod) {
        this.giInitiationMethod = giInitiationMethod;
    }

    public String getGiRealizationLevel() {
        return giRealizationLevel;
    }

    public void setGiRealizationLevel(String giRealizationLevel) {
        this.giRealizationLevel = giRealizationLevel;
    }

    public String getGiRegion() {
        return giRegion;
    }

    public void setGiRegion(String giRegion) {
        this.giRegion = giRegion;
    }

    public String getGiMunicipality() {
        return giMunicipality;
    }

    public void setGiMunicipality(String giMunicipality) {
        this.giMunicipality = giMunicipality;
    }

    public String getGiPublicPartner() {
        return giPublicPartner;
    }

    public void setGiPublicPartner(String giPublicPartner) {
        this.giPublicPartner = giPublicPartner;
    }

    public String getGiImplementer() {
        return giImplementer;
    }

    public void setGiImplementer(String giImplementer) {
        this.giImplementer = giImplementer;
    }

    public String getCrConcessionaire() {
        return crConcessionaire;
    }

    public void setCrConcessionaire(String crConcessionaire) {
        this.crConcessionaire = crConcessionaire;
    }

//    public String getGiOPF() {
//        return giOPF;
//    }
//
//    public void setGiOPF(String giOPF) {
//        this.giOPF = giOPF;
//    }

    public String getCrConcessionaireInn() {
        return crConcessionaireInn;
    }

    public void setCrConcessionaireInn(String crConcessionaireInn) {
        this.crConcessionaireInn = crConcessionaireInn;
    }


//    public Boolean getGiIsForeignInvestor() {
//        return giIsForeignInvestor;
//    }
//
//    public void setGiIsForeignInvestor(Boolean giIsForeignInvestor) {
//        this.giIsForeignInvestor = giIsForeignInvestor;
//    }

    public String getGiRealizationSphere() {
        return giRealizationSphere;
    }

    public void setGiRealizationSphere(String giRealizationSphere) {
        this.giRealizationSphere = giRealizationSphere;
    }

    public String getGiRealizationSector() {
        return giRealizationSector;
    }

    public void setGiRealizationSector(String giRealizationSector) {
        this.giRealizationSector = giRealizationSector;
    }

    public Set<Long> getGiObjectType() {
        return giObjectType;
    }

    public void setGiObjectType(Set<Long> giObjectType) {
        this.giObjectType = giObjectType;
    }

    public String getGiObjectLocation() {
        return giObjectLocation;
    }

    public void setGiObjectLocation(String giObjectLocation) {
        this.giObjectLocation = giObjectLocation;
    }

    public Set<Long> getGiAgreementSubject() {
        return giAgreementSubject;
    }

    public void setGiAgreementSubject(Set<Long> giAgreementSubject) {
        this.giAgreementSubject = giAgreementSubject;
    }

    public String getGiRealizationStatus() {
        return giRealizationStatus;
    }

    public void setGiRealizationStatus(String giRealizationStatus) {
        this.giRealizationStatus = giRealizationStatus;
    }

    public String getOdObjectName() {
        return odObjectName;
    }

    public void setOdObjectName(String odObjectName) {
        this.odObjectName = odObjectName;
    }

    public Date getPpActDate() {
        return ppActDate;
    }

    public void setPpActDate(Date ppActDate) {
        this.ppActDate = ppActDate;
    }

    public PlanCreationInvestments getPpCreationInvestmentPlanningAmount() {
        return ppCreationInvestmentPlanningAmount;
    }

    public void setPpCreationInvestmentPlanningAmount(PlanCreationInvestments ppCreationInvestmentPlanningAmount) {
        this.ppCreationInvestmentPlanningAmount = ppCreationInvestmentPlanningAmount;
    }

    public CbcInvestments1 getCbcInvestments1() {
        return cbcInvestments1;
    }

    public void setCbcInvestments1(CbcInvestments1 cbcInvestments1) {
        this.cbcInvestments1 = cbcInvestments1;
    }

    public CbcInvestments2 getCbcInvestments2() {
        return cbcInvestments2;
    }

    public void setCbcInvestments2(CbcInvestments2 cbcInvestments2) {
        this.cbcInvestments2 = cbcInvestments2;
    }

    public CbcInvestments3 getCbcInvestments3() {
        return cbcInvestments3;
    }

    public void setCbcInvestments3(CbcInvestments3 cbcInvestments3) {
        this.cbcInvestments3 = cbcInvestments3;
    }

    public CbcInvestments4 getCbcInvestments4() {
        return cbcInvestments4;
    }

    public void setCbcInvestments4(CbcInvestments4 cbcInvestments4) {
        this.cbcInvestments4 = cbcInvestments4;
    }

    public RemainingDebt getRemainingDebt() {
        return remainingDebt;
    }

    public void setRemainingDebt(RemainingDebt remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    //    public PlanInvestments getPpInvestmentPlanningAmount() {
//        return ppInvestmentPlanningAmount;
//    }
//
//    public void setPpInvestmentPlanningAmount(PlanInvestments ppInvestmentPlanningAmount) {
//        this.ppInvestmentPlanningAmount = ppInvestmentPlanningAmount;
//    }


    public String getPpConcludeAgreementLink() {
        return ppConcludeAgreementLink;
    }

    public void setPpConcludeAgreementLink(String ppConcludeAgreementLink) {
        this.ppConcludeAgreementLink = ppConcludeAgreementLink;
    }

    public Long getPpResultsOfPlacing() {
        return ppResultsOfPlacing;
    }

    public void setPpResultsOfPlacing(Long ppResultsOfPlacing) {
        this.ppResultsOfPlacing = ppResultsOfPlacing;
    }


    public Long getPpCompetitionResults() {
        return ppCompetitionResults;
    }

    public void setPpCompetitionResults(Long ppCompetitionResults) {
        this.ppCompetitionResults = ppCompetitionResults;
    }

    public String getPpTorgiGovRuUrl() {
        return ppTorgiGovRuUrl;
    }

    public void setPpTorgiGovRuUrl(String ppTorgiGovRuUrl) {
        this.ppTorgiGovRuUrl = ppTorgiGovRuUrl;
    }

    public Date getCrAgreementStartDate() {
        return crAgreementStartDate;
    }

    public void setCrAgreementStartDate(Date crAgreementStartDate) {
        this.crAgreementStartDate = crAgreementStartDate;
    }

    public Date getCrAgreementEndDate() {
        return crAgreementEndDate;
    }

    public void setCrAgreementEndDate(Date crAgreementEndDate) {
        this.crAgreementEndDate = crAgreementEndDate;
    }

    public Double getCrAgreementValidity() {
        return crAgreementValidity;
    }

    public void setCrAgreementValidity(Double crAgreementValidity) {
        this.crAgreementValidity = crAgreementValidity;
    }

    public Boolean getCrFinancialClosingIsMutualAgreement() {
        return crFinancialClosingIsMutualAgreement;
    }

    public void setCrFinancialClosingIsMutualAgreement(Boolean crFinancialClosingIsMutualAgreement) {
        this.crFinancialClosingIsMutualAgreement = crFinancialClosingIsMutualAgreement;
    }

//    public Date getCrFirstObjectCreationPlanDate() {
//        return crFirstObjectCreationPlanDate;
//    }
//
//    public void setCrFirstObjectCreationPlanDate(Date crFirstObjectCreationPlanDate) {
//        this.crFirstObjectCreationPlanDate = crFirstObjectCreationPlanDate;
//    }
//
//    public Date getCrFirstObjectCreationFactDate() {
//        return crFirstObjectCreationFactDate;
//    }
//
//    public void setCrFirstObjectCreationFactDate(Date crFirstObjectCreationFactDate) {
//        this.crFirstObjectCreationFactDate = crFirstObjectCreationFactDate;
//    }
//
//    public Date getCrLastObjectCreationPlanDate() {
//        return crLastObjectCreationPlanDate;
//    }
//
//    public void setCrLastObjectCreationPlanDate(Date crLastObjectCreationPlanDate) {
//        this.crLastObjectCreationPlanDate = crLastObjectCreationPlanDate;
//    }
//
//    public Date getCrLastObjectCreationFactDate() {
//        return crLastObjectCreationFactDate;
//    }
//
//    public void setCrLastObjectCreationFactDate(Date crLastObjectCreationFactDate) {
//        this.crLastObjectCreationFactDate = crLastObjectCreationFactDate;
//    }

    public CreationInvestments getCrInvestmentCreationAmount() {
        return crInvestmentCreationAmount;
    }

    public void setCrInvestmentCreationAmount(CreationInvestments crInvestmentCreationAmount) {
        this.crInvestmentCreationAmount = crInvestmentCreationAmount;
    }

    public Boolean getCrIsObligationExecuteOnCreationStage() {
        return crIsObligationExecuteOnCreationStage;
    }

    public void setCrIsObligationExecuteOnCreationStage(Boolean crIsObligationExecuteOnCreationStage) {
        this.crIsObligationExecuteOnCreationStage = crIsObligationExecuteOnCreationStage;
    }

    public Date getExLastObjectPlanDate() {
        return exLastObjectPlanDate;
    }

    public void setExLastObjectPlanDate(Date exLastObjectPlanDate) {
        this.exLastObjectPlanDate = exLastObjectPlanDate;
    }

    public Date getExLastObjectFactDate() {
        return exLastObjectFactDate;
    }

    public void setExLastObjectFactDate(Date exLastObjectFactDate) {
        this.exLastObjectFactDate = exLastObjectFactDate;
    }

    public ExploitationInvestments getExInvestmentExploitationAmount() {
        return exInvestmentExploitationAmount;
    }

    public void setExInvestmentExploitationAmount(ExploitationInvestments exInvestmentExploitationAmount) {
        this.exInvestmentExploitationAmount = exInvestmentExploitationAmount;
    }

    public Long getTmCause() {
        return tmCause;
    }

    public void setTmCause(Long tmCause) {
        this.tmCause = tmCause;
    }

//    public Date getTmActPlanDate() {
//        return tmActPlanDate;
//    }
//
//    public void setTmActPlanDate(Date tmActPlanDate) {
//        this.tmActPlanDate = tmActPlanDate;
//    }

    public Date getTmActFactDate() {
        return tmActFactDate;
    }

    public void setTmActFactDate(Date tmActFactDate) {
        this.tmActFactDate = tmActFactDate;
    }

    public Boolean getTmIsCompensationPayed() {
        return tmIsCompensationPayed;
    }

    public void setTmIsCompensationPayed(Boolean tmIsCompensationPayed) {
        this.tmIsCompensationPayed = tmIsCompensationPayed;
    }

    public BigDecimal getTmCompensationValue() {
        return tmCompensationValue;
    }

    public void setTmCompensationValue(BigDecimal tmCompensationValue) {
        this.tmCompensationValue = tmCompensationValue;
    }

    public Boolean getCcIsChangesMade() {
        return ccIsChangesMade;
    }

    public void setCcIsChangesMade(Boolean ccIsChangesMade) {
        this.ccIsChangesMade = ccIsChangesMade;
    }

    public Long getCcReason() {
        return ccReason;
    }

    public void setCcReason(Long ccReason) {
        this.ccReason = ccReason;
    }

    public Boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

//    public Double getCrFinancialClosingValue() {
//        return crFinancialClosingValue;
//    }
//
//    public void setCrFinancialClosingValue(Double crFinancialClosingValue) {
//        this.crFinancialClosingValue = crFinancialClosingValue;
//    }

    public Boolean getExIsObligationExecutingOnOperationPhase() {
        return exIsObligationExecutingOnOperationPhase;
    }

    public void setExIsObligationExecutingOnOperationPhase(Boolean exIsObligationExecutingOnOperationPhase) {
        this.exIsObligationExecutingOnOperationPhase = exIsObligationExecutingOnOperationPhase;
    }

    public String getGiPublicPartnerOgrn() {
        return giPublicPartnerOgrn;
    }

    public void setGiPublicPartnerOgrn(String giPublicPartnerOgrn) {
        this.giPublicPartnerOgrn = giPublicPartnerOgrn;
    }

    public Long getGiProjectStatus() {
        return giProjectStatus;
    }

    public void setGiProjectStatus(Long giProjectStatus) {
        this.giProjectStatus = giProjectStatus;
    }

    public Date getFirstSignDate() {
        return firstSignDate;
    }

    public void setFirstSignDate(Date firstSignDate) {
        this.firstSignDate = firstSignDate;
    }

    public List<CircumstanceStageIndicator> getCbcLimitCompensationAdditionalCostsAmount() {
        return cbcLimitCompensationAdditionalCostsAmount;
    }

    public void setCbcLimitCompensationAdditionalCostsAmount(List<CircumstanceStageIndicator> cbcLimitCompensationAdditionalCostsAmount) {
        this.cbcLimitCompensationAdditionalCostsAmount = cbcLimitCompensationAdditionalCostsAmount;
    }

    public List<CircumstanceStageIndicator> getCbcCircumstancesAdditionalCostsAmount() {
        return cbcCircumstancesAdditionalCostsAmount;
    }

    public void setCbcCircumstancesAdditionalCostsAmount(List<CircumstanceStageIndicator> cbcCircumstancesAdditionalCostsAmount) {
        this.cbcCircumstancesAdditionalCostsAmount = cbcCircumstancesAdditionalCostsAmount;
    }

}

