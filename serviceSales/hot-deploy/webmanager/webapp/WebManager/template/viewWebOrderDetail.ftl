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


<section class="g-flexview">

<#--<header class="m-navbar">-->
<#--<a href="../index.html" class="navbar-item"><i class="back-ico"></i></a>-->
<#--<div class="navbar-center"><span class="navbar-title">CitySelect</span></div>-->
<#--</header>-->

    <section class="g-scrollview">

        <aside class="demo-tip">
            <p style="font-size:25px;">${uiLabel.CreateOrderSuccess}</p>
        </aside>

    <#if !addressInfo?has_content>
        <div id="addressSettingBox">
            <div class="m-celltitle">${uiLabel.SetAddress}</div>
            <div class="m-cell">
                <div class="cell-item">
                    <div class="cell-left">${uiLabel.AddressScope}</div>
                    <div class="cell-right cell-arrow">
                        <input type="text" class="cell-input" readonly id="J_Address"
                               placeholder="${uiLabel.SelectAddress}">
                    </div>
                </div>
            </div>
            <form id="createPersonPartyPostalAddressForm" action="createPersonPartyPostalAddress">
                <input name="tarjeta" id="tarjeta" value="${(tarjeta)!}" type="hidden"/>
                <input name="address1" id="address1" type="hidden"/>
                <input name="orderId" id="orderId" type="hidden"/>
                <div class="m-celltitle">${uiLabel.AddressDetail}</div>
                <div class="m-cell">
                    <div class="cell-item">
                        <div class="cell-right">
                            <textarea name="address2" id="address2" class="cell-textarea"
                                      placeholder="${uiLabel.AddressDetailDemo}"></textarea>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <!-- 暂时没有偏好设置 -->
    <#--<div class="m-celltitle">${uiLabel.SettingAbout}</div>-->
    <#--<div class="m-cell" id="SetDefaultAddress">-->
    <#--<label class="cell-item">-->
    <#--<span class="cell-left">${uiLabel.SetDefaultAddress}</span>-->
    <#--<label class="cell-right">-->
    <#--<input type="checkbox" class="m-switch" checked/>-->

    <#--</label>-->
    <#--</label>-->
    <#--</div>-->
    <#--<div class="m-cell">-->
    <#--<label class="cell-item">-->
    <#--<span class="cell-left">${uiLabel.NoFuckSetting}</span>-->
    <#--<label class="cell-right">-->
    <#--<input type="checkbox" id="noSetting" class="m-switch"/>-->

    <#--</label>-->
    <#--</label>-->
    <#--</div>-->
    <#--<div class="m-cell"  id="CleanOne">-->
    <#--<label class="cell-item">-->
    <#--<span class="cell-left">${uiLabel.CleanOne}</span>-->
    <#--<label class="cell-right">-->
    <#--<input type="checkbox"  class="m-switch"/>-->
    <#--</label>-->
    <#--</label>-->
    <#--</div>-->
        <div class="m-button">
            <input type="button" class="btn-block btn-primary" id="J_Notify" value="${uiLabel.Setting}"/>
        </div>
    </#if>
    <#if addressInfo?has_content>
        <div class="m-celltitle">${uiLabel.CheckAddress}</div>

        <div class="m-cell">
            <#list addressInfo as list>
                <#if list_index ==0 >
                    <label class="cell-item">
                        <span class="cell-left">${(list.address1)!}${(list.address2)!}</span>
                        <label class="cell-right">
                            <input type="radio" value="${(list.contactMechId)!}" name="radio" checked/>
                            <i class="cell-radio-icon"></i>
                        </label>
                    </label>
                </#if>
                <#if list_index !=0 >
                    <label class="cell-item">
                        <span class="cell-left">${(list.address1)!}${(list.address2)!}</span>
                        <label class="cell-right">
                            <input type="radio" value="${(list.contactMechId)!}" name="radio" />
                            <i class="cell-radio-icon"></i>
                        </label>
                    </label>
                </#if>
            </#list>
        </div>
        <div class="m-button">
            <input type="button" class="btn-block btn-primary" id="J_Notify_apply" value="${uiLabel.apply}"/>
        </div>
    <#--<div class="m-cell">-->
    <#--<div class="cell-item">-->
    <#--<div class="cell-left">${uiLabel.AddressScope}</div>-->
    <#--<div class="cell-right cell-arrow">-->
    <#--<input type="text" class="cell-input" readonly id="J_Address" placeholder="${uiLabel.SelectAddress}">-->
    <#--</div>-->
    <#--</div>-->
    <#--</div>-->
    <#--<div class="m-celltitle">${uiLabel.SettingAbout}</div>-->
    <#--<div class="m-cell">-->
    <#--<label class="cell-item">-->
    <#--<span class="cell-left">${uiLabel.SetDefaultAddress}</span>-->
    <#--<label class="cell-right">-->
    <#--<input type="checkbox" class="m-switch" checked/>-->
    <#--</label>-->
    <#--</label>-->
    <#--</div>-->

    <!-- 订单的跳转在这里 -->
        <#--<button class="bac"-->
                <#--onclick="javascript:location.href = 'myOrder?tarjeta=${tarjeta}'">${uiLabel.ViewMyOrder}</button>-->



    <#--<span style="font-size:25px;color:darkgray;">${uiLabel.Over}</span>-->

    <#--<div class="m-button">-->
    <#--<input onclick="window.close();" type="button" class="btn-block btn-primary" id="J_Notify_SelectAddress" value="${uiLabel.Ok}"/>-->
    <#--</div>-->

    </#if>

    <#--<div class="m-celltitle">设置默认值</div>-->
    <#--<div class="m-cell">-->
    <#--<div class="cell-item">-->
    <#--<div class="cell-left">所在地区：</div>-->
    <#--<div class="cell-right cell-arrow">-->
    <#--<input type="text" class="cell-input" readonly id="J_Address2" value="新疆 乌鲁木齐市 天山区" placeholder="请选择收货地址">-->
    <#--</div>-->
    <#--</div>-->
    <#--</div>-->

    <#--<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>  <script src="../js/ydui.js"></script>-->
        <script src="http://static.ydcss.com/uploads/ydui/ydui.citys.js"></script>

        <script>
            $(
                    function () {

                        $("#noSetting").click(
                                function () {
                                    var flag = this.checked;
                                    if (flag) {
                                        $("#addressSettingBox").hide();
                                        $("#SetDefaultAddress").hide();
                                        $("#CleanOne").hide();
                                    } else {
                                        $("#addressSettingBox").show();
                                        $("#SetDefaultAddress").show();
                                        $("#CleanOne").show();
                                    }
                                }
                        );
                    }
            );
            /**
             * 默认调用
             */
            !function () {


                var $target = $('#J_Address');

                $target.citySelect();

                $target.on('click', function (event) {
                    event.stopPropagation();
                    $target.citySelect('open');
                });

                $target.on('done.ydui.cityselect', function (ret) {
//                alert(ret.provance + ' ' + ret.city + ' ' + ret.area);
                    $("#address1").val(ret.provance + ' ' + ret.city + ' ' + ret.area);
                    $(this).val(ret.provance + ' ' + ret.city + ' ' + ret.area);
                });
            }();
            //        /**
            //         * 设置默认值
            //         */
            //        !function () {
            //            var $target = $('#J_Address2');
            //
            //            $target.citySelect({
            //                provance: '新疆',
            //                city: '乌鲁木齐市',
            //                area: '天山区'
            //            });
            //
            //            $target.on('click', function (event) {
            //                event.stopPropagation();
            //                $target.citySelect('open');
            //            });
            //
            //            $target.on('done.ydui.cityselect', function (ret) {
            //                $(this).val(ret.provance + ' ' + ret.city + ' ' + ret.area);
            //            });
            //        }();
        </script>

        <script>
            !function (win, $) {
                var dialog = win.YDUI.dialog;
                $('#J_Notify_apply').on('click', function () {

                    var val_payPlatform = $('input[name="radio"]:checked ').val();
                    alert(val_payPlatform);
                    <#--if (flag) {-->
                        <#--dialog.notify('${uiLabel.SettingSuccess}', 2000, function () {-->
                            <#--$("#J_Notify").attr({"disabled": "disabled"});-->
                            <#--$("#createPersonPartyPostalAddressForm").submit();-->
                        <#--});-->
                    <#--} else {-->
                        <#--alert("请填写完整收货地址...");-->
                    <#--}-->

                });


                // 顶部提示框
                $('#J_Notify').on('click', function () {

                    var address1 = $("#address1").val();
                    var address2 = $("#address2").val();
                    var flag = true;
                    if (address1 == null || address1 === "") {
                        flag = false;
                    }
                    if (address2 == null || address2 === "") {
                        flag = false;
                    }

                    if (flag) {
                        dialog.notify('${uiLabel.SettingSuccess}', 2000, function () {
                            $("#J_Notify").attr({"disabled": "disabled"});
                            $("#createPersonPartyPostalAddressForm").submit();
                        });
                    } else {
                        alert("请填写完整收货地址...");
                    }
                });
            }(window, jQuery);
        </script>
    <#--<img style="width:80%;height:80%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg"/>-->
    </section>