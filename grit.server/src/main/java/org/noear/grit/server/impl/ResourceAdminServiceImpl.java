package org.noear.grit.server.impl;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.type.ResourceType;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.ResourceAdminService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/ResourceAdminService")
@Remoting
public class ResourceAdminServiceImpl implements ResourceAdminService {
    @Inject("grit.db")
    private DbContext db;
    @Inject("grit.cache")
    private ICacheService cache;

    /**
     * 添加资源
     *
     * @param resource 资源
     */
    @Override
    public long addResource(ResourceDo resource) throws SQLException {
        if (resource == null) {
            return -1;
        }

        resource.gmt_create = System.currentTimeMillis();
        resource.gmt_modified = resource.gmt_create;

        return db.table("grit_resource")
                .setEntity(resource).usingNull(false)
                .insert();
    }

    /**
     * 更新资源
     *
     * @param resourceId 资源Id
     * @param resource   资源
     */
    @Override
    public boolean updResourceById(long resourceId, ResourceDo resource) throws SQLException {
        if (resource == null) {
            return false;
        }

        if (resourceId == 0) {
            return false;
        }

        resource.gmt_modified = System.currentTimeMillis();

        return db.table("grit_resource")
                .setEntity(resource).usingNull(false)
                .whereEq("resource_id", resourceId)
                .update() > 0;
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源Id
     * */
    @Override
    public boolean delResourceById(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return false;
        }

        boolean isOk = db.table("grit_resource")
                .whereEq("resource_id", resourceId)
                .delete() > 0;

        db.table("grit_resource_linked")
                .whereEq("resource_id", resourceId)
                .delete();

        return isOk;
    }

    /**
     * 资源获取
     *
     * @param resourceId 资源Id
     */
    @Override
    public Resource getResourceById(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new Resource();
        }

        return db.table("grit_resource")
                .whereEq("resource_id", resourceId)
                .selectItem("*", Resource.class);
    }


    @Override
    public List<ResourceSpace> getSpaceList() throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_type", ResourceType.space.code)
                .selectList("*", ResourceSpace.class);
    }

    @Override
    public List<Resource> getResourceListBySpace(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .selectList("*", Resource.class);
    }

    @Override
    public List<ResourceGroup> getResourceGroupListBySpace(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .andEq("resource_type", ResourceType.group.code)
                .selectList("*", ResourceGroup.class);
    }

    @Override
    public List<Resource> getSubResourceListByPid(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .selectList("*", Resource.class);
    }
}
