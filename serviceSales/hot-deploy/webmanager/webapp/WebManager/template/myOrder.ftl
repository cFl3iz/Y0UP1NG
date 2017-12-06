<style>
    .bac{
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
        text-shadow: 0 1px 1px rgba(0,0,0,.3);
        -webkit-border-radius: .5em;
        -moz-border-radius: .5em;
        border-radius: .5em;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
        -moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
        box-shadow: 0 1px 2px rgba(0,0,0,.2);
        float: right;
    }
</style>
<#if orderList?has_content>
<script>
    function goMyOrderDetail(orderId,tarjeta,orderStatusCode){

        if(orderStatusCode === "0"){
          var flag =  confirm("卖家还没发货,是否立即推送提醒?");
           if(flag){
               alert("已为您提醒卖家发货!");
           }
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

                <a href="javascript:goMyOrderDetail('${list.orderId}','${tarjeta}','${orderStatusCode}');" class="list-item">
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
