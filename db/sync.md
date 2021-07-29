
```sql
-- bcf.group => grit.group
INSERT water_grit.grit_group(
group_id,group_root_id,group_parent_id,group_code,display_name,
display_code,order_index,order_refer,link_uri,icon_uri,
tags,is_branched,is_disabled,is_visibled,create_fulltime,update_fulltime)
SELECT pgid,r_pgid, p_pgid, pg_code,cn_name,
en_name,order_index,'',uri_path,'',
tags,is_branch,is_disabled,is_visibled,create_time,last_update
FROM water_bcf.bcf_group;
```


```sql
-- bcf.user => grit.user
INSERT water_grit.grit_user (
user_id,user_code,login_name,login_password,login_token,
display_name,remark,mail,tags,is_disabled,is_visibled,create_fulltime,update_fulltime
)
SELECT puid,user_id,user_id,pass_wd,token,
       cn_name,note,pw_mail, tags,is_disabled,is_visibled,create_time, last_update
FROM water_bcf.bcf_user;
```



```sql
-- bcf.resource => grit.resource
INSERT water_grit.grit_resource(
resource_id,resource_code,display_name,order_index,link_uri,link_target,
icon_uri,remark,is_fullview,is_disabled,
is_visibled,create_fulltime,update_fulltime
)
SELECT rsid,rs_code,cn_name,order_index,uri_path,uri_target,
ico_path,note,0,IFNULL(is_disabled,0),
1,create_time,last_update
FROM water_bcf.bcf_resource;

```


```sql
INSERT water_grit.grit_resource_linked(
resource_id,lk_objt,lk_objt_id
)
SELECT rsid,lk_objt,lk_objt_id
FROM water_bcf.bcf_resource_linked;
```

```sql
INSERT water_grit.grit_user_linked(
user_id,lk_objt,lk_objt_id
)
SELECT puid,lk_objt,lk_objt_id
FROM water_bcf.bcf_user_linked;
```