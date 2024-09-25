package org.noear.grit.server.controller;

import org.noear.grit.client.GritUtil;
import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.server.dso.AfterHandler;
import org.noear.grit.server.dso.BeforeHandler;
import org.noear.grit.service.SubjectService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.*;
import org.noear.wood.DbContext;
import org.noear.wood.cache.ICacheServiceEx;

import java.sql.SQLException;

/**
 * 主体服务实现
 *
 * @author noear
 * @since 1.0
 */
@Addition({BeforeHandler.class, AfterHandler.class})
@Mapping("/grit/api/v1/SubjectService")
@Remoting
public class SubjectServiceImpl implements SubjectService {
    @Inject("grit.db")
    private  DbContext db;
    @Inject("grit.cache")
    private ICacheServiceEx cache;


    /**
     * 检测主体是否存在
     *
     * @param loginName 登录名
     */
    @Override
    public boolean hasSubjectByLoginName(String loginName) throws SQLException {
        if (TextUtils.isEmpty(loginName)) {
            return false;
        }

        return db.table("grit_subject")
                .whereEq("login_name", loginName)
                .limit(1)
                .selectExists();
    }

    @Override
    public boolean hasSubjectByCode(String subjectCode) throws SQLException {
        if (TextUtils.isEmpty(subjectCode)) {
            return false;
        }

        return db.table("grit_subject")
                .whereEq("subject_code", subjectCode)
                .caching(cache)
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
                .log(true)
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

    @Override
    public long regSubject(String loginName, String loginPassword, String displayName) throws SQLException {
        SubjectDo subject = new SubjectDo();
        subject.subject_pid = -1L;
        subject.login_name = loginName;
        subject.login_password = loginPassword;

        if (Utils.isNotEmpty(subject.login_password)) {
            subject.login_password = GritUtil.buildPassword(subject.login_name, subject.login_password);
        } else {
            subject.login_password = ""; //即不改
        }

        subject.gmt_create = System.currentTimeMillis();
        subject.gmt_modified = subject.gmt_create;

        return db.table("grit_subject")
                .setEntity(subject)
                .usingNull(false)
                .usingExpr(false)
                .insert();
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
