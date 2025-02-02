package ru.ibs.gasu.client.widgets;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.utils.SortUtils;
import ru.ibs.gasu.client.widgets.componens.DefaultCallback;
import ru.ibs.gasu.common.soap.generated.gchpdicts.Dir;
import ru.ibs.gasu.common.soap.generated.gchpdicts.EgrulCriteria;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SimpleEgrulDomain;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SortField;

import java.util.Arrays;
import java.util.Collections;

import static ru.ibs.gasu.client.utils.GridUtils.createColumn;
import static ru.ibs.gasu.common.models.EgrulDomainProperties.SIMPLE_EGRUL_DOMAIN_PROPERTIES;

@SuppressWarnings("GwtToHtmlReferences")
public class EgrulGridWindow extends SelectObjectGridWindow<SimpleEgrulDomain> {

    @Override
    protected String createHeading() {
        return isEgrip() ? "Выберите индивидуального предпринимателя" : "Выберите юридическое лицо";
    }

    @Override
    protected ListStore<SimpleEgrulDomain> createStore() {
        return new ListStore<>(item -> String.valueOf(item.getId()));
    }

    protected boolean isEgrip() {
        return false;
    }

    @Override
    protected PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SimpleEgrulDomain>> createLoader() {
        RpcProxy<FilterPagingLoadConfig, PagingLoadResult<SimpleEgrulDomain>> rpcProxy =
                new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<SimpleEgrulDomain>>() {
                    @Override
                    public void load(FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<SimpleEgrulDomain>> callback) {
                        if (loadConfig != null) {
                            EgrulCriteria criteria = new EgrulCriteria();
                            criteria.setLimit(loadConfig.getLimit());
                            criteria.setOffset(loadConfig.getOffset());
                            criteria.setEgrip(isEgrip());

                            SortField defaultSortField = new SortField();
                            defaultSortField.setField("id");
                            defaultSortField.setDirection(Dir.ASC);
                            criteria.setSortInfo(SortUtils.createSortInfo(loadConfig, Collections.singletonList(defaultSortField)));

                            applyGridFilterToCriteria(loadConfig, criteria);

                            GchpApi.GCHP_API.getSimpleEgrulDomains(criteria, new DefaultCallback(callback));
                        }
                    }
                };
        return new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SimpleEgrulDomain>>(rpcProxy) {
            @Override
            protected FilterPagingLoadConfig newLoadConfig() {
                return new FilterPagingLoadConfigBean();
            }
        };
    }

    @Override
    protected ColumnModel<SimpleEgrulDomain> createColumnModel() {
//        ColumnConfig<SimpleEgrulDomain, Long> idColumn = createColumn(SIMPLE_EGRUL_DOMAIN_PROPERTIES.id(), 100, "Id");
        ColumnConfig<SimpleEgrulDomain, String> fullNameColumn = createColumn(SIMPLE_EGRUL_DOMAIN_PROPERTIES.fullName(), 200, "Полное название");
        ColumnConfig<SimpleEgrulDomain, String> shortColumn = createColumn(SIMPLE_EGRUL_DOMAIN_PROPERTIES.shortName(), 200, "Сокращенное название");
        ColumnConfig<SimpleEgrulDomain, String> innColumn = createColumn(SIMPLE_EGRUL_DOMAIN_PROPERTIES.inn(), 200, "ИНН");
        ColumnConfig<SimpleEgrulDomain, String> ogrnColumn = createColumn(SIMPLE_EGRUL_DOMAIN_PROPERTIES.ogrn(), 200, "ОГРН");
        return new ColumnModel<>(Arrays.asList(/*idColumn,*/ fullNameColumn, shortColumn, innColumn, ogrnColumn));
    }

    @Override
    protected GridFilters<SimpleEgrulDomain> createFilters(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<SimpleEgrulDomain>> loader) {
        GridFilters<SimpleEgrulDomain> filters = new GridFilters<>(loader);
//        filters.addFilter(new NumericFilter<>(SIMPLE_EGRUL_DOMAIN_PROPERTIES.id(), new NumberPropertyEditor.LongPropertyEditor()));
//        filters.addFilter(new StringFilter<>(SIMPLE_EGRUL_DOMAIN_PROPERTIES.fullName()));
//        filters.addFilter(new StringFilter<>(SIMPLE_EGRUL_DOMAIN_PROPERTIES.shortName()));
        filters.addFilter(new StringFilter<>(SIMPLE_EGRUL_DOMAIN_PROPERTIES.inn()));
        filters.addFilter(new StringFilter<>(SIMPLE_EGRUL_DOMAIN_PROPERTIES.ogrn()));
        return filters;
    }


    /**
     * Применть фильтра грида к criteria
     *
     * @param loadConfig
     * @param criteria
     */
    private void applyGridFilterToCriteria(FilterPagingLoadConfig loadConfig, EgrulCriteria criteria) {
        loadConfig.getFilters().forEach(x -> {
            if (x.getValue() == null || x.getValue().trim().isEmpty()) {
                return;
            }

//            if (SIMPLE_EGRUL_DOMAIN_PROPERTIES.id().getPath().equalsIgnoreCase(x.getField())) {
//                criteria.setId(Long.valueOf(x.getValue()));
//            }

//            if (SIMPLE_EGRUL_DOMAIN_PROPERTIES.fullName().getPath().equalsIgnoreCase(x.getField())) {
//                criteria.setFullName(x.getValue());
//            }

//            if (SIMPLE_EGRUL_DOMAIN_PROPERTIES.shortName().getPath().equalsIgnoreCase(x.getField())) {
//                criteria.setShortName(x.getValue());
//            }

            if (SIMPLE_EGRUL_DOMAIN_PROPERTIES.inn().getPath().equalsIgnoreCase(x.getField())) {
                criteria.setInn(x.getValue());
            }

            if (SIMPLE_EGRUL_DOMAIN_PROPERTIES.ogrn().getPath().equalsIgnoreCase(x.getField())) {
                criteria.setOgrn(x.getValue());
            }

        });
    }

}
