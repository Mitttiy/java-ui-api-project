package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;

/**
 * {@link HTML} виджет, содержащий в себе стилизованную надпись "НАЙДЕНО ДОКУМЕНТОВ: 0".
 */
public class DocumentCounter implements IsWidget {

    private HTML documentCounter;
    private Templates templates = GWT.create(Templates.class);

    @Override
    public Widget asWidget() {
        initWidget();
        return documentCounter;
    }

    public void updateDocumentsCount(Integer count) {
        documentCounter.setHTML(templates.header(count));
    }

    interface Templates extends XTemplates {
        @XTemplate("<div class='view-count'><span>НАЙДЕНО ДОКУМЕНТОВ: {count}</span></div>")
        SafeHtml header(Integer count);
    }

    private void initWidget() {
        StyleInjector.inject(
                ".view-count {" +
                        "  margin: 15px 0;" +
                        "  text-align: center;" +
                        "  overflow: hidden;" +
                        "  position: relative;" +
                        "}" +
                        ".view-count:before {" +
                        "  content: '';" +
                        "  border-top: 1px solid #dddddd;" +
                        "  width: 100%;" +
                        "  top: 50%;" +
                        "  z-index: 1;" +
                        "  position: absolute;" +
                        "  left: 0;" +
                        "}" +
                        ".view-count > span {" +
                        "  display: inline-block;" +
                        "  height: 25px;" +
                        "  line-height: 26px;" +
                        "  color: #fff;" +
                        "  text-transform: uppercase;" +
                        "  padding: 0  25px;" +
                        "  -moz-border-radius: 16px;" +
                        "  -webkit-border-radius: 16px;" +
                        "  border-radius: 16px;" +
                        "  font-size: 12px;" +
                        "  background: #797979;" +
                        "  position: relative;" +
                        "  z-index: 2;" +
                        "}");
        documentCounter = new HTML(templates.header(0));
        documentCounter.getElement().setAttribute("style", "z-index: -99999");
    }
}
