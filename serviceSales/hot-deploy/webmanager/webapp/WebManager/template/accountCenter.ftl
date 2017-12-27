<!-- 此后所有验证授权因用Tarjeta保存 -->




<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

<!-- TODO FIX ME -->

<#--<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" />-->

<script>

    function checkSubscribe(){
        var flag = false;
        var subscribe = getCookie("subscribe");
//        alert("IN COOKIE subscribe = " + subscribe);
        if(subscribe === "1" ){
            flag =  true;
        }else{
//           $("#showboxmenu1").show();
            //清Cookie中的登录数据,以便用户再次访问时刷新是否订阅。
            clearCookie("tarjeta");
            flag = false;
        }
        return flag;
    }


    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }

    function setCookie(name,value)
    {
//        alert("IN SET COOKIE NAME = " + name +"|value="+value);
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days*24*60*60*1000);

        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
    }

    function weChatOauthLogin(fromurl){
     //  alert("weChat Oauth Login");
//        alert("fromurl =" + fromurl);
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8b1eb42f8cadbff1&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect';
        location.href = url;
    }

    function validateTarjetaIsRight(tarjeta){
//        alert("validate tarjeta 2,tarjeta="+tarjeta);
        var url = "checkTarjeta";
        var param = {
            tarjeta:tarjeta
        };
        var result = false;
//       alert("result  = " + result);
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            async:false,
            success: function (data) {
                var validate = data.validate;
//               alert("validate result = " + validate);
//                alert("validate="+validate);
                if(validate === "true"){
                    var newTarjeta = data.tarjeta;
                    $("#tarjeta").val(tarjeta);
//                    setCookie("tarjeta",tarjeta);
                    if(newTarjeta!=null && newTarjeta.trim()!=""){
//                        alert("setCookie = " + newTarjeta);
                        setCookie("tarjeta",newTarjeta);
                        $("#tarjeta").val(newTarjeta);
                    }
                    result = true;

                }else{
//                   alert("return false");
                    clearCookie("tarjeta");
//                   alert(" validate result over return false");

                }

            },
            error: function (data) {
                alert("errror");

            }
        });

//        alert("result  = " + result);
        return result;
    }

    function validateTarjeta(tarjeta){

        var fromurl = $("#fromurl").val();



        if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){

            tarjeta = $("#tarjeta").val();

//           alert("#555tarjeta = " + tarjeta);

            if(tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined" ){
                //PageContext Empty
                return false;
            }else{
                var isRight =  validateTarjetaIsRight(tarjeta);
                if(isRight){
//                    alert("setCookie = " + $("#tarjeta").val());
                    setCookie("subscribe",$("#subscribe").val());
                    setCookie("tarjeta",$("#tarjeta").val());
                    setCookie("partyId",$("#partyId").val());
                    return true;
                }else{
//                   alert("132");

                    weChatOauthLogin(fromurl);
                }
            }
        }else{
            var isRight =  validateTarjetaIsRight(tarjeta);
//            alert("isRight = " + isRight);
            if(isRight){
                $("#tarjeta").val(getCookie("tarjeta"));
                var partyId = getCookie("partyId");

                $("#partyId").val(partyId);

//                alert("#tarjeta="+$("#tarjeta").val());
                return true;
            }else{
//               alert("254");
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

//                alert("onload from url = "+ fromurl);

//               alert("user account validate");

                var tarjeta = getCookie("tarjeta");

//                alert("in cookie tajeta = " + tarjeta);
                //如果Cookie里没有Tarjeta 且PageContext里也没。

                var link = location.href;
                alert("onload="+link);
                var url = 'http://www.yo-pe.com/api/common/'+link+'/wxJsRegister';
                alert("url =" + url);
                $.ajax({
                    type: 'GET',
                    url: url,
                    data: "",
                    success: function (data) {
                        alert(data);
                        alert("register success");
//                        wx.config({
//                            debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来
//                            appId: data.appId, // 必填，公众号的唯一标识
//                            timestamp: data.timestamp, // 必填，生成签名的时间戳
//                            nonceStr: data.nonceStr, // 必填，生成签名的随机串
//                            signature: data.signature,// 必填，签名，见附录1
//                            jsApiList: [
//                                "onMenuShareTimeline",
//                                "onMenuShareAppMessage"
//                            ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//                        });
                    },
                    error: function (data) {
                        alert("ERROR :" + data.status);
                    }
                });



                // do wx js
//                $.ajax({
//                    url:"http://www.yo-pe.com/api/common/"+"http://www.lyndonspace.com"+"/wxJsRegister",//后台给你提供的接口
//                    type:"GET",
//                    async:false,
//                    success:function (data){
//
//                        wx.error(function (res) {
//                            alert(res);
//                        });
//                    },
//                    error:function (error){
//                        alert(error)
//                    }
//                });


                //验证登录

//                if(!validateTarjeta(tarjeta)){
//                    weChatOauthLogin(fromurl);
//                }

            }

    );
</script>

