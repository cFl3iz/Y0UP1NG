<#include "component://webmanager/webapp/WebManager/template/accountCenter.ftl" />
<style>
    hr {
        display: block;
        -webkit-margin-before: 0.5em;
        -webkit-margin-after: 0.5em;
        -webkit-margin-start: auto;
        -webkit-margin-end: auto;
        border-style: inset;
        border-width: 1px;
    }
</style>
<section class="g-flexview" style="margin-left: 15px;margin-right: 15px;margin-top: 15px;margin-bottom: 15px;">
    <section class="g-scrollview">
        <div class="m-slider">
            <div class="slider-wrapper">
                <div class="slider-item">
                    <img style="height:425px;"
                         src="${(resourceDetail.detailImageUrl?default('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/banner3.png'))!}?x-oss-process=image/resize,w_500,h_500/quality,q_95">
                </div>
            </div>
            <div class="slider-pagination"></div>
        </div>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: " Helvetica Neue",
        Helvetica, "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif; font-size: medium; white-space:
        normal;"><br></p>
        <strong><span style="font-size: 24px; font-family: PingFangSC-Light, sans-serif;">
        ${(resourceDetail.productName?default('ProductName'))!}
        </span></strong>

        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: " Helvetica Neue",
        Helvetica, "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif; font-size: medium; white-space:
        normal;"><br></p>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal; line-height: 1.75em;">
            <span style="color: rgb(127, 127, 127); font-family: PingFangSC-Light, sans-serif;">作者:${(resourceDetail.firstName?default('StoreName'))!}</span>
        </p>
        <hr/>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal;">
            <br></p>
        <!-- Some Font -->
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal; line-height: 1.75em;">
            <span style="font-family: PingFangSC-Light, sans-serif;">
            ${(resourceDetail.description)!}
            </span>
        </p>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal;">
            <br></p>
<#list morePicture as list>
        <div class="m-slider">
            <div class="slider-wrapper">
                <div class="slider-item">
                    <a href="javascript:alert('儿子不卖')">
                        <img src="${(list.drObjectInfo)!}?x-oss-process=image/resize,w_500,h_500/quality,q_95">
                    </a>
                </div>

            </div>
            <div class="slider-pagination"></div>
        </div>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal;">
        <br></p>
</#list>
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal;">
            <br></p>
        <div style="border:1px solid #8C8C8C;padding:10px;">
        <p style="margin-top: 0px; margin-bottom: 0px; padding: 0px; clear: both; font-family: &quot;Helvetica Neue&quot;, Helvetica, &quot;Hiragino Sans GB&quot;, &quot;Microsoft YaHei&quot;, Arial, sans-serif; font-size: medium; white-space: normal; max-width: 100%; min-height: 1em; text-align: justify; line-height: 1.75em; box-sizing: border-box !important; word-wrap: break-word !important;">
            <span style="margin: 0px; padding: 0px; font-size: 16px; font-family: PingFangSC-Light, sans-serif;"><em
                    style="margin: 0px; padding: 0px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"><strong
                    style="margin: 0px; padding: 0px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"><span
                    style="font-size: 16px; font-family: PingFangSC-Light, sans-serif; margin: 0px; padding: 0px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"> ${uiLabel.Msg}</span></strong></em>
                <strong  style="margin: 0px; padding: 0px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;">
            </span>
        </p>
        <button type="button" onclick="contactMe();" class="btn-block btn-warning">
            ${uiLabel.ContactMe}
        </button>
        </div>
    </section>
</section>

<script>
    function contactMe (){
        var subscribe = $("#subscribe").val();
    }
</script>

<input id="subscribe" name="subscribe" value="${(subscribe)!}" type="hidden"/>

<form id="miniChatForm" action="miniChat" method="get">
    <input id="tarjeta"  name="tarjeta"  type="hidden" value="${(tarjeta)!}"/>
    <input id="payToPartyId"  name="payToPartyId"  type="hidden" value="${resourceDetail.payToPartyId}"/>
    <input id="payToPartyHead" name="payToPartyHead"  type="hidden" value="${resourceDetail.headPortrait?default('http://placehold.it/42x42')}"/>
    <input id="payToPartyFirstName" name="payToPartyFirstName" type="hidden" value="${resourceDetail.firstName}"/>
    <input id="productId" name="productId" type="hidden" value="${productId}"/>
</form>