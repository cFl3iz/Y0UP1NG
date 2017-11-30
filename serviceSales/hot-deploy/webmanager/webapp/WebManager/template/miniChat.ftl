

<head>
    <meta charset="utf-8"/>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <meta name="apple-touch-fullscreen" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0 user-scalable=no" media="screen"/>
    <title>${uiLabel.to}${(payToPartyFirstName)!}${uiLabel.chat}</title>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=NYGTLd8V2Lz3cGsolRnZzXaHmuXW2XZ3"></script>
    <#--<link rel="stylesheet" href="http://cdn.amazeui.org/amazeui/2.5.0/css/amazeui.min.css" />-->
    <#--<script src="//twemoji.maxcdn.com/2/twemoji.min.js?2.2.2"></script>-->
    <style type="text/css">





        -webkit-overflow-scrolling:touch;

        #allmap{height:80px;width:100px;}
        #r-result{width:100%; font-size:15px;}

        body {
            background: url(http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/chatBG.jpeg) no-repeat;
            background-size: 100%;
            background-attachment: fixed;

        }

        @media all and (min-width: 640px) {
            body, html, .wenwen-footer, .speak_window {
                width: 640px !important;
                margin: 0 auto
            }

            .speak_window, .wenwen-footer {
                left: 50% !important;
                margin-left: -320px
            }
        }

        input, button {
            outline: none;
        }

        .wenwen-footer {
            width: 100%;
            position: fixed;
            bottom: -5px;
            left: 0;
            background: #fff;
            padding: 3%;
            border-top: solid 1px #ddd;
            box-sizing: border-box;
        }

        .wenwen_btn, .wenwen_help {
            width: 15%;
            text-align: center;
        }

        .wenwen_btn img, .wenwen_help img {
            height: 36px;
        }

        .wenwen_text {
            height: 36px;

            border-radius: 5px;
            border: solid 1px #636162;
            box-sizing: border-box;
            width: 66%;
            text-align: center;
            overflow: hidden;
            margin-left: 2%;
        }

        .circle-button {
            padding: 0 5px;
        }

        .wenwen_text .circle-button {
            font-size: 16px;
            color: #666;
            line-height: 38px;
        }

        .write_box {
            background: #fff;
            width: 100%;
            height: 40px;
            line-height: 40px;
            display: none;
        }

        .write_box input {
            height: 40px;
            padding: 0 5px;
            font-size:17px;
            line-height: 40px;
            width: 100%;
            box-sizing: border-box;
            border: 0;
        }

        .wenwen_help button {
            width: 95%;
            background: #42929d;
            color: #fff;
            border-radius: 5px;
            border: 0;
            height: 40px;
            display: none;
        }

        #wenwen {
            height: 100%;
        }

        .speak_window {
            overflow-y: scroll;
            height: 100%;
            width: 100%;
            position: fixed;
            top: 0;
            left: 0;
        }

        .speak_box {
            margin-bottom: 70px;
            padding: 10px;
        }

        .question, .answer {
            margin-bottom: 1rem;
        }

        .question {
            text-align: right;
        }

        .question > div {
            display: inline-block;
        }

        .left {
            float: left;
        }

        .right {
            float: right;
        }

        .clear {
            clear: both;
        }

        .heard_img {
            height: 45px;
            width: 45px;
            border-radius: 5px;
            overflow: hidden;
            background: #ddd;
        }

        .heard_img img {
            width: 100%;
            height: 100%
        }

        .question_text, .answer_text {
            box-sizing: border-box;
            position: relative;
            display: table-cell;
            min-height: 60px;
        }

        .question_text {
            padding-right: 20px;
        }

        .answer_text {
            padding-left: 20px;
        }

        .question_text p, .answer_text p {
            border-radius: 10px;
            padding: .5rem;
            margin: 0;
            font-size: 15px;
            line-height: 28px;
            box-sizing: border-box;
            vertical-align: middle;
            display: table-cell;
            height: 60px;
            word-wrap: break-word;
        }

        .answer_text p {
            background: #fff;
        }

        .question_text p {
            background: #42929d;
            color: #fff;
            text-align: left;
        }

        .question_text i, .answer_text i {
            width: 0;
            height: 0;
            border-top: 5px solid transparent;
            border-bottom: 5px solid transparent;
            position: absolute;
            top: 25px;
        }

        .answer_text i {
            border-right: 10px solid #fff;
            left: 10px;
        }

        .question_text i {
            border-left: 10px solid #42929d;
            right: 10px;
        }

        .answer_text p a {
            color: #42929d;
            display: inline-block;
        }

        audio {
            display: none;
        }

        .saying {
            position: fixed;
            bottom: 30%;
            left: 50%;
            width: 120px;
            margin-left: -60px;
            display: none;
        }

        .saying img {
            width: 100%;
        }

        .write_list {
            font-size: 18px;
            position: absolute;
            left: 0;
            width: 100%;
            background: #fff;
            border-top: solid 1px #ddd;
            padding: 5px;
            line-height: 30px;
        }


    </style>

    <script>

        var messagesArray = new Array();

        var images  = new Array();

        /**
         * 更改订单中的支付状态
         */
        function setPaymentStatus(paymentId,orderId){

            var url = "setOrderPaymentStatus";
            var tarjeta = $("#tarjeta").val();
            var payToPartyId = $("#payToPartyId").val();
            var param = {
                tarjeta:tarjeta,
                paymentId:paymentId,
                payToPartyId:payToPartyId,
                orderId:orderId
            };
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                success: function (data) {
                    alert(data);
                },
                error: function (data) {
                    alert("ERROR :" + data.status);
                }
            });


        }


        //刷新消息
        function flushMessage() {

            var url = "queryMessage";
            var param = {
                payToPartyId:${(payToPartyId)!}
            };
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                success: function (data) {
                    alert(data);
                },
                error: function (data) {
                    alert("ERROR :" + data.status);
                }
            });
        }


        $(
                function () {

                    $("#wenwen_btn_left").click();

                    //初始化历史聊天记录
                    iniChatPage();
//                    $('body').on('touchmove touchstart', function (event) {
//                        event.preventDefault();
//                    });

                }
        );
    </script>
