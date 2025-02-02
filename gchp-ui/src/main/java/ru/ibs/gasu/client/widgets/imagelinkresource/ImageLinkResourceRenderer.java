package ru.ibs.gasu.client.widgets.imagelinkresource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ImageLinkResourceRenderer {
    private static final ImageLinkResourceRenderer.Template TEMPLATE = GWT.create(ImageLinkResourceRenderer.Template.class);
    private int imageWidth;
    private int imageHeight;

    public ImageLinkResourceRenderer(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public SafeHtml render(ImageResource image) {
        return image instanceof ImageResourcePrototype.Bundle ? AbstractImagePrototype.create(image).getSafeHtml() : TEMPLATE.image(image.getSafeUri(), /*image.getWidth()*/imageWidth, /*image.getHeight()*/imageHeight);
    }

    interface Template extends SafeHtmlTemplates {
        @com.google.gwt.safehtml.client.SafeHtmlTemplates.Template("<div style=\"width: 50%; margin: 0 auto;\"><img style=\"cursor: pointer;\" src='{0}' border='0' width='{1}' height='{2}'></div>")
        SafeHtml image(SafeUri var1, int var2, int var3);
    }

}
