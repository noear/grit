package org.noear.grit.client.model;

import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.snack.ONode;

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
