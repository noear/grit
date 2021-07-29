package org.noear.grit.client.model;

import org.noear.grit.client.StoneClient;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.snack.ONode;

import java.sql.SQLException;
import java.util.List;

/**
 * 分组领域模型（主要有：资源分组；用户分组；角色分组）
 *
 * @author noear
 * @since 1.0
 */
public class Group extends GroupDo {
    public Group() {
        super();
    }

    public Group(long groupId) {
        super();
        this.group_id = groupId;
    }


    /**
     * 获取当前组的父组
     */
    public Group getParent() throws SQLException {
        return StoneClient.group().getGroupById(group_parent_id);
    }

    /**
     * 获取当前组的子组列表
     */
    public List<Group> getChildren() throws SQLException {
        return StoneClient.group().getChildrenById(group_id);
    }

    /**
     * 获取当前组的资源列表
     */
    public List<Resource> getResourceList() throws SQLException {
        return StoneClient.resource().getResourceListByGroup(group_id);
    }

    /**
     * 获取当前组的用户列表
     * */
    public List<User> getUserList() throws SQLException {
        return StoneClient.user().getUserListByGroup(group_id);
    }







    /////////////////////////////////////

    ONode metaProp;
    /**
     * 获取元信息的数据节点
     * */
    public ONode getMetaNode() {
        if (metaProp == null) {
            if (TextUtils.isEmpty(meta)) {
                metaProp = new ONode();
            } else {
                metaProp = ONode.loadStr(meta);
            }
        }

        return metaProp;
    }

    /**
     * 获取元信息的数据实体
     * */
    public <T> T getMetaBean(Class<T> tClass) {
        return getMetaNode().toObject(tClass);
    }
}
