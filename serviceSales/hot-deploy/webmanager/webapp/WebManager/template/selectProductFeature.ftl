<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>CustRequest</title>
    <meta name="format-detection" content="telephone=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="viewport" content="width=device-width,initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no" />
    <meta name="description" content="">
    <meta name="keywords" content="">
    <style>
        body{line-height:1;color:#333;width:100%;margin:0 auto;-webkit-text-size-adjust:none;overflow-x:hidden}p,ol,ul,li{list-style:none}a,span{line-height:1;color:#333;text-decoration:none}button{outline:0}label{font-weight:normal}img{width:100%;display:block}a img{word-break:break-all;word-wrap:break-word}a:hover,a:focus{text-decoration:none}:focus{outline:0}*{-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;-webkit-text-size-adjust:none}
        html,body{padding:0; margin:0 auto; height:100%; font-size:15px; max-width:400px}
        *{-webkit-appearance:none; margin:0; padding:0}
        a, span{color:inherit ; line-height:inherit; text-decoration:none}
        img{width:auto }
        input{outline:none;}
        .wrapper{width:100%; margin:0 auto;}
        .card_wrap{width:400px; height:584px; position:relative; overflow:hidden; display:none}
        .card_cont{width:100%; height:530px; box-sizing:border-box; margin:0 auto; position:absolute;  background:url('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/test_card_bg.png') no-repeat center top; background-size:100% auto; padding:8px 20px 18px; transition:all ease .5s; -webkit-transition:all ease .5s; -moz-transition:all ease .5s; -ms-transition:all ease .5s; transform:scale(0,0); -moz-transform:scale(0,0);-ms-transform:scale(0,0); -o-transform:scale(0,0); -webkit-transform:scale(0,0); bottom:0; display:none;}
        .card{width:320px; height:100%; position:relative; margin:0 auto; padding:0 18px; }
        .card .card_bottom{width:100%; position:absolute; bottom:28px; left:0; box-sizing:border-box; padding:0 28px}
        .card .card_bottom a{color:#c733c5; cursor:pointer }
        .card .card_bottom span{float:right; color:#909090}.card .card_bottom span b{color:#666; font-weight:inherit}

        .card_cont.card1{ display:block;transform:scale(1,1) translate(0,0) !important ;
            -ms-transform:scale(1,1) translate(0,0) !important ;
            -moz-transform:scale(1,1) translate(0,0) !important ;
            -webkit-transform:scale(1,1) translate(0,0) !important ;
        }
        .card_cont.card2{ display:block;
            transform:scale(.85,.85) translate(0,-62px) !important ;
            -ms-transform:scale(.85,.85) translate(0,-62px) !important ;
            -moz-transform:scale(.85,.85) translate(0,-62px) !important ;
            -webkit-transform:scale(.85,.85) translate(0,-62px) !important;
        }
        .card_cont.card3{ display:block;
            transform:scale(.72,.72) translate(0,-135px) !important ;
            -ms-transform:scale(.72,.72) translate(0,-135px) !important ;
            -moz-transform:scale(.72,.72) translate(0,-135px) !important ;
            -webkit-transform:scale(.72,.72) translate(0,-135px) !important ;
        }
        .card_cont.cardn{display:block;
            transform:translate(0,-1000px) !important;
            -moz-transform:translate(0,-1000px) !important;
            -ms-transform:translate(0,-1000px) !important;
            -webkit-transform:translate(0,-1000px) !important;}

        .question{display:table-cell; height:80px; font-size:16px; font-weight:100; color:#fff; line-height:1.4; vertical-align:middle; padding-left:1em}
        .question span{margin-left:-1em}

        /*Radio Specific styles*/
        input[type='radio']{ display: none;cursor: pointer;}
        input[type='radio']:focus, input[type='radio']:active{outline: none;}
        input[type='radio'] + label {
            cursor: pointer;
            display: inline-block;
            position: relative;
            padding-left: 28px;
            color: #666;

        }
        input[type='radio']:checked + label {color: #c733c5 !important;}
        input[type='radio'] + label:before, input[type='radio'] + label:after{
            content: '';
            font-family: helvetica;
            display: inline-block;
            width: 20px;
            height:20px;
            left: 0;
            top: 0;
            text-align: center;
            position: absolute;
        }
        input[type='radio'] + label:before {background-color:transparent;}
        input[type='radio'] + label:after {color: #c733c5;}
        input[type='radio']:checked + label:before {
            -moz-box-shadow: inset 0 0 0 5px #fff;
            -webkit-box-shadow: inset 0 0 5px #fff;
            box-shadow: inset 0 0 0 5px #fff;
            border:1px solid #c733c5;
            background-color:#c733c5;
        }

        input[type='radio'] + label:before {
            -moz-border-radius: 50%;
            -webkit-border-radius: 50%;
            border-radius: 50%;
            border:1px solid #c733c5;
        }

        input[type='radio'] + label:hover:after {color: #c733c5;}
        input[type='radio']:checked + label:after, input[type='radio']:checked + label:hover:after {color: #c733c5;}
        ul.select{margin-top:30px}
        ul.select li{height:46px; line-height:1.5; margin:0 0 20px 0}
    </style>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script>
        $.fn.answerSheet = function(options) {var defaults={mold:'card',};var opts = $.extend({},defaults,options);return $(this).each(function(){var obj = $(this).find('.card_cont');var _length = obj.length,_b = _length -1,_len = _length - 1, _cont = '.card_cont';for(var a = 1; a <= _length; a++){obj.eq(_b).css({'z-index':a});_b-=1;}$(this).show();if(opts.mold == 'card'){obj.find('ul li label').click(function(){var _idx =  $(this).parents(_cont).index(),_cards =  obj,_cardcont = $(this).parents(_cont);if(_idx == _len){return;}else{setTimeout(function(){_cardcont.addClass('cardn');setTimeout(function(){_cards.eq(_idx + 3).addClass('card3');_cards.eq(_idx + 2).removeClass('card3').addClass('card2');_cards.eq(_idx + 1).removeClass('card2').addClass('card1');_cardcont.removeClass('card1');},200);},100);}});$('.card_bottom').find('.prev').click(function(){var _idx =  $(this).parents(_cont).index(), _cardcont = $(this).parents(_cont);obj.eq(_idx + 2).removeClass('card3').removeClass('cardn');obj.eq(_idx + 1).removeClass('card2').removeClass('cardn').addClass('card3');obj.eq(_idx).removeClass('card1').removeClass('cardn').addClass('card2');setTimeout(function(){obj.eq(_idx - 1).addClass('card1').removeClass('cardn');},200);})}});};
    </script>
    <script>
        function commitCustRequest(){
            var rowTypeCount = $("#rowTypeCount").val();
            var redios = $(":radio:checked");
            var partyId = $("#partyId").val();
            var payToPartyId = $("#payToPartyId").val();
            var markText = $("#markText").val();
            var productId = $("#productId").val();

            var redioLenght = redios.length;
//            alert(redioLenght + "|" + rowTypeCount + " is Right = " + (redioLenght==rowTypeCount));
            if(!(redioLenght==rowTypeCount)){
                alert("当前页面请至少选择一项!");
                return false;
            }
            var selectFeatures = "";
            for(var i = 0 ; i < redios.length;i++){
                selectFeatures = selectFeatures + $(redios[i]).val() + ",";
            }

            var url = "createCustRequest";

            var param = {
                partyId:partyId,
                payToPartyId:payToPartyId,
                productId:productId,
                selectFeatures:selectFeatures,
                markText:markText
            };
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                async:false,
                success: function (data) {
                    if(data.code === "200"){
                       alert("您的询价啊请求已提交成功!请关闭当前页面回家等通知...");
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
        $(function(){
            $("#answer").answerSheet({});

        })

    </script>
</head>

<body style="background-color:#1fc587">

<input id="payToPartyId" name="payToPartyId" value="${(payToPartyId)!}" type="text"/>
<input id="rowTypeCount" name="rowTypeCount" value="${(resultMap.rowTypeCount)!}" type="text"/>
<input id="partyId" name="partyId" value="${(partyId)!}" type="text"/>
<input id="productId" name="productId" value="${(productId)!}" type="text"/>
<#assign strHtml = resultMap.htmlBuilder>
<#assign beforeKey = "">
<#assign count = 1>
<#--${listKeys}-->



<div class="wrapper">
    <div id="answer" class="card_wrap">
        ${resultMap}
        <!--Q1-->
        <#--<div class="card_cont card1">-->
            <#--<div class="card">-->
                <#--<p class="question"><span>Q1</span>color?</p>-->
                <#--<ul class="select">-->
                    <#--<li>-->
                        <#--<input id="q1_1" type="radio" name="r-group-1" >-->
                        <#--<label for="q1_1">red</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q1_2" type="radio" name="r-group-1">-->
                        <#--<label for="q1_2">black</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q1_3" type="radio" name="r-group-1">-->
                        <#--<label for="q1_3">white</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q1_4" type="radio" name="r-group-1">-->
                        <#--<label for="q1_4">green</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q1_5" type="radio" name="r-group-1">-->
                        <#--<label for="q1_5">stat</label>-->
                    <#--</li>-->
                <#--</ul>-->
                <#--<div class="card_bottom"><span><b>1</b>/2</span></div>-->
            <#--</div>-->
        <#--</div>-->
        <#--<!--Q2&ndash;&gt;-->
        <#--<div class="card_cont card2" >-->
            <#--<div class="card">-->
                <#--<p class="question"><span>Q2</span>SIZE</p>-->
                <#--<ul class="select">-->
                    <#--<li>-->
                        <#--<input id="q2_1" type="radio" name="r-group-2" >-->
                        <#--<label for="q2_1">25</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q2_2" type="radio" name="r-group-2">-->
                        <#--<label for="q2_2">36</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q2_3" type="radio" name="r-group-2">-->
                        <#--<label for="q2_3">37</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q2_4" type="radio" name="r-group-2">-->
                        <#--<label for="q2_4">42</label>-->
                    <#--</li>-->
                    <#--<li>-->
                        <#--<input id="q2_5" type="radio" name="r-group-2">-->
                        <#--<label for="q2_5">47</label>-->
                    <#--</li>-->
                <#--</ul>-->
                <#--<div class="card_bottom"><a class="prev">BACK</a><span><b>2</b>/2</span></div>-->
            <#--</div>-->
        <#--</div>-->

    </div><!--/card_wrap-->

</div>


</body>