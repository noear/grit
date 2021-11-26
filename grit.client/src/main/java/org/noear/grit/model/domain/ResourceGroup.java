package org.noear.grit.model.domain;

import org.noear.grit.client.GritClient;

import java.sql.SQLException;
import java.util.List;

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


    /**
     * 获取上级分组
     */
    public Resource getSupGroup() throws SQLException {
        return GritClient.resource().getResourceById(resource_pid);
    }

    /**
     * 获取下级分组列表
     */
    public List<Resource> getSubGroupList() throws SQLException {
        return GritClient.resource().getSubResourceListById(resource_id);
    }

    /**
     * 获取下级资源列表
     */
    public List<ResourceEntity> getSubResourceEngityList() throws SQLException {
        return GritClient.resource().getSubResourceEngityListById(resource_pid);
    }
}
