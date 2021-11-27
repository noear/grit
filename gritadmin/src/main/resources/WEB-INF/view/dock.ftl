
<!DOCTYPE HTML>
<html>
<head>
    <title>GRIT - 权限管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="/css/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="/js/jtadmin.js"></script>
    <script src="/js/layer.js"></script>
    <style>
        body > header aside a{display:inline-block; height:100%; padding:0 15px; }
        body > header aside .split{border-left:1px solid #444;}
    </style>
</head>
<body>
<header>
    <label title='v2.4.2'>GRIT</label>
    <nav>
    </nav>
    <aside>
        <a><i class='fa fa-user'></i> 管理员</a>
        <a class='split' href='/admin/@设置?@='><i class='fa fa-cogs'></i></a>
        <a class='split' href='/'><i class='fa fa-fw fa-circle-o-notch'></i>退出</a>
    </aside>
</header>
<main>
    <left>
        <menu>
            <div onclick="$('main').toggleClass('smlmenu');if(window.onMenuHide){window.onMenuHide();}"><i class='fa fa-bars'></i></div>
            <items>
                <a class='sel' href='/grit/resource/space' target="dock">资源空间</a>
                <a href='/grit/resource/group' target="dock">资源组</a>
                <a href='/grit/resource/entity' target="dock">资源</a>
                <br /><br />
                <a href='/grit/subject/group' target="dock">主体组</a>
                <a href='/grit/subject/entity' target="dock">主体</a>
                <a href='/grit/config/' target="dock">权限配置</a>
            </items>
        </menu>
    </left>
    <right class="frm">
        <iframe src="/grit/resource/space" frameborder="0" name="dock"></iframe>
    </right>
</main>
</body>
</html>






