<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主体组管理</title>
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

        <a class="btn edit mar10-l" href="/grit/subject/edit?type=1">添加分组</a>
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
            <td class="left">显示名</td>
            <td width="80px" class="left">是否可见</td>
            <td width="80px" class="left">是否禁用</td>
            <td width="140px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as n1>
            <tr>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${n1.data.subject_id}" /><a></a></label></checkbox></td>
                <td >${n1.data.order_index}</td>
                <td class="left">
                    <#if n1.level gt 0>
                        |-
                    </#if>
                    ${n1.data.display_name!}
                    <#if n1.data.subject_code?length gt 0>
                        (${n1.data.subject_code!})
                    </#if>
                </td>
                <td >${n1.data.is_visibled?string("True","")}</td>
                <td >${n1.data.is_disabled?string("True","")}</td>
                <td class="op">
                    <a href="/grit/subject/edit?subject_id=${n1.data.subject_id}" class="btn minor">编辑</a>
                    <a href="/grit/subject/edit?group_id=${n1.data.subject_id}&type=1" class="btn minor">添加下级</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>