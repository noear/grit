package org.noear.grit.client.model;

import org.noear.grit.client.impl.utils.PropUtils;
import org.noear.grit.client.impl.utils.TextUtils;

import java.util.Properties;

/**
 * 资源领域模型（主要有：路径资源；权限资源；等等...角色也可以用资源来代替）
 *
 * @author noear
 * @since 1.0
 */
public class Resource extends ResourceDo {
    public Resource() {
        super();
    }

    public Resource(long reourceId) {
        super();
        this.resource_id = reourceId;
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
