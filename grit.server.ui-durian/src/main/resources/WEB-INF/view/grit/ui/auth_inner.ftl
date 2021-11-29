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

        section header{line-height: 40px; height: 40px; margin-bottom: 5px;}
        section header a{cursor: default; color: #666;}
        section boxlist{}
        section boxlist label{margin: 0 0 5px;}
    </style>
</head>
<script>
    let subject_id = ${subject_id!0};
    let subject_type = ${subject.subject_type!0}
    let space_id   = ${space_id!0};

    function save() {
        var vm = formToMap('form');

        vm.subject_id= subject_id;
        vm.subject_type = subject_type;
        vm.space_id = space_id;

        $.ajax({
            type:"POST",
            url:"/grit/auth/ajax/save",
            data:vm,
            success:function (data) {
                if(data.code==200) {
                    top.layer.msg('操作成功');
                }else{
                    top.layer.msg(data.msg);
                }
            }
        });
    }

    function selNode(cls, type){
        if(type){
            //1
            $("."+cls).each(function(){
                $(this).prop('checked', true);
            });
        }else{
            //0
            $("."+cls).each(function(){
                $(this).prop('checked', !$(this).prop('checked'));
            });
        }
    }

    function queryForm() {
        location.href = "/grit/auth/inner?subject_id=${subject_id!0}&space_id="+$('#space_id').val();
    };

    $(function(){
        $('#space_id').val(${space_id!});
        valToForm('authRes','${authRes!}');
    });
</script>
<body>
<toolbar>
    <div class="center">
        <strong>
            ${subject.display_name!}
        </strong>
    </div>
    <div>
        <left>
            <select style="width: 200px;" id="space_id"  onchange="queryForm();">
                <#list spaceList as m>
                    <option value=${m.resource_id}>${m.display_name!}</option>
                </#list>
            </select>
        </left>
        <right>
            <button type="button" class="edit" onclick="save()">保存</button>
        </right>
    </div>

</toolbar>
<article>
    <form>
        <#list groupList as g>
            <section>
                <header class="${g.is_visibled?string("","hid")} ${g.is_disabled?string("dis","")}">
                    <strong>
                        <#if g.level == 1>
                            |-
                        <#elseif g.level == 2>
                            |-|-
                        </#if>
                        ${g.display_name!}
                    </strong>
                    ( <a onclick="selNode('g${g.resource_id}',1)">全选</a> | <a onclick="selNode('g${g.resource_id}',0)">反选</a> )
                </header>
                <boxlist>
                    <#list resourceList as r>
                        <#if r.resource_pid == g.resource_id>
                            <label><input class="g${g.resource_id}" type="checkbox" name="authRes" value="${r.resource_id}" /><a>${r.display_name}</a></label>
                            <#if r.display_name == '$'>
                                <br/>
                            </#if>
                        </#if>
                    </#list>
                </boxlist>
            </section>
        </#list>
    </form>
</article>

</body>
</html>