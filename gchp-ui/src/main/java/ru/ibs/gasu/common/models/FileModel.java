package ru.ibs.gasu.common.models;

public class FileModel {
    private String name;
    private Long id;

    public FileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
