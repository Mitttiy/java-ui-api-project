package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.*;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import ru.ibs.gasu.client.presenters.GchpFilterPanelPresenter;
import ru.ibs.gasu.client.widgets.componens.*;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.Project;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.soap.generated.gchpdocs.ProjectStatus;
import ru.ibs.gasu.common.soap.generated.gchpdocs.SvrOrg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static com.google.gwt.safehtml.shared.SafeHtmlUtils.fromTrustedString;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

@SuppressWarnings("GWTStyleCheck")
public class ProjectsFiltersView implements IsWidget, GchpFilterPanelPresenter.Display {

    interface Resource extends ClientBundle {
        @Source("blue.png")
        ImageResource s_1();

        @Source("yellow.png")
        ImageResource s_2();

        @Source("red.png")
        ImageResource s_3();

        @Source("green.png")
        ImageResource s_4();

        @Source("nothing.png")
        ImageResource nothing();
    }

    private final ProjectsFiltersView.Resource resources = GWT.create(ProjectsFiltersView.Resource.class);

    public ProjectsFiltersView() {
        initWidget();
    }

    @Override
    public IconButton getSearchButton() {
        return searchButton;
    }

    @Override
    public IconButton getClearButton() {
        return clearButton;
    }

    @Override
    public IconButton getExcelExportButton() {
        return excelExportButton;
    }

    @Override
    public TextButton getAddButton() {
        return addButton;
    }

    @Override
    public TextButton getEditProjectButton() {
        return editButton;
    }

    @Override
    public TextButton getDeleteProjectButton() {
        return deleteButton;
    }

    @Override
    public TextButton getViewProjectButton() {
        return viewButton;
    }

    @Override
    public StoreFilterField<String> getIdFilter() {
        return idFilter;
    }

    @Override
    public MultiSelectComboBox<SimpleIdNameModel> getProjectStatusFilter() {
        return projectStatusFilter;
    }

    @Override
    public MultiSelectComboBox<SimpleIdNameModel> getRealizationStatusFilter() {
        return realizationStatusFilter;
    }

    @Override
    public Grid<Project> getGrid() {
        return grid;
    }

