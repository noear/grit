<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源组管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        .dis{text-decoration:line-through; color:#888;}
        .hid{color:green;}
    </style>
</head>
<script>
    $(function(){
        $('#sel_all').change(function(){
            var ckd= $(this).prop('checked');
            $('[name=sel_id]').prop('checked',ckd);
        });
    });
</script>
<body>
<toolbar>
        <left>
            <#if (space_id!0) gt 0>
                <a class="btn edit mar10-l" href="/grit/resource/edit?group_id=${space_id!}&type=1">新增分组</a>
            </#if>
        </left>
        <right>

        </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
            <td width="50px">排序</td>
            <td width="250px" class="left">显示名</td>
            <td class="left">路径</td>
            <td width="100px" class="left">备注</td>
            <td width="80px" class="left">是否可见</td>
            <td width="80px" class="left">是否禁用</td>
            <td width="140px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as m1>
            <tr title="Id: ${m1.resource_id}" class="${m1.is_visibled?string("","hid")} ${m1.is_disabled?string("dis","")}">
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m1.resource_id}" /><a></a></label></checkbox></td>
                <td>${m1.order_index}</td>
                <td class="left">
                    ${m1.levelSpan()}
                    ${m1.display_name!}
                    <#if m1.resource_code?length gt 0>
                        (${m1.resource_code!})
                    </#if>
                </td>
                <td class="left">${m1.link_uri!}</td>
                <td class="left">${m1.remark!}</td>
                <td >${m1.is_visibled?string("True","")}</td>
                <td >${m1.is_disabled?string("True","")}</td>
                <td class="op right">
                    <a href="/grit/resource/edit?resource_id=${m1.resource_id}" class="t2">编辑</a>
                    |
                    <a href="/grit/resource/edit?group_id=${m1.resource_id}&type=1" class="t2">新增下级</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>