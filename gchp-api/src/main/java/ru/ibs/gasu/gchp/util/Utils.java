package ru.ibs.gasu.gchp.util;

import org.springframework.data.domain.Sort;
import ru.ibs.gasu.gchp.domain.SortInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.util.ObjectUtils.isEmpty;

public class Utils {

    public static <T> T opt(Supplier<T> statement) {
        try {
            return statement.get();
        } catch (Exception exc) {
            return null;
        }
    }

    public static Sort createSort(SortInfo sortInfo) {
        List<Sort.Order> sortList = new ArrayList<>();

        if (!isEmpty(sortInfo) && !isEmpty(sortInfo.getFields())) {
            sortInfo.getFields().forEach(o -> {
                Sort.Direction direction = SortInfo.Dir.ASC.equals(o.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
                Sort.Order sortOrder = new Sort.Order(direction, o.getField());
                sortList.add(sortOrder);
            });
        }

        return Sort.by(sortList);
    }

    public static String logFormat(String pattern, Object... args) {
        return String.format(pattern, args);
    }

}
