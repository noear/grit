package org.noear.grit.server.dso.service.impl;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.data.ResourceLinkedDo;
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
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源管理服务实现
 *
 * @author noear
 * @since 1.0
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/v1/ResourceAdminService")
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
                .setEntity(resource)
                .usingNull(false)
                .usingExpr(false)
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
                .setEntity(resource)
                .usingNull(false)
                .usingExpr(false)
                .whereEq("resource_id", resourceId)
                .update() > 0;
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源Id
     */
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

    /**
     * 获取管理用的资源空间列表
     */
    @Override
    public List<ResourceSpace> getSpaceList() throws SQLException {
        return db.table("grit_resource")
                .whereEq("resource_type", ResourceType.space.code)
                .selectList("*", ResourceSpace.class);
    }

    /**
     * 获取管理用的空间内所有资源
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<Resource> getResourceListBySpace(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .selectList("*", Resource.class);
    }

    /**
     * 获取管理用的空间内所有资源分析
     *
     * @param resourceId 资源Id
     */
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

    /**
     * 获取管理用的空间内所有资源实体
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<ResourceGroup> getResourceEntityListBySpace(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .andEq("resource_type", ResourceType.entity.code)
                .selectList("*", ResourceGroup.class);
    }

    /**
     * 获取管理用的下级资源表表
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<Resource> getSubResourceListByPid(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .selectList("*", Resource.class);
    }

    ///////////////////

    /**
     * 添加资源关联
     *
     * @param resourceId  资源Id
     * @param subjectId   主体Id
     * @param subjectType 主体类型
     */
    @Override
    public long addResourceLink(long resourceId, long subjectId, int subjectType) throws SQLException {
        return db.table("grit_resource_linked")
                .set("resource_id", resourceId)
                .set("subject_id", subjectId)
                .set("subject_type", subjectType)
                .set("gmt_create", System.currentTimeMillis())
                .insert();
    }

    /**
     * 删除资源关联
     *
     * @param linkIds 资源连接Ids
     */
    @Override
    public void delResourceLink(long... linkIds) throws SQLException {
        db.table("grit_resource_linked")
                .whereIn("link_id", Arrays.asList(linkIds))
                .delete();
    }

    @Override
    public void addResourceLinkBySubject(long subjectId, int subjectType, List<Long> resourceIds) throws SQLException {
        List<DataItem> items = new ArrayList<>();
        long gmt_create = System.currentTimeMillis();

        for (Long resId : resourceIds) {
            DataItem item = new DataItem();
            item.set("resource_id", resId);
            item.set("subject_id", subjectId);
            item.set("subject_type", subjectType);
            item.set("gmt_create", gmt_create);

            items.add(item);
        }

        db.table("grit_resource_linked").insertList(items);
    }

    @Override
    public void delResourceLinkBySubject(long subjectId) throws SQLException {
        db.table("grit_resource_linked")
                .whereEq("subject_id", subjectId)
                .delete();
    }

    @Override
    public List<ResourceLinkedDo> getResourceLinkListBySubject(long subjectId) throws SQLException {
        return db.table("grit_resource_linked")
                .whereEq("subject_id", subjectId)
                .selectList("*", ResourceLinkedDo.class);
    }
}
