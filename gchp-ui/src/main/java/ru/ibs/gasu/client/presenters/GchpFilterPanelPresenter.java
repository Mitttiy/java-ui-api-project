package ru.ibs.gasu.client.presenters;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.*;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.events.*;
import ru.ibs.gasu.client.widgets.ExcelExportWidget;
import ru.ibs.gasu.client.widgets.componens.*;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.Project;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.soap.generated.gchpdicts.*;
import ru.ibs.gasu.common.soap.generated.gchpdocs.Dir;
import ru.ibs.gasu.common.soap.generated.gchpdocs.SortField;
import ru.ibs.gasu.common.soap.generated.gchpdocs.SortInfo;
import ru.ibs.gasu.common.soap.generated.gchpdocs.*;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GchpFilterPanelPresenter implements Presenter {

    private final GchpApi rpcService;
    private final HandlerManager handlerManager;
    private final Display view;
    private final RolePreferences preferences;

    public interface Display {
        IconButton getSearchButton();

        IconButton getClearButton();

        IconButton getExcelExportButton();

        TextButton getAddButton();

        TextButton getEditProjectButton();

        TextButton getDeleteProjectButton();

        TextButton getViewProjectButton();

        // Фильтры
        StoreFilterField<String> getIdFilter();

        MultiSelectComboBox<SimpleIdNameModel> getProjectStatusFilter();

        MultiSelectComboBox<SimpleIdNameModel> getRealizationStatusFilter();

        //TwinTriggerComboBox<SimpleIdNameModel> getImplementationStageFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getImplementationFormFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getImplementationLevelFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getImplementationSphereFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getImplementationSectorFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getRegionFilter();

        TwinTriggerComboBox<SimpleIdNameModel> getPpoFilter();

        TwinTriggerComboBox<SvrOrg> getPpkFilter();

        StoreFilterField<String> getProjectNameFilter();

        Container getMaskContainer();

        // Таблица
        Grid<Project> getGrid();

        PageToolBar getPageToolBar();

        // счетчик документов
        DocumentCounter getDocumentCounter();

        void setData(List<Project> projectModelList);

        Widget asWidget();
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.add(view.asWidget());
        fetchProjectStatusDictionary();
        fetchRealizationStatusDictionary();
        fetchImplementationFormFilterData();
        fetchRegionFilterData();
        view.getImplementationSphereFilter().mask();
        view.getImplementationSectorFilter().mask();
        view.getImplementationLevelFilter().mask();
        view.getPpoFilter().mask();
    }

    private void fetchFilteredDocumentsList(int limit, int offset, List<SortField> sortFields, AsyncCallback<PagingLoadResult<Project>> callback) {
        rpcService.paginateAndFilterDocuments(createDocumentsFilters(limit, offset, sortFields), new MethodCallback<PagingLoadResultBean<Project>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список документов").show();
                view.getGrid().unmask();
                view.getPageToolBar().getPagingToolBar().enable();
            }

            @Override
            public void onSuccess(Method method, PagingLoadResultBean<Project> response) {
                callback.onSuccess(response);
            }
        });
    }

    private DocumentsFilterPaginateCriteria createDocumentsFilters(int limit, int offset, List<SortField> sortFields) {
        DocumentsFilterPaginateCriteria criteria = new DocumentsFilterPaginateCriteria();
        criteria.setLimit(limit);
        criteria.setOffset(offset);
        if (preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)){
            criteria.setFormId(getStringValOrNull(() -> view.getImplementationFormFilter().getCurrentValue().getId()) == null? "101" : getStringValOrNull(() -> view.getImplementationFormFilter().getCurrentValue().getId()));
        } else {
            criteria.setFormId(getStringValOrNull(() -> view.getImplementationFormFilter().getCurrentValue().getId()));
        }
        criteria.setDocId(getLongValOrNull(() -> view.getIdFilter().getValue()));
        criteria.setDocName(getStringValOrNull(() -> view.getProjectNameFilter().getValue()));
        criteria.getRealizationStatusIds().addAll(view.getRealizationStatusFilter().getSelectedItems().stream().map(rStatus -> rStatus.getId()).collect(Collectors.toList()));
        criteria.getProjectStatusIds().addAll(view.getProjectStatusFilter().getSelectedItems().stream().map(pStatus -> pStatus.getId()).collect(Collectors.toList()));
        criteria.setLevelId(getStringValOrNull(() -> view.getImplementationLevelFilter().getCurrentValue().getId()));
        criteria.setSphereId(getStringValOrNull(() -> view.getImplementationSphereFilter().getCurrentValue().getId()));
        criteria.setDicGasuSp1Id(getStringValOrNull(() -> view.getRegionFilter().getCurrentValue().getId()));
        criteria.setSubjectId(getStringValOrNull(() -> view.getPpoFilter().getCurrentValue().getId()));
        criteria.setMunicipalId(getStringValOrNull(() -> view.getPpoFilter().getCurrentValue().getId()));
        criteria.setSectorId(getStringValOrNull(() -> view.getImplementationSectorFilter().getValue().getId()));
        criteria.setPublicPartnerId(getStringValOrNull(() -> view.getPpkFilter().getCurrentValue().getOgrn()));

        setRoleModelCriteria(criteria);

        SortInfo sortInfo = new SortInfo();

        sortInfo.getFields().addAll(sortFields);

        if (sortFields.size() == 0) {
            SortField sortField = new SortField();
            sortField.setField("createDate");
            sortField.setDirection(Dir.DESC);
            sortInfo.getFields().add(sortField);
        }

        criteria.setSortInfo(sortInfo);
        return criteria;
    }

    private void setRoleModelCriteria(DocumentsFilterPaginateCriteria criteria) {
        if (criteria.getSphereId() == null && preferences.isFilterByRealizationSphere()) {
            criteria.setSphereId(String.valueOf(preferences.getRealizationSphere()));
        }

        if (criteria.getLevelId() == null && preferences.isFilterByRealizationLevel() && !preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)) {
            criteria.setLevelId(String.valueOf(preferences.getRealizationLevel()));
        }

        if (criteria.getFormId() == null && preferences.isFilterByRealizationForm()) {
            criteria.setFormId(String.valueOf(preferences.getRealizationForm()));
        }

        if (criteria.getSectorId() == null && preferences.isFilterByRealizationSector()) {
            criteria.setSectorId(String.valueOf(preferences.getRealizationSector()));
        }

        if (criteria.getDicGasuSp1Id() == null && preferences.isFilterByRegion()) {
            criteria.setDicGasuSp1Id(preferences.getRegionId());
        }

        if (criteria.getMunicipalId() == null && preferences.isFilterByOktmo()) {
            criteria.setMunicipalId(preferences.getOktmoCode());
        }

        if ((criteria.getPublicPartnerId() == null || criteria.getPublicPartnerId().isEmpty()) && preferences.isFilterByOrg()) {
            criteria.setPublicPartnerId(preferences.getOgrn());
        }
    }

    // todo split in bind buttons, bind grid....
    void bind() {
        view.getSearchButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.getPageToolBar().getPagingToolBar().first();
            }
        });

        view.getAddButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                handlerManager.fireEvent(new CreateNewDocumentEvent());
            }
        });
        if (!preferences.isCanRegistry()) {
            view.getAddButton().removeFromParent();
        }

        view.getEditProjectButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Project selectedDocument = view.getGrid().getSelectionModel().getSelectedItem();
                if (selectedDocument == null) {
                    new MessageBox("Документ не выбран", "Выберите документ для редактирования").show();
                    return;
                }

                if (checkCanEdit()) {
                    handlerManager.fireEvent(new StartEditDocumentEvent(selectedDocument));
                } else {
                    new MessageBox("Недостаточно прав", "Недостаточно прав для выполнения действия!").show();
                }
            }
        });

        view.getViewProjectButton().addSelectHandler(event -> doView());

        view.getGrid().addRowDoubleClickHandler(event -> doView());

        view.getEditProjectButton().setVisible(preferences.isCanEdit());

        view.getDeleteProjectButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Project selectedDocument = view.getGrid().getSelectionModel().getSelectedItem();
                if (selectedDocument == null) {
                    new MessageBox("Документ не выбран", "Выберите документ для удаления").show();
                    return;
                }

                if (!preferences.getUserRoles().contains(UserRole.GCHP_ADMINISTRATOR) && (selectedDocument.getGiProjectStatus().getId() != 1 || selectedDocument.getGiAlwaysDraftStatus())) {
                    new MessageBox("Удаление", "Невозможно удалить опубликованный ранее документ").show();
                    return;
                }

                if (checkCanEdit()) {
                    ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox("Удалить документ", "Вы действительно хотите удалить документ c id = '" + selectedDocument.getId() + "'?");
                    confirmMessageBox.setBorders(false);

                    confirmMessageBox.addDialogHideHandler(confirmEvent -> {
                        if (confirmEvent.getHideButton() == Dialog.PredefinedButton.YES) {
                            view.getGrid().mask();
                            //оставлю событие, вдруг кто-то потом на него подпишется
                            handlerManager.fireEvent(new DeleteDocumentEvent(selectedDocument));

                            rpcService.deleteProject(selectedDocument.getId(), new DefaultCallback<Void>() {
                                @Override
                                public void onFailure(Method method, Throwable throwable) {
                                    view.getGrid().unmask();
                                    new AlertMessageBox("Ошибка", "Не удалось удалить документ").show();
                                }

                                @Override
                                public void onSuccess(Method method, Void response) {
                                    view.getGrid().getStore().remove(selectedDocument);
                                    view.getGrid().unmask();
                                    new MessageBox("Удалено успешно", "Документ c id ='" + selectedDocument.getId() + "' был удален успешно").show();
                                }
                            });
                        }
                    });
                    confirmMessageBox.show();

                } else {
                    new MessageBox("Недостаточно прав", "Недостаточно прав для выполнения действия!").show();
                }
            }
        });
        view.getDeleteProjectButton().setVisible(preferences.isCanDelete());

        view.getClearButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.getProjectStatusFilter().deselectAll();
                view.getRealizationStatusFilter().deselectAll();
                view.getImplementationFormFilter().clear();
                view.getImplementationLevelFilter().clear();
                view.getImplementationSphereFilter().clear();
                view.getImplementationSectorFilter().clear();
                view.getRegionFilter().clear();
                view.getPpoFilter().clear();
                view.getPpkFilter().clear();
                view.getImplementationSphereFilter().mask();
                view.getImplementationSectorFilter().mask();
                view.getImplementationLevelFilter().mask();
                view.getPpoFilter().mask();
                view.getPpkFilter().mask();
                view.getProjectNameFilter().clear();
            }
        });

        view.getExcelExportButton().disable();
        if (preferences.getUserRoles().contains(UserRole.GCHP_CONTROLLER)
                || preferences.isAdmin()
                || preferences.getUserRoles().contains(UserRole.GCHP_USER)
                || preferences.getUserRoles().contains(UserRole.GCHP_ADMINISTRATOR)
                || preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)) {
            view.getExcelExportButton().enable();
        }

        view.getExcelExportButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {

                ExcelExportWidget excelExportWidget = new ExcelExportWidget() {
                    @Override
                    public DocumentsFilterPaginateCriteria getCurrentSearchCriteria() {
                        return createDocumentsFilters(Integer.MAX_VALUE, 0, new ArrayList<>());
                    }
                };

                ((Window) excelExportWidget.asWidget()).show();
            }
        });

        RpcProxy<PagingLoadConfig, PagingLoadResult<Project>> documentsLoaderProxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Project>>() {
            @Override
            public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Project>> callback) {
                List<SortField> sortFields = new ArrayList<>();
                for (com.sencha.gxt.data.shared.SortInfo sortInfo : loadConfig.getSortInfo()) {
                    SortField sf = new SortField();
                    sf.setField(sortInfo.getSortField());
                    sf.setDirection(sortInfo.getSortDir() == SortDir.ASC ? Dir.ASC : Dir.DESC);
                    sortFields.add(sf);
                }
                fetchFilteredDocumentsList(loadConfig.getLimit(), loadConfig.getOffset(), sortFields, callback);
            }
        };
        gridLoader = new PagingLoader<>(documentsLoaderProxy);
        gridLoader.setRemoteSort(true);
        gridLoader.addLoadHandler(new LoadResultListStoreBinding<>(view.getGrid().getStore()));
        gridLoader.addBeforeLoadHandler(new BeforeLoadEvent.BeforeLoadHandler<PagingLoadConfig>() {
            @Override
            public void onBeforeLoad(BeforeLoadEvent<PagingLoadConfig> event) {
                view.getGrid().mask("Загрузка списка документов...");
            }
        });
        gridLoader.addLoadHandler(new LoadHandler<PagingLoadConfig, PagingLoadResult<Project>>() {
            @Override
            public void onLoad(LoadEvent<PagingLoadConfig, PagingLoadResult<Project>> event) {
                view.getGrid().unmask();
            }
        });

        gridLoader.addLoadHandler(new LoadHandler<PagingLoadConfig, PagingLoadResult<Project>>() {
            @Override
            public void onLoad(LoadEvent<PagingLoadConfig, PagingLoadResult<Project>> event) {
                view.getDocumentCounter().updateDocumentsCount(event.getLoadResult().getTotalLength());
            }
        });

        view.getGrid().setLoader(gridLoader);
        view.getPageToolBar().getPagingToolBar().bind(gridLoader);

        view.getRegionFilter().addTwinTriggerClickHandler(new TwinTriggerClickEvent.TwinTriggerClickHandler() {
            @Override
            public void onTwinTriggerClick(TwinTriggerClickEvent event) {
                view.getPpoFilter().getStore().clear();
                view.getPpoFilter().clear();
                view.getPpoFilter().mask();

                view.getPpkFilter().getStore().clear();
                view.getPpkFilter().clear();
                view.getPpkFilter().mask();
            }
        });

        view.getPpoFilter().addTwinTriggerClickHandler(new TwinTriggerClickEvent.TwinTriggerClickHandler() {
            @Override
            public void onTwinTriggerClick(TwinTriggerClickEvent event) {
                view.getPpkFilter().getStore().clear();
                view.getPpkFilter().clear();
                view.getPpkFilter().mask();
            }
        });

        view.getRegionFilter().addSelectionHandler(event -> {
            view.getRegionFilter().setValue(event.getSelectedItem());
            String id = event.getSelectedItem().getId();
            fetchMunicipalityFilterData(id);

            if (!preferences.isFilterByOktmo()) {
                fetchPpkFilterData(id, null);
            }
        });

        view.getImplementationFormFilter().addSelectionHandler(event -> {
            view.getImplementationFormFilter().setValue(event.getSelectedItem());
            String id = event.getSelectedItem().getId();
            fetchImplementationLevelFilterData(Long.valueOf(id));
            fetchImplementationSphereFilterData(Long.valueOf(id));
        });

        view.getImplementationSphereFilter().addSelectionHandler(event -> {
            view.getImplementationSphereFilter().setValue(event.getSelectedItem());
            String formId = view.getImplementationFormFilter().getCurrentValue().getId();
            String id = event.getSelectedItem().getId();
            fetchImplementationSectorFilterData(Long.valueOf(formId), Long.valueOf(id));
        });

        view.getImplementationFormFilter().addTwinTriggerClickHandler(event -> {
            view.getImplementationLevelFilter().getStore().clear();
            view.getImplementationLevelFilter().setEnabled(true);
            view.getImplementationLevelFilter().clear();
            view.getImplementationLevelFilter().mask();

            view.getImplementationSphereFilter().getStore().clear();
            view.getImplementationSphereFilter().setEnabled(true);
            view.getImplementationSphereFilter().clear();
            view.getImplementationSphereFilter().mask();
        });

        view.getImplementationSphereFilter().addTwinTriggerClickHandler(event -> {
            view.getImplementationSectorFilter().getStore().clear();
            view.getImplementationSectorFilter().setEnabled(true);
            view.getImplementationSectorFilter().clear();
            view.getImplementationSectorFilter().mask();
        });

        view.getPpoFilter().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                view.getPpoFilter().setValue(event.getSelectedItem());
                String regionId = view.getRegionFilter().getCurrentValue().getId();
                String id = event.getSelectedItem().getId();
                fetchPpkFilterData(regionId, id);
            }
        });

        view.getPpkFilter().addSelectionHandler(new SelectionHandler<SvrOrg>() {
            @Override
            public void onSelection(SelectionEvent<SvrOrg> event) {
                view.getPpkFilter().setValue(event.getSelectedItem());
                Info.display("Концендент", "Будут загружены все проекты, созданные пользователями с ОГРН: " + event.getSelectedItem().getOgrn());
            }
        });

        view.getImplementationLevelFilter().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                view.getImplementationLevelFilter().setValue(event.getSelectedItem());
            }
        });

        view.getImplementationSectorFilter().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                view.getImplementationSectorFilter().setValue(event.getSelectedItem());
            }
        });

        handlerManager.addHandler(DocumentSuccessfullySavedEvent.TYPE, new DocumentSuccessfullySavedEventHandler() {
            @Override
            public void onDocumentSuccessfullySaved(DocumentSuccessfullySavedEvent event) {
                view.getPageToolBar().getPagingToolBar().first();
                SimpleIdNameModel region = view.getRegionFilter().getCurrentValue();
                if (region != null) {
                    fetchPpkFilterData(region.getId(), getStringValOrNull(() -> view.getPpoFilter().getCurrentValue().getId()));
                }
            }
        });
    }

    void fetchImplementationFormFilterData() {
        RealisationFormFilters filters = new RealisationFormFilters();
        if (preferences.isFilterByRealizationForm()) {
            view.getImplementationFormFilter().setEnabled(false);
            filters.setId(preferences.getRealizationForm().toString());
        }

        if (preferences.isFilterByRealizationLevel()) {
            filters.setImplLevelId(preferences.getRealizationLevel());
        }

        if (preferences.isFilterByRealizationSphere()) {
            filters.setImplSphereId(preferences.getRealizationSphere());
        }

        view.getImplementationFormFilter().clear();
        view.getImplementationFormFilter().mask();
        rpcService.getFilteredRealizationForms(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список форм реализации.").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getImplementationFormFilter().getStore().clear();
                if (preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)){
                    view.getImplementationFormFilter().getStore().addAll(response.stream().filter(status -> status.getId().equals("101") || status.getId().equals("151")).collect(Collectors.toList()));
                    view.getImplementationFormFilter().setValue(view.getImplementationFormFilter().getStore().get(0));
                    fetchImplementationLevelFilterData(Long.valueOf(view.getImplementationFormFilter().getStore().get(0).getId()));
                    fetchImplementationSphereFilterData(Long.valueOf(view.getImplementationFormFilter().getStore().get(0).getId()));
                } else {
                    view.getImplementationFormFilter().getStore().addAll(response);
                }
                view.getImplementationFormFilter().unmask();

                if (preferences.isFilterByRealizationForm()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getImplementationFormFilter(), item));
                }
            }
        });
    }

    void fetchImplementationLevelFilterData(Long formId) {
        Filter filter = new Filter();
        filter.setParentId(formId);

        if (preferences.isFilterByRealizationLevel()) {
            filter.setId(preferences.getRealizationLevel());
        }

        if (preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)) {
            filter.setId(null);
        }
        view.getImplementationLevelFilter().clear();
        view.getImplementationLevelFilter().mask();
        rpcService.getFilteredRealizationLevels(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список уровней реализации.").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getImplementationLevelFilter().getStore().clear();
                view.getImplementationLevelFilter().getStore().addAll(response);
                view.getImplementationLevelFilter().unmask();

                if (preferences.isFilterByRealizationLevel() && !preferences.getUserRoles().contains(UserRole.FIN_CURATOR_GCHP)) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getImplementationLevelFilter(), item));
                    view.getImplementationLevelFilter().mask();
                }
            }
        });
    }

    void fetchImplementationSphereFilterData(Long formId) {
        Filter filter = new Filter();
        filter.setParentId(formId);

        if (preferences.isFilterByRealizationSphere()) {
            filter.setId(preferences.getRealizationSphere());
        }

        view.getImplementationSphereFilter().clear();
        view.getImplementationSphereFilter().mask();
        rpcService.getFilteredRealizationSpheres(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список сфер реализации.").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getImplementationSphereFilter().getStore().clear();
                view.getImplementationSphereFilter().getStore().addAll(response);
                view.getImplementationSphereFilter().unmask();

                if (preferences.isFilterByRealizationSphere()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getImplementationSphereFilter(), item));
                    view.getImplementationSphereFilter().mask();
                }
            }
        });
    }

    void fetchImplementationSectorFilterData(Long formId, Long sphereId) {
        FilterByInitFormAndSphere filter = new FilterByInitFormAndSphere();
        filter.setFormId(formId);
        filter.setSphereId(sphereId);

        if (preferences.isFilterByRealizationSector()) {
            filter.setId(preferences.getRealizationSector());
        }

        view.getImplementationSectorFilter().clear();
        view.getImplementationSectorFilter().mask();
        rpcService.getFilteredRealizationSectors(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список отраслей реализации.").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getImplementationSectorFilter().getStore().clear();
                view.getImplementationSectorFilter().getStore().addAll(response);
                view.getImplementationSectorFilter().unmask();

                if (preferences.isFilterByRealizationSector()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getImplementationSectorFilter(), item));
                    view.getImplementationSectorFilter().mask();
                }
            }
        });
    }

    void fetchRegionFilterData() {
        RfRegionFilters filters = new RfRegionFilters();
        if (preferences.isFilterByRegion()) {
            filters.setId(preferences.getRegionId());
        }

        view.getRegionFilter().clear();
        view.getRegionFilter().mask();
        rpcService.getAllRFRegions(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить список регионов РФ.").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getRegionFilter().getStore().clear();
                view.getRegionFilter().getStore().addAll(response);
                view.getRegionFilter().unmask();

                if (preferences.isFilterByRegion()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getRegionFilter(), item));
                    view.getRegionFilter().mask();
                }

                if (preferences.isFilterByOrg()) {
                    view.getRegionFilter().mask();
                }
            }
        });
    }

    private void fetchMunicipalityFilterData(String regionId) {
        view.getPpoFilter().mask();
        RfMunicipalityFilters filters = new RfMunicipalityFilters();
        filters.setRegionId(regionId);

        if (preferences.isFilterByOktmo()) {
            filters.setOktmo(preferences.getOktmoCode());
        }

        view.getPpoFilter().clear();
        view.getPpoFilter().mask();
        rpcService.getFilteredMunicipalities(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень муниципальных образований");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getPpoFilter().getStore().clear();
                view.getPpoFilter().getStore().addAll(response);
                view.getPpoFilter().unmask();

                if (preferences.isFilterByOktmo()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getPpoFilter(), item));
                    view.getPpoFilter().mask();
                }

                if (preferences.isFilterByOrg()) {
                    view.getPpoFilter().mask();
                }
            }
        });
    }

    private void fetchPpkFilterData(String regionId, String oktmoCode) {
        view.getPpkFilter().mask();
        FilterSvrOrgs filters = new FilterSvrOrgs();
        filters.setRegion(regionId);

        if (oktmoCode != null) {
            filters.setOktmo(oktmoCode);
        }

        if (preferences.isFilterByOrg()) {
            filters.getIds().addAll(preferences.getOrgIds());
            view.getRegionFilter().mask();
            view.getPpoFilter().mask();
        }

        view.getPpkFilter().clear();
        view.getPpkFilter().mask();
        rpcService.getSvrOrgs(filters, new MethodCallback<List<SvrOrg>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень концендентов");
            }

            @Override
            public void onSuccess(Method method, List<SvrOrg> response) {
                view.getPpkFilter().getStore().clear();
                view.getPpkFilter().getStore().addAll(response);
                view.getPpkFilter().unmask();

                if (preferences.isFilterByOrg()) {
                    response.stream().findFirst().ifPresent(item -> SelectionEvent.fire(view.getPpkFilter(), item));

                    if (response.size() == 1) {
                        view.getPpkFilter().mask();
                    }
                }
            }
        });
    }

    private PagingLoader<PagingLoadConfig, PagingLoadResult<Project>> gridLoader;

    private void fetchProjectStatusDictionary() {
        view.getProjectStatusFilter().mask();
        rpcService.getAllProjectStatuses(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник статусы проектов");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectStatusFilter().getStore().addAll(response);

                if (preferences.getUserRoles().contains(UserRole.GCHP_ADMINISTRATOR)) {
                    view.getProjectStatusFilter().select("2"); // Сведения о проекте необходимо актуализоровать
                    view.getProjectStatusFilter().select("4"); // Сведения о проекте предоставлены полностью
                }
                view.getProjectStatusFilter().unmask();
                view.getPageToolBar().getPagingToolBar().first();
            }
        });
    }

    private void fetchRealizationStatusDictionary() {
        view.getRealizationStatusFilter().mask();
        rpcService.getAllRealizationStatuses(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник статусы реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getRealizationStatusFilter().getStore().addAll(response);
                view.getRealizationStatusFilter().unmask();
            }
        });
    }

    // Обертка, чтобы каждый раз не проверять на NPE.
    private String getStringValOrNull(Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            return null;
        }
    }

    private Long getLongValOrNull(Supplier<String> supplier) {
        try {
            return Long.parseLong(supplier.get());
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean checkCanEdit() {
        if (preferences.getUserRoles().contains(UserRole.GCHP_ADMINISTRATOR)) {
            return true;
        }

        if (preferences.getUserRoles().contains(UserRole.GCHP_CONTROLLER)) {
            SimpleIdNameModel region = view.getRegionFilter().getValue();
            if (region != null && region.getId().equals(preferences.getRegionId())) {
                return true;
            }

            Project project = view.getGrid().getSelectionModel().getSelectedItem();
            if (project != null && project.getGiRegion() != null && project.getGiRegion().getId().equals(preferences.getRegionId())) {
                return true;
            }
        }

        if (preferences.getUserRoles().contains(UserRole.GCHP_USER)) {
            SvrOrg ppk = view.getPpkFilter().getValue();
            if (ppk != null && preferences.getOrgIds().contains(ppk.getId())) {
                return true;
            }

            Project project = view.getGrid().getSelectionModel().getSelectedItem();
            return project != null && project.getGiPublicPartner() != null && preferences.getOrgIds().contains(project.getGiPublicPartner().getId());
        }

        return false;
    }

    private void doView() {
        Project selectedDocument = view.getGrid().getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            new MessageBox("Документ не выбран", "Выберите документ для просмотра").show();
        } else {
            handlerManager.fireEvent(new ViewDocumentEvent(selectedDocument));
        }
    }

    public GchpFilterPanelPresenter(RestService rpcService, HandlerManager handlerManager, Display view, RolePreferences preferences) {
        this.rpcService = (GchpApi) rpcService;
        this.handlerManager = handlerManager;
        this.view = view;
        this.preferences = preferences;
    }
}
