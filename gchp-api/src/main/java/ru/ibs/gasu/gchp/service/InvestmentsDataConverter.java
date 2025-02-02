package ru.ibs.gasu.gchp.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.entities.interfaces.InvestmentIndicator;
import ru.ibs.gasu.gchp.entities.interfaces.Investments;

@Service
public class InvestmentsDataConverter {

    @Data
    @AllArgsConstructor
    public static class HasNameWrapper {
        private Object name;
    }

    enum Type {
        PLAN, FACT
    }

    public Double getValueFromInvestments(Object o, Long id, Type type) {
        Investments investments = (Investments) o;
        if (investments != null && investments.getIndicators() != null) {
            for (InvestmentIndicator indicator : investments.getIndicators()) {
                if (indicator.getType().getId().equals(id)) {
                    if (type == Type.FACT)
                        return indicator.getFact();
                    else if (type == Type.PLAN)
                        return indicator.getPlan();
                }
            }
        }
        return null;
    }

    public HasNameWrapper getId1Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.PLAN));
    }

    public HasNameWrapper getId1Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.FACT));
    }

    public HasNameWrapper getId2Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 2L, Type.PLAN));
    }

    public HasNameWrapper getId2Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 2L, Type.FACT));
    }

    public HasNameWrapper getId3Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 3L, Type.PLAN));
    }

    public HasNameWrapper getId3Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 3L, Type.FACT));
    }

    public HasNameWrapper getId7Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 7L, Type.PLAN));
    }

    public HasNameWrapper getId7Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 7L, Type.FACT));
    }

    public HasNameWrapper getId8Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 8L, Type.PLAN));
    }

    public HasNameWrapper getId8Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 8L, Type.FACT));
    }

    public HasNameWrapper getId9Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 9L, Type.PLAN));
    }

    public HasNameWrapper getId9Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 9L, Type.FACT));
    }

    public HasNameWrapper getId10Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 10L, Type.PLAN));
    }

    public HasNameWrapper getId10Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 10L, Type.FACT));
    }

    public HasNameWrapper getId11Plan(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 11L, Type.PLAN));
    }

    public HasNameWrapper getId11Fact(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 11L, Type.FACT));
    }

    public HasNameWrapper getCbcInvestments1(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.PLAN));
    }

    public HasNameWrapper getCbcInvestments2(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.PLAN));
    }

    public HasNameWrapper getCbcInvestments3(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.PLAN));
    }

    public HasNameWrapper getCbcInvestments4(Object o) {
        return new HasNameWrapper(getValueFromInvestments(o, 1L, Type.PLAN));
    }


}
