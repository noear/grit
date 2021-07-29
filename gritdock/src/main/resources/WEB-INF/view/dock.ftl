
<!DOCTYPE HTML>
<html>
<head>
    <title>${env} - ${fun_name} ${timenow!}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        header aside > *{cursor: pointer;}
        header aside a{display:inline-block; height:100%; padding:0 10px; border-left:1px solid #444; }
        header aside a img{vertical-align: middle; width: 14px;}
        header aside t{padding: 0 10px; color: #999!important;}
        header aside .logout{color: #fff; }
        menu > div img{width: 20px;height: 20px; vertical-align: middle;}
    </style>
    <style>
        .layui-layer { box-shadow: 1px 1px 5px rgba(0,0,0,.3);}
    </style>
    <script>
        window.openRes = function (uri) {
            // url ~= /xx/xxx/xxx

            var a = top.location.protocol;
            var b = top.location.hostname;
            var c = top.location.pathname.match(/\.[^/]+/)[0];

            top.location = (a +'//' + b + '/'+ c + uri);
        };


        function _dock_home_open() {
            layer.open({
                type: 2,
                title: false,
                closeBtn: 0,
                shade: [0],
                shadeClose: true,
                anim: 0,
                area: '600px',
                offset: ['60px', 'calc(100% - 610px)'],
                content: '/dock/home',
                success: function(layero, index){
                    layer.iframeAuto(index);
                }
            });
        }
    </script>
</head>
<body>
<@header/>
<main>
    <#if fun_type = 0>
        <left>
            <@leftmenu/>
        </left>
        <right class="frm">
            <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
        </right>
    <#else>
        <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
    </#if>
</main>
</body>
</html>






