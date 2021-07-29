package org.noear.grit.client.model;

import java.io.Serializable;

/**
 * 资源连接（resource->group || resource->user || resource->resource）
 *
 * @author noear
 * @since 1.0
 */
public class ResourceLinkedDo implements Serializable {
    /** 资源ID */
    public long resource_id;
    /** 连接对象 */
    public int lk_objt;
    /** 连接对象ID */
    public long lk_objt_id;
}