</head>
<body>
<input type="hidden" value="${uiLabel.About}" id="about"/>

<ul data-am-widget="gallery" class="am-gallery am-avg-sm-2
  am-avg-md-3 am-avg-lg-4 am-gallery-overlay" data-am-gallery="{ pureview: true }" >

<div class="speak_window">
    <div class="speak_box">
        <div class="answer" id="firstAboutMessage" style="display:none;">
            <div class="heard_img left">
                <img src="${(payToPartyHead)!}">
            </div>
            <div class="answer_text" >
                <p >${uiLabel.About}<br/>
                <#--<a href="#">${uiLabel.BuyNow}</a>-->
                </p>
                <i></i>
            </div>
        </div>
    </div>
</div>
<div class="saying">
    <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saying.gif">
</div>
<div class="wenwen-footer">
    <div class="wenwen_btn left" id="wenwen_btn_left" onClick="to_write()">
        <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jp_btn.png">
    </div>
    <div class="wenwen_text left">
        <div class="write_box">
            <input type="text" class="left" onKeyUp="keyup()" placeholder=""/>
        </div>
        <div class="circle-button" id="wenwen">
        ${uiLabel.clickOnSay}
        </div>
    </div>
    <div class="wenwen_help right">
        <a href="javascript:window.history.back();">
            <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/help_btn.png">
        </a>
        <button onClick="up_say()" class="right">${uiLabel.send}</button>
    </div>
    <div style="opacity:0;" class="clear"></div>
</div>
<#--<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>-->
<input type="hidden" id="payToPartyId" value="${(payToPartyId)!}"/>
<input type="hidden" id="payFromPartyId" value="${(personInfo.partyId)!}"/>
<input type="hidden" id="tarjeta" value="${(tarjeta)!}"/>
<input type="hidden" id="productId" value="${(productId)!}"/>

<input type="hidden" id="payToPartyHead" value="${(payToPartyHead)!}"/>
<input type="hidden" id="payFromPartyHead" value="${(personInfo.headPortrait)!}"/>

