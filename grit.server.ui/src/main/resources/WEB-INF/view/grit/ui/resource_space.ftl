<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源空间管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        datagrid tr.dis{text-decoration:line-through; color:#888;}
        datagrid tr.hid{color:blue;}
    </style>
</head>
<script>
    function imp(file) {
        if(confirm("确定要导入吗？") == false){
            return;
        }

        var fromData = new FormData();
        fromData.append("file", file);
        fromData.append("tag","${tag_name!}");

        $.ajax({
            type:"POST",
            url:"ajax/import",
            data:fromData,
            processData: false,
            contentType: false,
            success:function (data) {
                if(data.code==1) {
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

    function exp() {
        var vm = formToMap(".sel_from");
        if(!vm.sel_id){
            alert("请选择..");
            return;
        }

        window.open("ajax/export?tag=${tag_name!}&ids=" + vm.sel_id, "_blank");
    }

    function del(act,hint){
        var vm = formToMap(".sel_from");

        if(!vm.sel_id){
            alert("请选择..");
            return;
        }

        if(confirm("确定要"+hint+"吗？") == false) {
            return;
        }

        $.ajax({
            type:"POST",
            url:"ajax/batch",
            data:{act: act, ids: vm.sel_id},
            success:function (data) {
                if(data.code==1) {
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

        $("#imp_file").change(function () {
            imp(this.files[0]);
        })
    });
</script>
<body>
<toolbar>
        <left class="col-4">
            <file>
                <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor">导入</a></label>
            </file>

            <button type='button' class="minor mar10-l" onclick="exp('${tag_name!}')" >导出</button>

            <a class="btn edit mar10-l" href="/grit/resource/edit?type=2">添加空间</a>
        </left>
        <right class="col-4">

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
            <td width="60px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as m1>
            <tr title="Id: ${m1.resource_id}" class="${m1.is_visibled?string("","hid")} ${m1.is_disabled?string("dis","")}">
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${m1.resource_id}" /><a></a></label></checkbox></td>
                <td >${m1.order_index}</td>
                <td class="left">
                    <#if m1.resource_type = 0>
                        |-
                    </#if>
                    ${m1.display_name!}
                    <#if m1.resource_code?length gt 0>
                        (${m1.resource_code!})
                    </#if>
                </td>
                <td class="left">${m1.link_uri!}</td>
                <td class="left">${m1.remark!}</td>
                <td >${m1.is_visibled?string("True","")}</td>
                <td class="op"><a href="/grit/resource/edit?resource_id=${m1.resource_id}" class="btn minor">编辑</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>