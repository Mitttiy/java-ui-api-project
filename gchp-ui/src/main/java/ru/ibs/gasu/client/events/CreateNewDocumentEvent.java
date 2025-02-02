package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class CreateNewDocumentEvent extends GwtEvent<CreateNewDocumentEventHandler> {
    public static Type<CreateNewDocumentEventHandler> TYPE = new Type<>();

    @Override
    public Type<CreateNewDocumentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateNewDocumentEventHandler handler) {
        handler.onCreateNewDocument(this);
    }
}
