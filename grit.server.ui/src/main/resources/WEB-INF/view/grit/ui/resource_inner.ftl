<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}

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
    <left>
        <form>
            <input type="hidden"  name="space_id" value="${space_id!}"/>
            <input type="hidden" name="state" value="${state!}" />
            <a class="w60">显示名：</a><input type="text"  name="key" placeholder="key" value="${key!}" class="w350"/>
            <button type="submit">查询</button>
            <a class="btn edit mar10-l" href="/grit/resource/edit?space_id=${space_id!}">添加资源空间</a>
            <#if (space_id!0) gt 0>
                <a class="btn edit mar10-l" href="/grit/resource/edit?space_id=${space_id!}">添加资源分组</a>
                <a class="btn edit mar10-l" href="/grit/resource/edit?space_id=${space_id!}">添加资源</a>
            </#if>
        </form>

        <div><a class="w60"></a><file>
                <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor w80">导入</a></label>
            </file>

            <button type='button' class="minor w80 mar10-l" onclick="exp('${tag_name!}')" >导出</button>

            <#if state==1>
                <button type='button' class="minor mar10-l" onclick="del(0,'禁用')" >禁用</button>
            <#else>
                <button type='button' class="minor mar10-l" onclick="del(1,'启用')" >启用</button>
                <button type='button' class="minor mar10-l" onclick="del(9,'删除')" >删除</button>
            </#if>
        </div>
    </left>
    <right>
        <selector>
            <a class="${(state =1)?string('sel','')}" href="inner?space_id=${space_id}&state=1">启用</a>
            <a class="${(state !=1)?string('sel','')}" href="inner?space_id=${space_id}&state=0">未启用</a>
        </selector>
    </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td width="20px"><checkbox><label><input type="checkbox" id="sel_all" /><a></a></label></checkbox></td>
            <td width="100px" class="left">Id</td>
            <td width="100px" class="left">代号</td>
            <td class="left">显示名</td>
            <td class="left">路径</td>
            <td width="100px" class="left">是否可见</td>
            <td width="50px">操作</td>
        </tr>
        </thead>
        <tbody id="tbody" class="sel_from">
        <#list list as group>
            <tr>
                <td><checkbox><label><input type="checkbox" name="sel_id" value="${group.resource_id}" /><a></a></label></checkbox></td>
                <td class="left">${group.resource_id!}</td>
                <td class="left">${group.resource_code!}</td>
                <td class="left">${group.display_name!}</td>
                <td class="left">${group.link_uri!}</td>
                <td class="left">${group.is_visibled?string("True","False")}</td>
                <td class="op"><a href="/grit/resource/edit?resource_id=${group.resource_id}" class="t2">编辑</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>