    @Override
    public void setData(List<Project> projectModelList) {
        grid.getStore().clear();
        grid.getStore().addAll(projectModelList);
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getImplementationFormFilter() {
        return implementationFormFilter;
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getImplementationLevelFilter() {
        return implementationLevelFilter;
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getImplementationSphereFilter() {
        return implementationSphereFilter;
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getImplementationSectorFilter() {
        return implementationSectorFilter;
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getRegionFilter() {
        return regionFilter;
    }

    @Override
    public TwinTriggerComboBox<SimpleIdNameModel> getPpoFilter() {
        return ppoFilter;
    }

    @Override
    public TwinTriggerComboBox<SvrOrg> getPpkFilter() {
        return ppkFilter;
    }

    @Override
    public StoreFilterField<String> getProjectNameFilter() {
        return projectNameFilter;
    }

    @Override
    public Container getMaskContainer() {
        return mainContainer;
    }

    @Override
    public DocumentCounter getDocumentCounter() {
        return documentCounter;
    }

    private VerticalLayoutContainer mainContainer;
    private Viewport viewport;
    private FieldSet filtersCollapsingField;

    private VerticalLayoutContainer filterContainer;
    private final int TEXT_FILTERS_SIZE = 605;
    private final int COMBO_BOX_FILTERS_SIZE = 300;
    private final int BUTTON_SEARCH_FILTERS_SIZE = 150;
    private final int BUTTON_CLEAR_FILTERS_SIZE = 143;

    // Фильтры
    /**
     * Фильтр "ID"
     */
    private StoreFilterField<String> idFilter;
    /**
     * Фильтр "Статус проекта"
     */
    private MultiSelectComboBox<SimpleIdNameModel> projectStatusFilter;
    /**
     * Фильтр "Статус реализации проекта"
     */
    private MultiSelectComboBox<SimpleIdNameModel> realizationStatusFilter;
    /**
     * Фильтр "Стадия реализации"
     */
    //private TwinTriggerComboBox<SimpleIdNameModel> implementationStageFilter;
    /**
     * Фильтр "Форма реализации"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> implementationFormFilter;
    /**
     * Фильтр "Уровень реализации"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> implementationLevelFilter;
    /**
     * Фильтр "Сфера реализации"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> implementationSphereFilter;
    /**
     * Фильтр "Отрасль реализации"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> implementationSectorFilter;
    /**
     * Фильтр "Регион РФ"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> regionFilter;
    /**
     * Фильтр "Муниципальное образование"
     */
    private TwinTriggerComboBox<SimpleIdNameModel> ppoFilter;
    /**
     * Фильтр "Публичный партнер*Концедент"
     */
    private TwinTriggerComboBox<SvrOrg> ppkFilter;
    /**
     * Фильтр "Наименование проекта"
     */
    private StoreFilterField<String> projectNameFilter;

    // Кнопки фильтров
    private IconButton searchButton;
    private IconButton clearButton;
    private IconButton excelExportButton;

    // Счетчик документов
    private DocumentCounter documentCounter;

    // Таблица
    private Grid<Project> grid;
    private ListStore<Project> gridStore;

    // Кнопки редактирования
    private ToolbarButton addButton;
    private ToolbarButton editButton;
    private ToolbarButton deleteButton;
    private ToolbarButton viewButton;
    private PageToolBar pageToolBar;

    private void initButtons() {
        addButton = new ToolbarButton("Новый документ", "fa-plus");
        editButton = new ToolbarButton("Редактировать документ", "fa-edit");
        deleteButton = new ToolbarButton("Удалить документ", "fa-trash");
        viewButton = new ToolbarButton("Просмотр документа", "fa-eye");
    }

    private void initWidget() {
        viewport = new Viewport();
        mainContainer = new VerticalLayoutContainer();
        initButtons();
        initFilters();
        initDocumentCounter();
        initGrid();
        initToolbar();
        mainContainer.add(filtersCollapsingField);

        HBoxLayoutContainer buttonContainer = new HBoxLayoutContainer();
        buttonContainer.add(addButton, new BoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 5)));
        buttonContainer.add(editButton, new BoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 5)));
        buttonContainer.add(deleteButton, new BoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 5)));
        buttonContainer.add(viewButton, new BoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 5)));

        CssFloatLayoutContainer c = new CssFloatLayoutContainer();
        HTML manual = new HTML(new SafeHtmlBuilder().appendHtmlConstant("<a style=\"text-decoration:none\" href=\"/preview?fileId=231\">\n" +
                "    <i style=\"padding: 5px; cursor: pointer; color:blue;\" class=\"fa fa-file-pdf-o btn fa-lg\" aria-hidden=\"true\"></i>\n" +
                "    <span style=\"color:blue; font-style: italic;\">Руководство пользователя</span>\n" +
                "</a>").toSafeHtml());

        c.setStyleFloat(Style.Float.RIGHT);
        c.getElement().getStyle().setMarginBottom(-38, Style.Unit.PX);
        c.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        c.getElement().getStyle().setMarginLeft(300, Style.Unit.PX);
        c.getElement().getStyle().setZIndex(800);
        c.add(manual);

        mainContainer.add(c);
        mainContainer.add(buttonContainer);

        mainContainer.add(documentCounter);

        VerticalLayoutContainer griContainer = new VerticalLayoutContainer();
        griContainer.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        griContainer.add(pageToolBar);
        mainContainer.add(griContainer, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

        viewport.add(mainContainer, new MarginData(0, 2, 0, 2));

        mainContainer.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                ProjectsFiltersView.this.adjustGrid();
            }
        });

        filtersCollapsingField.addCollapseHandler(new CollapseEvent.CollapseHandler() {
            @Override
            public void onCollapse(CollapseEvent event) {
                ProjectsFiltersView.this.adjustGrid();
            }
        });

        filtersCollapsingField.addExpandHandler(new ExpandEvent.ExpandHandler() {
            @Override
            public void onExpand(ExpandEvent event) {
                ProjectsFiltersView.this.adjustGrid();
            }
        });
    }

    @Override
    public PageToolBar getPageToolBar() {
        return pageToolBar;
    }

    private void initToolbar() {
        pageToolBar = new PageToolBar();
    }

    private void initDocumentCounter() {
        documentCounter = new DocumentCounter();
    }

    private void initFilters() {
        filtersCollapsingField = new FieldSet();

        filtersCollapsingField.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                Scheduler.get().scheduleDeferred(() -> filtersCollapsingField.setExpanded(false));
            }
        });
        fillFilterContainer();
        filtersCollapsingField.setCollapsible(true);
        filtersCollapsingField.addBeforeCollapseHandler(event -> {
            filtersCollapsingField.setHeading("Отобразить фильтры");
        });
        filtersCollapsingField.addBeforeExpandHandler(event -> {
            filtersCollapsingField.setHeading("Скрыть фильтры");
        });
        filtersCollapsingField.add(filterContainer, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
    }

    private void createProjectStatusFilter() {
        projectStatusFilter = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Выберите статус проекта");
        projectStatusFilter.setWidth(300);
    }

    private void createStatusFilter() {
        realizationStatusFilter = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Выберите статус реализации");
        realizationStatusFilter.setWidth(300);
    }

    private TwinTriggerComboBox<SimpleIdNameModel> createCommonFilterModelComboBox(String emptyText) {
        TwinTriggerComboBox<SimpleIdNameModel> comboBox = new TwinTriggerComboBox<>(
                new ListStore<>(item -> item.getId()),
                item -> item.getName(),
                new AbstractSafeHtmlRenderer<SimpleIdNameModel>() {
                    @Override
                    public SafeHtml render(SimpleIdNameModel object) {
                        return wrapString(object.getName());
                    }
                });
        comboBox.setEmptyText(emptyText);
        comboBox.setEditable(false);
        comboBox.setWidth(COMBO_BOX_FILTERS_SIZE);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return comboBox;
    }

    private TwinTriggerComboBox<SvrOrg> createSvrOrgsModelComboBox(String emptyText) {
        TwinTriggerComboBox<SvrOrg> comboBox = new TwinTriggerComboBox<>(
                new ListStore<>(item -> item.getId()),
                item -> item.getName(),
                new AbstractSafeHtmlRenderer<SvrOrg>() {
                    @Override
                    public SafeHtml render(SvrOrg object) {
                        return wrapString(object.getName());
                    }
                });
        comboBox.setEmptyText(emptyText);
        comboBox.setEditable(false);
        comboBox.setWidth(COMBO_BOX_FILTERS_SIZE);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return comboBox;
    }

    private void createImplementationFormFilter() {
        implementationFormFilter = createCommonFilterModelComboBox("Выберите форму реализации");
    }

    private void createImplementationLevelFilter() {
        implementationLevelFilter = createCommonFilterModelComboBox("Выберите уровень");
    }

    private void createImplementationSphereFilter() {
        implementationSphereFilter = createCommonFilterModelComboBox("Выберите сферу реализации проекта");
    }

    private void createImplementationBranchFilter() {
        implementationSectorFilter = createCommonFilterModelComboBox("Выберите отрасль реализации");
    }

    private void createRegionFilter() {
        regionFilter = createCommonFilterModelComboBox("Выберите регион РФ");
    }

    private void createMunicipalityFilter() {
        ppoFilter = createCommonFilterModelComboBox("Выберите МО");
    }

    private void createPublicPartnerFilter() {
        ppkFilter = createSvrOrgsModelComboBox("Выберите публичного партнера");
        ppkFilter.setWidth(604);
    }

    private void createProjectNameFilter() {
        projectNameFilter = new StoreFilterField<String>() {
            @Override
            protected boolean doSelect(Store<String> store, String parent, String item, String filter) {
                return false;
            }
        };
        projectNameFilter.setBorders(false);
        projectNameFilter.setValidationDelay(500);
        projectNameFilter.setWidth(TEXT_FILTERS_SIZE);
        projectNameFilter.setEmptyText("Введите наименование");
    }

    private FieldLabel createTestFilter() {
        StoreFilterField<String> projectNameFilter = new StoreFilterField<String>() {
            @Override
            protected boolean doSelect(Store<String> store, String parent, String item, String filter) {
                return false;
            }
        };
        projectNameFilter.setBorders(false);
        projectNameFilter.setValidationDelay(500);
        projectNameFilter.setEmptyText("Введите наименование");

        FieldLabel projectNameFilterLabel = new FieldLabel(projectNameFilter, "Наименование проекта");
        projectNameFilterLabel.setLabelAlign(FormPanel.LabelAlign.TOP);
        return projectNameFilterLabel;
    }

    private FieldLabel createFieldLabel(Widget widget, String text) {
        FieldLabel fieldLabel = new FieldLabel(widget, text);
        fieldLabel.setLabelAlign(FormPanel.LabelAlign.TOP);
        return fieldLabel;
    }

    private void createIdFilter() {
        idFilter = new StoreFilterField<String>() {
            @Override
            protected boolean doSelect(Store<String> store, String parent, String item, String filter) {
                return false;
            }
        };
        idFilter.setBorders(false);
        idFilter.setValidationDelay(500);
        idFilter.setWidth(TEXT_FILTERS_SIZE);
        idFilter.setEmptyText("Введите ID проекта");
    }

    private void createSearchButton() {
        searchButton = new IconButton("Применить", "fa-search", 1);
        searchButton.addStyleName("x-toolbar-mark");
    }

    private void createClearButton() {
        clearButton = new IconButton("Очистить", "fa-eraser", 1);
        clearButton.addStyleName("x-toolbar-mark");
    }

    private void createExcelExportButton() {
        excelExportButton = new IconButton("Экспорт", "fa-file-excel-o", 1);
        excelExportButton.addStyleName("x-toolbar-mark");
    }

    private void fillFilterContainer() {
        filterContainer = new VerticalLayoutContainer();

        createIdFilter();
        createProjectStatusFilter();
        createStatusFilter();
        //createImplementationStageFilter();
        createImplementationFormFilter();
        createImplementationLevelFilter();
        createImplementationSphereFilter();
        createImplementationBranchFilter();
        createRegionFilter();
        createMunicipalityFilter();
        createPublicPartnerFilter();
        createProjectNameFilter();
        createSearchButton();
        createClearButton();
        createExcelExportButton();

        List<Widget> filters = Arrays.asList(
                createFieldLabel(projectStatusFilter, "Статус заполнения по проекту"),
                createFieldLabel(realizationStatusFilter, "Статус реализации проекта"),
                //createFieldLabel(implementationStageFilter, "Стадия реализации"),
                createFieldLabel(implementationFormFilter, "Форма реализации"),
                createFieldLabel(implementationLevelFilter, "Уровень реализации"),
                createFieldLabel(implementationSphereFilter, "Сфера реализации"),
                createFieldLabel(implementationSectorFilter, "Отрасль реализации"),
                createFieldLabel(regionFilter, "Регион РФ"),
                createFieldLabel(ppoFilter, "Муниципальное образование"),
                createFieldLabel(ppkFilter, "Концедент/ Публичная сторона")
        );

        CssFloatLayoutContainer.CssFloatData floatData = new CssFloatLayoutContainer.CssFloatData(-1, new Margins(2));

        CssFloatLayoutContainer combosContainer = new CssFloatLayoutContainer();
        filters.forEach(filter -> combosContainer.add(filter, floatData));
        combosContainer.add(searchButton, new CssFloatLayoutContainer.CssFloatData(BUTTON_SEARCH_FILTERS_SIZE, new Margins(22, 5, 0, 2)));
        combosContainer.add(clearButton, new CssFloatLayoutContainer.CssFloatData(BUTTON_CLEAR_FILTERS_SIZE, new Margins(22, 5, 0, 0)));
        combosContainer.add(excelExportButton, new CssFloatLayoutContainer.CssFloatData(BUTTON_CLEAR_FILTERS_SIZE, new Margins(22, 0, 0, 0)));

        CssFloatLayoutContainer textsContainer = new CssFloatLayoutContainer();
        textsContainer.add(createFieldLabel(idFilter, "ID"), floatData);
        textsContainer.add(createFieldLabel(projectNameFilter, "Наименование проекта"), floatData);

        filterContainer.add(textsContainer);
        filterContainer.add(combosContainer);
    }

    private SafeHtml createHeader(String name) {
        return fromTrustedString("<span style=\"text-align: center; font-weight: bold;\">" + name + "</span>");
    }

    private void initGrid() {
        final int INIT_COL_WIDTH = 350;
        // ID
        // Наименование проекта
        // Стадия реализации
        // Уровень реализации
        // Сфера реализации
        // Отрасль реализации
        // Форма реализации
        // Статус проекта
        // Наличие ЧИ
        // Публичный партнер
        // Регион
        // Дата соглашения
        // Срок действия
        // Объем инвестиций
        ColumnConfig<Project, String> idCol = new ColumnConfig<>(
                createReadOnlyVP(projectModel -> String.valueOf(projectModel.getId()), "id"), 100, createHeader("ID")); // почему-то с method reference конкретно здесь ошибка на клиенте

        ColumnConfig<Project, ProjectStatus> projectStatusCol = new ColumnConfig<>(
                new ValueProvider<Project, ProjectStatus>() {
                    @Override
                    public ProjectStatus getValue(Project object) {
                        if (object == null || object.getGiProjectStatus() == null) {
                            return null;
                        }
                        return object.getGiProjectStatus();

                    }

                    @Override
                    public void setValue(Project object, ProjectStatus value) {
                    }

                    @Override
                    public String getPath() {
                        return "projectStatus";
                    }
                }, 110);

        projectStatusCol.setCell(new AbstractCell<ProjectStatus>() {
            @Override
            public void render(Context context, ProjectStatus projectStatus, SafeHtmlBuilder sb) {

                sb.appendHtmlConstant("<div title='" + projectStatus.getName() + "'><i class='fa fa-dot-circle-o fa-2x' aria-hidden='true' style='color: " + projectStatus.getColorCode() + ";'></i></div>");
            }
        });

        projectStatusCol.setHeader((SafeHtml) () -> "<span style=\"text-align: center; font-size: 14px; font-weight: bold;\">Статус заполнения по проекту</span>");
        projectStatusCol.setMenuDisabled(true);
        projectStatusCol.setSortable(false);

        ColumnConfig<Project, String> nameCol = new ColumnConfig<>(
                createReadOnlyVP(Project::getGiName, "giName"),
                400,
                createHeader("Наименование проекта"));

        ColumnConfig<Project, String> implementationLevelCol = new ColumnConfig<>(
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRealizationLevel().getName()), "implementationLevelCol"),
                INIT_COL_WIDTH,
                createHeader("Уровень реализации"));
        implementationLevelCol.setSortable(false);

        ColumnConfig<Project, String> implementationSphereCol = new ColumnConfig<>(
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRealizationSphere().getName()), "implementationSphereCol"),
                INIT_COL_WIDTH,
                createHeader("Сфера реализации"));
        implementationSphereCol.setSortable(false);

        ColumnConfig<Project, String> implementationBranchCol = new ColumnConfig<>(
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRealizationSector().getName()), "implementationBranchCol"),
                INIT_COL_WIDTH,
                createHeader("Отрасль реализации"));
        implementationBranchCol.setSortable(false);

        ColumnConfig<Project, String> implementationFormCol = new ColumnConfig<>(
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRealizationForm().getName()), "implementationFormCol"),
                INIT_COL_WIDTH,
                createHeader("Форма реализации"));
        implementationFormCol.setSortable(false);

        ColumnConfig<Project, String> realizationStatusCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRealizationStatus().getName()), "realizationStatusCol"),
                INIT_COL_WIDTH,
                createHeader("Статус реализации проекта"));
        realizationStatusCol.setSortable(false);

        ColumnConfig<Project, String> chiCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiInitiationMethod().getName()), "chiCol"),
                INIT_COL_WIDTH,
                createHeader("Способ инициации проекта"));
        chiCol.setSortable(false);

        ColumnConfig<Project, String> publicPartnerCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiPublicPartner().getName()), "publicPartnerCol"),
                INIT_COL_WIDTH,
                createHeader("Концедент/ Публичная сторона"));
        publicPartnerCol.setSortable(false);

        ColumnConfig<Project, String> concessionaireCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getCrConcessionaire()), "concessionaireCol"),
                INIT_COL_WIDTH,
                createHeader("Концессионер/ Частная сторона"));
        concessionaireCol.setSortable(false);

        ColumnConfig<Project, String> regionCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> pm.getGiRegion().getName()), "regionCol"),
                INIT_COL_WIDTH,
                createHeader("Регион"));
        regionCol.setSortable(false);

        ColumnConfig<Project, String> dateCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> formatDate(getDate(pm.getCrAgreementStartDate()))), "crAgreementStartDate"),
                INIT_COL_WIDTH,
                createHeader("Дата соглашения"));

        ColumnConfig<Project, String> validityCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> String.valueOf(pm.getCrAgreementValidity())), "crAgreementValidity"),
                INIT_COL_WIDTH,
                createHeader("Срок действия"));
        validityCol.setHorizontalAlignment(ALIGN_RIGHT);

        ColumnConfig<Project, String> investmentSizeCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> String.valueOf(NumberFormat.getFormat("#,##0.0#").format(pm.getCrInvestmentCreationAmount().getIndicators().get(0).getPlan()))), "crInvestmentsTotal"),
                INIT_COL_WIDTH,
                createHeader("Общий объем инвестиций, тыс. руб"));
        investmentSizeCol.setHorizontalAlignment(ALIGN_RIGHT);
        investmentSizeCol.setComparator(WidgetUtils.createStringDoubleComparator());

        ColumnConfig<Project, String> privateInvestmentSizeCol = new ColumnConfig<>( // default hidden
                createReadOnlyVP(pm -> getStringValOrEmpty(() -> String.valueOf(NumberFormat.getFormat("#,##0.0#").format(pm.getCrInvestmentCreationAmount().getIndicators().get(1).getPlan()))), "crInvestmentsPrivate"),
                INIT_COL_WIDTH,
                createHeader("Объем частных инвестиций, тыс. руб"));
        privateInvestmentSizeCol.setHorizontalAlignment(ALIGN_RIGHT);
        privateInvestmentSizeCol.setComparator(WidgetUtils.createStringDoubleComparator());

        realizationStatusCol.setHidden(true);
        chiCol.setHidden(true);
        publicPartnerCol.setHidden(true);
        concessionaireCol.setHidden(true);
        regionCol.setHidden(true);
        dateCol.setHidden(true);
        validityCol.setHidden(true);
        investmentSizeCol.setHidden(true);
        privateInvestmentSizeCol.setHidden(true);

        List<ColumnConfig<Project, ?>> columns = Arrays.asList(
                idCol,
                projectStatusCol,
                nameCol,
                implementationLevelCol,
                implementationSphereCol,
                implementationBranchCol,
                implementationFormCol,
                realizationStatusCol,
                chiCol,
                publicPartnerCol,
                concessionaireCol,
                regionCol,
                dateCol,
                validityCol,
                investmentSizeCol,
                privateInvestmentSizeCol
        );

        gridStore = new ListStore<Project>(new ModelKeyProvider<Project>() {
            @Override
            public String getKey(Project item) {
                return String.valueOf(item.getId());
            }
        });
        ColumnModel<Project> cm = new ColumnModel<>(new ArrayList<>(columns));

        cm.addColumnHiddenChangeHandler(new ColumnHiddenChangeEvent.ColumnHiddenChangeHandler() {
            @Override
            public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
                Scheduler.get().scheduleDeferred(() -> grid.getView().refresh(false));
            }
        });

        grid = new Grid<Project>(gridStore, cm);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);
        grid.setLoadMask(true);

        columns.forEach(column -> {
            if (!"projectStatus".equals(column.getPath())) {
                ((ColumnConfig<Project, String>) column).setCell(new AbstractCell<String>() {
                    @Override
                    public void render(Context context, String value, SafeHtmlBuilder sb) {
                        if (grid.getColumnModel().getColumns().get(context.getColumn()).isHidden()) {
                            sb.append(Util.NBSP_SAFE_HTML);
                            return;
                        }
                        if (value == null) {
                            sb.append(Util.NBSP_SAFE_HTML);
                        } else {
                            sb.append(wrapString(value));
                        }
                    }
                });
                // vertical header alignment
                column.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
            }
        });
    }

    private void adjustGrid() {
        int top = grid.getAbsoluteTop();
        int clientHeight = Window.getClientHeight();
        grid.setHeight(clientHeight - top - 50); // or toolbar height
    }

    /**
     * @param function функция получения строкового значения ячейки из объекта документа
     * @param path строковое наименование столбца; в случае, если поле имеет возможность сортировки на
     *             стороне сервера, то этот путь должен совпадать с наименованием поля entity в api.
     */
    private ValueProvider<Project, String> createReadOnlyVP(Function<Project, String> function, String path) {
        return new ValueProvider<Project, String>() {
            @Override
            public String getValue(Project object) {
                return function.apply(object);
            }

            @Override
            public void setValue(Project object, String value) {
            }

            @Override
            public String getPath() {
                return path;
            }
        };
    }

    @Override
    public Widget asWidget() {
        return viewport;
    }
}
