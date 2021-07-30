package org.noear.grit.client.model;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.impl.utils.PropUtils;
import org.noear.grit.client.impl.utils.TextUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

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
        return GritClient.group().getGroupById(group_parent_id);
    }

    /**
     * 获取当前组的子组列表
     */
    public List<Group> getChildren() throws SQLException {
        return GritClient.group().getChildrenById(group_id);
    }

    /**
     * 获取当前组的资源列表
     */
    public List<Resource> getResourceList() throws SQLException {
        return GritClient.resource().getResourceListByGroup(group_id);
    }

    /**
     * 获取当前组的用户列表
     */
    public List<User> getUserList() throws SQLException {
        return GritClient.user().getUserListByGroup(group_id);
    }


    /////////////////////////////////////

    Properties attributesProp;

    /**
     * 获取属性集
     */
    public Properties getAttributeProp() {
        if (attributesProp == null) {
            if (TextUtils.isEmpty(attributes)) {
                attributesProp = new Properties();
            } else {
                attributesProp = PropUtils.build(attributes);
            }
        }

        return attributesProp;
    }

    /**
     * 获取属性
     * */
    public String getAttribute(String name) {
        return getAttributeProp().getProperty(name);
    }
}
