<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        section{display: inline-block; margin-bottom: ${section_margin}px; margin-left: 3px; margin-right: 3px; border-left: #ccc solid 1px; padding-left: 4px;}
        section header{font-weight: bold;margin-bottom: ${header_margin}px;}
        section ul{display: block;}
        section li{display: inline-block; margin: 5px 10px 5px 0px;}
        section li a{color: #444;text-decoration: none;}
    </style>
    <script>
        function jump(pack) {
            $.ajax({
                url:"/dock/jump/",
                data:{pack:pack},
                success:function(data){
                    if(data.code==1)
                        top.location.href=data.url;
                    else
                        alert(data.msg);
                }
            });
            return false;
        }
    </script>
</head>
<body>
<main>
    ${code}
</main>
</body>
</html>






