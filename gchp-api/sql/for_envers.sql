set search_path = gchp;

insert into revinfo(rev) values (1);
insert into revinfo(rev) values (2);
insert into revinfo(rev) values (3);
insert into revinfo(rev) values (4);
insert into revinfo(rev) values (5);
insert into revinfo(rev) values (6);
insert into revinfo(rev) values (45);
insert into revinfo(rev) values (46);
insert into revinfo(rev) values (47);
insert into revinfo(rev) values (48);
insert into revinfo(rev) values (49);
insert into revinfo(rev) values (50);
insert into revinfo(rev) values (51);
insert into revinfo(rev) values (52);
insert into revinfo(rev) values (53);
insert into revinfo(rev) values (54);

insert into investment_indicator_type_his(id, rev, revtype, name) values (1, 1, 0, 'Общий объем инвестиций');
insert into investment_indicator_type_his(id, rev, revtype, name) values (2, 2, 0,'Объем частных инвестиций');
insert into investment_indicator_type_his(id, rev, revtype, name) values (3, 3, 0, 'Объем бюджетных расходов');
insert into investment_indicator_type_his(id, rev, revtype, name) values (4, 4, 0, 'Размер аванса, выплачиваемого заказчиком');
insert into investment_indicator_type_his(id, rev, revtype, name) values (5, 5, 0, 'Субсидии на эксплуатационные расходы');
insert into investment_indicator_type_his(id, rev, revtype, name) values (6, 6, 0, 'Субсидии на возмещение инвестиций');
insert into investment_indicator_type_his(id, rev, revtype, name) values (7, 45, 0, 'За счет средств федерального бюджета');
insert into investment_indicator_type_his(id, rev, revtype, name) values (8, 46, 0, 'За счет средств регионального бюджета');
insert into investment_indicator_type_his(id, rev, revtype, name) values (9, 47, 0, 'За счет средств местного бюджета');
insert into investment_indicator_type_his(id, rev, revtype, name) values (10, 48, 0, 'Собственные инвестиции');
insert into investment_indicator_type_his(id, rev, revtype, name) values (11, 49, 0, 'Заемные средства');
insert into investment_indicator_type_his(id, rev, revtype, name) values (12, 50, 0, 'За счет иных источников');

insert into investment_indicator_type_his(id, rev, revtype, name) values (13, 51, 0, 'Всего расходы концессионера/частного партнера');
insert into investment_indicator_type_his(id, rev, revtype, name) values (14, 52, 0, 'Условно-постоянные расходы концессионера/частного партнера');
insert into investment_indicator_type_his(id, rev, revtype, name) values (15, 53, 0, 'Условно-переменные расходы концессионера/частного партнера');
insert into investment_indicator_type_his(id, rev, revtype, name) values (16, 54, 0, 'Капитальный ремонт');

insert into circumstance_stage_type(id, name) values (1, 'Всего предельная сумма компенсации');
insert into circumstance_stage_type(id, name) values (2, 'Подготовительный этап');
insert into circumstance_stage_type(id, name) values (3, 'Этап строительства');
insert into circumstance_stage_type(id, name) values (4, 'Этап эксплуатации');

insert into revinfo(rev) values (14);
insert into revinfo(rev) values (15);

insert into sanction_type_his(id, rev, revtype, name) values (1, 14, 0, 'Штрафные санкции');
insert into sanction_type_his(id, rev, revtype, name) values (2, 15, 0, 'Возмещение убытков публичного партнера');

insert into revinfo(rev) values (16);
insert into revinfo(rev) values (17);
insert into revinfo(rev) values (18);
insert into revinfo(rev) values (19);
insert into revinfo(rev) values (20);
insert into revinfo(rev) values (21);
insert into revinfo(rev) values (22);
insert into revinfo(rev) values (23);
insert into revinfo(rev) values (24);
insert into revinfo(rev) values (25);
insert into revinfo(rev) values (26);
insert into revinfo(rev) values (27);
insert into revinfo(rev) values (28);
insert into revinfo(rev) values (40);
insert into revinfo(rev) values (41);
insert into revinfo(rev) values (42);
insert into revinfo(rev) values (43);
insert into revinfo(rev) values (44);


insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (1, 16, 0, '1', 'Общий объем инвестиций');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (2, 17, 0, '1.1', 'Объем частных средств');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (3, 18, 0, '1.1.1', 'Собственные');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (4, 19, 0, '1.1.1.1', 'Имущественный взнос');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (14, 40, 0, '1.1.1.1.1', '- в виде денежных средств');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (15, 41, 0, '1.1.1.1.2', '- в виде материальных активов');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (5, 20, 0, '1.1.1.2', 'Акционерный заем');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (6, 21, 0, '1.1.1.3', 'Иные формы финансирования');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (7, 22, 0, '1.1.2', 'Заемные');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (16, 42, 0, '1.1.2.1', 'Долговое финансирование');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (17, 43, 0, '1.1.2.2', 'Облигационное финансирование');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (18, 44, 0, '1.1.2.3', 'Иное заемное финансирование');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (8, 23, 0, '1.2', 'Объем бюджетных средств');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (9, 24, 0, '1.2.1', 'Федеральный бюджет');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (10, 25, 0, '1.2.2', 'Региональный бюджет');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (11, 26, 0, '1.2.2.1', '- из них предоставлено в виде межбюджетных трансфертов из федерального бюджета');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (12, 27, 0, '1.2.3', 'Местный бюджет');
insert into finance_indicator_his(id, rev, revtype, serial_num, name) values (13, 28, 0, '1.2.3.1', '- из них предоставлено в виде межбюджетных трансфертов из регионального бюджета');


insert into revinfo(rev) values (29);
insert into revinfo(rev) values (30);

insert into measure_type_his(id, rev, revtype, name) values (1, 29, 0, 'В ценах соответствующих лет');
insert into measure_type_his(id, rev, revtype, name) values (2, 30, 0, 'На дату');

insert into revinfo(rev) values (31);
insert into revinfo(rev) values (32);
insert into revinfo(rev) values (33);
insert into revinfo(rev) values (34);
insert into revinfo(rev) values (35);
insert into revinfo(rev) values (36);
insert into revinfo(rev) values (37);
insert into revinfo(rev) values (38);
insert into revinfo(rev) values (39);

insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (1, 31, 0, 'Создаются ли новые рабочие места после ввода объекта в эксплуатацию?');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (2, 32, 0, 'Проходят ли сотрудники создаваемого предприятия курсы повышения квалификации?');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (3, 33, 0, 'Предусматривает ли проект меры по снижению негативного воздействия на окружающую среду?');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (4, 34, 0, 'В проекте учтены риски природных катастроф, изменения климата, чрезвычайных происшествий и прочие риски');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (5, 35, 0, 'Проект ориентирован на предоставление товаров и услуг местному населению');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (6, 36, 0, 'Граждане принимают участие в разработке проекта');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (7, 37, 0, 'В проекте минимизировано изъятие земельных участков и иной собственности у граждан');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (8, 38, 0, 'В проекте предусмотрены меры по восстановлению условий жизнедеятельности для населения, переселенного в результате реализации проекта');
insert into investments_criteria_ind_boolean_his(id, rev, revtype, name) values (9, 39, 0, 'В проекте предусмотрено использование инновационных технологий');

SELECT setval('hibernate_sequence', 100);