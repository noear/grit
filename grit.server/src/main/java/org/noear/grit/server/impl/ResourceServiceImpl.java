package org.noear.grit.server.impl;

import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.type.ResourceType;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.ResourceService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.List;

/**
 * 资源服务实现
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/ResourceService")
@Remoting
public class ResourceServiceImpl implements ResourceService {
    @Db("grit.db")
    private  DbContext db;
    @Inject("grit.cache")
    private  ICacheService cache;


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
    public boolean updResource(long resourceId, ResourceDo resource) throws SQLException {
        if (resource == null) {
            return false;
        }

        if (resourceId < 1) {
            return false;
        }

        return db.table("grit_resource")
                .setEntity(resource).usingNull(false)
                .whereEq("resource_id", resourceId)
                .update() > 0;
    }

    /**
     * 资源获取
     *
     * @param resourceId 资源Id
     */
    @Override
    public Resource getResourceById(long resourceId) throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_id", resourceId)
                .caching(cache)
                .selectItem("*", Resource.class);
    }

    /**
     * 资源获取
     *
     * @param resourceCode 资源代号
     */
    @Override
    public Resource getResourceByCode(long resourceSpaceId, String resourceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceCode)) {
            return new Resource();
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceCode)
                .andIf(resourceSpaceId > 0,  "resource_sid=?", resourceSpaceId)
                .limit(1)
                .caching(cache)
                .selectItem("*", Resource.class);
    }

    /**
     * 资源获取
     *
     * @param resourceUri 资源路径
     */
    @Override
    public Resource getResourceByUri(long resourceSpaceId, String resourceUri) throws SQLException {
        if (TextUtils.isEmpty(resourceUri)) {
            return new Resource();
        }

        return db.table("grit_resource")
                .whereEq("link_uri", resourceUri)
                .andIf(resourceSpaceId > 0,  "resource_sid=?", resourceSpaceId)
                .limit(1)
                .caching(cache)
                .selectItem("*", Resource.class);
    }



    /**
     * 检测资源代号是否存在
     *
     * @param resourceCode 资源代号
     * @return 是否存在
     */
    @Override
    public boolean hasResourceCode(long resourceSpaceId, String resourceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceCode)) {
            return false;
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceCode)
                .andIf(resourceSpaceId > 0,  "resource_sid=?", resourceSpaceId)
                .caching(cache)
                .selectExists();
    }


    /**
     * 极测资源路径是否存在
     *
     * @param resourceUri 资源路径
     * @return 是否存在
     */
    @Override
    public boolean hasResourceUri(long resourceSpaceId, String resourceUri) throws SQLException {
        if (TextUtils.isEmpty(resourceUri)) {
            return false;
        }

        return db.table("grit_resource")
                .whereEq("link_uri", resourceUri)
                .andIf(resourceSpaceId > 0,  "resource_sid=?", resourceSpaceId)
                .caching(cache)
                .selectExists();
    }


    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<Resource> getSubResourceListById(long resourceId) throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .caching(cache)
                .selectList("*", Resource.class);
    }

    /**
     * 下级资源列表根据组获取
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<ResourceEntity> getSubResourceEngityListById(long resourceId) throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .andEq("resource_type", ResourceType.entity.code)
                .caching(cache)
                .selectList("*", ResourceEntity.class);
    }
}
