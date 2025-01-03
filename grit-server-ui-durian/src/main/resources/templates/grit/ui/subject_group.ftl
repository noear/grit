<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主体组管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
    </style>
    <script>
        $(function(){
            $('#sel_all').change(function(){
                var ckd= $(this).prop('checked');
                $('[name=sel_id]').prop('checked',ckd);
            });

            $("#imp_file").change(function () {
                imp(this.files[0]);
            })
        });
    </script>
</head>
<body>
<toolbar>
    <left>
        <a class="btn edit" href="/grit/ui/subject/edit?type=1">新增分组</a>
    </left>
    <right>

    </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="50px">排序</td>
            <td class="left">显示名</td>
            <td width="50px">是否<br/>可见</td>
            <td width="50px">是否<br/>禁用</td>
            <td width="130px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as n1>
            <tr title="Id: ${n1.subject_id}"
                class="${n1.is_visibled?string("","hid")} ${n1.is_disabled?string("dis","")}">
                <td >${n1.order_index}</td>
                <td class="left">
                    ${n1.levelSpan()}
                    ${n1.display_name!}
                    <#if n1.subject_code?length gt 0>
                        (${n1.subject_code!})
                    </#if>
                </td>
                <td >${n1.is_visibled?string("Yes","")}</td>
                <td >${n1.is_disabled?string("Yes","")}</td>
                <td class="op">
                    <a href="/grit/ui/subject/edit?subject_id=${n1.subject_id}" class="t2">编辑</a>
                    |
                    <a href="/grit/ui/subject/edit?group_id=${n1.subject_id}&type=1" class="t2">新增下级</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>