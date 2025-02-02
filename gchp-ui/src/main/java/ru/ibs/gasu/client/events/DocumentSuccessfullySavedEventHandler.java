package ru.ibs.gasu.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentSuccessfullySavedEventHandler extends EventHandler {
    void onDocumentSuccessfullySaved(DocumentSuccessfullySavedEvent event);
}
