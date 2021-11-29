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
        function entity_load(group_id){
            if(!group_id){
                return;
            }

            let entityTree = $('tree.entity ul');

            $.ajax({
                type:"POST",
                url:"/grit/auth/subject.entity.get",
                data:{group_id:group_id},
                success:function (rst) {
                    if(rst.code == 200) {
                        entityTree.empty();
                        for(ent of rst.data){
                            entityTree.append('<li id="e'+ent.subject_id+'" onclick="node2_onclick('+ent.subject_id+',this)">'+ent.display_name+'</li>');
                        }
                    }
                }
            });
        }

        function node_show(subject_id){
            $("#table").attr('src',"/grit/auth/inner?subject_id="+subject_id);
        }

        function node_onclick(group_id,obj) {
            $('#tree li.sel').removeClass('sel');
            $(obj).addClass("sel");
            entity_load(group_id);
            node_show(group_id);
        }

        function node2_onclick(subject_id,obj) {
            $('#tree2 li.sel').removeClass('sel');
            $(obj).addClass("sel");
            node_show(subject_id);
        }

        $(function () {
            let group_id = ${group_id!0};
            if (group_id) {
                $('#e'+group_id).addClass('sel');
                setTimeout(function() {
                    node_show(group_id);
                    entity_load(group_id);
                },100);
            } else {
                $('tree li:first').addClass('sel');
            }
        });
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
                <li title="Id: 0" id="e0" onclick="node_onclick('0',this)">全部</li>
                <#list list as n1>
                    <li title="Id: ${n1.subject_id}" onclick="node_onclick('${n1.subject_id}',this)" id="e${n1.subject_id}">
                        ${n1.levelSpan()}
                        ${n1.display_name}
                    </li>
                </#list>
            </ul>
        </tree>
    </left>
    <middle>
        <tree id="tree2" class="entity">
            <ul></ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>