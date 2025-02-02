set search_path = gchp;

insert into investment_indicator_type(id, name) values (1,  'Общий объем инвестиций');
insert into investment_indicator_type(id, name) values (2,  'Объем частных инвестиций');
insert into investment_indicator_type(id, name) values (3,  'Объем бюджетных расходов');
insert into investment_indicator_type(id, name) values (4,  'Размер аванса, выплачиваемого заказчиком');
insert into investment_indicator_type(id, name) values (5,  'Субсидии на эксплуатационные расходы');
insert into investment_indicator_type(id, name) values (6,  'Субсидии на возмещение инвестиций');
insert into investment_indicator_type(id, name) values (7,  'За счет средств федерального бюджета');
insert into investment_indicator_type(id, name) values (8,  'За счет средств регионального бюджета');
insert into investment_indicator_type(id, name) values (9,  'За счет средств местного бюджета');

insert into measure_type(id, name) values (1, 'В ценах соответствующих лет');
insert into measure_type(id, name) values (2, 'На дату');

insert into finance_indicator(id, serial_num, name) values (1, '1', 'Общий объем инвестиций');
insert into finance_indicator(id, serial_num, name) values (2, '1.1', 'Объем частных средств');
insert into finance_indicator(id, serial_num, name) values (3, '1.1.1', 'Собственные');
insert into finance_indicator(id, serial_num, name) values (4, '1.1.1.1', 'Имущественный взнос');
insert into finance_indicator(id, serial_num, name) values (14, '1.1.1.1.1', '- в виде денежных средств');
insert into finance_indicator(id, serial_num, name) values (15, '1.1.1.1.2', '- в виде материальных активов');
insert into finance_indicator(id, serial_num, name) values (5, '1.1.1.2', 'Акционерный заем');
insert into finance_indicator(id, serial_num, name) values (6, '1.1.1.3', 'Иные формы финансирования');
insert into finance_indicator(id, serial_num, name) values (7, '1.1.2', 'Заемные');
insert into finance_indicator(id, serial_num, name) values (16, '1.1.2.1', 'Долговое финансирование');
insert into finance_indicator(id, serial_num, name) values (17, '1.1.2.2', 'Облигационное финансирование');
insert into finance_indicator(id, serial_num, name) values (18, '1.1.2.3', 'Иное заемное финансирование');
insert into finance_indicator(id, serial_num, name) values (8, '1.2', 'Объем бюджетных средств');
insert into finance_indicator(id, serial_num, name) values (9, '1.2.1', 'Федеральный бюджет');
insert into finance_indicator(id, serial_num, name) values (10, '1.2.2', 'Региональный бюджет');
insert into finance_indicator(id, serial_num, name) values (11, '1.2.2.1', '- из них предоставлено в виде межбюджетных трансфертов из федерального бюджета');
insert into finance_indicator(id, serial_num, name) values (12, '1.2.3', 'Местный бюджет');
insert into finance_indicator(id, serial_num, name) values (13, '1.2.3.1', '- из них предоставлено в виде межбюджетных трансфертов из регионального бюджета');

insert into sanction_type(id, name) values (1, 'Штрафные санкции');
insert into sanction_type(id, name) values (2, 'Возмещение убытков публичного партнера');

insert into investments_criteria_ind_boolean(id, name) values (1, 'Создаются ли новые рабочие места после ввода объекта в эксплуатацию?');
insert into investments_criteria_ind_boolean(id, name) values (2, 'Проходят ли сотрудники создаваемого предприятия курсы повышения квалификации?');
insert into investments_criteria_ind_boolean(id, name) values (3, 'Предусматривает ли проект меры по снижению негативного воздействия на окружающую среду?');
insert into investments_criteria_ind_boolean(id, name) values (4, 'В проекте учтены риски природных катастроф, изменения климата, чрезвычайных происшествий и прочие риски');
insert into investments_criteria_ind_boolean(id, name) values (5, 'Проект ориентирован на предоставление товаров и услуг местному населению');
insert into investments_criteria_ind_boolean(id, name) values (6, 'Граждане принимают участие в разработке проекта');
insert into investments_criteria_ind_boolean(id, name) values (7, 'В проекте минимизировано изъятие земельных участков и иной собственности у граждан');
insert into investments_criteria_ind_boolean(id, name) values (8, 'В проекте предусмотрены меры по восстановлению условий жизнедеятельности для населения, переселенного в результате реализации проекта');
insert into investments_criteria_ind_boolean(id, name) values (9, 'В проекте предусмотрено использование инновационных технологий');
