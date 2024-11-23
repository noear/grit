package org.noear.grit.client.comparator;

import org.noear.grit.model.domain.Resource;

import java.util.Comparator;

/**
 * @author noear
 * @since 1.0
 */
public class ResourceComparator implements Comparator<Resource> {
    public static ResourceComparator instance = new ResourceComparator();

    @Override
    public int compare(Resource o1, Resource o2) {
        if (o1.order_index == o2.order_index) {
            if (o1.resource_id < o2.resource_id) {
                return -1;
            } else {
                return 1;
            }
        }

        if (o1.order_index < o2.order_index) {
            return -1;
        } else {
            return 1;
        }
    }
}
