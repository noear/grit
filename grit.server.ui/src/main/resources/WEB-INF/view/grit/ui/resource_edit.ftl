<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 配置列表-编辑</title>
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
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        // setTimeout(function(){
                        //     location.href="/cfg/prop?tag_name="+vm.tag;
                        // },800);
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
                            parent.location.href="/cfg/prop?tag_name=${m1.tag!}";
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }



        var ext_tools = ace.require("ace/ext/language_tools");

        ext_tools.addCompleter({
            getCompletions: function(editor, session, pos, prefix, callback) {
                callback(null,
                    [
                        {name: "schema",value: "schema", meta: "",type: "local",score: 1000},
                        {name: "url",value: "url", meta: "",type: "local",score: 1000},
                        {name: "username",value: "username", meta: "",type: "local",score: 1000},
                        {name: "password",value: "password", meta: "",type: "local",score: 1000},
                        {name: "server",value: "server", meta: "",type: "local",score: 1000},
                        {name: "user",value: "user", meta: "",type: "local",score: 1000},
                        {name: "name",value: "name", meta: "",type: "local",score: 1000},
                        {name: "accessKeyId",value: "accessKeyId", meta: "",type: "local",score: 1000},
                        {name: "accessSecret",value: "accessSecret", meta: "",type: "local",score: 1000},
                        {name: "regionId",value: "regionId", meta: "",type: "local",score: 1000},
                        {name: "endpoint",value: "endpoint", meta: "",type: "local",score: 1000},
                        {name: "bucket",value: "bucket", meta: "",type: "local",score: 1000}
                    ]);
            }
        });


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
                <#if type==2>
                    资源空间
                <#elseif type==1>
                    资源组
                <#else>
                    资源
                </#if>
            </a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" class="w80" onclick="save()">保存</button>
        <#if is_admin == 1>
            <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>


<detail>
    <form>

        <table>
            <tr>
            <tr>
                <th>代号</th>
                <td><input type="text" id="resource_code" autofocus value="${m1.resource_code!}"/></td>
            </tr>
            <tr>
                <th>显示名</th>
                <td><input type="text" id="display_name" value="${m1.display_name!}"/></td>
            </tr>
            <tr>
                <th>排序</th>
                <td><input type="text" id="order_index" value="${m1.order_index!}"/></td>
            </tr>
            <tr>
                <th>链接路径</th>
                <td><input type="text" id="link_uri" value="${m1.link_uri!}"/></td>
            </tr>
            <tr>
                <th>链接目标</th>
                <td><input type="text" id="link_target" value="${m1.link_target!}"/></td>
            </tr>
            <tr>
                <th>链接标签</th>
                <td><input type="text" id="link_tags" value="${m1.link_tags!}"/></td>
            </tr>
            <tr>
                <th>图标路径</th>
                <td><input type="text" id="icon_uri" value="${m1.icon_uri!}"/></td>
            </tr>
            <tr>
                <th>备注</th>
                <td><input type="text" id="remark" value="${m1.remark!}"/></td>
            </tr>
            <tr>
                <th>是否全屏</th>
                <td><input type="text" id="is_fullview" value="${m1.is_fullview!}"/></td>
            </tr>
            <tr>
                <th>是否可见</th>
                <td><input type="text" id="is_visibled" value="${m1.is_visibled!}"/></td>
            </tr>
            <tr>
                <th>是否禁用</th>
                <td><input type="text" id="is_disabled" value="${m1.is_disabled!}"/></td>
            </tr>
            <tr>
                <th>扩展属性</th>
                <td>
                    <div style="line-height: 1em;">
                        <boxlist>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('text')" value="text" /><a>text</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('properties')" value="properties" /><a>properties</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('yaml')" value="yaml" /><a>yaml</a></label>
                            <label><input type="radio" name="edit_mode" onclick="editor_shift('json')" value="json" /><a>json</a></label>
                        </boxlist>
                    </div>
                    <div>
                        <textarea id="attrs" class="hidden">${m1.attrs!}</textarea>
                        <pre style="height:300px;width:calc(100vw - 260px);"  id="attrs_edit">${m1.attrs!}</pre>
                    </div>
                </td>
            </tr>

        </table>
    </form>
</detail>

</body>
</html>