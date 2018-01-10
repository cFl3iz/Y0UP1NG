<!-- 此后所有验证授权因用Tarjeta保存 -->


<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

<!-- TODO FIX ME -->


<script>
    //弹出隐藏层
    function ShowDiv(show_div,bg_div){
        document.getElementById(show_div).style.display='block';
        document.getElementById(bg_div).style.display='block' ;
        var bgdiv = document.getElementById(bg_div);
        bgdiv.style.width = document.body.scrollWidth;
        // bgdiv.style.height = $(document).height();
        $("#"+bg_div).height($(document).height());
    };
    //关闭弹出层
    function CloseDiv(show_div,bg_div)
    {
        document.getElementById(show_div).style.display='none';
        document.getElementById(bg_div).style.display='none';
    };
    function checkSubscribe() {
        var flag = false;
        var subscribe = getCookie("subscribe");
//        alert("IN COOKIE subscribe = " + subscribe);
        if (subscribe === "1") {
            flag = true;
        } else {
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

    function setCookie(name, value) {
//        alert("IN SET COOKIE NAME = " + name +"|value="+value);
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);

        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }

    function weChatOauthLogin(fromurl) {
        //  alert("weChat Oauth Login");
//        alert("fromurl =" + fromurl);
        var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8b1eb42f8cadbff1&redirect_uri=' + encodeURIComponent(fromurl) + '&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect';
        location.href = url;
    }

    function validateTarjetaIsRight(tarjeta) {
//        alert("validate tarjeta 2,tarjeta="+tarjeta);
        var url = "checkTarjeta";
        var param = {
            tarjeta: tarjeta
        };
        var result = false;
//       alert("result  = " + result);
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            async: false,
            success: function (data) {
                var validate = data.validate;
                var partyId  = data.partyId;
//               alert("validate result = " + validate);
//                alert("validate="+validate);
                if (validate === "true") {
                    var newTarjeta = data.tarjeta;
                    $("#tarjeta").val(tarjeta);
                    //将当前用户的partyId放到页面
//                    alert("partyId="+partyId);
                    $("#partyId").val(partyId);

//                    setCookie("tarjeta",tarjeta);
                    if (newTarjeta != null && newTarjeta.trim() != "") {
//                        alert("setCookie = " + newTarjeta);
                        setCookie("tarjeta", newTarjeta);
                        $("#tarjeta").val(newTarjeta);
                    }
                    result = true;

                } else {
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

    function validateTarjeta(tarjeta) {

        var fromurl = $("#fromurl").val();


        if (tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined") {

            tarjeta = $("#tarjeta").val();

//           alert("#555tarjeta = " + tarjeta);

            if (tarjeta == null || tarjeta.trim() == "" || tarjeta == "undefined") {
                //PageContext Empty
                return false;
            } else {
                var isRight = validateTarjetaIsRight(tarjeta);
                if (isRight) {
//                    alert("setCookie = " + $("#tarjeta").val());
                    setCookie("subscribe", $("#subscribe").val());
                    setCookie("tarjeta", $("#tarjeta").val());
                    setCookie("partyId", $("#partyId").val());
                    return true;
                } else {
//                   alert("132");

                    weChatOauthLogin(fromurl);
                }
            }
        } else {
            var isRight = validateTarjetaIsRight(tarjeta);
//            alert("isRight = " + isRight);
            if (isRight) {
                $("#tarjeta").val(getCookie("tarjeta"));
                var partyId = getCookie("partyId");

                $("#partyId").val(partyId);

//                alert("#tarjeta="+$("#tarjeta").val());
                return true;
            } else {
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

    function isEmpty(obj) {
        for (var name in obj) {
            return false;
        }
        return true;
    }
    ;

    //清除cookie
    function clearCookie(name) {
        setCookie(name, "", -1);
    }

    $(
            function () {
                // clearCookie("tarjeta");

                //回调地址
                var fromurl = $("#fromurl").val();

                var tarjeta = getCookie("tarjeta");


                //验证Tarjeta

                if (!validateTarjeta(tarjeta)) {
                    weChatOauthLogin(fromurl);
                }



                //如果Cookie里没有Tarjeta 且PageContext里也没。

                var link = location.href;

                var url = 'https://www.yo-pe.com/api/common/wxJsRegister';

                var ajaxData = {
                    link: link
                };
                $.ajax({
                    type: 'POST',
                    url: url,
                    async: false,
                    data: ajaxData,
                    dataType: "json",
                    timeout: 50000, //超时时间：50秒
                    success: function (data) {
//                        alert(JSON.stringify(data));
//                        alert("register success");
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来
                            appId: data.data.appId, // 必填，公众号的唯一标识
                            timestamp: data.data.timestamp, // 必填，生成签名的时间戳
                            nonceStr: data.data.nonceStr, // 必填，生成签名的随机串
                            signature: data.data.signature,// 必填，签名，见附录1
                            jsApiList: [
                                "onMenuShareTimeline",
                                "onMenuShareAppMessage"
                            ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                        });
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                });


                wx.ready(function () {

//                    alert("非常不容易,wx.ready认证成功了");



                    //验证是否可用
//                    wx.checkJsApi({
//                        jsApiList: [
//                            'getLocation',
//                            'onMenuShareTimeline',
//                            'onMenuShareAppMessage'
//                        ],
//                        success: function (res) {
//                            alert(JSON.stringify(res));
//                        }
//                    });

                    function UnicodeToUtf8(unicode) {
                        var uchar;
                        var utf8str = "";
                        var i;

                        for(i=0; i<unicode.length;i+=2){
                            uchar = (unicode[i]<<8) | unicode[i+1];               //UNICODE为2字节编码，一次读入2个字节
                            utf8str = utf8str  + String.fromCharCode(uchar);    //使用String.fromCharCode强制转换
                        }
                        return utf8str;
                    }


                    var partyId = $("#partyId").val();

                    var linkUrl = "https://www.yo-pe.com/productjump/${(productId)!}/${(spm)!},"+ partyId +"/${(payToPartyId)!}";
                    var title = "${(resourceDetail.productName)!}";
                    var imgUrl = "${(resourceDetail.detailImageUrl)!}";
                    alert(title);
                    alert(imgUrl);
                    wx.onMenuShareAppMessage({
                        title:title, // 分享标题
                        desc: '这是我的秘密...', // 分享描述
                        link: linkUrl, // 分享链接
                        imgUrl:imgUrl, // 分享图标
                        type: '', // 分享类型,music、video或link，不填默认为link
                        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                        success: function () {
                            alert("我们知道你分享给了谁,卖家也会知道新客户是通过 '你的分享' 联系他的。");
// 用户确认分享后执行的回调函数
                        },
                        cancel: function () {
                            alert("万万没想到,你居然取消了分享! 是不好意思了吗??");
// 用户取消分享后执行的回调函数
                        }
                    });


                    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
                });

                wx.error(function (res) {
// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
//                    alert("认证失败=" + res);
                    location.reload();
                });




            }
    );
</script>



<!-- Buy Product Model -->
<script>
    function buyProductModel(){
        $("#showboxmenu2").show();
        $("#showboxmenuspec2").show();
    }

    function buyProduct(){



        var amount = $("#cool").val();

        var confirmMessage = $("#confirmMessage").val();

        var a=confirm(confirmMessage);

        if(a == true)
        {
            var tarjeta = $("#tarjeta").val();
            var payToParty = ${(resourceDetail.payToPartyId)!};
            var productId = ${(productId)!};
            var prodCatalogId = ${(resourceDetail.prodCatalogId)!};
            var productStoreId = ${(resourceDetail.productStoreId)!};
            var url = "placeResourceOrder";

            var param = {
                payToPartyId:payToParty,
                productId:productId,
                prodCatalogId:prodCatalogId,
                productStoreId:productStoreId,
                tarjeta:tarjeta,
                amount:amount
            };
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                async:false,
                success: function (data) {
                    //   alert("code="+data.code);
                    if(data.code === "200"){
                        var orderId = data.orderId;
                        location.href = "viewWebOrderDetail?orderId="+orderId+"&tarjeta="+tarjeta;
                    }
                    if(data.code === "500"){
                        alert("CODE-403:网络出现问题请刷新页面重试");
                    }

                },
                error: function (data) {
                    alert("CODE-403:网络出现问题请刷新页面重试");
                }
            });

        }
        else
        {
            return false;
        }
    }
</script>

<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" charset="gb2312"/>