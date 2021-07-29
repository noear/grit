package org.noear.grit.client.impl;

import org.noear.grit.client.model.Branch;

import java.sql.SQLException;
import java.util.List;

/**
 * 分支组服务
 *
 * @author noear
 * @since 1.0
 */
public interface BranchService {

    /**
     * 获取分支组
     *
     * @param groupCode 分组代号
     * */
    Branch getBranchByCode(String groupCode) throws SQLException;

    /**
     * 获取所有的分支组
     * */
    List<Branch> getBranchList() throws SQLException;



    /**
     * 获取用户的分支组
     *
     * @param userId 用户ID
     * */
    List<Branch> getBranchListByUser(long userId) throws SQLException;

    /**
     * 获取用户的第一个分支组
     *
     * @param userId 用户ID
     * */
    Branch getBranchFristByUser(long userId) throws SQLException;
}
