package ru.ibs.gasu.gchp.entities;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.ibs.gasu.gchp.entities.files.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Связь ManyToOne указывается только для тех полей, для которых требуется явное отображение
 * текстового значения на клиенте.
 */
@EntityListeners(AuditingEntityListener.class)
@Audited(withModifiedFlag = true)
@Entity
@Table(name = "project_details")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. Общие сведения
    /**
     * Наименование проекта
     */
    @Column(columnDefinition = "TEXT")
    private String giName;
    /**
     * Форма реализации
     */

    @Column(name = "gi_realization_form_id")
    String giRealizationForm;
    /* Способ инициации проекта */
    /**
     * Уровень реализации проекта
     */
    @Column(name = "gi_initiation_method_id")
    String giInitiationMethod;

    @Column(name = "gi_realization_level_id")
    String giRealizationLevel;
//    /**
//     *
//     */
//    @Column(name = "realization_form_id")
//    String giRealizationForm;
    /**
     * Российская Федерация является стороной соглашения
     */
    private Boolean giIsRFPartOfAgreement;
    /**
     * Регион является стороной соглашения
     */
    private Boolean giIsRegionPartOfAgreement;
    /**
     * Муниципальное образование является стороной соглашения
     */
    private Boolean giIsMunicipalityPartOfAgreement;
    /**
     * Субъект РФ
     */
    @Column(name = "gi_region_id")
    private String giRegion;

    /**
     * Муниципальное образование
     */
    @Column(name = "gi_municipality_id")
    private String giMunicipality;

    /**
     * Публичный партнер / Концедент / Заказчик
     */
    @Column(name = "gi_public_partner_id")
    private String giPublicPartner;

    /**
     * Публичный партнер / Концедент / Заказчик
     */
    @Column(name = "gi_public_partner_ogrn")
    private String giPublicPartnerOgrn;

    /**
     * ИНН Заказчика
     */
    private String giInn;
    /**
     * Балансодержатель
     */
    private String giBalanceHolder;
    /**
     * Частный партнер/ Концессионер/ Исполнитель/ поставщик-инвестор/ совместное…
     */
    @Column(length = 500)
    private String giImplementer;
    /**
     * ИНН Частного партнера/ Концессионера/ Исполнителя/ Поставщика-инвестора/…
     */
    private String giImplementerInn;
    /**
     * ОПФ
     */
    @Column(name = "gi_opf_id")
    private String giOPF;
    /**
     * Иностранный инвестор
     */
    private Boolean giIsForeignInvestor;
    /**
     * Субъект МСП
     */
    private Boolean giIsMcpSubject;
    /**
     * Специальная проектная компания
     */
    private Boolean giIsSpecialProjectCompany;
    /**
     * владеет недвижимым и (или) движимым имуществом, в отношении которого предполагается осуществление инвестиционных мероприятий
     */
    private Boolean giHasInvestmentProperty;
    /**
     * Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, до реализации проекта, %
     */
    private Double giPublicSharePercentage;
    /**
     * Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация
     */
    private Boolean giIsRFHasShare;
    /**
     * Сфера реализации
     */
    @Column(name = "gi_realization_sphere_id")
    private String giRealizationSphere;

    /**
     * Отрасль реализации
     */
    @Column(name = "gi_realization_sector_id")
    private String giRealizationSector;

    /**
     * Вид объекта
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_OBJECTKIND",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "OBJECTKIND_ID")
    private List<Long> giObjectType = new ArrayList<>();

    /**
     * Место нахождения объекта
     */
    @Column(length = 5000)
    private String giObjectLocation;
    /**
     * Предмет соглашения
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_AGREEMENTSUBJECT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "AGREEMENTSUBJECT_ID")
    private List<Long> giAgreementSubject = new ArrayList<>();

    /**
     * Статус реализции
     */
    @Column(name = "gi_realization_status_id")
    private String giRealizationStatus;

    /**
     * Загрузить заполненный шаблон
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompletedTemplateFile> giCompletedTemplateTextFVId = new ArrayList<>();

    /**
     * Статус проекта
     */
    @Column(name = "gi_project_status_id")
    private Long giProjectStatus;

    /**
     * Проект принудительно в статусе Черновик
     */
    private Boolean giAlwaysDraftStatus;

    // 2. Описание объекта
    /**
     * Наименование объекта / товара
     */
    @Column(columnDefinition = "TEXT")
    private String odObjectName;
    /**
     * Краткая характеристика объекта / товара
     */
    @Column(length = 5000)
    private String odObjectDescription;
    /**
     * Создаваемое концессионером/ частной стороной имущество, не входящее в Объект соглашения, остается у концессионера/ частной стороны
     */
    private Boolean odIsPropertyStaysWithPrivateSide;
    /**
     * В рамках концессионного соглашения Концессионеру/ частной стороне передается иное имущество
     */
    private Boolean odIsNewPropertyBeGivenToPrivateSide;
    /**
     * Осуществляемые арендатором отделимые улучшения объекта по истечении срока действия договора передаются арендодателю
     */
    private Boolean odIsObjectImprovementsGiveAway;
    /**
     * Объектом договора аренды является
     */
    @Column(name = "od_rent_object_id")
    private Long odRentObject;
    /**
     * Энергетический паспорт объекта/результаты энергообследования (Приложение из контракта с перечнем товаров)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentPassportFilesEntity> odRentPassportFileVersionId = new ArrayList<>();
    /**
     * Технико-экономические показатели
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechEconomicsObjectIndicator> odTechEconomicsObjectIndicators = new ArrayList<>();
    /**
     * План энергоэффективных мероприятий
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnergyEfficiencyPlan> odEnergyEfficiencyPlans = new ArrayList<>();

    // 3. Подготовка проекта
    /**
     * Объект соглашения  включен в перечень объектов, в отношении которых планируется заключение концессионных соглашений
     */
    private Boolean ppIsObjInListWithConcessionalAgreements;
    /**
     * Ссылка на перечень объектов
     */
    private String ppObjectsListUrl;
    /**
     * Предполагаемая частная сторона - наименование
     */
    private String ppSupposedPrivatePartnerName;
    /**
     * Предполагаемая частная сторона - ИНН
     */
    private String ppSupposedPrivatePartnerInn;
    /**
     * Иностранный инвестор
     */
    private Boolean ppIsForeignInvestor;
    /**
     * Субъект МСП
     */
    private Boolean ppIsMspSubject;
    /**
     * Комплекс соглашений, заключаемых в рамках проекта
     */
    @Column(name = "agreements_set_id")
    private Long ppAgreementsSet;
    /**
     * Предполагаемая дата заключения
     */
    private Date ppSupposedAgreementStartDate;
    /**
     * Предполагаемая дата окончания
     */
    private Date ppSupposedAgreementEndDate;
    /**
     * Предполагаемый срок действия соглашения, лет
     */
    private Double ppSupposedValidityYears;
    /**
     * Срок поставки товара/выполнения работ
     */
    private Date ppDeliveryTimeOfGoodsWorkDate;
    /**
     * Основание (проект соглашения)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectAgreementFile> ppProjectAgreementFileVersionId = new ArrayList<>();
    /**
     * Основание заключения соглашения
     */
    @Column(name = "pp_grounds_of_agreement_conclusion_id")
    private Long ppGroundsOfAgreementConclusion;
    /**
     * Номер решения (распоряжения)
     */
    private String ppActNumber;
    /**
     * Дата решения (распоряжения)
     */
    private Date ppActDate;
    /**
     * Текст решения (распоряжения)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActTextFile> ppActTextFileVersionId = new ArrayList<>();
    /**
     * Текст договора аренды/ постановления о присвоении ЕТО
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaseAgreementTextFile> ppLeaseAgreementTextFileVersionId = new ArrayList<>();
    /**
     * Проекту присвоен статус приоритетного/масштабного/ стратегического/комплексного инвестиционного проекта либо инвестиционного проекта, имеющего региональное значение
     */
    private Boolean ppProjectAssignedStatus;
    /**
     * Номер решения
     */
    private String ppDecisionNumber;
    /**
     * Дата решения
     */
    private Date ppDecisionDate;
    /**
     * Текст решения
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DecisionTextFile> ppDecisionTextFileVersionId = new ArrayList<>();
    /**
     * Дата розмещения предложения
     */
    private Date ppProposalPublishDate;
    /**
     * Текст предложения
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProposalTextFile> ppProposalTextFileVersionId = new ArrayList<>();
    /**
     * Реквизиты и копия решения уполномоченного органа - ссылка на torgi.gov.ru
     */
    @Column(length = 1000)
    private String ppTorgiGovRuUrl;
    /**
     * Реквизиты и копия решения уполномоченного органа - протокол
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProtocolFile> ppProtocolFileVersionId = new ArrayList<>();
    /**
     * Реквизиты и копия решения уполномоченного органа - Поступили заявки о готовности участия в конкурсе
     */
    private Boolean ppIsReadinessRequestReceived;
    /**
     * Реквизиты и копия решения уполномоченного органа - Принято решение о заключении соглашения
     */
    private Boolean ppIsDecisionMadeToConcludeAnAgreement;
    /**
     * Реквизиты и копия решения о заключении соглашения - номер решения
     */
    private String ppConcludeAgreementActNum;
    /**
     * Реквизиты и копия решения о заключении соглашения - дата решения
     */
    private Date ppConcludeAgreementActDate;
    /**
     * Реквизиты и копия решения о заключении соглашения - текст решения
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcludeAgreementFile> ppConcludeAgreementFvId = new ArrayList<>();
    /**
     * Срок инвестиционной стадии
     */
    private Date ppInvestmentStageDurationDate;
    /**
     * Реквизиты и копия решения о заключении соглашения - соглашение подписано
     */
    private Boolean ppConcludeAgreementIsSigned;
    /**
     * Реквизиты и копия решения о заключении соглашения - совместный конкурс
     */
    private Boolean ppConcludeAgreementIsJoint;
    /**
     * Реквизиты и копия решения о заключении соглашения - информация об иных проектах в совместном конкурсе
     */
    private String ppConcludeAgreementOtherPjInfo;
    /**
     * Реквизиты и копия решения о заключении соглашения - идентификаторы иных проектов в совместном конкурсе
     */
    private String ppConcludeAgreementOtherPjIdent;
    /**
     * Проведение конкурса - Дата окончания сбора заявок на участие в конкурсе план
     */
    private Date ppCompetitionBidCollEndPlanDate;
    /**
     * Проведение конкурса - Дата окончания сбора заявок на участие в конкурсе факт
     */
    private Date ppCompetitionBidCollEndFactDate;
    /**
     * Проведение конкурса - Дата окончания представления конкурсных предложений план
     */
    private Date ppCompetitionTenderOfferEndPlanDate;
    /**
     * Проведение конкурса - Дата окончания представления конкурсных предложений факт
     */
    private Date ppCompetitionTenderOfferEndFactDate;
    /**
     * Проведение конкурса - Дата  подведения итогов конкурса план
     */
    private Date ppCompetitionResultsPlanDate;
    /**
     * Проведение конкурса - Дата  подведения итогов конкурса факт
     */
    private Date ppCompetitionResultsFactDate;
    /**
     * Проведение конкурса - Текст сообщения о проведении конкурса
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionText> ppCompetitionTextFVId = new ArrayList<>();
    /**
     * Проведение конкурса - Конкурс является аукционом в электронной форме (только по ценовому критерию)
     */
    private Boolean ppCompetitionIsElAuction;
    /**
     * Проведение конкурса - Итоги конкурса подведены
     */
    private Boolean ppCompetitionHasResults;
    /**
     * Результаты проведения конкурса - Результаты проведения конкурса
     */
    @Column(name = "pp_competition_results_id")
    private Long ppCompetitionResults;
    /**
     * Результаты проведения конкурса - номер протокола
     */
    private String ppCompetitionResultsProtocolNum;
    /**
     * Результаты проведения конкурса - дата протокола
     */
    private Date ppCompetitionResultsProtocolDate;
    /**
     * Результаты проведения конкурса - количство участников
     */
    private Integer ppCompetitionResultsParticipantsNum;
    /**
     * Результаты проведения конкурса - Текст протокола/ решения о признании конкурса несостоявшимся
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionResultsProtocolFile> ppCompetitionResultsProtocolTextFvId = new ArrayList<>();
    /**
     * Результаты проведения конкурса - Текст конкурсной документации
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionResultsFile> ppCompetitionResultsDocFvId = new ArrayList<>();
    /**
     * Результаты проведения конкурса - статус подписания соглашения
     */
    @Column(name = "pp_competition_results_sign_status_id")
    private Long ppCompetitionResultsSignStatus;
    /**
     * Цена контракта - порядок расчета цены
     */
    @Column(name = "pp_contract_price_order_id")
    private Long ppContractPriceOrder;
    /**
     * Цена контракта - описание формулы
     */
    private String ppContractPriceFormula;
    /**
     * Цена контракта - цена контракта
     */
    private Double ppContractPricePrice;
    /**
     * Включая НДС
     */
    private Boolean ppNdsCheck;
    /**
     * тип измерения
     */
    @Column(name = "pp_measure_type_id")
    private Long ppMeasureType;
    /**
     * "Включая НДС" + Дата
     */
    private Long ppDateField;
