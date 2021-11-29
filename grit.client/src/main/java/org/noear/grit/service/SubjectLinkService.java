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
     * 检测是否存在主体连接（一般用于角色检测）
     *
     * @param subjectId      主体Id
     * @param subjectGroupId 分组的主体Id
     * @return 是否存在
     */
    boolean hasSubjectLink(long subjectId, long subjectGroupId) throws SQLException;



    /**
     * 获取主体分组关联的主体实体列表
     *
     * @param subjectGroupId 分组的主体Id
     * @return 主体列表
     */
    List<SubjectEntity> getSubjectEntityListByGroup(long subjectGroupId) throws SQLException;


    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    List<SubjectGroup> getSubjectGroupListByEntity(long subjectId) throws SQLException;

    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    List<Long> getSubjectGroupIdListByEntity(long subjectId) throws SQLException;
}
