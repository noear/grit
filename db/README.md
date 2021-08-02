
## 约定

##### LK_OBJT 内部约定

|  对象 | LI_OBJT | 
| -------- | -------- |
| grit_resource (type[0 node, 1 namespace, 2 group, 11 permission_page, 12 permission_operation, 13 permission_data])           | 5     | 
| grit_resource_linked     | 6     | 
| grit_user_group          | 2     | 
| grit_user                | 7     | 
| grit_user_linked         | 8     | 


##### DEFINE 内部约定

|  对象 | GROUP-ID | 
| -------- | -------- | 
| resource group root    | group_root_id : 1     | 
| user group root        | group_root_id : 2     | 
| role group root        | group_root_id : 3     | 

## 兼容
mysql 5.6+
h2


grit_resource (type[group,page,operation,data])
grit_resource_linked
grit_user_group         
grit_user               
grit_user_linked       