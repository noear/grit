package org.noear.grit.model.type;

/**
 * 资源类型
 *
 * @author noear
 * @since 1.0
 */
public enum ResourceType {
    entity(0),
    group(1),
    space(2);

    public final int code;

    ResourceType(int code) {
        this.code = code;
    }
}
