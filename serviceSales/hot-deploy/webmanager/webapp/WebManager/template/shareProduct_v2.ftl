<#include "component://webmanager/webapp/WebManager/template/accountCenter.ftl" />
<style>
    .picture_text{
        color:#000000;
    }
</style>
<script>


     function buyProduct(){
         var confirmMessage = $("#confirmMessage").val();
         var a=confirm(confirmMessage);
         if(a == true)
         {
             var tarjeta = $("#tarjeta").val();
             var payToParty = ${(resourceDetail.payToPartyId)!};
             var productId = ${(resourceDetail.productId)!};
             var prodCatalogId = ${(resourceDetail.prodCatalogId)!};
             var productStoreId = ${(resourceDetail.productStoreId)!};
             var url = "placeResourceOrder";

             var param = {
                 payToPartyId:payToParty,
                 productId:productId,
                 prodCatalogId:prodCatalogId,
                 productStoreId:productStoreId,
                 tarjeta:tarjeta
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
                         location.href = "viewWebOrderDetail?orderId="+orderId;
                     }
                     if(data.code === "500"){
                        alert("ERROR:500");
                     }

                 },
                 error: function (data) {
                     alert("ERROR :" + data.status);
                 }
             });

         }
         else
         {
             return false;
         }
     }
</script>
<#if resourceDetail?has_content>

<input type="hidden" id="confirmMessage" value="${uiLabel.confirmMessage}"/>
<input type="hidden" id="confirmMarkMessage" value="${uiLabel.confirmMarkMessage}"/>
<input type="hidden" id="confirmMarkProductMessage" value="${uiLabel.confirmMarkProductMessage}"/>
<input type="hidden" id="productId" value="${(resourceDetail.productId)!}"/>
<input type="hidden" id="prodCatalogId" value="${(resourceDetail.prodCatalogId)!}"/>
<input type="hidden" id="productStoreId" value="${(resourceDetail.productStoreId)!}"/>
<input id="fromurl" type="hidden" value="${fromurl}"/>
<input id="mark" type="hidden" value="${(mark)!}"/>

<div id="content">
    <div class="scroller">
        <div id="p-summary" class="page">
            <div class="container">
                <section id="s-showcase">
                    <div class="scroller">
                        <div class="itbox main"> <a class="item"> <img src="
${(resourceDetail.detailImageUrl?default('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/banner3.png'))!}" alt="" /> </a>
                        </div>
                    </div>
                </section>
                <section id="s-from" class="mui-flex"> </section>
                <section id="s-title">
                    <div class="main">
                        <h1>${(resourceDetail.productName?default('ProductName'))!}</h1>
                    </div>
                    <div class="sub">
                        <div class="slogon"></div>
                    </div>
                </section>
                <section id="s-price"> <span class="mui-price big"> <i class="mui-price-rmb">&yen;</i> <span class="mui-price-integer">${(resourceDetail.price?default('0'))!}</span> <span class="mui-price-decimal"></span> </span> </section>
                <section id="s-adds"> </section>
                <section id="s-hkDeliver"></section>
                <section class="itemExtraInfo"> </section>
                <section id="s-advantage" data-spm=""> </section>
                <div id="s-action-container">
                    <section id="s-action"> </section>
                </div>
                <section id="s-rate" data-spm=""> </section>

                <ul class="table-view" style="margin-top: 10px;">
                    <li class="table-view-cell media"> <a class="">

                        <img class="media-object pull-left" style="width: 15%;height:15%;" src="${(resourceDetail.headPortrait?default('http://placehold.it/42x42'))!}" />

                        <div class="media-body"> ${(resourceDetail.firstName?default('StoreName'))!}
                            <p>${(resourceDetail.partyNote?default('Description'))!}</p>
                        </div>
                    </a> </li>
                </ul>
                <ul class="table-view" style="margin-top:10px;">
                    <li class="table-view-cell">
                        <a  style="font-size: 18px;font-style:bold;color:#666;">
        <#assign listSize = resourceDetail.partyBuyOrder?size/>
                        <span class="badge">${(listSize?default(0))!} ${uiLabel.Person}</span> ${uiLabel.FriendSay}
                        </a>
                    </li>
                </ul>
                <div class="ipo">
                    <ul class="table-view">
                        <li class="table-view-cell media"> <a class="">
                            <div class="media-body"> ${uiLabel.ProductSpecDetail} </div>
                        </a> </li>
                    </ul>
                    <p class="picture_text">It Very Good !
                    </p>
                    <div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/paocai.png" alt="" /> </a>
                    </div>
                    <p class="picture_text">My Motehr Make !</p>
                    <div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/paocai.png" alt="" /> </a>
                    </div>
                    <p class="picture_text">Very Good</p>
                    <div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/paocai.png" alt="" /> </a>
                    </div>

                </div>

                <div class="flick-menu-mask" style=""></div>
                <div class="spec-menu">
                    <div class="spec-menu-content spec-menu-show" style="display: block;">
                        <div class="spec-menu-top bdr-b">
                            <p style="font-size:19px;color: #83CCFC;">${uiLabel.YouHaoTiShi}</p>
                            <a class="rt-close-btn-wrap spec-menu-close">
                                <p class="flick-menu-close tclck"> <img src="
