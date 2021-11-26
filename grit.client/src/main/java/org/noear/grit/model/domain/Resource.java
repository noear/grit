package org.noear.grit.model.domain;

import org.noear.grit.client.impl.utils.PropUtils;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.model.data.ResourceDo;

import java.util.Properties;

/**
 * 资源领域模型（主要有：路径资源；权限资源；等等...角色也可以用资源来代替）
 *
 * @author noear
 * @since 1.0
 */
public class Resource extends ResourceDo {


    /////////////////////////////////////

    transient Properties attrsProp;

    /**
     * 获取属性集
     */
    public Properties getAttrsProp() {
        if (attrsProp == null) {
            if (TextUtils.isEmpty(attrs)) {
                attrsProp = new Properties();
            } else {
                attrsProp = PropUtils.build(attrs);
            }
        }

        return attrsProp;
    }

    /**
     * 获取属性
     */
    public String getAttr(String name) {
        return getAttrsProp().getProperty(name);
    }
}
