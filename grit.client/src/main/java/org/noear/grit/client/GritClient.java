package org.noear.grit.client;

import org.noear.grit.client.impl.*;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import org.noear.grit.client.model.User;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noear
 * @since 1.0
 * */
public class GritClient {
    private static UserService userService;
    private static GroupService groupService;
    private static ResourceService resourceService;
    private static BranchService branchService;
    private static String branchedGroupCode;
    private static long branchedGroupId;
    private static Group branchedGroup;

    private static DbContext db;
    private static ICacheService cache;

    /**
     * 初始化
     */
    public static void init(DbContext db, ICacheService cache) {
        GritClient.db = db;
        GritClient.cache = cache;


        groupService = new GroupServiceImpl(db, cache);
        branchService = new BranchServiceImpl(db, cache);

        userService = new UserServiceImpl(db, cache);
        resourceService = new ResourceServiceImpl(db, cache);
    }

    /**
     * 隔离分组
     */
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
     * 分支组接口
     * */
    public static BranchService branched(){
        return branchService;
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
     */
    public static boolean userHasPath(long userId, String path) {
        return false;
    }

    /**
     * 用户是否有权限
     */
    public static boolean userHasPermission(long userId, String resourceCode) {
        return false;
    }


    /**
     * 用户是否有角色
     */
    public static boolean userHasRole(long userId, String roleCode) {
        return false;
    }


    /**
     * 获取用户所有权限
     */
    public static List<Resource> getUserPermissions(long userId) throws SQLException {
        return null;
    }

    /**
     * 获取用户所有角色
     */
    public static List<Group> getUserRoles(long userId) throws SQLException {
        return user().getGroupListByUser(userId);
    }


    /**
     * 获取用户菜单
     */
    public static List<Resource> getUserMenus(long userId, String groupCode) throws SQLException {
        Group group = groupService.getGroupByCode(groupCode);
        return getUserMenus(userId, group.group_id);
    }

    /**
     * 获取用户菜单
     */
    public static List<Resource> getUserMenus(long userId, long groupId) throws SQLException {
        //1.找出这个包下的资源id
        List<Integer> ids = db.table("grit_resource_linked rl")
                .where("rl.lk_objt = ? AND rl.lk_objt_id = ?", Constants.OBJT_group, groupId)
                .caching(cache)
                .selectArray("rl.resource_id");

        if (ids.size() == 0) {
            return new ArrayList<>();
        }

        //2.找出在某个包下的我的资源
        return db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id = rl.resource_id")
                .where("rl.lk_objt_id=? AND rl.lk_objt=? AND r.is_disabled=0 AND r.resource_id <> '' AND r.resource_id IN(?...)", userId, Constants.OBJT_user, ids)
                .orderBy("r.Order_Index ASC")
                .caching(cache)
                .selectList("r.*", Resource.class);
    }

    /**
     * 获取用户菜单分组
     */
    public static Resource getUserMenusFirstOfModule(long userId, long groupId) throws SQLException {
        //1.找出这个包下的资源id
        List<Object> ids = db.table("grit_resource_linked rl")
                .where("rl.lk_objt = ? AND rl.lk_objt_id = ?", Constants.OBJT_group, groupId)
                .caching(cache)
                .selectArray("rl.resource_id");

        if (ids.size() == 0) {
            return new Resource();
        }

        //2.找出在某个包下的我的资源
        return db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id = rl.resource_id")
                .whereEq("rl.lk_objt_id", userId)
                .andEq("rl.lk_objt", Constants.OBJT_user)
                .andNeq("r.link_uri", "")
                .andIn("r.resource_id", ids)
                .orderBy("r.Order_Index ASC")
                .limit(1)
                .caching(cache)
                .selectItem("r.*", Resource.class);
    }

    public static Resource getUserMenusFirstOfBranched(long userId) throws SQLException {
        return getUserMenusFirstOfBranched(userId, branchedGroupId);
    }

    public static Resource getUserMenusFirstOfBranched(long userId, long groupId) throws SQLException {
        List<Group> groupList = getUserModules(userId, groupId);
        for (Group group : groupList) {
            Resource res = getUserMenusFirstOfModule(userId, group.group_id);

            if (TextUtils.isEmpty(res.link_uri) == false) {
                return res;
            }
        }

        return new Resource();

    }



    /**
     * 获取用户菜单分组
     */
    public static List<Group> getUserModules(long userId, String groupCode) throws SQLException {
        Group group = groupService.getGroupByCode(groupCode);
        return getUserModules(userId, group.group_id);
    }


    /**
     * 获取用户菜单分组
     */
    public static List<Group> getUserModules(long userId) throws SQLException {
        return getUserModules(userId, branchedGroupId);
    }

    /**
     * 获取用户菜单分组
     */
    public static List<Group> getUserModules(long userId, long groupId) throws SQLException {

        //1.找出我所有的资源(注意uri_path<>'')
        List<Integer> rids = db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id = rl.resource_id")
                .where("rl.lk_objt_id=? AND rl.lk_objt=? AND r.link_uri<>'' AND r.is_disabled=0", userId, Constants.OBJT_user)
                .caching(cache)
                .selectArray("r.resource_id");

        if (rids.size() == 0) {
            return new ArrayList<>();
        }

        //2.找出资源相关的组id
        List<Integer> pids = db.table("grit_resource_linked rl")
                .where("rl.lk_objt=? AND rl.resource_id IN (?...)", Constants.OBJT_group, rids)
                .caching(cache)
                .selectArray("DISTINCT rl.lk_objt_id");

        if (pids.size() == 0) {
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db.table("grit_group")
                .where("group_id IN (?...) AND is_disabled=0 AND is_visibled=1", pids)
                .build((tb) -> {
                    if (groupId > 0) {
                        tb.and("group_parent_id=?", groupId);
                    }
                })
                .orderBy("order_index")
                .caching(cache)
                .selectList("*", Group.class);
    }
}
