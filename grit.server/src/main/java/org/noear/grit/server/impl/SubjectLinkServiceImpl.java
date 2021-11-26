package org.noear.grit.server.impl;

import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.SubjectLinkService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
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
@Before(BeforeHandler.class)
@Mapping("/grit/api/SubjectLinkService")
@Remoting
public class SubjectLinkServiceImpl implements SubjectLinkService {
    @Inject("grit.db")
    private  DbContext db;
    @Inject("grit.cache")
    private  ICacheService cache;

    /**
     * 添加主体连接
     *
     * @param subjectId      主体Id
     * @param groupSubjectId 分组的主体Id
     */
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
     */
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
}
