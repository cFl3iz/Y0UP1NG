<#include "component://webmanager/webapp/WebManager/template/accountCenter.ftl" />
<style>
    .picture_text{
        color:#000000;
    }
</style>
<script>


     function buyProduct(){

         $("#showboxmenu2").show();
         $("#showboxmenuspec2").show();
         return false;

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
${(resourceDetail.detailImageUrl?default('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/banner3.png'))!}?x-oss-process=image/resize,w_500,h_500/quality,q_80" alt="" /> </a>
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

                        <img class="media-object pull-left" style="width: 15%;height:15%;" src="${(resourceDetail.headPortrait?default('http://placehold.it/42x42'))!}?x-oss-process=image/resize,w_50,h_50/quality,q_80" />

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
                    <#if morePicture?has_content>
                        ${(resourceDetail.description)!}
                        <#list morePicture as list>
                            <p class="picture_text">
                            </p>
                            <div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="${(list.drObjectInfo)!}?x-oss-process=image/resize,w_500,h_500/quality,q_80" alt="" /> </a>
                            </div>
                        </#list>

                    <#--<p class="picture_text">My Motehr Make !</p>-->
                    <#--<div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/paocai.png" alt="" /> </a>-->
                    <#--</div>-->
                    <#--<p class="picture_text">Very Good</p>-->
                    <#--<div class="itbox main"> <a class="item"> <img style="display: block;height: auto;max-width: 100%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/paocai.png" alt="" /> </a>-->
                    <#--</div>-->
                    </#if>
    <#if !morePicture?has_content>
        <p class="picture_text">
            ${uiLabel.NoProductMoreDetail}
        </p>

    </#if>
                </div>
                <!-- 弹出0 -->
                <div class="flick-menu-mask" id="showboxmenu2" style=""></div>
                <div class="spec-menu" id="showboxmenuspec2">
                    <div class="spec-menu-content spec-menu-show" style="display: block;">
                        <div class="spec-menu-top bdr-b">
                            <div class="spec-first-pic"> <img id="spec_image" src="${(resourceDetail.detailImageUrl?default('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/banner3.png'))!}?x-oss-process=image/resize,w_500,h_500/quality,q_80" /> </div>
                            <a class="rt-close-btn-wrap spec-menu-close" id="hidemenu">
                                <p class="flick-menu-close tclck"> <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/close.png" width="24" height="24" /> </p>
                            </a>
                            <div id="specPlus" class="plus-price-wrap spec-plus-price-mtl" style="display: none">
                                <div class="plus-price-row">
                                    <div class="spec-jd-price"> <span class="plus-jd-price-title">京东价：</span> <span class="price-icon grey"></span> <span class="plus-jd-price-text" id="specJdPrice"> 36.80 </span> </div>
                                </div>
                                <div class="plus-price-row">
                                    <div class="spec-plus-price"> <span class="plus-member-price-title">会员价：</span> <span class="price-icon black"></span> <span class="plus-member-price-text" id="specPlusPrice"></span> <img class="plus-pri-icon" src="" /> </div>
                                </div>
                            </div>
                            <div class="spec-price" id="specJdPri" style="display: block"> <span class="yang-pic spec-yang-pic"></span> <span id="spec_price"> 36.80 </span> </div>
                            <div id="specWeightDiv" class="spec-weight"> <span>${uiLabel.Weight}:</span> <span id="spec_weight">3.26kg</span> </div>
                        </div>
                        <div class="spec-menu-middle">
                            <div class="prod-spec" id="prodSpecArea">
                                <!-- 已选 -->
                                <div class="prod-spec" id="prodSpecArea">
                                    <!-- 已选 -->
                                    <div class="spec-desc"> <span class="part-note-msg">已选</span>
                                        <div id="specDetailInfo_spec" class="base-txt"> 款式 默认款 &nbsp;&nbsp; <span class="amount" id="amount">1</span>件 </div>
                                    </div>
                                    <div class="nature-container" id="natureCotainer">
                                        <!--颜色 5.5版本之前的规格属性-->
                                        <div class="pro-color"> <span class="part-note-msg"> 规格 </span>
                                            <p id="color">
                                                <a title="款式 默认款" class="a-item selected J_ping"   report-eventparam="款式 默认款" >款式 默认款</a>
                                                <#--<a title="蓝色 直袖款" class="a-item J_ping"   report-eventparam="蓝色 直袖款" >蓝色 直袖款</a>-->
                                                <#--<a title="黑色 直袖款" class="a-item J_ping"   report-eventparam="黑色 直袖款" >黑色 直袖款</a>-->
                                                <#--<a title="灰色 直袖款" class="a-item J_ping"   report-eventparam="灰色 直袖款" >灰色 直袖款</a>-->
                                                <#--<a title="粉色 直袖款" class="a-item J_ping"   report-eventparam="粉色 直袖款" >粉色 直袖款</a>-->
                                                <#--<a title="紫色 直袖款" class="a-item J_ping"   report-eventparam="紫色 直袖款" >紫色 直袖款</a>-->
                                            </p>
                                        </div>
                                        <!--尺寸-->
                                        <!--容量-->
                                        <!--数量-->
                                    </div>
                                    <!--尺寸-->
                                    <!--容量-->
                                    <!--数量-->
                                    <div id="addCartNum" class="pro-count"> <span class="part-note-msg" style="margin-right: 10px;">${uiLabel.Amount}</span>
                                        <div style="width: 100%;margin-left: 10px;" class="num">
                                            <p class="jian" style="float: left;margin-top: 5px;"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jian.png" width="16" height="16"></p>
                                            <input id="cool" class="inputBorder"  value="1" onKeyUp="this.value=this.value.replace(/[^0-9]/g,'')" onafterpaste="this.value=this.value.replace(/[^0-9]/g,'')" type="text" maxlength="5" placeholder="数量" style="font-size: 12px;width: 60px;height: 20px;float: left;padding: 0 5px;margin-top:3px;margin-left: 5px;" />
                                            <p class="jia" style="float: left;margin-top: 5px;margin-left:4px;"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jia.png" width="16" height="16"></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--延保start-->
                            <div class="warranty-wrap bdr-t" style="display: block;">
                                <div id="spec_yanbaoInfo" class="warranty-title"> 保障服务 </div>
                                <div id="spec_yanbaoItems">卖家承诺是好东西包退货</div>
                            </div>
                            <!--延保end-->
                        </div>
                        <div class="flick-menu-btn spec-menu-btn">
                            <a class="yellow-color add_cart" onclick="javascript:$('#hidemenu').click();" style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">取消</a>
                            <a class="red-color directorder" id="directorder_spec" style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">立即购买</a> <a class="yellow-color J_ping looksimilar looksimilara looksimilar_spec" id="looksimilar_speca" href=" " style="display: none;" report-eventid="MProductdetail_Similar" report-eventparam="W" report-eventlevel="5">查看相似</a>
                            <!--大家电-查看同款-->
                            <a class="yellow-color J_ping looksimilar looksimilarb looksimilar_spec" id="looksimilar_specb" href="" style="display: none;" report-eventid="MProductdetail_SameProduct" report-pageparam="" report-eventlevel="4">查看同款</a> <a class="red-color J_ping arrivalInform" id="arrivalInform_spec" style="display: none;" report-eventid="MProductdetail_ArrivalNotice" report-eventparam="2015246_W" report-eventlevel="5">到货通知</a> <a class="red-color disabled waitingForAppointments" style="display: none;" id="waitingForAppointments_spec">等待预约</a> <a class="red-color makeAppointments" style="display: none;" id="makeAppointments_spec">立即预约</a> <a class="red-color disabled waitingForBuying" style="display: none;" id="waitingForBuying_spec">等待抢购</a> <a class="red-color buyImmediately" style="display: none;" id="buyImmediately_spec">立即抢购</a> <a class="red-color disabled appointmentsEnd" style="display: none;" id="appointmentsEnd_spec">预约结束</a> <a class="red-color" style="display: none;" id="yushou_add_cart_spec">立即预定</a> </div>
                    </div>
                </div>
                <!-- 弹出 0-->






                <!-- 弹出层1 -->
                <div class="flick-menu-mask" id="showboxmenu1" style=""></div>
                <div class="spec-menu" id="showboxmenuspec1">
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
                <!-- 弹出层1 结束-->
                <section id="s-actionBar-container">
                    <div id="s-actionbar" class="action-bar mui-flex align-center">
                        <div class="web" onclick="doMiniChat();"> <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/atm.png" width="20" height="20" />
                            <p>${uiLabel.Talk}</p>
                        </div>
                        <div class="web"  onclick="javascript:alert('${uiLabel.DefaultAction}');"> <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/trade-assurance.png" width="20" height="20" />
                            <p>${uiLabel.More}</p>
                        </div>
                        <div class="web" id="markProduct"> <img id="markProductImg" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/favorite.png" width="20" height="20" />
                            <p>${uiLabel.Mark}</p>
                        </div>
                        <button class="cart cell" onclick="myOrder();">${uiLabel.MyOrder}</button>
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
                    alert("CODE:409 网络延迟,请刷新页面重试!");
                }

            },
            error: function (data) {
                alert("CODE:409 网络延迟,请刷新页面重试!");
            }
        });


    }


    function myOrder(){
        var tarjeta = $("#tarjeta").val();
        location.href = 'myOrder?tarjeta='+tarjeta;
//        if(checkSubscribe()==true){
//            location.href = 'https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI5NDkzMzYyNA==&scene=124#wechat_redirect';
//        }
    }


    function checkSubscribe(){
        var flag = false;
        var subscribe = getCookie("subscribe");
//        alert("IN COOKIE subscribe = " + subscribe);
//        $("#subscribe").val(subscribe);
      //  alert(subscribe);
        if(subscribe === "1" ){
            flag =  true;
        }else{
            //alert("2");
            $("#showboxmenu1").show();
            $("#showboxmenuspec1").show();
            clearCookie("tarjeta");
            flag = false;
        }
        return flag;
    }


    function doMiniChat(){

        //验证是否订阅

        if(checkSubscribe() == true){
            $("#miniChatForm").submit();
        }

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



        $(".tclck").click(function () {
            $("#showboxmenu1").hide();
            $("#showboxmenuspec1").hide();

        })

        function hidemenu(){
            $("#showboxmenu2").hide();
            $("#showboxmenuspec2").hide();
        }
        $("#hidemenu").click(
                function(){
                    hidemenu();
                }
        );


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