<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主体管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
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
    <div class="center">
        <form class="mar10-r">
        <input type="text" name="key" class="w200" value="${key!}" />
        <button>查询</button>
        </form>
        <a class="btn edit" href="/grit/subject/edit?type=0&group_id=${group_id!0}">新增主体</a>
    </div>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="50px">排序</td>
            <td class="left">显示名</td>
            <td width="50px">是否<br/>可见</td>
            <td width="50px">是否<br/>禁用</td>
            <td width="60px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as m1>
            <tr class="${m1.is_visibled?string("","hid")} ${m1.is_disabled?string("dis","")}">
                <td >${m1.order_index}</td>
                <td class="left">
                    ${m1.display_name!}
                    <#if m1.login_name?length gt 0>
                        (${m1.login_name!})
                    </#if>
                </td>
                <td >${m1.is_visibled?string("Yes","")}</td>
                <td >${m1.is_disabled?string("Yes","")}</td>
                <td class="op"><a href="/grit/subject/edit?subject_id=${m1.subject_id}&group_id=${group_id!0}" class="t2">编辑</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>