package org.noear.grit.client.impl;

import org.noear.grit.model.domain.*;
import org.noear.grit.service.SubjectLinkService;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 主体连接服务实现
 *
 * @author noear
 * @since 1.0
 */
public class SubjectLinkServiceImpl implements SubjectLinkService {
    private final DbContext db;
    private final ICacheService cache;

    public SubjectLinkServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }

    /**
     * 添加主体连接
     *
     * @param subjectId 主体Id
     * @param groupSubjectId 分组的主体Id
     * */
    @Override
    public long addSubjectLink(long subjectId, long groupSubjectId) throws SQLException {
        return db.table("grit_subject_linked")
                .set("subject_id", subjectId)
                .set("group_subject_id", groupSubjectId)
                .set("gmt_create", System.currentTimeMillis())
                .insert();
    }

    /**
     * 删除主体连接
     *
     * @param linkIds 主体Ids
     * */
    @Override
    public void delSubjectLink(long... linkIds) throws SQLException {
        db.table("grit_subject_linked")
                .whereIn("link_id", Arrays.asList(linkIds))
                .delete();
    }

    /**
     * 检测是否存在主体连接（一般用于角色检测）
     *
     * @param subjectId      主体Id
     * @param groupSubjectId 分组的主体Id
     * @return 是否存在
     */
    @Override
    public boolean hasSubjectLink(long subjectId, long groupSubjectId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .andEq("group_subject_id", groupSubjectId)
                .caching(cache)
                .selectExists();
    }


    /**
     * 获取主体分组关联的主体实体列表
     *
     * @param groupSubjectId 分组的主体Id
     * @return 主体列表
     */
    @Override
    public List<SubjectEntity> getSubjectEntityListByGroup(long groupSubjectId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("group_subject_id", groupSubjectId)
                .caching(cache)
                .selectList("*", SubjectEntity.class);

    }

    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    @Override
    public List<SubjectGroup> getSubjectGroupListByEntity(long subjectId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .caching(cache)
                .selectList("*", SubjectGroup.class);
    }



//    @Override
//    public List<ResourceGroup> getReourceGroupListByUser(long userId, long groupId) throws SQLException {
//        if (userId < 1 || groupId < 1L) {
//            return new ArrayList<>();
//        }
//
//        //1.找出我所有的资源
//        List<Integer> resourceIds = db.table("grit_resource r")
//                .innerJoin("grit_resource_linked rl").on("r.resource_id=rl.resource_id")
//                .where("rl.lk_objt_id=? AND rl.lk_objt=? AND r.is_branched=0 AND r.is_disabled=0", userId, Constants.GROUP_user_root_id)
//                .caching(cache)
//                .selectArray("r.resource_id");
//
//        if (resourceIds.size() == 0) {
//            return new ArrayList<>();
//        }
//
//        //2.找出资源相关的组id
//        List<Integer> groupIds = db.table("grit_resource_linked rl")
//                .where("rl.lk_objt=? AND rl.resource_id IN (?...)", Constants.OBJT_group, resourceIds)
//                .caching(cache)
//                .selectArray("DISTINCT rl.lk_objt_id");
//
//        if (groupIds.size() == 0) {
//            return new ArrayList<>();
//        }
//
//        //3.找出相关组的诚意情
//        return db.table("grit_group")
//                .where("group_id IN (?...) AND is_disabled=0 AND is_visibled=1", groupIds)
//                .andEq("group_pid", groupId)
//                .orderBy("Order_Index")
//                .caching(cache)
//                .selectList("*", ResourceGroup.class);
//
//    }
//
//    @Override
//    public List<Subject> getUserListByGroup(long groupId) throws SQLException {
//        List<Object> userIds = db.table("grit_subject_linked")
//                .whereEq("lk_objt", Constants.OBJT_group)
//                .andEq("lk_objt_id", groupId)
//                .caching(cache)
//                .selectArray("user_id");
//
//        return db.table("grit_user")
//                .whereIn("user_id", userIds)
//                .caching(cache)
//                .selectList("*", Subject.class);
//    }
}
