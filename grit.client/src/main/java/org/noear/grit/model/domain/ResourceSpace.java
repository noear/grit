package org.noear.grit.model.domain;

import org.noear.grit.model.type.ResourceType;

/**
 * 资源空间
 *
 * @author noear
 * @since 1.0
 */
public class ResourceSpace extends Resource {
    public ResourceSpace() {
        this(0);
    }

    public ResourceSpace(long resourceSpaceId) {
        super();

        resource_id = resourceSpaceId;
        resource_type = ResourceType.space.code;
    }
}
