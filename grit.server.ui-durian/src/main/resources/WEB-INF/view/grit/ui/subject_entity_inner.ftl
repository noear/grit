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
    let group_id = ${group_id!0};
    function copy() {
        var vm = formToMap('#tbody');
        if (!vm.sel_id) {
            alert("没有已选择的主体");
            return
        }

        window.parent.copied = vm.sel_id;
    }

    function paste() {
        if (!window.parent.copied) {
            alert("没有已复制的主体");
        }

        let vm = {};
        vm.subject_ids = window.parent.copied;
        vm.group_id = group_id;

        $.ajax({
            type:"POST",
            url:"/grit/subject/edit/ajax/paste",
            data:vm,
            success:function (data) {
                if(data.code==200) {
                    top.layer.msg('操作成功');

                    setTimeout(function(){
                        location.reload();
                    },800);
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    }

    $(function(){
        $('#sel_all').change(function(){
            var ckd= $(this).prop('checked');
            $('[name=sel_id]').prop('checked',ckd);
        });
    });
</script>
<body>
<toolbar>
    <flex>
        <left class="col-3">
            <a class="btn minor" onclick="copy()">复制</a>
            <#if group_id == 0>
            <a class="btn minor" onclick="paste()">粘贴</a>
            </#if>
        </left>
        <mid class="col-6 center">
            <#if group_id == 0>
                <form class="mar10-r">
                    <input type="text" name="key" class="w200" value="${key!}" />
                    <button>查询</button>
                </form>
            </#if>
            <a class="btn edit" href="/grit/subject/edit?type=0&group_id=${group_id!0}">新增主体</a>
        </mid>
        <right class="col-3">

        </right>
    </flex>

</toolbar>

<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="40px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
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
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m1.subject_id}" /><a></a></label></checkbox></td>
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