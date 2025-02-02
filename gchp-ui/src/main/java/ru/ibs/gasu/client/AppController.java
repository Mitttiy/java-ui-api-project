package ru.ibs.gasu.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.client.events.*;
import ru.ibs.gasu.client.presenters.GchpFilterPanelPresenter;
import ru.ibs.gasu.client.presenters.GchpNewDocumentPanelPresenter;
import ru.ibs.gasu.client.presenters.Presenter;
import ru.ibs.gasu.client.widgets.NewProjectView;
import ru.ibs.gasu.client.widgets.ProjectsFiltersView;
import ru.ibs.gasu.common.models.GiProjectStatus;
import ru.ibs.gasu.common.models.Project;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RolePreferences;

import static ru.ibs.gasu.client.utils.ClientUtils.getProjectStatus;

public class AppController implements Presenter {
    private final HandlerManager eventBus;
    private final RestService rpcService;
    private HasWidgets container;
    private RolePreferences preferences;

    public AppController(RestService rpcService, HandlerManager eventBus, RolePreferences preferences) {
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.preferences = preferences;
        eventBus.addHandler(CreateNewDocumentEvent.TYPE, new CreateNewDocumentEventHandler() {
            @Override
            public void onCreateNewDocument(CreateNewDocumentEvent event) {
                NewProjectView newProjectView = new NewProjectView();
                GchpNewDocumentPanelPresenter presenter = new GchpNewDocumentPanelPresenter(rpcService, eventBus, newProjectView, preferences);
                Project managedProject = new Project();
                managedProject.setGiProjectStatus(getProjectStatus(GiProjectStatus.DRAFT));
                newProjectView.setManagedProject(managedProject);
                presenter.setManagedProject(managedProject);
                presenter.go(container);
            }
        });

        eventBus.addHandler(StartEditDocumentEvent.TYPE, new StartEditDocumentEventHandler() {
            @Override
            public void onStartEditDocument(StartEditDocumentEvent event) {
                NewProjectView newProjectView = new NewProjectView();
                GchpNewDocumentPanelPresenter presenter = new GchpNewDocumentPanelPresenter(rpcService, eventBus, newProjectView, preferences);
                newProjectView.setManagedProject(event.getDocument());
                presenter.setManagedProject(event.getDocument());
                presenter.go(container);
            }
        });

        eventBus.addHandler(ViewDocumentEvent.TYPE, event -> {
            NewProjectView newProjectView = new NewProjectView();
            GchpNewDocumentPanelPresenter presenter = new GchpNewDocumentPanelPresenter(rpcService, eventBus, newProjectView, preferences);
            newProjectView.setManagedProject(event.getDocument());
            presenter.setManagedProject(event.getDocument(), true);
            presenter.go(container);
        });
    }

    @Override
    public void go(HasWidgets container) {
        this.container = container;
        Presenter presenter = new GchpFilterPanelPresenter(rpcService, eventBus, new ProjectsFiltersView(), preferences);
        presenter.go(container);
    }
}
