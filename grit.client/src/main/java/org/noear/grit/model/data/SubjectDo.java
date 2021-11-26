package org.noear.grit.model.data;

import java.io.Serializable;

/**
 * 主体。分2个概念：实体、分组
 *
 * @author noear
 * @since 1.0
 */
public class SubjectDo implements Serializable {
    /** 主体ID */
    public Long subject_id;
    /** 主体父ID */
    public Long subject_pid;
    /** 主体类型(0:entity, 1:group) */
    public Integer subject_type;
    /** 主体代号 */
    public String subject_code;
    /** 主体登录名,默认用guid填充 */
    public String login_name;
    /** 主体登录密码 */
    public String login_password;
    /** 主体显示名 */
    public String display_name;
    /** 排序 */
    public Integer order_index;
    /** 备注 */
    public String remark;
    /** 邮箱 */
    public String mail;
    /** 属性(kv) */
    public String attrs;
    /** 是否禁用 */
    public Boolean is_disabled;
    /** 是否可见 */
    public Boolean is_visibled;
    /** 创建时间 */
    public Long gmt_create;
    /** 更新时间 */
    public Long gmt_modified;
}
