package org.noear.grit.client.impl;

import org.noear.snack.ONode;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户服务
 *
 * @author noear
 * @since 1.0
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    User login(String loginName, String loginPassword) throws SQLException;

    /**
     * 用户检测是否存在
     *
     * @param loginName 登录名
     */
    boolean hesLoginname(String loginName) throws SQLException;

    /**
     * 用户获取
     *
     * @param userId 用户Id
     */
    User getUserById(long userId) throws SQLException;

    /**
     * 用户获取
     *
     * @param loginName 登录名
     */
    User getUserByLoginName(String loginName) throws SQLException;

    /**
     * 用户元信息获取
     * */
    ONode getUserMeta(long userId) throws SQLException;

    /**
     * 用户密码修改
     *
     * @param loginName        用户登录名
     * @param loginPassword    登录密码
     * @param newLoginPassword 新的登录密码
     * @return 影响行数
     */
    int modUserPassword(String loginName, String loginPassword, String newLoginPassword) throws SQLException;

    /**
     * 用户的通用状态设置
     *
     * @param userId 用户Id
     * @param state  状态值
     * @return 影响行数
     */
    int setUserState(long userId, int state) throws SQLException;

    /**
     * 用户的禁用状态设置
     *
     * @param userId   用户Id
     * @param disabled 是否禁用
     * @return 影响行数
     */
    int setUserDisabled(long userId, boolean disabled) throws SQLException;

    /**
     * 用户的可见状态设置
     *
     * @param userId   用户Id
     * @param visibled 是否可见
     * @return 影响行数
     */
    int setUserVisibled(long userId, boolean visibled) throws SQLException;

    /**
     * 用户分组是否存在（一般用于角色检测）
     *
     * @param userId    用户Id
     * @param groupCode 分组代号
     * @return 是否存在
     */
    boolean userHasGroupCode(long userId, String groupCode) throws SQLException;

    /**
     * 用户资源是否存在
     *
     * @param userId 用户Id
     * @param uri    资源路径
     * @return 是否存在
     */
    boolean userHasReourceUri(long userId, String uri) throws SQLException;

    /**
     * 用户资源是否存在
     *
     * @param userId      用户Id
     * @param reourceCode 资源代号
     * @return 是否存在
     */
    boolean userHasReourceCode(long userId, String reourceCode) throws SQLException;

    /**
     * 用户所在的分组获取（一般用于角色检测）
     *
     * @param userId 用户Id
     * @return 分组列表
     */
    List<Group> getGroupListByUser(long userId) throws SQLException;


    /**
     * 用户授权的资源分组获取
     *
     * @param userId    用户Id
     * @param groupCode 分组代号
     * @return 分组列表
     */
    List<Group> getReourceGroupListByUser(long userId, String groupCode) throws SQLException;


    /**
     * 用户授权的资源分组获取
     *
     * @param userId  用户Id
     * @param groupId 分组Id
     * @return 分组列表
     */
    List<Group> getReourceGroupListByUser(long userId, long groupId) throws SQLException;


    List<User> getUserListByGroup(long groupId) throws SQLException;
}
