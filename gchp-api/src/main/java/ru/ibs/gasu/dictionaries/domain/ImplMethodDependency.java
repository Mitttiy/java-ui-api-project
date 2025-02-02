package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImplMethodDependency {
    private Long formId;
    private Long implMethodId;
}
