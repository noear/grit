package org.noear.grit.client.utils;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
public class ResourceTreeUtils {
    /**
     * 开始构建
     */
    public static <T extends Resource> List<T> build(List<T> list, long rootId) {
        List<T> list2 = new ArrayList<>(list.size());

        transDo(list, list2, rootId, 0);

        return list2;
    }

    private static <T extends Resource> void transDo(List<T> list, List<T> list2, long pId, int level) {
        list.stream().filter(m -> m.resource_pid == pId)
                .sorted(ResourceComparator.instance)
                .forEachOrdered(r -> {
                    r.level = level;
                    list2.add(r);
                    transDo(list, list2, r.resource_id, level + 1);
                });
    }
}
