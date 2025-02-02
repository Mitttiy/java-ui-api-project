package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class DocumentSuccessfullySavedEvent extends GwtEvent<DocumentSuccessfullySavedEventHandler> {
    public static Type<DocumentSuccessfullySavedEventHandler> TYPE = new Type<>();

    @Override
    public Type<DocumentSuccessfullySavedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DocumentSuccessfullySavedEventHandler handler) {
        handler.onDocumentSuccessfullySaved(this);
    }
}
