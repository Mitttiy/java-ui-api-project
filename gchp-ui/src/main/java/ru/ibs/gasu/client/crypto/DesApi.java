package ru.ibs.gasu.client.crypto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

import java.util.ArrayList;

public class DesApi {

    public interface JsResources extends ClientBundle {
        final JsResources INSTANCE = GWT.create(JsResources.class);

        @Source("ie_eventlistner_polyfill.js")
        TextResource ieEventListener();

        @Source("es6-promise.min.js")
        TextResource esPromise();

        @Source("cadesplugin_api.js")
        TextResource cadesplugin();

        @Source("Code.js")
        TextResource code();

        @Source("async_code.js")
        TextResource asyncCode();
    }

    private static String signedData;
    private static String dataForSign;
    private static String certData;

    private boolean isInited = false;

    public interface DesApiListener {
        void onInitCompleteSuccess(ArrayList<String> certNameList, ArrayList<String> thumbprintList, ArrayList<String> certMetaList);

        void onInitCompleteFailure(String message);

        void onSignCompleteSuccess(String signedText);

        void onSignCompleteFailure(String message);
    }

    private static CertMetaDataEncoder certMetaDataEncoder = GWT.create(CertMetaDataEncoder.class);

    private static DesApiListener eventListener;

    public DesApi(DesApiListener listener) {

        signedData = null;
        dataForSign = null;
        certData = null;

        eventListener = listener;

        if (!isInited) {

            ScriptInjector.fromString(JsResources.INSTANCE.ieEventListener().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
            ScriptInjector.fromString(JsResources.INSTANCE.esPromise().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();

            ScriptInjector.fromString(JsResources.INSTANCE.cadesplugin().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
            ScriptInjector.fromString(JsResources.INSTANCE.code().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();

            exportMyFunction();

            isInited = true;
        }

        _checkPlugin();

    }

    public static CertMetaData getCertificateMetaData(String certMetaData) {
        return certMetaDataEncoder.decode(JSONParser.parseStrict(certMetaData));
    }

    public static String getSignedData() {
        return signedData;
    }

    public static String getDataForSign() {
        return dataForSign;
    }

    public static String getCertData() {
        return certData;
    }

    public void signData(String thumbprint, String data, String setDisplayData, String certMetaData) {

        dataForSign = data;
        certData = certMetaData;

        _signData(thumbprint, data, setDisplayData);
    }

    public static native void exportMyFunction()/*-{
        $wnd.handleCheckPlugin = @ru.ibs.gasu.client.crypto.DesApi::handleCheckPlugin(*);
        $wnd.alertMessage = @ru.ibs.gasu.client.crypto.DesApi::alertMessage(*);
        $wnd.callback.loadAsyncCode = @ru.ibs.gasu.client.crypto.DesApi::handleLoadAsyncCode(*);
        $wnd.callback.initCompleteSuccess = @ru.ibs.gasu.client.crypto.DesApi::handleInitCompleteSuccess(*);
        $wnd.callback.initCompleteFailure = @ru.ibs.gasu.client.crypto.DesApi::handleInitCompleteFailure(*);
        $wnd.callback.signCompleteSuccess = @ru.ibs.gasu.client.crypto.DesApi::handleSignCompleteSuccess(*);
        $wnd.callback.signCompleteFailure = @ru.ibs.gasu.client.crypto.DesApi::handleSignCompleteFailure(*);
    }-*/;

    public static void alertMessage(String message) {
    }

    public static void handleLoadAsyncCode() {

        ScriptInjector.fromString(JsResources.INSTANCE.asyncCode().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();

    }

    public static void handleInitCompleteSuccess(JsArrayString jsCertNameList, JsArrayString jsThumbprint, JsArrayString jsCertMetaList) {

        ArrayList<String> certNameList = new ArrayList<String>();
        for (int i = 0; i < jsCertNameList.length(); i++) {
            certNameList.add(jsCertNameList.get(i));
        }

        ArrayList<String> thumbprintList = new ArrayList<String>();
        for (int i = 0; i < jsThumbprint.length(); i++) {
            thumbprintList.add(jsThumbprint.get(i));
        }

        ArrayList<String> certMetaList = new ArrayList<String>();
        for (int i = 0; i < jsCertMetaList.length(); i++) {
            certMetaList.add(jsCertMetaList.get(i));
        }

        if (eventListener != null) {
            eventListener.onInitCompleteSuccess(certNameList, thumbprintList, certMetaList);
        }
    }

    public static void handleInitCompleteFailure(String message) {

        if (eventListener != null) {
            eventListener.onInitCompleteFailure(message);
        }
    }

    public static void handleSignCompleteSuccess(String signedText) {

        signedData = signedText;

        if (eventListener != null) {
            eventListener.onSignCompleteSuccess(signedText);
        }
    }

    public static void handleSignCompleteFailure(String message) {

        if (eventListener != null) {
            eventListener.onSignCompleteFailure(message);
        }
    }

    public static void handleCheckPlugin(boolean isPluginPresent) {

        if (isPluginPresent) {

        } else {
            alertMessage("Для продолжения работы требуется установка плагина. Обратитесь в службу технической поддержки");
        }
    }

    private static native void _checkPlugin() /*-{

        var canPromise = !!$wnd.Promise;

        if (canPromise) {

            $wnd.cadesplugin.then(function () {
                    $wnd.Common_CheckForPlugIn();
                },
                function (error) {
                    $wnd.callback.initCompleteFailure("Плагин не установлен");
                }
            );
        } else {

            $wnd.addEventListener("message", function (event) {
                    if (event.data == "cadesplugin_loaded") {
                        //$wnd.CheckForPlugIn_NPAPI();
                        $wnd.Common_CheckForPlugIn();
                    } else if (event.data == "cadesplugin_load_error") {
                        $wnd.callback.initCompleteFailure("Плагин не установлен");
                    }
                },
                false);
            $wnd.postMessage("cadesplugin_echo_request", "*");
        }
    }-*/;

    private static native void _signData(String thumbprint, String text, String setDisplayData) /*-{

        if (setDisplayData != null) {
            signedData = $wnd.Common_SignCadesBES(thumbprint, text, setDisplayData);
        } else {
            signedData = $wnd.Common_SignCadesBES(thumbprint, text);
        }

    }-*/;

}
