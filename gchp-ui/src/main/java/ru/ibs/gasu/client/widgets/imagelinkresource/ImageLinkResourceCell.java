package ru.ibs.gasu.client.widgets.imagelinkresource;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.function.Function;

public class ImageLinkResourceCell extends ImageResourceCell {
    private static ImageLinkResourceRenderer renderer;
    private final Function<String, String> tooltipFunc;

    public ImageLinkResourceCell(int imageWidth, int imageHeight, Function<String, String> tooltipFunc) {
        super();
        this.tooltipFunc = tooltipFunc;
        if (renderer == null) {
            renderer = new ImageLinkResourceRenderer(imageWidth, imageHeight);
        }

    }

    public void render(Cell.Context context, ImageResource value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendHtmlConstant("<div title=\"" + tooltipFunc.apply(value.getName()) + "\">");
            sb.append(renderer.render(value));
            sb.appendHtmlConstant("</div>");
        }

    }
}
