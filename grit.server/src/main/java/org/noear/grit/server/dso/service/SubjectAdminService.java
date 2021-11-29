package org.noear.grit.server.dso.service;

import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;

import java.sql.SQLException;
import java.util.List;

/**
 * 主体管理服务
 *
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
     * @param subject  主体实体
     * @param subjectGroupId 分组的主体Id
     */
    long addSubjectEntity(SubjectDo subject, long subjectGroupId) throws SQLException;

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
     * 获取管理用的资源实体列表
     */
    List<SubjectEntity> getSubjectEntityListByAll() throws SQLException;

    /**
     * 获取管理用的资源实体列表
     *
     * @param subjectGroupId 主体组Id
     */
    List<SubjectEntity> getSubjectEntityListByGroup(long subjectGroupId) throws SQLException;

    /**
     * 获取主体实体关联的主体分组列表
     *
     * @param subjectId 主体Id
     * @return 主体列表
     */
    List<Long> getSubjectGroupIdListByEntity(long subjectId) throws SQLException;


    //////////////////////

    /**
     * 添加主体连接
     *
     * @param subjectId      主体Id
     * @param subjectGroupId 分组的主体Id
     */
    long addSubjectLink(long subjectId, long subjectGroupId) throws SQLException;

    /**
     * 删除主体连接
     *
     * @param linkIds 主体Ids
     */
    void delSubjectLink(long... linkIds) throws SQLException;
}
