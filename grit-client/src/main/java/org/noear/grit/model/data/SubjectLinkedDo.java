package org.noear.grit.model.data;

import java.io.Serializable;

/**
 * 主体连接（连接自己）
 *
 * @author noear
 * @since 1.0
 */
public class SubjectLinkedDo implements Serializable {
    /** 连接ID */
    public Long link_id;
    /** 主体ID */
    public Long subject_id;
    /** 分组主体ID */
    public Long group_subject_id;
    /** 创建时间 */
    public Long gmt_create;
}
