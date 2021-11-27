package org.noear.grit.client.comparator;

import org.noear.grit.model.domain.Subject;

import java.util.Comparator;

/**
 * @author noear
 * @since 1.0
 */
public class SubjectComparator implements Comparator<Subject> {
    public static SubjectComparator instance = new SubjectComparator();

    @Override
    public int compare(Subject o1, Subject o2) {
        if (o1.order_index == o2.order_index) {
            if (o1.subject_id < o2.subject_id) {
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
