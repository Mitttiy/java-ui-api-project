package ru.ibs.gasu.common.models;

import com.sencha.gxt.data.shared.TreeStore;

import java.util.Comparator;
import java.util.Objects;

public interface SortableTreeNode extends TreeStore.TreeNode<SortableTreeNode> {
    String getChildSortField();

    Long getRootSortField();

    static Comparator<SortableTreeNode> getComparator() {
        return (o1, o2) -> {
            //По этому условию можно определить, являются ли сравниваемые объекты rootItems.
            //Нужно использовать именно != null т.к. у элемента Node метод getChild() возвращает null.
            //А у рутовых объектов RootNode extend Node метод getChild() возвращает ArrayList, пусть даже и пустой.
            //Примеры можно посмотреть в коде у классов, реализующих TreeNodeComparable

            if (o1.getRootSortField() == null) {
                if (o2.getRootSortField() == null) {
                    return 0;
                }
                return -1;
            }
            if (o2.getRootSortField() == null) {
                return 1;
            }

            if (o1.getChildren() != null) {
                return o1.getRootSortField().compareTo(o2.getRootSortField());
            } else {
                return o1.getChildSortField().compareTo(o2.getChildSortField());
            }
        };
    }
}
