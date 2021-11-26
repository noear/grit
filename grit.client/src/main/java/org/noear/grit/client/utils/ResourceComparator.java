package org.noear.grit.client.utils;

import org.noear.grit.model.domain.Resource;

import java.util.Comparator;

/**
 * @author noear 2021/11/27 created
 */
public class ResourceComparator implements Comparator<Resource> {
    public static ResourceComparator instance = new ResourceComparator();

    @Override
    public int compare(Resource r1, Resource r2) {
        if (r1.order_index == r2.order_index) {
            if (r1.resource_id < r2.resource_id) {
                return -1;
            } else {
                return 1;
            }
        }

        if (r1.order_index < r2.order_index) {
            return -1;
        } else {
            return 1;
        }
    }
}
