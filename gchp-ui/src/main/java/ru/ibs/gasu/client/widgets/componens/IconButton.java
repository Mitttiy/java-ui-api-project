package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.button.TextButton;

public class IconButton extends TextButton {

    interface Templates extends XTemplates {
        @XTemplate("<i class='fa {icon}' aria-hidden='true'></i> {text}")
        SafeHtml button(String text, String icon);

        @XTemplate("<i class='fa {icon} {size}' aria-hidden='true'></i> {text}")
        SafeHtml buttonWithSize(SafeHtml text, String icon, String size);
    }

    private Templates templates;

    public IconButton(String text, String icon) {
        super();
        templates = GWT.create(Templates.class);
        getCell().setHTML(templates.button(text, icon));
    }

    public IconButton(String text, String icon, int size) {
        super();
        if (size < 1 && size > 6) throw new RuntimeException("Size must be between 1 and 6");
        templates = GWT.create(Templates.class);
        String space = "&nbsp;";
        for (int i = 0; i < size; i++) {
            space += "&nbsp;";
        }
        getCell().setHTML(templates.buttonWithSize(new SafeHtmlBuilder().appendHtmlConstant(space).appendEscaped(text).toSafeHtml(), icon, size == 1 ? "fa-lg" : "fa-" + size + "x"));
    }

    public void setText(String text, String icon) {
        getCell().setHTML(templates.button(text, icon));
        redraw();
    }


}
