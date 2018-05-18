<#include "component://webmanager/webapp/WebManager/template/accountCenter.ftl" />
<style>
    .bac {
        display: inline-block;
        zoom: 1; /* zoom and *display = ie7 hack for display:inline-block */
        *display: inline;
        vertical-align: baseline;
        margin: 0 2px;
        outline: none;
        cursor: pointer;
        text-align: center;
        text-decoration: none;
        font: 14px/100% Arial, Helvetica, sans-serif;
        padding: .5em 2em .55em;
        text-shadow: 0 1px 1px rgba(0, 0, 0, .3);
        -webkit-border-radius: .5em;
        -moz-border-radius: .5em;
        border-radius: .5em;
        -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
        -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
        box-shadow: 0 1px 2px rgba(0, 0, 0, .2);
        float: right;
    }
</style>
<input id="subscribe" name="subscribe" value="${(subscribe)!}" type="hidden"/>
<input id="fromurl" type="hidden" value="${fromurl}"/>
<#if orderList?has_content>
<script>

    function viewQrCode(path) {
        var   jumpurl = "viewContactQrCode?qrCodePath="+path+"&title=卖家的收款码";
        location.href = jumpurl;
    }


    function goMyOrderDetail(orderId, tarjeta, orderStatusCode) {

        if (orderStatusCode === "0") {
            var flag = confirm("是否立即推送提醒?");
            if (flag) {
                alert("已为您提醒卖家!");
            }
        }
        if (orderStatusCode === "1") {
            location.href = 'myOrderDetail?orderId=' + orderId + '&tarjeta=' + tarjeta;
        }
    }
</script>
<section class="g-flexview">



    <section class="g-scrollview">

        <article class="m-list list-theme4">

            <#list orderList as list>

                <#assign orderStatusCode = "${list.orderStatusCode}" />

                <div class="list-item">
                    <div class="list-img">
                        <img src="${list.detailImageUrl}" data-url="${list.detailImageUrl}">
                    </div>
                    <div class="list-mes">

                        <h1 class="list-title">${list.productName}</h1>
                        <#assign payStat = "${list.payStatusCode}" />
                        <#if payStat == '0'>
                            <a href="javascript:viewQrCode('${(list.weChatPayQrCode)!}');">
                                <div class="list-mes-item">
                                    <div>
                                <span class="list" style="font-size:17px;">${uiLabel.orderTime}<span
                                        style="font-size:19px;">&nbsp;${list.orderDate?string("yyyy-MM-dd")}</span></span><br/>
                                <span class="list" style="font-size:17px;">${uiLabel.orderPrice}&nbsp;¥<span
                                        style="font-size:19px;">${list.grandTotal}</span></span>

                                        <br/>
                                <span class="list" style="font-size:17px;">${uiLabel.orderStatus}<span
                                        style="font-size:19px;">${list.statusId}</span></span>
                                    </div>
                                    <div>

                                    <#--${payStat}-->
                                        <#if payStat == '1'>
                                            <span class="list"><span style="color:#008000;font-size:19px;">
                                            ${list.orderPayStatus}</span>
                                    </span>
                                        </#if>
                                        <#if payStat == '0'>
                                            <span class="list">
                                        <button type="button" value="${(list.weChatPayQrCode)!}"
                                                class="btn-block btn-primary" style="z-index: 999999;">去付款
                                        </button>
                                    </span>
                                        </#if>
                                    </div>
                                </div>
                            </a>
                        </#if>

                        <#if payStat != '0'>
                            <div class="list-mes-item">
                                <div>
                                <span class="list" style="font-size:17px;">${uiLabel.orderTime}<span
                                        style="font-size:19px;">&nbsp;${list.orderDate?string("yyyy-MM-dd")}</span></span><br/>
                                <span class="list" style="font-size:17px;">${uiLabel.orderPrice}&nbsp;¥<span
                                        style="font-size:19px;">${list.grandTotal}</span></span>

                                    <br/>
                                <span class="list" style="font-size:17px;">${uiLabel.orderStatus}<span
                                        style="font-size:19px;">${list.statusId}</span></span>
                                </div>
                                <div>
                                    <#assign payStat = "${list.payStatusCode}" />
                                <#--${payStat}-->
                                    <#if payStat == '1'>
                                        <span class="list"><span style="color:#008000;font-size:19px;">
                                        ${list.orderPayStatus}</span>
                                    </span>
                                    </#if>
                                    <#if payStat == '0'>
                                        <span class="list">
                                        <button type="button" onclick="viewQrCode(${(list.weChatPayQrCode)!});"
                                                value="${(list.weChatPayQrCode)!}" class="btn-block btn-primary"
                                                style="z-index: 999999;">去付款
                                        </button>
                                    </span>
                                    </#if>
                                </div>
                            </div>
                        </#if>

                    </div>
                </div>
                <hr/>
            </#list>
        </article>

    </section>

</section>


<div id="fade" style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;height: 100%;background-color: black;z-index:1001;-moz-opacity: 0.8;opacity:.80;filter: alpha(opacity=80);">
</div>
<div id="MyDiv"  style="display: none;position:fixed;bottom:0;left: 10%;width: 80%;height: 80%;border: 1px solid lightblue;background-color: white;z-index:1002;overflow: auto;">
    <div style="text-align: right; cursor: default; height:17px;">
        <span style="font-size: 11px;" onclick="CloseDiv('MyDiv','fade')">${(uiLabel.CLOSE)!}</span>
    </div>
    <span style="font-size:12px;font-family:PingFang;">${(uiLabel.YouHaoTiShi)!}</span>
    <img id="showboxmenu1" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg" style="background-repeat:no-repeat; width:100%;height:100%;" />
</div>


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
    //清除cookie
    function clearCookie(name) {
        setCookie(name, "", -1);
    }
    function setCookie(name, value) {
//        alert("IN SET COOKIE NAME = " + name +"|value="+value);
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);

        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }
    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }

    !function () {
        var flag = checkSubscribe();
        if (flag == true) {

        }else{
            alert("关注公众号可以获得订单推送。");
            ShowDiv('MyDiv','fade');
        }
    }();

</script>
</#if>
<#if !orderList?has_content>
<section class="g-flexview">
    <section class="g-scrollview">
        <h1>列表是空的!您还没有任何订单</h1>
    </section>
</section>
</#if>
</body>
