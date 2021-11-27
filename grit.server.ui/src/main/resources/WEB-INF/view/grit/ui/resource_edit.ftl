<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 资源编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="//mirror.noear.org/lib/ace/ace.js" ></script>
    <script src="//mirror.noear.org/lib/ace/ext-language_tools.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
        pre{border:1px solid #C9C9C9;}

        a.clone{display: inline-block; color: #666; width: 26px; height: 26px; line-height: 26px; text-align: center; }
        a.clone:hover{color: #000;}

        boxlist label a{ background:#fff; border-color:#f1f1f1 #f1f1f1 #fff #f1f1f1;}
        boxlist label a:hover{ border-color:#C9C9C9 #C9C9C9 #fff #C9C9C9;}
        boxlist input:checked + a{background:#C9C9C9; border-color:#C9C9C9}
    </style>

    <script>
        var resource_id = '${m1.resource_id!}';
        var resource_type = ${m1.resource_type!0};

        function save() {
            var vm = formToMap('form');

            if (!vm.display_name) {
                top.layer.msg("显示名不能为空！");
                return;
            }

            vm.resource_id= resource_id;

            $.ajax({
                type:"POST",
                url:"/grit/resource/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==200) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            if(resource_type == 2){
                                location.href="/grit/resource/space";
                            }else if(resource_type == 1){
                                location.href="/grit/resource/group/inner?space_id=${m1.resource_sid!}";
                            }else{
                                location.href="/grit/resource/entity/inner?group_id=${m1.resource_pid!}";
                            }
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {
            if(!resource_id){
                return;
            }

            if(!confirm("确定要删除吗？")){
                return;
            }

            $.ajax({
                type:"POST",
                url:"/grit/resource/edit/ajax/del",
                data:{"resource_id":resource_id},
                success:function (data) {
                    if(data.code==200) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            if(resource_type == 2){
                                location.href="/grit/resource/space";
                            }else if(resource_type == 1){
                                location.href="/grit/resource/group?space_id=${m1.resource_sid!}";
                            }else{
                                location.href="/grit/resource/entity?group_id=${m1.resource_pid!}";
                            }
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function build_editor(mod){
            if(window.editor){
                window.editor.getSession().setMode("ace/mode/"+mod);
                return
            }

            var editor = ace.edit(document.getElementById('attrs_edit'));

            editor.setTheme("ace/theme/chrome");
            editor.getSession().setMode("ace/mode/"+mod);
            editor.setOptions({
                showFoldWidgets:false,
                showLineNumbers:false,
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });

            editor.setShowPrintMargin(false);
            editor.moveCursorTo(0, 0);

            editor.getSession().on('change', function(e) {
                var value = editor.getValue();
                $('#attrs').val(value);
            });

            window.editor = editor;
        }

        function editor_shift(mod){
            localStorage.setItem("water_prop_edit_mode", mod);

            build_editor(mod);
        }

        $(function(){
            //编辑模式支持
            var _edit_mode="properties";
            if(!_edit_mode){
                _edit_mode =localStorage.getItem("water_prop_edit_mode");
            }

            if(!_edit_mode){
                _edit_mode = 'text';
            }

            $("input[name='edit_mode'][value='"+_edit_mode+"']").prop("checked",true);
            build_editor(_edit_mode);

            ctl_s_save_bind(document,save);
        });

    </script>
</head>
<body>
<toolbar class="blockquote">
    <left>
        <h2 class="ln30"><a href="#" onclick="history.back(-1)" class="noline">
                <#if m1.resource_type==2>
                    资源空间
                <#elseif m1.resource_type==1>
                    资源组
                <#else>
                    资源
                </#if>
            </a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" class="w80" onclick="save()">保存</button>
        <button type="button" class="minor" onclick="del()">删除</button>
    </right>
</toolbar>


<detail>
    <form>
        <input type="hidden" id="resource_type" value="${m1.resource_type!0}">
        <input type="hidden" id="resource_pid" value="${m1.resource_pid!0}">
        <input type="hidden" id="resource_sid" value="${m1.resource_sid!0}">
        <table>
            <tr>
                <th>代号</th>
                <td><input type="text" id="resource_code" autofocus value="${m1.resource_code!}"/></td>
            </tr>
            <tr>
                <th>显示名*</th>
                <td><input type="text" id="display_name" value="${m1.display_name!}"/></td>
            </tr>
            <tr>
                <th>排序</th>
                <td><input type="text" id="order_index" value="${m1.order_index!}"/></td>
            </tr>


            <tr>
                <th>备注</th>
                <td><input type="text" class="longtxt" id="remark" value="${m1.remark!}"/></td>
            </tr>

            <tr>
                <th>图标路径</th>
                <td><input type="text" class="longtxt" id="icon_uri" value="${m1.icon_uri!}"/></td>
            </tr>

            <tr>
                <th>链接路径</th>
                <td><input type="text" class="longtxt" id="link_uri" value="${m1.link_uri!}"/></td>
            </tr>

            <#if m1.resource_type==0>
            <tr>
                <th>链接目标</th>
                <td><input type="text" id="link_target" value="${m1.link_target!}"/></td>
            </tr>
            <tr>
                <th>链接标签</th>
                <td><input type="text" id="link_tags" value="${m1.link_tags!}"/></td>
            </tr>

            <tr>
                <th>是否全屏</th>
                <td>
                    <switcher><label><input type="checkbox" id="is_fullview" value="1" ${m1.is_fullview?string("checked","")} /><a></a></label></switcher>
                </td>
            </tr>
            </#if>
            <tr>
                <th>是否可见</th>
                <td>
                    <switcher><label><input type="checkbox" id="is_visibled" value="1" ${m1.is_visibled?string("checked","")} /><a></a></label></switcher>
                </td>
            </tr>
            <tr>
                <th>是否禁用</th>
                <td>
                    <switcher><label><input type="checkbox" id="is_disabled" value="1" ${m1.is_disabled?string("checked","")} /><a></a></label></switcher>
                </td>
            </tr>
        </table>
    </form>
</detail>

</body>
</html>