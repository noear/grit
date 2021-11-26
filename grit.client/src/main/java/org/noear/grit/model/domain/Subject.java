package org.noear.grit.model.domain;

import org.noear.grit.client.utils.PropUtils;
import org.noear.grit.client.utils.TextUtils;
import org.noear.grit.model.data.SubjectDo;

import java.util.Properties;

/**
 * 主体
 *
 * @author noear
 * @since 1.0
 */
public class Subject extends SubjectDo {

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
