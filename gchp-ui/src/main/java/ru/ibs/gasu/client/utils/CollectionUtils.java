package ru.ibs.gasu.client.utils;

import java.util.Collection;

/**
 * Утилитные методы для работы с коллекциями
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Проверить, пуста ли коллекция
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Проверить, не пуста ли коллекция
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }
}
