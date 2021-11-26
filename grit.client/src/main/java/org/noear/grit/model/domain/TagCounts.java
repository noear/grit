package org.noear.grit.model.domain;

import java.io.Serializable;

/**
 * @author noear
 * @since 1.0
 */
public class TagCounts implements Serializable {
    public String tag;
    public long counts;

    public String getTag() {
        return tag;
    }

    public long getCounts() {
        return counts;
    }
}
