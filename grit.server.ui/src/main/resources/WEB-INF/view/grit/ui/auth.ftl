<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 主体管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        $(function () {
            if ('${group_id!}') {
                $('#e${group_id!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });

        function entity_get(group_id){
            let entityTree = $('tree.entity');

            $.ajax({
                type:"POST",
                url:"/grit/resource/edit/ajax/save",
                data:{group_id:group_id},
                success:function (rst) {
                    if(rst.code == 200) {
                        for(ent of rst.data){
                            entityTree.append('<li id="'+ent.subject_id+'">'+ent.display_name+'</li>');
                        }
                    }
                }
            });
        }

        function node_onclick(group_id,obj) {
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            entity_get(group_id);
            //$("#table").attr('src',"/grit/subject/entity/inner?group_id="+group_id);
        };
    </script>
    <style>
        tree.group{background: #dadde1;}
        tree.group ul li.sel{background:#eaedf1;}
    </style>
</head>
<body>
<main>
    <left>
        <tree id="tree" class="group">
            <ul>
                <#list list as n1>
                    <li onclick="node_onclick('${n1.subject_id}',this)" id="e${n1.subject_id}">
                        <#if n1.level gt 0>
                            |-
                        </#if>
                        ${n1.display_name}
                    </li>
                </#list>
            </ul>
        </tree>
    </left>
    <middle>
        <tree class="entity">
            <ul>

            </ul>
        </tree>
    </middle>
    <right class="frm">
        <!-- src="/grit/auth/inner?subject_id="  -->
        <iframe frameborder="0"
                id="table"></iframe>
    </right>
</main>
</body>
</html>