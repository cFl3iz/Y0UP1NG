<#if orderList?has_content>
<section class="g-flexview">



    <section class="g-scrollview">

        <article class="m-list list-theme4">

           <#list orderList as list>

            <a href="javascript:alert('订单明细不给你看的。');" class="list-item">
                <div class="list-img">
                    <img src="${list.detailImageUrl}" data-url="http://img1.shikee.com/try/2016/06/19/09430120929215230041.jpg_180x180.jpg">
                </div>
                <div class="list-mes">
                    <h3 class="list-title">${list.productName}</h3>
                    <div class="list-mes-item">
                        <div>
                            <#if list.orderPayStatus == '未付款'>
                                <span class="list-price">${list.orderPayStatus}</span>
                            </#if>
                            <#if list.orderPayStatus == '已付款'>
                                <span class="list-price" style="color:#008000;">${list.orderPayStatus}</span>
                            </#if>
                            <span class="list">¥${list.grandTotal}</span>
                            <span class="list">${uiLabel.orderStatus}${list.statusId}</span>
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
