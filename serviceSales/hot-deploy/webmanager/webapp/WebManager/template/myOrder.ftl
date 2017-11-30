<#if orderList?has_content>
<section class="g-flexview">


    <section class="g-scrollview">

        <article class="m-list list-theme4">

            <#list orderList as list>

                <a href="javascript:alert('订单明细不给你看的。');" class="list-item">
                    <div class="list-img">
                        <img src="${list.detailImageUrl}" data-url="${list.detailImageUrl}">
                    </div>
                    <div class="list-mes">
                        <h3 class="list-title">${list.productName}</h3>

                        <div class="list-mes-item">
                            <div>

                                <span class="list" style="font-size:17px;">${uiLabel.orderTime}<span
                                        style="font-size:19px;">${list.orderDate}</span></span><br/>
                                <span class="list" style="font-size:17px;">${uiLabel.orderPrice}¥<span
                                        style="font-size:19px;">${list.grandTotal}</span></span&nbsp;&nbsp;&nbsp;&nbsp;
                                <#if (list.orderPayStatus!"未付款") == "已付款">
                                    <span class="list-price"><span style="color:#008000;">${list.orderPayStatus}</span></span>
                                <#else >
                                    <span class="list-price">${list.orderPayStatus}</span>
                                </#if>
                                <br/>
                                <span class="list" style="font-size:20px;">${uiLabel.orderStatus}${list.statusId}</span>
                            </div>
                            <div>right</div>
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
