package ru.ibs.gasu.gchp.domain;

import java.util.ArrayList;
import java.util.List;

public class SortInfo {

    private List<SortField> fields = new ArrayList<>();

    public SortInfo() {
    }

    public SortInfo(String field, Dir direction) {
        set(field, direction);
    }

    public void set(String field, Dir direction) {
        fields.add(new SortField(field, direction));
    }

    public List<SortField> getFields() {
        return fields;
    }

    public void setFields(List<SortField> fields) {
        this.fields = fields;
    }

    public enum Dir {ASC, DESC}

    public static class SortField {
        private String field;
        private Dir direction;

        public SortField() {
        }

        public SortField(String field, Dir direction) {
            this.field = field;
            this.direction = direction;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Dir getDirection() {
            return direction;
        }

        public void setDirection(Dir direction) {
            this.direction = direction;
        }
    }

}
