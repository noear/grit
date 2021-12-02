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

    /**
     * 是否为空
     * */
    public static boolean isEmpty(Subject subject) {
        if (subject == null || subject.subject_id == null || subject.subject_id == 0) {
            return true;
        } else {
            return false;
        }
    }

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

    public String levelSpan() {
        if (level == null) {
            return "";
        }

        switch (level) {
            case 1:
                return "&ensp;";
            case 2:
                return "&ensp;&ensp;";
            case 3:
                return "&ensp;&ensp;&ensp;";
            case 4:
                return "&ensp;&ensp;&ensp;&ensp;";
            case 5:
                return "&ensp;&ensp;&ensp;&ensp;&ensp;";
            default:
                return "";
        }
    }
}
