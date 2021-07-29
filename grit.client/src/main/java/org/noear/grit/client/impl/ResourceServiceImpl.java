package org.noear.grit.client.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务实现
 *
 * @author noear
 * @since 1.0
 */
public class ResourceServiceImpl implements ResourceService {
    private final DbContext db;
    private final ICacheService cache;

    public ResourceServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }


    @Override
    public Resource getResourceById(long resourceId) throws SQLException {
        return db.table("stone_resource")
                .whereEq("resource_id", resourceId)
                .limit(1)
                .caching(cache)
                .selectItem("*", Resource.class);
    }

    @Override
    public Resource getResourceByCode(String resourceCode) throws SQLException {
        return db.table("stone_resource")
                .whereEq("resource_code", resourceCode)
                .limit(1)
                .caching(cache)
                .selectItem("*", Resource.class);
    }

    @Override
    public Resource getResourceByPath(String resourcePath) throws SQLException {
        return db.table("stone_resource")
                .whereEq("link_uri", resourcePath)
                .limit(1)
                .caching(cache)
                .selectItem("*", Resource.class);
    }


    @Override
    public boolean hasResourcePath(String uri) throws SQLException {
        if (TextUtils.isEmpty(uri)) {
            return false;
        }

        return db.table("stone_resource")
                .whereEq("uri_path", uri)
                .caching(cache)
                .selectExists();
    }

    @Override
    public boolean hasResourceCode(String resourceCode) throws SQLException {
        return false;
    }

    @Override
    public List<Resource> getResourceListByGroup(String groupCode) throws SQLException {
        if (TextUtils.isEmpty(groupCode)) {
            return new ArrayList<>();
        }

        Group group = GritClient.group().getGroupByCode(groupCode);

        return getResourceListByGroup(group.group_id);
    }

    @Override
    public List<Resource> getResourceListByGroup(long groupId) throws SQLException {
        if (groupId < 1) {
            return new ArrayList<>();
        }

        List<Object> resourceIds = db.table("stone_resource_linked")
                .whereEq("lk_objt_id", groupId)
                .andEq("lk_objt", Constants.OBJT_stone_group)
                .caching(cache)
                .selectArray("resource_id");


        return db.table("stone_resource")
                .whereIn("resource_id", resourceIds)
                .caching(cache)
                .selectList("*", Resource.class);
    }

    @Override
    public List<Resource> getResourceListByUser(long userId) throws SQLException {
        if (userId < 1) {
            return new ArrayList<>();
        }

        List<Object> resourceIds = db.table("stone_resource_linked")
                .whereEq("lk_objt_id", userId)
                .andEq("lk_objt", Constants.OBJT_stone_user)
                .caching(cache)
                .selectArray("resource_id");


        return db.table("stone_resource")
                .whereIn("resource_id", resourceIds)
                .caching(cache)
                .selectList("*", Resource.class);
    }

    @Override
    public List<Resource> getResourceListByUserAndGroup(long userId, String groupCode) throws SQLException {
        if (TextUtils.isEmpty(groupCode)) {
            return new ArrayList<>();
        }

        Group group = GritClient.group().getGroupByCode(groupCode);

        return getResourceListByUserAndGroup(userId, group.group_id);
    }

    @Override
    public List<Resource> getResourceListByUserAndGroup(long userId, long groupId) throws SQLException {
        return null;
    }
}
