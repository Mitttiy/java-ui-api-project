package ru.ibs.gasu.common.models;

import java.util.Objects;

public class SimpleIdNameModel {
    private String id;
    private String name;

    public SimpleIdNameModel() {
    }

    public SimpleIdNameModel(String id) {
        this.id = id;
    }

    public SimpleIdNameModel(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleIdNameModel that = (SimpleIdNameModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
