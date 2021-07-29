package org.noear.grit.client.impl;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Branch;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
public class BranchServiceImpl implements BranchService {
    private final DbContext db;
    private final ICacheService cache;

    public BranchServiceImpl(DbContext db, ICacheService cache) {
        this.db = db;
        this.cache = cache;
    }

    @Override
    public Branch getBranchByCode(String groupCode) throws SQLException {
        if (TextUtils.isEmpty(groupCode)) {
            return new Branch();
        }

        return db.table("grit_group")
                .whereEq("group_code", groupCode)
                .andEq("is_branched",1)
                .limit(1)
                .caching(cache)
                .selectItem("*", Branch.class);
    }

    @Override
    public List<Branch> getBranchList() throws SQLException {
        return db.table("grit_group")
                .whereEq("is_branched", 1)
                .andEq("is_visibled", 1)
                .andEq("is_disabled",0)
                .caching(cache)
                .selectList("*", Branch.class);
    }

    @Override
    public List<Branch> getBranchListByUser(long userId) throws SQLException {
        List<Branch> list = getBranchList();
        List<Branch> list2 = new ArrayList<>();

        for(Branch group : list){
            if(GritClient.getUserModules(userId, group.group_id).size() > 0){
                list2.add(group);
            }
        }

        return list2;
    }


    public Branch getBranchFristByUser(long userId) throws SQLException{
        List<Branch> branchList = getBranchListByUser(userId);
        if (branchList.size() == 0) {
            return new Branch();
        } else {
            return branchList.get(0);
        }
    }
}
