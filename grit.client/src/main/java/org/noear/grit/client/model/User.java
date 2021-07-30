package org.noear.grit.client.model;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.snack.ONode;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户领域模型
 *
 * @author noear
 * @since 1.0
 */
public class User extends UserDo {
    public User() {
        super();
    }

    public User(long userId) {
        super();
        this.user_id = userId;
    }

    /**
     * 获取当前用户的所在组列表
     */
    public List<Group> getGroupList() throws SQLException {
        return GritClient.group().getGroupsByUser(user_id);
    }

    /**
     * 获取当前用户的资源列有
     */
    public List<Resource> getResourceListByGroup(long groupId) throws SQLException {
        return GritClient.resource().getResourceListByUserAndGroup(user_id, groupId);
    }


    /////////////////////////////////////

    ONode metaProp;
    /**
     * 获取元信息的数据节点
     * */
    public ONode getMetaNode() {
        if (metaProp == null) {
            if (TextUtils.isEmpty(attributes)) {
                metaProp = new ONode();
            } else {
                metaProp = ONode.loadStr(attributes);
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
