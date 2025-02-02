package ru.ibs.gasu.client.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class ContentPanelWithHint extends ContentPanel {

    public ContentPanelWithHint(ContentPanelAppearance appearance) {
        super(appearance);
    }

    public void setHeading(SafeHtml html) {
        super.setHeading(html);
    }
}
