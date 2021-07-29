package org.noear.grit.client;

import org.noear.grit.client.impl.*;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.grit.client.model.User;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author noear
 * @since 1.0
 * */
public class StoneClient {
    private static UserService userService;
    private static GroupService groupService;
    private static ResourceService resourceService;
    private static String branchedGroupCode;
    private static long branchedGroupId;
    private static Group branchedGroup;

    /**
     * 初始化
     */
    public static void init(DbContext db, ICacheService cache) {
        userService = new UserServiceImpl(db, cache);
        groupService = new GroupServiceImpl(db, cache);
        resourceService = new ResourceServiceImpl(db, cache);
    }

    /**
     * 隔离分组
     * */
    public static void branched(String groupCode) {
        if (groupCode == null || groupCode.equals(branchedGroupCode)) {
            return;
        }

        try {
            branchedGroup = group().getGroupByCode(groupCode);
            branchedGroupId = branchedGroup.group_id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 用户接口
     */
    public static UserService user() {
        return userService;
    }

    /**
     * 分组接口
     */
    public static GroupService group() {
        return groupService;
    }

    /**
     * 资源接口
     */
    public static ResourceService resource() {
        return resourceService;
    }

    /**
     * 用户登录
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    public static User login(String loginName, String loginPassword) throws SQLException {
        return userService.login(loginName, loginPassword);
    }


    /**
     * 用户是否有路径
     * */
    public static boolean userHasPath(long userId, String path) {
        return false;
    }

    /**
     * 用户是否有权限
     * */
    public static boolean userHasPermission(long userId, String resourceCode) {
        return false;
    }


    /**
     * 用户是否有角色
     * */
    public static boolean userHasRole(long userId, String roleCode) {
        return false;
    }


    /**
     * 获取用户所有权限
     * */
    public static List<Resource> getUserPermissions(long userId) throws SQLException {
        return null;
    }

    /**
     * 获取用户所有角色
     * */
    public static List<Group> getUserRoles(long userId) throws SQLException {
        return user().getGroupListByUser(userId);
    }


    /**
     * 获取用户菜单
     * */
    public static List<Resource> getUserMenus(long userId, String groupCode) throws SQLException {
        Group group = groupService.getGroupByCode(groupCode);
        return getUserMenus(userId, group.group_id);
    }

    /**
     * 获取用户菜单
     * */
    public static List<Resource> getUserMenus(long userId, long groupId) throws SQLException {
        return null;
    }

    /**
     * 获取用户菜单分组
     * */
    public static Resource getUserMenusFirst(long userId, long groudId) throws SQLException {
        return null;
    }

    /**
     * 获取用户菜单分组
     * */
    public static List<Group> getUserModules(long userId, String groupCode) throws SQLException {
        Group group = groupService.getGroupByCode(groupCode);
        return getUserModules(userId, group.group_id);
    }

    /**
     * 获取用户菜单分组
     * */
    public static List<Group> getUserModules(long userId, long groupId) throws SQLException {
        return null;
    }

    /**
     * 获取用户菜单分组
     * */
    public static List<Group> getUserModules(long userId) throws SQLException {
        return null;
    }
}
