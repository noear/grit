<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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

            $('#space_id').val(${space_id!});
        });

        function queryForm() {
            location.href = "/grit/ui/resource/entity?space_id="+$('#space_id').val();
        };

        function node_onclick(group_id,obj) {
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/grit/ui/resource/entity/inner?group_id="+group_id);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <div style="margin: 5px;">
                    <select style="width: 100%;" id="space_id"  onchange="queryForm();">
                        <#list spaceList as m>
                            <option value=${m.resource_id}>${m.display_name}</option>
                        </#list>
                    </select>
                </div>
                <#list groupList as m1>
                    <li title="Id: ${m1.resource_id}"
                        class="${m1.is_visibled?string("","hid")} ${m1.is_disabled?string("dis","")}"
                        onclick="node_onclick('${m1.resource_id}',this)"
                        id="e${m1.resource_id}">
                        ${m1.levelSpan()}
                        ${m1.display_name}
                    </li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/grit/ui/resource/entity/inner?group_id=${group_id!}" frameborder="0" id="table"></iframe>
    </right>
</main>
</body>
</html>