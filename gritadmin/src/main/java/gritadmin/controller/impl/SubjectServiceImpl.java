package gritadmin.controller.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.service.SubjectService;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Remoting;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;

/**
 * 主体服务实现
 *
 * @author noear
 * @since 1.0
 */
@Mapping("/api/v1/SubjectService")
@Remoting
public class SubjectServiceImpl implements SubjectService {
    @Db("grit.db")
    private  DbContext db;
    @Inject("grit.cache")
    private  ICacheService cache;

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

    /**
     * 检测主体是否存在
     *
     * @param loginName 登录名
     */
    @Override
    public boolean hesSubjectByLoginName(String loginName) throws SQLException {
        if (TextUtils.isEmpty(loginName)) {
            return false;
        }

        return db.table("grit_subject")
                .whereEq("login_name", loginName)
                .selectExists();
    }

    /**
     * 获取主体
     *
     * @param loginName 登录名
     */
    @Override
    public Subject getSubjectByLoginName(String loginName) throws SQLException {
        if (TextUtils.isEmpty(loginName)) {
            return new Subject();
        }

        return db.table("grit_subject")
                .whereEq("login_name", loginName)
                .limit(1)
                .caching(cache)
                .selectItem("*", Subject.class);
    }

    /**
     * 获取主体根据登录名与密码（用于登录）
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    @Override
    public Subject getSubjectByLoginNameAndPassword(String loginName, String loginPassword) throws SQLException {
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPassword)) {
            return new Subject();
        }

        String loginPasswordHash = GritUtil.buildPassword(loginName, loginPassword);

        return db.table("grit_subject")
                .whereEq("login_name", loginName)
                .andEq("login_password", loginPasswordHash)
                .andEq("is_disabled", 0)
                .selectItem("*", Subject.class);
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
                .caching(cache)
                .selectItem("*", Subject.class);
    }

    /**
     * 获取主体
     *
     * @param subjectCode 主体代号
     */
    @Override
    public Subject getSubjectByCode(String subjectCode) throws SQLException {
        if (TextUtils.isEmpty(subjectCode)) {
            return new Subject();
        }

        return db.table("grit_subject")
                .whereEq("subject_code", subjectCode)
                .limit(1)
                .caching(cache)
                .selectItem("*", Subject.class);
    }



    /**
     * 修改主体密码（需要登录密验证）
     *
     * @param loginName        用户登录名
     * @param loginPassword    登录密码
     * @param newLoginPassword 新的登录密码
     * @return 0表示参数有误或修改失败；1表示用户不存在或密码不对；2表示修改成功
     */
    @Override
    public int modSubjectPassword(String loginName, String loginPassword, String newLoginPassword) throws SQLException {
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPassword) || TextUtils.isEmpty(newLoginPassword)) {
            return 0;
        }

        Subject subject = getSubjectByLoginNameAndPassword(loginName, loginPassword);

        if (subject.subject_id > 0) {
            String loginNewPasswordHash = GritUtil.buildPassword(loginName, newLoginPassword);

            return db.table("grit_subject")
                    .set("login_password", loginNewPasswordHash)
                    .whereEq("subject_id", subject.subject_id)
                    .update() > 0 ? 2 : 0;
        } else {
            //用户不存在；或密码不对；
            return 1;
        }
    }

    /**
     * 设置主体密码
     *
     * @param loginName        用户登录名
     * @param newLoginPassword 新的登录密码
     * @return 0,表示参数有误或修改失败；1,表示用户不存在；2,表示修改成功
     */
    @Override
    public int setSubjectPassword(String loginName, String newLoginPassword) throws SQLException {
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(newLoginPassword)) {
            return 0;
        }

        Subject subject = getSubjectByLoginName(loginName);


        if (subject.subject_id > 0) {
            String loginNewPasswordHash = GritUtil.buildPassword(loginName, newLoginPassword);

            return db.table("grit_subject")
                    .set("login_password", loginNewPasswordHash)
                    .whereEq("subject_id", subject.subject_id)
                    .update() > 0 ? 2 : 0;
        } else {
            //用户不存在；或密码不对；
            return 1;
        }
    }

    /**
     * 设置主体的禁用状态
     *
     * @param subjectId 主体Id
     * @param disabled  是否禁用
     * @return 影响行数
     */
    @Override
    public int setSubjectDisabled(long subjectId, boolean disabled) throws SQLException {
        if (subjectId < 1) {
            return 0;
        }

        return db.table("grit_subject")
                .set("is_disabled", (disabled ? 1 : 0))
                .whereEq("subject_id", subjectId)
                .update();
    }

    /**
     * 设置主体的可见状态
     *
     * @param subjectId 主体Id
     * @param visibled  是否可见
     * @return 影响行数
     */
    @Override
    public int setSubjectVisibled(long subjectId, boolean visibled) throws SQLException {
        if (subjectId < 1) {
            return 0;
        }

        return db.table("grit_subject")
                .set("is_visibled", (visibled ? 1 : 0))
                .whereEq("subject_id", subjectId)
                .update();
    }

    /**
     * 设置主体的特性
     *
     * @param subjectId 主体Id
     * @param attrs     特性
     * @return 影响行数
     */
    @Override
    public int setSubjectAttrs(long subjectId, String attrs) throws SQLException {
        if (subjectId < 1) {
            return 0;
        }

        if (attrs == null) {
            attrs = "";
        }

        return db.table("grit_subject")
                .set("attrs", attrs)
                .whereEq("subject_id", subjectId)
                .update();
    }
}
