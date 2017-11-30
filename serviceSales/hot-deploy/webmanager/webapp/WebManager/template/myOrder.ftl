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
                                <#if list.orderPayStatus == "已付款">
                                    <span class="list"><span style="color:#008000;font-size:19px;">
                                        1${list.orderPayStatus}</span>
                                    </span>
                                </#if>
                                <#if list.orderPayStatus ! == "已付款">
                                    <span class="list"><span style="color:indianred;font-size:19px;">
                                        2${list.orderPayStatus}</span>
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
