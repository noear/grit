package org.noear.grit.model.domain;

import org.noear.grit.model.type.ResourceType;

/**
 * 资源实体
 *
 * @author noear
 * @since 1.0
 */
public class ResourceEntity extends Resource {
    public ResourceEntity() {
        this(0);
    }

    public ResourceEntity(long reourceId) {
        super();

        resource_id = reourceId;
        resource_type = ResourceType.entity.code;
    }
}
