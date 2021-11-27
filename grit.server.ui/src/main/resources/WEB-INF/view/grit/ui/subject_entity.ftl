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
            if ('${group_id!}') {
                $('#e${group_id!}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }
        });
        function node_onclick(group_id,obj) {
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/grit/subject/entity/inner?group_id="+group_id);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list list as n1>
                    <li onclick="node_onclick('${n1.data.subject_id}',this)" id="e${n1.data.subject_id}">
                        <#if n1.level gt 0>
                            |-
                        </#if>
                        ${n1.data.display_name}
                    </li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/grit/subject/entity/inner?group_id=${group_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>