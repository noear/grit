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
        function node_show(subject_id){
            $("#table").attr('src',"/grit/auth/inner?subject_id="+subject_id);
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
                },100);
            } else {
                $('tree li:first').addClass('sel');
            }
        });
    </script>
    <style>
        tree.group{background: #dadde1;}
        tree.group ul li.sel{background:#eaedf1;}

        .dis{text-decoration:line-through; color:#888;}
        .hid{color:#666;}
    </style>
</head>
<body>
<main>
    <middle>
        <tree id="tree2" class="entity">
            <ul>
                <#list enityList as n1>
                    <li id="e${n1.subject_id}" onclick="node2_onclick('${n1.subject_id}',this)">${n1.display_name}</li>'
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>