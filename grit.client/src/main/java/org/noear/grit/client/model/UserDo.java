package org.noear.grit.client.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author noear
 * @since 1.0
 */
public class UserDo implements Serializable {
    /** 用户ID */
    public long user_id;
    /** 用户代号 */
    public String user_code;
    /** 用户登录名 */
    public String login_name;
    /** 用户登录密码 */
    public String login_password;
    /** 用户登录令牌 */
    public String login_token;
    /** 用户显示名 */
    public String display_name;
    /** 备注 */
    public String remark;
    /** 邮箱 */
    public String mail;
    /** 标签 */
    public String tags;
    /** 元信息(json) */
    public String meta;
    /** 是否禁用 */
    public boolean is_disabled;
    /** 是否可见 */
    public boolean is_visibled;

    /** 创建时间 */
    public Date create_fulltime;
    /** 更新时间 */
    public Date update_fulltime;

    @Override
    public String toString() {
        return "UserDo{" +
                "user_id=" + user_id +
                ", user_code='" + user_code + '\'' +
                ", login_name='" + login_name + '\'' +
                ", login_password='" + login_password + '\'' +
                ", login_token='" + login_token + '\'' +
                ", display_name='" + display_name + '\'' +
                ", remark='" + remark + '\'' +
                ", mail='" + mail + '\'' +
                ", tags='" + tags + '\'' +
                ", meta='" + meta + '\'' +
                ", is_disabled=" + is_disabled +
                ", is_visibled=" + is_visibled +
                ", create_fulltime=" + create_fulltime +
                ", update_fulltime=" + update_fulltime +
                '}';
    }
}
