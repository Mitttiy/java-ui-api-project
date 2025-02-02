package ru.ibs.gasu.common.models.view_attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAttrState {

    private List<String> columns = new ArrayList<>();
    private Map<String, List<Integer>> state = new HashMap<>();

    public List<String> getColumns() {
        return columns;
    }

    public Map<String, List<Integer>> getState() {
        return state;
    }

    public boolean isAttrActive(String formId, String attrName) {
        if (formId == null) {
            // Show all if formId is null - for new project formId is empty
            return true;
        }

        if (attrName == null) {
            throw new RuntimeException("attrName is not specified.");
        }

        if (columns == null) {
            throw new RuntimeException("columns is empty.");
        }

        if (state == null) {
            throw new RuntimeException("state is empty.");
        }

        // Find and check column
        int columnIndex = columns.indexOf(formId);
        if (columnIndex == -1) {
            throw new RuntimeException("Column not found by formId = " + formId);
        }

        int columnsCount = columns.size();

        // Find and check attr
        if (!state.containsKey(attrName)) {
            throw new RuntimeException("Attribute not found by formId = " + formId + ", attrName = " + attrName);
        }

        List<Integer> indexes = state.get(attrName);
        if (indexes == null || indexes.size() != columnsCount) {
            throw new RuntimeException("Incorrect state - count columns != count state of attribute. / "
                    + (indexes != null ? indexes.toString() : null)
                    + " / "
                    + columns.toString()

            );
        }

        Integer currentState = indexes.get(columnIndex);

        return currentState != null && currentState == 1;
    }
}