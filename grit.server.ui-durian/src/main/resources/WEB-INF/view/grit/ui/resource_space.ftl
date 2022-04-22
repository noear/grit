<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源空间管理</title>
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
    function imp(file) {
        if(confirm("确定要导入吗？") == false){
            return;
        }

        var fromData = new FormData();
        fromData.append("file", file);

        layer.load(2);

        $.ajax({
            type:"POST",
            url:"/grit/ui/resource/space/ajax/import",
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
        <a class="btn edit mar10-r" href="/grit/ui/resource/edit?type=2">新增空间</a>
    </left>
    <right>
        <file>
            <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor w80">导入空间</a></label>
        </file>
    </right>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
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
                <td >${m1.order_index}</td>
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
                    <a href="/grit/ui/resource/space/ajax/export?space_id=${m1.resource_id}" target="_blank" class="t2">导出空间</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>

</body>
</html>