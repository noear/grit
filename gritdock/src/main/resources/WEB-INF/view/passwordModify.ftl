<!DOCTYPE HTML>
<html>
<head>
    <title>修改密码</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script type="text/javascript" src="${js}/lib.js" ></script>
    <script src="${js}/layer.js"></script>
    <script type="text/javascript">
        function confirm() {
            top.layer.confirm('确认修改?', {
                btn: ['确定','取消'] //按钮
            }, function(){
                top.layer.close(top.layer.index);
                var newPass = $("#newPass").val();
                var oldPass = $("#oldPass").val();
                var conNewPass = $("#conNewPass").val();

                if(oldPass==null||oldPass==""){
                    top.layer.msg("原密码不能为空")
                } else if(newPass==null||newPass=="" || conNewPass==null||conNewPass=="") {
                    top.layer.msg("新密码不能为空")
                } else if (newPass != conNewPass){
                    top.layer.msg("请输入相同的新密码")
                } else {
                    $.ajax({
                        type:"POST",
                        url:"/user/confirmModify",
                        data:{"newPass":newPass,"oldPass":oldPass},
                        success: function(data){
                            if(data.code == 1){
                                layer.msg('修改成功,去重新登陆吧');
                                setTimeout(function(){
                                    var index = top.layer.getFrameIndex(window.name);

                                    top.layer.close(index);
                                },1500);

                            } else {
                                layer.msg(data.msg)
                            }
                        }
                    });
                }

            });
        }
    </script>
    <style>
        detail form input{width: 200px!important;}
        detail table{margin: auto;}
    </style>
</head>
<body>
<detail>
    <form>
        <table>
            <tr>
                <th>原密码：</th><td><input type="password" id="oldPass" ></td>
            </tr>
            <tr>
                <th>新密码：</th><td><input type="password" id="newPass" ></td>
            </tr>
            <tr>
                <th>确认新密码：</th><td><input type="password" id="conNewPass" ></td>
            </tr>
            <tr>
                <th></th><td><button type="button" onclick="confirm()">修改</button></td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>