//    /**
//     * Цена контракта - Метод
//     */
//    @Column(name = "pp_contract_price_method_id")
//    private Long ppContractPriceMethod;
    /**
     * Цена контракта - предложение заказчика
     */
    @Column(name = "pp_contract_price_offer_id")
    private Long ppContractPriceOffer;
    /**
     * Цена контракта - размер предложения
     */
    private Double ppContractPriceOfferValue;
    /**
     * Предложение победителя конкурса
     */
    @Column(name = "winner_contract_price_offer_id")
    private Long ppWinnerContractPriceOffer;
    /**
     * Цена контракта - начальный срок достижения экономии
     */
    private Date ppContractPriceSavingStartDate;
    /**
     * Цена контракта - конечный срок достижения экономии
     */
    private Date ppContractPriceSavingEndDate;
    /**
     * Планируемый объем инвестиций на стадии создания
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlanCreationInvestments ppCreationInvestmentPlanningAmount;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments1 cbcInvestments1;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments2 cbcInvestments2;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments3 cbcInvestments3;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CbcInvestments4 cbcInvestments4;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private RemainingDebt remainingDebt;

    /**
     * Планируемый объем инвестиций на стадии эксплуатации
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlanInvestments ppInvestmentPlanningAmount;
    /////////////////////test


/////////////////
    /**
     * Основание (финансовая модель)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialModelFile> ppFinancialModelFVId = new ArrayList<>();
    /**
     * Способ возмещения расходов
     */
    @Column(name = "pp_private_partner_cost_recovery_method_id")
    private Long ppPrivatePartnerCostRecoveryMethod;
    /**
     * Размер авансового платежа
     */
    private Double ppAdvancePaymentAmount;
    /**
     * Дата начала создания / реконструкции первого объекта соглашения
     */
    private Date ppFirstObjectOperationDate;
    /**
     * Дата ввода последнего объекта в эксплуатацию
     */
    private Date ppLastObjectCommissioningDate;
    /**
     * Основание (обосновывающие документы)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportingDocumentsFile> ppSupportingDocumentsFileVersionIds = new ArrayList<>();
    /**
     * Чистый дисконтированный расход бюджетных средств (Соглашение о ГЧП/МЧП)
     * Соглашение о ГЧП/МЧП
     */
    private Double ppBudgetExpendituresAgreementOnGchpMchp;
    /**
     * Чистый дисконтированный расход бюджетных средств (Государственный контракт)
     * Государственный контракт
     */
    private Double ppBudgetExpendituresGovContract;
    /**
     * Объем принимаемых публичным партнером обязательств в случае возникновения рисков (Соглашение о ГЧП/МЧП)
     * Соглашение о ГЧП/МЧП
     */
    private Double ppObligationsInCaseOfRisksAgreementOnGchpMchp;
    /**
     * Объем принимаемых публичным партнером обязательств в случае возникновения рисков (Государственный контракт)
     * Государственный контракт
     */
    private Double ppObligationsInCaseOfRisksGovContract;
    /**
     * Значение показателя оценки сравнительного преимущества
     */
    private Double ppIndicatorAssessmentComparativeAdvantage;
    /**
     * Заключение УО
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConclusionUOTextFile> ppConclusionUOTextFileVersionId = new ArrayList<>();
    /**
     * Финансовая модель
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialModelTextFile> ppFinancialModelTextFileVersionId = new ArrayList<>();
    /**
     * Обязательства частной стороны, предусмотренные соглашением
     */
    @Column(name = "method_of_execute_obligation_id")
    private Long ppMethodOfExecuteObligation;
    /**
     * Ссылка на пункт соглашения
     */
    private String ppLinkToClauseAgreement;
    /**
     * Договором предусмотрена ответственность частной стороны за несоблюдение вышеуказанных обязательств
     */
    private Boolean ppIsPrivateLiabilityProvided;
    /**
     * Ссылка на пункт соглашения
     */
    private String ppLinkToClauseAgreementLiabilityProvided;
    /**
     * Иные меры господдержки, предоставляемые в рамках СПИК
     */
    @Column(name = "other_gov_support_id")
    private Long ppStateSupportMeasuresSPIC;

    /**
     * Ссылка на torgi.gov.ru, где размещено предложение о заключении соглашения
     */
    private String ppConcludeAgreementLink;
    /**
     * Принято решение о заключении соглашения/ Наличие решения о реализации проекта
     */
    private Boolean ppImplementProject;
    /**
     * Результаты размещения предложения на torgi.gov.ru
     */
    @Column(name = "pp_results_of_placing_id")
    private Long ppResultsOfPlacing;

    // 4. Создание.
    /**
     * Комплекс соглашений, заключенных в рамках проекта
     * DIC_GASU_GCHP_AGREEMENT_SETS
     */
    @Column(name = "cr_agreement_set_id")
    private Long crAgreementComplex;

    /**
     * Дата заключения соглашения
     */
    private Date crAgreementStartDate;
    /**
     * Дата окончания действия соглашения
     */
    private Date crAgreementEndDate;
    /**
     * Срок действия соглашения, лет
     */
    private Double crAgreementValidity;

    /**
     * Срок поставки товара/выполнения работ
     */
    private Date crJobDoneTerm;

    /**
     * Начальный срок достижения экономии
     */
    private Date crSavingStartDate;

    /**
     * Конечный срок достижения экономии
     */
    private Date crSavingEndDate;

    /**
     * Срок инвестиционной стадии
     */
    private Double crInvestmentStageTerm;

    /**
     * Основангие (текст соглашения)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementTextFile> crAgreementTextFvId = new ArrayList<>();

    /**
     * Договором предусмотрена автоматиеская пролонгация после окончания его срока
     */
    private Boolean crIsAutoProlongationProvided;

    /**
     * Дата окончания действия договора после пролонгации
     */
    private Date crAgreementEndDateAfterProlongation;

    /**
     * Срок действия договора после пролонгации, лет
     */
    private Double crAgreementValidityAfterProlongation;

    /**
     * Текст договора
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreationAgreementFile> crAgreementTextFiles = new ArrayList<>();

    /**
     * Концессионер (частный партнер)
     */
    @Column(length = 500)
    private String crConcessionaire;

    /**
     * ОПФ
     * DIC_GASU_GCHP_OPF
     */
    @Column(name = "cr_opf_id")
    private Long crOpf;

    /**
     * ИНН Концессионера (частного партнера)
     */
    private String crConcessionaireInn;

    /**
     * Иностранный инвестор
     */
    private Boolean crIsForeignInvestor;

    /**
     * Субъукт МСП
     */
    private Boolean crIsMcpSubject;

    /**
     * Финансовое закрытие - Финансовое закрытие предусмотрено
     */
    private Boolean crFinancialClosingProvided;
    /**
     * Финансовое закрытие - Дата финансового закрытия
     */
    private Date crFinancialClosingDate;
    /**
     * Финансовое закрытие - Объем финансового закрытия
     */
    private Double crFinancialClosingValue;
    /**
     * Финансовое закрытие - Акт фмнансового закрытия
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialClosingActFile> crFinancialClosingActFVId = new ArrayList<>();
    /**
     * Наличие соглашения между концедентом (публичным партнером), концессионером (частным партнером) и
     * финансирующей организацией (прямое соглашение)
     */
    private Boolean crFinancialClosingIsMutualAgreement;
    /**
     * Дата создания первого объекта соглашения - план
     */
    private Date crFirstObjectCreationPlanDate;
    /**
     * Дата создания первого объекта соглашения - факт
     */
    private Date crFirstObjectCreationFactDate;
    /**
     * Основание (пункт соглашения, акт выполненных работ)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FirstObjectCompleteActFile> crFirstObjectCompleteActFVId = new ArrayList<>();

    /**
     * Регион является стороной соглашения
     */
    private Boolean crIsRegionPartyAgreement;

    /**
     * Создаётся несколько объектов соглашения
     */
    private Boolean crIsSeveralObjects;

    /**
     * Плановая дата завершения реконструкции/ввода в эксплуатацию первого объекта соглашения
     */
    private Long crFirstSeveralObjectPlanDate;

    /**
     * Первый объект соглашения создан (реконструирован) / введён в эксплуатацию
     */
    private Boolean crIsFirstSeveralObject;

    /**
     * Плановая дата завершения реконструкции/ввода в эксплуатацию последнего объекта
     * соглашения
     */
    private Long crLastSeveralObjectPlanDate;

    /**
     * Последний объект соглашения создан (реконструирован) / введён в эксплуатацию
     */
    private Boolean crIsLastSeveralObject;

    /**
     * Плановая дата завершения реконструкции/ввода
     * в эксплуатацию объекта соглашения
     */
    private Long crSeveralObjectPlanDate;

    /**
     * Объект соглашения (последний из объектов соглашения) создан (реконструирован) / введён в
     * эксплуатацию
     */
    private Boolean crIsSeveralObjectInOperation;

    /**
     * Пункт соглашения, в котором устанавливаются инвестиционные расходы концедента (публичного партнера)
     */
    private String crInvestCostsGrantor;


    /**
     * Установлены формулы или порядок индексации размера инвестиционных расходов концедента (публичного партнера)
     */
    private Boolean crIsFormulasInvestCosts;

    /**
     * Расчет планового размера инвестиционных расходов концедента (публичного партнера)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalcInvestCostsActFile> crCalcInvestCostsActFVId = new ArrayList<>();

    /**
     * Фактические расходы концедента/публично го партнера на предоставление концессионеру/частн ому партнеру земельных участков, подготовку территории (накопленным итогом с начала
     * срока действия соглашения)
     */
    private Double crActualCostsValue;

    /**
     * Средневзвешенная процентная ставка по привлекаемому
     * заемному финансированию
     */
    private Double crAverageInterestRateValue;

    /**
     * Дата создания последнего объекта соглашения - план
     */
    private Date crLastObjectCreationPlanDate;
    /**
     * Дата создания последнего объекта соглашения - факт
     */
    private Date crLastObjectCreationFactDate;
    /**
     * Основание (пункт соглашения, акт выполненных работ)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LastObjectCompleteActFile> crLastObjectCompleteActFVId = new ArrayList<>();
    /**
     * Объем инвестиций на стадии создания
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private CreationInvestments crInvestmentCreationAmount;

    /**
     * Год, в котором ожидается полное погашения привлеченного заемного финансирования
     */
    private String crExpectedRepaymentYear;

    /**
     * Остаток задолженности по привлеченному заемному финансированию, факт
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceOfDebt> crBalanceOfDebt = new ArrayList<>();
    /**
     * Для сортировки по общему объему инвестиций в списке проектов
     */
    @Formula("(select cr.plan from gchp.creation_investment_indicator cr where cr.creation_investment_id = id and cr.investment_indicator_type_id = 1)")
    @NotAudited
    private Double crInvestmentsTotal;

    /**
     * Для сортировки по объему частных инвестиций в списке проектов
     */
    @Formula("(select cr.plan from gchp.creation_investment_indicator cr where cr.creation_investment_id = id and cr.investment_indicator_type_id = 2)")
    @NotAudited
    private Double crInvestmentsPrivate;

    /**
     * Обосновывающие документы
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentVolumeStagOfCreationActFile> crInvestmentVolumeStagOfCreationActFVId = new ArrayList<>();
    /**
     * Концессионным соглашением предусмотрена передача концессионеру существующего на момент
     * заключения соглашения объекта соглашения
     */
    private Boolean crIsObjectTransferProvided;
    /**
     * Дата возникновения права владения и пользования объектом у частной стороны - планируемая
     */
    private Date crObjectRightsPlanDate;
    /**
     * Дата возникновения права владения и пользования объектом у частной стороны - фактическая
     */
    private Date crObjectRightsFactDate;
    /**
     * Основание (пункт соглашения, акт приема-передачи)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentsActFile> crActFVId = new ArrayList<>();
    /**
     * Возобновляемая банковская гарантия
     */
    private Boolean crIsRenewableBankGuarantee;
    /**
     * Размер банковской гарантии изменяется по годам
     */
    private Boolean crIsGuaranteeVariesByYear;
    /**
     * Размер банковской гарантии по годам
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private BankGuarantee crBankGuaranteeByYears;
    /**
     * Балансовая стоимость передаваемого объекта соглашения
     */
    private Double crObjectValue;
    /**
     * Основание (ссылка на текст соглашения)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentsLinkFile> crReferenceFVId = new ArrayList<>();
    // Права на земельный участок
    /**
     * Права на земельный участок - Соглашением предусмотрено предоставление земельного участка
     */
    private Boolean crLandProvided;
    /**
     * Права на земельный участок - Земельный участок находится в собственности Концедента (Публичного партнера)
     */
    private Boolean crLandIsConcessionaireOwner;
    /**
     * Договор о предоставлении земельного участка - плановая дата заключения
     */
    private Date crLandActStartPlanDate;
    /**
     * Договор о предоставлении земельного участка - фактическая дата заключения
     */
    private Date crLandActStartFactDate;
    /**
     * Договор о предоставлении земельного участка - плановый срок действия
     */
    private Date crLandActEndPlanDate;
    /**
     * Договор о предоставлении земельного участка - фактический срок действия
     */
    private Date crLandActEndFactDate;
    /**
     * Договор о предоставлении земельного участка - Основание (пункт соглашения, текст договора)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LandActFile> crLandActFVId = new ArrayList<>();
    /**
     * Договор о предоставлении земельного участка - Обеспечение исполнения обязательств на этапе создания
     */
    private Boolean crIsObligationExecuteOnCreationStage;
    /**
     * Договор о предоставлении земельного участка - Способы обеспечения обязательств частного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreationEnsureMethod> crEnsureMethods = new ArrayList<>();

    /**
     * Договор о предоставлении земельного участка - государственная поддержка, оказываемая проекту
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_GOVSUPPORT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "GOVSUPPORT_ID")
    private List<Long> crGovSupport = new ArrayList<>();

    /**
     * Договор о предоставлении земельного участка - тексты подтверждающих документов
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfirmationDocFile> crConfirmationDocFVId = new ArrayList<>();

    // 5. Эксплуатация.
    /**
     * Дата ввода последнего объекта в эксплуатацию - планируемая дата
     */
    private Date exLastObjectPlanDate;
    /**
     * Дата ввода последнего объекта в эксплуатацию - фактическая дата
     */
    private Date exLastObjectFactDate;
    /**
     * Основание (пункт соглашения, акт ввода в эксплуатацию)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LastObjectActFile> exLastObjectActFVId = new ArrayList<>();
    /**
     * Объем инвестиций на этапе эксплуатации
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExploitationInvestments exInvestmentExploitationAmount;
    /**
     * Обосновывающие документы
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentVolumeStagOfExploitationActFile> exInvestmentVolumeStagOfExploitationActFVId = new ArrayList<>();
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Cоглашением предусмотрено возмещение частных
     * инвестиций на этапе эксплуатации
     */
    private Boolean exIsInvestmentsRecoveryProvided;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExploitationInvestmentsRecovery exInvestmentExploitationRecoveryAmount;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Основание (финансовая модель)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentRecoveryFinancialModelFile> exInvestmentRecoveryFinancialModelFileVersionId = new ArrayList<>();
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Источник возмещения
     */
    @Column(name = "ex_ir_source_id")
    private Long exIRSource;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Уровень бюджета для возмещения
     */
    @Column(name = "ex_ir_level_id")
    private Long exIRLevel;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Обеспечение исполнения обязательств на этапе эксплуатации
     */
    private Boolean exIsObligationExecutingOnOperationPhase;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Способы обеспечения обязательств частного партнера
     */
    ////@Column(name = "ex_method_exec_pp_obligation_id")
    ////private Long exMethodOfExecOfPublicPartnerObligation;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Срок обеспечения
     */
    ////private Date exInvestmentRecoveryTerm;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации - Размер обеспечения
     */
    ////private Double exInvestmentRecoveryValue;

    /**
     * Способы обеспечения обязательств частного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationEnsureMethod> exEnsureMethods = new ArrayList<>();
    /**
     * Возмещение частных инвестиций на этапе эксплуатации -Возобновляемая банковская гарантия
     */
    private Boolean exIsRenewableBankGuarantee;
    /**
     * Возмещение частных инвестиций на этапе эксплуатации -Размер банковской гарантии изменяется по годам
     */
    private Boolean exIsGuaranteeVariesByYear;

    /**
     * Размер банковской гарантии по годам
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExBankGuarantee exBankGuaranteeByYears;

    /**
     * Соглашением предусмотрена концессионная (арендная) плата
     */
    private Boolean exIsConcessionPayProvideded;
    /**
     * Форма взимания платы
     */
    @Column(name = "ex_payment_form_id")
    private Long exPaymentForm;
    /**
     * Плата по годам
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExploitationPayment exPayment;

    /**
     * Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, после ввода объекта в эксплуатацию
     */
    private BigDecimal exPublicShareExpl;

    /**
     * Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация
     */
    private Boolean exHasPublicShareExpl;

    /**
     * Финансовая модель
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationFinModelFile> exFinModelFVIds = new ArrayList<>();

    /**
     * Способ возмещения расходов арендатора
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_COSTRECOVERYMETHOD",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "COSTRECOVERYMETHOD_ID")
    private List<Long> exCostRecoveryMethod = new ArrayList<>();

    /**
     * Описание механизма возмещения расходов
     */
    @Column(length = 5000)
    private String exCostRecoveryMechanism;

    /**
     * Обосновывающие документы
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationSupportDoclFile> exSupportDocFVIds = new ArrayList<>();

    /**
     * Плата по годам
     */
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExploitationCompensation exCompensation;

    /**
     * Обосновывающие документы (возмещение расходов)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationSupportCompensDoclFile> exSupportCompensDocFVIds = new ArrayList<>();

    /**
     * Дата возникновения права собственности у частной стороны, план
     */
    private Date exOwnPrivatePlanDate;

    /**
     * Дата возникновения права собственности у частной стороны, факт
     */
    private Date exOwnPrivateFactDate;

    /**
     * Дата возникновения права собственности у публичной стороны, план
     */
    private Date exOwnPublicPlanDate;

    /**
     * Дата возникновения права собственности у публичной стороны, факт
     */
    private Date exOwnPublicFactDate;

    /**
     * Пункт соглашения + ссылка на текст соглашения или доп соглашение
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationAgreementFile> exAgreementFVIds = new ArrayList<>();

    /**
     * Акт приема-передачи
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationAcceptActFile> exAcceptActFVIds = new ArrayList<>();

    /**
     * Начальный срок достижения экономии, план
     */
    private Date exStartAchEconPlanDate;

    /**
     * Конечный срок достижения экономии, план
     */
    private Date exEndAchEconPlanDate;

    /**
     * Начальный срок достижения экономии, факт
     */
    private Date exStartAchEconFactDate;

    /**
     * Конечный срок достижения экономии, факт
     */
    private Date exEndAchEconFactDate;

    /**
     * Акт о приемке ЭЭМ
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExploitationAcceptActAAMFile> exAcceptActAAMFVIds = new ArrayList<>();

    /**
     * Расчет планового размера расходов концедента (публичного партнера) на стадии эксплуатации
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExCalculationPlannedAmountFile> exCalculationPlannedAmountFVIds = new ArrayList<>();

    /**
     * Срок инвестиционной стадии, план
     */
    private Date exInvestStagePlanDate;

    /**
     * Срок инвестиционной стадии, факт
     */
    private Date exInvestStageFactDate;

    // 6. Прекращение
    /**
     * Причина прекращения
     */
    @Column(name = "tm_termination_cause_id")
    private Long tmCause;
    /**
     * Дата прекращения соглашения - планируемая дата
     */
    private Date tmActPlanDate;
    /**
     * Дата прекращения соглашения - фактическая дата
     */
    private Date tmActFactDate;
    /**
     * Соглашение сторон - Номер соглашения
     */
    private String tmActNumber;
    /**
     * Соглашение сторон - Дата соглашения
     */
    private Date tmActDate;
    /**
     * Соглашение сторон - Текст соглашения
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TerminationActFile> tmTextFileVersionId = new ArrayList<>();
    /**
     * Соглашение сторон - Описание причины расторжения
     */
    private String tmCauseDescription;
    /**
     * Соглашение сторон - Дата прекращения права владения и пользования у частной стороны - планинруемая
     */
    private Date tmPlanDate;
    /**
     * Соглашение сторон - Дата прекращения права владения и пользования у частной стороны - фактическая
     */
    private Date tmFactDate;
    /**
     * Соглашение сторон - Основание (пункт соглашения, акт приема-передачи)
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TerminationActTextFile> tmTaActTextFileVersionId = new ArrayList<>();
    /**
     * Соглашение сторон - Предусмотрено разделение права собственности на объект соглашения
     */
    private Boolean tmPropertyJointProvided;
    /**
     * Соглашение сторон - разделение права собственности % частной стороне
     */
    private Double tmPropertyJointPrivatePercent;
    /**
     * Соглашение сторон - разделение права собственности % публичной стороне
     */
    private Double tmPropertyJointPublicPercent;
    /**
     * Соглашение сторон - Концедентом (публичным партнером) выплачена компенсация концессионеру при расторжении соглашения
     */
    private Boolean tmIsCompensationPayed;
    /**
     * Соглашение сторон - Сумма компенсации
     */
    private BigDecimal tmCompensationValue;

    /**
     * Соглашение сторон - Расчет суммы компенсации
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TerminationCompensationFile> tmCompensationFVIds = new ArrayList<>();

    /**
     * последствия прекращения договора
     */
    private Long tmAftermath;
    /**
     * номер договора
     */
    private String tmContractNumber;
    /**
     * дата договора
     */
    private Date tmContractDate;
    /**
     * ID проекта
     */
    private Long tmProjectId;
    /**
     * Доля публичной стороны в капитале совместного юридического лица  (совместного предприятия), реализующего проект, после реализации проекта, %
     */
    private Double tmPublicShare;
    /**
     * Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация
     */
    private Boolean tmIsRfHasShare;

    /**
     * Описание иного вида компенсации
     */
    private String tmAnotherDescription;
    /**
     * Пункт (ы) соглашения
     */
    private String tmClausesOfAgreement;
    /**
     * Установлен предел компенсации концессионеру/частному партнеру при прекращении соглашения
     */
    private Boolean tmCompensationLimit;
    /**
     * Соглашение прекращено
     */
    private Boolean tmAgreementTerminated;
    /**
     * Предельная сумма компенсации концессионеру/частному  при прекращении соглашения, тыс.руб
     */
    private Double tmCompensationSum;
    /**
     * Включая НДС
     */
    private Boolean tmNdsCheck;
    /**
     * тип измерения
     */
    @Column(name = "tm_measure_type_id")
    private Long tmMeasureType;
    /**
     * "Включая НДС" + Дата
     */
    private Long tmDateField;
    /**
     * Состав компенсации при прекращении соглашения
     */
    private Double tmCompositionOfCompensation;

    /**
     * Состав компенсации при прекращении соглашения по вине концедента/публичного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompositionOfCompensation> tmCompositionOfCompensationView = new ArrayList<>();
    /**
     * Обосновывающие документы
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TerminationSupportingDocumentsFile> tmSupportingDocuments = new ArrayList<>();

    /**
     * Состав компенсации при прекращении соглашения по вине концедента/публичного партнера
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_COMPOSITIONOFCOMPENSATIONGRANTORFAULT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "tm_composition_of_compensation_grantor_fault_id")
    private List<Long> tmCompositionOfCompensationGrantorFault = new ArrayList<>();

    // 7. Изменение условий
    /**
     * В соглашение (контракт) внесены изменения
     */
    private Boolean ccIsChangesMade;
    /**
     * Изменение условий соглашения (контракта)
     */
    @Column(name = "cc_changes_reason_id")
    private Long ccReason;
    /**
     * Реквизиты и копия решения (соглашения) - Номер решения (соглашения)
     */
    private String ccActNumber;
    /**
     * Реквизиты и копия решения (соглашения) - Дата решения (соглашения)
     */
    private Date ccActDate;
    /**
     * Реквизиты и копия решения (соглашения) - Текст решения (соглашения)
     */

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeTextFile> ccTextFileVersionId = new ArrayList<>();

    /**
     * Данные ЭЦП
     */
    @Column(name = "C_CERTIFICATE")
    private String cert;

    @Column(name = "SIGNATURE")
    private String signature;

    @Column(name = "S_CERTIFICATE_SNILS", length = 200)
    private String snils;

    // 8. Дополнительно
    /**
     * События проекта
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> adsEvents = new ArrayList<>();
    /**
     * Привлечение организаций к реализации проекта
     */
    private Boolean adsIsThirdPartyOrgsProvided;
    /**
     * Региональный инвестиционный проект
     */
    private Boolean adsIsRegInvestmentProject;

    /**
     * Налоговые льготы
     */
    private Boolean adsHasIncomeTax;

    /**
     * Инвестиции в объект
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentInObjectMainIndicator> feiInvestmentsInObject = new ArrayList<>();

    /**
     * Расходы концессионера/частного партнера на эксплуатационной стадии
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationalCostsIndicator> feiOperationalCosts = new ArrayList<>();

    /**
     * Налоговые условия
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaxConditionIndicator> feiTaxCondition = new ArrayList<>();

    /**
     * Показатели выручки по проекту
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RevenueServiceIndicator> feiRevenue = new ArrayList<>();

    /**
     * Существуют налоговые льготы
     */
    private Boolean feiTaxIncentivesExist;

    /**
     * Остаточная стоимость передаваемого концедентом/ публичным партнером концессионеру/частному партнеру имущества на дату заключения соглашения
     */
    private Double feiResidualValue;

    /**
     * Средний срок эксплуатации переданного концессионеру/ частному партнеру имущества, существующего на дату заключения соглашения
     */
    private Double feiAverageServiceLife;

    /**
     * Дата внесения данных о прогнозных значениях финансово-экономических показателях реализации соглашения
     */
    private Long feiForecastValuesDate;

    /**
     * Установлены формулы или порядок индексации размера расходов концедента (публичного партнера) на стадии эксплуатации
     */
    private Boolean exFormulasOrIndexingOrderEstablished;

    /**
     * Пункт соглашения, в котором устанавливаются расходы концедента (публичного партнера) на стадии эксплуатации
     */
    private String exGrantorExpenses;

    /**
     * Налоговые льготы ставка
     */
    private Long adsIncomeTaxRate;

    /**
     * Земельный налог
     */
    private Boolean adsHasLandTax;

    /**
     * Земельный налог ставка
     */
    private Long adsLandTaxRate;

    /**
     * Налог на имущество
     */
    private Boolean adsHasPropertyTax;

    /**
     * Налог на имущество ставка
     */
    private Long adsPropertyTaxRate;

    /**
     * Уточнение льготы
     */
    private Boolean adsHasBenefitClarificationTax;

    /**
     * Уточнение льготы ставка
     */
    private Long adsBenefitClarificationRate;

    /**
     * Уточнение льготы описание
     */
    private String adsBenefitDescription;

    /**
     * Осуществление казначейского сопровождения
     */
    private Boolean adsIsTreasurySupport;
    /**
     * Решение
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdsDecisionTextFile> adsDecisionTextFileId = new ArrayList<>();
    /**
     * Привлеченные организации к реализации проекта со стороны частного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrivatePartnerThirdPartyOrg> adsPrivatePartnerThirdPartyOrgs = new ArrayList<>();
    /**
     * Привлеченные организации к реализации проекта со стороны публичного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicPartnerThirdPartyOrg> adsPublicPartnerThirdPartyOrgs = new ArrayList<>();
    /**
     * Плановое значение чистой приведенной стоимости проекта (NPV)
     */
    private Double adsNpv;
    /**
     * Плановое значение внутренней нормы доходности проекта (IRR)
     */
    private Double adsIrr;
    /**
     * Период окупаемости проекта (простой), лет
     */
    private Double adsPb;
    /**
     * Период окупаемости проекта (дисконтированный), лет
     */
    private Double adsPbDiscounted;
    /**
     * Прибыль до вычета процентов, налогов и амортизации (EBITDA)
     */
    private Double adsEbidta;
    /**
     * Средневзвешенная стоимость капитала (WACC)
     */
    private Double adsWacc;
    /**
     * Налоговые льготы
     */
    private Long adsTaxRelief;
    /**
     * Санкции в отношении частного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sanction> adsSanctions = new ArrayList<>();
    /**
     * Судебная активность по проекту
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JudicialActivity> adsJudicialActivities = new ArrayList<>();
    /**
     * Концессионер - ОПФ
     */
    @Column(name = "ads_opf_id")
    private Long adsConcessionaireOpf;
    /**
     * Концессионер - наименование
     */
    @Column(length = 500)
    private String adsConcessionaireName;
    /**
     * Концессионер - ИНН
     */
    private String adsConcessionaireInn;
    /**
     * Концессионер - режим
     */
    @Column(name = "ads_regime_id")
    private String adsConcessionaireRegime;
    /**
     * Концессионер - кредитный рейтинг
     */
    private String adsConcessionaireCreditRating;
    /**
     * Концессионер - кредитный рейтинг дата присвоения
     */
    private Date adsConcessionaireCreditRatingStartDate;
    /**
     * Концессионер - кредитный рейтинг дата отзыва
     */
    private Date adsConcessionaireCreditRatingEndDate;
    /**
     * Концессионер - кредитный рейтинг агенство
     */
    private String adsConcessionaireCreditRatingAgency;
    /**
     * структура владения частным партнером
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnershipStructure> adsOwnershipStructures = new ArrayList<>();

    /**
     * структура финансирования проекта
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialStructure> adsFinancialStructure = new ArrayList<>();
    /**
     * Критерии качественных инфраструктурных инвестиций - да/нет
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentsCriteriaBoolean> adsInvestmentBoolCriterias = new ArrayList<>();
    /**
     * Критерии качественных инфраструктурных инвестиций - Доля непредвиденных расходов от всех расходов на создание
     */
    private Double adsUnforeseenExpencesShare;
    /**
     * Количество рабочих мест
     */
    private Double adsWorkPlacesCount;
    /**
     * Критерии качественных инфраструктурных инвестиций - Доля непредвиденных расходов от всех расходов на создание комментарий
     */
    private String adsUnforeseenExpencesShareComment;

    /**
     * Критерии конкурса
     */
