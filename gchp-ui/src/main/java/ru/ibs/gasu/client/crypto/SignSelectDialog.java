package ru.ibs.gasu.client.crypto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import ru.ibs.gasu.client.widgets.componens.IconButton;

import java.util.ArrayList;

/**
 * Created by abogomolov on 18.07.2017.
 */
public class SignSelectDialog extends Dialog {

    interface Templates extends XTemplates {
        @XTemplate(source = "SignSelectDialogListViewRow.html")
        SafeHtml renderRow(SignSelectDialogListViewRow style, String name, String create);
    }

    interface Resource extends ClientBundle {
        @Source("SignSelectDialogListViewRow.gss")
        SignSelectDialogListViewRow style();
    }

    private Templates templates = GWT.create(Templates.class);
    private Resource resources = GWT.create(Resource.class);

    private VerticalLayoutContainer fieldsContainer = new VerticalLayoutContainer();

    public SignSelectDialog(ArrayList<String> certNameList, ArrayList<String> thumbprintList, ArrayList<String> certMetaList) {

        StyleInjectorHelper.ensureInjected(resources.style(), true);

        setHeading("Выбор сертификата ключа проверки электронной подписи");
        setPredefinedButtons();

        ListStore<String> store = new ListStore<>(new ModelKeyProvider<String>() {
            @Override
            public String getKey(String s) {
                return s;
            }
        });
/*
        certNameList.add("CN=2 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=3 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=4 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=5 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=6 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=7 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
        certNameList.add("CN=8 Чернов Сергей Александрович; Выдан: 15.09.2016 08:14:10");
*/
        store.addAll(certNameList);

        ListViewCustomAppearance<String> appearance = new ListViewCustomAppearance<String>("." + resources.style().itemWrap(), resources.style().itemOver(), resources.style().itemSelected()) {

            @Override
            public void renderEnd(SafeHtmlBuilder builder) {

                String markup = new StringBuilder("<div class=\"").append(CommonStyles.get().clear()).append("\"></div>").toString();
                builder.appendHtmlConstant(markup);
            }

            @Override
            public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {

                builder.appendHtmlConstant("<div class='" + resources.style().itemWrap() + "'>");
                builder.append(content);
                builder.appendHtmlConstant("</div>");
            }
        };

        ListView<String, String> signListView = new ListView<String, String>(store, new ValueProvider<String, String>() {
            @Override
            public String getValue(String s) {

                return s;
            }

            @Override
            public void setValue(String s, String s2) {

            }

            @Override
            public String getPath() {
                return null;
            }
        }, appearance);
        signListView.setHeight("160px");
        signListView.setCell(new SimpleSafeHtmlCell<String>(new AbstractSafeHtmlRenderer<String>() {
            @Override
            public SafeHtml render(String s) {

                String name = s;
                String created = "";

                String[] items = s.split(";");

                if (items.length == 2) {
                    name = items[0].replace("CN=", "");

                    created = items[1];

                    String[] dateItems = items[1].split(" ");

                    if (dateItems.length == 4) {
                        created = dateItems[2] + " " + dateItems[3];
                    } else {
                        created = items[1];
                    }

                }
                return templates.renderRow(resources.style(), name, created);
            }

        }));
        signListView.setBorders(false);

        signListView.addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
            }
        }, DoubleClickEvent.getType());

        fieldsContainer.add(signListView, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

        add(fieldsContainer, new MarginData(10, 10, 10, 10));


        IconButton signButton = new IconButton("Подписать", "fa-pencil-square-o");
        signButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                final String selectedItem = signListView.getSelectionModel().getSelectedItem();

                int index = certNameList.indexOf(selectedItem);

                if (selectedItem != null && index != -1) {
                    onCertThumbprintSelect(selectedItem, thumbprintList.get(index), certMetaList.get(index));
                }
            }
        });
        addButton(signButton);

        IconButton cancelButton = new IconButton("Отмена", "fa-times");
        cancelButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                onCertCancelSelect();
                SignSelectDialog.this.hide();
            }
        });

        addButton(cancelButton);

    }

    public void onCertThumbprintSelect(String certName, String thumbprint, String certMeta) {

    }

    public void onCertCancelSelect() {

    }
}
