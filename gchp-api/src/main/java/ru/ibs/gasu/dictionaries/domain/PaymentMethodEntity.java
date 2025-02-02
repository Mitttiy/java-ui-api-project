package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DIC_GASU_GCHP_PAYMENT_METHOD
 */
@Getter
@Setter
public class PaymentMethodEntity {
    private Long id;
    private String name;
}
