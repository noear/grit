package org.noear.grit.server.impl;

import org.noear.grit.client.GritClient;
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
    public long addSubject(Subject subject) throws SQLException {
        return db.table("grit_subject")
                .setEntity(subject).usingNull(false)
                .insert();
    }

    @Override
    public long addSubjectEntity(SubjectEntity subjectEntity, long groupSubjectId) throws SQLException {
        long subjectEntityId = db.table("grit_subject")
                .setEntity(subjectEntity).usingNull(false)
                .insert();

        GritClient.global().subjectLink().addSubjectLink(subjectEntityId, groupSubjectId);

        return subjectEntityId;
    }

    /**
     * 更新主体
     *
     * @param subjectId 主体Id
     * @param subject   主体
     */
    @Override
    public boolean updSubject(long subjectId, Subject subject) throws SQLException {
        return db.table("grit_subject")
                .setEntity(subject).usingNull(false)
                .whereEq("subject_id", subjectId)
                .update() > 0;
    }

    @Override
    public List<SubjectGroup> getGroupList() throws SQLException {
        return db.table("grit_subject")
                .whereEq("subject_type", SubjectType.group.code)
                .selectList("*", SubjectGroup.class);
    }

    @Override
    public List<Subject> getSubSubjectListByPid(long subjectId) throws SQLException {
        return db.table("grit_subject")
                .whereEq("subject_pid", subjectId)
                .selectList("*", Subject.class);
    }
}
