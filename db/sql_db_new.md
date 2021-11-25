
## 建库脚本

```sql

-- 资源树
CREATE TABLE `grit_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源父ID',
  `resource_type` int(11) NOT NULL DEFAULT '0' COMMENT '资源类型(0:node, 1:resource, 2:namespace)',
  `resource_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '资源代码(例，user:del)',
  `display_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序值',
  `link_uri` varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '链接地址(例，/user/add)',
  `link_target` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接目标',
  `link_attrs` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接特性(用,隔开)',
  `icon_uri` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标地址',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `attributes` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性(kv)',
  `is_fullview` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全屏',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见（可见为页面，不可见为操作）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `gmt_create` bigint(20) NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `IX_grit_resource__resource_code` (`resource_code`) USING BTREE,
  KEY `IX_grit_resource__link_uri` (`link_uri`) USING BTREE,
  KEY `IX_grit_resource__resource_parent_id` (`resource_parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源表';


CREATE TABLE `grit_resource_linked` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `lk_objt` int(11) NOT NULL COMMENT '连接对象',
  `lk_objt_id` bigint(20) NOT NULL COMMENT '连接对象ID',
  `gmt_create` bigint(20) NULL COMMENT '创建时间',
  PRIMARY KEY (`resource_id`,`lk_objt`,`lk_objt_id`) USING BTREE,
  KEY `IX_grit_resource_linked__lk_objt` (`lk_objt_id`,`lk_objt`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-资源连接表';

-- 用户组
CREATE TABLE `grit_user_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `group_parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '分组父ID',
  `group_code` varchar(50) DEFAULT NULL COMMENT '分组代号',
  `display_name` varchar(200) DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序',
  `attributes` varchar(4000) DEFAULT NULL COMMENT '属性(kv)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `gmt_create` bigint(20) NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE,
  KEY `IX_grit_user_group__group_parent_id` (`group_parent_id`) USING BTREE,
  KEY `IX_grit_user_group__group_code` (`group_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-用户分组表';

INSERT INTO grit_user_group(group_id,group_parent_id,display_name) VALUES(2,0,'User group');
INSERT INTO grit_user_group(group_id,group_parent_id,display_name) VALUES(3,0,'Role group');


CREATE TABLE `grit_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_code` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户代号',
  `login_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录名',
  `login_password` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户登录密码',
  `display_name` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户显示名',
  `remark` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `mail` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `attributes` varchar(4000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性(kv)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `gmt_create` bigint(20) NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `IX_grit_user__login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='grit-用户表';


CREATE TABLE `grit_user_link_user_group` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '连接ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `group_id` bigint(20) NOT NULL COMMENT '用户分组ID',
  `gmt_create` bigint(20) NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`group_id`) USING BTREE,
  KEY `IX_grit_user_link_user_group__group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='grit-用户连接表';


```