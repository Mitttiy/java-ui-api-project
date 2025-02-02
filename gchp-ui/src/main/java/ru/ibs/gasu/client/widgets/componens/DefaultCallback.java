package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class DefaultCallback<T> implements MethodCallback<T> {

    private AsyncCallback<T> callback;

    public DefaultCallback() {
    }

    public DefaultCallback(AsyncCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onFailure(Method method, Throwable exception) {

        new AlertMessageBox("Ошибка", "Не удалось загрузить справочник единиц измерения");
        if (callback != null) {
            callback.onFailure(exception);
        }
    }

    @Override
    public void onSuccess(Method method, T response) {

        if (callback != null) {
            callback.onSuccess(response);
        }
    }
}