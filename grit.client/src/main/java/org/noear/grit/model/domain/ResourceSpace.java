package org.noear.grit.model.domain;

import org.noear.grit.client.GritClient;

import java.sql.SQLException;
import java.util.List;

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

    /**
     * 获取下级分组列表
     */
    public List<Resource> getSubGroupList() throws SQLException {
        return GritClient.global().resource().getSubResourceListById(resource_id);
    }
}
