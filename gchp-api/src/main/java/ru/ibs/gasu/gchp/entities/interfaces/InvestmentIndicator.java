package ru.ibs.gasu.gchp.entities.interfaces;

public interface InvestmentIndicator {
    Double getPlan();
    Double getFact();
    InvestmentType getType();
}
