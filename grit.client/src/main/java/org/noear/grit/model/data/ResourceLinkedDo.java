package org.noear.grit.model.data;

import java.io.Serializable;

/**
 * 资源连接（连接主体）
 *
 * @author noear
 * @since 1.0
 */
public class ResourceLinkedDo implements Serializable {
    /** 连接ID */
    public Long link_id;
    /** 资源ID */
    public Long resource_id;
    /** 主体ID */
    public Long subject_id;
    /** 主体类型 */
    public Integer subject_type;
    /** 创建时间 */
    public Long gmt_create;
}
