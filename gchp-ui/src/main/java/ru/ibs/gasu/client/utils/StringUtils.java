package ru.ibs.gasu.client.utils;

import java.util.List;

/**
 * Утилитные методы для работы со строками
 */
public class StringUtils {

    /**
     * Проверить, пуста ли строка
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Проверить, не пуста ли строка
     *
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * Создать из списка строк в одну строку
     *
     * @param data
     * @param lineDelimiter
     * @return
     */
    public static String join(List<String> data, String lineDelimiter) {
        StringBuilder stringBuilder = new StringBuilder();

        if (CollectionUtils.isNotEmpty(data)) {
            stringBuilder.append(lineDelimiter);
            data.forEach(x -> stringBuilder.append(x).append("<br>"));
        }

        return stringBuilder.toString();
    }

}
