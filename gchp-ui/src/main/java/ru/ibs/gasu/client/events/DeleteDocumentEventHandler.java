package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface DeleteDocumentEventHandler extends EventHandler {
    void onDeleteDocument(DeleteDocumentEvent event);
}
