<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 主体管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer/layer.js"></script>
    <style>
        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
    </style>
    <script>
        $(function () {
            if ('${group_id!}') {
                $('#e${group_id!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });
        function node_onclick(group_id,obj) {
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/grit/ui/subject/entity/inner?group_id="+group_id);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <li title="Id: 0" id="e0" onclick="node_onclick('0',this)">全部</li>
                <#list list as n1>
                    <li title="Id: ${n1.subject_id}"
                        class="${n1.is_visibled?string("","hid")} ${n1.is_disabled?string("dis","")}"
                        onclick="node_onclick('${n1.subject_id}',this)" id="e${n1.subject_id}">
                        ${n1.levelSpan()}
                        ${n1.display_name}
                    </li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/grit/ui/subject/entity/inner?group_id=${group_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>