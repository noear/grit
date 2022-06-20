package org.noear.grit.server.dso.service.impl;

import org.noear.grit.client.GritUtil;
import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.model.type.SubjectType;
import org.noear.grit.server.dso.service.SubjectAdminService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.*;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.aspect.annotation.Service;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 主体管理服务实现
 *
 * @author noear
 * @since 1.0
 */
@Service
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
        if (subject.subject_type == SubjectType.entity.code) {
            subject.subject_pid = -1L;
        }

        if (Utils.isEmpty(subject.login_name)) {
            subject.login_name = Utils.guid();
        }

        if (Utils.isNotEmpty(subject.login_password)) {
            subject.login_password = GritUtil.buildPassword(subject.login_name, subject.login_password);
        } else {
            subject.login_password = null; //即不改
        }

        subject.gmt_create = System.currentTimeMillis();
        subject.gmt_modified = subject.gmt_create;

        if(Utils.isEmpty(subject.guid)){
            subject.guid = Utils.guid();
        }

        return db.table("grit_subject")
                .setEntity(subject)
                .usingNull(false)
                .usingExpr(false)
                .insert();
    }

    @Override
    public long addSubjectEntity(SubjectDo subject, long subjectGroupId) throws SQLException {
        long subjectEntityId = addSubject(subject);

        if (subjectGroupId > 0) {
            addSubjectLink(subjectEntityId, subjectGroupId);
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
        if (subject.subject_type == SubjectType.entity.code) {
            subject.subject_pid = -1L;
        }

        if (Utils.isEmpty(subject.login_name)) {
            subject.login_name = Utils.guid();
        }

        if (Utils.isNotEmpty(subject.login_password)) {
            subject.login_password = GritUtil.buildPassword(subject.login_name, subject.login_password);
        } else {
            subject.login_password = null; //即不改
        }

        subject.gmt_modified = System.currentTimeMillis();

        return db.table("grit_subject")
                .setEntity(subject)
                .usingNull(false)
                .usingExpr(false)
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
    public Subject getSubjectByGuid(String guid) throws SQLException {
        if (Utils.isEmpty(guid)) {
            return new Subject();
        }

        return db.table("grit_subject")
                .whereEq("guid", guid)
                .selectItem("*", Subject.class);
    }

    @Override
    public List<SubjectGroup> getGroupList() throws SQLException {
        return db.table("grit_subject")
                .whereEq("subject_type", SubjectType.group.code)
                .limit(200)
                .selectList("*", SubjectGroup.class);
    }

    @Override
    public List<SubjectEntity> getSubjectEntityListByAll() throws SQLException {
        return db.table("grit_subject")
                .whereEq("subject_type", SubjectType.entity.code)
                .limit(200)
                .selectList("*", SubjectEntity.class);
    }

    @Override
    public List<SubjectEntity> getSubjectEntityListByFind(String key) throws SQLException {
        String likeKey = "%" + key + "%";

        return db.table("grit_subject")
                .whereEq("subject_type", SubjectType.entity.code)
                .and("(login_name LIKE ? OR display_name LIKE ?)", likeKey, likeKey)
                .limit(200)
                .selectList("*", SubjectEntity.class);
    }

    @Override
    public List<SubjectEntity> getSubjectEntityListByGroup(long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked l")
                .innerJoin("grit_subject s")
                .on("l.subject_id=s.subject_id").andEq("l.group_subject_id", subjectGroupId)
                .selectList("*", SubjectEntity.class);
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
                .selectArray("group_subject_id");
    }

    /////////////////////////////

    /**
     * 添加主体连接
     *
     * @param subjectId      主体Id
     * @param subjectGroupId 分组的主体Id
     */
    @Override
    public long addSubjectLink(long subjectId, long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked")
                .set("subject_id", subjectId)
                .set("group_subject_id", subjectGroupId)
                .set("gmt_create", System.currentTimeMillis())
                .insert();
    }


    /**
     * 检查是否存在主体连接
     *
     * @param subjectId      主体Id
     * @param subjectGroupId 分组的主体Id
     */
    @Override
    public boolean hasSubjectLink(long subjectId, long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereEq("subject_id", subjectId)
                .andEq("group_subject_id", subjectGroupId)
                .selectExists();
    }

    /**
     * 删除主体连接
     *
     * @param subjectIds     主体Ids
     * @param subjectGroupId 分组的主体Id
     */
    @Override
    public boolean delSubjectLinkBySubjects(Collection<Long> subjectIds, long subjectGroupId) throws SQLException {
        return db.table("grit_subject_linked")
                .whereIn("subject_id", subjectIds)
                .andEq("group_subject_id", subjectGroupId)
                .delete() > 0;
    }
}
