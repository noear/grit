<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 主体管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        tree.group{background: #dadde1;}
        tree.group ul li.sel{background:#eaedf1;}

        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
    </style>
    <script>
        function node_show(subject_id){
            $("#table").attr('src',"/grit/ui/auth/inner?subject_id="+subject_id);
        }

        function node2_onclick(subject_id,obj) {
            $('#tree2 li.sel').removeClass('sel');
            $(obj).addClass("sel");
            node_show(subject_id);
        }

        $(function () {
            $('tree li:first').click();
        });
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree2" class="entity">
            <ul>
                <#list enityList as n1>
                    <li id="e${n1.subject_id}"
                        class="${n1.is_visibled?string("","hid")} ${n1.is_disabled?string("dis","")}"
                        onclick="node2_onclick('${n1.subject_id}',this)">${n1.display_name}</li>
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