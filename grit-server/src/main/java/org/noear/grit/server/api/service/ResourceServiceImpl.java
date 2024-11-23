package org.noear.grit.server.api.service;

import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.type.ResourceType;
import org.noear.grit.server.api.dso.AfterHandler;
import org.noear.grit.server.api.dso.BeforeHandler;
import org.noear.grit.service.ResourceService;
import org.noear.solon.annotation.*;
import org.noear.wood.DbContext;
import org.noear.wood.cache.ICacheServiceEx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务实现
 *
 * @author noear
 * @since 1.0
 */
@Addition({BeforeHandler.class, AfterHandler.class})
@Mapping("/grit/api/v1/ResourceService")
@Remoting
public class ResourceServiceImpl implements ResourceService {
    @Inject("grit.db")
    private  DbContext db;
    @Inject("grit.cache")
    private ICacheServiceEx cache;


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
     * @param resourceGuid 资源Guid
     */
    @Override
    public Resource getResourceByGuid(long resourceGuid) throws SQLException {
        return db.table("grit_resource")
                .whereEq("guid", resourceGuid)
                .caching(cache)
                .selectItem("*", Resource.class);
    }

    @Override
    public boolean hasResourceByGuid(long resourceGuid) throws SQLException {
        return db.table("grit_resource")
                .whereEq("guid", resourceGuid)
                .caching(cache)
                .selectExists();
    }

    /**
     * 资源获取
     *
     * @param resourceCode 资源代号
     */
    @Override
    public Resource getResourceByCodeAndSpace(long resourceSpaceId, String resourceCode) throws SQLException {
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
    public Resource getResourceByUriAndSpace(long resourceSpaceId, String resourceUri) throws SQLException {
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
    public boolean hasResourceByCodeAndSpace(long resourceSpaceId, String resourceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceCode)) {
            return false;
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceCode)
                .andIf(resourceSpaceId > 0,  "resource_sid=?", resourceSpaceId)
                .limit(1)
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
    public boolean hasResourceByUriAndSpace(long resourceSpaceId, String resourceUri) throws SQLException {
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
    public List<Resource> getSubResourceListByPid(long resourceId) throws SQLException {
        if(resourceId == 0){
            return new ArrayList<>();
        }

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
    public List<ResourceEntity> getSubResourceEngityListByPid(long resourceId) throws SQLException {
        if(resourceId == 0){
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .andEq("resource_type", ResourceType.entity.code)
                .caching(cache)
                .selectList("*", ResourceEntity.class);
    }

    ///////////////////////////
    /**
     * 获取资源空间
     *
     * @param resourceSpaceCode 资源空间代号
     */
    @Override
    public ResourceSpace getSpaceByCode(String resourceSpaceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceSpaceCode)) {
            return new ResourceSpace();
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceSpaceCode)
                .limit(1)
                .caching(cache)
                .selectItem("*", ResourceSpace.class);
    }

    @Override
    public boolean hasSpaceByCode(String resourceSpaceCode) throws SQLException {
        if (TextUtils.isEmpty(resourceSpaceCode)) {
            return false;
        }

        return db.table("grit_resource")
                .whereEq("resource_code", resourceSpaceCode)
                .caching(cache)
                .selectExists();
    }

    /**
     * 获取所有的资源空间列表
     */
    @Override
    public List<ResourceSpace> getSpaceList() throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_type", ResourceType.space.code)
                .andEq("is_visibled", 1)
                .andEq("is_disabled", 0)
                .caching(cache)
                .selectList("*", ResourceSpace.class);
    }
}
