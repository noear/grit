package org.noear.grit.client.impl;

import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 分组服务实现
 *
 * @author noear
 * @since 1.0
 */
public class GroupServiceImpl implements GroupService {
    private final DbContext db;
    private final ICacheService cache;

    public GroupServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }

    @Override
    public Group getGroupById(long groupId) throws SQLException {
        if (groupId < 1L) {
            return new Group();
        }

        return db.table("stone_group")
                .whereEq("group_id", groupId)
                .limit(1)
                .caching(cache)
                .selectItem("*", Group.class);
    }

    @Override
    public Group getGroupByCode(String groupCode) throws SQLException {
        if (TextUtils.isEmpty(groupCode)) {
            return new Group();
        }

        return db.table("stone_group")
                .whereEq("group_code", groupCode)
                .limit(1)
                .caching(cache)
                .selectItem("*", Group.class);
    }

    @Override
    public List<Group> getChildrenByCode(String groupCode) throws SQLException {
        if (TextUtils.isEmpty(groupCode)) {
            return new ArrayList<>();
        }

        Group group = getGroupByCode(groupCode);

        return getChildrenById(group.group_id);
    }

    @Override
    public List<Group> getChildrenById(long groupId) throws SQLException {
        if (groupId < 1) {
            return new ArrayList<>();
        }

        return db.table("stone_group")
                .whereEq("group_parent_id", groupId)
                .andEq("is_disabled", 0)
                .andEq("is_visibled", 1)
                .caching(cache)
                .selectList("*", Group.class);
    }

    @Override
    public List<Group> getGroupsByBranched() throws SQLException {
        return db.table("stone_group")
                .whereEq("is_branched", 1)
                .andEq("is_visibled", 1)
                .andEq("is_disabled",0)
                .caching(cache)
                .selectList("*", Group.class);
    }

    @Override
    public List<Group> getGroupsByUser(long userId) throws SQLException {
        List<Object> groupIds = db.table("stone_user_linked")
                                .whereEq("user_id",userId)
                                .andEq("lk_objt", Constants.OBJT_stone_group)
                                .caching(cache)
                                .selectArray("lk_objt_id");

        return db.table("stone_group")
                .whereIn("group_id", groupIds)
                .caching(cache)
                .selectList("*", Group.class);
    }
}
