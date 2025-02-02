package ru.ibs.gasu.dictionaries.domain;

public class FilterByInitFormAndMethod {
    private Long formId;
    private Long initMethodId;

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getInitMethodId() {
        return initMethodId;
    }

    public void setInitMethodId(Long initMethodId) {
        this.initMethodId = initMethodId;
    }
}
