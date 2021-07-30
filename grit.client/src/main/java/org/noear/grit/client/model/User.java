package org.noear.grit.client.model;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.impl.utils.PropUtils;
import org.noear.grit.client.impl.utils.TextUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

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
