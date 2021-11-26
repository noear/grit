package org.noear.grit.model.domain;

/**
 * 资源分组领域模型（主要有：资源分组；用户分组；角色分组）
 *
 * @author noear
 * @since 1.0
 */
public class ResourceGroup extends Resource {
    public ResourceGroup() {
        this(0);
    }

    public ResourceGroup(long resourceGoupId) {
        super();

        resource_id = resourceGoupId;
        resource_type = ResourceType.group.code;
    }
}
