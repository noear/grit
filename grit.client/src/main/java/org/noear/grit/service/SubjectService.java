package org.noear.grit.service;

import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectEntity;

import java.sql.SQLException;

/**
 * 主体服务
 *
 * @author noear
 * @since 1.0
 */
public interface SubjectService {

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

    /**
     * 检测主体是否存在
     *
     * @param loginName 登录名
     */
    boolean hesSubjectByLoginName(String loginName) throws SQLException;

    /**
     * 获取主体
     *
     * @param loginName 登录名
     */
    Subject getSubjectByLoginName(String loginName) throws SQLException;

    /**
     * 获取主体根据登录名与密码（用于登录）
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    Subject getSubjectByLoginNameAndPassword(String loginName, String loginPassword) throws SQLException;


    /**
     * 获取主体
     *
     * @param subjectId 主体Id
     */
    Subject getSubjectById(long subjectId) throws SQLException;


    /**
     * 获取主体
     *
     * @param subjectCode 主体代号
     */
    Subject getSubjectByCode(String subjectCode) throws SQLException;


    /**
     * 修改主体密码（需要登录密验证）
     *
     * @param loginName        用户登录名
     * @param loginPassword    登录密码
     * @param newLoginPassword 新的登录密码
     * @return 0表示参数有误或修改失败；1表示用户不存在或密码不对；2表示修改成功
     */
    int modSubjectPassword(String loginName, String loginPassword, String newLoginPassword) throws SQLException;

    /**
     * 设置主体密码
     *
     * @param loginName        用户登录名
     * @param newLoginPassword 新的登录密码
     * @return 0, 表示参数有误或修改失败；1,表示用户不存在；2,表示修改成功
     */
    int setSubjectPassword(String loginName, String newLoginPassword) throws SQLException;


    /**
     * 设置主体的禁用状态
     *
     * @param subjectId 主体Id
     * @param disabled  是否禁用
     * @return 影响行数
     */
    int setSubjectDisabled(long subjectId, boolean disabled) throws SQLException;

    /**
     * 设置主体的可见状态
     *
     * @param subjectId 主体Id
     * @param visibled  是否可见
     * @return 影响行数
     */
    int setSubjectVisibled(long subjectId, boolean visibled) throws SQLException;

    /**
     * 设置主体的特性
     *
     * @param subjectId 主体Id
     * @param attrs     特性
     * @return 影响行数
     */
    int setSubjectAttrs(long subjectId, String attrs) throws SQLException;
}
