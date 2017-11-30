<#if orderList?has_content>
<script>
    function goMyOrderDetail(orderId,tarjeta,orderStatusCode){
        alert(orderId);
        alert(tarjeta);
        alert(orderStatusCode);
        if(orderStatusCode === "0"){
            alert("是否提醒卖家发货?");
        }
        if(orderStatusCode === "1"){
            location.href = 'myOrderDetail?orderId='+orderId+'&tarjeta='+tarjeta;
        }
    }
</script>
<section class="g-flexview">


    <section class="g-scrollview">

        <article class="m-list list-theme4">

            <#list orderList as list>

                <#assign orderStatusCode = "${list.orderStatusCode}" />

                <a href="javascript:goMyOrderDetail('${list.orderId},${tarjeta},${orderStatusCode}');" class="list-item">
                    <div class="list-img">
                        <img src="${list.detailImageUrl}" data-url="${list.detailImageUrl}">
                    </div>
                    <div class="list-mes">

                        <h1 class="list-title">${list.productName}</h1>

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
                                    <span class="list"><span style="color:indianred;font-size:19px;">
                                        ${list.orderPayStatus}</span>
                                    </span>
                                </#if>
                            </div>
                        </div>
                    </div>
                </a>
            </#list>
        </article>

    </section>

</section>

<script>
    !function () {
        $('.m-list').find('img').lazyLoad({binder: '.g-scrollview'});
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
