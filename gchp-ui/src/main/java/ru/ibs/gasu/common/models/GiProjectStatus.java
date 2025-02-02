package ru.ibs.gasu.common.models;

public enum GiProjectStatus {
    DRAFT(1L),
    NEED_ACTUALIZE(2L),
    NEED_REWORK(3L),
    COMPLETE(4L);

    private Long id;

    GiProjectStatus(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
