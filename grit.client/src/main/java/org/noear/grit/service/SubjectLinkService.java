package org.noear.grit.service;

import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;

import java.sql.SQLException;
import java.util.List;

/**
 * 主体连接服务
 *
 * @author noear
 * @since 1.0
 */
public interface SubjectLinkService {

    /**
     * 添加主体连接
     *
     * @param subjectId      主体Id
     * @param groupSubjectId 分组的主体Id
     */
    long addSubjectLink(long subjectId, long groupSubjectId) throws SQLException;

    /**
     * 删除主体连接
     *
     * @param linkIds 主体Ids
     */
    void delSubjectLink(long... linkIds) throws SQLException;

    /**
     * 检测是否存在主体连接（一般用于角色检测）
     *
     * @param subjectId      主体Id
     * @param groupSubjectId 分组的主体Id
     * @return 是否存在
     */
    boolean hasSubjectLink(long subjectId, long groupSubjectId) throws SQLException;


    /**
     * 获取主体分组关联的主体实体列表
     *
     * @param groupSubjectId 分组的主体Id
     * @return 主体列表
     */
    List<SubjectEntity> getSubjectEntityListByGroup(long groupSubjectId) throws SQLException;


    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    List<SubjectGroup> getSubjectGroupListByEntity(long subjectId) throws SQLException;
}