http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/close.png" width="24" height="24" /> </p>
                            </a>
                             </div>

                        <div class="spec-menu-middle">
                            <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg" style="background-repeat:no-repeat; width:95%;height:100%;" />
                        </div>
                       </div>
                </div>
                <section id="s-actionBar-container">
                    <div id="s-actionbar" class="action-bar mui-flex align-center">
                        <div class="web" onclick="doMiniChat();"> <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/atm.png" width="20" height="20" />
                            <p>${uiLabel.Talk}</p>
                        </div>
                        <div class="web" id="moreAction"> <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/trade-assurance.png" width="20" height="20" />
                            <p>${uiLabel.More}</p>
                        </div>
                        <div class="web" id="markProduct"> <img id="markProductImg" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/favorite.png" width="20" height="20" />
                            <p>${uiLabel.Mark}</p>
                        </div>
                        <button class="cart cell" onclick="javascript:alert('${uiLabel.DefaultAction}');">${uiLabel.MyOrder}</button>
                        <button class="buy cell" onclick="buyProduct();">${uiLabel.Buy}</button>
                        <div class="activity-box cell"></div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    //MarkProduct
    function markOrOutMark(markBoolean){

        var tarjeta = $("#tarjeta").val();

        var productId = $("#productId").val();



//        alert(markBoolean);

        var url = "markOrOutMarkProduct";

        var param = {
            productId:productId,
            tarjeta:tarjeta,
            markIt:markBoolean
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            async:false,
            success: function (data) {
                if(data.code === "200"){

                }
                if(data.code === "500"){
                    alert("ERROR:500");
                }

            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });


    }

    function checkSubscribe(){



        var subscribe = getCookie("subscribe");
        alert("IN COOKIE subscribe = " + subscribe);
        $("#subscribe").val(subscribe);

        if(null != subscribe && subscribe === "1" ){

        }

    }


    function doMiniChat(){

        //验证是否订阅
        checkSubscribe();

        $("#miniChatForm").submit();
    }

    //Init
    function initShareProductPage(){




//        alert("subscribe="+subscribe);



        var tarjeta = $("#tarjeta").val();
        var productId = $("#productId").val();
        if(null != tarjeta && tarjeta.trim() != "" && tarjeta != "undefined"){


        var url = "queryProductRole";

        var param = {

            productId:productId,
            tarjeta:tarjeta
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            async:false,
            success: function (data) {
                //   alert("code="+data.code);
                if(data.code === "200"){
                    var mark = data.mark;

                    $("#mark").val(mark);
                }
                if(data.code === "500"){
                    alert("ERROR:500");
                }

            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });
        }

    }

    $(function () {

        initShareProductPage();

        var mark = $("#mark").val();

        //已经收藏了
        if(mark != null && mark.trim() !="" && mark==="true"){
            $("#markProductImg").attr("src","http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/favorite_make.png");
        }


        $("#markProduct").click(
                function(){

                    var src = $("#markProductImg").attr("src")+"";


                    if(src.indexOf("make") > 0){
                        var confirmMarkMessage = $("#confirmMarkMessage").val();
                        var a=confirm(confirmMarkMessage);
                        if(a == true) {
                            markOrOutMark(false);
                            $("#markProductImg").attr("src", "http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/favorite.png");
                        }
                     }else{
                        var confirmMarkProductMessage = $("#confirmMarkProductMessage").val();
                        markOrOutMark(true);
                        $("#markProductImg").attr("src","http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/favorite_make.png");
                        alert(confirmMarkProductMessage);
                    }
                }
        );

        //弹出层在这
        $("#moreAction").click(function () {
            $(".flick-menu-mask").show();
            $(".spec-menu").show();
        })

        $(".tclck").click(function () {
            $(".flick-menu-mask").hide();
            $(".spec-menu").hide();
        })


        $('#cool').bind('input propertychange', function () {
            /* alert(this.value);*/
            $('.amount').html(this.value)

        }).bind('input input', function () {

        });


        $('#color a').click(function () {
            var cook = $(this).index();
            $('#color a').eq(cook).addClass('selected').siblings().removeClass('selected');
        })


        //加减面板
        $(function(){
            //加号
            $(".jia").click(function(){

                var $parent=$(this).parent(".num");
                var $num=window.Number($(".inputBorder",$parent).val());
                $(".inputBorder",$parent).val($num+1);

                $('.amount').html($num+1)

            });

            //减号
            $(".jian").click(function(){
                var $parent=$(this).parent(".num");
                var $num=window.Number($(".inputBorder",$parent).val());
                if($num>2){
                    $(".inputBorder",$parent).val($num-1);
                    $('.amount').html($num-1)

                }else{
                    $(".inputBorder",$parent).val(1);
                    $('.amount').html($num)


                }
            });

        })


    })
</script>


<input id="subscribe" name="subscribe" value="${(subscribe)!}" type="hidden"/>
<form id="miniChatForm" action="miniChat" method="get">
    <input id="tarjeta"  name="tarjeta"  type="hidden" value="${(tarjeta)!}"/>
    <input id="payToPartyId"  name="payToPartyId"  type="hidden" value="${resourceDetail.payToPartyId}"/>
    <input id="payToPartyHead" name="payToPartyHead"  type="hidden" value="${resourceDetail.headPortrait?default('http://placehold.it/42x42')}"/>
    <input id="payToPartyFirstName" name="payToPartyFirstName" type="hidden" value="${resourceDetail.firstName}"/>
    <input id="productId" name="productId" type="hidden" value="${(productId)!}"/>
</form>

</#if>
<#if !resourceDetail?has_content>
NOT FOUND:550
</#if>