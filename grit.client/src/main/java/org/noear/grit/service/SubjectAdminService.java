package org.noear.grit.service;

import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;

import java.sql.SQLException;

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
    long addSubject(Subject subject) throws SQLException;

    /**
     * 添加主体，并关联分组
     *
     * @param subjectEntity  主体实体
     * @param groupSubjectId 分组的主体Id
     */
    long addSubjectEntity(SubjectEntity subjectEntity, long groupSubjectId) throws SQLException;

    /**
     * 更新主体
     *
     * @param subjectId 主体Id
     * @param subject   主体
     */
    boolean updSubject(long subjectId, Subject subject) throws SQLException;
}
