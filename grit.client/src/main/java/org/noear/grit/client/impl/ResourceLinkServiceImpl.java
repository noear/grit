package org.noear.grit.client.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.domain.*;
import org.noear.grit.service.ResourceLinkService;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源连接服务实现
 *
 * @author noear
 * @since 1.0
 */
public class ResourceLinkServiceImpl implements ResourceLinkService {
    private final DbContext db;
    private final ICacheService cache;

    public ResourceLinkServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }

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
    public boolean hasResourceLink(long resourceId, long subjectId) throws SQLException {
        return db.table("grit_resource_linked")
                .whereEq("resource_id", resourceId)
                .andEq("subject_id", subjectId)
                .caching(cache)
                .selectExists();
    }

    /**
     * 获取资源授权过的所有主体
     *
     * @param resourceId 资源Id
     * @return 主体列表
     */
    @Override
    public List<Subject> getSubjectListByResource(long resourceId) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_subject s")
                .on("l.subject_id=s.subject_id").andEq("l.resource_id", resourceId)
                .caching(cache)
                .selectList("s.*", Subject.class);
    }

    /**
     * 获取主体所有的授权资源列表
     *
     * @param subjectId 主体Id
     * @return 资源列表
     */
    @Override
    public List<ResourceEntity> getResourceEntityListBySubject(long subjectId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id").andEq("l.subject_id", subjectId)
                .whereIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .caching(cache)
                .selectList("s.*", ResourceEntity.class);
    }


    /**
     * 获取主体在某一个资源组下的授权资源列表
     *
     * @param subjectId       主体Id
     * @param resourceGroupId 资源组Id
     * @return 资源列表
     */
    @Override
    public List<ResourceEntity> getResourceEntityListBySubjectAndGroup(long subjectId, long resourceGroupId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andEq("l.subject_id", subjectId)
                .andEq("r.resource_pid", resourceGroupId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .caching(cache)
                .selectList("s.*", ResourceEntity.class);
    }

    @Override
    public List<ResourceEntity> getResourceEntityListBySubjectAndSpace(long subjectId, long resourceSpaceId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andEq("l.subject_id", subjectId)
                .andEq("r.resource_sid", resourceSpaceId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .caching(cache)
                .selectList("s.*", ResourceEntity.class);
    }

    @Override
    public List<ResourceEntity> getResourceEntityListBySubjectsAndGroup(List<Long> subjectIds, long resourceGroupId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andEq("r.resource_pid", resourceGroupId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .caching(cache)
                .selectList("s.*", ResourceEntity.class);
    }

    @Override
    public List<ResourceEntity> getResourceEntityListBySubjectsAndSpace(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andEq("r.resource_sid", resourceSpaceId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .caching(cache)
                .selectList("s.*", ResourceEntity.class);
    }

    @Override
    public ResourceEntity getResourceEntityFristBySubjectsAndSpace(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andEq("r.resource_sid", resourceSpaceId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .limit(1)
                .caching(cache)
                .selectItem("s.*", ResourceEntity.class);
    }

    @Override
    public ResourceEntity getResourceEntityFristBySubjectsAndGroup(List<Long> subjectIds, long resourceGroupId, Boolean isVisibled) throws SQLException {
        return db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andEq("r.resource_pid", resourceGroupId)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .limit(1)
                .caching(cache)
                .selectItem("s.*", ResourceEntity.class);
    }

    @Override
    public List<ResourceGroup> getResourceGroupListBySubjects(List<Long> subjectIds, long resourceSpaceId, Boolean isVisibled) throws SQLException {
        List<Long> groupIds = GritClient.resource().getSubResourceListById(resourceSpaceId)
                .stream()
                .map(r->r.resource_id)
                .collect(Collectors.toList());

        List<Long> groupIds2 = db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andIn("r.resource_pid", groupIds)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .groupBy("r.resource_pid")
                .caching(cache)
                .selectArray("r.resource_pid");

        return db.table("grit_resource")
                .whereIn("resource_id", groupIds2)
                .andEq("is_visibled", 1)
                .andEq("is_disabled", 0)
                .caching(cache)
                .selectList("*", ResourceGroup.class);
    }

    @Override
    public List<ResourceSpace> getResourceSpaceListBySubjects(List<Long> subjectIds, Boolean isVisibled) throws SQLException {
        List<Long> spaceIds = db.table("grit_resource_linked l")
                .innerJoin("grit_resource r")
                .on("l.subject_id=r.resource_id")
                .andIn("l.subject_id", subjectIds)
                .andIf(isVisibled != null, "r.is_visibled=?", isVisibled)
                .andEq("r.is_disabled", 0)
                .groupBy("r.resource_sid")
                .caching(cache)
                .selectArray("r.resource_sid");

        return db.table("grit_resource")
                .whereIn("resource_id", spaceIds)
                .andEq("is_visibled", 1)
                .andEq("is_disabled", 0)
                .caching(cache)
                .selectList("*", ResourceSpace.class);
    }
}
