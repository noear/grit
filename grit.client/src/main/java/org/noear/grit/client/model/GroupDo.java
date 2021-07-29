package org.noear.grit.client.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 分组（主要有：资源分组；用户分组；角色分组）
 *
 * @author noear
 * @since 1.0
 */
public class GroupDo implements Serializable {
    /** 分组ID */
    public long group_id;
    /** 分组根ID */
    public long group_root_id;
    /** 分组父ID */
    public long group_parent_id;
    /** 分组代号 */
    public String group_code;
    /** 显示代号 */
    public String display_code;
    /** 显示名 */
    public String display_name;
    /** 排序 */
    public int order_index;
    /** 排序参考(供内容显示序列参考) */
    public String order_refer;
    /** 链接地址 */
    public String link_uri;
    /** 标签 */
    public String tags;
    /** 元信息(json) */
    public String meta;
    /** 是否分杈（多系统资源隔离） */
    public boolean is_branched;
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
        return "GroupDo{" +
                "group_id=" + group_id +
                ", group_root_id=" + group_root_id +
                ", group_parent_id=" + group_parent_id +
                ", group_code='" + group_code + '\'' +
                ", display_code='" + display_code + '\'' +
                ", display_name='" + display_name + '\'' +
                ", order_index=" + order_index +
                ", order_refer='" + order_refer + '\'' +
                ", link_uri='" + link_uri + '\'' +
                ", tags='" + tags + '\'' +
                ", meta='" + meta + '\'' +
                ", is_branched=" + is_branched +
                ", is_disabled=" + is_disabled +
                ", is_visibled=" + is_visibled +
                ", create_fulltime=" + create_fulltime +
                ", update_fulltime=" + update_fulltime +
                '}';
    }
}
