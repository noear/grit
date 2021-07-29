
```sql

CREATE TABLE `stone_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `group_root_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '分组根ID',
  `group_parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '分组父ID',
  `group_code` varchar(50) DEFAULT NULL COMMENT '分组代号',
  `display_name` varchar(200) DEFAULT NULL COMMENT '显示名',
  `display_code` varchar(200) DEFAULT NULL COMMENT '显示代号',
  `order_index` int(11) DEFAULT '0' COMMENT '排序',
  `order_refer` varchar(50) DEFAULT NULL COMMENT '排序参考(供内容显示序列参考)',
  `link_uri` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `icon_uri` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `meta` varchar(2000) DEFAULT NULL COMMENT '元信息(json)',
  `is_branched` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否分杈（多系统资源隔离）',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `create_fulltime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_fulltime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`) USING BTREE,
  KEY `IX_stone_group__group_parent_id` (`group_parent_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  ROW_FORMAT=DYNAMIC COMMENT='stone-分组表';


CREATE TABLE `stone_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_code` varchar(50) NOT NULL DEFAULT '' COMMENT '资源代码',
  `display_name` varchar(200) DEFAULT NULL COMMENT '显示名',
  `order_index` int(11) DEFAULT '0' COMMENT '排序值',
  `link_uri` varchar(200) NOT NULL DEFAULT '' COMMENT '链接地址',
  `link_target` varchar(50) DEFAULT NULL COMMENT '链接目标',
  `link_attrs` varchar(200) DEFAULT NULL COMMENT '链接特性(用,隔开)',
  `icon_uri` varchar(200) DEFAULT NULL COMMENT '图标地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `meta` varchar(2000) DEFAULT NULL COMMENT '元信息(json)',
  `is_fullview` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全视图',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `create_fulltime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_fulltime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `IX_stone_resource__resource_code` (`resource_code`) USING BTREE,
  KEY `IX_stone_resource__link_uri` (`link_uri`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  ROW_FORMAT=DYNAMIC COMMENT='stone-资源表';


CREATE TABLE `stone_resource_linked` (
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `lk_objt` int(11) NOT NULL COMMENT '连接对象',
  `lk_objt_id` bigint(20) NOT NULL COMMENT '连接对象ID',
  PRIMARY KEY (`resource_id`,`lk_objt`,`lk_objt_id`) USING BTREE,
  KEY `IX_stone_resource_linked__lk_objt` (`lk_objt`,`lk_objt_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  ROW_FORMAT=DYNAMIC COMMENT='stone-资源连接表';


CREATE TABLE `stone_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_code` varchar(50) DEFAULT NULL COMMENT '用户代号',
  `login_name` varchar(50) NOT NULL COMMENT '用户登录名',
  `login_password` varchar(50) DEFAULT NULL COMMENT '用户登录密码',
  `login_token` varchar(200) DEFAULT NULL COMMENT '用户登录令牌',
  `display_name` varchar(200) DEFAULT NULL COMMENT '用户显示名',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `mail` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `meta` varchar(2000) DEFAULT NULL COMMENT '元信息(json)',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `is_visibled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `create_fulltime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_fulltime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `IX_stone_user__login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  ROW_FORMAT=DYNAMIC COMMENT='stone-用户表';


CREATE TABLE `stone_user_linked` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `lk_objt` int(11) NOT NULL COMMENT '连接对象',
  `lk_objt_id` bigint(20) NOT NULL COMMENT '连接对象ID',
  PRIMARY KEY (`user_id`,`lk_objt`,`lk_objt_id`) USING BTREE,
  KEY `IX_stone_user_linked__lk_objt` (`lk_objt`,`lk_objt_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  COMMENT='stone-用户连接表';

```