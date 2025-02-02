package ru.ibs.gasu.client.utils;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import ru.ibs.gasu.common.soap.generated.gchpdicts.Dir;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SortField;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SortInfo;

import java.util.List;

public class SortUtils {

    public static SortInfo createSortInfo(FilterPagingLoadConfig loadConfig) {
        return createSortInfo(loadConfig, null);
    }

    public static SortInfo createSortInfo(ListLoadConfig loadConfig, List<SortField> defaultSortFields) {
        SortInfo sortInfo = new SortInfo();

        // Если нет сортировки из грида, применяем дефолтные сортировки
        if (loadConfig == null || loadConfig.getSortInfo() == null || loadConfig.getSortInfo().isEmpty()) {

            // Если нет и дефолтных сортировок, то возвращаем пустую сортировку
            if (defaultSortFields == null || defaultSortFields.isEmpty()) {
                return sortInfo;
            }

            // Применяем дефолные сортировки
            defaultSortFields.forEach(x -> {
                if (x != null) {
                    sortInfo.getFields().add(x);
                }
            });

            return sortInfo;
        }

        // Применяем сортировки из грида
        loadConfig.getSortInfo().forEach(x -> {
            SortField sortField = new SortField();
            sortField.setField(x.getSortField());
            sortField.setDirection(Dir.fromValue(x.getSortDir().name()));
            sortInfo.getFields().add(sortField);
        });

        return sortInfo;
    }

    public static SortInfo createSortInfo(List<SortField> defaultSortFields) {
        SortInfo sortInfo = new SortInfo();

        // Если нет и дефолтных сортировок, то возвращаем пустую сортировку
        if (defaultSortFields == null || defaultSortFields.isEmpty()) {
            return sortInfo;
        }

        // Применяем дефолные сортировки
        defaultSortFields.forEach(x -> {
            if (x != null) {
                sortInfo.getFields().add(x);
            }
        });

        return sortInfo;
    }
}
