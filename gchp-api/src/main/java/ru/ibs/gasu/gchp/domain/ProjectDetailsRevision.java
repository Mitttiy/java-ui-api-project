package ru.ibs.gasu.gchp.domain;

import ru.ibs.gasu.gchp.entities.files.ProjectFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDetailsRevision implements Comparable<ProjectDetailsRevision> {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private Date date;
    private String editor;
    private String editedSection;
    private String editedSubSection;
    private String editedField;
    private String prevValue;
    private String currentValue;
    private int key;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = nonNullStr(editor);
    }

    public String getEditedSection() {
        return editedSection;
    }

    public void setEditedSection(String editedSection) {
        this.editedSection = nonNullStr(editedSection);
    }

    public String getEditedField() {
        return editedField;
    }

    public void setEditedField(String editedField) {
        this.editedField = nonNullStr(editedField);
    }

    public void setEditedField(ProjectField editedField) {
        this.editedField = editedField.getName();
        setEditedSection(editedField.getSectionName());
        setEditedSubSection(editedField.getSubSectionName());
    }

    public String getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(String prevValue) {
        this.prevValue = nonNullStr(prevValue);
    }

    public void setPrevValue(Boolean prevValue) {
        this.prevValue = yesNo(prevValue);
    }

    public <X extends Number> void setPrevValue(X prevValue) {
        this.prevValue = number(prevValue);
    }

    public void setPrevValue(Date prevValue) {
        this.prevValue = date(prevValue);
    }

    public void setPrevValue(List<? extends ProjectFile> currentValue) {
        this.prevValue = files(currentValue);
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = nonNullStr(currentValue);
    }

    public void setCurrentValue(Boolean currentValue) {
        this.currentValue = yesNo(currentValue);
    }

    public <X extends Number> void setCurrentValue(X currentValue) {
        this.currentValue = number(currentValue);
    }

    public void setCurrentValue(Date currentValue) {
        this.currentValue = date(currentValue);
    }

    public void setCurrentValue(List<? extends ProjectFile> currentValue) {
        this.currentValue = files(currentValue);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getEditedSubSection() {
        return editedSubSection;
    }

    public void setEditedSubSection(String editedSubSection) {
        this.editedSubSection = editedSubSection;
    }

    private String files(List<? extends ProjectFile> files) {
        if (files != null) {
            return files.stream().map(ProjectFile::getFileName).collect(Collectors.joining(", "));
        }
        return "";
    }

    private String date(Date value) {
        return (value == null) ? "" : SIMPLE_DATE_FORMAT.format(value);
    }

    private String number(Number value) {
        return (value == null) ? "" : String.valueOf(value);
    }

    private String yesNo(Boolean value) {
        return (value == null) ? "" : value ? "Да" : "Нет";
    }

    private String nonNullStr(String value) {
        return (value == null) ? "" : value;
    }

    @Override
    public int compareTo(ProjectDetailsRevision o) {
        if (getDate() == null && o.getDate() == null) {
            return 0;
        }

        if (getDate() == null && o.getDate() != null) {
            return -1;
        }

        if (o.getDate() == null) {
            return 1;
        }

        return getDate().compareTo(o.getDate());
    }
}
