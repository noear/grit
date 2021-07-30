package org.noear.grit.client.impl;

import org.noear.snack.ONode;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.User;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现
 *
 * @author noear
 * @since 1.0
 */
public class UserServiceImpl implements UserService{
    private final DbContext db;
    private final ICacheService cache;

    public UserServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }

    @Override
    public boolean hesLoginname(String loginName) throws SQLException {
        if (TextUtils.isEmpty(loginName)) {
            return false;
        }

        return db.table("grit_user")
                .whereEq("login_name", loginName)
                .selectExists();
    }

    @Override
    public User getUserById(long userId) throws SQLException {
        if (userId < 1) {
            return new User();
        }

        return db.table("grit_user")
                .whereEq("user_id", userId)
                .caching(cache)
                .selectItem("*", User.class);
    }

    @Override
    public User getUserByLoginName(String loginName) throws SQLException {
        if (TextUtils.isEmpty(loginName)) {
            return new User();
        }

        return db.table("grit_user")
                .whereEq("login_name", loginName)
                .caching(cache)
                .selectItem("*", User.class);
    }

    @Override
    public ONode getUserMeta(long userId) throws SQLException {
        String meta = getUserById(userId).attributes;
        return ONode.loadStr(meta);
    }

    @Override
    public User login(String loginName, String loginPassword) throws SQLException {
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPassword)) {
            return new User();
        }

        String loginPasswordHash = GritUtil.buildPassword(loginName, loginPassword);

        return db.table("grit_user")
                .whereEq("login_name", loginName)
                .andEq("login_password", loginPasswordHash)
                .andEq("is_disabled", 0)
                .selectItem("*", User.class);
    }

    @Override
    public int modUserPassword(String loginName, String loginPassword, String newLoginPassword) throws SQLException {
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPassword) || TextUtils.isEmpty(newLoginPassword)) {
            return 0;
        }

        User user = login(loginName, loginPassword);

        if (user.user_id > 0) {
            String loginNewPasswordHash = GritUtil.buildPassword(loginName, newLoginPassword);

            return db.table("grit_user")
                    .set("login_password", loginNewPasswordHash)
                    .whereEq("user_id", user.user_id)
                    .update() > 0 ? 2 : 0;
        } else {
            //用户不存在；或密码不对；
            return 1;
        }
    }

    @Override
    public int setUserState(long userId, int state) throws SQLException {
        if (userId < 1) {
            return 0;
        }

        return db.table("grit_user")
                .set("state", state)
                .whereEq("user_id", userId)
                .update();
    }

    @Override
    public int setUserDisabled(long userId, boolean disabled) throws SQLException {
        if (userId < 1) {
            return 0;
        }

        return db.table("grit_user")
                .set("is_disabled", (disabled ? 1 : 0))
                .whereEq("user_id", userId)
                .update();
    }

    @Override
    public int setUserVisibled(long userId, boolean visibled) throws SQLException {
        if (userId < 1) {
            return 0;
        }

        return db.table("grit_user")
                .set("is_visibled", (visibled ? 1 : 0))
                .whereEq("user_id", userId)
                .update();
    }

    @Override
    public boolean userHasGroupCode(long userId, String groupCode) throws SQLException {
        if (userId < 1 || TextUtils.isEmpty(groupCode)) {
            return false;
        }

        Group group = GritClient.group().getGroupByCode(groupCode);

        if (group.group_id < 1) {
            return false;
        }

        return db.table("grit_user_linked")
                .whereEq("user_id", userId)
                .andEq("lk_objt", Constants.OBJT_group)
                .andEq("lk_objt_id", group.group_id)
                .limit(1)
                .caching(cache)
                .selectExists();
    }

    @Override
    public boolean userHasReourceUri(long userId, String uri) throws SQLException {
        if (userId < 1 || TextUtils.isEmpty(uri)) {
            return false;
        }

        return db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id=rl.resource_id")
                .whereEq("rl.lk_objt_id", userId)
                .andEq("rl.lk_objt", Constants.OBJT_user)
                .andEq("r.link_uri", uri)
                .andEq("r.is_disabled", 0)
                .limit(1)
                .caching(cache)
                .selectValue("r.resource_id", 0L) > 0;
    }

    @Override
    public boolean userHasReourceCode(long userId, String reourceCode) throws SQLException {
        if (userId < 1 || TextUtils.isEmpty(reourceCode)) {
            return false;
        }

        return db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id=rl.resource_id")
                .whereEq("rl.lk_objt_id", userId)
                .andEq("rl.lk_objt", Constants.OBJT_user)
                .andEq("r.resource_code", reourceCode)
                .andEq("r.is_disabled", 0)
                .limit(1)
                .caching(cache)
                .selectValue("r.resource_id", 0L) > 0;
    }


    @Override
    public List<Group> getGroupListByUser(long userId) throws SQLException {
        if (userId < 1) {
            return new ArrayList<>();
        }

        List<Object> groupIds = db.table("grit_user_linked")
                .whereEq("user_id", userId)
                .andEq("lk_objt", Constants.OBJT_group)
                .caching(cache)
                .selectArray("lk_objt_id");


        return db.table("grit_group")
                .whereIn("group_id", groupIds)
                .caching(cache)
                .selectList("*", Group.class);
    }

    @Override
    public List<Group> getReourceGroupListByUser(long userId, String groupCode) throws SQLException {
        if (userId < 1 || TextUtils.isEmpty(groupCode)) {
            return new ArrayList<>();
        }

        Group group = GritClient.group().getGroupByCode(groupCode);

        if (group.group_id < 1) {
            return new ArrayList<>();
        }

        return getReourceGroupListByUser(userId, group.group_id);
    }

    @Override
    public List<Group> getReourceGroupListByUser(long userId, long groupId) throws SQLException {
        if (userId < 1 || groupId < 1L) {
            return new ArrayList<>();
        }

        //1.找出我所有的资源
        List<Integer> resourceIds = db.table("grit_resource r")
                .innerJoin("grit_resource_linked rl").on("r.resource_id=rl.resource_id")
                .where("rl.lk_objt_id=? AND rl.lk_objt=? AND r.is_branched=0 AND r.is_disabled=0", userId, Constants.GROUP_user_root_id)
                .caching(cache)
                .selectArray("r.resource_id");

        if (resourceIds.size() == 0) {
            return new ArrayList<>();
        }

        //2.找出资源相关的组id
        List<Integer> groupIds = db.table("grit_resource_linked rl")
                .where("rl.lk_objt=? AND rl.resource_id IN (?...)", Constants.OBJT_group, resourceIds)
                .caching(cache)
                .selectArray("DISTINCT rl.lk_objt_id");

        if (groupIds.size() == 0) {
            return new ArrayList<>();
        }

        //3.找出相关组的诚意情
        return db.table("grit_group")
                .where("group_id IN (?...) AND is_disabled=0 AND is_visibled=1", groupIds)
                .andEq("group_pid", groupId)
                .orderBy("Order_Index")
                .caching(cache)
                .selectList("*", Group.class);

    }

    @Override
    public List<User> getUserListByGroup(long groupId) throws SQLException {
        List<Object> userIds = db.table("grit_user_linked")
                .whereEq("lk_objt", Constants.OBJT_group)
                .andEq("lk_objt_id",groupId)
                .caching(cache)
                .selectArray("user_id");

        return db.table("grit_user")
                .whereIn("user_id", userIds)
                .caching(cache)
                .selectList("*", User.class);
    }
}
