package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.GwtEvent;
import ru.ibs.gasu.common.models.Project;

public class ViewDocumentEvent extends GwtEvent<ViewDocumentEventHandler> {
    public static Type<ViewDocumentEventHandler> TYPE = new Type<>();

    private final Project document;

    public ViewDocumentEvent(Project document) {
        this.document = document;
    }

    public Project getDocument() {
        return document;
    }

    @Override
    public Type<ViewDocumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ViewDocumentEventHandler handler) {
        handler.onViewDocument(this);
    }
}
