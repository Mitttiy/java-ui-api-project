package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Кнопки в стиле "toolbar"
 */
public class ToolbarButton extends TextButton {

    private static final int DEFAULT_SIZE = 2;
    public static final int DEFAULT_WIDTH = 40;
    public static final int DEFAULT_HEIGHT = 40;

    interface Templates extends XTemplates {
        @XTemplate("<i class='fa {icon} {size}' title='{title}' aria-hidden='true'></i>")
        SafeHtml button(String title, String icon, String size);
    }

    private ToolbarButton.Templates templates;

    public ToolbarButton(String title, String icon) {
        this(title, icon, DEFAULT_SIZE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public ToolbarButton(String title, String icon, int size) {
        this(title, icon, size, null, null);
    }

    public ToolbarButton(String title, String icon, Integer width, Integer height) {
        this(title, icon, DEFAULT_SIZE, width, height);
    }

    public ToolbarButton(String title, String icon, int size, Integer width, Integer height) {
        super();

        if (size < 1 && size > 6) {
            throw new RuntimeException("Size must be between 1 and 6");
        }

        templates = GWT.create(ToolbarButton.Templates.class);

        String buttonSize = (size == 1) ? "fa-lg" : "fa-" + size + "x";

        getCell().setHTML(templates.button(title, icon, buttonSize));

        if (width != null) {
            this.setWidth(width);
        }

        if (height != null) {
            this.setHeight(height);
        }
    }
}
