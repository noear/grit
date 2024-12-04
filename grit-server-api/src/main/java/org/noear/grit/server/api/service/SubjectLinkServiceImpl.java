package org.noear.grit.server.api.service;

import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.server.api.dso.AfterHandler;
import org.noear.grit.server.api.dso.BeforeHandler;
import org.noear.grit.service.SubjectLinkService;
import org.noear.solon.annotation.*;
import org.noear.wood.DbContext;
import org.noear.wood.cache.ICacheServiceEx;

import java.sql.SQLException;
import java.util.List;

/**
 * 主体连接服务实现
 *
 * @author noear
 * @since 1.0
 */
@Addition({BeforeHandler.class, AfterHandler.class})
@Mapping("/grit/api/v1/SubjectLinkService")
@Remoting
public class SubjectLinkServiceImpl implements SubjectLinkService {
    @Inject("grit.db")
    private DbContext db;
    @Inject("grit.cache")
    private ICacheServiceEx cache;


    /**
     * 检测是否存在主体连接（一般用于角色检测）
     *
     * @param subjectId      主体Id
     * @param subjectGroupId 分组的主体Id
     * @return 是否存在
     */
    @Override
    public boolean hasSubjectLink(long subjectId, long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .andEq("group_subject_id", subjectGroupId)
                .caching(cache)
                .selectExists();
    }


    /**
     * 获取主体分组关联的主体实体列表
     *
     * @param subjectGroupId 分组的主体Id
     * @return 主体列表
     */
    @Override
    public List<SubjectEntity> getSubjectEntityListByGroup(long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("group_subject_id", subjectGroupId)
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
        return db.table("grit_subject_linked l")
                .innerJoin("grit_subject s").onEq("l.subject_id", "s.group_subject_id")
                .andEq("l.subject_id", subjectId)
                .caching(cache)
                .selectList("s.*", SubjectGroup.class);
    }

    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    @Override
    public List<Long> getSubjectGroupIdListByEntity(long subjectId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .caching(cache)
                .selectArray("group_subject_id");
    }
}
