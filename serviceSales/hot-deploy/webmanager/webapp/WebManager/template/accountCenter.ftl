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

    function validateTarjetaIsRight(tarjeta){
        alert("validate tarjeta 2");
        var url = "checkTarjeta";
        var param = {
            tarjeta:tarjeta
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {
                var validate = data.validate;
                alert("validate result = " + validate);
                if(validate==="true"){
                    var newTarjeta = data.tarjeta;
                    if(newTarjeta!=null && newTarjeta.trim()!=""){
                        setCookie("tarjeta",newTarjeta);
                        return true;
                    }
                    return true;
                }else{
                    return false;
                }

            },
            error: function (data) {
                return false;
            }
        });


        return false;
    }

    function validateTarjeta(tarjeta){
        //CookieTarjeta Empty
        alert("validateTarjeta = " + tarjeta);

        if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){

            tarjeta = $("#tarjeta").val();

            alert("#tarjeta = " + tarjeta);

            if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){
                //PageContext Empty
                return false;
            }else{
                if(validateTarjetaIsRight(tarjeta)){
                    $("#tarjeta").val(getCookie("tarjeta"));
                    return true;
                }
            }
        }else{
            if(validateTarjetaIsRight(tarjeta)){
                $("#tarjeta").val(getCookie("tarjeta"));
                return true;
            }
        }
        return true;
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

    //清除cookie
    function clearCookie(name) {
        setCookie(name, "", -1);
    }

    $(
            function(){
               // clearCookie("tarjeta");

                //回调地址
                var fromurl = $("#fromurl").val();

//                alert(fromurl);

                alert("user account validate");

                var tarjeta = getCookie("tarjeta");
                alert("in cookie tajeta = " + tarjeta);
                //如果Cookie里没有Tarjeta 且PageContext里也没。

                if(!validateTarjeta(tarjeta)){

                    weChatOauthLogin(fromurl);

                }

            }

    );
</script>

