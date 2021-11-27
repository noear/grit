package org.noear.grit.service;

import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
public interface SubjectAdminService {
    /**
     * 添加主体
     *
     * @param subject 主体
     */
    long addSubject(SubjectDo subject) throws SQLException;

    /**
     * 添加主体，并关联分组
     *
     * @param subjectEntity  主体实体
     * @param groupSubjectId 分组的主体Id
     */
    long addSubjectEntity(SubjectDo subject, long groupSubjectId) throws SQLException;

    /**
     * 更新主体
     *
     * @param subjectId 主体Id
     * @param subject   主体
     */
    boolean updSubjectById(long subjectId, SubjectDo subject) throws SQLException;

    /**
     * 删除主体
     *
     * @param subjectId 主体Id
     */
    boolean delSubjectById(long subjectId) throws SQLException;


    /**
     * 获取主体
     *
     * @param subjectId 主体Id
     */
    Subject getSubjectById(long subjectId) throws SQLException;


    /**
     * 获取管理用的资源空间列表
     */
    List<SubjectGroup> getGroupList() throws SQLException;

    /**
     * 获取管理用的资源空间列表
     */
    List<SubjectEntity> getSubjectEntityListByGroup(long groupSubjectId) throws SQLException;
}
