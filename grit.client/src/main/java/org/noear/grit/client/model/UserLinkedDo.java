package org.noear.grit.client.model;

import java.io.Serializable;

/**
 * 用户连接（user->group || user->user）
 *
 * @author noear
 * @since 1.0
 */
public class UserLinkedDo implements Serializable {
    /** 用户ID */
    public long user_id;
    /** 连接对象 */
    public int lk_objt;
    /** 连接对象ID */
    public long lk_objt_id;
}
