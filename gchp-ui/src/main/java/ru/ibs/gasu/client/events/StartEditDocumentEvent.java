package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.GwtEvent;
import ru.ibs.gasu.common.models.Project;

public class StartEditDocumentEvent extends GwtEvent<StartEditDocumentEventHandler> {
    public static Type<StartEditDocumentEventHandler> TYPE = new Type<>();

    private final Project document;

    public StartEditDocumentEvent(Project document) {
        this.document = document;
    }

    public Project getDocument() {
        return document;
    }

    @Override
    public Type<StartEditDocumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StartEditDocumentEventHandler handler) {
        handler.onStartEditDocument(this);
    }
}
