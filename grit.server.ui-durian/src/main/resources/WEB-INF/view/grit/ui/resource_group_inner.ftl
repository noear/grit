<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源组管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
    </style>
</head>
<script>
    var space_id = "${space_id!0}";
    function imp(file) {
        if(confirm("确定要导入吗？") == false){
            return;
        }

        var fromData = new FormData();
        fromData.append("file", file);
        fromData.append("space_id", space_id);

        layer.load(2);

        $.ajax({
            type:"POST",
            url:"ajax/import",
            data:fromData,
            processData: false,
            contentType: false,
            success:function (data) {
                layer.closeAll();

                if(data.code == 200) {
                    layer.msg('操作成功');
                    setTimeout(function(){
                        location.reload();
                    },800);
                }else{
                    layer.msg(data.description);
                }
            },
            error:function(data){
                layer.closeAll();
                layer.msg('网络请求出错...');
            }
        });
    }

    function exp() {
        var vm = formToMap(".sel_from");
        if(!vm.sel_id){
            alert("请选择..");
            return;
        }

        window.open("ajax/export?space_id=${space_id!0}&ids=" + vm.sel_id, "_blank");
    }

    function del(act,hint) {
        var vm = formToMap(".sel_from");

        if (!vm.sel_id) {
            alert("请选择..");
            return;
        }

        if (confirm("确定要" + hint + "吗？") == false) {
            return;
        }

        $.ajax({
            type: "POST",
            url: "ajax/batch",
            data: {space_id: space_id, act: act, ids: vm.sel_id},
            success: function (data) {
                if (data.code == 200) {
                    layer.msg('操作成功');
                    setTimeout(function () {
                        location.reload();
                    }, 800);
                } else {
                    layer.msg(data.description);
                }
            }
        });
    }


    $(function(){
        $('#sel_all').change(function(){
            var ckd= $(this).prop('checked');
            $('[name=sel_id]').prop('checked',ckd);
        });

        $("#imp_file").change(function () {
            imp(this.files[0]);
        });
    });
</script>
<body>
<toolbar>
    <left>
        <#if (space_id!0) gt 0>
            <#if state!=1>
                <button type='button' class="minor mar10-r" onclick="del(1,'禁用')" >禁用</button>
            <#else>
                <button type='button' class="minor mar10-r" onclick="del(0,'启用')" >启用</button>
            </#if>
            <a class="btn edit" href="/grit/ui/resource/edit?group_id=${space_id!}&type=1">新增分组</a>
        </#if>
    </left>
    <right>
        <selector>
            <a class="${(state !=1)?string('sel','')}" href="./inner?space_id=${space_id}&state=0">启用</a>
            <a class="${(state =1)?string('sel','')}" href="./inner?space_id=${space_id}&state=1">未启用</a>
        </selector>
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
            <td width="50px">是否<br/>全屏</td>
            <td width="50px">是否<br/>可见</td>
            <td width="50px">是否<br/>禁用</td>
            <td width="130px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as m1>
            <tr title="Id: ${m1.resource_id}"
                class="${m1.is_visibled?string("","hid")} ${m1.is_disabled?string("dis","")}">
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
                <td >${m1.is_fullview?string("Yes","")}</td>
                <td >${m1.is_visibled?string("Yes","")}</td>
                <td >${m1.is_disabled?string("Yes","")}</td>
                <td class="op">
                    <a href="/grit/ui/resource/edit?resource_id=${m1.resource_id}" class="t2">编辑</a>
                    |
                    <a href="/grit/ui/resource/edit?group_id=${m1.resource_id}&type=1" class="t2">新增下级</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>