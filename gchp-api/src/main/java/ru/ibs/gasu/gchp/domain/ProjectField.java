package ru.ibs.gasu.gchp.domain;

import static ru.ibs.gasu.gchp.domain.ProjectField.SectionName.*;
import static ru.ibs.gasu.gchp.domain.ProjectField.SubSectionName.*;

public enum ProjectField {
    GI_NAME("Наименование проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REALIZATION_FORM("Форма реализации проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_INITIATION_METHOD("Способ инициации проекта/ определения поставщика", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REALIZATION_LEVEL("Уровень реализации проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_RF_PART_OF_AGREEMENT("РФ является стороной соглашения", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_REGION_PART_OF_AGREEMENT("Регион является стороной соглашения", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_MUNICIPALITY_PART_OF_AGREEMENT("МО является стороной соглашения", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REGION("Субъект РФ", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_MUNICIPALITY("Муниципальное образование", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_PUBLIC_PARTNER("Публичный партнер/Концедент/Заказчик/Арендодатель", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_INN("ИНН заказчика", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IMPLEMENTER("Частный партнер/ Концессионер/ исполнитель/ поставщик-инвестор/ совместное предприятие/ арендатор", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_FOREIGN_INVESTOR("Иностранный инвестор", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_MCP_SUBJECT("Субъект МСП", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_SPECIAL_PROJECT_COMPANY("Совместное юридическое лицо является специальной проектной компанией", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_HAS_INVESTMENT_PROPERTY("Совместное юридическое лицо владеет недвижимым и " +
            "(или) движимым имуществом, в отношении которого предполагается осуществление инвестиционных мероприятий", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_PUBLIC_SHARE_PERCENTAGE("Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), " +
            "реализующего проект, до реализации проекта, %", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IS_RF_HAS_SHARE("Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REALIZATION_SPHERE("Сфера реализации проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REALIZATION_SECTOR("Отрасль реализации проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_OBJECT_TYPE("Вид объекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_OBJECT_LOCATION("Место нахождения объекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_AGREEMENT_SUBJECT("Предмет соглашения/ контракта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_REALIZATION_STATUS("Стадия реализации проекта", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_COMPLETED_TEMPLATE_TEXT_FV_ID("Сведения по условным и безусловным обязательствам", GENERAL_INFO, WITHOUT_SUBSECTION), //file

    OD_OBJECT_NAME("Наименование объекта / товара", DESCRIPTION, COMMON_INFO),

    OD_OBJECT_DESCRIPTION("Краткая характеристика объекта / товара", DESCRIPTION, COMMON_INFO),

    OD_IS_PROPERTY_STAYS_WITH_PRIVATE_SIDE("Создаваемое концессионером/ частной стороной имущество, " +
            "не входящее в Объект соглашения, остается у концессионера/ частной стороны", DESCRIPTION, COMMON_INFO),

    OD_IS_NEW_PROPERTY_BE_GIVEN_TO_PRIVATE_SIDE("В рамках концессионного соглашения Концессионеру/ частной " +
            "стороне передается иное имущество", DESCRIPTION, COMMON_INFO),

    OD_IS_OBJECT_IMPROVEMENTS_GIVE_AWAY("Осуществляемые арендатором отделимые улучшения объекта по истечении " +
            "срока действия договора передаются арендодателю", DESCRIPTION, COMMON_INFO),

    OD_RENT_OBJECT("Объектом договора аренды является", DESCRIPTION, COMMON_INFO),

    OD_RENT_PASSPORT_FILE_VERSION_ID("Энергетический паспорт объекта/результаты энергообследования " +
            "(Приложение из контракта с перечнем товаров)", DESCRIPTION, COMMON_INFO),

    OD_TECH_ECONOMICS_INDICATORS("Технико-экономические показатели", DESCRIPTION, TECH_ECONOMICS_INDICATORS),

    OD_ENERGY_EFFICIENCY_PLANS("План энергоэффективных мероприятий", DESCRIPTION, ENERGY_EFFICIENCY_PLANS),

    CR_AGREEMENT_START_DATE("Дата заключения соглашения", CREATION, REQUISITES_DOCUMENT),

    CR_AGREEMENT_END_DATE("Дата окончания действия соглашения", CREATION, REQUISITES_DOCUMENT),

    CR_AGREEMENT_VALIDITY("Срок действия соглашения, лет", CREATION, REQUISITES_DOCUMENT),

    CR_AGREEMENT_TEXT_FV_ID("Основание (текст соглашения)", CREATION, REQUISITES_DOCUMENT), //file

    CR_CONCESSIONAIRE("Концессионер (частный партнер)", CREATION, REQUISITES_DOCUMENT),

    CR_CONCESSIONAIRE_INN("ИНН Концессионера (частного партнера)", CREATION, REQUISITES_DOCUMENT),

    CR_FINANCIAL_CLOSING_PROVIDED("Финансовое закрытие предусмотрено", CREATION, FINANCIAL_CLOSING),

    CR_FINANCIAL_CLOSING_DATE("Дата финансового закрытия", CREATION, FINANCIAL_CLOSING),

    CR_FINANCIAL_CLOSING_VALUE("Объем финансового закрытия", CREATION, FINANCIAL_CLOSING),

    CR_FINANCIAL_CLOSING_ACT_FV_ID("Акт финансового закрытия", CREATION, FINANCIAL_CLOSING), //file

    CR_FINANCIAL_CLOSING_IS_MUTUAL_AGREEMENT("Наличие соглашения между концедентом (публичным партнером) " +
            "концессионером (частным партнером) и финансирующей организацией (прямое соглашение)", CREATION, FINANCIAL_CLOSING),

    CR_FIRST_OBJECT_CREATION_PLAN_DATE("Дата создания первого объекта соглашения - план", CREATION, FINANCIAL_CLOSING),

    CR_FIRST_OBJECT_CREATION_FACT_DATE("Дата создания первого объекта соглашения - факт", CREATION, FINANCIAL_CLOSING),

    CR_FIRST_OBJECT_COMPLETE_ACT_FV_ID("Создание первого объекта соглашения: Основание (пункт соглашения, " +
            "акт выполненных работ)", CREATION, FINANCIAL_CLOSING), //file

    CR_LAST_OBJECT_CREATION_PLAN_DATE("Дата создания последнего объекта соглашения - план", CREATION, FINANCIAL_CLOSING),

    CR_LAST_OBJECT_CREATION_FACT_DATE("Дата создания последнего объекта соглашения - факт", CREATION, FINANCIAL_CLOSING),

    CR_LAST_OBJECT_COMPLETE_ACT_FV_ID("Создание последнего объекта соглашения: Основание (пункт соглашения, " +
            "акт выполненных работ)", CREATION, FINANCIAL_CLOSING), //file

    CR_INVESTMENT_CREATION_AMOUNT("Объем инвестиций на стадии создания", CREATION, INVESTMENT_CREATION_AMOUNT), //table

    CR_INVESTMENT_VOLUME_STAG_OF_CREATION_ACT_FV_ID("Обосновывающие документы", CREATION, INVESTMENT_CREATION_AMOUNT), //file

    CR_IS_OBJECT_TRANSFER_PROVIDED("Концессионным соглашением предусмотрена передача концессионеру существующего на момент " +
            "заключения соглашения объекта соглашения", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_OBJECT_RIGHTS_PLAN_DATE("Дата возникновения права владения и пользования объектом у частной стороны - " +
            "планируемая", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_OBJECT_RIGHTS_FACT_DATE("Дата возникновения права владения и пользования объектом у частной стороны - " +
            "фактическая", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_ACT_FV_ID("Возникновение права владения и пользования объектом у частной стороны: Основание (пункт соглашения, " +
            "акт приема-передачи)", CREATION, INVESTMENT_CREATION_AMOUNT), //file

    CR_OBJECT_VALUE("Балансовая стоимость передаваемого объекта соглашения", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_IS_RENEWABLE_BANK_GUARANTEE("Возобновляемая банковская гарантия", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_IS_GUARANTEE_VARIES_BY_YEAR("Размер банковской гарантии изменяется по годам", CREATION, INVESTMENT_CREATION_AMOUNT),

    CR_BANK_GUARANTEE_BY_YEARS("Размер банковской гарантии по годам",CREATION, INVESTMENT_CREATION_AMOUNT),//table

    CR_REFERENCE_FV_ID("Балансовая стоимость передаваемого объекта соглашения: Основание " +
            "(ссылка на текст соглашения)", CREATION, INVESTMENT_CREATION_AMOUNT), //file

    CR_CALC_INVEST_COSTS_ACT_FV_ID("Расчет планового размера инвестиционных расходов концедента (публичного партнера):", CREATION, INVESTMENT_CREATION_AMOUNT), //file;

    CR_LAND_PROVIDED("Соглашением предусмотрено предоставление земельного участка", CREATION, LAND_LAW),

    CR_LAND_IS_CONCESSIONAIRE_OWNER("Земельный участок находится в собственности Концедента (Публичного партнера)", CREATION, LAND_LAW),

    CR_LAND_ACT_START_PLAN_DATE("Договор о предоставлении замельного участка: Плановая дата заключения", CREATION, LAND_LAW),

    CR_LAND_ACT_START_FACT_DATE("Договор о предоставлении замельного участка: Фактическая дата заключения", CREATION, LAND_LAW),

    CR_LAND_ACT_END_PLAN_DATE("Договор о предоставлении замельного участка: Плановый срок действия", CREATION, LAND_LAW),

    CR_LAND_ACT_END_FACT_DATE("Договор о предоставлении замельного участка: Фактический срок действия", CREATION, LAND_LAW),

    CR_LAND_ACT_FV_ID("Договор о предоставлении замельного участка: Основание (пункт соглашения, текст договора)", CREATION, LAND_LAW), //file

    CR_IS_OBLIGATION_EXECUTE_ON_CREATION_STAGE("Обеспечение исполнения обязательств на этапе создания", CREATION, LAND_LAW),

    CR_GOV_SUPPORT("Государственная поддержка, оказываемая проекту", CREATION, LAND_LAW), //combo

    CR_CONFIRMATION_DOC_FV_ID("Тексты подтверждающих документов", CREATION, LAND_LAW), //file;

    PP_IS_OBJ_IN_LIST_WITH_CONCESSIONAL_AGREEMENTS("Объект соглашения  включен в перечень объектов, в отношении которых " +
            "планируется заключение концессионных соглашений", PREPARATION, COMMON_INFO),

    PP_OBJECTS_LIST_URL("Ссылка на перечень объектов", PREPARATION, COMMON_INFO),

    PP_SUPPOSED_PRIVATE_PARTNER_NAME("Предполагаемая частная сторона", PREPARATION, COMMON_INFO),

    PP_SUPPOSED_PRIVATE_PARTNER_INN("ИНН", PREPARATION, COMMON_INFO),

    PP_IS_FOREIGN_INVESTOR("Иностранный инвестор", PREPARATION, COMMON_INFO),

    PP_IS_MSP_SUBJECT("Субъект МСП", PREPARATION, COMMON_INFO),

    PP_SUPPOSED_AGREEMENT_START_DATE("Предполагаемая дата заключения", PREPARATION, COMMON_INFO),

    PP_SUPPOSED_AGREEMENT_END_DATE("Предполагаемая дата окончания", PREPARATION, COMMON_INFO),

    PP_SUPPOSED_VALIDITY_YEARS("Предполагаемый срок действия соглашения, лет", PREPARATION, COMMON_INFO),

    PP_PROJECT_AGREEMENT_FILE_VERSION_ID("Основание (проект соглашения)", PREPARATION, COMMON_INFO), //file

    PP_GROUNDS_OF_AGREEMENT_CONCLUSION("Основание заключения соглашения", PREPARATION, COMMON_INFO), //combo

    PP_ACT_NUMBER("Номер решения (распоряжения)", PREPARATION, COMMON_INFO),

    PP_ACT_DATE("Дата решения (распоряжения)", PREPARATION, COMMON_INFO),

    PP_ACT_TEXT_FILE_VERSION_ID("Текст решения (распоряжения)", PREPARATION, COMMON_INFO), //file

    PP_LEASE_AGREEMENT_TEXT_FILE_VERSION_ID("Текст договора аренды/ постановления о присвоении ЕТО", PREPARATION, COMMON_INFO), //file

    PP_PROPOSAL_PUBLISH_DATE("Дата размещения предложения", PREPARATION, COMMON_INFO),

    PP_PROPOSAL_TEXT_FILE_VERSION_ID("Текст предложения", PREPARATION, COMMON_INFO), //file

    PP_TORGI_GOV_RU_URL("Ссылка на torgi.gov.ru", PREPARATION, COMMON_INFO),

    PP_PROTOCOL_FILE_VERSION_ID("Протокол", PREPARATION, COMMON_INFO), //file

    PP_IS_READINESS_REQUEST_RECEIVED("Поступили заявки о готовности участия в конкурсе", PREPARATION, COMMON_INFO),

    PP_IS_DECISION_MADE_TO_CONCLUDE_AN_AGREEMENT("Принято решение о заключении соглашения", PREPARATION, COMMON_INFO),

    PP_CONCLUDE_AGREEMENT_ACT_NUM("Номер решения", PREPARATION, REQUISITES_TENDER),

    PP_CONCLUDE_AGREEMENT_ACT_DATE("Дата решения", PREPARATION, REQUISITES_TENDER),

    PP_CONCLUDE_AGREEMENT_FV_ID("Текст решения", PREPARATION, REQUISITES_TENDER),  //file

    PP_CONCLUDE_AGREEMENT_IS_SIGNED("Соглашение подписано", PREPARATION, REQUISITES_TENDER),

    PP_CONCLUDE_AGREEMENT_IS_JOINT("Совместный конкурс", PREPARATION, REQUISITES_TENDER),

    PP_CONCLUDE_AGREEMENT_OTHER_PJ_INFO("Информация об иных проектах в совместном конкурсе", PREPARATION, REQUISITES_TENDER),

    PP_CONCLUDE_AGREEMENT_OTHER_PJ_IDENT("Идентификаторы иных проектов в совместном конкурсе", PREPARATION, REQUISITES_TENDER),

    PP_COMPETITION_BID_COLL_END_PLAN_DATE("Дата окончания сбора заявок на участие в конкурсе план", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_BID_COLL_END_FACT_DATE("Дата окончания сбора заявок на участие в конкурсе факт", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_TENDER_OFFER_END_PLAN_DATE("Дата окончания представления конкурсных предложений план", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_TENDER_OFFER_END_FACT_DATE("Дата окончания представления конкурсных предложений факт", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_RESULTS_PLAN_DATE("Дата  подведения итогов конкурса план", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_RESULTS_FACT_DATE("Дата  подведения итогов конкурса факт", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_TEXT_FV_ID("Текст сообщения о проведении конкурса", PREPARATION, COMPETITION_TENDER),  //file

    PP_COMPETITION_IS_EL_AUCTION("Конкурс является аукционом в электронной форме (только по ценовому критерию)", PREPARATION, COMPETITION_TENDER),

    PP_COMPETITION_HAS_RESULTS("Итоги конкурса подведены", PREPARATION, RESULT_COMPETITION_TENDER),

    PP_COMPETITION_RESULTS("Результаты проведения конкурса", PREPARATION, RESULT_COMPETITION_TENDER),  //combo

    PP_COMPETITION_RESULTS_PROTOCOL_NUM("Номер протокола", PREPARATION, RESULT_COMPETITION_TENDER),

    PP_COMPETITION_RESULTS_PROTOCOL_DATE("Дата протокола", PREPARATION, RESULT_COMPETITION_TENDER),

    PP_COMPETITION_RESULTS_PARTICIPANTS_NUM("Количество участников", PREPARATION, RESULT_COMPETITION_TENDER),

    PP_COMPETITION_RESULTS_PROTOCOL_TEXT_FV_ID("Текст протокола/ решения о признании конкурса несостоявшимся", PREPARATION, RESULT_COMPETITION_TENDER),  //file

    PP_COMPETITION_RESULTS_DOC_FV_ID("Текст конкурсной документации", PREPARATION, RESULT_COMPETITION_TENDER), //file

    PP_COMPETITION_RESULTS_SIGN_STATUS("Статус подписания соглашения", PREPARATION, RESULT_COMPETITION_TENDER),  //combo

    PP_CONTRACT_PRICE_ORDER("Порядок расчета цены", PREPARATION, CONTRACT_PRICE),  //combo

    PP_CONTRACT_PRICE_FORMULA("Описание формулы", PREPARATION, CONTRACT_PRICE),

    PP_CONTRACT_PRICE_PRICE("Цена контракта", PREPARATION, CONTRACT_PRICE),

    PP_CONTRACT_PRICE_OFFER("Предложение заказчика", PREPARATION, CONTRACT_PRICE), //combo

    PP_CONTRACT_PRICE_OFFER_VALUE("Размер предложения", PREPARATION, CONTRACT_PRICE),

    PP_CONTRACT_PRICE_SAVING_START_DATE("Начальный срок достижения экономии", PREPARATION, CONTRACT_PRICE),

    PP_CONTRACT_PRICE_SAVING_END_DATE("Конечный срок достижения экономии", PREPARATION, CONTRACT_PRICE),

    PP_INVESTMENT_PLANNING_AMOUNT("Планируемый объем инвестиций на стадии создания и эксплуатации", PREPARATION, INVESTMENT_CREATION_AMOUNT), //table

    CBC_INVESTMENTS1("Предельная сумма компенсации концессионеру/частному партнеру по обеспечению его минимального гарантированного дохода", CBC, CBC_INVEST1), //table

    CBC_INVESTMENTS2("Размер фактически выплаченной компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода", CBC, CBC_INVEST2), //table

    CBC_INVESTMENTS3("Размер фактически выплаченной концессионеру/частному партнеру компенсации недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг", CBC, CBC_INVEST3), //table

    CBC_INVESTMENTS4("Размер фактически выплаченной компенсации концессионеру/частному партнеру льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента", CBC, CBC_INVEST4), //table

    REMAINING_DEBT("Остаток задолженности", CREATION, REMAIN_DEBT), //table

    PP_FINANCIAL_MODEL_FV_ID("Основание (финансовая модель)", PREPARATION, CONTRACT_PRICE), //file

    PP_PRIVATE_PARTNER_COST_RECOVERY_METHOD("Способ возмещения расходов", PREPARATION, CONTRACT_PRICE), //combo

    PP_FIRST_OBJECT_OPERATION_DATE("Дата начала создания / реконструкции первого объекта соглашения", PREPARATION, CONTRACT_PRICE),

    PP_LAST_OBJECT_COMMISSIONING_DATE("Дата ввода последнего объекта в эксплуатацию", PREPARATION, CONTRACT_PRICE),

    PP_SUPPORTING_DOCUMENTS_FILE_VERSION_IDS("Основание (обосновывающие документы)", PREPARATION, CONTRACT_PRICE), //file

    EX_LAST_OBJECT_PLAN_DATE("Дата ввода последнего объекта в эксплуатацию - план", EXPLOITATION, COMMON_INFO),

    EX_LAST_OBJECT_FACT_DATE("Дата ввода последнего объекта в эксплуатацию - факт", EXPLOITATION, COMMON_INFO),

    EX_LAST_OBJECT_ACT_FV_ID("Основание (пункт соглашения, акт ввода в эксплуатацию)", EXPLOITATION, COMMON_INFO), //file

    EX_INVESTMENT_EXPLOITATION_AMOUNT("Объем инвестиций на этапе эксплуатации", EXPLOITATION, COMMON_INFO), //table

    EX_INVESTMENT_VOLUME_STAG_OF_EXPLOITATION_ACT_FV_ID("Обосновывающие документы", EXPLOITATION, COMMON_INFO), //file

    EX_PUBLIC_SHARE_EXPL("Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), " +
            "реализующего проект, после ввода объекта в эксплуатацию", EXPLOITATION, COMMON_INFO),

    EX_HAS_PUBLIC_SHARE_EXPL("Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация", EXPLOITATION, COMMON_INFO),

    EX_IS_INVESTMENTS_RECOVERY_PROVIDED("Cоглашением предусмотрено возмещение частных " +
            "инвестиций на этапе эксплуатации", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT("Возмещение частных инвестиций на этапе эксплуатации", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //table

    EX_FIN_MODEL_FV_IDS("Финансовая модель", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    EX_COST_RECOVERY_METHOD("Способ возмещения расходов арендатора", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //combo

    EX_COST_RECOVERY_MECHANISM("Описание механизма возмещения расходов", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_SUPPORT_DOC_FV_IDS("Обосновывающие документы", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    EX_COMPENSATION("Возмещение расходов арендатора за счет передачи объекта договора в субаренду", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //table

    EX_SUPPORT_COMPENS_DOC_FV_IDS("Обосновывающие документы (возмещение расходов)", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    EX_IR_SOURCE("Источник возмещения", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //combo

    PP_RESULTS_OF_PLACING("Результаты размещения предложения на torgi.gov.ru", PREPARATION, COMMON_INFO), //combo

    EX_IR_LEVEL("Уровень бюджета для возмещения", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),//combo

    EX_IS_OBLIGATION_EXECUTING_ON_OPERATION_PHASE("Обеспечение исполнения обязательств на этапе эксплуатации", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    ////EX_METHOD_OF_EXEC_OF_PUBLIC_PARTNER_OBLIGATION("Способы обеспечения обязательств частного партнера", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),//combo

    ////EX_INVESTMENT_RECOVERY_TERM("Срок обеспечения", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    ////EX_INVESTMENT_RECOVERY_VALUE("Возмещение частных инвестиций на этапе эксплуатации - Размер обеспечения", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_ENSURE_METHODS("Способы обеспечения обязательств частного партнера", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //table

    EX_IS_RENEWABLE_BANK_GUARANTEE("Возобновляемая банковская гарантия", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_IS_GUARANTEE_VARIES_BY_YEAR("Размер банковской гарантии изменяется по годам", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_BANK_GUARANTEE_BY_YEARS("Размер банковской гарантии по годам",EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),//table

    EX_IS_CONCESSION_PAY_PROVIDEDED("Соглашением предусмотрена концессионная (арендная) плата", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_PAYMENT_FORM("Форма взимания платы", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //combo

    EX_PAYMENT("Плата по годам", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //table

    EX_OWN_PRIVATE_PLAN_DATE("Дата возникновения права собственности у частной стороны, план", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_OWN_PRIVATE_FACT_DATE("Дата возникновения права собственности у частной стороны, факт", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_OWN_PUBLIC_PLAN_DATE("Дата возникновения права собственности у публичной стороны, план", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_OWN_PUBLIC_FACT_DATE("Дата возникновения права собственности у публичной стороны, факт", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_AGREEMENT_FV_IDS("Пункт соглашения", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file - Пункт соглашения + ссылка на текст соглашения или доп соглашение

    EX_ACCEPT_ACT_FV_IDS("Акт приема-передачи", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    EX_START_ACH_ECON_PLAN_DATE("Начальный срок достижения экономии, план", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_END_ACH_ECON_PLAN_DATE("Конечный срок достижения экономии, план", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_START_ACH_ECON_FACT_DATE("Начальный срок достижения экономии, факт", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_END_ACH_ECON_FACT_DATE("Конечный срок достижения экономии, факт", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_ACCEPT_ACT_AA_MF_VIDS("Акт о приемке ЭЭМ", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    EX_INVEST_STAGE_PLAN_DATE("Срок инвестиционной стадии, план", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    EX_INVEST_STAGE_FACT_DATE("Срок инвестиционной стадии, факт", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT),

    TM_CAUSE("Причина прекращения", TERMINATE, COMMON_INFO), //combo

    TM_ACT_PLAN_DATE("Дата прекращения соглашения - пла", TERMINATE, COMMON_INFO),

    TM_ACT_FACT_DATE("Дата прекращения соглашения - факт", TERMINATE, COMMON_INFO),

    TM_COMPOSITION_OF_COMPENSATION("Состав компенсации при прекращении соглашения", TERMINATE, COMMON_INFO), //table

    TM_ACT_NUMBER("Номер соглашения", TERMINATE, AGREEMENT_PARTIES),

    TM_ACT_DATE("Дата соглашения", TERMINATE, AGREEMENT_PARTIES),

    TM_TEXT_FILE_VERSION_ID("Текст соглашения", TERMINATE, AGREEMENT_PARTIES), //file

    TM_CAUSE_DESCRIPTION("Описание причины расторжения", TERMINATE, AGREEMENT_PARTIES),

    TM_PLAN_DATE("Дата прекращения права владения и пользования у частной стороны - план", TERMINATE, AGREEMENT_PARTIES),

    TM_FACT_DATE("Дата прекращения права владения и пользования у частной стороны - факт", TERMINATE, AGREEMENT_PARTIES),

    TM_TA_ACT_TEXT_FILE_VERSION_ID("Основание (пункт соглашения, акт приема-передачи)", TERMINATE, AGREEMENT_PARTIES), //file

    TM_PROPERTY_JOINT_PROVIDED("Предусмотрено разделение права собственности на объект соглашения", TERMINATE, AGREEMENT_PARTIES),

    TM_PROPERTY_JOINT_PRIVATE_PERCENT("Разделение права собственности % частной стороне", TERMINATE, AGREEMENT_PARTIES),

    TM_PROPERTY_JOINT_PUBLIC_PERCENT("Разделение права собственности % публичной стороне", TERMINATE, AGREEMENT_PARTIES),

    TM_IS_COMPENSATION_PAYED("Концедентом (публичным партнером) выплачена компенсация концессионеру при расторжении соглашения", TERMINATE, AGREEMENT_PARTIES),

    TM_COMPENSATION_VALUE("Сумма компенсации", TERMINATE, AGREEMENT_PARTIES),

    TM_COMPENSATION_FV_IDS("Расчет суммы компенсации", TERMINATE, AGREEMENT_PARTIES), //file

    TM_AFTERMATH("Последствия прекращения договора", TERMINATE, AGREEMENT_PARTIES), //combo

    TM_CONTRACT_NUMBER("Номер договора", TERMINATE, AGREEMENT_PARTIES),

    TM_CONTRACT_DATE("Дата договора", TERMINATE, AGREEMENT_PARTIES),

    TM_PROJECT_ID("ID проекта", TERMINATE, AGREEMENT_PARTIES),

    TM_PUBLIC_SHARE("Доля публичной стороны в капитале совместного юридического лица  " +
            "(совместного предприятия), реализующего проект, после реализации проекта, %", TERMINATE, AGREEMENT_PARTIES),

    TM_IS_RF_HAS_SHARE("Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация", TERMINATE, AGREEMENT_PARTIES),

    CC_IS_CHANGES_MADE("В соглашение (контракт) внесены изменения", CHANGE_CONDITIONS, COMMON_INFO),

    CC_REASON("Изменение условий соглашения (контракта)", CHANGE_CONDITIONS, COMMON_INFO),

    CC_ACT_NUMBER("Номер решения (соглашения)", CHANGE_CONDITIONS, REQUISITES_DOCUMENT),

    CC_ACT_DATE("Дата решения (соглашения)", CHANGE_CONDITIONS, REQUISITES_DOCUMENT),

    CC_TEXT_FILE_VERSION_ID("Текст решения (соглашения)", CHANGE_CONDITIONS, REQUISITES_DOCUMENT),

    ADS_EVENTS("События проекта", ADDITIONALLY, EVENTS), //table

    ADS_IS_THIRD_PARTY_ORGS_PROVIDED("Привлечение организаций к реализации проекта", ADDITIONALLY, PRIVATE_PARTNER_THIRD_PARTY_ORGS),

    ADS_IS_REG_INVESTMENT_PROJECT("Региональный инвестиционный проект", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_HAS_INCOME_TAX("Налог на прибыль", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_INCOME_TAX_RATE("Налог на прибыль - ставка", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_HAS_LAND_TAX("Земальный налог", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_LAND_TAX_RATE("Земальный налог - ставка", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_HAS_PROPERTY_TAX("Налог на имущество", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_PROPERTY_TAX_RATE("Налог на имущество - ставка", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_HAS_BENEFIT_CLARIFICATION_TAX("Уточнение льготы", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_BENEFIT_CLARIFICATION_RATE("Уточнение льготы - ставка", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_BENEFIT_DESCRIPTION("Уточнение льготы - описание", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_IS_TREASURY_SUPPORT("Осуществление казначейского сопровождения", ADDITIONALLY, FINANCIAL_STRUCTURE),

    ADS_DESCISION_TEXT_FILE_ID("Решение", ADDITIONALLY, COMPETITION_CRITERIA),  //file

    ADS_PRIVATE_PARTNER_THIRD_PARTY_ORGS("Привлеченные организации к реализации проекта со стороны " +
            "частного партнера", ADDITIONALLY, PRIVATE_PARTNER_THIRD_PARTY_ORGS), //table

    ADS_PUBLIC_PARTNER_THIRD_PARTY_ORGS("Привлеченные организации к реализации проекта со стороны " +
            "публичного партнера", ADDITIONALLY, PUBLIC_PARTNER_THIRD_PARTY_ORGS), //table

    ADS_NPV("Плановое значение чистой приведенной стоимости проекта (NPV)", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_IRR("Плановое значение внутренней нормы доходности проекта (IRR)", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_PB("Период окупаемости проекта (простой), лет", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_PB_DISCOUNTED("Период окупаемости проекта (дисконтированный), лет", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_EBIDTA("Прибыль до вычета процентов, налогов и амортизации (EBITDA)", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_WACC("Средневзвешенная стоимость капитала (WACC)", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_TAX_RELIEF("Налоговые льготы", ADDITIONALLY, FIN_ECONOMICS_INDICATORS),

    ADS_SANCTIONS("Санкции в отношении частного партнера", ADDITIONALLY, SANCTIONS), //table

    ADS_JUDICIAL_ACTIVITIES("Судебная активность по проекту", ADDITIONALLY, JUDICIAL_ACTIVITIES), //table

    ADS_CONCESSIONAIRE_OPF("ОПФ", ADDITIONALLY, CONCESSIONAIRE), //combo

    ADS_CONCESSIONAIRE_NAME("Наименование", ADDITIONALLY, CONCESSIONAIRE),

    ADS_CONCESSIONAIRE_INN("ИНН", ADDITIONALLY, CONCESSIONAIRE),

    ADS_CONCESSIONAIRE_REGIME("Режим", ADDITIONALLY, CONCESSIONAIRE), //combo

    ADS_CONCESSIONAIRE_CREDIT_RATING("Кредитный рейтинг", ADDITIONALLY, CONCESSIONAIRE),

    ADS_CONCESSIONAIRE_CREDIT_RATING_START_DATE("Кредитный рейтинг дата присвоения", ADDITIONALLY, CONCESSIONAIRE),

    ADS_CONCESSIONAIRE_CREDIT_RATING_END_DATE("Кредитный рейтинг дата отзыва", ADDITIONALLY, CONCESSIONAIRE),

    ADS_CONCESSIONAIRE_CREDIT_RATING_AGENCY("Кредитный рейтинг агенство", ADDITIONALLY, CONCESSIONAIRE),

    ADS_OWNERSHIP_STRUCTURES("Структура владения частным партнером", ADDITIONALLY, OWNERSHIP_STRUCTURES), //table

    ADS_FINANCIAL_STRUCTURE("Структура финансирования проекта", ADDITIONALLY, FINANCIAL_STRUCTURE), //table

    ADS_INVESTMENT_BOOL_CRITERIAS("Критерии качественных инфраструктурных инвестиций", ADDITIONALLY, INVESTMENT_BOOL_CRITERIAS), //table

    ADS_UNFORESEEN_EXPENCES_SHARE("Доля непредвиденных расходов от всех расходов на создание", ADDITIONALLY, INVESTMENT_BOOL_CRITERIAS),

    ADS_UNFORESEEN_EXPENCES_SHARE_COMMENT("Доля непредвиденных расходов от всех расходов на " +
            "создание комментарий", ADDITIONALLY, INVESTMENT_BOOL_CRITERIAS),

    ADS_COMPETITION_CRITERIA("Критерии конкурса", ADDITIONALLY, COMPETITION_CRITERIA), //combo

    ADS_FINANCIAL_REQUIREMENT("Финансовые требования к участникам конкурса", ADDITIONALLY, COMPETITION_CRITERIA), //combo

    ADS_NON_FINANCIAL_REQUIREMENTS("Нефинансовые требования к участникам конкурса", ADDITIONALLY, COMPETITION_CRITERIA), //combo

    CM_COMMENT("Комментарий", COMMENT, WITHOUT_SUBSECTION),

    GI_PROJECT_STATUS("Статус", COMMENT, WITHOUT_SUBSECTION),

    CR_AGREEMENT_COMPLEX("Комплекс соглашений, заключенных в рамках проекта", CREATION, REQUISITES_DOCUMENT), //combo

    CR_AGREEMENT_END_DATE_AFTER_PROLONGATION("Дата окончания действия договора после пролонгации ", CREATION, REQUISITES_DOCUMENT),

    CR_AGREEMENT_TEXT_FILES("Текст договора", CREATION, REQUISITES_DOCUMENT), //file

    CR_AGREEMENT_VALIDITY_AFTER_PROLONGATION("Срок действия договора после пролонгации, лет", CREATION, REQUISITES_DOCUMENT),

    CR_ENSURE_METHODS("Способы обеспечения обязательств частного партнера", CREATION, LAND_LAW), //table

    CR_INVESTMENT_STAGE_TERM("Срок инвестиционной стадии", CREATION, REQUISITES_DOCUMENT),

    CR_IS_FOREIGN_INVESTOR("Иностранный инвестор", CREATION, REQUISITES_DOCUMENT),

    CR_IS_MCP_SUBJECT("Субъект МСП", CREATION, REQUISITES_DOCUMENT),

    CR_JOB_DONE_TERM("Срок поставки товара/выполнения работ", CREATION, REQUISITES_DOCUMENT),

    CR_OPF("ОПФ", CREATION, REQUISITES_DOCUMENT), //combo

    CR_SAVING_END_DATE("Конечный срок достижения экономии", CREATION, REQUISITES_DOCUMENT),

    CR_SAVING_START_DATE("Начальный срок достижения экономии", CREATION, REQUISITES_DOCUMENT),

    EX_INVESTMENT_RECOVERY_FINANCIAL_MODEL_FILE_VERSION_ID("Основание (финансовая модель)", EXPLOITATION, INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT), //file

    GI_ALWAYS_DRAFT_STATUS("Перевести принудительно в черновик", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_BALANCE_HOLDER("Балансодержатель", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_IMPLEMENTER_INN("ИНН Исполнителя/Поставщика-инвестора/Совместного предприятия", GENERAL_INFO, WITHOUT_SUBSECTION),

    GI_OP_F("ОПФ", GENERAL_INFO, WITHOUT_SUBSECTION),

    PP_ADVANCE_PAYMENT_AMOUNT("Размер авансового платежа", PREPARATION, CONTRACT_PRICE),

    PP_AGREEMENTS_SET("Комплекс соглашений, заключаемых в рамках проекта", PREPARATION, COMMON_INFO), //combo

    PP_BUDGET_EXPENDITURES_AGREEMENT_ON_GCHP_MCHP("Чистый дисконтированный расход бюджетных средств: Соглашение о ГЧП/МЧП", PREPARATION, WITHOUT_SUBSECTION),

    PP_BUDGET_EXPENDITURES_GOV_CONTRACT("Чистый дисконтированный расход бюджетных средств: Государственный контракт", PREPARATION, WITHOUT_SUBSECTION),

    PP_CONCLUSION_UO_TEXT_FILE_VERSION_ID("Заключение УО", PREPARATION, CONTRACT_PRICE), //file

    PP_CREATION_INVESTMENT_PLANNING_AMOUNT("Планируемый объем инвестиций на стадии создания/реконструкции", PREPARATION, INVESTMENT_CREATION_AMOUNT), //table

    PP_DATE_FIELD("На дату", PREPARATION, CONTRACT_PRICE),

    PP_DECISION_DATE("Дата решения", PREPARATION, INVESTMENT_CREATION_AMOUNT),

    PP_DECISION_NUMBER("Номер решения (распоряжения)", PREPARATION, COMMON_INFO),

    PP_DECISION_TEXT_FILE_VERSION_ID("Текст решения", PREPARATION, COMMON_INFO), //file

    PP_DELIVERY_TIME_OF_GOODS_WORK_DATE("Срок поставки товара/выполнения работ", PREPARATION, COMMON_INFO),

    PP_FINANCIAL_MODEL_TEXT_FILE_VERSION_ID("Основание (финансовая модель)", PREPARATION, WITHOUT_SUBSECTION), //file

    PP_INDICATOR_ASSESSMENT_COMPARATIVE_ADVANTAGE("Значение показателя оценки сравнительного преимущества", PREPARATION, CONTRACT_PRICE),

    PP_INVESTMENT_STAGE_DURATION_DATE("Срок инвестиционной стадии", PREPARATION, REQUISITES_DOCUMENT),

    PP_IS_PRIVATE_LIABILITY_PROVIDED("Договором предусмотрена ответственность частной стороны за несоблюдение " +
            "вышеуказанных обязательств", PREPARATION, CONTRACT_PRICE),

    PP_LINK_TO_CLAUSE_AGREEMENT("Ссылка на пункт соглашения", PREPARATION, CONTRACT_PRICE),

    PP_LINK_TO_CLAUSE_AGREEMENT_LIABILITY_PROVIDED("Договором предусмотрена ответственность частной стороны за несоблюдение " +
            "вышеуказанных обязательств", PREPARATION, CONTRACT_PRICE),

    PP_MEASURE_TYPE("Тип измерения", PREPARATION, CONTRACT_PRICE), //combo

    PP_METHOD_OF_EXECUTE_OBLIGATION("Обязательства частной стороны, предусмотренные соглашением", PREPARATION, CONTRACT_PRICE),

    PP_NDS_CHECK("Включая НДС", PREPARATION, CONTRACT_PRICE),

    PP_OBLIGATIONS_IN_CASE_OF_RISKS_AGREEMENT_ON_GCHP_MCHP("Объем принимаемых публичным партнером обязательств в случае возникновения " +
            "рисков: Соглашение о ГЧП/МЧП", PREPARATION, WITHOUT_SUBSECTION),

    PP_OBLIGATIONS_IN_CASE_OF_RISKS_GOV_CONTRACT("Объем принимаемых публичным партнером обязательств в случае возникновения " +
            "рисков: Государственный контракт", PREPARATION, WITHOUT_SUBSECTION),

    PP_PROJECT_ASSIGNED_STATUS("Проекту присвоен статус приоритетного/масштабного/ стратегического/комплексного инвестиционного проекта " +
            "либо инвестиционного проекта, имеющего региональное значение", PREPARATION, COMMON_INFO),

    PP_STATE_SUPPORT_MEASURES_SP_IC("Иные меры господдержки, предоставляемые в рамках СПИК", PREPARATION, CONTRACT_PRICE), //combo

    PP_WINNER_CONTRACT_PRICE_OFFER("Предложение победителя конкурса", PREPARATION, CONTRACT_PRICE),
    ; //combo

    String name;
    SectionName sectionName;
    SubSectionName subSectionName;

    ProjectField(String name, SectionName sectionName, SubSectionName subSectionName) {
        this.name = name;
        this.sectionName = sectionName;
        this.subSectionName = subSectionName;
    }

    public String getName() {
        return name;
    }

    public String getSectionName() {
        return sectionName.getName();
    }

    public String getSubSectionName() {
        return subSectionName.getName();
    }

    enum SubSectionName {
        WITHOUT_SUBSECTION(""),
        COMMON_INFO("Общая информация"),
        INVESTMENT_EXPLOITATION_RECOVERY_AMOUNT("Возмещение частных инвестиций на этапе эксплуатации"),
        TECH_ECONOMICS_INDICATORS("Технико-экономические показатели"),
        ENERGY_EFFICIENCY_PLANS("План энергоэффективных мероприятий"),
        REQUISITES_DOCUMENT("Реквизиты и копия соглашения (контракта)"),
        FINANCIAL_CLOSING("Финансовое закрытие"),
        INVESTMENT_CREATION_AMOUNT("Объем инвестиций на стадии создания"),
        LAND_LAW("Права на земельный участок"),
        REQUISITES_TENDER("Реквизиты и копия решения о заключении соглашения"),
        COMPETITION_TENDER("Проведение конкурса"),
        RESULT_COMPETITION_TENDER("Результаты проведения конкурса"),
        CONTRACT_PRICE("Цена контракта"),
        AGREEMENT_PARTIES("Соглашение сторон"),
        EVENTS("События проекта"),
        PRIVATE_PARTNER_THIRD_PARTY_ORGS("Со стороны частного партнера"),
        PUBLIC_PARTNER_THIRD_PARTY_ORGS("Со стороны публичного партнера"),
        FIN_ECONOMICS_INDICATORS("Финансово-экономические показатели"),
        SANCTIONS("Санкции в отношении частного партнера"),
        JUDICIAL_ACTIVITIES("Судебная активность по проекту"),
        CONCESSIONAIRE("Концессионер"),
        OWNERSHIP_STRUCTURES("Структура владения частным партнером"),
        FINANCIAL_STRUCTURE("Структура финансирования проекта"),
        INVESTMENT_BOOL_CRITERIAS("Критерии качественных инфраструктурных инвестиций"),
        COMPETITION_CRITERIA("Критерии конкурса"),
        CBC_INVEST1("Предельная сумма компенсации концессионеру/частному партнеру по обеспечению его минимального гарантированного дохода"),
        CBC_INVEST2("Размер фактически выплаченной компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода"),
        CBC_INVEST3("Размер фактически выплаченной концессионеру/частному партнеру компенсации недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг"),
        CBC_INVEST4("Размер фактически выплаченной компенсации концессионеру/частному партнеру льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента"),
        REMAIN_DEBT("Остаток задолженности");
        String name;

        SubSectionName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum SectionName {
        GENERAL_INFO("Общие сведения"),
        DESCRIPTION("Описание проекта"),
        CREATION("Создание проекта"),
        PREPARATION("Подготовка проекта"),
        EXPLOITATION("Эксплуатация"),
        TERMINATE("Прекращение"),
        CBC("Условные бюджетные обяхательства"),
        CHANGE_CONDITIONS("Изменение условий"),
        ADDITIONALLY("Дополнительно"),
        COMMENT("Комментарий");

        String name;

        SectionName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
