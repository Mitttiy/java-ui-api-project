package ru.ibs.gasu.gchp.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentsFilterPaginateCriteria extends PagingSearchCriteria {
    private Long docId;
    private String docName;
    private String formId;
    private List<String> projectStatusIds = new ArrayList<>();
    private List<String> realizationStatusIds = new ArrayList<>();
    private String levelId;
    private String sphereId;
    private String dicGasuSp1Id;
    private String subjectId;
    private String municipalId;
    private String publicPartnerId;
    private String sectorId;
}
