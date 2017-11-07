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
//        alert("weChat Oauth Login");
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8b1eb42f8cadbff1&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect';
        location.href = url;
    }

    function validateTarjetaIsRight(tarjeta){
//        alert("validate tarjeta 2,tarjeta="+tarjeta);
        var url = "checkTarjeta";
        var param = {
            tarjeta:tarjeta
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            async:false,
            success: function (data) {
                var validate = data.validate;
//                alert("validate result = " + validate);
                if(validate === "true"){
                    var newTarjeta = data.tarjeta;
                    if(newTarjeta!=null && newTarjeta.trim()!=""){
                        setCookie("tarjeta",newTarjeta);
                    }
                    return true;
                }else{
//                    alert(" validate result over return false");
                    return false;
                }

            },
            error: function (data) {
//                alert("errror");
                return false;
            }
        });

    }

    function validateTarjeta(tarjeta){
        var fromurl = $("#fromurl").val();
        if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){

            tarjeta = $("#tarjeta").val();

//            alert("#tarjeta = " + tarjeta);

            if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){
                //PageContext Empty
                return false;
            }else{
                var isRight =  validateTarjetaIsRight(tarjeta);
                if(isRight){
                    $("#tarjeta").val(getCookie("tarjeta"));
                    alert("#tarjeta="+$("#tarjeta").val());
                    return true;
                }else{
//                    alert("132");
                    weChatOauthLogin(fromurl);
                }
            }
        }else{
            var isRight =  validateTarjetaIsRight(tarjeta);
            if(isRight){
                $("#tarjeta").val(getCookie("tarjeta"));
                alert("#tarjeta="+$("#tarjeta").val());
                return true;
            }
            if(isRight == false){
//                alert("254");
                weChatOauthLogin(fromurl);
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

//                alert("user account validate");

                var tarjeta = getCookie("tarjeta");
//                alert("in cookie tajeta = " + tarjeta);
                //如果Cookie里没有Tarjeta 且PageContext里也没。

                if(!validateTarjeta(tarjeta)){
                    weChatOauthLogin(fromurl);
                }

            }

    );
</script>

