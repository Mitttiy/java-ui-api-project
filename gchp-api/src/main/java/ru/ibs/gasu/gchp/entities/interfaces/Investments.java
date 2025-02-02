package ru.ibs.gasu.gchp.entities.interfaces;


import java.util.List;

public interface Investments {
    List<? extends InvestmentIndicator> getIndicators();
}
