package org.noear.grit.client.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源（主要有：路径资源；权限资源；等等...角色也可以用资源来代替）
 *
 * @author noear
 * @since 1.0
 */
public class ResourceDo implements Serializable {
    /** 资源ID */
    public long resource_id;
    /** 资源代码 */
    public String resource_code;
    /** 显示名 */
    public String display_name;
    /** 排序值 */
    public int order_index;
    /** 链接地址 */
    public String link_uri;
    /** 链接目标 */
    public String link_target;
    /** 链接特性(用,隔开) */
    public String link_attrs;
    /** 图标地址 */
    public String icon_uri;
    /** 备注 */
    public String remark;
    /** 标签 */
    public String tags;
    /** 属性(kv) */
    public String attributes;
    /** 是否全视图 */
    public boolean is_fullview;
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
        return "ResourceDo{" +
                "resource_id=" + resource_id +
                ", resource_code='" + resource_code + '\'' +
                ", display_name='" + display_name + '\'' +
                ", order_index=" + order_index +
                ", link_uri='" + link_uri + '\'' +
                ", link_target='" + link_target + '\'' +
                ", link_attrs='" + link_attrs + '\'' +
                ", icon_uri='" + icon_uri + '\'' +
                ", remark='" + remark + '\'' +
                ", tags='" + tags + '\'' +
                ", attributes='" + attributes + '\'' +
                ", is_fullview=" + is_fullview +
                ", is_disabled=" + is_disabled +
                ", is_visibled=" + is_visibled +
                ", create_fulltime=" + create_fulltime +
                ", update_fulltime=" + update_fulltime +
                '}';
    }
}
