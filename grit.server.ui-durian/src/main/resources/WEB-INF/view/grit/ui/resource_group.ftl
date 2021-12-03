<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源组管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        .dis{text-decoration:line-through; color:#aaa;}
        .hid{color:#888;}
    </style>
    <script>
        $(function () {
            if ('${space_id!}') {
                $('#e${space_id!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });
        function node_onclick(space_id,obj) {
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/grit/ui/resource/group/inner?space_id="+space_id);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list list as n1>
                    <li id="e${n1.resource_id}"
                        class="${n1.is_visibled?string("","hid")} ${n1.is_disabled?string("dis","")}"
                        onclick="node_onclick('${n1.resource_id}',this)" >${n1.display_name}</li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/grit/ui/resource/group/inner?space_id=${space_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>