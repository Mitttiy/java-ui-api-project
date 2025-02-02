package ru.ibs.gasu.common.models;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SimpleEgrulDomain;

public interface EgrulDomainProperties extends PropertyAccess<SimpleEgrulDomain> {

    EgrulDomainProperties SIMPLE_EGRUL_DOMAIN_PROPERTIES = GWT.create(EgrulDomainProperties.class);

    ValueProvider<SimpleEgrulDomain, Long> id();

    ValueProvider<SimpleEgrulDomain, String> fullName();

    ValueProvider<SimpleEgrulDomain, String> shortName();

    ValueProvider<SimpleEgrulDomain, String> inn();

    ValueProvider<SimpleEgrulDomain, String> ogrn();

}
