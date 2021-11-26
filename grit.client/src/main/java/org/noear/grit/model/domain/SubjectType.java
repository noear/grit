package org.noear.grit.model.domain;

/**
 * 主体类型
 *
 * @author noear
 * @since 1.0
 */
public enum SubjectType {
    entity(0),
    group(1);

    public final int code;

    SubjectType(int code) {
        this.code = code;
    }
}
