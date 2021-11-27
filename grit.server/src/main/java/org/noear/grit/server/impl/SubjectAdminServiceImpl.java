package org.noear.grit.server.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.model.type.SubjectType;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.SubjectAdminService;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.solon.data.annotation.Tran;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear 2021/11/27 created
 */
@Before(BeforeHandler.class)
@Mapping("/grit/api/SubjectAdminService")
@Remoting
public class SubjectAdminServiceImpl implements SubjectAdminService {
    @Inject("grit.db")
    private DbContext db;
    @Inject("grit.cache")
    private ICacheService cache;

    /**
     * 添加主体
     *
     * @param subject 主体
     */
    @Override
    public long addSubject(SubjectDo subject) throws SQLException {
        if(subject.subject_type == SubjectType.entity.code){
            subject.subject_pid = -1L;
        }

        return db.table("grit_subject")
                .setEntity(subject).usingNull(false)
                .insert();
    }

    @Override
    public long addSubjectEntity(SubjectDo subject, long groupSubjectId) throws SQLException {
        if(subject.subject_type == SubjectType.entity.code){
            subject.subject_pid = -1L;
        }

        long subjectEntityId = db.table("grit_subject")
                .setEntity(subject).usingNull(false)
                .insert();

        if (groupSubjectId > 0) {
            GritClient.global().subjectLink().addSubjectLink(subjectEntityId, groupSubjectId);
        }

        return subjectEntityId;
    }

    /**
     * 更新主体
     *
     * @param subjectId 主体Id
     * @param subject   主体
     */
    @Override
    public boolean updSubjectById(long subjectId, SubjectDo subject) throws SQLException {
        if(subject.subject_type == SubjectType.entity.code){
            subject.subject_pid = -1L;
        }

        return db.table("grit_subject")
                .setEntity(subject).usingNull(false)
                .whereEq("subject_id", subjectId)
                .update() > 0;
    }

    @Tran
    @Override
    public boolean delSubjectById(long subjectId) throws SQLException {
        boolean isOk = db.table("grit_subject")
                .whereEq("subject_id", subjectId)
                .delete() > 0;

        db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .orEq("group_subject_id", subjectId)
                .delete();

        db.table("grit_resource_linked")
                .whereEq("subject_id", subjectId)
                .delete();

        return isOk;
    }

    /**
     * 获取主体
     *
     * @param subjectId 主体Id
     */
    @Override
    public Subject getSubjectById(long subjectId) throws SQLException {
        if (subjectId < 1) {
            return new Subject();
        }

        return db.table("grit_subject")
                .whereEq("subject_id", subjectId)
                .selectItem("*", Subject.class);
    }

    @Override
    public List<SubjectGroup> getGroupList() throws SQLException {
        return db.table("grit_subject")
                .whereEq("subject_type", SubjectType.group.code)
                .selectList("*", SubjectGroup.class);
    }

    @Override
    public List<SubjectEntity> getSubjectEntityListByGroup(long groupSubjectId) throws SQLException {
        return db.table("grit_subject_linked l")
                .innerJoin("grit_subject s")
                .on("l.subject_id=s.subject_id").andEq("l.group_subject_id", groupSubjectId)
                .selectList("*", SubjectEntity.class);
    }
}
