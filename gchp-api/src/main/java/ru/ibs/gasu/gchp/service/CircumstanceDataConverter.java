package ru.ibs.gasu.gchp.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.entities.CircumstanceStageIndicator;

import java.util.Objects;

@Service
public class CircumstanceDataConverter {

    @Data
    @AllArgsConstructor
    public static class HasNameWrapper {
        private Object name;
    }


    public Double getValueFromInvestments(CircumstanceStageIndicator indicator, Long id) {
        if (indicator.getCircumstanceType() != null
                && indicator.getCircumstanceType().getId() != null
                && Objects.equals(indicator.getCircumstanceType().getId(), id))
            return indicator.getSum();
        return null;
    }

    public HasNameWrapper getId1Sum(Object o) {
        return new HasNameWrapper(getValueFromInvestments((CircumstanceStageIndicator) o, 1L));
    }

}
