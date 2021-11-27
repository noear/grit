<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 主体编辑</title>
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
        var subject_id = '${m1.subject_id!}';
        var subject_type = ${m1.subject_type!0};

        function save() {
            var vm = formToMap('form');

            if (!vm.display_name) {
                top.layer.msg("显示名不能为空！");
                return;
            }

            vm.subject_id= subject_id;

            $.ajax({
                type:"POST",
                url:"/grit/subject/edit/ajax/save",
                data:vm,
                success:function (data) {
                    if(data.code==200) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            if(subject_type == 1){
                                location.href="/grit/subject/group";
                            }else{
                                location.href="/grit/subject/entity/inner?group_id=${group_id!}";
                            }
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {
            if(!subject_id){
                return;
            }

            if(!confirm("确定要删除吗？")){
                return;
            }

            $.ajax({
                type:"POST",
                url:"/grit/subject/edit/ajax/del",
                data:{"subject_id":subject_id},
                success:function (data) {
                    if(data.code==200) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            if(subject_type == 1){
                                location.href="/grit/subject/group";
                            }else{
                                location.href="/grit/subject/entity/inner?group_id=${group_id!}";
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
                <#if m1.subject_type==1>
                    主体组
                <#else>
                    主体
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
        <input type="hidden" id="subject_type" value="${m1.subject_type!0}">
        <input type="hidden" id="subject_pid" value="${m1.subject_pid!0}">
        <input type="hidden" id="group_id" value="${group_id!0}">
        <table>
            <tr>
                <th>代号</th>
                <td><input type="text" id="subject_code" autofocus value="${m1.subject_code!}"/></td>
            </tr>
            <tr>
                <th>显示名*</th>
                <td><input type="text" id="display_name" value="${m1.display_name!}"/></td>
            </tr>

            <#if m1.subject_type == 0>
                <tr>
                    <th>登录名*</th>
                    <td><input type="text" id="login_name" value="${m1.login_name!}"/></td>
                </tr>

                <tr>
                    <th>登录密码</th>
                    <td><input type="text" id="login_password" value="${m1.login_password!}"/></td>
                </tr>
            </#if>


            <tr>
                <th>排序</th>
                <td><input type="text" id="order_index" value="${m1.order_index!}"/></td>
            </tr>
            <tr>
                <th>备注</th>
                <td><input type="text" id="remark" value="${m1.remark!}"/></td>
            </tr>

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