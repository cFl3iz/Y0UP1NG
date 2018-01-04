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

    .overhide {
        overflow: hidden;
    }


    .black_overlay{

    }
    .white_content {

    }
    .white_content_small {
        display: none;
        position: absolute;
        top: 20%;

        left: 30%;
        width: 40%;
        height: 50%;
        border: 16px solid lightblue;
        background-color: white;
        z-index:1002;
        overflow: auto;
    }
</style>

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
</script>


<section class="g-flexview" style="margin-left: 15px;margin-right: 15px;margin-top: 15px;margin-bottom: 15px;">
    shares:${(spm)!}
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
                    <a href="javascript:alert('为您服务')">
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
                <strong style="margin: 0px; padding: 0px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;">
            </span>
            </p>
            <button type="button" onclick="contactMe();" class="btn-block btn-warning">
            ${uiLabel.ContactMe}
            </button>
        </div>
    </section>
</section>


<script>

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


    function contactMe() {
        var flag = checkSubscribe();
//        alert("flag=" + flag);
//        alert("是否订阅==" + flag);
        if (flag == true) {
//            $("#miniChatForm").submit();
            var payToPartyId = $("#payToPartyId").val();
            var partyId      = $("#partyId").val();
            var productId      = $("#productId").val();
            var spm            = $("#spm").val();

            if(payToPartyId == null || payToPartyId ===""){

                location.reload();
                contactMe();
            }
            var jumpurl = "https://www.yo-pe.com/pejump/"+partyId+"/"+partyId+"111"+"/"+payToPartyId+"/"+productId;
//            alert("jump productId="+jumpurl);
//            alert("jump productId="+productId);
//            return false;

            var mark = $("#mark").val();
            if(mark != null && mark.trim() !="" && mark==="true"){

            }else{
                markOrOutMark(true);
            }

            location.href = jumpurl;

        } else {
            ShowDiv('MyDiv','fade');
        }

    }
</script>

<input id="spm" type="hidden" value="${(spm)!}" />

<input id="mark" type="hidden" value="${(mark)!}"/>
<input id="subscribe" name="subscribe" value="${(subscribe)!}" type="hidden"/>
<input id="fromurl" type="hidden" value="${fromurl}"/>
<input id="payToPartyId" name="payToPartyId" value="${(payToPartyId)!}" type="hidden"/>
<input id="partyId" name="partyId" value="${(partyId)!}" type="hidden"/>
<form id="miniChatForm" action="miniChat" method="get">
    <input id="tarjeta" name="tarjeta" type="hidden" value="${(tarjeta)!}"/>
    <input id="payToPartyId" name="payToPartyId" type="hidden" value="${resourceDetail.payToPartyId}"/>
    <input id="payToPartyHead" name="payToPartyHead" type="hidden"
           value="${resourceDetail.headPortrait?default('http://placehold.it/42x42')}"/>
    <input id="payToPartyFirstName" name="payToPartyFirstName" type="hidden" value="${(resourceDetail.firstName)!}"/>
    <input id="productId" name="productId" type="hidden" value="${productId}"/>
</form>


<!-- 弹出层时背景层 DIV -->
<div id="fade" style="display: none;position: absolute;top: 0%;left: 0%;width: 100%;height: 100%;background-color: black;z-index:1001;-moz-opacity: 0.8;opacity:.80;filter: alpha(opacity=80);">
</div>
<div id="MyDiv"  style="display: none;position:fixed;bottom:0;left: 10%;width: 80%;height: 80%;border: 1px solid lightblue;background-color: white;z-index:1002;overflow: auto;">
    <div style="text-align: right; cursor: default; height:17px;">
        <span style="font-size: 11px;" onclick="CloseDiv('MyDiv','fade')">${(uiLabel.CLOSE)!}</span>
    </div>
    <span style="font-size:12px;font-family:PingFang;">${(uiLabel.YouHaoTiShi)!}</span>
    <img id="showboxmenu1" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg" style="background-repeat:no-repeat; width:100%;height:100%;" />
</div>


