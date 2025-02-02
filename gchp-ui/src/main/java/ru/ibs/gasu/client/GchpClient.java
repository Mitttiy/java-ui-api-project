package ru.ibs.gasu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RolePreferences;

public class GchpClient implements EntryPoint {

    @Override
    public void onModuleLoad() {
        RootPanel.get("pageLoader").setVisible(false);
        RootPanel mainContainer = RootPanel.get("mainContent");

        GchpApi gchpApi = GWT.create(GchpApi.class);

        gchpApi.getLodRolePreferences(new MethodCallback<RolePreferences>() {
            @Override
            public void onFailure(Method method, Throwable caught) {
                mainContainer.add(loadError("При определении организации пользователя произошла ошибка!"));
            }

            @Override
            public void onSuccess(Method method, RolePreferences preferences) {
                if (!preferences.isAccess())
                    mainContainer.add(loadError(preferences.getAccessErr()));
                else {
                    HandlerManager eventBus = new HandlerManager(null);
                    AppController appController = new AppController(gchpApi, eventBus, preferences);
                    appController.go(mainContainer);
                }
            }
        });
    }

    private HTML loadError(String errText) {
        SafeHtml html = SafeHtmlUtils.fromSafeConstant("<div class=\"block-page-title\">" +
                "<div class=\"content container\">" +
                "<h1 id=\"page-title\">" +
                errText +
                "</h1>" +
                "</div>" +
                "</div>");
        HTML error = new HTML(html);
        error.getElement().getStyle().setLineHeight(2, Style.Unit.EM);
        error.getElement().getStyle().setFontSize(8, Style.Unit.PT);
        return error;
    }
}
