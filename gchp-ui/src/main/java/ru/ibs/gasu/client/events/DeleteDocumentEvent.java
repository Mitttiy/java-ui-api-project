package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.GwtEvent;
import ru.ibs.gasu.common.models.Project;

public class DeleteDocumentEvent extends GwtEvent<DeleteDocumentEventHandler> {
    public static Type<DeleteDocumentEventHandler> TYPE = new Type<>();

    private final Project document;

    public DeleteDocumentEvent(Project document) {
        this.document = document;
    }

    public Project getDocument() {
        return document;
    }

    @Override
    public Type<DeleteDocumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeleteDocumentEventHandler handler) {
        handler.onDeleteDocument(this);
    }
}