<script type="text/javascript">





    // 用经纬度设置地图中心点
    function theLocation(name,longitude,latitude){
        // 百度地图API功能
        var map = new BMap.Map("allmap");
        map.centerAndZoom(new BMap.Point(116.331398,39.897445),11);
        map.enableScrollWheelZoom(true);

        if(longitude != "" && latitude != ""){
            map.clearOverlays();
            var new_point = new BMap.Point(longitude,latitude);
            var marker = new BMap.Marker(new_point);  // 创建标注
            map.addOverlay(marker);              // 将标注添加到地图中
            map.panTo(new_point);
        }
    }


    var payToPartyHead = $("#payToPartyHead").val();
    var payFromPartyHead = $("#payFromPartyHead").val();
    payToPartyHead   = payToPartyHead   + "?x-oss-process=image/resize,w_50,h_50/quality,q_60";
    payFromPartyHead = payFromPartyHead + "?x-oss-process=image/resize,w_50,h_50/quality,q_60";
    function to_write() {
        $('.wenwen_btn img').attr('src', 'http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/yy_btn.png');
        $('.wenwen_btn').attr('onclick', 'to_say()');
        $('.write_box,.wenwen_help button').show();
        $('.circle-button,.wenwen_help a').hide();
        $('.write_box input').focus();
        for_bottom();
    }

    function to_say() {
        $('.write_list').remove();
        $('.wenwen_btn img').attr('src', 'http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jp_btn.png');
        $('.wenwen_btn').attr('onclick', 'to_write()');
        $('.write_box,.wenwen_help button').hide();
        $('.circle-button,.wenwen_help a').show();
    }

    /*
    初始化聊天界面,如果已经有历史聊天记录就显示出来
    */
    function iniChatPage(){
        var payToPartyId = $("#payToPartyId").val();
        var payFromPartyId = $("#payFromPartyId").val();
        var tarjeta = $("#tarjeta").val();

        var url = "loadMessage";

        var param = {
            partyIdTo: payFromPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payToPartyId,
            bizType: "webChat"
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {

                var messages = eval(data.messages);

                for (var i = 0; i < messages.length; i++) {
//                    alert(messages[i].messageId+" ");
                    var partyIdTo = messages[i].user.toPartyId;

                    var text =  messages[i].text;
                 //   alert("toPartyId="+partyIdTo);
                //    alert("text= "+ text);
                    var bool = (payToPartyId === partyIdTo);
               //     alert("bool = " + bool+"");

                    var messageId = messages[i].messageId;

                    var messageLogTypeId =   messages[i].messageLogTypeId;

                    // True 等于 左边
                    if (bool) {
                        if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){


                            var ans =  '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            //text = text + "?x-oss-process=image/resize,w_200,h_160/quality,q_100";
                            ans += '<div class="answer_text"><img style="width:260px;height:356px;no-repeat;"  src=' + text + '></img><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);

                        }
                        if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){
                            $("#allmap").replaceWith("<p>${uiLabel.ErrorLocation}</p>");
                            var latitude = messages[i].latitude;
                            var longitude = messages[i].longitude;
                            var tip = "${uiLabel.MyLocation}";
                            var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                            var ans =  '<a href="'+baiduUrl+'">'+ '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><div style="width:250px;height:120px;" id="' + "allmap" + '"></div><i></i>';
                            ans += '</div></div></a>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
                                 theLocation(longitude,longitude,latitude);
                        }
                       // autoWidth();
                     //   for_bottom();
                      //  for_bottom();
                    }else{

                        if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){

                            var str = '<div class="question">';
                          //  text = text + "?x-oss-process=image/resize,w_200,h_160/quality,q_100";
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str += '<div class="question_text clear"><img style="width:260px;height:356px;no-repeat;"  src=' + text + '></img><i></i>';
                            str += '</div></div>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');


                        }
                        if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                            var str = '<div class="question">';
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str += '<div class="question_text clear"><p>' + text + '</p><i></i>';
                            str += '</div></div>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){

                            $("#allmap").replaceWith("<p>${uiLabel.ErrorLocation}</p>");

                            var latitude = messages[i].latitude;
                            var longitude = messages[i].longitude;
                            var tip = "${uiLabel.MyLocation}";
                            var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                            var str =  '<a href="'+baiduUrl+'">'+ '<div class="question">';
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str +=  '<div class="question_text clear"><div style="width:250px;height:120px;" id="' + "allmap" + '"></div><i></i>';
                            str += '</div></div></a>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');

                            theLocation(longitude,longitude,latitude);
                        }


//                        $('.write_box input').focus();

                    //    for_bottom();
                    }
//                    messagesArray.push(messages[i].messageId);

                }
                autoWidth();
                for_bottom();

