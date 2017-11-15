
    <section class="g-flexview">

        <#--<header class="m-navbar">-->
            <#--<a href="../index.html" class="navbar-item"><i class="back-ico"></i></a>-->
            <#--<div class="navbar-center"><span class="navbar-title">CitySelect</span></div>-->
        <#--</header>-->

        <section class="g-scrollview">

            <aside class="demo-tip">
                <p style="font-size:25px;color:#4CD864;">${uiLabel.CreateOrderSuccess}</p>
            </aside>
            <#if !addressInfo?has_content>
            <div class="m-celltitle">${uiLabel.SetAddress}</div>
            <div class="m-cell">
                <div class="cell-item">
                    <div class="cell-left">${uiLabel.AddressScope}</div>
                    <div class="cell-right cell-arrow">
                        <input type="text" class="cell-input" readonly id="J_Address" placeholder="${uiLabel.SelectAddress}">
                    </div>
                </div>
            </div>
    <div class="m-celltitle">${uiLabel.AddressDetail}</div>
    <div class="m-cell">
        <div class="cell-item">
            <div class="cell-right">
                <textarea class="cell-textarea" placeholder="${uiLabel.AddressDetailDemo}"></textarea>
            </div>
        </div>
    </div>
                <div class="m-celltitle">${uiLabel.SettingAbout}</div>
                <div class="m-cell">
                    <label class="cell-item">
                        <span class="cell-left">${uiLabel.SetDefaultAddress}</span>
                        <label class="cell-right">
                            <input type="checkbox" class="m-switch" checked/>

                        </label>
                    </label>
                </div>
                <div class="m-cell">
                    <label class="cell-item">
                        <span class="cell-left">${uiLabel.NoFuckSetting}</span>
                        <label class="cell-right">
                            <input type="checkbox" class="m-switch"/>

                        </label>
                    </label>
                </div>
</#if>
        <#if addressInfo?has_content>
            <div class="m-celltitle">${uiLabel.CheckAddress}</div>
            <div class="m-cell">
                <div class="cell-item">
                    <div class="cell-left">${uiLabel.AddressScope}</div>
                    <div class="cell-right cell-arrow">
                        <input type="text" class="cell-input" readonly id="J_Address" placeholder="${uiLabel.SelectAddress}">
                    </div>
                </div>
            </div>
            <div class="m-celltitle">${uiLabel.SettingAbout}</div>
            <div class="m-cell">
                <label class="cell-item">
                    <span class="cell-left">${uiLabel.SetDefaultAddress}</span>
                    <label class="cell-right">
                        <input type="checkbox" class="m-switch" checked/>
                    </label>
                </label>
            </div>


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
            <div class="m-button">

                <#--<input type="button" class="btn-block btn-primary" id="J_Confirm" value="Confirm"/>-->
                <#--<input type="button" class="btn-block btn-primary" id="J_ConfirmCustom" value="Confirm->Custom"/>-->
                <#--<input type="button" class="btn-block btn-hollow" id="J_Alert" value="Alert"/>-->
                <#--<input type="button" class="btn-block btn-danger" id="J_ToastS" value="Toast->Success"/>-->
                <#--<input type="button" class="btn-block btn-danger" id="J_ToastE" value="Toast->Error"/>-->
                <#--<input type="button" class="btn-block btn-danger" id="J_ToastN" value="Toast->None"/>-->
                <#--<input type="button" class="btn-block btn-hollow" id="J_Loading" value="Loading"/>-->
                <input type="button" class="btn-block btn-primary" id="J_Notify" value="${uiLabel.Setting}"/>

            </div>
    <#--<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>  <script src="../js/ydui.js"></script>-->
    <script src="http://static.ydcss.com/uploads/ydui/ydui.citys.js"></script>

    <script>
        $(
                function(win, $){


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


                    // 顶部提示框
                    $('#J_Notify').on('click', function () {
                        dialog.notify('${uiLabel.SettingSuccess}', 5000, function(){
                            console.log('我走咯！');
                        });
                    });
                }(window, jQuery);
            </script>
    <#--<img style="width:80%;height:80%;" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saomaguanzhu.jpg"/>-->
</section>