//    @ManyToMany
//    @JoinTable(
//            name = "PROJECT_COMPETITIONCRITERIA",
//            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "COMPETITIONCRITERIA_ID", referencedColumnName = "id"))
//    private List<CompetitionCriteria> adsCompetitionCriteria = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_COMPETITIONCRITERIA",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "COMPETITIONCRITERIA_ID")
    private List<Long> adsCompetitionCriteria = new ArrayList<>();
    /**
     * Финансовые требования к участникам конкурса
     */
//    @ManyToMany
//    @JoinTable(
//            name = "PROJECT_FINANCIALREQUIREMENT",
//            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "FINANCIALREQUIREMENT_ID", referencedColumnName = "id"))
//    private List<FinancialRequirement> adsFinancialRequirement = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_FINANCIALREQUIREMENT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "FINANCIALREQUIREMENT_ID")
    private List<Long> adsFinancialRequirement = new ArrayList<>();
    /**
     * Нефинансовые требования к участникам конкурса
     */
//    @ManyToMany
//    @JoinTable(
//            name = "PROJECT_NONFINANCIALREQUIREMENT",
//            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "NONFINANCIALREQUIREMENT_ID", referencedColumnName = "id"))
//    private List<NonFinancialRequirement> adsNonFinancialRequirements = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_NONFINANCIALREQUIREMENT",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "NONFINANCIALREQUIREMENT_ID")
    private List<Long> adsNonFinancialRequirements = new ArrayList<>();

    /**
     * Коментарий
     */
    @Column(length = 5000)
    private String cmComment;

    /**
     * Контактные данные
     */
    @Column(length = 5000)
    private String cmContacts;

    //11 Условные бюджетные обязательства

    /**
     * Условные бюджетные обязательства по проекту отсутствуют
     */

    private Boolean cbcProjectBudgetObligationMissing;

    /**
     * Наличие в соглашении условий по выплате недополученных доходов,
     * обеспечивающих концессионеру/частному партнеру минимальный гарантированный доход (выручку)
     */
    private Boolean cbcMinimumGuaranteedExist;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcMinimumGuaranteedClauseAgreement;

    /**
     * Предельная сумма компенсации концессионеру/частному партнеру по обеспечению его минимального гарантированного дохода, тыс.руб.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "indicator = 'cbcMinimumGuaranteedAmount'")
    private List<SimpleYearIndicator> cbcMinimumGuaranteedAmount = new ArrayList<>();

    /**
     * Включая НДС
     */
    private Boolean cbcMinimumGuaranteedAmountNdsCheck;
    /**
     * тип измерения
     */
    @Column(name = "cbc_min_guarant_measure_type_id")
    private Long cbcMinimumGuaranteedAmountMeasureType;
    /**
     * "Включая НДС" + Дата
     */
    private Long cbcMinimumGuaranteedAmountDateField;


    /**
     * Минимальный гарантированный доход установлен в виде:
     */
    private Long cbcMinimumGuaranteedIncomeForm;
    /**
     * Произведена компенсация концессионеру/частному партнеру недополученных доходов, обеспечивающих концессионеру/частному партнеру минимальный гарантированный доход (выручку)
     */
    private Boolean cbcCompensationMinimumGuaranteedExist;

    /**
     * Размер фактически выплаченной компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "indicator = 'cbcCompensationMinimumGuaranteedAmount'")
    private List<SimpleYearIndicator> cbcCompensationMinimumGuaranteedAmount = new ArrayList<>();

    /**
     * Наличие в соглашении условий по выплате концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг
     */
    private Boolean cbcNonPaymentConsumersGoodsProvidedExist;


    /**
     * Включается ли в соответствии с условиями соглашения выплата концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг, в состав компенсации концессионеру/частному партнеру недополученных доходов, обеспечивающих минимальный гарантированный доход (выручку):
     */
    private Long cbcNonPaymentConsumersGoodsProvidedForm;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcNonPaymentConsumersGoodsProvidedClauseAgreement;

    /**
     * Установлен предел компенсации концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг
     */
    private Boolean cbcLimitNonPaymentConsumersGoodsProvidedExist;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcLimitNonPayConsGoodsProvidedClauseAgree;

    /**
     * Произведена компенсация концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг
     */
    private Boolean cbcCompLimitNonPaymentConsGoodsProvidedExist;

    /**
     * Наличие в соглашении условий по выплате концессионеру/частному партнеру недополученных доходов, возникающих при предоставлении льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента
     */
    private Boolean cbcArisingProvisionOfBenefitsExist;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcArisingProvisionOfBenefitsClauseAgreement;

    /**
     * Произведена компенсация концессионеру/частному партнеру недополученных доходов, возникающих при предоставлении льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента
     */
    private Boolean cbcCompensationArisingProvisionOfBenefitsExist;

    /**
     * Размер фактически выплаченной концессионеру/частному партнеру компенсации недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "indicator = 'cbcCompensationLimitNonPaymentAmount'")
    private List<SimpleYearIndicator> cbcCompensationLimitNonPaymentAmount = new ArrayList<>();

    /**
     * Пункт(ы) соглашения
     */
    private String cbcCompArisingProvisionOfBenefitsClauseAgree;

    /**
     * Размер фактически выплаченной компенсации концессионеру/частному партнеру льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "indicator = 'cbcCompensationArisingProvisionOfBenefitsAmount'")
    private List<SimpleYearIndicator> cbcCompensationArisingProvisionOfBenefitsAmount = new ArrayList<>();

    /**
     * Наличие в соглашении условий по компенсации концессионеру/частному партнеру дополнительных расходов/недополученных доходов в связи с наступлением определенных обстоятельств, предусмотренных соглашением(за исключением обстоятельств, указанных ранее)
     */
    private Boolean cbcDueToOnsetOfCertainCircumstancesExist;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcDueToOnsetOfCertainCircumstancesClauseAgreement;

    /**
     * Установлен предел  компенсации концессионеру/частному партнеру его дополнительных расходов/недополученных доходов по соглашению
     */
    private Boolean cbcLimitCompAdditionalCostsAgreeExist;

    /**
     * Пункт(ы) соглашения
     */
    private String cbcLimitCompensationAdditionalClauseAgreement;

    /**
     * Предельная сумма компенсации концессионеру/частному партнеру его дополнительных
     * расходов/недополученных доходов по соглашению
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_field = 'cbcLimitCompensationAdditionalCostsAmount'")
    private List<CircumstanceStageIndicator> cbcLimitCompensationAdditionalCostsAmount = new ArrayList<>();

    /**
     * Включая НДС
     */
    private Boolean cbcNdsCheck;
    private Boolean ioNdsCheck;
    /**
     * тип измерения
     */
    @Column(name = "cbc_measure_type_id")
    private Long cbcMeasureType;
    private Long ioMeasureType;
    /**
     * "Включая НДС" + Дата
     */
    private Long cbcDateField;
    private Long ioDateField;

    /**
     * Наименование обстоятельства, возникновение которого может повлечь за собой обязательство
     * концедента/публичного партнера по компенсации дополнительных расходов/недополученных
     * доходов концессионера/частного партнера
     */
    /**
     * Этап подготовки
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_CIRCUMSTANCEADDITIONALCOSTPREPARE",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "CIRCUMSTANCEADDITIONALCOSTPREPARE_ID")
    private List<Long> cbcNameOfCircumstanceAdditionalCostPrepare = new ArrayList<>();

    /**
     * Указать прочие обстоятельства
     */
    private String cbcSpecifyOtherCircumstancesPrepare;

    /**
     * Этап строительства
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_CIRCUMSTANCEADDITIONALCOSTBUILD",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "CIRCUMSTANCEADDITIONALCOSTBUILD_ID")
    private List<Long> cbcNameOfCircumstanceAdditionalCostBuild = new ArrayList<>();

    /**
     * Указать прочие обстоятельства
     */
    private String cbcSpecifyOtherCircumstancesBuild;

    /**
     * Этап эксплуатации
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PROJECT_CIRCUMSTANCEADDITIONALCOSTEXPLOITATION",
            joinColumns = @JoinColumn(name = "PROJECT_ID", referencedColumnName = "id")
    )
    @Column(name = "CIRCUMSTANCEADDITIONALCOSTEXPLOITATION_ID")
    private List<Long> cbcNameOfCircumstanceAdditionalCostExploitation = new ArrayList<>();

    /**
     * Указать прочие обстоятельства
     */
    private String cbcSpecifyOtherCircumstancesExploitation;

    /**
     * Обстоятельства, возникновение которого повлекло за собой обязательство концедента/публичного партнера
     * по компенсации дополнительных расходов/недополученных доходов концессионера/частного партнера
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_field = 'cbcCircumstancesAdditionalCostsAmount'")
    private List<CircumstanceStageIndicator> cbcCircumstancesAdditionalCostsAmount = new ArrayList<>();

    /**
     * Произведена компенсация концессионеру/частному партнеру его
     * дополнительных расходов/недополученных доходов по соглашению
     */
    private Boolean cbcCompensationAdditionalCostsAgreementExist;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArisingProvisionOfBenefitFile> cbcArisingProvisionOfBenefitFVId = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompensationAdditionalCostsAgreementFile> cbcCompensationAdditionalCostsAgreementFVId = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompensationArisingProvisionOfBenefitsFile> cbcCompensationArisingProvisionOfBenefitsFVId = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinimumGuaranteedFile> cbcMinimumGuaranteedFVId = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NonPaymentConsumersFile> cbcNonPaymentConsumersFVId = new ArrayList<>();

    private Boolean published;

    //    @Version
    @Column
    private Long version;

    @Column(name = "CREATE_DATE")
    @CreatedDate
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    private Date updateDate;

    @Column(name = "CREATE_USER_ID")
    @CreatedBy
    private Long createUserId;

    @Column(name = "UPDATE_USER_ID")
    @LastModifiedBy
    private Long updateUserId;

    //костыль, без которого не работают ревизии для таблиц.
    private Long saveIndex = 0L;

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

    public Boolean getGiIsRFPartOfAgreement() {
        return giIsRFPartOfAgreement;
    }

    public void setGiIsRFPartOfAgreement(Boolean giIsRFPartOfAgreement) {
        this.giIsRFPartOfAgreement = giIsRFPartOfAgreement;
    }

    public Boolean getGiIsRegionPartOfAgreement() {
        return giIsRegionPartOfAgreement;
    }

    public void setGiIsRegionPartOfAgreement(Boolean giIsRegionPartOfAgreement) {
        this.giIsRegionPartOfAgreement = giIsRegionPartOfAgreement;
    }

    public Boolean getGiIsMunicipalityPartOfAgreement() {
        return giIsMunicipalityPartOfAgreement;
    }

    public void setGiIsMunicipalityPartOfAgreement(Boolean giIsMunicipalityPartOfAgreement) {
        this.giIsMunicipalityPartOfAgreement = giIsMunicipalityPartOfAgreement;
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

    public String getGiInn() {
        return giInn;
    }

    public void setGiInn(String giInn) {
        this.giInn = giInn;
    }

    public String getGiBalanceHolder() {
        return giBalanceHolder;
    }

    public void setGiBalanceHolder(String giBalanceHolder) {
        this.giBalanceHolder = giBalanceHolder;
    }

    public String getGiImplementer() {
        return giImplementer;
    }

    public void setGiImplementer(String giImplementer) {
        this.giImplementer = giImplementer;
    }

    public String getGiImplementerInn() {
        return giImplementerInn;
    }

    public void setGiImplementerInn(String giImplementerInn) {
        this.giImplementerInn = giImplementerInn;
    }

    public String getGiOPF() {
        return giOPF;
    }

    public void setGiOPF(String giOPF) {
        this.giOPF = giOPF;
    }

    public Boolean getGiIsForeignInvestor() {
        return giIsForeignInvestor;
    }

    public void setGiIsForeignInvestor(Boolean giIsForeignInvestor) {
        this.giIsForeignInvestor = giIsForeignInvestor;
    }

    public Boolean getGiIsMcpSubject() {
        return giIsMcpSubject;
    }

    public void setGiIsMcpSubject(Boolean giIsMcpSubject) {
        this.giIsMcpSubject = giIsMcpSubject;
    }

    public Boolean getGiIsSpecialProjectCompany() {
        return giIsSpecialProjectCompany;
    }

    public void setGiIsSpecialProjectCompany(Boolean giIsSpecialProjectCompany) {
        this.giIsSpecialProjectCompany = giIsSpecialProjectCompany;
    }

    public Boolean getGiHasInvestmentProperty() {
        return giHasInvestmentProperty;
    }

    public void setGiHasInvestmentProperty(Boolean giHasInvestmentProperty) {
        this.giHasInvestmentProperty = giHasInvestmentProperty;
    }

    public Double getGiPublicSharePercentage() {
        return giPublicSharePercentage;
    }

    public void setGiPublicSharePercentage(Double giPublicSharePercentage) {
        this.giPublicSharePercentage = giPublicSharePercentage;
    }

    public Boolean getGiIsRFHasShare() {
        return giIsRFHasShare;
    }

    public void setGiIsRFHasShare(Boolean giIsRFHasShare) {
        this.giIsRFHasShare = giIsRFHasShare;
    }

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

    public List<Long> getGiObjectType() {
        return giObjectType;
    }

    public void setGiObjectType(List<Long> giObjectType) {
        this.giObjectType = giObjectType;
    }

    public String getGiObjectLocation() {
        return giObjectLocation;
    }

    public void setGiObjectLocation(String giObjectLocation) {
        this.giObjectLocation = giObjectLocation;
    }

    public List<Long> getGiAgreementSubject() {
        return giAgreementSubject;
    }

    public void setGiAgreementSubject(List<Long> giAgreementSubject) {
        this.giAgreementSubject = giAgreementSubject;
    }

    public String getGiRealizationStatus() {
        return giRealizationStatus;
    }

    public void setGiRealizationStatus(String giRealizationStatus) {
        this.giRealizationStatus = giRealizationStatus;
    }


    public List<CompletedTemplateFile> getGiCompletedTemplateTextFVId() {
        return giCompletedTemplateTextFVId;
    }

    public void setGiCompletedTemplateTextFVId(List<CompletedTemplateFile> giCompletedTemplateTextFVId) {
        this.giCompletedTemplateTextFVId = giCompletedTemplateTextFVId;
    }

    public Long getGiProjectStatus() {
        return giProjectStatus;
    }

    public void setGiProjectStatus(Long giProjectStatus) {
        this.giProjectStatus = giProjectStatus;
    }

    public Boolean getGiAlwaysDraftStatus() {
        return giAlwaysDraftStatus;
    }

    public void setGiAlwaysDraftStatus(Boolean giAlwaysDraftStatus) {
        this.giAlwaysDraftStatus = giAlwaysDraftStatus;
    }

    public PlanCreationInvestments getPpCreationInvestmentPlanningAmount() {
        return ppCreationInvestmentPlanningAmount;
    }

    public void setPpCreationInvestmentPlanningAmount(PlanCreationInvestments ppCreationInvestmentPlanningAmount) {
        this.ppCreationInvestmentPlanningAmount = ppCreationInvestmentPlanningAmount;
    }

    public PlanInvestments getPpInvestmentPlanningAmount() {
        return ppInvestmentPlanningAmount;
    }

    public void setPpInvestmentPlanningAmount(PlanInvestments ppInvestmentPlanningAmount) {
        this.ppInvestmentPlanningAmount = ppInvestmentPlanningAmount;
    }

    public String getOdObjectName() {
        return odObjectName;
    }

    public void setOdObjectName(String odObjectName) {
        this.odObjectName = odObjectName;
    }

    public String getOdObjectDescription() {
        return odObjectDescription;
    }

    public void setOdObjectDescription(String odObjectDescription) {
        this.odObjectDescription = odObjectDescription;
    }

    public Boolean getOdIsPropertyStaysWithPrivateSide() {
        return odIsPropertyStaysWithPrivateSide;
    }

    public void setOdIsPropertyStaysWithPrivateSide(Boolean odIsPropertyStaysWithPrivateSide) {
        this.odIsPropertyStaysWithPrivateSide = odIsPropertyStaysWithPrivateSide;
    }

    public Boolean getOdIsNewPropertyBeGivenToPrivateSide() {
        return odIsNewPropertyBeGivenToPrivateSide;
    }

    public void setOdIsNewPropertyBeGivenToPrivateSide(Boolean odIsNewPropertyBeGivenToPrivateSide) {
        this.odIsNewPropertyBeGivenToPrivateSide = odIsNewPropertyBeGivenToPrivateSide;
    }

    public Boolean getOdIsObjectImprovementsGiveAway() {
        return odIsObjectImprovementsGiveAway;
    }

    public void setOdIsObjectImprovementsGiveAway(Boolean odIsObjectImprovementsGiveAway) {
        this.odIsObjectImprovementsGiveAway = odIsObjectImprovementsGiveAway;
    }

    public Long getOdRentObject() {
        return odRentObject;
    }

    public void setOdRentObject(Long odRentObject) {
        this.odRentObject = odRentObject;
    }

    public List<RentPassportFilesEntity> getOdRentPassportFileVersionId() {
        return odRentPassportFileVersionId;
    }

    public void setOdRentPassportFileVersionId(List<RentPassportFilesEntity> odRentPassportFileVersionId) {
        this.odRentPassportFileVersionId = odRentPassportFileVersionId;
    }

    public List<TechEconomicsObjectIndicator> getOdTechEconomicsObjectIndicators() {
        return odTechEconomicsObjectIndicators;
    }

    public void setOdTechEconomicsObjectIndicators(List<TechEconomicsObjectIndicator> odTechEconomicsObjectIndicators) {
        this.odTechEconomicsObjectIndicators = odTechEconomicsObjectIndicators;
    }

    public List<EnergyEfficiencyPlan> getOdEnergyEfficiencyPlans() {
        return odEnergyEfficiencyPlans;
    }

    public void setOdEnergyEfficiencyPlans(List<EnergyEfficiencyPlan> odEnergyEfficiencyPlans) {
        this.odEnergyEfficiencyPlans = odEnergyEfficiencyPlans;
    }

    public Boolean getPpIsObjInListWithConcessionalAgreements() {
        return ppIsObjInListWithConcessionalAgreements;
    }

    public void setPpIsObjInListWithConcessionalAgreements(Boolean ppIsObjInListWithConcessionalAgreements) {
        this.ppIsObjInListWithConcessionalAgreements = ppIsObjInListWithConcessionalAgreements;
    }

    public String getPpObjectsListUrl() {
        return ppObjectsListUrl;
    }

    public void setPpObjectsListUrl(String ppObjectsListUrl) {
        this.ppObjectsListUrl = ppObjectsListUrl;
    }

    public String getPpSupposedPrivatePartnerName() {
        return ppSupposedPrivatePartnerName;
    }

    public void setPpSupposedPrivatePartnerName(String ppSupposedPrivatePartnerName) {
        this.ppSupposedPrivatePartnerName = ppSupposedPrivatePartnerName;
    }

    public String getPpSupposedPrivatePartnerInn() {
        return ppSupposedPrivatePartnerInn;
    }

    public void setPpSupposedPrivatePartnerInn(String ppSupposedPrivatePartnerInn) {
        this.ppSupposedPrivatePartnerInn = ppSupposedPrivatePartnerInn;
    }

    public Boolean getPpIsForeignInvestor() {
        return ppIsForeignInvestor;
    }

    public void setPpIsForeignInvestor(Boolean ppIsForeignInvestor) {
        this.ppIsForeignInvestor = ppIsForeignInvestor;
    }

    public Boolean getPpIsMspSubject() {
        return ppIsMspSubject;
    }

    public void setPpIsMspSubject(Boolean ppIsMspSubject) {
        this.ppIsMspSubject = ppIsMspSubject;
    }

    public Long getPpAgreementsSet() {
        return ppAgreementsSet;
    }

    public void setPpAgreementsSet(Long ppAgreementsSet) {
        this.ppAgreementsSet = ppAgreementsSet;
    }


    public Date getPpSupposedAgreementStartDate() {
        return ppSupposedAgreementStartDate;
    }

    public void setPpSupposedAgreementStartDate(Date ppSupposedAgreementStartDate) {
        this.ppSupposedAgreementStartDate = ppSupposedAgreementStartDate;
    }

    public Date getPpSupposedAgreementEndDate() {
        return ppSupposedAgreementEndDate;
    }

    public void setPpSupposedAgreementEndDate(Date ppSupposedAgreementEndDate) {
        this.ppSupposedAgreementEndDate = ppSupposedAgreementEndDate;
    }

    public Double getPpSupposedValidityYears() {
        return ppSupposedValidityYears;
    }

    public void setPpSupposedValidityYears(Double ppSupposedValidityYears) {
        this.ppSupposedValidityYears = ppSupposedValidityYears;
    }

    public Date getPpDeliveryTimeOfGoodsWorkDate() {
        return ppDeliveryTimeOfGoodsWorkDate;
    }

    public void setPpDeliveryTimeOfGoodsWorkDate(Date ppDeliveryTimeOfGoodsWorkDate) {
        this.ppDeliveryTimeOfGoodsWorkDate = ppDeliveryTimeOfGoodsWorkDate;
    }

    public List<ProjectAgreementFile> getPpProjectAgreementFileVersionId() {
        return ppProjectAgreementFileVersionId;
    }

    public void setPpProjectAgreementFileVersionId(List<ProjectAgreementFile> ppProjectAgreementFileVersionId) {
        this.ppProjectAgreementFileVersionId = ppProjectAgreementFileVersionId;
    }

    public Long getPpGroundsOfAgreementConclusion() {
        return ppGroundsOfAgreementConclusion;
    }

    public void setPpGroundsOfAgreementConclusion(Long ppGroundsOfAgreementConclusion) {
        this.ppGroundsOfAgreementConclusion = ppGroundsOfAgreementConclusion;
    }

    public String getPpActNumber() {
        return ppActNumber;
    }

    public void setPpActNumber(String ppActNumber) {
        this.ppActNumber = ppActNumber;
    }

    public Date getPpActDate() {
        return ppActDate;
    }

    public void setPpActDate(Date ppActDate) {
        this.ppActDate = ppActDate;
    }

    public List<ActTextFile> getPpActTextFileVersionId() {
        return ppActTextFileVersionId;
    }

    public void setPpActTextFileVersionId(List<ActTextFile> ppActTextFileVersionId) {
        this.ppActTextFileVersionId = ppActTextFileVersionId;
    }

    public List<LeaseAgreementTextFile> getPpLeaseAgreementTextFileVersionId() {
        return ppLeaseAgreementTextFileVersionId;
    }

    public void setPpLeaseAgreementTextFileVersionId(List<LeaseAgreementTextFile> ppLeaseAgreementTextFileVersionId) {
        this.ppLeaseAgreementTextFileVersionId = ppLeaseAgreementTextFileVersionId;
    }


    public Boolean getPpProjectAssignedStatus() {
        return ppProjectAssignedStatus;
    }

    public void setPpProjectAssignedStatus(Boolean ppProjectAssignedStatus) {
        this.ppProjectAssignedStatus = ppProjectAssignedStatus;
    }

    public String getPpDecisionNumber() {
        return ppDecisionNumber;
    }

    public void setPpDecisionNumber(String ppDecisionNumber) {
        this.ppDecisionNumber = ppDecisionNumber;
    }

    public Date getPpDecisionDate() {
        return ppDecisionDate;
    }

    public void setPpDecisionDate(Date ppDecisionDate) {
        this.ppDecisionDate = ppDecisionDate;
    }

    public List<DecisionTextFile> getPpDecisionTextFileVersionId() {
        return ppDecisionTextFileVersionId;
    }

    public void setPpDecisionTextFileVersionId(List<DecisionTextFile> ppDecisionTextFileVersionId) {
        this.ppDecisionTextFileVersionId = ppDecisionTextFileVersionId;
    }

    public Date getPpProposalPublishDate() {
        return ppProposalPublishDate;
    }

    public void setPpProposalPublishDate(Date ppProposalPublishDate) {
        this.ppProposalPublishDate = ppProposalPublishDate;
    }

    public List<ProposalTextFile> getPpProposalTextFileVersionId() {
        return ppProposalTextFileVersionId;
    }

    public void setPpProposalTextFileVersionId(List<ProposalTextFile> ppProposalTextFileVersionId) {
        this.ppProposalTextFileVersionId = ppProposalTextFileVersionId;
    }

    public String getPpTorgiGovRuUrl() {
        return ppTorgiGovRuUrl;
    }

    public void setPpTorgiGovRuUrl(String ppTorgiGovRuUrl) {
        this.ppTorgiGovRuUrl = ppTorgiGovRuUrl;
    }

    public List<ProtocolFile> getPpProtocolFileVersionId() {
        return ppProtocolFileVersionId;
    }

    public void setPpProtocolFileVersionId(List<ProtocolFile> ppProtocolFileVersionId) {
        this.ppProtocolFileVersionId = ppProtocolFileVersionId;
    }

    public Boolean getPpIsReadinessRequestReceived() {
        return ppIsReadinessRequestReceived;
    }

    public void setPpIsReadinessRequestReceived(Boolean ppIsReadinessRequestReceived) {
        this.ppIsReadinessRequestReceived = ppIsReadinessRequestReceived;
    }

    public Boolean getPpIsDecisionMadeToConcludeAnAgreement() {
        return ppIsDecisionMadeToConcludeAnAgreement;
    }

    public void setPpIsDecisionMadeToConcludeAnAgreement(Boolean ppIsDecisionMadeToConcludeAnAgreement) {
        this.ppIsDecisionMadeToConcludeAnAgreement = ppIsDecisionMadeToConcludeAnAgreement;
    }

    public String getPpConcludeAgreementActNum() {
        return ppConcludeAgreementActNum;
    }

    public void setPpConcludeAgreementActNum(String ppConcludeAgreementActNum) {
        this.ppConcludeAgreementActNum = ppConcludeAgreementActNum;
    }

    public Date getPpConcludeAgreementActDate() {
        return ppConcludeAgreementActDate;
    }

    public void setPpConcludeAgreementActDate(Date ppConcludeAgreementActDate) {
        this.ppConcludeAgreementActDate = ppConcludeAgreementActDate;
    }

    public List<ConcludeAgreementFile> getPpConcludeAgreementFvId() {
        return ppConcludeAgreementFvId;
    }

    public void setPpConcludeAgreementFvId(List<ConcludeAgreementFile> ppConcludeAgreementFvId) {
        this.ppConcludeAgreementFvId = ppConcludeAgreementFvId;
    }

    public Date getPpInvestmentStageDurationDate() {
        return ppInvestmentStageDurationDate;
    }

    public void setPpInvestmentStageDurationDate(Date ppInvestmentStageDurationDate) {
        this.ppInvestmentStageDurationDate = ppInvestmentStageDurationDate;
    }

    public Boolean getPpConcludeAgreementIsSigned() {
        return ppConcludeAgreementIsSigned;
    }

    public void setPpConcludeAgreementIsSigned(Boolean ppConcludeAgreementIsSigned) {
        this.ppConcludeAgreementIsSigned = ppConcludeAgreementIsSigned;
    }

    public Boolean getPpConcludeAgreementIsJoint() {
        return ppConcludeAgreementIsJoint;
    }

    public void setPpConcludeAgreementIsJoint(Boolean ppConcludeAgreementIsJoint) {
        this.ppConcludeAgreementIsJoint = ppConcludeAgreementIsJoint;
    }

    public String getPpConcludeAgreementOtherPjInfo() {
        return ppConcludeAgreementOtherPjInfo;
    }

    public void setPpConcludeAgreementOtherPjInfo(String ppConcludeAgreementOtherPjInfo) {
        this.ppConcludeAgreementOtherPjInfo = ppConcludeAgreementOtherPjInfo;
    }

    public String getPpConcludeAgreementOtherPjIdent() {
        return ppConcludeAgreementOtherPjIdent;
    }

    public void setPpConcludeAgreementOtherPjIdent(String ppConcludeAgreementOtherPjIdent) {
        this.ppConcludeAgreementOtherPjIdent = ppConcludeAgreementOtherPjIdent;
    }

    public Date getPpCompetitionBidCollEndPlanDate() {
        return ppCompetitionBidCollEndPlanDate;
    }

    public void setPpCompetitionBidCollEndPlanDate(Date ppCompetitionBidCollEndPlanDate) {
        this.ppCompetitionBidCollEndPlanDate = ppCompetitionBidCollEndPlanDate;
    }

    public Date getPpCompetitionBidCollEndFactDate() {
        return ppCompetitionBidCollEndFactDate;
    }

    public void setPpCompetitionBidCollEndFactDate(Date ppCompetitionBidCollEndFactDate) {
        this.ppCompetitionBidCollEndFactDate = ppCompetitionBidCollEndFactDate;
    }

    public Date getPpCompetitionTenderOfferEndPlanDate() {
        return ppCompetitionTenderOfferEndPlanDate;
    }

    public void setPpCompetitionTenderOfferEndPlanDate(Date ppCompetitionTenderOfferEndPlanDate) {
        this.ppCompetitionTenderOfferEndPlanDate = ppCompetitionTenderOfferEndPlanDate;
    }

    public Date getPpCompetitionTenderOfferEndFactDate() {
        return ppCompetitionTenderOfferEndFactDate;
    }

    public void setPpCompetitionTenderOfferEndFactDate(Date ppCompetitionTenderOfferEndFactDate) {
        this.ppCompetitionTenderOfferEndFactDate = ppCompetitionTenderOfferEndFactDate;
    }

    public Date getPpCompetitionResultsPlanDate() {
        return ppCompetitionResultsPlanDate;
    }

    public void setPpCompetitionResultsPlanDate(Date ppCompetitionResultsPlanDate) {
        this.ppCompetitionResultsPlanDate = ppCompetitionResultsPlanDate;
    }

    public Date getPpCompetitionResultsFactDate() {
        return ppCompetitionResultsFactDate;
    }

    public void setPpCompetitionResultsFactDate(Date ppCompetitionResultsFactDate) {
        this.ppCompetitionResultsFactDate = ppCompetitionResultsFactDate;
    }

    public List<CompetitionText> getPpCompetitionTextFVId() {
        return ppCompetitionTextFVId;
    }

    public void setPpCompetitionTextFVId(List<CompetitionText> ppCompetitionTextFVId) {
        this.ppCompetitionTextFVId = ppCompetitionTextFVId;
    }

    public Boolean getPpCompetitionIsElAuction() {
        return ppCompetitionIsElAuction;
    }

    public void setPpCompetitionIsElAuction(Boolean ppCompetitionIsElAuction) {
        this.ppCompetitionIsElAuction = ppCompetitionIsElAuction;
    }

    public Boolean getPpCompetitionHasResults() {
        return ppCompetitionHasResults;
    }

    public void setPpCompetitionHasResults(Boolean ppCompetitionHasResults) {
        this.ppCompetitionHasResults = ppCompetitionHasResults;
    }

    public Long getPpCompetitionResults() {
        return ppCompetitionResults;
    }

    public void setPpCompetitionResults(Long ppCompetitionResults) {
        this.ppCompetitionResults = ppCompetitionResults;
    }

    public String getPpCompetitionResultsProtocolNum() {
        return ppCompetitionResultsProtocolNum;
    }

    public void setPpCompetitionResultsProtocolNum(String ppCompetitionResultsProtocolNum) {
        this.ppCompetitionResultsProtocolNum = ppCompetitionResultsProtocolNum;
    }

    public Date getPpCompetitionResultsProtocolDate() {
        return ppCompetitionResultsProtocolDate;
    }

    public void setPpCompetitionResultsProtocolDate(Date ppCompetitionResultsProtocolDate) {
        this.ppCompetitionResultsProtocolDate = ppCompetitionResultsProtocolDate;
    }

    public Integer getPpCompetitionResultsParticipantsNum() {
        return ppCompetitionResultsParticipantsNum;
    }

    public void setPpCompetitionResultsParticipantsNum(Integer ppCompetitionResultsParticipantsNum) {
        this.ppCompetitionResultsParticipantsNum = ppCompetitionResultsParticipantsNum;
    }

    public List<CompetitionResultsProtocolFile> getPpCompetitionResultsProtocolTextFvId() {
        return ppCompetitionResultsProtocolTextFvId;
    }

    public void setPpCompetitionResultsProtocolTextFvId(List<CompetitionResultsProtocolFile> ppCompetitionResultsProtocolTextFvId) {
        this.ppCompetitionResultsProtocolTextFvId = ppCompetitionResultsProtocolTextFvId;
    }

    public List<CompetitionResultsFile> getPpCompetitionResultsDocFvId() {
        return ppCompetitionResultsDocFvId;
    }

    public void setPpCompetitionResultsDocFvId(List<CompetitionResultsFile> ppCompetitionResultsDocFvId) {
        this.ppCompetitionResultsDocFvId = ppCompetitionResultsDocFvId;
    }

    public Long getPpCompetitionResultsSignStatus() {
        return ppCompetitionResultsSignStatus;
    }

    public void setPpCompetitionResultsSignStatus(Long ppCompetitionResultsSignStatus) {
        this.ppCompetitionResultsSignStatus = ppCompetitionResultsSignStatus;
    }

    public Long getPpContractPriceOrder() {
        return ppContractPriceOrder;
    }

    public void setPpContractPriceOrder(Long ppContractPriceOrder) {
        this.ppContractPriceOrder = ppContractPriceOrder;
    }

    public String getPpContractPriceFormula() {
        return ppContractPriceFormula;
    }

    public void setPpContractPriceFormula(String ppContractPriceFormula) {
        this.ppContractPriceFormula = ppContractPriceFormula;
    }

    public Double getPpContractPricePrice() {
        return ppContractPricePrice;
    }

    public void setPpContractPricePrice(Double ppContractPricePrice) {
        this.ppContractPricePrice = ppContractPricePrice;
    }

    public Boolean getPpNdsCheck() {
        return ppNdsCheck;
    }

    public void setPpNdsCheck(Boolean ppNdsCheck) {
        this.ppNdsCheck = ppNdsCheck;
    }

    public Long getPpMeasureType() {
        return ppMeasureType;
    }

    public void setPpMeasureType(Long ppMeasureType) {
        this.ppMeasureType = ppMeasureType;
    }

    public Long getPpDateField() {
        return ppDateField;
    }

    public void setPpDateField(Long ppDateField) {
        this.ppDateField = ppDateField;
    }

//    public Long getPpContractPriceMethod() {
//        return ppContractPriceMethod;
//    }

//    public void setPpContractPriceMethod(Long ppContractPriceMethod) {
//        this.ppContractPriceMethod = ppContractPriceMethod;
//    }

    public Long getPpContractPriceOffer() {
        return ppContractPriceOffer;
    }

    public void setPpContractPriceOffer(Long ppContractPriceOffer) {
        this.ppContractPriceOffer = ppContractPriceOffer;
    }

    public Double getPpContractPriceOfferValue() {
        return ppContractPriceOfferValue;
    }

    public void setPpContractPriceOfferValue(Double ppContractPriceOfferValue) {
        this.ppContractPriceOfferValue = ppContractPriceOfferValue;
    }

    public Long getPpWinnerContractPriceOffer() {
        return ppWinnerContractPriceOffer;
    }

    public void setPpWinnerContractPriceOffer(Long ppWinnerContractPriceOffer) {
        this.ppWinnerContractPriceOffer = ppWinnerContractPriceOffer;
    }

    public Date getPpContractPriceSavingStartDate() {
        return ppContractPriceSavingStartDate;
    }

    public void setPpContractPriceSavingStartDate(Date ppContractPriceSavingStartDate) {
        this.ppContractPriceSavingStartDate = ppContractPriceSavingStartDate;
    }

    public Date getPpContractPriceSavingEndDate() {
        return ppContractPriceSavingEndDate;
    }

    public void setPpContractPriceSavingEndDate(Date ppContractPriceSavingEndDate) {
        this.ppContractPriceSavingEndDate = ppContractPriceSavingEndDate;
    }

    public List<FinancialModelFile> getPpFinancialModelFVId() {
        return ppFinancialModelFVId;
    }

    public void setPpFinancialModelFVId(List<FinancialModelFile> ppFinancialModelFVId) {
        this.ppFinancialModelFVId = ppFinancialModelFVId;
    }

    public Long getPpPrivatePartnerCostRecoveryMethod() {
        return ppPrivatePartnerCostRecoveryMethod;
    }

    public void setPpPrivatePartnerCostRecoveryMethod(Long ppPrivatePartnerCostRecoveryMethod) {
        this.ppPrivatePartnerCostRecoveryMethod = ppPrivatePartnerCostRecoveryMethod;
    }

    public Double getPpAdvancePaymentAmount() {
        return ppAdvancePaymentAmount;
    }

    public void setPpAdvancePaymentAmount(Double ppAdvancePaymentAmount) {
        this.ppAdvancePaymentAmount = ppAdvancePaymentAmount;
    }

    public Date getPpFirstObjectOperationDate() {
        return ppFirstObjectOperationDate;
    }

    public void setPpFirstObjectOperationDate(Date ppFirstObjectOperationDate) {
        this.ppFirstObjectOperationDate = ppFirstObjectOperationDate;
    }

    public Date getPpLastObjectCommissioningDate() {
        return ppLastObjectCommissioningDate;
    }

    public void setPpLastObjectCommissioningDate(Date ppLastObjectCommissioningDate) {
        this.ppLastObjectCommissioningDate = ppLastObjectCommissioningDate;
    }

    public List<SupportingDocumentsFile> getPpSupportingDocumentsFileVersionIds() {
        return ppSupportingDocumentsFileVersionIds;
    }

    public void setPpSupportingDocumentsFileVersionIds(List<SupportingDocumentsFile> ppSupportingDocumentsFileVersionIds) {
        this.ppSupportingDocumentsFileVersionIds = ppSupportingDocumentsFileVersionIds;
    }

    public Double getPpBudgetExpendituresAgreementOnGchpMchp() {
        return ppBudgetExpendituresAgreementOnGchpMchp;
    }

    public void setPpBudgetExpendituresAgreementOnGchpMchp(Double ppBudgetExpendituresAgreementOnGchpMchp) {
        this.ppBudgetExpendituresAgreementOnGchpMchp = ppBudgetExpendituresAgreementOnGchpMchp;
    }

    public Double getPpBudgetExpendituresGovContract() {
        return ppBudgetExpendituresGovContract;
    }

    public void setPpBudgetExpendituresGovContract(Double ppBudgetExpendituresGovContract) {
        this.ppBudgetExpendituresGovContract = ppBudgetExpendituresGovContract;
    }

    public Double getPpObligationsInCaseOfRisksAgreementOnGchpMchp() {
        return ppObligationsInCaseOfRisksAgreementOnGchpMchp;
    }

    public void setPpObligationsInCaseOfRisksAgreementOnGchpMchp(Double ppObligationsInCaseOfRisksAgreementOnGchpMchp) {
        this.ppObligationsInCaseOfRisksAgreementOnGchpMchp = ppObligationsInCaseOfRisksAgreementOnGchpMchp;
    }

    public Double getPpObligationsInCaseOfRisksGovContract() {
        return ppObligationsInCaseOfRisksGovContract;
    }

    public void setPpObligationsInCaseOfRisksGovContract(Double ppObligationsInCaseOfRisksGovContract) {
        this.ppObligationsInCaseOfRisksGovContract = ppObligationsInCaseOfRisksGovContract;
    }

    public Double getPpIndicatorAssessmentComparativeAdvantage() {
        return ppIndicatorAssessmentComparativeAdvantage;
    }

    public void setPpIndicatorAssessmentComparativeAdvantage(Double ppIndicatorAssessmentComparativeAdvantage) {
        this.ppIndicatorAssessmentComparativeAdvantage = ppIndicatorAssessmentComparativeAdvantage;
    }

    public List<ConclusionUOTextFile> getPpConclusionUOTextFileVersionId() {
        return ppConclusionUOTextFileVersionId;
    }

    public void setPpConclusionUOTextFileVersionId(List<ConclusionUOTextFile> ppConclusionUOTextFileVersionId) {
        this.ppConclusionUOTextFileVersionId = ppConclusionUOTextFileVersionId;
    }

    public List<FinancialModelTextFile> getPpFinancialModelTextFileVersionId() {
        return ppFinancialModelTextFileVersionId;
    }

    public void setPpFinancialModelTextFileVersionId(List<FinancialModelTextFile> ppFinancialModelTextFileVersionId) {
        this.ppFinancialModelTextFileVersionId = ppFinancialModelTextFileVersionId;
    }

    public Long getPpMethodOfExecuteObligation() {
        return ppMethodOfExecuteObligation;
    }

    public void setPpMethodOfExecuteObligation(Long ppMethodOfExecuteObligation) {
        this.ppMethodOfExecuteObligation = ppMethodOfExecuteObligation;
    }

    public String getPpLinkToClauseAgreement() {
        return ppLinkToClauseAgreement;
    }

    public void setPpLinkToClauseAgreement(String ppLinkToClauseAgreement) {
        this.ppLinkToClauseAgreement = ppLinkToClauseAgreement;
    }

    public Boolean getPpIsPrivateLiabilityProvided() {
        return ppIsPrivateLiabilityProvided;
    }

    public void setPpIsPrivateLiabilityProvided(Boolean ppIsPrivateLiabilityProvided) {
        this.ppIsPrivateLiabilityProvided = ppIsPrivateLiabilityProvided;
    }

    public String getPpLinkToClauseAgreementLiabilityProvided() {
        return ppLinkToClauseAgreementLiabilityProvided;
    }

    public void setPpLinkToClauseAgreementLiabilityProvided(String ppLinkToClauseAgreementLiabilityProvided) {
        this.ppLinkToClauseAgreementLiabilityProvided = ppLinkToClauseAgreementLiabilityProvided;
    }

    public Long getPpStateSupportMeasuresSPIC() {
        return ppStateSupportMeasuresSPIC;
    }

    public void setPpStateSupportMeasuresSPIC(Long ppStateSupportMeasuresSPIC) {
        this.ppStateSupportMeasuresSPIC = ppStateSupportMeasuresSPIC;
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

    public List<AgreementTextFile> getCrAgreementTextFvId() {
        return crAgreementTextFvId;
    }

    public void setCrAgreementTextFvId(List<AgreementTextFile> crAgreementTextFvId) {
        this.crAgreementTextFvId = crAgreementTextFvId;
    }

    public String getCrConcessionaire() {
        return crConcessionaire;
    }

    public void setCrConcessionaire(String crConcessionaire) {
        this.crConcessionaire = crConcessionaire;
    }

    public String getCrConcessionaireInn() {
        return crConcessionaireInn;
    }

    public void setCrConcessionaireInn(String crConcessionaireInn) {
        this.crConcessionaireInn = crConcessionaireInn;
    }

    public Boolean getCrFinancialClosingProvided() {
        return crFinancialClosingProvided;
    }

    public void setCrFinancialClosingProvided(Boolean crFinancialClosingProvided) {
        this.crFinancialClosingProvided = crFinancialClosingProvided;
    }

    public Date getCrFinancialClosingDate() {
        return crFinancialClosingDate;
    }

    public void setCrFinancialClosingDate(Date crFinancialClosingDate) {
        this.crFinancialClosingDate = crFinancialClosingDate;
    }

    public Double getCrFinancialClosingValue() {
        return crFinancialClosingValue;
    }

    public void setCrFinancialClosingValue(Double crFinancialClosingValue) {
        this.crFinancialClosingValue = crFinancialClosingValue;
    }

    public List<FinancialClosingActFile> getCrFinancialClosingActFVId() {
        return crFinancialClosingActFVId;
    }

    public void setCrFinancialClosingActFVId(List<FinancialClosingActFile> crFinancialClosingActFVId) {
        this.crFinancialClosingActFVId = crFinancialClosingActFVId;
    }

    public Boolean getCrFinancialClosingIsMutualAgreement() {
        return crFinancialClosingIsMutualAgreement;
    }

    public void setCrFinancialClosingIsMutualAgreement(Boolean crFinancialClosingIsMutualAgreement) {
        this.crFinancialClosingIsMutualAgreement = crFinancialClosingIsMutualAgreement;
    }

    public Date getCrFirstObjectCreationPlanDate() {
        return crFirstObjectCreationPlanDate;
    }

    public void setCrFirstObjectCreationPlanDate(Date crFirstObjectCreationPlanDate) {
        this.crFirstObjectCreationPlanDate = crFirstObjectCreationPlanDate;
    }

    public Date getCrFirstObjectCreationFactDate() {
        return crFirstObjectCreationFactDate;
    }

    public void setCrFirstObjectCreationFactDate(Date crFirstObjectCreationFactDate) {
        this.crFirstObjectCreationFactDate = crFirstObjectCreationFactDate;
    }

    public List<FirstObjectCompleteActFile> getCrFirstObjectCompleteActFVId() {
        return crFirstObjectCompleteActFVId;
    }

    public void setCrFirstObjectCompleteActFVId(List<FirstObjectCompleteActFile> crFirstObjectCompleteActFVId) {
        this.crFirstObjectCompleteActFVId = crFirstObjectCompleteActFVId;
    }

    public Boolean getCrIsRegionPartyAgreement() {
        return crIsRegionPartyAgreement;
    }

    public void setCrIsRegionPartyAgreement(Boolean crIsRegionPartyAgreement) {
        this.crIsRegionPartyAgreement = crIsRegionPartyAgreement;
    }

    public Boolean getCrIsSeveralObjects() {
        return crIsSeveralObjects;
    }

    public void setCrIsSeveralObjects(Boolean crIsSeveralObjects) {
        this.crIsSeveralObjects = crIsSeveralObjects;
    }


    public Long getCrFirstSeveralObjectPlanDate() {
        return crFirstSeveralObjectPlanDate;
    }

    public void setCrFirstSeveralObjectPlanDate(Long crFirstSeveralObjectPlanDate) {
        this.crFirstSeveralObjectPlanDate = crFirstSeveralObjectPlanDate;
    }


    public Boolean getCrIsFirstSeveralObject() {
        return crIsFirstSeveralObject;
    }

    public void setCrIsFirstSeveralObject(Boolean crIsFirstSeveralObject) {
        this.crIsFirstSeveralObject = crIsFirstSeveralObject;
    }


    public Long getCrLastSeveralObjectPlanDate() {
        return crLastSeveralObjectPlanDate;
    }

    public void setCrLastSeveralObjectPlanDate(Long crLastSeveralObjectPlanDate) {
        this.crLastSeveralObjectPlanDate = crLastSeveralObjectPlanDate;
    }


    public Boolean getCrIsLastSeveralObject() {
        return crIsLastSeveralObject;
    }

    public void setCrIsLastSeveralObject(Boolean crIsLastSeveralObject) {
        this.crIsLastSeveralObject = crIsLastSeveralObject;
    }

    public Long getCrSeveralObjectPlanDate() {
        return crSeveralObjectPlanDate;
    }

    public void setCrSeveralObjectPlanDate(Long crSeveralObjectPlanDate) {
        this.crSeveralObjectPlanDate = crSeveralObjectPlanDate;
    }

    public Boolean getCrIsSeveralObjectInOperation() {
        return crIsSeveralObjectInOperation;
    }

    public void setCrIsSeveralObjectInOperation(Boolean crIsSeveralObjectInOperation) {
        this.crIsSeveralObjectInOperation = crIsSeveralObjectInOperation;
    }

    public String getCrInvestCostsGrantor() {
        return crInvestCostsGrantor;
    }

    public void setCrInvestCostsGrantor(String crInvestCostsGrantor) {
        this.crInvestCostsGrantor = crInvestCostsGrantor;
    }

    public Boolean getCrIsFormulasInvestCosts() {
        return crIsFormulasInvestCosts;
    }

    public void setCrIsFormulasInvestCosts(Boolean crIsFormulasInvestCosts) {
        this.crIsFormulasInvestCosts = crIsFormulasInvestCosts;
    }

    public List<CalcInvestCostsActFile> getCrCalcInvestCostsActFVId() {
        return crCalcInvestCostsActFVId;
    }

    public void setCrCalcInvestCostsActFVId(List<CalcInvestCostsActFile> crCalcInvestCostsActFVId) {
        this.crCalcInvestCostsActFVId = crCalcInvestCostsActFVId;
    }

    public Double getCrActualCostsValue() {
        return crActualCostsValue;
    }

    public void setCrActualCostsValue(Double crActualCostsValue) {
        this.crActualCostsValue = crActualCostsValue;
    }

    public Double getCrAverageInterestRateValue() {
        return crAverageInterestRateValue;
    }

    public void setCrAverageInterestRateValue(Double crAverageInterestRateValue) {
        this.crAverageInterestRateValue = crAverageInterestRateValue;
    }

    public Date getCrLastObjectCreationPlanDate() {
        return crLastObjectCreationPlanDate;
    }

    public void setCrLastObjectCreationPlanDate(Date crLastObjectCreationPlanDate) {
        this.crLastObjectCreationPlanDate = crLastObjectCreationPlanDate;
    }

    public Date getCrLastObjectCreationFactDate() {
        return crLastObjectCreationFactDate;
    }

    public void setCrLastObjectCreationFactDate(Date crLastObjectCreationFactDate) {
        this.crLastObjectCreationFactDate = crLastObjectCreationFactDate;
    }

    public List<LastObjectCompleteActFile> getCrLastObjectCompleteActFVId() {
        return crLastObjectCompleteActFVId;
    }

    public void setCrLastObjectCompleteActFVId(List<LastObjectCompleteActFile> crLastObjectCompleteActFVId) {
        this.crLastObjectCompleteActFVId = crLastObjectCompleteActFVId;
    }

    public Boolean getCrIsObjectTransferProvided() {
        return crIsObjectTransferProvided;
    }

    public void setCrIsObjectTransferProvided(Boolean crIsObjectTransferProvided) {
        this.crIsObjectTransferProvided = crIsObjectTransferProvided;
    }

    public Date getCrObjectRightsPlanDate() {
        return crObjectRightsPlanDate;
    }

    public void setCrObjectRightsPlanDate(Date crObjectRightsPlanDate) {
        this.crObjectRightsPlanDate = crObjectRightsPlanDate;
    }

    public Date getCrObjectRightsFactDate() {
        return crObjectRightsFactDate;
    }

    public void setCrObjectRightsFactDate(Date crObjectRightsFactDate) {
        this.crObjectRightsFactDate = crObjectRightsFactDate;
    }

    public List<InvestmentsActFile> getCrActFVId() {
        return crActFVId;
    }

    public void setCrActFVId(List<InvestmentsActFile> crActFVId) {
        this.crActFVId = crActFVId;
    }

    public Boolean getCrIsRenewableBankGuarantee() {
        return crIsRenewableBankGuarantee;
    }

    public void setCrIsRenewableBankGuarantee(Boolean crIsRenewableBankGuarantee) {
        this.crIsRenewableBankGuarantee = crIsRenewableBankGuarantee;
    }

    public Boolean getCrIsGuaranteeVariesByYear() {
        return crIsGuaranteeVariesByYear;
    }

    public void setCrIsGuaranteeVariesByYear(Boolean crIsGuaranteeVariesByYear) {
        this.crIsGuaranteeVariesByYear = crIsGuaranteeVariesByYear;
    }

    public BankGuarantee getCrBankGuaranteeByYears() {
        return crBankGuaranteeByYears;
    }

    public void setCrBankGuaranteeByYears(BankGuarantee crBankGuaranteeByYears) {
        this.crBankGuaranteeByYears = crBankGuaranteeByYears;
    }

    public Double getCrObjectValue() {
        return crObjectValue;
    }

    public void setCrObjectValue(Double crObjectValue) {
        this.crObjectValue = crObjectValue;
    }

    public List<InvestmentsLinkFile> getCrReferenceFVId() {
        return crReferenceFVId;
    }

    public void setCrReferenceFVId(List<InvestmentsLinkFile> crReferenceFVId) {
        this.crReferenceFVId = crReferenceFVId;
    }

    public Boolean getCrLandProvided() {
        return crLandProvided;
    }

    public void setCrLandProvided(Boolean crLandProvided) {
        this.crLandProvided = crLandProvided;
    }

    public Boolean getCrLandIsConcessionaireOwner() {
        return crLandIsConcessionaireOwner;
    }

    public void setCrLandIsConcessionaireOwner(Boolean crLandIsConcessionaireOwner) {
        this.crLandIsConcessionaireOwner = crLandIsConcessionaireOwner;
    }

    public Date getCrLandActStartPlanDate() {
        return crLandActStartPlanDate;
    }

    public void setCrLandActStartPlanDate(Date crLandActStartPlanDate) {
        this.crLandActStartPlanDate = crLandActStartPlanDate;
    }

    public Date getCrLandActStartFactDate() {
        return crLandActStartFactDate;
    }

    public void setCrLandActStartFactDate(Date crLandActStartFactDate) {
        this.crLandActStartFactDate = crLandActStartFactDate;
    }

    public Date getCrLandActEndPlanDate() {
        return crLandActEndPlanDate;
    }

    public void setCrLandActEndPlanDate(Date crLandActEndPlanDate) {
        this.crLandActEndPlanDate = crLandActEndPlanDate;
    }

    public Date getCrLandActEndFactDate() {
        return crLandActEndFactDate;
    }

    public void setCrLandActEndFactDate(Date crLandActEndFactDate) {
        this.crLandActEndFactDate = crLandActEndFactDate;
    }

    public List<LandActFile> getCrLandActFVId() {
        return crLandActFVId;
    }

    public void setCrLandActFVId(List<LandActFile> crLandActFVId) {
        this.crLandActFVId = crLandActFVId;
    }

    public Boolean getCrIsObligationExecuteOnCreationStage() {
        return crIsObligationExecuteOnCreationStage;
    }

    public void setCrIsObligationExecuteOnCreationStage(Boolean crIsObligationExecuteOnCreationStage) {
        this.crIsObligationExecuteOnCreationStage = crIsObligationExecuteOnCreationStage;
    }

    public List<CreationEnsureMethod> getCrEnsureMethods() {
        return crEnsureMethods;
    }

    public void setCrEnsureMethods(List<CreationEnsureMethod> crEnsureMethods) {
        this.crEnsureMethods = crEnsureMethods;
    }

    public List<Long> getCrGovSupport() {
        return crGovSupport;
    }

    public void setCrGovSupport(List<Long> crGovSupport) {
        this.crGovSupport = crGovSupport;
    }

    public List<ConfirmationDocFile> getCrConfirmationDocFVId() {
        return crConfirmationDocFVId;
    }

    public void setCrConfirmationDocFVId(List<ConfirmationDocFile> crConfirmationDocFVId) {
        this.crConfirmationDocFVId = crConfirmationDocFVId;
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

    public List<LastObjectActFile> getExLastObjectActFVId() {
        return exLastObjectActFVId;
    }

    public void setExLastObjectActFVId(List<LastObjectActFile> exLastObjectActFVId) {
        this.exLastObjectActFVId = exLastObjectActFVId;
    }

    public Boolean getExIsInvestmentsRecoveryProvided() {
        return exIsInvestmentsRecoveryProvided;
    }

    public void setExIsInvestmentsRecoveryProvided(Boolean exIsInvestmentsRecoveryProvided) {
        this.exIsInvestmentsRecoveryProvided = exIsInvestmentsRecoveryProvided;
    }

    public ExploitationInvestmentsRecovery getExInvestmentExploitationRecoveryAmount() {
        return exInvestmentExploitationRecoveryAmount;
    }

    public void setExInvestmentExploitationRecoveryAmount(ExploitationInvestmentsRecovery exInvestmentExploitationRecoveryAmount) {
        this.exInvestmentExploitationRecoveryAmount = exInvestmentExploitationRecoveryAmount;
    }

    public List<InvestmentRecoveryFinancialModelFile> getExInvestmentRecoveryFinancialModelFileVersionId() {
        return exInvestmentRecoveryFinancialModelFileVersionId;
    }

    public void setExInvestmentRecoveryFinancialModelFileVersionId(List<InvestmentRecoveryFinancialModelFile> exInvestmentRecoveryFinancialModelFileVersionId) {
        this.exInvestmentRecoveryFinancialModelFileVersionId = exInvestmentRecoveryFinancialModelFileVersionId;
    }

    public Long getExIRSource() {
        return exIRSource;
    }

    public void setExIRSource(Long exIRSource) {
        this.exIRSource = exIRSource;
    }

    public Long getExIRLevel() {
        return exIRLevel;
    }

    public void setExIRLevel(Long exIRLevel) {
        this.exIRLevel = exIRLevel;
    }

    public Boolean getExIsObligationExecutingOnOperationPhase() {
        return exIsObligationExecutingOnOperationPhase;
    }

    public void setExIsObligationExecutingOnOperationPhase(Boolean exIsObligationExecutingOnOperationPhase) {
        this.exIsObligationExecutingOnOperationPhase = exIsObligationExecutingOnOperationPhase;
    }

    /*
        public Long getExMethodOfExecOfPublicPartnerObligation() {
            return exMethodOfExecOfPublicPartnerObligation;
        }

        public void setExMethodOfExecOfPublicPartnerObligation(Long exMethodOfExecOfPublicPartnerObligation) {
            this.exMethodOfExecOfPublicPartnerObligation = exMethodOfExecOfPublicPartnerObligation;
        }

        public Date getExInvestmentRecoveryTerm() {
            return exInvestmentRecoveryTerm;
        }

        public void setExInvestmentRecoveryTerm(Date exInvestmentRecoveryTerm) {
            this.exInvestmentRecoveryTerm = exInvestmentRecoveryTerm;
        }

        public Double getExInvestmentRecoveryValue() {
            return exInvestmentRecoveryValue;
        }

        public void setExInvestmentRecoveryValue(Double exInvestmentRecoveryValue) {
            this.exInvestmentRecoveryValue = exInvestmentRecoveryValue;
        }
    */
    public List<ExploitationEnsureMethod> getExEnsureMethods() {
        return exEnsureMethods;
    }

    public void setExEnsureMethods(List<ExploitationEnsureMethod> exEnsureMethods) {
        this.exEnsureMethods = exEnsureMethods;
    }

    public Boolean getExIsRenewableBankGuarantee() {
        return exIsRenewableBankGuarantee;
    }

    public void setExIsRenewableBankGuarantee(Boolean exIsRenewableBankGuarantee) {
        this.exIsRenewableBankGuarantee = exIsRenewableBankGuarantee;
    }

    public Boolean getExIsGuaranteeVariesByYear() {
        return exIsGuaranteeVariesByYear;
    }

    public void setExIsGuaranteeVariesByYear(Boolean exIsGuaranteeVariesByYear) {
        this.exIsGuaranteeVariesByYear = exIsGuaranteeVariesByYear;
    }

    public ExBankGuarantee getExBankGuaranteeByYears() {
        return exBankGuaranteeByYears;
    }

    public void setExBankGuaranteeByYears(ExBankGuarantee exBankGuaranteeByYears) {
        this.exBankGuaranteeByYears = exBankGuaranteeByYears;
    }

    public Boolean getExIsConcessionPayProvideded() {
        return exIsConcessionPayProvideded;
    }

    public void setExIsConcessionPayProvideded(Boolean exIsConcessionPayProvideded) {
        this.exIsConcessionPayProvideded = exIsConcessionPayProvideded;
    }

    public Long getExPaymentForm() {
        return exPaymentForm;
    }

    public void setExPaymentForm(Long exPaymentForm) {
        this.exPaymentForm = exPaymentForm;
    }

    public Long getTmCause() {
        return tmCause;
    }

    public void setTmCause(Long tmCause) {
        this.tmCause = tmCause;
    }

    public Date getTmActPlanDate() {
        return tmActPlanDate;
    }

    public void setTmActPlanDate(Date tmActPlanDate) {
        this.tmActPlanDate = tmActPlanDate;
    }

    public Date getTmActFactDate() {
        return tmActFactDate;
    }

    public void setTmActFactDate(Date tmActFactDate) {
        this.tmActFactDate = tmActFactDate;
    }

    public String getTmActNumber() {
        return tmActNumber;
    }

    public void setTmActNumber(String tmActNumber) {
        this.tmActNumber = tmActNumber;
    }

    public Date getTmActDate() {
        return tmActDate;
    }

    public void setTmActDate(Date tmActDate) {
        this.tmActDate = tmActDate;
    }

    public List<TerminationActFile> getTmTextFileVersionId() {
        return tmTextFileVersionId;
    }

    public void setTmTextFileVersionId(List<TerminationActFile> tmTextFileVersionId) {
        this.tmTextFileVersionId = tmTextFileVersionId;
    }

    public String getTmCauseDescription() {
        return tmCauseDescription;
    }

    public void setTmCauseDescription(String tmCauseDescription) {
        this.tmCauseDescription = tmCauseDescription;
    }

    public Date getTmPlanDate() {
        return tmPlanDate;
    }

    public void setTmPlanDate(Date tmPlanDate) {
        this.tmPlanDate = tmPlanDate;
    }

    public Date getTmFactDate() {
        return tmFactDate;
    }

    public void setTmFactDate(Date tmFactDate) {
        this.tmFactDate = tmFactDate;
    }

    public List<TerminationActTextFile> getTmTaActTextFileVersionId() {
        return tmTaActTextFileVersionId;
    }

    public void setTmTaActTextFileVersionId(List<TerminationActTextFile> tmTaActTextFileVersionId) {
        this.tmTaActTextFileVersionId = tmTaActTextFileVersionId;
    }

    public Boolean getTmPropertyJointProvided() {
        return tmPropertyJointProvided;
    }

    public void setTmPropertyJointProvided(Boolean tmPropertyJointProvided) {
        this.tmPropertyJointProvided = tmPropertyJointProvided;
    }

    public Double getTmPropertyJointPrivatePercent() {
        return tmPropertyJointPrivatePercent;
    }

    public void setTmPropertyJointPrivatePercent(Double tmPropertyJointPrivatePercent) {
        this.tmPropertyJointPrivatePercent = tmPropertyJointPrivatePercent;
    }

    public Double getTmPropertyJointPublicPercent() {
        return tmPropertyJointPublicPercent;
    }

    public void setTmPropertyJointPublicPercent(Double tmPropertyJointPublicPercent) {
        this.tmPropertyJointPublicPercent = tmPropertyJointPublicPercent;
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

    public List<CompositionOfCompensation> getTmCompositionOfCompensationView() {
        return tmCompositionOfCompensationView;
    }

    public void setTmCompositionOfCompensationView(List<CompositionOfCompensation> tmCompositionOfCompensationView) {
        this.tmCompositionOfCompensationView = tmCompositionOfCompensationView;
    }

    public List<TerminationCompensationFile> getTmCompensationFVIds() {
        return tmCompensationFVIds;
    }

    public void setTmCompensationFVIds(List<TerminationCompensationFile> tmCompensationFVIds) {
        this.tmCompensationFVIds = tmCompensationFVIds;
    }

    public Long getTmAftermath() {
        return tmAftermath;
    }

    public void setTmAftermath(Long tmAftermath) {
        this.tmAftermath = tmAftermath;
    }

    public String getTmContractNumber() {
        return tmContractNumber;
    }

    public void setTmContractNumber(String tmContractNumber) {
        this.tmContractNumber = tmContractNumber;
    }

    public Date getTmContractDate() {
        return tmContractDate;
    }

    public void setTmContractDate(Date tmContractDate) {
        this.tmContractDate = tmContractDate;
    }

    public Long getTmProjectId() {
        return tmProjectId;
    }

    public void setTmProjectId(Long tmProjectId) {
        this.tmProjectId = tmProjectId;
    }

    public Double getTmPublicShare() {
        return tmPublicShare;
    }

    public void setTmPublicShare(Double tmPublicShare) {
        this.tmPublicShare = tmPublicShare;
    }

    public Boolean getTmIsRfHasShare() {
        return tmIsRfHasShare;
    }

    public void setTmIsRfHasShare(Boolean tmIsRfHasShare) {
        this.tmIsRfHasShare = tmIsRfHasShare;
    }

    public String getTmAnotherDescription() {
        return tmAnotherDescription;
    }

    public void setTmAnotherDescription(String tmAnotherDescription) {
        this.tmAnotherDescription = tmAnotherDescription;
    }

    public String getTmClausesOfAgreement() {
        return tmClausesOfAgreement;
    }

    public void setTmClausesOfAgreement(String tmClausesOfAgreement) {
        this.tmClausesOfAgreement = tmClausesOfAgreement;
    }

    public Boolean getTmCompensationLimit() {
        return tmCompensationLimit;
    }

    public void setTmCompensationLimit(Boolean tmCompensationLimit) {
        this.tmCompensationLimit = tmCompensationLimit;
    }

    public Boolean getTmAgreementTerminated() {
        return tmAgreementTerminated;
    }

    public void setTmAgreementTerminated(Boolean tmAgreementTerminated) {
        this.tmAgreementTerminated = tmAgreementTerminated;
    }

    public Double getTmCompensationSum() {
        return tmCompensationSum;
    }

    public void setTmCompensationSum(Double tmCompensationSum) {
        this.tmCompensationSum = tmCompensationSum;
    }

    public Boolean getTmNdsCheck() {
        return tmNdsCheck;
    }

    public void setTmNdsCheck(Boolean tmNdsCheck) {
        this.tmNdsCheck = tmNdsCheck;
    }

    public Long getTmMeasureType() {
        return tmMeasureType;
    }

    public void setTmMeasureType(Long tmMeasureType) {
        this.tmMeasureType = tmMeasureType;
    }

    public Long getTmDateField() {
        return tmDateField;
    }

    public void setTmDateField(Long tmDateField) {
        this.tmDateField = tmDateField;
    }

    public Double getTmCompositionOfCompensation() {
        return tmCompositionOfCompensation;
    }

    public void setTmCompositionOfCompensation(Double tmCompositionOfCompensation) {
        this.tmCompositionOfCompensation = tmCompositionOfCompensation;
    }

    public List<TerminationSupportingDocumentsFile> getTmSupportingDocuments() {
        return tmSupportingDocuments;
    }

    public void setTmSupportingDocuments(List<TerminationSupportingDocumentsFile> tmSupportingDocuments) {
        this.tmSupportingDocuments = tmSupportingDocuments;
    }

    public List<Long> getTmCompositionOfCompensationGrantorFault() {
        return tmCompositionOfCompensationGrantorFault;
    }

    public void setTmCompositionOfCompensationGrantorFault(List<Long> tmCompositionOfCompensationGrantorFault) {
        this.tmCompositionOfCompensationGrantorFault = tmCompositionOfCompensationGrantorFault;
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

    public String getCcActNumber() {
        return ccActNumber;
    }

    public void setCcActNumber(String ccActNumber) {
        this.ccActNumber = ccActNumber;
    }

    public Date getCcActDate() {
        return ccActDate;
    }

    public void setCcActDate(Date ccActDate) {
        this.ccActDate = ccActDate;
    }

    public List<ChangeTextFile> getCcTextFileVersionId() {
        return ccTextFileVersionId;
    }

    public void setCcTextFileVersionId(List<ChangeTextFile> ccTextFileVersionId) {
        this.ccTextFileVersionId = ccTextFileVersionId;
    }

    public CreationInvestments getCrInvestmentCreationAmount() {
        return crInvestmentCreationAmount;
    }

    public List<InvestmentVolumeStagOfCreationActFile> getCrInvestmentVolumeStagOfCreationActFVId() {
        return crInvestmentVolumeStagOfCreationActFVId;
    }

    public void setCrInvestmentVolumeStagOfCreationActFVId(List<InvestmentVolumeStagOfCreationActFile> crInvestmentVolumeStagOfCreationActFVId) {
        this.crInvestmentVolumeStagOfCreationActFVId = crInvestmentVolumeStagOfCreationActFVId;
    }


    public void setCrInvestmentCreationAmount(CreationInvestments crInvestmentCreationAmount) {
        this.crInvestmentCreationAmount = crInvestmentCreationAmount;
    }

    public String getCrExpectedRepaymentYear() {
        return crExpectedRepaymentYear;
    }

    public void setCrExpectedRepaymentYear(String crExpectedRepaymentYear) {
        this.crExpectedRepaymentYear = crExpectedRepaymentYear;
    }

    public List<BalanceOfDebt> getCrBalanceOfDebt() {
        return crBalanceOfDebt;
    }

    public void setCrBalanceOfDebt(List<BalanceOfDebt> crBalanceOfDebt) {
        this.crBalanceOfDebt = crBalanceOfDebt;
    }

    public ExploitationInvestments getExInvestmentExploitationAmount() {
        return exInvestmentExploitationAmount;
    }

    public void setExInvestmentExploitationAmount(ExploitationInvestments exInvestmentExploitationAmount) {
        this.exInvestmentExploitationAmount = exInvestmentExploitationAmount;
    }

    public List<InvestmentVolumeStagOfExploitationActFile> getExInvestmentVolumeStagOfExploitationActFVId() {
        return exInvestmentVolumeStagOfExploitationActFVId;
    }

    public void setExInvestmentVolumeStagOfExploitationActFVId(List<InvestmentVolumeStagOfExploitationActFile> exInvestmentVolumeStagOfExploitationActFVId) {
        this.exInvestmentVolumeStagOfExploitationActFVId = exInvestmentVolumeStagOfExploitationActFVId;
    }

    public ExploitationPayment getExPayment() {
        return exPayment;
    }

    public void setExPayment(ExploitationPayment exPayment) {
        this.exPayment = exPayment;
    }

    public BigDecimal getExPublicShareExpl() {
        return exPublicShareExpl;
    }

    public void setExPublicShareExpl(BigDecimal exPublicShareExpl) {
        this.exPublicShareExpl = exPublicShareExpl;
    }

    public Boolean getExHasPublicShareExpl() {
        return exHasPublicShareExpl;
    }

    public void setExHasPublicShareExpl(Boolean exHasPublicShareExpl) {
        this.exHasPublicShareExpl = exHasPublicShareExpl;
    }

    public List<ExploitationFinModelFile> getExFinModelFVIds() {
        return exFinModelFVIds;
    }

    public void setExFinModelFVIds(List<ExploitationFinModelFile> exFinModelFVIds) {
        this.exFinModelFVIds = exFinModelFVIds;
    }

    public List<Long> getExCostRecoveryMethod() {
        return exCostRecoveryMethod;
    }

    public void setExCostRecoveryMethod(List<Long> exCostRecoveryMethod) {
        this.exCostRecoveryMethod = exCostRecoveryMethod;
    }

    public String getExCostRecoveryMechanism() {
        return exCostRecoveryMechanism;
    }

    public void setExCostRecoveryMechanism(String exCostRecoveryMechanism) {
        this.exCostRecoveryMechanism = exCostRecoveryMechanism;
    }

    public List<ExploitationSupportDoclFile> getExSupportDocFVIds() {
        return exSupportDocFVIds;
    }

    public void setExSupportDocFVIds(List<ExploitationSupportDoclFile> exSupportDocFVIds) {
        this.exSupportDocFVIds = exSupportDocFVIds;
    }

    public ExploitationCompensation getExCompensation() {
        return exCompensation;
    }

    public void setExCompensation(ExploitationCompensation exploitationCompensation) {
        this.exCompensation = exploitationCompensation;
    }

    public List<ExploitationSupportCompensDoclFile> getExSupportCompensDocFVIds() {
        return exSupportCompensDocFVIds;
    }

    public void setExSupportCompensDocFVIds(List<ExploitationSupportCompensDoclFile> exSupportCompensDocFVIds) {
        this.exSupportCompensDocFVIds = exSupportCompensDocFVIds;
    }

    public Date getExOwnPrivatePlanDate() {
        return exOwnPrivatePlanDate;
    }

    public void setExOwnPrivatePlanDate(Date exOwnPrivatePlanDate) {
        this.exOwnPrivatePlanDate = exOwnPrivatePlanDate;
    }

    public Date getExOwnPrivateFactDate() {
        return exOwnPrivateFactDate;
    }

    public void setExOwnPrivateFactDate(Date exOwnPrivateFactDate) {
        this.exOwnPrivateFactDate = exOwnPrivateFactDate;
    }

    public Date getExOwnPublicPlanDate() {
        return exOwnPublicPlanDate;
    }

    public void setExOwnPublicPlanDate(Date exOwnPublicPlanDate) {
        this.exOwnPublicPlanDate = exOwnPublicPlanDate;
    }

    public Date getExOwnPublicFactDate() {
        return exOwnPublicFactDate;
    }

    public void setExOwnPublicFactDate(Date exOwnPublicFactDate) {
        this.exOwnPublicFactDate = exOwnPublicFactDate;
    }

    public List<ExploitationAgreementFile> getExAgreementFVIds() {
        return exAgreementFVIds;
    }

    public void setExAgreementFVIds(List<ExploitationAgreementFile> exAgreementFVIds) {
        this.exAgreementFVIds = exAgreementFVIds;
    }

    public List<ExploitationAcceptActFile> getExAcceptActFVIds() {
        return exAcceptActFVIds;
    }

    public void setExAcceptActFVIds(List<ExploitationAcceptActFile> exAcceptActFVIds) {
        this.exAcceptActFVIds = exAcceptActFVIds;
    }

    public Date getExStartAchEconPlanDate() {
        return exStartAchEconPlanDate;
    }

    public void setExStartAchEconPlanDate(Date exStartAchEconPlanDate) {
        this.exStartAchEconPlanDate = exStartAchEconPlanDate;
    }

    public Date getExEndAchEconPlanDate() {
        return exEndAchEconPlanDate;
    }

    public void setExEndAchEconPlanDate(Date exEndAchEconPlanDate) {
        this.exEndAchEconPlanDate = exEndAchEconPlanDate;
    }

    public Date getExStartAchEconFactDate() {
        return exStartAchEconFactDate;
    }

    public void setExStartAchEconFactDate(Date exStartAchEconFactDate) {
        this.exStartAchEconFactDate = exStartAchEconFactDate;
    }

    public Date getExEndAchEconFactDate() {
        return exEndAchEconFactDate;
    }

    public void setExEndAchEconFactDate(Date exEndAchEconFactDate) {
        this.exEndAchEconFactDate = exEndAchEconFactDate;
    }

    public List<ExploitationAcceptActAAMFile> getExAcceptActAAMFVIds() {
        return exAcceptActAAMFVIds;
    }

    public void setExAcceptActAAMFVIds(List<ExploitationAcceptActAAMFile> exAcceptActAAMFVIds) {
        this.exAcceptActAAMFVIds = exAcceptActAAMFVIds;
    }

    public List<ExCalculationPlannedAmountFile> getExCalculationPlannedAmountFVIds() {
        return exCalculationPlannedAmountFVIds;
    }

    public void setExCalculationPlannedAmountFVIds(List<ExCalculationPlannedAmountFile> exCalculationPlannedAmountFVIds) {
        this.exCalculationPlannedAmountFVIds = exCalculationPlannedAmountFVIds;
    }

    public Date getExInvestStagePlanDate() {
        return exInvestStagePlanDate;
    }

    public void setExInvestStagePlanDate(Date exInvestStagePlanDate) {
        this.exInvestStagePlanDate = exInvestStagePlanDate;
    }

    public Date getExInvestStageFactDate() {
        return exInvestStageFactDate;
    }

    public void setExInvestStageFactDate(Date exInvestStageFactDate) {
        this.exInvestStageFactDate = exInvestStageFactDate;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public List<Event> getAdsEvents() {
        return adsEvents;
    }

    public void setAdsEvents(List<Event> adsEvents) {
        this.adsEvents = adsEvents;
    }

    public Boolean getAdsIsThirdPartyOrgsProvided() {
        return adsIsThirdPartyOrgsProvided;
    }

    public void setAdsIsThirdPartyOrgsProvided(Boolean adsIsThirdPartyOrgsProvided) {
        this.adsIsThirdPartyOrgsProvided = adsIsThirdPartyOrgsProvided;
    }


    public Boolean getAdsIsRegInvestmentProject() {
        return adsIsRegInvestmentProject;
    }

    public void setAdsIsRegInvestmentProject(Boolean adsIsRegInvestmentProject) {
        this.adsIsRegInvestmentProject = adsIsRegInvestmentProject;
    }

    public Boolean getAdsHasIncomeTax() {
        return adsHasIncomeTax;
    }

    public void setAdsHasIncomeTax(Boolean adsHasIncomeTax) {
        this.adsHasIncomeTax = adsHasIncomeTax;
    }

    public List<InvestmentInObjectMainIndicator> getFeiInvestmentsInObject() {
        return feiInvestmentsInObject;
    }

    public void setFeiInvestmentsInObject(List<InvestmentInObjectMainIndicator> feiInvestmentsInObject) {
        this.feiInvestmentsInObject = feiInvestmentsInObject;
    }

    public List<OperationalCostsIndicator> getFeiOperationalCosts() {
        return feiOperationalCosts;
    }

    public void setFeiOperationalCosts(List<OperationalCostsIndicator> feiOperationalCosts) {
        this.feiOperationalCosts = feiOperationalCosts;
    }

    public List<TaxConditionIndicator> getFeiTaxCondition() {
        return feiTaxCondition;
    }

    public void setFeiTaxCondition(List<TaxConditionIndicator> feiTaxCondition) {
        this.feiTaxCondition = feiTaxCondition;
    }

    public List<RevenueServiceIndicator> getFeiRevenueService() {
        return feiRevenue;
    }

    public void setFeiRevenueService(List<RevenueServiceIndicator> feiRevenue) {
        this.feiRevenue = feiRevenue;
    }

    public Boolean getFeiTaxIncentivesExist() {
        return feiTaxIncentivesExist;
    }

    public void setFeiTaxIncentivesExist(Boolean feiTaxIncentivesExist) {
        this.feiTaxIncentivesExist = feiTaxIncentivesExist;
    }

    public Double getFeiResidualValue() {
        return feiResidualValue;
    }

    public void setFeiResidualValue(Double feiResidualValue) {
        this.feiResidualValue = feiResidualValue;
    }

    public Double getFeiAverageServiceLife() {
        return feiAverageServiceLife;
    }

    public void setFeiAverageServiceLife(Double feiAverageServiceLife) {
        this.feiAverageServiceLife = feiAverageServiceLife;
    }

    public Long getFeiForecastValuesDate() {
        return feiForecastValuesDate;
    }

    public void setFeiForecastValuesDate(Long feiForecastValuesDate) {
        this.feiForecastValuesDate = feiForecastValuesDate;
    }

    public Long getAdsIncomeTaxRate() {
        return adsIncomeTaxRate;
    }

    public void setAdsIncomeTaxRate(Long adsIncomeTaxRate) {
        this.adsIncomeTaxRate = adsIncomeTaxRate;
    }

    public Boolean getAdsHasLandTax() {
        return adsHasLandTax;
    }

    public void setAdsHasLandTax(Boolean adsHasLandTax) {
        this.adsHasLandTax = adsHasLandTax;
    }

    public Long getAdsLandTaxRate() {
        return adsLandTaxRate;
    }

    public void setAdsLandTaxRate(Long adsLandTaxRate) {
        this.adsLandTaxRate = adsLandTaxRate;
    }

    public Boolean getAdsHasPropertyTax() {
        return adsHasPropertyTax;
    }

    public void setAdsHasPropertyTax(Boolean adsHasPropertyTax) {
        this.adsHasPropertyTax = adsHasPropertyTax;
    }

    public Long getAdsPropertyTaxRate() {
        return adsPropertyTaxRate;
    }

    public void setAdsPropertyTaxRate(Long adsPropertyTaxRate) {
        this.adsPropertyTaxRate = adsPropertyTaxRate;
    }

    public Boolean getAdsHasBenefitClarificationTax() {
        return adsHasBenefitClarificationTax;
    }

    public void setAdsHasBenefitClarificationTax(Boolean adsHasBenefitClarificationTax) {
        this.adsHasBenefitClarificationTax = adsHasBenefitClarificationTax;
    }

    public Long getAdsBenefitClarificationRate() {
        return adsBenefitClarificationRate;
    }

    public void setAdsBenefitClarificationRate(Long adsBenefitClarificationRate) {
        this.adsBenefitClarificationRate = adsBenefitClarificationRate;
    }

    public String getAdsBenefitDescription() {
        return adsBenefitDescription;
    }

    public void setAdsBenefitDescription(String adsBenefitDescription) {
        this.adsBenefitDescription = adsBenefitDescription;
    }

    public Boolean getAdsIsTreasurySupport() {
        return adsIsTreasurySupport;
    }

    public void setAdsIsTreasurySupport(Boolean adsIsTreasurySupport) {
        this.adsIsTreasurySupport = adsIsTreasurySupport;
    }

    public List<AdsDecisionTextFile> getAdsDecisionTextFileId() {
        return adsDecisionTextFileId;
    }

    public void setAdsDecisionTextFileId(List<AdsDecisionTextFile> adsDecisionTextFileId) {
        this.adsDecisionTextFileId = adsDecisionTextFileId;
    }

    public List<PrivatePartnerThirdPartyOrg> getAdsPrivatePartnerThirdPartyOrgs() {
        return adsPrivatePartnerThirdPartyOrgs;
    }

    public void setAdsPrivatePartnerThirdPartyOrgs(List<PrivatePartnerThirdPartyOrg> adsPrivatePartnerThirdPartyOrgs) {
        this.adsPrivatePartnerThirdPartyOrgs = adsPrivatePartnerThirdPartyOrgs;
    }

    public List<PublicPartnerThirdPartyOrg> getAdsPublicPartnerThirdPartyOrgs() {
        return adsPublicPartnerThirdPartyOrgs;
    }

    public void setAdsPublicPartnerThirdPartyOrgs(List<PublicPartnerThirdPartyOrg> adsPublicPartnerThirdPartyOrgs) {
        this.adsPublicPartnerThirdPartyOrgs = adsPublicPartnerThirdPartyOrgs;
    }

    public Double getAdsNpv() {
        return adsNpv;
    }

    public void setAdsNpv(Double adsNpv) {
        this.adsNpv = adsNpv;
    }

    public Double getAdsIrr() {
        return adsIrr;
    }

    public void setAdsIrr(Double adsIrr) {
        this.adsIrr = adsIrr;
    }

    public Double getAdsPb() {
        return adsPb;
    }

    public void setAdsPb(Double adsPb) {
        this.adsPb = adsPb;
    }

    public Double getAdsPbDiscounted() {
        return adsPbDiscounted;
    }

    public void setAdsPbDiscounted(Double adsPbDiscounted) {
        this.adsPbDiscounted = adsPbDiscounted;
    }

    public Double getAdsEbidta() {
        return adsEbidta;
    }

    public void setAdsEbidta(Double adsEbidta) {
        this.adsEbidta = adsEbidta;
    }

    public Double getAdsWacc() {
        return adsWacc;
    }

    public void setAdsWacc(Double adsWacc) {
        this.adsWacc = adsWacc;
    }

    public Long getAdsTaxRelief() {
        return adsTaxRelief;
    }

    public void setAdsTaxRelief(Long adsTaxRelief) {
        this.adsTaxRelief = adsTaxRelief;
    }

    public List<Sanction> getAdsSanctions() {
        return adsSanctions;
    }

    public void setAdsSanctions(List<Sanction> adsSanctions) {
        this.adsSanctions = adsSanctions;
    }

    public List<JudicialActivity> getAdsJudicialActivities() {
        return adsJudicialActivities;
    }

    public void setAdsJudicialActivities(List<JudicialActivity> adsJudicialActivities) {
        this.adsJudicialActivities = adsJudicialActivities;
    }

    public Long getAdsConcessionaireOpf() {
        return adsConcessionaireOpf;
    }

    public void setAdsConcessionaireOpf(Long adsConcessionaireOpf) {
        this.adsConcessionaireOpf = adsConcessionaireOpf;
    }

    public String getAdsConcessionaireName() {
        return adsConcessionaireName;
    }

    public void setAdsConcessionaireName(String adsConcessionaireName) {
        this.adsConcessionaireName = adsConcessionaireName;
    }

    public String getAdsConcessionaireInn() {
        return adsConcessionaireInn;
    }

    public void setAdsConcessionaireInn(String adsConcessionaireInn) {
        this.adsConcessionaireInn = adsConcessionaireInn;
    }

    public String getAdsConcessionaireRegime() {
        return adsConcessionaireRegime;
    }

    public void setAdsConcessionaireRegime(String adsConcessionaireRegime) {
        this.adsConcessionaireRegime = adsConcessionaireRegime;
    }

    public String getAdsConcessionaireCreditRating() {
        return adsConcessionaireCreditRating;
    }

    public void setAdsConcessionaireCreditRating(String adsConcessionaireCreditRating) {
        this.adsConcessionaireCreditRating = adsConcessionaireCreditRating;
    }

    public Date getAdsConcessionaireCreditRatingStartDate() {
        return adsConcessionaireCreditRatingStartDate;
    }

    public void setAdsConcessionaireCreditRatingStartDate(Date adsConcessionaireCreditRatingStartDate) {
        this.adsConcessionaireCreditRatingStartDate = adsConcessionaireCreditRatingStartDate;
    }

    public Date getAdsConcessionaireCreditRatingEndDate() {
        return adsConcessionaireCreditRatingEndDate;
    }

    public void setAdsConcessionaireCreditRatingEndDate(Date adsConcessionaireCreditRatingEndDate) {
        this.adsConcessionaireCreditRatingEndDate = adsConcessionaireCreditRatingEndDate;
    }

    public String getAdsConcessionaireCreditRatingAgency() {
        return adsConcessionaireCreditRatingAgency;
    }

    public void setAdsConcessionaireCreditRatingAgency(String adsConcessionaireCreditRatingAgency) {
        this.adsConcessionaireCreditRatingAgency = adsConcessionaireCreditRatingAgency;
    }

    public List<OwnershipStructure> getAdsOwnershipStructures() {
        return adsOwnershipStructures;
    }

    public void setAdsOwnershipStructures(List<OwnershipStructure> adsOwnershipStructures) {
        this.adsOwnershipStructures = adsOwnershipStructures;
    }

    public List<FinancialStructure> getAdsFinancialStructure() {
        return adsFinancialStructure;
    }

    public void setAdsFinancialStructure(List<FinancialStructure> adsFinancialStructure) {
        this.adsFinancialStructure = adsFinancialStructure;
    }

    public List<InvestmentsCriteriaBoolean> getAdsInvestmentBoolCriterias() {
        return adsInvestmentBoolCriterias;
    }

    public void setAdsInvestmentBoolCriterias(List<InvestmentsCriteriaBoolean> adsInvestmentBoolCriterias) {
        this.adsInvestmentBoolCriterias = adsInvestmentBoolCriterias;
    }

    public Double getAdsUnforeseenExpencesShare() {
        return adsUnforeseenExpencesShare;
    }

    public void setAdsUnforeseenExpencesShare(Double adsUnforeseenExpencesShare) {
        this.adsUnforeseenExpencesShare = adsUnforeseenExpencesShare;
    }

    public String getAdsUnforeseenExpencesShareComment() {
        return adsUnforeseenExpencesShareComment;
    }

    public void setAdsUnforeseenExpencesShareComment(String adsUnforeseenExpencesShareComment) {
        this.adsUnforeseenExpencesShareComment = adsUnforeseenExpencesShareComment;
    }

    public List<Long> getAdsCompetitionCriteria() {
        return adsCompetitionCriteria;
    }

    public void setAdsCompetitionCriteria(List<Long> adsCompetitionCriteria) {
        this.adsCompetitionCriteria = adsCompetitionCriteria;
    }

    public List<Long> getAdsFinancialRequirement() {
        return adsFinancialRequirement;
    }

    public void setAdsFinancialRequirement(List<Long> adsFinancialRequirement) {
        this.adsFinancialRequirement = adsFinancialRequirement;
    }

    public List<Long> getAdsNonFinancialRequirements() {
        return adsNonFinancialRequirements;
    }

    public void setAdsNonFinancialRequirements(List<Long> adsNonFinancialRequirements) {
        this.adsNonFinancialRequirements = adsNonFinancialRequirements;
    }

    public String getCmComment() {
        return cmComment;
    }

    public void setCmComment(String cmComment) {
        this.cmComment = cmComment;
    }

    public Long getCrAgreementComplex() {
        return crAgreementComplex;
    }

    public void setCrAgreementComplex(Long crAgreementComplex) {
        this.crAgreementComplex = crAgreementComplex;
    }

    public Date getCrJobDoneTerm() {
        return crJobDoneTerm;
    }

    public void setCrJobDoneTerm(Date crJobDoneTerm) {
        this.crJobDoneTerm = crJobDoneTerm;
    }

    public Date getCrSavingStartDate() {
        return crSavingStartDate;
    }

    public void setCrSavingStartDate(Date crSavingStartDate) {
        this.crSavingStartDate = crSavingStartDate;
    }

    public Date getCrSavingEndDate() {
        return crSavingEndDate;
    }

    public void setCrSavingEndDate(Date crSavingEndDate) {
        this.crSavingEndDate = crSavingEndDate;
    }

    public Double getCrInvestmentStageTerm() {
        return crInvestmentStageTerm;
    }

    public void setCrInvestmentStageTerm(Double crInvestmentStageTerm) {
        this.crInvestmentStageTerm = crInvestmentStageTerm;
    }

    public Boolean getCrIsAutoProlongationProvided() {
        return crIsAutoProlongationProvided;
    }

    public void setCrIsAutoProlongationProvided(Boolean crIsAutoProlongationProvided) {
        this.crIsAutoProlongationProvided = crIsAutoProlongationProvided;
    }

    public Date getCrAgreementEndDateAfterProlongation() {
        return crAgreementEndDateAfterProlongation;
    }

    public void setCrAgreementEndDateAfterProlongation(Date crAgreementEndDateAfterProlongation) {
        this.crAgreementEndDateAfterProlongation = crAgreementEndDateAfterProlongation;
    }

    public Double getCrAgreementValidityAfterProlongation() {
        return crAgreementValidityAfterProlongation;
    }

    public void setCrAgreementValidityAfterProlongation(Double crAgreementValidityAfterProlongation) {
        this.crAgreementValidityAfterProlongation = crAgreementValidityAfterProlongation;
    }

    public List<CreationAgreementFile> getCrAgreementTextFiles() {
        return crAgreementTextFiles;
    }

    public void setCrAgreementTextFiles(List<CreationAgreementFile> crAgreementTextFiles) {
        this.crAgreementTextFiles = crAgreementTextFiles;
    }

    public Long getCrOpf() {
        return crOpf;
    }

    public void setCrOpf(Long crOpf) {
        this.crOpf = crOpf;
    }

    public Boolean getCrIsForeignInvestor() {
        return crIsForeignInvestor;
    }

    public void setCrIsForeignInvestor(Boolean crIsForeignInvestor) {
        this.crIsForeignInvestor = crIsForeignInvestor;
    }

    public Boolean getCrIsMcpSubject() {
        return crIsMcpSubject;
    }

    public void setCrIsMcpSubject(Boolean crIsMcpSubject) {
        this.crIsMcpSubject = crIsMcpSubject;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
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

    public Long getSaveIndex() {
        return saveIndex;
    }

    public void setSaveIndex(Long saveIndex) {
        this.saveIndex = saveIndex;
    }

    public String getGiPublicPartnerOgrn() {
        return giPublicPartnerOgrn;
    }

    public void setGiPublicPartnerOgrn(String giPublicPartnerOgrn) {
        this.giPublicPartnerOgrn = giPublicPartnerOgrn;
    }

    public Boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getExGrantorExpenses() {
        return exGrantorExpenses;
    }

    public void setExGrantorExpenses(String exGrantorExpenses) {
        this.exGrantorExpenses = exGrantorExpenses;
    }

    public Boolean getExFormulasOrIndexingOrderEstablished() {
        return exFormulasOrIndexingOrderEstablished;
    }

    public void setExFormulasOrIndexingOrderEstablished(Boolean exFormulasOrIndexingOrderEstablished) {
        this.exFormulasOrIndexingOrderEstablished = exFormulasOrIndexingOrderEstablished;
    }

    public Boolean getCbcMinimumGuaranteedExist() {
        return cbcMinimumGuaranteedExist;
    }

    public void setCbcMinimumGuaranteedExist(Boolean cbcMinimumGuaranteedExist) {
        this.cbcMinimumGuaranteedExist = cbcMinimumGuaranteedExist;
    }

    public String getCbcMinimumGuaranteedClauseAgreement() {
        return cbcMinimumGuaranteedClauseAgreement;
    }

    public void setCbcMinimumGuaranteedClauseAgreement(String cbcMinimumGuaranteedClauseAgreement) {
        this.cbcMinimumGuaranteedClauseAgreement = cbcMinimumGuaranteedClauseAgreement;
    }

    public Boolean getCbcCompensationMinimumGuaranteedExist() {
        return cbcCompensationMinimumGuaranteedExist;
    }

    public void setCbcCompensationMinimumGuaranteedExist(Boolean cbcCompensationMinimumGuaranteedExist) {
        this.cbcCompensationMinimumGuaranteedExist = cbcCompensationMinimumGuaranteedExist;
    }

    public Boolean getCbcNonPaymentConsumersGoodsProvidedExist() {
        return cbcNonPaymentConsumersGoodsProvidedExist;
    }

    public void setCbcNonPaymentConsumersGoodsProvidedExist(Boolean cbcNonPaymentConsumersGoodsProvidedExist) {
        this.cbcNonPaymentConsumersGoodsProvidedExist = cbcNonPaymentConsumersGoodsProvidedExist;
    }

    public Boolean getCbcLimitNonPaymentConsumersGoodsProvidedExist() {
        return cbcLimitNonPaymentConsumersGoodsProvidedExist;
    }

    public void setCbcLimitNonPaymentConsumersGoodsProvidedExist(Boolean cbcLimitNonPaymentConsumersGoodsProvidedExist) {
        this.cbcLimitNonPaymentConsumersGoodsProvidedExist = cbcLimitNonPaymentConsumersGoodsProvidedExist;
    }

    public String getCbcLimitNonPayConsGoodsProvidedClauseAgree() {
        return cbcLimitNonPayConsGoodsProvidedClauseAgree;
    }

    public void setCbcLimitNonPayConsGoodsProvidedClauseAgree(String cbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement) {
        this.cbcLimitNonPayConsGoodsProvidedClauseAgree = cbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement;
    }

    public Boolean getCbcCompLimitNonPaymentConsGoodsProvidedExist() {
        return cbcCompLimitNonPaymentConsGoodsProvidedExist;
    }

    public void setCbcCompLimitNonPaymentConsGoodsProvidedExist(Boolean cbcCompensationLimitNonPaymentConsumersGoodsProvidedExist) {
        this.cbcCompLimitNonPaymentConsGoodsProvidedExist = cbcCompensationLimitNonPaymentConsumersGoodsProvidedExist;
    }

    public Boolean getCbcArisingProvisionOfBenefitsExist() {
        return cbcArisingProvisionOfBenefitsExist;
    }

    public void setCbcArisingProvisionOfBenefitsExist(Boolean cbcArisingProvisionOfBenefitsExist) {
        this.cbcArisingProvisionOfBenefitsExist = cbcArisingProvisionOfBenefitsExist;
    }

    public Boolean getCbcCompensationArisingProvisionOfBenefitsExist() {
        return cbcCompensationArisingProvisionOfBenefitsExist;
    }

    public void setCbcCompensationArisingProvisionOfBenefitsExist(Boolean cbcCompensationArisingProvisionOfBenefitsExist) {
        this.cbcCompensationArisingProvisionOfBenefitsExist = cbcCompensationArisingProvisionOfBenefitsExist;
    }

    public String getCbcCompArisingProvisionOfBenefitsClauseAgree() {
        return cbcCompArisingProvisionOfBenefitsClauseAgree;
    }

    public void setCbcCompArisingProvisionOfBenefitsClauseAgree(String cbcCompensationArisingProvisionOfBenefitsClauseAgreement) {
        this.cbcCompArisingProvisionOfBenefitsClauseAgree = cbcCompensationArisingProvisionOfBenefitsClauseAgreement;
    }

    public Boolean getCbcDueToOnsetOfCertainCircumstancesExist() {
        return cbcDueToOnsetOfCertainCircumstancesExist;
    }

    public void setCbcDueToOnsetOfCertainCircumstancesExist(Boolean cbcDueToOnsetOfCertainCircumstancesExist) {
        this.cbcDueToOnsetOfCertainCircumstancesExist = cbcDueToOnsetOfCertainCircumstancesExist;
    }

    public Boolean getCbcLimitCompAdditionalCostsAgreeExist() {
        return cbcLimitCompAdditionalCostsAgreeExist;
    }

    public void setCbcLimitCompAdditionalCostsAgreeExist(Boolean cbcLimitCompensationAdditionalCostsAgreementExist) {
        this.cbcLimitCompAdditionalCostsAgreeExist = cbcLimitCompensationAdditionalCostsAgreementExist;
    }

    public String getCbcLimitCompensationAdditionalClauseAgreement() {
        return cbcLimitCompensationAdditionalClauseAgreement;
    }

    public void setCbcLimitCompensationAdditionalClauseAgreement(String cbcLimitCompensationAdditionalClauseAgreement) {
        this.cbcLimitCompensationAdditionalClauseAgreement = cbcLimitCompensationAdditionalClauseAgreement;
    }

    public String getCbcSpecifyOtherCircumstancesPrepare() {
        return cbcSpecifyOtherCircumstancesPrepare;
    }

    public void setCbcSpecifyOtherCircumstancesPrepare(String cbcSpecifyOtherCircumstancesPrepare) {
        this.cbcSpecifyOtherCircumstancesPrepare = cbcSpecifyOtherCircumstancesPrepare;
    }

    public String getCbcSpecifyOtherCircumstancesBuild() {
        return cbcSpecifyOtherCircumstancesBuild;
    }

    public void setCbcSpecifyOtherCircumstancesBuild(String cbcSpecifyOtherCircumstancesBuild) {
        this.cbcSpecifyOtherCircumstancesBuild = cbcSpecifyOtherCircumstancesBuild;
    }

    public String getCbcSpecifyOtherCircumstancesExploitation() {
        return cbcSpecifyOtherCircumstancesExploitation;
    }

    public void setCbcSpecifyOtherCircumstancesExploitation(String cbcSpecifyOtherCircumstancesExploitation) {
        this.cbcSpecifyOtherCircumstancesExploitation = cbcSpecifyOtherCircumstancesExploitation;
    }

    public Boolean getCbcCompensationAdditionalCostsAgreementExist() {
        return cbcCompensationAdditionalCostsAgreementExist;
    }

    public void setCbcCompensationAdditionalCostsAgreementExist(Boolean cbcCompensationAdditionalCostsAgreementExist) {
        this.cbcCompensationAdditionalCostsAgreementExist = cbcCompensationAdditionalCostsAgreementExist;
    }

    public List<ArisingProvisionOfBenefitFile> getCbcArisingProvisionOfBenefitFVId() {
        return cbcArisingProvisionOfBenefitFVId;
    }

    public void setCbcArisingProvisionOfBenefitFVId(List<ArisingProvisionOfBenefitFile> cbcArisingProvisionOfBenefitFVId) {
        this.cbcArisingProvisionOfBenefitFVId = cbcArisingProvisionOfBenefitFVId;
    }

    public List<CompensationAdditionalCostsAgreementFile> getCbcCompensationAdditionalCostsAgreementFVId() {
        return cbcCompensationAdditionalCostsAgreementFVId;
    }

    public void setCbcCompensationAdditionalCostsAgreementFVId(List<CompensationAdditionalCostsAgreementFile> cbcCompensationAdditionalCostsAgreementFVId) {
        this.cbcCompensationAdditionalCostsAgreementFVId = cbcCompensationAdditionalCostsAgreementFVId;
    }

    public List<CompensationArisingProvisionOfBenefitsFile> getCbcCompensationArisingProvisionOfBenefitsFVId() {
        return cbcCompensationArisingProvisionOfBenefitsFVId;
    }

    public void setCbcCompensationArisingProvisionOfBenefitsFVId(List<CompensationArisingProvisionOfBenefitsFile> cbcCompensationArisingProvisionOfBenefitsFVId) {
        this.cbcCompensationArisingProvisionOfBenefitsFVId = cbcCompensationArisingProvisionOfBenefitsFVId;
    }

    public List<MinimumGuaranteedFile> getCbcMinimumGuaranteedFVId() {
        return cbcMinimumGuaranteedFVId;
    }

    public void setCbcMinimumGuaranteedFVId(List<MinimumGuaranteedFile> cbcMinimumGuaranteedFVId) {
        this.cbcMinimumGuaranteedFVId = cbcMinimumGuaranteedFVId;
    }

    public List<NonPaymentConsumersFile> getCbcNonPaymentConsumersFVId() {
        return cbcNonPaymentConsumersFVId;
    }

    public void setCbcNonPaymentConsumersFVId(List<NonPaymentConsumersFile> cbcNonPaymentConsumersFVId) {
        this.cbcNonPaymentConsumersFVId = cbcNonPaymentConsumersFVId;
    }

    public Long getCbcMinimumGuaranteedIncomeForm() {
        return cbcMinimumGuaranteedIncomeForm;
    }

    public void setCbcMinimumGuaranteedIncomeForm(Long cbcMinimumGuaranteedIncomeForm) {
        this.cbcMinimumGuaranteedIncomeForm = cbcMinimumGuaranteedIncomeForm;
    }

    public Long getCbcNonPaymentConsumersGoodsProvidedForm() {
        return cbcNonPaymentConsumersGoodsProvidedForm;
    }

    public void setCbcNonPaymentConsumersGoodsProvidedForm(Long cbcNonPaymentConsumersGoodsProvidedForm) {
        this.cbcNonPaymentConsumersGoodsProvidedForm = cbcNonPaymentConsumersGoodsProvidedForm;
    }

    public List<Long> getCbcNameOfCircumstanceAdditionalCostPrepare() {
        return cbcNameOfCircumstanceAdditionalCostPrepare;
    }

    public void setCbcNameOfCircumstanceAdditionalCostPrepare(List<Long> cbcNameOfCircumstanceAdditionalCostPrepare) {
        this.cbcNameOfCircumstanceAdditionalCostPrepare = cbcNameOfCircumstanceAdditionalCostPrepare;
    }

    public List<Long> getCbcNameOfCircumstanceAdditionalCostBuild() {
        return cbcNameOfCircumstanceAdditionalCostBuild;
    }

    public void setCbcNameOfCircumstanceAdditionalCostBuild(List<Long> cbcNameOfCircumstanceAdditionalCostBuild) {
        this.cbcNameOfCircumstanceAdditionalCostBuild = cbcNameOfCircumstanceAdditionalCostBuild;
    }

    public List<Long> getCbcNameOfCircumstanceAdditionalCostExploitation() {
        return cbcNameOfCircumstanceAdditionalCostExploitation;
    }

    public void setCbcNameOfCircumstanceAdditionalCostExploitation(List<Long> cbcNameOfCircumstanceAdditionalCostExploitation) {
        this.cbcNameOfCircumstanceAdditionalCostExploitation = cbcNameOfCircumstanceAdditionalCostExploitation;
    }

    public String getPpConcludeAgreementLink() {
        return ppConcludeAgreementLink;
    }

    public void setPpConcludeAgreementLink(String ppConcludeAgreementLink) {
        this.ppConcludeAgreementLink = ppConcludeAgreementLink;
    }

    public Boolean getPpImplementProject() {
        return ppImplementProject;
    }

    public void setPpImplementProject(Boolean ppImplementProject) {
        this.ppImplementProject = ppImplementProject;
    }

    public Long getPpResultsOfPlacing() {
        return ppResultsOfPlacing;
    }

    public void setPpResultsOfPlacing(Long ppResultsOfPlacing) {
        this.ppResultsOfPlacing = ppResultsOfPlacing;
    }

    public List<SimpleYearIndicator> getCbcMinimumGuaranteedAmount() {
        return cbcMinimumGuaranteedAmount;
    }

    public void setCbcMinimumGuaranteedAmount(List<SimpleYearIndicator> cbcMinimumGuaranteedAmount) {
        this.cbcMinimumGuaranteedAmount = cbcMinimumGuaranteedAmount;
    }

    public Double getCrInvestmentsTotal() {
        return crInvestmentsTotal;
    }

    public void setCrInvestmentsTotal(Double crInvestmentsTotal) {
        this.crInvestmentsTotal = crInvestmentsTotal;
    }

    public Boolean getCbcMinimumGuaranteedAmountNdsCheck() {
        return cbcMinimumGuaranteedAmountNdsCheck;
    }

    public void setCbcMinimumGuaranteedAmountNdsCheck(Boolean cbcMinimumGuaranteedAmountNdsCheck) {
        this.cbcMinimumGuaranteedAmountNdsCheck = cbcMinimumGuaranteedAmountNdsCheck;
    }

    public Long getCbcMinimumGuaranteedAmountMeasureType() {
        return cbcMinimumGuaranteedAmountMeasureType;
    }

    public void setCbcMinimumGuaranteedAmountMeasureType(Long cbcMinimumGuaranteedAmountMeasureType) {
        this.cbcMinimumGuaranteedAmountMeasureType = cbcMinimumGuaranteedAmountMeasureType;
    }

    public Long getCbcMinimumGuaranteedAmountDateField() {
        return cbcMinimumGuaranteedAmountDateField;
    }

    public void setCbcMinimumGuaranteedAmountDateField(Long cbcMinimumGuaranteedAmountDateField) {
        this.cbcMinimumGuaranteedAmountDateField = cbcMinimumGuaranteedAmountDateField;
    }

    public List<SimpleYearIndicator> getCbcCompensationMinimumGuaranteedAmount() {
        return cbcCompensationMinimumGuaranteedAmount;
    }

    public void setCbcCompensationMinimumGuaranteedAmount(List<SimpleYearIndicator> cbcCompensationMinimumGuaranteedAmount) {
        this.cbcCompensationMinimumGuaranteedAmount = cbcCompensationMinimumGuaranteedAmount;
    }

    public List<SimpleYearIndicator> getCbcCompensationLimitNonPaymentAmount() {
        return cbcCompensationLimitNonPaymentAmount;
    }

    public void setCbcCompensationLimitNonPaymentAmount(List<SimpleYearIndicator> cbcCompensationLimitNonPaymentAmount) {
        this.cbcCompensationLimitNonPaymentAmount = cbcCompensationLimitNonPaymentAmount;
    }

    public List<SimpleYearIndicator> getCbcCompensationArisingProvisionOfBenefitsAmount() {
        return cbcCompensationArisingProvisionOfBenefitsAmount;
    }

    public void setCbcCompensationArisingProvisionOfBenefitsAmount(List<SimpleYearIndicator> cbcCompensationArisingProvisionOfBenefitsAmount) {
        this.cbcCompensationArisingProvisionOfBenefitsAmount = cbcCompensationArisingProvisionOfBenefitsAmount;
    }

    public List<CircumstanceStageIndicator> getCbcLimitCompensationAdditionalCostsAmount() {
        return cbcLimitCompensationAdditionalCostsAmount;
    }

    public void setCbcLimitCompensationAdditionalCostsAmount(List<CircumstanceStageIndicator> cbcLimitCompensationAdditionalCostsAmount) {
        this.cbcLimitCompensationAdditionalCostsAmount = cbcLimitCompensationAdditionalCostsAmount;
    }

    public Boolean getCbcNdsCheck() {
        return cbcNdsCheck;
    }

    public void setCbcNdsCheck(Boolean cbcNdsCheck) {
        this.cbcNdsCheck = cbcNdsCheck;
    }

    public Long getCbcMeasureType() {
        return cbcMeasureType;
    }

    public void setCbcMeasureType(Long cbcMeasureType) {
        this.cbcMeasureType = cbcMeasureType;
    }

    public Long getIoMeasureType() {
        return ioMeasureType;
    }

    public void setIoMeasureType(Long ioMeasureType) {
        this.ioMeasureType = ioMeasureType;
    }

    public Boolean getIoNdsCheck() {
        return ioNdsCheck;
    }

    public void setIoNdsCheck(Boolean ioNdsCheck) {
        this.ioNdsCheck = ioNdsCheck;
    }

    public Long getIoDateField() {
        return ioDateField;
    }

    public void setIoDateField(Long ioDateField) {
        this.ioDateField = ioDateField;
    }

    public Long getCbcDateField() {
        return cbcDateField;
    }

    public void setCbcDateField(Long cbcDateField) {
        this.cbcDateField = cbcDateField;
    }

    public List<CircumstanceStageIndicator> getCbcCircumstancesAdditionalCostsAmount() {
        return cbcCircumstancesAdditionalCostsAmount;
    }

    public void setCbcCircumstancesAdditionalCostsAmount(List<CircumstanceStageIndicator> cbcCircumstancesAdditionalCostsAmount) {
        this.cbcCircumstancesAdditionalCostsAmount = cbcCircumstancesAdditionalCostsAmount;
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

    public Boolean getCbcProjectBudgetObligationMissing() {
        return cbcProjectBudgetObligationMissing;
    }

    public void setCbcProjectBudgetObligationMissing(Boolean cbcProjectBudgetObligationMissing) {
        this.cbcProjectBudgetObligationMissing = cbcProjectBudgetObligationMissing;
    }

    public String getCbcNonPaymentConsumersGoodsProvidedClauseAgreement() {
        return cbcNonPaymentConsumersGoodsProvidedClauseAgreement;
    }

    public void setCbcNonPaymentConsumersGoodsProvidedClauseAgreement(String cbcNonPaymentConsumersGoodsProvidedClauseAgreement) {
        this.cbcNonPaymentConsumersGoodsProvidedClauseAgreement = cbcNonPaymentConsumersGoodsProvidedClauseAgreement;
    }


    public String getCbcArisingProvisionOfBenefitsClauseAgreement() {
        return cbcArisingProvisionOfBenefitsClauseAgreement;
    }

    public void setCbcArisingProvisionOfBenefitsClauseAgreement(String cbcArisingProvisionOfBenefitsClauseAgreement) {
        this.cbcArisingProvisionOfBenefitsClauseAgreement = cbcArisingProvisionOfBenefitsClauseAgreement;
    }

    public String getCbcDueToOnsetOfCertainCircumstancesClauseAgreement() {
        return cbcDueToOnsetOfCertainCircumstancesClauseAgreement;
    }

    public void setCbcDueToOnsetOfCertainCircumstancesClauseAgreement(String cbcDueToOnsetOfCertainCircumstancesClauseAgreement) {
        this.cbcDueToOnsetOfCertainCircumstancesClauseAgreement = cbcDueToOnsetOfCertainCircumstancesClauseAgreement;
    }

    public String getCmContacts() {
        return cmContacts;
    }

    public void setCmContacts(String cmContacts) {
        this.cmContacts = cmContacts;
    }

    public Double getAdsWorkPlacesCount() {
        return adsWorkPlacesCount;
    }

    public void setAdsWorkPlacesCount(Double adsWorkPlacesCount) {
        this.adsWorkPlacesCount = adsWorkPlacesCount;
    }

}

