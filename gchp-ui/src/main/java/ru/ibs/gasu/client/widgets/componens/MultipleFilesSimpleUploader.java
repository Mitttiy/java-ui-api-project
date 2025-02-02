package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;


/**
 * Простой загрузчик с возможностью загружать несколько файлов<br/> Сейчас жестко зашит автоматический submit()!!.
 * {@code}
 * <pre>
 *     MultipleFilesSimpleUploader uploader = new MultipleFilesSimpleUploader("api/upload");
 *     Button b = new Button("Submit");
 *     b.addClickHandler(e -> uploader.submit());
 * </pre>
 */
public class MultipleFilesSimpleUploader implements IsWidget {

    interface Styles extends ClientBundle {
        @Source("MultipleFilesSimpleUploaderStyle.gss")
        MultipleFilesSimpleUploaderStyle style();
    }

    interface MultipleFilesSimpleUploaderStyle extends CssResource {
        @ClassName("container")
        String container();

        @ClassName("file-input")
        String fileInput();

        @ClassName("file-ctn")
        String fileCtn();

        @ClassName("file-name")
        String fileName();
    }

    interface Template extends XTemplates {
        @XTemplate("<div class=\"{style.container}\">\n" +
                "  <form id=\"form-{id}\" enctype=\"multipart/form-data\">\n" +
                "    <input class=\"{style.fileInput}\" id=\"input-{id}\" type=\"file\" name=\"file\" multiple=\"multiple\" />\n" +
                "    <label for=\"input-{id}\">\n" +
                "      <span class=\"{style.fileCtn}\">\n" +
                "        <span style=\"margin-right: 5px\"><i class=\"fas fa-upload\"></i></span>\n" +
                "        <span>Приложить файлы</span>\n" +
                "      </span>\n" +
                "    </label>\n" +
                "  </form>\n" +
                "</div>")
        SafeHtml createForm(String id, MultipleFilesSimpleUploaderStyle style);
    }

    private Template domTemplates = GWT.create(Template.class);
    private Styles resources = GWT.create(Styles.class);
    private String fileNameStyle;
    private VerticalLayoutContainer container;
    private HTML formHolder;
    private String uploadUrl = "";

    public MultipleFilesSimpleUploader(String uploadUrl) {
        this();
        this.uploadUrl = uploadUrl;
    }

    public MultipleFilesSimpleUploader() {
        StyleInjectorHelper.ensureInjected(resources.style(), true);
        fileNameStyle = resources.style().fileName();
        initWidget();
        this.asWidget().addAttachHandler(event -> {
            MultipleFilesSimpleUploader.this.addEventListeners();
        });
    }

    /**
     * Вызывается при ответе 2xx. <br>
     * По-умолчанию выводит окно с текстом ответа.
     *
     * @param response ответ сервера
     */
    public void onSuccess(String response) {
        new AlertMessageBox("Response", response);
    }

    /**
     * Вызывается при ответе, отличном от 2xx. <br>
     * По-умолчанию выводит окно с кодом и текстом ответа.
     *
     * @param code     код ответа
     * @param response текст ответа
     */
    public void onError(String code, String response) {
        new AlertMessageBox(
                "Ошибка",
                "Возникла ошибка при обработке файла. Код " + code + ". " + response)
                .show();
        this.clearInput();
    }

    /**
     * Есть ли выбранные файлы.
     *
     * @return true/false
     */
    public native boolean hasFiles() /*-{
        var fileInput = $doc.getElementById("input-" +
            this.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::hashCode()());
        return fileInput.files.length > 0;
    }-*/;

    /**
     * Установить url для загрузки.
     *
     * @param uploadUrl url для загрузки.
     */
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    /**
     * Отправить выбранные файлы по установленному url.
     */
    public native void submit() /*-{
        var that = this;
        var xhr = new XMLHttpRequest();
        xhr.open("POST", that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::uploadUrl);
        xhr.onload = function (event) {
            if (Math.floor(this.status / 100) === 2) {
                that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::onSuccess(Ljava/lang/String;)(event.target.response);
            } else {
                that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::onError(*)("" + this.status, event.target.response);
            }
        };
        var formData = new FormData($doc.getElementById("form-" + that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::hashCode()()));
        xhr.send(formData);
    }-*/;

    @Override
    public Widget asWidget() {
        return container;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        formHolder = new HTML(domTemplates.createForm(String.valueOf(this.hashCode()), resources.style()));
        container.add(formHolder);
    }

    public native void clearInput() /*-{
        var fileInput = $doc.getElementById("input-" + this.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::hashCode()());
        fileInput.value = "";
    }-*/;

    private native void addEventListeners() /*-{
        var that = this;
        var fileInput = $doc.getElementById("input-" + this.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::hashCode()());
        var qs = "#form-" +
            this.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::hashCode()() + " ." +
            this.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::fileNameStyle;
        var fileName = $doc.querySelector(qs);
        fileInput.onchange = function () {
            if (fileInput.files.length === 1) {
                that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::submit()();
            } else if (fileInput.files.length > 1) {
                that.@ru.ibs.gasu.client.widgets.componens.MultipleFilesSimpleUploader::submit()();
            } else {
            }
        };
    }-*/;
}
