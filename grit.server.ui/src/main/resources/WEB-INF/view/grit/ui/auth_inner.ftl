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
        .dis{text-decoration:line-through; color:#888;}
        .hid{color:blue;}

        section header{line-height: 40px; height: 40px; font-weight: bold; margin-bottom: 5px;}
        section boxlist{}
        section boxlist label{margin: 0 0 5px;}
    </style>
</head>
<script>
    let subject_id = ${subject_id!0};
    let space_id   = ${space_id!0};

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

    function queryForm() {
        location.href = "/grit/auth/inner?subject_id=${subject_id!0}&space_id="+$('#space_id').val();
    };

    $(function(){
        $("#imp_file").change(function () {
            imp(this.files[0]);
        })

        $('#space_id').val(${space_id!});
    });
</script>
<body>
<toolbar>
    <left class="col-4">
        <file>
            <label><input id="imp_file" type="file" accept=".jsond"/><a class="btn minor">导入</a></label>
        </file>

        <button type='button' class="minor mar10-l" onclick="exp('${tag_name!}')" >导出</button>
    </left>
    <right class="col-4">
        <select style="width: 200px;" id="space_id"  onchange="queryForm();">
            <#list spaceList as m>
                <option value=${m.resource_id}>${m.display_name!}</option>
            </#list>
        </select>
    </right>
</toolbar>
<article>
    <#list groupList as g>
    <section>
        <header class="${g.is_visibled?string("","hid")} ${g.is_disabled?string("dis","")}">
            <#if g.level gt 0>
                |-
            </#if>
            ${g.display_name!}
        </header>
        <boxlist>
            <#list resourceList as r>
                <#if r.resource_pid == g.resource_id>
                   <label><input type="checkbox" id="r${r.resource_id}" /><a>${r.display_name}</a></label>
                    <#if r.display_name == '$'>
                        <br/>
                    </#if>
                </#if>
            </#list>
        </boxlist>
    </section>
    </#list>

</article>

</body>
</html>