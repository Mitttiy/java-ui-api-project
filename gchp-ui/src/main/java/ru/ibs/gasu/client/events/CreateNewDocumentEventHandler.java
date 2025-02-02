package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface CreateNewDocumentEventHandler extends EventHandler {
    void onCreateNewDocument(CreateNewDocumentEvent event);
}
