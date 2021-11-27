<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
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
            $("#table").attr('src',"/grit/resource/group/inner?space_id="+space_id);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list list as m>
                    <li onclick="node_onclick('${m.resource_id}',this)" id="e${m.resource_id}">${m.resource_code}</li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/grit/resource/group/inner?space_id=${space_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>