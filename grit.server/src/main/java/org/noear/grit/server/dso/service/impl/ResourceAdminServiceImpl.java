package org.noear.grit.server.dso.service.impl;

import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.data.ResourceLinkedDo;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceEntity;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.model.type.ResourceType;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.server.dso.service.SubjectAdminService;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.*;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.extend.aspect.annotation.Service;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * 资源管理服务实现
 *
 * @author noear
 * @since 1.0
 */
@Service
public class ResourceAdminServiceImpl implements ResourceAdminService {
    @Inject("grit.db")
    private DbContext db;
    @Inject("grit.cache")
    private ICacheService cache;

    @Inject
    SubjectAdminService subjectAdminService;

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

        if (resource.gmt_create == null) {
            resource.gmt_create = System.currentTimeMillis(); //可能是导入的
        }

        if (resource.gmt_modified == null) {
            resource.gmt_modified = resource.gmt_create; //可能是导入的
        }

        if (Utils.isEmpty(resource.guid)) {
            resource.guid = Utils.guid(); //guid 必须要有
        }

        return db.table("grit_resource")
                .setEntity(resource)
                .usingNull(false)
                .usingExpr(false)
                .insert();
    }

    @Override
    public boolean synResourceByGuid(ResourceDo resource) throws SQLException {
        if (resource == null || Utils.isEmpty(resource.guid)) {
            return false;
        }

        resource.resource_id = null;

        ResourceDo tmp = getResourceByGuid(resource.guid);
        if (tmp.resource_id == null) {
            resource.resource_id = addResource(resource);
        } else {
            resource.resource_id = tmp.resource_id;
        }

        return true;
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
    @Tran
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

    @Tran
    @Override
    public boolean delResourceByIds(String ids) throws SQLException {
        if (Utils.isEmpty(ids)) {
            return false;
        }

        List<Object> idList = getIdList(ids);

        boolean isOk = db.table("grit_resource")
                .whereIn("resource_id", idList)
                .delete() > 0;

        db.table("grit_resource_linked")
                .whereIn("resource_id", idList)
                .delete();

        return isOk;
    }

    @Tran
    @Override
    public boolean desResourceByIds(String ids, boolean disabled) throws SQLException {
        if (Utils.isEmpty(ids)) {
            return false;
        }

        List<Object> idList = getIdList(ids);

        return db.table("grit_resource")
                .set("is_disabled", disabled)
                .whereIn("resource_id", idList)
                .update() > 0;
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
    public Resource getResourceByGuid(String guid) throws SQLException {
        if (Utils.isEmpty(guid)) {
            return new Resource();
        }

        return db.table("grit_resource")
                .whereEq("guid", guid)
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

    @Override
    public List<ResourceGroup> getResourceGroupListBySpaceAndIds(long resourceId, String ids) throws SQLException {
        if (resourceId == 0 || Utils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        List<Object> idList = getIdList(ids);

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .andEq("resource_type", ResourceType.group.code)
                .andIn("resource_id", idList)
                .selectList("*", ResourceGroup.class);
    }

    /**
     * 获取管理用的空间内所有资源实体
     *
     * @param resourceId 资源Id
     */
    @Override
    public List<ResourceEntity> getResourceEntityListBySpace(long resourceId) throws SQLException {
        if (resourceId == 0) {
            return new ArrayList<>();
        }

        return db.table("grit_resource")
                .whereEq("resource_sid", resourceId)
                .andEq("resource_type", ResourceType.entity.code)
                .selectList("*", ResourceEntity.class);
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

    @Override
    public List<Resource> getSubResourceListByPidAndIds(long resourceId, String ids) throws SQLException {
        if (resourceId == 0 || Utils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        List<Object> idList = getIdList(ids);

        return db.table("grit_resource")
                .whereEq("resource_pid", resourceId)
                .andIn("resource_id", idList)
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
    public void delResourceLinkBySubjectBySpace(long subjectId, long resourceSpaceId) throws SQLException {
//        db.sql("DELETE l FROM grit_resource_linked l,grit_resource r WHERE l.subject_id=? AND r.subject_sid=?", subjectId, resourceSpaceId)
//                .execute();

        db.table("l").from("grit_resource_linked l,grit_resource r")
                .whereEq("l.subject_id", subjectId)
                .and("l.resource_id=r.resource_id")
                .andEq("r.resource_sid", resourceSpaceId)
                .delete();
    }


    /**
     * 获取资源关联（仅自己的）
     */
    @Override
    public List<ResourceLinkedDo> getResourceLinkListBySubjectSlf(long subjectId) throws SQLException {
        return db.table("grit_resource_linked")
                .whereEq("subject_id", subjectId)
                .selectList("*", ResourceLinkedDo.class);
    }

    /**
     * 获取资源关联（自己的 + 继承的）
     */
    @Override
    public List<ResourceLinkedDo> getResourceLinkListBySubjectAll(long subjectId) throws SQLException {
        //获取实体相关的所有主体Id
        List<Long> subjectIds = getSubjectIdsByEntityOnAuth(subjectId);


        return db.table("grit_resource_linked")
                .whereIn("subject_id", subjectIds)
                .selectList("*", ResourceLinkedDo.class)
                .stream()
                .collect(collectingAndThen(
                        //去重
                        toCollection(() -> new TreeSet<>(Comparator.comparing(r -> r.resource_id)))
                        , ArrayList::new)
                );
    }

    @Override
    public boolean importSpaceSchema(String json) throws SQLException {
        if (Utils.isEmpty(json)) {
            return false;
        }

        //space
        ONode oNode = ONode.loadStr(json);

        ONode oSpace = oNode.getOrNull("space");
        if (oSpace == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        ResourceDo spaceD = oSpace.get("meta").toObject(ResourceDo.class);
        if (Utils.isEmpty(spaceD.guid)) {
            throw new IllegalArgumentException("Invalid space schema json");
        }
        spaceD.resource_sid = 0L;
        spaceD.resource_pid = 0L;
        if (synResourceByGuid(spaceD) == false) {
            //同步失则，表示格式不对
            throw new IllegalArgumentException("Invalid space schema json");
        }

        // groups
        ONode oGroups = oSpace.getOrNull("groups");
        if (oGroups == null) {
            throw new IllegalArgumentException("Invalid space schema json");
        }

        for (ONode oG1 : oGroups.ary()) {
            ResourceDo g1 = oG1.get("meta").toObject(ResourceDo.class);

            g1.resource_pid = spaceD.resource_id;
            g1.resource_sid = spaceD.resource_id;

            if (synResourceByGuid(g1) == false) {
                //同步失则，表示格式不对
                throw new IllegalArgumentException("Invalid space schema json");
            }

            List<ResourceDo> engitys = oG1.get("engitys").toObjectList(ResourceDo.class);

            for (ResourceDo e1 : engitys) {
                e1.resource_sid = g1.resource_sid;
                e1.resource_pid = g1.resource_id;

                if (synResourceByGuid(e1) == false) {
                    //同步失则，表示格式不对
                    throw new IllegalArgumentException("Invalid space schema json");
                }
            }
        }

        return false;
    }

    @Cache(seconds = 10)
    @Override
    public String exportSpaceSchema(long resourceSpaceId) throws SQLException {
        if(resourceSpaceId == 0){
            return "";
        }

        ONode oNode = new ONode();

        ResourceDo space = getResourceById(resourceSpaceId);
        List<ResourceGroup> groups = getResourceGroupListBySpace(resourceSpaceId);

        space.resource_id = null;
        space.resource_pid = null;
        space.resource_sid = null;

        ONode oSpace = oNode.getOrNew("space");
        oSpace.getOrNew("meta").fill(space);

        ONode oGroups = oSpace.getOrNew("groups").asArray();
        for (ResourceGroup g1 : groups) {
            List<Resource> engitys = getSubResourceListByPid(g1.resource_id);

            g1.resource_id = null;
            g1.resource_pid = null;
            g1.resource_sid = null;
            ONode oG1 = oGroups.addNew();
            oG1.getOrNew("meta").fill(g1);

            ONode oEngitys = oG1.getOrNew("engitys");
            for (Resource e1 : engitys) {
                e1.resource_id = null;
                e1.resource_pid = null;
                e1.resource_sid = null;

                oEngitys.addNew().fill(e1);
            }
        }

        return oNode.toJson();
    }

    /**
     * 获取实验验证时的所有主体Id
     */
    private List<Long> getSubjectIdsByEntityOnAuth(long subjectEntityId) throws SQLException {
        //获取所在组的主体id
        List<Long> subjectIds = subjectAdminService.getSubjectGroupIdListByEntity(subjectEntityId);

        //加上自己的主体id
        subjectIds.add(subjectEntityId);

        return subjectIds;
    }

    private List<Object> getIdList(String ids) {
        return Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Long.parseLong(s))
                .collect(Collectors.toList());
    }
}
