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
    let space_id   = ${space_id!0};

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
    });
</script>
<body>
<toolbar>
    <left class="col-4">
        <strong>
            ${subject.display_name!}
        </strong>
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
            <strong>
            <#if g.level gt 0>
                |-
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

</article>

</body>
</html>