//                alert(messagesArray);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });

        if(messagesArray.length == null){
            $("#firstAboutMessage").show();
        }




    }


    /**
     * 发送说的话
     */
    function up_say() {
        $('.write_list').remove();

        var text = $('.write_box input').val(),
                str = '<div class="question">';
        str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
        str += '<div class="question_text clear"><p>' + text + '</p><i></i>';
        str += '</div></div>';

        if (text == '') {
            alert('NULL!');
            $('.write_box input').focus();
        } else {
            $('.speak_box').append(str);
            $('.write_box input').val('');
//            $('.write_box input').focus();
            autoWidth();
            for_bottom();

        }

        var payToPartyId = $("#payToPartyId").val();
        var payFromPartyId = $("#payFromPartyId").val();
        var tarjeta = $("#tarjeta").val();
        var productId = $("#productId").val();
        var url = "pushMessage";
//        alert("productId = " + productId);
        var param = {
            partyIdTo: payToPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payFromPartyId,
            text: text,
            objectId: productId
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {
//                alert(data.messages);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });
    }


    setInterval(function () {

        var payToPartyId = $("#payToPartyId").val();

        var payFromPartyId = $("#payFromPartyId").val();

        var tarjeta = $("#tarjeta").val();

        var url = "loadMaiJiaMessage";

        var param = {
            partyIdTo: payFromPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payToPartyId,
            bizType: "webChat"
        };
        //alert("messagesArray="+messagesArray);
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {
                var messages = eval(data.messages);

                for (var i = 0; i < messages.length; i++) {
                    var messageLogTypeId =   messages[i].messageLogTypeId;
                    var messageId = messages[i].messageId;
                    var text =  messages[i].text;
                    if ( 0 == messagesArray.length) {

                        if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){


                            var ans =  '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                       //     text = text + "?x-oss-process=image/resize,w_200,h_160/quality,q_100";
                            ans += '<div class="answer_text"><img style="width:260px;height:356px;no-repeat;"  src=' + text + '></img><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);

                        }
                        if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){
                            $("#allmap").replaceWith("<p>${uiLabel.ErrorLocation}</p>");
                            var latitude = messages[i].latitude;
                            var longitude = messages[i].longitude;
                            var tip = "${uiLabel.MyLocation}";
                            var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                            var ans =  '<a href="'+baiduUrl+'">'+ '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><div style="width:250px;height:120px;" id="' + "allmap" + '"></div><i></i>';
                            ans += '</div></div></a>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
                            theLocation(longitude,longitude,latitude);
                        }




//                        messagesArray.push(messageId);
//                        var text =  messages[i].text;
//                        var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
//                        ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
//                        ans += '</div></div>';
//                        $('.speak_box').append(ans);
                        autoWidth();
                       for_bottom();
                    }else{

                        if($.inArray(messageId, messagesArray)>=0){

                        }else{
                            if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){


                                var ans =  '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                              //  text = text + "?x-oss-process=image/resize,w_200,h_160/quality,q_100";
                                ans += '<div class="answer_text"><img style="width:260px;height:356px;no-repeat;"  src=' + text + '></img><i></i>';
                                ans += '</div></div>';
                                $('.speak_box').append(ans);
                                messagesArray.push(messageId);

                            }
                            if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                                var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                                ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                                ans += '</div></div>';
                                $('.speak_box').append(ans);
                                messagesArray.push(messageId);
                            }
                            if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){
                                $("#allmap").replaceWith("<p>${uiLabel.ErrorLocation}</p>");
                                var latitude = messages[i].latitude;
                                var longitude = messages[i].longitude;
                                var tip = "${uiLabel.MyLocation}";
                                var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                                var ans =  '<a href="'+baiduUrl+'">'+ '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                                ans += '<div class="answer_text"><div style="width:250px;height:120px;" id="' + "allmap" + '"></div><i></i>';
                                ans += '</div></div></a>';
                                $('.speak_box').append(ans);
                                messagesArray.push(messageId);
                                theLocation(longitude,longitude,latitude);
                            }
//                            messagesArray.push(messageId);
//                            var text =  messages[i].text;
//                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
//                            ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
//                            ans += '</div></div>';
//                            $('.speak_box').append(ans);
                            autoWidth();
                            for_bottom();
                        }
                    }
//                    messagesArray.push(messages[i].messageId);

                }
//                alert(messagesArray);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });


    }, 8000);


    //输入文字的放大镜效果
    function keyup() {
        var footer_height = $('.wenwen-footer').outerHeight(),
                text = $('.write_box input').val(),
                str = '<div class="write_list">'+text+'</div>';
        if (text == '' || text == undefined){
            $('.write_list').remove();
        }else{
            $('.wenwen-footer').append(str);
            $('.write_list').css('bottom',footer_height);
        }
    }

    var wen = document.getElementById('wenwen');
    function _touch_start(event) {
        event.preventDefault();
        $('.wenwen_text').css('background', '#c1c1c1');
        $('.wenwen_text span').css('color', '#fff');
        $('.saying').show();
    }

    function _touch_end(event) {
        event.preventDefault();
        $('.wenwen_text').css('background', '#fff');
        $('.wenwen_text .circle-button').css('color', '#666');
        $('.saying').hide();
        var str = '<div class="question">';
        str += '<div class="heard_img right"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png"></div>';
        str += '<div class="question_text clear"><p>Send Soud</p><i></i>';
        str += '</div></div>';
        $('.speak_box').append(str);
        for_bottom();
        setTimeout(function () {
            var ans = '<div class="answer"><div class="heard_img left"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png"></div>';
            ans += '<div class="answer_text"><p>I dont know</p><i></i>';
            ans += '</div></div>';
            $('.speak_box').append(ans);
            for_bottom();
        }, 1000);
    }

    wen.addEventListener("touchstart", _touch_start, false);
    wen.addEventListener("touchend", _touch_end, false);

    function for_bottom() {
        var speak_height = $('.speak_box').height();
        $('.speak_box,.speak_window').animate({scrollTop: speak_height}, 100);
    }

    function autoWidth() {
        $('.question_text').css('max-width', $('.question').width() - 60);
    }
    autoWidth();
</script>
</body>
