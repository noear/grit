package org.noear.grit.model.domain;

import org.noear.grit.client.GritClient;

import java.sql.SQLException;
import java.util.List;

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

    /**
     * 获取授权的主体列表
     */
    public List<Subject> getAuthSubjectList() throws SQLException {
        return GritClient.resourceLink().getSubjectListByResource(resource_id);
    }
}
