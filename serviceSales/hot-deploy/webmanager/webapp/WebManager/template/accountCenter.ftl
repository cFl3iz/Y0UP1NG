<!-- 此后所有验证授权因用Tarjeta保存 -->




<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

<script>
    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }

    function setCookie(name,value)
    {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days*24*60*60*1000);

        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
    }

    function weChatOauthLogin(fromurl){
        alert("weChat Oauth Login");
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8b1eb42f8cadbff1&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect';
        location.href = url;
    }

    function validateTarjeta(tarjeta){
        //CookieTarjeta Empty
        alert("#tarjeta = " + $("#tarjeta").val());
        if(isEmpty(tarjeta)){
            tarjeta = $("#tarjeta").val();

            if(isEmpty(tarjeta)){
                //PageContext Empty
                return false;
            }else{
                alert("set tarjeta ok !");
                setCookie("tarjeta",tarjeta);
            }
        }
//        else{
//            var tjt = "<input type='hidden' id='tarjeta' value='" + tarjeta +   "'/>";
//            $("#fromurl").parent().append(tjt);
//        }
    }

    function isEmpty(obj)
    {
        for (var name in obj)
        {
            return false;
        }
        return true;
    };

    $(
            function(){
                //回调地址
                var fromurl = $("#fromurl").val();

                alert(fromurl);

                alert("user account validate");

                var tarjeta = getCookie("tarjeta");

                //如果Cookie里没有Tarjeta 且PageContext里也没。

                if(!validateTarjeta(tarjeta)){

                    weChatOauthLogin(fromurl);

                }

            }

    );
</script>

