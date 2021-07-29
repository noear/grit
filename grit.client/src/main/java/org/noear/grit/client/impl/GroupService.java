package org.noear.grit.client.impl;

import org.noear.grit.client.model.Group;

import java.sql.SQLException;
import java.util.List;

/**
 * 分组服务
 *
 * @author noear
 * @since 1.0
 */
public interface GroupService {

    /**
     * 分组获取
     *
     * @param groupId 分组Id
     */
    Group getGroupById(long groupId) throws SQLException;

    /**
     * 分组获取
     *
     * @param groupCode 分组代号
     */
    Group getGroupByCode(String groupCode) throws SQLException;


    /**
     * 分组获取子节点
     *
     * @param groupId 分组Id
     */
    List<Group> getChildrenById(long groupId) throws SQLException;

    /**
     * 分组获取子节点
     *
     * @param groupCode 分组代号
     */
    List<Group> getChildrenByCode(String groupCode) throws SQLException;



    /**
     * 获取分杈的分组
     * */
    List<Group> getGroupsOfBranched() throws SQLException;

    /**
     * 获取用户的分组
     * */
    List<Group> getGroupsByUser(long userId) throws SQLException;
}
