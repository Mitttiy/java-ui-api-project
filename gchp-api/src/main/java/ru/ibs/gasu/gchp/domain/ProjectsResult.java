package ru.ibs.gasu.gchp.domain;

import java.util.ArrayList;
import java.util.List;

public class ProjectsResult {
    private int totalLength;
    private int offset;
    private List<ProjectDetailDTO> list = new ArrayList<>();

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<ProjectDetailDTO> getList() {
        return list;
    }

    public void setList(List<ProjectDetailDTO> list) {
        this.list = list;
    }
}
